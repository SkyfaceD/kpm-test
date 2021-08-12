## Download

Debug|Release
---|---
[with leakcanary](https://github.com/SkyfaceD/kpm-test/tree/main/apks/debug)|[with minify and shrink][https://github.com/SkyfaceD/kpm-test/tree/main/apks/release]

You can install both versions on one device.

## Stack

- [Kotlin](https://kotlinlang.org/) - main language
- [Coil](https://github.com/coil-kt/coil) - an image loading library for Android backed by Kotlin
  Coroutines
- [Timber](https://github.com/JakeWharton/timber) - logger with a small, extensible API which
  provides utility on top of Android's normal `Log` class
- [LeakCanary](https://github.com/square/leakcanary) - a memory leak detection library for Android
- [Material Components](https://material.io/) - an adaptable system of guidelines, components, and
  tools that support the best practices of user interface design
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) - consists of a compiler
  plugin, that generates visitor code for serializable classes, runtime library with core
  serialization API and support libraries with various serialization formats
- [Koin](https://insert-koin.io/) - a smart Kotlin injection library to keep you focused on your
  app, not on your tools
- [Startup](https://developer.android.com/topic/libraries/app-startup) - provides a straightforward,
  performant way to initialize components at application startup
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
  + [Flow](https://developer.android.com/kotlin/flow) - asynchronous world
- [Retrofit2](https://github.com/square/retrofit) + [OkHttp3](https://github.com/square/okhttp) - A
  type-safe HTTP client for Android and Java
- [Ksoap2](https://simpligility.github.io/ksoap2-android/) - a lightweight and efficient SOAP client
  library for the Android platform
- [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - perform actions
  in response to a change in the lifecycle status of another component, such as activities and
  fragments
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - class is
  designed to store and manage UI-related data in a lifecycle conscious way
- [Navigation Components](https://developer.android.com/jetpack/androidx/releases/navigation) -
  framework for navigating between 'destinations' within an Android application that provides a
  consistent API whether destinations are implemented as Fragments, Activities, or other components

## Screenshots

![Sample!](/screenshots/sample.png "Sample")

## Known issues

- Multi back stack not work as wish. Gonna try to come up with some hack or just replace navigation
  components with [Cicerone](https://github.com/terrakok/Cicerone)
  / [Modo](https://github.com/terrakok/Modo);
- DatePicker may cause memory leak.

## Try it out

### Requirements

- Android SDK 21+
- Gradle 7.0.2+
- Java 11+

### Launch

- Clone repository (wrapper included):
    - via ssh `git@github.com:SkyfaceD/kpm-test.git`;
    - via https `https://github.com/SkyfaceD/kpm-test.git`;
- Build - `gradlew clean build`
- Run

## License

(License)[https://github.com/SkyfaceD/kpm-test/blob/main/LICENSE] can found in root directory of
repository 