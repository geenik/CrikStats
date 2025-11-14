# CrikStats – Dynamic Feature Module Setup & Testing Guide

This README explains how **Dynamic Feature Modules** are configured in CrikStats, how **Hilt dependencies are shared across modules**, and how to **test on‑demand module downloads using Bundletool**.

---

## 📁 Project Structure
```
CrikStats/
 ├── app/                 # Base app module
 ├── feature_player/      # Dynamic feature module
 ├── bundletool.jar       # Tool for local-testing dynamic delivery
 └── settings.gradle
```

---

# 1️⃣ Dynamic Feature Setup

## ✔️ settings.gradle
The dynamic feature module is registered here:

```kotlin
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CrikStats"
include(":app")
include(":feature_player")
```

### ❗ Inside *app* module’s `build.gradle`
Register the dynamic feature:
```kotlin
dynamicFeatures += setOf(":feature_player")
```

Include the Play Feature Delivery dependencies:
```kotlin
implementation("com.google.android.play:feature-delivery:2.1.0")
implementation("com.google.android.play:feature-delivery-ktx:2.1.0")
```

### ❗ Inside *feature_player* module’s `build.gradle`
```kotlin
dependencies {
    implementation(project(":app"))
}
```

This makes the feature module depend on the base app for shared components.

---

# 2️⃣ How Hilt Dependencies Are Shared Across Modules

## ✔️ App-wide Hilt Module
`NetworkModule` provides shared instances for Retrofit and Repository inside the **SingletonComponent**, meaning they are available app‑wide (including dynamic features).

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    fun provideRepository(api: ApiService): GetDataRepo = GetDataRepo(api)
}
```

## ✔️ Accessing Hilt dependencies from a Dynamic Feature
Because dynamic modules are loaded at runtime, they cannot directly use `@AndroidEntryPoint`. Instead, you use **EntryPoints**:

```kotlin
@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    fun getRepo(): GetDataRepo
}
```

Usage inside dynamic module:
```kotlin
val entryPoint = EntryPointAccessors.fromApplication(
    applicationContext,
    AppEntryPoint::class.java
)

val repo = entryPoint.getRepo()
val playerViewModel = PlayerViewModel(repo)

setContent {
    PlayerScreen(playerViewModel) { finish() }
}
```

This ensures dynamic feature modules receive the same **Retrofit**, **Repository**, and **Hilt graph** as the base app.

---

# 3️⃣ Testing Dynamic Feature Module Download (Local Testing)

Dynamic delivery must be tested using a `.aab` file, not an apk.
Google recommends **Bundletool** for local testing.

## ✔️ Step 1 – Build `.apks` bundle
You already placed `bundletool.jar` inside the repo.

Run:
```bash
java -jar bundletool.jar build-apks \
  --local-testing \
  --bundle=app-debug.aab \
  --output=apk.apks
```
This generates a set of installable APKs representing base + dynamic feature.

## ✔️ Step 2 – Install the APK set
```bash
java -jar bundletool.jar install-apks --apks=apk.apks
```
This installs the base app and allows local on-demand module downloads.

## ✔️ Step 3 – Trigger dynamic module download
When the code requests:
```kotlin
SplitInstallManager.requestInstall(listOf("feature_player"))
```
Android performs a **local** download from the `apk.apks` file.

---

# ✔️ Summary
This setup allows:
- Building and loading dynamic modules on demand
- Sharing Hilt dependencies safely using EntryPoints
- Testing the download process using Bundletool without Play Store

Your repo now fully supports **modularization**, **on-demand delivery**, and **dependency‑shared dynamic features**.

---

If you want, I can add:
✅ architecture diagram
✅ dynamic feature loading flowchart
✅ commands section
✅ troubleshooting section

Just tell me!

