# Notera - Application Documentation

## Overview

**Notera** is a sophisticated Android note-taking and audio transcription application that allows users to record audio from media playback, convert videos to audio, and generate AI-enhanced transcriptions and summaries. The app features a unique floating window interface for seamless multitasking.

### Application Details

- **Package Name:** `com.myapp.notera`
- **Current Version:** 1.3.3 (Build 43)
- **Minimum Android Version:** Android 10 (API 29)
- **Target Android Version:** Android 15 (API 35)

---

## Key Features

### Core Functionality

1. **Internal Audio Recording**
   - Captures audio from media playback on the device using MediaProjection API
   - Real-time audio detection with user warnings if no audio is detected
   - Automatic PCM to MP3 conversion using FFmpeg
   - 10-minute recording limit with auto-stop

2. **Video to Audio Conversion**
   - Convert video files to audio format
   - Automatic transcription of converted audio

3. **Rich Text Note Taking**
   - WordPress Aztec Editor integration for rich text editing
   - Markdown support via TextieMD library
   - Text formatting and styling capabilities

4. **Screenshot Integration**
   - Capture screenshots during recording sessions
   - Timestamps embedded in transcription
   - Images stored locally and referenced in notes

5. **AI-Powered Enhancements**
   - Backend transcription service integration
   - LinkedIn post generation from transcripts
   - Text enhancement and improvement
   - Summary vs. Full Transcript modes

6. **Floating Window Interface**
   - Picture-in-Picture style recording UI
   - Works across all apps (overlay permission required)
   - Draggable and minimizable interface
   - Real-time recording controls (start/pause/stop)
   - Header/subheader input during recording
   - Timer display with 10-minute countdown

7. **Multi-Theme Support**
   - 10 pre-defined color schemes:
     - Green, Blue, Purple, Red, Teal
     - Grey, Yellow, Black, White, Deep Green
   - System dynamic colors support
   - Light/Dark mode for each theme

8. **Usage Limits System**
   - Free tier: 10 minutes of transcription time
   - AI feature usage tracking
   - LinkedIn conversion limits
   - In-app upgrade request functionality

---

## Technical Architecture

### Architecture Pattern

The application follows the **MVVM (Model-View-ViewModel)** architecture pattern with clean architecture principles:

- **Model:** Room database entities and data classes
- **View:** Jetpack Compose UI components
- **ViewModel:** Business logic and state management
- **Repository Pattern:** Data access abstraction through DAOs

### Technology Stack

#### Programming Languages & Frameworks
- **Kotlin:** 2.0.21
- **Jetpack Compose:** 100% modern declarative UI
- **Coroutines:** 1.8.0 for asynchronous operations

#### Core Android Libraries
- **Room Database:** 2.6.1 (local data persistence)
- **DataStore:** 1.1.7 (preferences and settings)
- **Navigation Compose:** 2.8.0
- **AndroidX Lifecycle:** 2.9.0
- **Material 3:** 1.4.0-alpha12

#### Media Processing
- **FFmpeg Kit:** 6.0-2.LTS (audio/video conversion)
- **WordPress Aztec Editor:** v2.1.4 (rich text editing)
- **Rich Text Editor Compose:** 1.0.0-rc10

#### Networking
- **Retrofit:** 2.9.0 (REST API client)
- **OkHttp:** 4.12.0 (HTTP client)
- **Gson:** 2.11.0 (JSON parsing)

#### Image Loading
- **Coil:** 3.2.0
- **Glide:** For video thumbnails

#### Firebase Services
- Firebase Authentication (anonymous)
- Firebase Crashlytics
- Firebase Analytics
- Firebase App Check

#### Other Libraries
- Intro Showcase View 2.0.2 (onboarding)
- TextieMD Library 1.0.4 (markdown support)
- Accompanist System UI Controller 0.36.0

---

## Project Structure

```
com.example.notera/
├── database/
│   ├── AudioText.kt                # Data model
│   ├── AudioTextDatabase.kt        # Database instance
│   ├── AudioTextDao.kt             # Data access object
│   └── AudioTextDbData.kt          # Database entity
├── services/
│   ├── MediaCaptureService.kt      # Audio recording service
│   ├── FloatingWindowService.kt    # Overlay UI service
│   └── FloatingTextWindowService.kt
├── pages/
│   ├── Homepage/                   # Main screen
│   ├── EditPage/                   # Note editing
│   ├── Settings/                   # App settings
│   └── RecordingList.kt            # List view
├── viewModals/
│   ├── AppViewModel.kt
│   └── AddMediaViewModel.kt
├── network/
│   └── APIservice.kt               # Backend API integration
├── ui/theme/                       # Theme variants (10 color schemes)
│   ├── greenScheme/
│   ├── blueScheme/
│   ├── purpleScheme/
│   └── ... (7 more themes)
├── wordpress_comments/             # WordPress integration
├── wordpress_shortcodes/           # Media shortcode handling
└── glideloader/                    # Image loading utilities
```

