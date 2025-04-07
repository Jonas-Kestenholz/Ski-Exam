# Ski Instructor Backend - Exam Project 2025

## ✨ Project Overview
This is a backend service for managing ski lessons and instructors. Built with **Java, Javalin, and JPA**, it exposes a REST API, integrates with an external API for ski instructions, and uses JWT authentication.

---

## 📁 Technologies Used
- Java 17
- Javalin
- Hibernate (JPA)
- PostgreSQL
- Jackson (JSON serialization)
- JWT (Authentication)
- Rest Assured (Testing)

---

## ▶️ How to Run This Project
1. **Setup Database**: Start PostgreSQL and configure credentials in `persistence.xml`.
2. **Run the App**: Run `Main.java` to launch the Javalin server.
3. **Test Endpoints**: Use Postman, `dev.http`, or RestAssured tests to hit endpoints.

---

## 🔗 API Endpoints Overview

### Ski Lessons
| Method | Route | Description |
|--------|-------|-------------|
| GET    | `/skilessons` | Get all lessons |
| GET    | `/skilessons/{id}` | Get lesson by ID (with instructions) |
| POST   | `/skilessons` | Create new lesson |
| PUT    | `/skilessons/{id}` | Update lesson |
| DELETE | `/skilessons/{id}` | Delete lesson |
| PUT    | `/skilessons/{lessonId}/instructors/{instructorId}` | Add instructor to lesson |
| GET    | `/skilessons/filter?level=BEGINNER` | Filter by level |
| GET    | `/skilessons/{id}/instructions/totalduration` | Instruction duration summary |

### Authentication
| Method | Route | Description |
|--------|-------|-------------|
| POST   | `/auth/register` | Register a user |
| POST   | `/auth/login` | Login and get JWT token |

---

## 🔐 Security Notes
- Login returns JWT tokens.
- **Admin role** required for create/update/delete lesson routes.

#### Example Admin User
```json
{
  "username": "admintest",
  "password": "admin123"
}
```

---

## 🌍 External API Integration
- Fetches ski instruction data from:
```
https://apiprovider.cphbusinessapps.dk/skilesson/{level}
```
- Returned values include tips and duration (used in `/instructions/totalduration`).

---

## 🧠 Stream Operations Implemented
1. **Filter by level**: `/skilessons/filter?level=BEGINNER`
2. **Total price per instructor** (via grouping): `/skilessons/totalPrice`

---

## ✅ Testing with RestAssured
All testable endpoints are covered in `SkiLessonRoutesTest.java`.

### Covered:
- Get all lessons
- Get by ID
- Create lesson (no auth)
- Update lesson (no auth)
- Delete
- Filter by level

> 📝 Authentication was excluded in tests due to time constraints.

---

## 📘 Theoretical Answer
**Why use PUT for assigning instructor instead of POST?**
> PUT is idempotent and intended for updates. Since assigning an instructor is not creating a new resource, PUT is more semantically correct.

---

## 🧩 Future Improvements
- Re-enable JWT in tests
- More negative testing (404, 401, etc)

---

## 📤 Hand-In Checklist
- ✅ GitHub link submitted on Wiseflow
- ✅ README answers theory question
- ✅ Project zipped and submitted
- ✅ Tests and all tasks implemented

---

🎿 *Thanks for reading – good skiing!*

