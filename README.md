# WeatherApp

A small Android weather client written in 2017 as a learning project and brought back to a
modern toolchain in 2026. It shows the current weather and a five-day forecast for the device
location or for a city picked through Google Places autocomplete, signs the user in with
Google and can share the current weather through the system share sheet.

Data comes from the [OpenWeatherMap](https://openweathermap.org/api) API; city search uses the
[Places SDK for Android](https://developers.google.com/maps/documentation/places/android-sdk).

## Stack

| Area | Library |
| --- | --- |
| Build | Gradle 9.8, Android Gradle Plugin 9.4, Java 17, compileSdk 37, minSdk 24 |
| UI | AndroidX AppCompat, RecyclerView, Material Components, ViewBinding |
| Network | Retrofit 3, OkHttp 5, Gson |
| Images | Glide 5 |
| Location and places | play-services-location, Places SDK 6 |
| Sign-in | Credential Manager 1.6 + Sign in with Google (googleid 1.2) |
| Architecture | MVP: `screen/<feature>/{Contract,Presenter,Fragment}` over a repository facade |

## Setup

API keys are not committed. Put them into `local.properties` in the project root (the file is
git-ignored; Android Studio creates it with `sdk.dir`):

```properties
WEATHER_API_KEY=<OpenWeatherMap API key>
PLACES_API_KEY=<Google Cloud API key with the Places API (New) enabled>
GOOGLE_SERVER_CLIENT_ID=<OAuth 2.0 client id of type "Web application" from the same Google Cloud project>
```

Sign in with Google also needs an OAuth client of type "Android" in that project with this
app's package name `com.example.dmitro.weatherapp` and the SHA-1 of the signing key; for a debug
build that is the key in `~/.android/debug.keystore`:

```sh
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android | grep SHA1
```

Missing keys do not break the build: the keys compile in as empty strings, and the matching
feature fails at runtime instead (Places autocomplete shows the failure dialog, the sign-in
screen hides the Google button and offers a guest entry).

```sh
./gradlew assembleDebug           # app/build/outputs/apk/debug/app-debug.apk
./gradlew lintDebug testDebugUnitTest
```

## Project layout

```
app/src/main/java/com/example/dmitro/weatherapp
  data/model        Gson models for OpenWeatherMap and Places web responses
  data/repository   WeatherDataSource + WeatherRepositoryManager (remote + in-memory cache)
  service           Retrofit factories and interceptors, Google sign-in, share sheet, loaders
  screen            one package per screen: Contract, Presenter, Fragment/Activity, adapters
  utils             MyUtil (date grouping, blur), ImageLoader (Glide), callbacks, Injection
```

## Known limitations

- The current-weather and forecast child fragments still read the repository cache through
  `AsyncTaskLoader`; the loaders are deprecated and only re-read an in-memory value.
- `MyUtil.blur` uses `android.renderscript`, deprecated since API 31 and running on the CPU
  fallback on new devices.
- `CityChoiceFragment` (Places web API autocomplete through Retrofit) is not reachable from
  the navigation; the toolbar location button uses the Places SDK widget instead.
- Forecast days are grouped by weekday name, so a five-day window spanning the same weekday
  twice would merge them.
- No unit tests beyond the template; presenters depend on the `WeatherApp` singleton.
