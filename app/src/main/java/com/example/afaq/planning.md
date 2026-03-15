
## 4. fix permission bugs 

1. 
4. test whole app 


# Tasks for Today [Thursday]
1. apply test cases [unit tests] 
3. study testing mechanism proccess
4. implement it 
5. 
# Today

1. Refactoring
```
1. Home ✅
2. Favourites ✅
3. Alarms ✅
4. Settings ✅
```

2. Fix 
```
1. user can add without select any location ✅
2. language changes from settings not listening at first one ✅
3. refactor service 
4. fix location permission bugs ✅
5. change icons to custom one
6. make favourite card clickable to display ✅
7. make digits arabic  
```








----

- **Core Weather Features**:
    - Implement `HomeLocalDataSource` and `WeatherDataStore` to cache weather and forecast data uniquely by latitude/longitude.
    - Add `WindConverter` and integrate `TempConverter` and `WindConverter` across UI components.
    - Introduce `LocationMapBottomSheet` using OpenStreetMap (osmdroid) for manual location selection.

- **Settings & Localization**:
    - Expand `SettingsViewModel` to manage user-defined coordinates and localized unit settings.
    - Provide full Arabic and English string translations for all UI labels, units, and dialogs.
    - Implement `fetchGpsLocation` helper for reactive GPS updates.

- **Alarms & Favorites**:
    - Refactor Alert/Alarm system: rename "Alert" to "Alarm" for consistency and implement sticky notifications for alarms.
    - Add `DeleteFavouriteDialog` with Lottie animations for a better user experience when removing items.
    - Update `FavouriteViewModel` to fetch remote weather data before saving a new favorite location.

- **Refactoring & UI**:
    - Restructure project packages (data, network, ui) for better separation of concerns.
    - Update `AfaqThemeColors` to support a more robust Dark Theme.
    - Clean up `HomeScreen` to reactively load data based on saved coordinates in `AppSettings`.
    - Improve navigation by removing hardcoded coordinates from `HomeRoute`.
- **Splash Screen**: Refactor location and GPS permission handling using `LifecycleEventObserver` to better manage app resumes.
  - **Splash Screen**: Remove "Skip" and "Use Default" options from permission/GPS dialogs to ensure mandatory location setup for first-time users.
  - **Favorites**: Prevent adding a favorite location without selecting a point on the map first.
  - **Favorites**: Add a `Snackbar` to notify users when attempting to save without a selected location.
  - **Resources**: Add string resources for location selection prompts and offline error messages in English and Arabic.
  - **Documentation**: Update `planning.md` to reflect completed tasks and new UI/UX bug fixes.

- Add `FavouriteDetailsScreen` to display detailed weather, hourly, and 5-day forecasts for saved locations
- Define `FavouriteDetailsRoute` with latitude and longitude parameters in `Routes`
- Configure navigation logic in `AppNavigation` to handle transitions from the favourites list to city details
- Update `FavouritesScreen` and `FavouriteCityCard` to support click events for navigating to specific city details
- Mark language setting synchronization as completed in planning documentation