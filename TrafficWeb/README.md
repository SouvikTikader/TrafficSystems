# Smart Traffic System — Railway Deployment

## Project Structure
```
TrafficWeb/
├── railway.toml                          ← Railway build/start config
├── Procfile                              ← Fallback start command
├── pom.xml                               ← Maven (Spring Boot 3)
└── src/main/
    ├── java/com/traffic/
    │   ├── TrafficApplication.java       ← Spring Boot entry point
    │   ├── model/
    │   │   ├── VehicleEvent.java         ← Request DTO
    │   │   └── ViolationRecord.java      ← JPA Entity
    │   ├── db/
    │   │   └── ViolationRepository.java  ← Spring Data JPA
    │   ├── service/
    │   │   └── TrafficService.java       ← Business logic
    │   └── controller/
    │       └── TrafficController.java    ← REST endpoints
    └── resources/
        ├── application.properties        ← Local (H2) config
        ├── application-railway.properties← Production (PostgreSQL) config
        └── static/index.html            ← Web frontend
```

---

## REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/process` | Submit vehicle event |
| GET | `/api/violations` | List all violations |
| GET | `/api/violations/search?q=MH12` | Search by vehicle ID |
| DELETE | `/api/violations/{id}` | Delete violation |
| GET | `/api/stats` | Total violations & fines |

---

## Deploy to Railway (Step by Step)

### Step 1 — Push to GitHub
```bash
cd TrafficWeb
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/traffic-system.git
git push -u origin main
```

### Step 2 — Create Railway Project
1. Go to [railway.app](https://railway.app) → **New Project**
2. Choose **Deploy from GitHub repo**
3. Select your `traffic-system` repository
4. Railway auto-detects Java + Maven via `railway.toml`

### Step 3 — Add PostgreSQL Database
1. In your Railway project dashboard, click **+ New**
2. Select **Database → PostgreSQL**
3. Railway automatically injects these env vars into your app:
   - `DATABASE_URL`
   - `PGUSER`
   - `PGPASSWORD`
   - `PGHOST`, `PGPORT`, `PGDATABASE`

### Step 4 — Set Environment Variable
In Railway project → **Variables**, add:
```
SPRING_PROFILES_ACTIVE = railway
```

### Step 5 — Deploy
Railway builds and deploys automatically on every `git push`.

Your app will be live at:
```
https://your-app-name.up.railway.app
```

---

## Local Development

```bash
mvn clean package -DskipTests
java -jar target/TrafficSystem.jar
```
Opens at http://localhost:8080
Uses H2 in-memory DB automatically (no setup needed).

---

## Fine Rules
| Speed | Fine |
|-------|------|
| > 120 km/h | ₹5,000 |
| > 100 km/h | ₹2,000 |
| > 80 km/h | ₹1,000 |
| Emergency vehicle | Exempt |
