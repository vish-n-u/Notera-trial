# Notera - Audio Transcription & Note-Taking Android App

## Project Overview
Notera captures internal device audio via MediaProjection API, transcribes it via a backend service, and organizes transcriptions alongside rich-text notes. Features include floating window recording controls, video-to-audio conversion, AI content generation (LinkedIn posts, text enhancement), and screenshot timeline bookmarks.

## Build & Run

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean

# Lint check
./gradlew lint
```

**Note:** On Windows, use `gradlew.bat` instead of `./gradlew`.

## Project Structure

```
app/src/main/
├── java/com/example/notera/       # Kotlin source (see Package Note below)
│   ├── MainActivity.kt            # Entry point, permissions, service orchestration
│   ├── Routes.kt                  # Navigation enum + NavHost composable
│   ├── RecordTranscription.kt     # Global upload state manager
│   ├── DataStore.kt               # DataStore preference keys
│   ├── SplashScreen.kt            # Onboarding swipe screens
│   ├── PermissionUtil.kt          # Permission checking utilities
│   ├── AztecView.kt / AztecView2.kt  # Rich text editor activities (Aztec)
│   ├── QuillCheck.kt / TextEditor.kt # Editor utilities
│   ├── database/
│   │   ├── AudioText.kt           # Domain model + FlowType enum + HeaderAndCreatedAt
│   │   ├── AudioTextDbData.kt     # Room @Entity
│   │   ├── AudioTextDao.kt        # Room @Dao interface
│   │   └── AudioTextDatabse.kt    # Room database singleton
│   ├── services/
│   │   ├── MediaCaptureService.kt     # Audio capture foreground service (MediaProjection + FFmpeg)
│   │   ├── FloatingWindowService.kt   # Draggable overlay recording controls
│   │   └── FloatingTextWindowService.kt # Quick text note overlay
│   ├── pages/
│   │   ├── Homepage/Homepage.kt       # Main screen with notes list + FAB
│   │   ├── Homepage/ExtendableFAB.kt  # Expandable floating action button
│   │   ├── EditPage/EditPage.kt       # Note editor
│   │   ├── EditPage/AudioPlayerCard.kt # Audio playback UI
│   │   ├── EditPage/AddImage.kt       # Image attachment
│   │   ├── EditPage/UtilFunctions.kt  # Editor helpers
│   │   ├── Settings/Settings.kt       # Theme & preferences
│   │   ├── Settings/PrivacyPolicy.kt  # Privacy policy WebView
│   │   ├── RecordingList.kt           # Notes list by header
│   │   └── AIGeneratedText.kt         # AI content display
│   ├── viewModals/
│   │   ├── AppViewModel.kt            # Main ViewModel (CRUD, API, state)
│   │   └── AddMediaViewModel.kt       # Video-to-audio + upload
│   ├── network/
│   │   └── APiservice.kt              # Retrofit API client + data classes
│   ├── ui/theme/                       # 10+ color schemes (each with Color.kt + Theme.kt)
│   ├── glideloader/                    # Glide image loading for Aztec editor
│   └── wordpress_comments/ & wordpress_shortcodes/  # Aztec editor plugins
├── res/
│   ├── layout/                    # XML layouts (floating windows, editor toolbars)
│   ├── drawable/                  # Icons, backgrounds, shapes
│   ├── values/                    # colors.xml, strings.xml, themes.xml
│   ├── xml/                       # network_security_config, backup_rules
│   └── raw/                       # Raw assets
└── AndroidManifest.xml
```

## Critical Architecture Details

### Package Name Mismatch
- **Build namespace / applicationId**: `com.myapp.notera`
- **Actual code package declarations**: `com.example.devaudioreccordings`
- **Source directory**: `com/example/notera/`
- **Manifest references**: `com.example.devaudioreccordings` (activities/services)
- When adding new files, follow the existing convention: place files under `com/example/notera/` directory but use `package com.example.devaudioreccordings` in the package declaration.

### Architecture: MVVM
```
Compose UI → ViewModels (AppViewModel, AddMediaViewModel) → Room DAO → SQLite
                                    ↕
                             Retrofit API → Backend
