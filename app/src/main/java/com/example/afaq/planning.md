
## 1. ~Finish Home Screen with my design~
### 1.1 ~~add bottom navigation bar~~
### 1.2 ~REQUEST DATA OF FORECAST~
## 2. ~~Start Fav Screen~~

- ~~edit updated at home screen~~
- ~~pass real data for day details~~

-----
# Tasks for Today [Monday]
## 1. Display name of point on map [Done]
## 2. Give user ability to delete from fav [Done]
## 3. refactor viewmodel of Home Feature to be testable [Done]
## 4. fix permission bugs 
## 5. Display valid data at home "Day Details" [Done]
## 6. plan for Weather Alerts  [Done]
---- 
# Weather Alerts [Notification]
## 1. Create Notification Channel
## 2. Create Notification Service interface + class
## 3. Create BroadCast Reciever

# Weather Alerts [Alarm]
## 3. Create Alarm Item
## 1. Create Alarm Service interface + class
-> use Notification to display sticky notification with sound 


1. create extended worker class
2. define work request One,Peri
3. define work manager .getinstance
--- 
# Tasks for Today [Wednesday]
1. fix location permission bugs
2. make notification sticky for alarm 
3. Settings -> location and wind speed [Done] 
4. test whole app 
5. refactor & restucture code
6. redesign dark theme [Done]

# Tasks for Today [Thursday]
1. apply test cases [unit tests] 
------
Today
1. refactor & restucture code ✅✅
1. test whole app ✅
3. study testing mechanism proccess
4. implement it 
5. add offline checks "Dialogs" [Done] 

-----
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
1. user can add without select any location 
2. language changes from settings not listening at first one 
3. refactor service 
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