---

## Core Components

### Services

#### MediaCaptureService
**Location:** `services/MediaCaptureService.kt`

Handles all audio recording functionality:
- Captures internal audio using MediaProjection API
- Converts PCM to MP3 using FFmpeg
- Takes screenshots with timestamp tracking
- Handles device orientation changes
- Auto-stops after 10-minute limit
- Sends audio to backend for transcription

#### FloatingWindowService
**Location:** `services/FloatingWindowService.kt`

Provides overlay UI during recording:
- Draggable floating window interface
- Recording controls (start/pause/stop)
- Header/subheader input fields
- Screenshot capture button
- Transcript vs. Summary mode selection
- Timer display with countdown

#### FloatingTextWindowService
**Location:** `services/FloatingTextWindowService.kt`

Quick text note creation overlay.

### ViewModels

#### AppViewModel
**Location:** `viewModals/AppViewModel.kt`

Primary ViewModel managing:
- Notes data and CRUD operations
- Theme preferences
- User usage limits (transcription time, AI features)
- Database operations via Room
- API calls for AI enhancement features

#### AddMediaViewModel
**Location:** `viewModals/AddMediaViewModel.kt`

Handles:
- Video-to-audio conversion
- File upload functionality
- Media transcription requests

### Database

#### AudioTextDatabase
**Location:** `database/AudioTextDatabase.kt`

Room database implementation with:
- **Entity:** `AudioTextDbData`
- **DAO:** `AudioTextDao`
- **Singleton pattern** for database instance

#### Data Model Fields
- Header and subheader
- Text content (transcription/notes)
- Screenshot URIs (stored as String)
- Screenshot timestamps
- Editing timestamps
- Audio file URI

### UI Screens

#### Homepage
**Location:** `pages/Homepage/`

Main screen features:
- Notes list grouped by headers
- Search functionality
- Usage statistics display
- Expandable FAB for actions (record, add media, text note)

#### EditPage
**Location:** `pages/EditPage/`

Note editing interface:
- Rich text editor (Aztec + custom editor)
- Audio playback controls
- Image gallery display
- AI enhancement features (LinkedIn, enhance, improve)
- Save and export functionality

#### Settings
**Location:** `pages/Settings/`

Configuration screen:
- Theme selection (10 color schemes)
- Light/Dark/System mode toggle
- Privacy policy link

---

## Network & API Integration

### Backend API Service
**Location:** `network/APIservice.kt`

Retrofit-based API client handling:
- Audio file upload for transcription
- Transcription retrieval
- AI enhancement requests:
  - LinkedIn post generation
  - Text enhancement
  - Text improvement
  - Summary generation

### API Endpoints
All endpoints communicate with a backend service for:
- Speech-to-text transcription
- AI-powered content generation
- Usage tracking and limits

---

## Permissions

The application requires the following permissions:

- `RECORD_AUDIO` - For audio capture
- `FOREGROUND_SERVICE_MEDIA_PROJECTION` - Screen recording capability
- `SYSTEM_ALERT_WINDOW` - Overlay windows (floating UI)
- `POST_NOTIFICATIONS` - Notification display
- `INTERNET` - API communication
- `READ_MEDIA_AUDIO` - Media file access
- `ACCESS_NETWORK_STATE` - Network status checking

---

## Build Configuration

### Gradle Setup

**Build Tools:**
- Android Gradle Plugin: 8.6.1
- Kotlin: 2.0.21
- KSP (Kotlin Symbol Processing): 2.0.21-1.0.27
- Google Services: 4.4.2

**SDK Configuration:**
```kotlin
compileSdk = 35
minSdk = 29
targetSdk = 35
```

**Build Types:**
- **Debug:** Default configuration
- **Release:**
  - Code minification enabled
  - Resource shrinking enabled
  - ProGuard rules applied

### Repositories
- Google Maven Repository
- Maven Central
- JitPack (for GitHub-hosted libraries)
- WordPress S3 (for Aztec editor dependency)

