📅 JSS Student App

A smart and modern timetable management app designed for students to easily extract, manage, and view their class schedules using AI.

<img width="200" height="500" alt="Screenshot_20260307-175803 Schedo" src="https://github.com/user-attachments/assets/542a6201-50dc-4ce4-bf7a-06e9342602e9" />
<img width="200" height="500" alt="Screenshot_20260307-175814 Schedo" src="https://github.com/user-attachments/assets/7e60270e-d16f-4467-8d4f-9ef1f2a81ea0" />
<img width="200" height="500" alt="Screenshot_20260307-180623 Schedo" src="https://github.com/user-attachments/assets/e1add91d-fa31-4ff1-a5a0-f9350cbb2b7e" />
<img width="200" height="500" alt="Screenshot_20260307-180734 Schedo" src="https://github.com/user-attachments/assets/6d7d006a-ae85-413e-8caa-f34e3f796bbd" />
<img width="200" height="500" alt="Screenshot_20260308-161447 Schedo" src="https://github.com/user-attachments/assets/fae962a7-0d26-4917-984d-2142c64900a5" />



🚀 Features

📸 AI Timetable Extraction
Upload a timetable image
Automatically converts it into structured JSON using AI (Gemini API)
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
