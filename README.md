# RecipeFinder

A modern Android recipe app built with **Kotlin** and **Jetpack Compose**. Browse recipes by category, search for any dish, open full recipes with ingredients and step-by-step instructions, and save your favorites.

Data comes from the free [TheMealDB](https://www.themealdb.com/api.php) API.

## Video Tutorial

Watch the full tutorial and code walkthrough on YouTube (Bukhari Codes): [Build a Recipe Finder Android App | Kotlin + Jetpack Compose](https://youtu.be/jT_exr84dAQ)

## Features
- Browse recipes by category with pill-style filters
- Search any dish by name
- Detail screen with full-screen photo, ingredients (with images), numbered instructions and a video tutorial link
- Favorites saved on the device, available between app launches
- Loading skeletons, error states with retry, and empty states
- Light and dark theme, edge-to-edge layout, bottom navigation

## Screenshots
| Home | Detail | Favorited |
|------|--------|-----------|
| <img src="WhatsApp%20Image%202026-11-11%20at%2001.34.20.jpeg" width="250"> | <img src="WhatsApp%20Image%202026-11-11%20at%2001.34.20%20(1).jpeg" width="250"> | <img src="WhatsApp%20Image%202026-11-11%20at%2001.34.20%20(2).jpeg" width="250"> |

## Tech stack
- Kotlin, Jetpack Compose, Material 3
- MVVM with `ViewModel` and `StateFlow`
- Retrofit + Gson for networking
- Coil for image loading
- Navigation Compose
- Kotlin Coroutines

## Architecture
```
app/src/main/java/com/choubapp/recipefinder
├── data/          # Retrofit API, DTOs, repository, favorites storage
├── model/         # UI models
└── ui/
    ├── home/      # Search + category list
    ├── detail/    # Recipe details
    ├── favorites/ # Saved recipes
    ├── components/# Reusable UI (MealCard, skeleton, buttons)
    ├── navigation/# NavHost + bottom bar
    └── theme/     # Colors, typography, shapes
```

## Run it
1. Clone the repo and open it in Android Studio.
2. Let Gradle sync finish.
3. Run on an emulator or a phone (Android 7.0+, minSdk 24).

No API key is needed.

## Roadmap
- [ ] Room database for offline favorites
- [ ] Random "Surprise me" recipe
- [ ] Unit tests for ViewModels

## Author
Syed Wajid Ali Bukhari, Android developer.