---

## Design Patterns

### Architectural Patterns

1. **MVVM (Model-View-ViewModel)**
   - Clear separation of UI and business logic
   - ViewModel survives configuration changes

2. **Repository Pattern**
   - Data access abstraction through DAOs
   - Single source of truth for data

3. **Singleton Pattern**
   - Database instance
   - API service
   - RecordTranscription state manager

4. **Observer Pattern**
   - StateFlow for reactive UI updates
   - DataStore for preference changes
   - Room Flow for database queries

5. **Factory Pattern**
   - ViewModel factories
   - Database builder

### Code Quality Patterns

1. **Coroutines Usage**
   - Proper dispatcher usage (IO, Main, Default)
   - Structured concurrency
   - Flow-based reactive streams

2. **Compose Best Practices**
   - State hoisting
   - Remember and mutableStateOf
   - LaunchedEffect for side effects
   - Stable composables

3. **Error Handling**
   - Try-catch blocks in network calls
   - Fallback UI states
   - User-friendly error messages

4. **Resource Management**
   - Proper service cleanup
   - File deletion on failure
   - MediaPlayer release

---

## Data Flow

### Recording Flow
1. User initiates recording from FAB or floating window
2. `MediaCaptureService` starts with MediaProjection permission
3. Audio captured in PCM format
4. FFmpeg converts PCM to MP3 in real-time
5. Screenshots captured on demand with timestamps
6. Recording auto-stops at 10 minutes
7. Audio file sent to backend API for transcription
8. Transcription saved to Room database
9. UI updated via StateFlow in ViewModel

### Note Editing Flow
1. User selects note from Homepage
2. `EditPage` loads note data from ViewModel
3. Rich text editor initialized with note content
4. User can:
   - Edit text
   - Play audio
   - View screenshots
   - Request AI enhancements
5. Changes saved to database via ViewModel
6. Usage limits checked for AI features

---

## State Management

### User Preferences (DataStore)
- Selected theme
- Light/Dark mode preference
- Onboarding completion status
- Usage statistics

### Application State (StateFlow)
- Notes list
- Current note being edited
- Recording status
- Usage limits (transcription time, AI credits)
- Loading states

### Database State (Room + Flow)
- All notes (AudioTextDbData)
- Reactive queries using Flow
- Automatic UI updates on data changes

---

## Key Innovations

1. **Internal Audio Capture**
   - Unique ability to capture audio from other apps
   - Real-time audio level monitoring
   - Automatic silence detection

2. **Floating Window Multitasking**
   - Record while using other apps
   - Minimalist, draggable interface
   - Instant access to recording controls

3. **AI-Powered Content Generation**
   - Transform transcripts into LinkedIn posts
   - Text enhancement for clarity
   - Automatic summarization

4. **Integrated Screenshot Timeline**
   - Visual bookmarks in audio recordings
   - Timestamp-based navigation
   - Context preservation

5. **Flexible Theme System**
   - 10 distinct color schemes
   - Dynamic color support
   - Per-theme light/dark modes

---

## Future Considerations

### Potential Enhancements
- Cloud backup and sync
- Export to multiple formats (PDF, DOCX)
- Collaboration features
- Advanced search with filters
- Folder/tag organization
- Widget support
- Apple Watch companion app
- Real-time transcription during recording

### Technical Debt
- Consider migration to Kotlin Multiplatform for iOS support
- Implement proper dependency injection (Hilt/Koin)
- Add comprehensive unit and integration tests
- Implement offline-first architecture with sync
- Add proper error analytics beyond Crashlytics

---

## Development Setup

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or higher
- Android SDK 35
- Gradle 8.6.1

### Build Instructions
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Configure Firebase (add `google-services.json`)
5. Build and run on device/emulator (API 29+)

### Note on Permissions
- Overlay permission must be granted manually in Settings
- MediaProjection requires runtime permission dialog
- Audio recording requires runtime permission

---

## Credits

### Third-Party Libraries
- **WordPress Aztec Editor** - Rich text editing
- **FFmpeg Kit** - Media processing
- **Jetpack Compose** - Modern Android UI
- **Room Database** - Local persistence
- **Retrofit** - Network communication
- **Firebase** - Analytics and crash reporting

---

## License

This documentation is for the Notera Android application. For licensing information, please refer to the project's LICENSE file.

---

**Last Updated:** 2025-11-01
**Version:** 1.3.3 (Build 43)
