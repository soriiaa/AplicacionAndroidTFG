# InvestLearn 📈

A real-time stock market simulator for Android. Learn to invest using live market data — with zero financial risk.

> Built solo in Kotlin as a final degree project at U-Tad, Madrid.

---

## What it does

InvestLearn gives users a realistic trading experience with real market data and virtual money. The goal: make financial education accessible to everyone, regardless of prior knowledge.

- Browse and search stocks from global markets
- Buy and sell shares using a virtual €5,000 portfolio
- Track your portfolio performance over time
- Stay informed with a live financial news feed
- Multi-currency support with real-time exchange rates

---

## Screenshots

![InvestLearn App Overview](AplicacionCompleta.png)

---

## Architecture

Built following **MVVM (Model-View-ViewModel)** with a clean separation of concerns:

```
├── Model       — Data layer: API clients, Firebase repositories, domain models
├── ViewModel   — Business logic, state management, reactive data flows
└── View        — Jetpack Compose UI, navigation, reusable components
```

Dependency injection handled via **Hilt**, ensuring modular and testable code across all layers.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Hilt (DI) |
| Auth & DB | Firebase Authentication + Firestore |
| Market Data | Finnhub.io API + Polygon.io API |
| Currency | ExchangeRate API |
| Charts | MPAndroidChart |
| Networking | Retrofit + Gson |
| Image Loading | Coil |
| Build | Gradle |

---

## Key Features

**Authentication**
- Email/password registration with email verification
- Password recovery via email
- Persistent session management with Firebase tokens

**Home Screen**
- Portfolio stats: balance, profit/loss, number of trades
- Watchlist of favourited stocks with live prices

**Market Explorer**
- Global stock search powered by Finnhub
- Featured stocks updated on every visit
- Favourite/unfavourite any stock

**Stock Detail**
- Candlestick chart with Daily / Weekly / Monthly views
- Real-time price and % change
- Company info panel with external link

**Trading**
- Buy screen: select quantity, see estimated cost in both USD and EUR
- Sell screen: view each individual position with P&L, select which lots to sell
- Prevents selling if no positions held

**Profile & Settings**
- Full trade history
- Current positions with live P&L
- Edit name, nickname
- Change display currency (converts portfolio balance automatically)
- Change password / delete account

---

## Project Structure

```
app/
├── data/
│   ├── api/          # Retrofit interfaces for Finnhub, Polygon, ExchangeRate
│   ├── firebase/     # Firestore repositories (users, transactions, favourites)
│   └── model/        # Data classes
├── ui/
│   ├── screens/      # Composable screens (Home, Explorer, News, Profile, Stock, Buy, Sell)
│   ├── components/   # Reusable UI components
│   └── theme/        # Color palette, typography (Inter font)
├── viewmodel/        # ViewModels per screen
└── di/               # Hilt modules
```

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 26+
- A Firebase project with Authentication and Firestore enabled
- API keys for [Finnhub](https://finnhub.io), [Polygon](https://polygon.io) and [ExchangeRate](https://exchangerate.host)

### Setup

```bash
git clone https://github.com/soriiaa/AplicacionAndroidTFG.git
cd AplicacionAndroidTFG
```

1. Add your `google-services.json` from Firebase to `/app`
2. Create a `local.properties` file in the root with your API keys:

```properties
FINNHUB_API_KEY=your_key_here
POLYGON_API_KEY=your_key_here
EXCHANGERATE_API_KEY=your_key_here
```

3. Build and run on an emulator or physical Android device

---

## Design

Minimalist dark UI built around a blue/navy palette (`#00263B` → `#3DB7FF`), using the **Inter** typeface throughout. Designed first in Figma, then implemented in Jetpack Compose with fully reusable components.

---

## Future Work

- Cryptocurrency support
- Social features — compare portfolios with friends
- Auto-translated news feed (Google Translate API)
- Full dark/light mode toggle
- Localization (EN/ES)

---

## Author

**Alejandro Soria** — [@soriiaa](https://github.com/soriiaa)

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/alejandrosoriaalcaraz/)
[![Email](https://img.shields.io/badge/Email-D14836?style=flat&logo=gmail&logoColor=white)](mailto:alex.soriaalcaraz@gmail.com)

---

*Final Degree Project — DAM (Multiplatform Application Development), U-Tad Madrid, June 2025*
