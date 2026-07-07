# 📅 Schedo — An AI Attendance Tracker App

**Your timetable, sorted by AI. Your attendance, tracked automatically.**

Schedo is an AI-powered timetable and academics companion built for college students. Snap a photo of your printed timetable and Schedo turns it into a clean, structured, offline-ready schedule — complete with attendance ("bunk") analytics, exam paper archives, and smart reminders.

📲 **Live on the Google Play Store — trusted by 500+ students**

<p>
  <img alt="Platform" src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white">
  <img alt="Language" src="https://img.shields.io/badge/Kotlin-100%25-7F52FF?logo=kotlin&logoColor=white">
  <img alt="Min SDK" src="https://img.shields.io/badge/minSdk-24-blue">
  <img alt="Play Store Users" src="https://img.shields.io/badge/Play%20Store-500%2B%20users-brightgreen?logo=googleplay&logoColor=white">
  <img alt="License" src="https://img.shields.io/badge/License-MIT-lightgrey">
</p>

<p>
  <img width="200" alt="Schedo screenshot 1" src="https://github.com/user-attachments/assets/542a6201-50dc-4ce4-bf7a-06e9342602e9" />
  <img width="200" alt="Schedo screenshot 2" src="https://github.com/user-attachments/assets/7e60270e-d16f-4467-8d4f-9ef1f2a81ea0" />
  <img width="200" alt="Schedo screenshot 3" src="https://github.com/user-attachments/assets/e1add91d-fa31-4ff1-a5a0-f9350cbb2b7e" />
  <img width="200" alt="Schedo screenshot 4" src="https://github.com/user-attachments/assets/6d7d006a-ae85-413e-8caa-f34e3f796bbd" />
  <img width="200" alt="Schedo screenshot 5" src="https://github.com/user-attachments/assets/fae962a7-0d26-4917-984d-2142c64900a5" />
</p>

---

## 📥 Download

<!-- Replace with your actual Play Store listing link -->
[**➡️ Get it on Google Play**](https://play.google.com/store/apps/details?id=com.jhainusa.jss_student)

---

## 🚀 Features

### 📸 AI Timetable Extraction
Upload a photo of your printed or handwritten timetable and Schedo uses the **Gemini API** to automatically convert it into a structured schedule — no manual data entry required.

### 🗂️ Smart Schedule Management
Timetables are parsed and organized by **day, subject, time, and teacher**, then displayed in a clean, scrollable weekly view.

### 📊 Attendance / Bunk Analytics
A dedicated analytics screen tracks how many classes you've attended vs. missed per subject, helping you stay on top of attendance requirements before it's too late.

### 📝 CIA & Semester Exam Papers
Browse and download previous **CIA (Continuous Internal Assessment)** and semester exam papers, organized subject-wise, right from the app.

### 🔔 Smart Notifications
Powered by **Firebase Cloud Messaging**, Schedo can notify students about class updates, uploads, and other important academic events.

### ⚡ Offline-First
Once your timetable is extracted, it's stored locally in a **Room database** — so the app works fully offline after the initial setup.

### 🧑‍🎓 Personalized Onboarding & Profile
A guided onboarding flow collects your name and preferences, with profile data optionally synced via **Supabase**.

### 🎨 Modern, Student-Friendly UI
Built entirely with **Jetpack Compose** and **Material 3**, featuring Lottie animations, skeleton loaders, swipeable actions, and a home-screen widget (via Jetpack Glance) for at-a-glance schedule info.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose · Material 3 |
| Local Storage | Room Database |
| Preferences | DataStore |
| Networking | Retrofit · OkHttp |
| AI | Google Gemini API |
| Backend / Sync | Supabase (Postgrest) · Firebase (Firestore, Realtime DB, Analytics, Cloud Messaging) |
| Widgets | Jetpack Glance |
| Media | Coil (image loading) · Lottie (animations) |
| Async | Kotlin Coroutines |

---

## 📂 Project Structure

```
com.jhainusa.jss_student
│
├── RoomDatabase/          # Local persistence — Schedule, DaySchedule, DAO, Repository
├── GeminiBackend/         # AI timetable extraction via Gemini API
├── UserPref/              # DataStore preferences, Supabase client, user profile & ViewModel
├── ciaPaperPage/          # CIA & semester exam paper browsing
├── FirebaseMessaging/     # Push notification service
├── onboarding/            # First-launch onboarding flow
├── ui/theme/              # Colors, typography, and Compose theming
│
├── HomeScreen.kt          # Main dashboard
├── TimeTable.kt           # Weekly timetable view
├── CalendarView.kt        # Calendar screen
├── BunkAnalyticsScreen.kt # Attendance analytics
├── UploadTImeTable.kt     # Timetable image upload & extraction flow
├── SplashScreen.kt        # App launch screen
└── MainActivity.kt        # Entry point & navigation host
```

---

## 📸 How It Works

1. User uploads a photo of their timetable.
2. The image is sent to the **Gemini API** for parsing.
3. Gemini returns structured JSON (day, subject, time, teacher).
4. The data is saved to the local **Room database**.
5. The timetable is rendered in a clean, color-coded weekly view.
6. Attendance is logged per class and visualized in the **Bunk Analytics** screen.

---

## 📦 Getting Started (for developers)

### Prerequisites
- Android Studio (latest stable)
- JDK 11
- An Android device/emulator running **API 24+**

### Clone the repository
```bash
git clone https://github.com/sanketjha872/JSS_STUDENT.git
cd JSS_STUDENT
git checkout master2
```

### Configure API keys
Create a `local.properties` file in the project root (this file is git-ignored) and add:

```properties
GEMINI_API_KEY=your_gemini_api_key
SUPABASE_URL=your_supabase_project_url
SUPABASE_ANON_KEY=your_supabase_anon_key
```

You'll also need your own `google-services.json` from the Firebase Console, placed inside the `app/` directory, to enable Firebase Analytics, Firestore, and Cloud Messaging.

### Build & run
Open the project in Android Studio and run it on an emulator or physical device, or via the command line:

```bash
./gradlew assembleDebug
```

---

## 🎯 Who It's For

- College and university students juggling multiple subjects and sections
- Students who want to track attendance without manual spreadsheets
- Anyone tired of manually re-typing a printed timetable into their phone

---

## 🧠 Roadmap

- ☁️ Full cloud sync across devices
- 📝 Per-subject notes
- 🤖 AI-based study planner
- 🔔 Smarter class reminders based on live schedule changes

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!
Feel free to check the [issues page](https://github.com/sanketjha872/JSS_STUDENT/issues) or open a pull request.

## 📄 License

This project is licensed under the **MIT License**.

---

<p align="center">Built to make student life a little easier — one AI-parsed timetable at a time. 💙</p>
