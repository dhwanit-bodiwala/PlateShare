# 🍽️ PlateShare

**PlateShare** is an Android application that connects food donors with receivers (NGOs, charities, and individuals in need) to reduce food wastage and fight hunger at a community level. The platform supports role-based access for **Donors** and **Receivers**, enabling a two-way marketplace of food donations and food requests — complete with atomic claim-locking to prevent race conditions, and a built-in directory of nearby food charity centers.

---

## 📱 Overview

| | |
|---|---|
| **Platform** | Android (Java) |
| **Min SDK** | API 24 (Android Nougat) |
| **Database** | SQLite (local, offline-first) |
| **Architecture** | Activity + Fragment based, role-driven UI |
| **Status** | Feature-complete |

PlateShare was built as part of a diploma-level skill-based training program, with an emphasis on relational database design, role-based access control (RBAC), and real-world transactional logic (claim-locking, atomic status transitions).

---

## ✨ Core Features

### 🔐 Authentication & Role-Based Access
- Secure signup/login with email + password validation
- Three-tier role system: **Donor**, **Receiver**, and Guest browsing
- Donor sub-types: **Individual** or **Organization** (with dedicated profile fields for organization name & contact person)
- Role-aware Home screen — UI elements and navigation tabs adapt dynamically based on the logged-in user's role

### 🍲 Donation Lifecycle
- Donors can list surplus food with quantity, food type, expiry date, and allergy information
- Receivers can browse all **available** donations in real time
- Full donation detail view before committing to a claim
- **Atomic claim-locking** — guarantees that once a donation is claimed, no other receiver can claim the same donation, even under concurrent access
- Donation status lifecycle: `Available → Claimed → Completed`
- **Delivery confirmation** — receiver confirms pickup before a donation is marked complete; declining a delivery releases the claim back to `Available` for other receivers

### 🙋 Food Requests
- Receivers can post specific food requests (title, description, quantity needed, pickup location)
- Donors can browse all **open** requests and accept the ones they can fulfill
- Same atomic locking mechanism applied to request acceptance — no double-accepting
- Request status lifecycle: `Open → Accepted`
- Dedicated "My Requests" view for receivers to track the status of their own requests

### ⭐ Rating System
- After a donation is confirmed delivered, the receiver rates the donor on **reliability** and **communication**
- An overall score is calculated dynamically from these two metrics — no redundant data stored
- Average rating surfaces directly on the donor's profile

### 📊 Profile Dashboard
- Every user's profile includes a glanceable **stat strip** summarizing their activity, tailored to their role:
  - **Donors** see Total Donations, Completed donations, and Average Rating
  - **Receivers** see Total Claimed, Requests Posted, and Requests Fulfilled
- Stats are calculated live from the database on every profile load — no caching, always accurate

### 🏢 Nearby Food Centers
- Browsable directory of partner food banks and charity centers
- Seeded center data including address, contact info, and current stock needs

### 👤 Profile Management
- View and edit personal profile (name, phone, address)
- Partial-update support — leave a field blank to keep its current value unchanged
- Secure password change flow with validation, including a full Forgot Password recovery flow

---

## 🗄️ Database Schema

PlateShare uses a normalized relational schema built on SQLite, with foreign-key relationships connecting users to their donations, requests, and donor sub-profiles.

| Table | Purpose |
|---|---|
| `users` | Core user accounts — credentials, contact info, and role |
| `donor_profile` | Extended profile data for Donor accounts (Individual / Organization) |
| `donations` | Food donation listings with status and claim tracking |
| `requests` | Food requests posted by receivers, with acceptance tracking |
| `ratings` | Post-delivery ratings given by receivers to donors |
| `centers` | Seeded directory of partner food charity centers |