```

### State Management
- **UI state**: `mutableStateOf()` / `remember {}`
- **ViewModel state**: `StateFlow`
- **Persistent prefs**: DataStore (`Context.user` extension)
- **Database**: Flow-based reactive queries from Room

### Navigation
- Jetpack Navigation Compose via `Routes` enum
- Routes: `Homepage`, `ListRecordings`, `EditPage`, `AIGeneratedText`, `Settings`, `PrivacyPolicy`
- EditPage takes `?id={id}&flow={flow}` query params

### Key Enums
- `FlowType`: `MediaCaptureService`, `AddMedia`, `AddText`, `AddAudio`, `RecordAudio`
- `Routes`: Navigation destinations
- `Flows`: `MediaCaptureService`, `AddMedia`, `AddText`, `FloatingText`

### Broadcast Actions (defined in MainActivity companion)
All use `com.example.devaudioreccordings` prefix pattern:
- `ACTION_START_AUDIOCAPTURE`, `ACTION_STOP_AUDIOCAPTURE`
- `ACTION_PAUSE_AUDIOCAPTURE`, `ACTION_RESUME_AUDIOCAPTURE`
- `ACTION_STOP_MEDIACAPTURE`, `ACTION_SAVE_FLOATING_TEXT`
- `ACTION_TAKE_SCREENSHOT`
- `ACTION_AUDIO_NOT_AVAILABLE_INDICATOR`, `ACTION_AUDIO_AVAILABLE_INDICATOR`
- `ACTION_ORIENTATION_CHANGE_TO_LANDSCAPE`, `ACTION_ORIENTATION_CHANGE_TO_POTRAIT`

### Audio Capture Pipeline
1. MediaProjection grants access → AudioRecord captures PCM (16-bit, 44.1kHz, mono)
2. FFmpeg Kit converts PCM → MP3 in real-time
3. On stop → MP3 uploaded via Retrofit multipart to backend
4. Backend transcribes → text saved to Room database

### Backend API
- **Base URL**: `https://audiotext-backend-tvik.onrender.com/`
- **Auth**: Firebase App Check token in `X-Firebase-AppCheck` header
- **Endpoints**: `uploadFile` (multipart), `user`, `linkedinShareableText`, `enhanceText`, `increase`
- **Timeouts**: 190s read/write, 200s connect (long transcription processing)

## Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Database**: Room (v2.6.1)
- **Networking**: Retrofit 2.9.0 + OkHttp 4.12.0
- **Audio**: MediaProjection API + FFmpeg Kit 6.0-2.LTS
- **Rich Text**: WordPress Aztec Editor v2.1.4 + TextieMD 1.0.4
- **Preferences**: DataStore
- **Auth**: Firebase Anonymous Auth + App Check
- **Image Loading**: Glide (Landscapist) + Coil 3.2.0
- **Monitoring**: Firebase Crashlytics + Analytics
- **Build**: AGP 8.6.1, Kotlin 2.0.21, KSP, Compose BOM 2025.06.00
- **Min SDK**: 29 (Android 10) | **Target SDK**: 35 (Android 15)
- **Java**: 1.8 source/target compatibility

## Coding Conventions
- Use Kotlin idioms (data classes, sealed classes, extension functions, coroutines)
- Compose UI with Material 3 theming (`MaterialTheme.colorScheme`)
- Coroutine dispatchers: `Dispatchers.IO` for DB/network, `Dispatchers.Main` for UI updates
- Room DAO methods return `Flow<List<T>>` for reactive queries, `suspend` for single operations
- ViewModels use `viewModelScope` for coroutine management
- DataStore accessed via `Context.user` extension property
- Firebase App Check token required on all API calls
- Services communicate via broadcasts (LocalBroadcastManager pattern with explicit intents)
- Floating windows use `WindowManager.LayoutParams` with `TYPE_APPLICATION_OVERLAY`

## Important Notes
- `google-services.json` contains Firebase config - never commit changes to this file carelessly
- ProGuard is enabled for release builds - use `@Keep` annotation on Retrofit data classes
- The `app/libs/` directory may contain local JARs - check before modifying dependencies
- Database entity is `AudioTextDbData`; domain model is `AudioText` - they are separate classes
- DAO method `instertAudioText` has a typo ("insert") - maintain consistency, do not rename without a migration
