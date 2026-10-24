# WeatherApp

A small Android weather client. It shows the current weather and a five-day forecast for the
device location or for any city picked through Google Places autocomplete, signs the user in
with Google and shares the current weather through the system share sheet.

The app was written in September 2017 as a learning project (Java, MVP, Retrofit, the support
library) and brought back to a current toolchain in October 2026 without a rewrite: same
screens, same architecture, modern libraries. The history section below says what changed.

Weather data comes from the [OpenWeatherMap API](https://openweathermap.org/api); city search
and coordinates come from the
[Places SDK for Android](https://developers.google.com/maps/documentation/places/android-sdk);
sign-in goes through
[Credential Manager](https://developer.android.com/identity/sign-in/credential-manager-siwg).

## Features

- **Sign-in screen.** Sign in with Google through Credential Manager, or continue as a guest.
  The profile (name, email, avatar) is kept between launches and shown in the navigation drawer;
  Sign out clears it.
- **Current weather.** Temperature, min/max range, humidity and wind for the last known device
  location, with the OpenWeatherMap condition icon, over a blurred city background.
- **Five-day forecast.** The 3-hour forecast grouped by day, one row per day with its icon and
  temperature range; tapping a day opens the list of 3-hour entries for it.
- **Another city.** The toolbar pin opens the Places autocomplete widget filtered to cities; the
  chosen place is resolved to coordinates and both weather requests run again for it.
- **Share.** The drawer item sends "City: temperature, condition" as text to any app through
  the system share sheet.
- **Failure handling.** A failed request shows a dialog with a retry; a missing location
  permission or a null last location ends in the same dialog instead of a crash.

## Requirements

- Android Studio with the Android 37 platform (compileSdk 37); the Gradle wrapper fetches
  Gradle 9.8.2 itself.
- JDK 17 or newer to run Gradle (the project compiles with Java 17 language level).
- A device or emulator on Android 7.0 (API 24) or newer with Google Play services; sign-in and
  Places need Play services, the weather screens do not.

## Setup

API keys are not committed. Put them into `local.properties` in the project root (the file is
git-ignored; Android Studio creates it with `sdk.dir`):

```properties
WEATHER_API_KEY=<OpenWeatherMap API key>
PLACES_API_KEY=<Google Cloud API key with the Places API (New) enabled>
GOOGLE_SERVER_CLIENT_ID=<OAuth 2.0 client id of type "Web application">
```

Each value becomes a `BuildConfig` constant at build time. A missing key does not break the
build; the matching feature degrades at runtime instead:

| Missing key | Behaviour |
| --- | --- |
| `WEATHER_API_KEY` | OpenWeatherMap answers 401, the failure dialog appears on the weather screen |
| `PLACES_API_KEY` | The Places SDK is not initialised; the toolbar pin shows the failure dialog |
| `GOOGLE_SERVER_CLIENT_ID` | The sign-in screen hides the Google button and offers only the guest entry |

### OpenWeatherMap

Create a free account at [openweathermap.org](https://home.openweathermap.org/users/sign_up)
and copy the default API key. New keys take a few minutes to activate; until then the API
answers 401 as if the key were missing.

### Google Cloud: Places and Sign in with Google

Both live in one Google Cloud project. The easiest way to get the OAuth clients right is to let
Firebase create them:

1. Create a Firebase project (or add Firebase to an existing Cloud project).
2. Add an Android app with package name `com.example.dmitro.weatherapp` and the SHA-1 of the
   signing key. For a debug build that is the default debug keystore:

   ```sh
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android | grep SHA1
   ```

3. In **Authentication > Sign-in method** enable **Google**. At this moment Google creates two
   OAuth clients in the Cloud project: an Android client bound to the package and SHA-1, and a
   web client. The web client id is the value for `GOOGLE_SERVER_CLIENT_ID`; it is printed by
   `firebase apps:sdkconfig ANDROID <app id>` as the `oauth_client` entry with `client_type: 3`.
4. In the Cloud console enable **Places API (New)** and create an API key for it; that is
   `PLACES_API_KEY`. Restrict it to the Android app with the same package name and SHA-1.

Every machine that builds a debug APK signs it with its own debug keystore, so each one needs
its SHA-1 added to the Android app in Firebase, otherwise sign-in fails with
`GetCredentialCancellationException: [16] Account reauth failed`.

The same can be done by hand in **APIs & Services > Credentials** (OAuth consent screen, a
Web application client, an Android client); the Firebase route only saves the clicks.

## Build and run

```sh
./gradlew assembleDebug                 # app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug                  # onto the connected device
./gradlew lintDebug testDebugUnitTest   # lint (0 errors expected) and JVM tests
./gradlew connectedDebugAndroidTest     # instrumented tests on the connected device
```

The release build type has no signing config and no shrinking; the app has never been published.

## Architecture

MVP over a repository facade, one package per screen.

```
AuthActivity ─── AuthPresenter ─── GoogleAuthService (Credential Manager)
     │                               UserSessionStore (SharedPreferences)
     ▼
NavigationActivity ── NavigationPresenter ── UserSessionStore
     │  drawer: current weather, share (WeatherShare), sign out
     ▼
WeatherDetailsFragment ── WeatherDetailsPresenter ── WeatherRepositoryManager
     │  toolbar: drawer button, city name, Places pin        │
     │  location permission, Places autocomplete + fetchPlace │ in-memory cache
     ├── WeatherCurrentDetailsFragment  (CurrentWeatherLoader) │
     └── WeatherListFragment            (CurrentWeatherManyDayLoader)
              │ tap on a day                                   ▼
              ▼                                   WeatherRemoteRepository
ForecastListActivity ── ForecastListPresenter        Retrofit: WeatherApiServer (OpenWeatherMap)
                                                     Retrofit: GeoService (Places web API, unused)
```

- **Contract.** Every screen has a `<Screen>Contract` with a `View` and a `Presenter`
  interface. The presenter gets the view in its constructor and calls `view.setPresenter(this)`;
  views are Fragments or Activities with ViewBinding.
- **Repository.** `WeatherDataSource` is the interface; `WeatherRepositoryManager` is a
  singleton facade that forwards to `WeatherRemoteRepository` (Retrofit) and keeps the last
  current-weather and forecast responses as an in-memory cache. `WeatherLocalRepository` is an
  empty placeholder for a persistent cache that was never written.
- **Callbacks.** Asynchronous results travel through `Action1<T>` (success and failure) and
  `Action0` (complete), the project's own two functional interfaces from before RxJava or
  coroutines were an option here.
- **Loaders.** The two child fragments of the weather screen read the repository cache through
  `AsyncTaskLoader` subclasses; the parent fragment pokes the loaders with `onContentChanged()`
  when the presenter has new data. This is the 2017 design kept as is.
- **Services.** `WeatherFactory` and `GeoFactory` build the Retrofit instances; the
  interceptors append the API keys as query parameters. `GoogleAuthService` wraps Credential
  Manager; `WeatherShare` builds the share intent; `ImageLoader` wraps Glide.
- **Composition.** `Injection.provideManager()` is the only wiring point; presenters call it
  directly. There is no DI framework.

## Project layout

```
app/src/main/java/com/example/dmitro/weatherapp
  WeatherApp                      Application: Places SDK initialisation
  data/model/weather              Gson models for OpenWeatherMap (current + 5-day)
  data/model/geo                  Gson models for the Places web API (autocomplete, details)
  data/model/user                 UserProfile shown in the drawer
  data/repository                 WeatherDataSource, WeatherRepositoryManager, remote/local
  data/repository/local           UserSessionStore
  service/weather_service         Retrofit factory, interceptor, cache loaders
  service/geo_service             Retrofit factory and interceptor for the Places web API
  service/auth                    GoogleAuthService
  service/share                   WeatherShare
  screen/authorization            sign-in screen
  screen/navigation               drawer host activity
  screen/weather                  current weather + forecast list (parent and two children)
  screen/forecast_list            3-hour entries of one day
  screen/select_city              Places web API autocomplete screen (not wired in)
  screen/failure                  retry dialog
  utils                           MyUtil (day grouping, blur), ImageLoader, Injection, callbacks
app/src/main/res/font             Calibri, set as the app font in the theme
```

## History

**2017.** Written in one week at the end of September as a first Android project: MVP after the
Google `android-architecture` samples, Retrofit 2 with Gson, ButterKnife, Calligraphy for the
font, Picasso, the Facebook SDK for login and sharing, PlacePicker from Play services, Jack
toolchain for lambdas, `targetSdk 19`.

**2026, step 1: toolchain.** Gradle 3.3 to 9.8, AGP 2.3 to 9.4, Java 17, compileSdk 37,
minSdk 24, support library to AndroidX, jcenter removed. Libraries without a future replaced by
their successors: ButterKnife by ViewBinding, Calligraphy by `res/font`, Picasso by Glide,
PlacePicker by the Places SDK autocomplete widget. Every API key moved out of the repository
into `local.properties`. Runtime permission for location, HTTP status checks in the callbacks,
`OnBackPressedDispatcher`.

**2026, step 2: sign-in.** The Facebook SDK removed; Sign in with Google through Credential
Manager, profile kept in SharedPreferences, sharing through the system share sheet. Two bugs
found on a device along the way: the weather screen hid its toolbar after a failed request, and
the failure dialog crashed when both requests failed in the same frame.

The keys committed in 2017 are still in the git history and have been or must be revoked; they
were never valid for anything but this app.

## Known limitations

- The two child fragments still go through `AsyncTaskLoader` (deprecated) to re-read an
  in-memory value the parent already holds.
- `MyUtil.blur` uses `android.renderscript`, deprecated since API 31 and running on the CPU
  fallback on current devices; `RenderEffect` would replace it on API 31+.
- `CityChoiceFragment` and the Places web API client behind it are not reachable from the
  navigation; the toolbar pin uses the Places SDK widget instead.
- Forecast entries are grouped by weekday name, so a window that contains the same weekday
  twice would merge the two days.
- `WeatherResponse` is both the API model and the UI model, and the forecast is passed between
  screens as `HashMap<String, ArrayList<WeatherResponse>>` through a `Serializable` extra.
- The `<fragment>` tags in `activity_weather.xml` predate `FragmentContainerView`.
- The only tests are the templates; presenters depend on the `WeatherApp` singleton and
  cannot be unit-tested without Android.
- English only, no dark theme, no tablet layout.

## Roadmap

In the order they pay off:

1. Replace the loaders with a direct call from the parent fragment to its children and delete
   `service/weather_service/weather_loader`.
2. Introduce a `DayForecast` domain model and a mapper from the API responses; stop passing
   maps through intents.
3. Pass dependencies through constructors instead of `Injection` and the `WeatherApp`
   singleton, then write presenter tests.
4. Blur with `RenderEffect`, with the current code as the fallback below API 31.
5. Either wire `CityChoiceFragment` into the navigation or delete it with `GeoService`.
