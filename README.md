Ndejje Nexus: Connecting the Campus
Welcome to the official documentation for Ndejje Nexus, a comprehensive campus companion application specifically engineered for the students and staff of Ndejje University. This platform serves as a centralized digital hub to streamline university life through modern mobile technology.
1. Project Overview
Ndejje Nexus is a robust mobile application built to enhance the daily campus experience. By leveraging the latest Android development standards, the app provides critical services—ranging from real-time logistics to student safety—under a single, unified interface.
2. Core Features
Authentication & Security
•	Smart Registration: I implemented an intelligent registration flow featuring dynamic dropdowns for Faculty and Course selection to ensure data integrity.
•	Secure Access: The system utilizes a secure login protocol to ensure that campus-specific features are accessible only to authorized university members.
•	Account Recovery: Includes an integrated "Forgot Password" workflow for seamless account retrieval.
Shuttle Hub
•	Live Tracker: I developed a real-time visualization tool for university shuttle locations using a simulated Map API.
•	Schedule Management: Students can view live schedules for major routes, such as the commute between the Main Campus and Kampala.
•	Seat Reservation: A real-time system where students book seats and drivers manage requests instantaneously.
Emergency Hub (SOS)
•	Instant SOS: A high-priority one-tap button that immediately notifies campus security with the student’s location and profile details.
•	Safe Walk Timer: A proactive safety feature that triggers an automatic SOS if a student fails to mark themselves as "Safe" before a user-defined timer expires.
•	Security Interface: A dedicated monitoring dashboard for security staff to track and respond to active alerts in real-time.


Notice Board
•	Categorized Announcements: I organized the board into Academic, Social, and Financial streams to reduce information noise.
•	Push-Style Updates: Real-time updates ensure the student body never misses critical deadlines or university news.
3. Technical Stack
I utilized a modern, reactive tech stack to ensure the "engine" of the app runs with high performance:
•	Language: Kotlin.
•	UI Framework: Jetpack Compose with Material 3.
•	Architecture: MVVM (Model-View-ViewModel) for a clean separation of concerns.
•	Logic: Kotlin Coroutines and StateFlow for reactive, real-time UI updates.
•	Theming: A customized NexusTheme featuring the signature Ndejje Green and Yellow palette.
4. Project Structure
The codebase is organized into a modular architecture for scalability:
•	model/: Defines data entities like Users and SOS Alerts.
•	repository/: I refactored these into Singletons to manage data logic and maintain a stable state throughout the app.
•	viewmodel/: Acts as the "Brain," handling business logic and UI state.
•	ui/: Divided into screens/ for feature views and components/ for reusable elements like the MockMapView.
5. Build Instructions
1.	Open the project in Android Studio (Ladybug or newer).
2.	Verify that Kotlin and Compose plugins are up to date.
3.	Run the build command: ./gradlew assembleDebug.
