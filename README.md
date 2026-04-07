📅 JSS Student App

A smart and modern timetable management app designed for students to easily extract, manage, and view their class schedules using AI.

<img width="1220" height="2712" alt="Screenshot_20260307-175803 Schedo" src="https://github.com/user-attachments/assets/542a6201-50dc-4ce4-bf7a-06e9342602e9" />

🚀 Features
📸 AI Timetable Extraction
Upload a timetable image
Automatically converts it into structured JSON using AI (Gemini Vision API)
No manual entry required
🗂️ Smart Schedule Management
Stores timetable locally using Room Database
Organized by:
Day (Mon–Sat)
Subject
Time
Teacher
📆 Clean UI & UX
Built with Jetpack Compose
Smooth onboarding experience
Minimal and student-friendly design

⚡ Offline Support
Once extracted, timetable is saved locally
Works without internet after initial upload

🎨 Intelligent UI Enhancements
Auto color assignment for subjects
Scrollable and structured timetable view
Dynamic layout handling

🛠️ Tech Stack
Language: Kotlin
UI: Jetpack Compose
Database: Room DB
API Integration: Retrofit
AI: Gemini API
Backend : Supabase / Firebase

📂 Project Structure
com.jhainusa.jss_student
│── data
│   ├── local (Room DB)
│   ├── remote (API calls)
│
│── domain
│   ├── model (Schedule, DaySchedule)
│
│── ui
│   ├── screens
│   ├── components
│
│── viewmodel
│
│── utils

📸 How It Works
User uploads a timetable image
Image is sent to Gemini API
API returns structured JSON
JSON is parsed into:
Day
Subject
Time
Teacher
Data is stored in Room Database
Displayed in a clean timetable UI

📦 Installation
Clone the repository:
git clone https://github.com/your-username/jss-student-app.git



Run the app 🚀
🧠 Future Improvements
🔔 Class reminders & notifications
☁️ Cloud sync across devices
📊 Attendance tracking
📝 Notes per subject
🤖 AI-based study planner
🎯 Use Case

Perfect for:

College students
School students
Anyone tired of manually managing timetables
🤝 Contributing

Contributions are welcome!
Feel free to open issues or submit pull requests.

📄 License

This project is licensed under the MIT License.

💡 Inspiration

Built to simplify student life by combining:

AI automation
Clean UI
Offline-first experience
