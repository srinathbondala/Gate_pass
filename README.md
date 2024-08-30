# Campus Connect

**Campus Connect** is an Android application designed to streamline the permission handling process within educational institutions. The app allows students to request permissions, which are reviewed and managed by faculty members and security personnel. The goal is to create a seamless communication flow between students, faculty, and security, ensuring efficient handling of permissions and student movement.

## Features

- **User Roles**:
  - **Student**: Can submit permission requests, view the status of their requests, and receive notifications from faculty.
  - **Faculty**: Can approve or decline student requests, view past data of students, add new student details, and send notifications to mentee students or all students.
  - **Security**: Can view the details of students whose requests have been approved by faculty and manage their movement accordingly.

- **Permission Request Process**:
  - Students submit a request, including their details and an image.
  - The request is sent to the assigned faculty mentor.
  - The faculty can approve or decline the request based on the provided reason.
  - Students can view the status of their requests in real-time.
  - Once approved, security personnel can view the student's details and manage their exit.

- **Data Management**:
  - All request data is stored in **Firestore**, allowing faculty to view historical data of students.
  - Faculty can add and manage student details within the app.

- **Notifications**:
  - Faculty can send notifications to specific mentee students or broadcast messages to all students.

## Firebase Integration

The app leverages **Firebase** services, specifically:

- **Firestore**: Used as the primary database for storing user data, requests, and historical records.
- **Firebase Authentication**: Manages user authentication, allowing secure login for students, faculty, and security personnel.
- **Firebase Cloud Messaging (FCM)**: Facilitates sending notifications from faculty to students.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/campus-connect.git