### Key Design Decisions
- **Atomic locking via conditional `UPDATE`** — claim/accept actions use a `WHERE status = 'Available'` (or `'Open'`) clause directly inside the `UPDATE` statement, ensuring that simultaneous claim attempts cannot both succeed. This is the same principle used in production-grade inventory and booking systems.
- **Separate `donor_profile` table** rather than extending `users` directly — keeps the core user table lean and avoids null-heavy columns for non-donor accounts.
- **Status-driven workflows** — both donations and requests follow an explicit state machine (`Available/Open → Claimed/Accepted → Completed`), making the system's business logic transparent and auditable.
- **Computed metrics over stored aggregates** — overall donor rating and profile stats are calculated live via SQL aggregate queries (`COUNT`, `AVG`) rather than stored and synced separately, eliminating an entire category of data-consistency bugs.

---

## 🎨 Design System — "Warm Brutalist"

PlateShare uses a custom-built visual identity rather than default Material components, combining:

- **Warm Market palette** — deep green, saffron, and off-white tones evoking a community food market
- **Brutalist typography** — bold uppercase headers, monospace labels, and sharp-edged components for a confident, no-nonsense interface

| Color | Hex | Usage |
|---|---|---|
| Deep Green | `#2D6A4F` | Primary actions, top bars |
| Saffron | `#F4A261` | Accents, highlights |
| Off-White | `#FAFAF7` | App background |

---

## 🏗️ Architecture

```
SplashActivity
      ↓
LoginActivity ──→ SignupActivity (Role + Donor Type selection)
      ↓               ↓
      ├──→ ForgotPasswordActivity → ChangePasswordActivity
      ↓
HomeActivity (role-aware)
  ├── Bottom Nav: Nearby Centers
  ├── Bottom Nav: My Donations (Donor) / Browse Donations (Receiver)
  ├── Bottom Nav: Browse Requests (Donor) / My Requests (Receiver)
  ├── + Donate Food (Donor only) → DonateFoodActivity
  ├── + Request Food (Receiver only) → CreateRequestActivity
  └── Profile (with live stat dashboard)
        ├── Edit Profile
        ├── Change Password
        └── Logout

Donation flow:
  BrowseDonations → DonationDetails → Claim → DeliveryConfirmation → RatingActivity

Request flow:
  BrowseRequests → RequestDetails → Accept
```

- **Activities** handle full-screen, navigational flows (auth, detail views, forms, confirmations)
- **Fragments** handle tab-based content within `HomeActivity`, swapped dynamically based on the active bottom navigation tab and the user's role
- **Adapters** bind SQLite `Cursor` data directly to `RecyclerView` rows, including filtered and JOIN-based queries for cross-table displays (e.g. showing a donor's name alongside their donation)

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest stable release recommended)
- Android SDK 24+
- A physical device or emulator running Android 7.0+

### Setup
```bash
git clone https://github.com/dhwanit-bodiwala/PlateShare.git
```
1. Open the project in Android Studio
2. Let Gradle sync complete
3. Run on an emulator or physical device

No external API keys or backend configuration required — PlateShare runs entirely offline using local SQLite storage.

> **Note:** If reinstalling after a schema change during development, uninstall the existing app from the device/emulator first to force a fresh database, or bump `DATABASE_VERSION` in `DatabaseHelper.java`.

---

## 🛣️ Roadmap

- [x] Role-based authentication (Donor / Receiver)
- [x] Donor sub-types (Individual / Organization)
- [x] Donation lifecycle with atomic claim-locking
- [x] Food request lifecycle with atomic accept-locking
- [x] Delivery confirmation flow
- [x] Rating system (Receiver → Donor)
- [x] Profile activity dashboard

---

## 🧰 Tech Stack

- **Language:** Java
- **UI:** Android XML layouts, custom drawables, RecyclerView
- **Database:** SQLite via `SQLiteOpenHelper`
- **Architecture:** Activity/Fragment-based navigation with `BottomNavigationView`

---

## 👤 Author

**Dhwanit Bodiwala**
GitHub: [@dhwanit-bodiwala](https://github.com/dhwanit-bodiwala)

---

## 📄 License

This project is currently unlicensed. All rights reserved by the author unless otherwise stated.
