
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
5. change icons to custom one ✅
6. make favourite card clickable to display ✅
7. make digits arabic  ✅
8. change alarm and notification badge ✅
9. test offline mode 
10. add change theme ✅
11. add testing cases  
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

- Add `localizeDigits` helper functions to handle Arabic numeral conversion based on app locale
- Integrate localization into `TempConverter`, `WindConverter`, and date/time formatting utilities
- Apply digit localization across Home and Favourite screens, including weather cards and detail grids
- Ensure consistent locale-aware geocoding in location helpers

- Refactor `WeatherAlertWorker` and `AlarmReciever` to fetch current weather data (temperature, description, city) from `HomeRemoteDataSource` before displaying notifications or alarms.
- Update `AlertDao`, `AlertLocalDataSource`, and `AlertRepo` to return the row ID upon alert insertion.
- Enhance `AlertViewModel` to manage the scheduling and cancellation of alarms and work requests directly using `AndroidAlarmManager` and `WorkManagerScheduler`.
- Improve `NotificationServiceImpl` with better icons, styles, and full-screen intent support for alarms.
- Update `planning.md` to reflect progress on localized digits and alert system improvements.
- Refactor `AlarmsScreen` to use the updated `AlertViewModel` for handling alert deletions and permissions.
  Implement custom weather icons, update alarm badge UI, and add testing dependencies

- Replace OpenWeatherMap network icons with custom local drawables across all weather cards
- Implement `getWeatherIcon` helper to map temperature and conditions to local resources
- Update `AlarmCard` badge styling with dynamic colors for light and dark themes
- Add JUnit, Robolectric, MockK, and Compose testing dependencies to `build.gradle.kts`
- Update `planning.md` to reflect progress on icon and UI tasks
  Implement theme selection support and dynamic app theming

- Add theme preference (Light, Dark, System) to `SettingsRepo` and `SettingsViewModel`
- Implement theme selection UI in `SettingsScreen` with localized strings
- Update `MainActivity` to collect and apply the selected theme
- Refactor `AfaqTheme` and `AfaqThemeColors` to use `CompositionLocalProvider` for theme-aware color management
- Update `planning.md` with recent UI and testing progress
  Implement offline mode indicators and connectivity handling

- Add connectivity checks to `HomeScreen`, `SettingsScreen`, and `FavouritesScreen`
- Display "No internet" warning and "Showing cached data" status when offline
- Implement `AlertDialog` to restrict map access and adding favorites without a connection
- Add localized strings for offline error messages and status indicators
- Update `planning.md` to reflect theme and offline mode progress
  Implement offline mode handling and connectivity status observation

- Add `NetworkObserver` and `NetworkViewModel` to monitor and provide real-time network status
- Integrate `NetworkViewModel` into `MainActivity`, `AppNavigation`, and `BottomNavBar`
- Update `BottomNavBar` to adjust transparency when offline
- Implement offline indicators and localized status messages in `HomeScreen` and `SettingsScreen`
- Update `planning.md` to reflect progress on connectivity handling and cached data display