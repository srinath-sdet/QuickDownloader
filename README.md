# QuickDownloader 🚀

**QuickDownloader** is an open-source, high-speed, ad-free Android video downloader powered by `yt-dlp` and `FFmpeg`. It allows you to download videos seamlessly via the native Android Share sheet or by directly pasting links into the application.

---

## ✨ Features

- **100% Ad-Free & Clean**: No third-party advertisements, tracking SDKs, or background battery drain.
- **One-Tap Share Integration**: Share any video link directly from YouTube, Twitter/X, Instagram, or a mobile browser into QuickDownloader.
- **Embedded Engine**: Powered by `youtubedl-android` (embedded `yt-dlp` and `FFmpeg` binaries).
- **Direct Gallery Integration**: Automatically registers saved files with the Android `MediaScannerConnection` so videos instantly appear in your Gallery/Photos app.
- **Real-Time Progress Tracking**: Displays download percentage, speed, and estimated time remaining (ETA).
- **High-Quality MP4 Formats**: Extracts the best compatible single-stream MP4 files directly to your device's `Downloads` folder.

---

## 📲 Installation

### Option 1: Direct APK Download (Ready-to-use)

1. Go to the [Releases](https://github.com/srinath-sdet/QuickDownloader/releases) page.
2. Download the latest `QuickDownloader.apk`.
3. Open the downloaded file on your Android device and tap **Install** (allow *Install from Unknown Sources* if prompted).

### Option 2: Install via ADB

Ensure your device is connected with USB Debugging enabled:

```bash
adb install -r QuickDownloader.apk
```

*(Note for Xiaomi / Poco / MIUI users: Enable **Install via USB** in Developer Options if the installation is blocked).*

---

## 📖 How to Use

### Method 1: Via YouTube Share Sheet (Fastest)

1. Open **YouTube** (or any supported video app).
2. Tap the **Share** button on any video or Short.
3. Select **Quick Downloader** from the app list.
4. A confirmation dialog will display the video URL. Tap **Download**.
5. The download will begin immediately, and you can view the real-time progress.
6. Once complete, your video will be saved in your phone's **Downloads** folder and will appear in your **Gallery**.

### Method 2: Manual Link Download

1. Copy the URL of any video from your browser or app.
2. Open **Quick Downloader**.
3. Paste the URL into the input field.
4. Tap **Download Video**.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin
- **Build System**: Gradle 8.7 / Android Gradle Plugin 8.3.2
- **Core Engine**:
  - [`io.github.junkfood02.youtubedl-android:library`](https://github.com/junkfood02/youtubedl-android) — Native wrapper around `yt-dlp`
  - [`io.github.junkfood02.youtubedl-android:ffmpeg`](https://github.com/junkfood02/youtubedl-android) — Embedded FFmpeg binaries for stream handling
- **Minimum Android Version**: Android 7.0 (API level 24)
- **Target Android Version**: Android 14 (API level 34)
- **Supported ABIs**: `arm64-v8a`, `armeabi-v7a`

---

## 🏗️ Building from Source

To build the APK locally on your development machine:

1. Clone the repository:
   ```bash
   git clone https://github.com/srinath-sdet/QuickDownloader.git
   cd QuickDownloader
   ```

2. Make sure you have **JDK 17** and **Android SDK (API 34)** installed.

3. Build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```

4. The generated APK will be located at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

---

## ⚠️ Disclaimer

This project is created strictly for personal and educational purposes. Please respect intellectual property rights and adhere to the Terms of Service of content providers. The developers are not liable for any misuse of this software.

---

## 📄 License

This project is open-source under the MIT License.
