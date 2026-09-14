# ASZ Management Suite

Die ASZ Management Suite ist ein Programm zum Verwalten von Mitgliedern, Inventar, Finanzbuchungen und Veranstaltungen. Das Backend basiert auf SpringBoot, das Frontend auf React. Die Anwendung kann über Docker gestartet werden.

## Features

#### Bereits umgesetzt
- Mitglieder
    - einfache Benutzerverwaltung
- Bierwart
  - Einzahlungen auf Nutzerkonten
  - Getränkebuchungen
  - Inventartracking
  - Rechnungstracking
  - Kontostandstracking

#### Geplant
- Mitglieder
    - individuelle Benutzerverwaltung
- Bierwart
  - automatische Schwundberechnung
  - Erstellung von Kassenberichten
- Kassenwart
  - Buchungen 
  - Kontostandstracking
  - Erstellung von Kassenberichten
- Schriftwart
  - Protokollverwaltung 
  - direktes Feedback auf Conventsprotokolle 
  - elektronische Unterschrift
- Fuxmajor
  - Verwaltung von Schulungsmaterial

## Technologien

### Backend
- Java 17
- Spring Boot 4.1.0
- Maven 3.9 (Docker-Build)

### Frontend
- React 19.2.7
- Vite 8.1.1
- Tailwind CSS 4.3.3
- JavaScript

### Deployment
- Docker
- Docker Compose
- Node.js 22 (Docker-Build)

## Voraussetzungen

Für den regulären Betrieb per Docker müssen folgende Programme installiert sein:

- Docker
- Docker Compose

Für die lokale Entwicklung ohne Docker (siehe unten) werden zusätzlich benötigt:

- Java 17
- Node.js 22
- eine lokale PostgreSQL-Instanz

## Installation & Deployment (Docker)

1. Repository klonen:
   ```bash
   git clone [...]
   cd ASZ_Management_Suite
   ```

2. `.env`-Datei im Projekt-Hauptverzeichnis anlegen (wird **nicht** mit ins Repo committet). Sie enthält die Zugangsdaten für die Datenbank sowie die UID, mit der der Backend-Container läuft:
   ```
   DB_USER=<beliebiger DB-Benutzername>
   DB_PASSWORD=<sicheres Passwort>
   UID=1000
   ```
   `UID` sollte der User-ID entsprechen, unter der auf diesem Rechner später `logs/` beschrieben werden soll (siehe nächster Punkt). Mit `id -u` lässt sich die eigene UID anzeigen.

3. Das `logs`-Verzeichnis anlegen und für den Backend-Container beschreibbar machen. Der Container schreibt intern als User `ubuntu` (UID 1000), das Host-Verzeichnis muss daher passend gehören:
   ```bash
   mkdir -p logs
   sudo chown -R 1000:1000 logs
   ```
   Dieser Schritt ist nur einmalig pro Rechner nötig.

4. Container bauen und starten:
   ```bash
   docker compose up -d --build
   ```

5. Danach ist die Anwendung erreichbar unter:
   ```
   http://localhost:8081
   ```
   Das Backend läuft intern im Docker-Netzwerk auf Port 8080 und wird über das Frontend (Nginx-Reverse-Proxy) angesprochen – ein direkter Host-Zugriff auf das Backend ist im Docker-Betrieb nicht vorgesehen.

## Lokale Entwicklung (ohne Docker)

Für die Entwicklung lassen sich Backend und Frontend auch direkt auf dem Host betreiben:

**Backend:**
- Lokale PostgreSQL-Instanz auf Port `5432` bereitstellen (Datenbank `asz_management_suite`).
- Zugangsdaten in `src/main/resources/application-local.properties` eintragen (Datei ist gitignored):
  ```properties
  spring.datasource.username=<DB-Benutzername>
  spring.datasource.password=<DB-Passwort>
  ```
- Anwendung mit dem Profil `local` starten (z. B. über die IDE oder `mvn spring-boot:run -Dspring-boot.run.profiles=local`).
- Backend ist danach erreichbar unter `http://localhost:8080`.

**Frontend:**
```bash
cd frontend
npm install
npm run dev
```
- Frontend ist danach erreichbar unter `http://localhost:5173`. API-Aufrufe werden über die Proxy-Konfiguration in `vite.config.js` an das lokale Backend (`http://localhost:8080`) weitergeleitet.

## Umgebungsvariablen (.env)

| Variable | Beschreibung |
|---|---|
| `DB_USER` | Benutzername der PostgreSQL-Datenbank im `db`-Container |
| `DB_PASSWORD` | Passwort der PostgreSQL-Datenbank im `db`-Container |
| `UID` | UID, mit der das Backend-Image gebaut wird; muss zum Eigentümer des Host-Verzeichnisses `logs/` passen |

## Projektstruktur

```
ASZ_Management_Suite/
├── src/main/java/de/tobi/asz_inventory_api/
│   ├── member/            # Mitgliederverwaltung
│   ├── familyLine/        # Familienlinien der Mitglieder
│   ├── position/          # Ämter/Positionen
│   ├── academicDegree/    # Akademische Grade
│   ├── credential/        # Login-Credentials (WebAuthn/Passkey, in Vorbereitung)
│   ├── bierwart/          # Bierwart-Modul
│   │   ├── drink/                # Getränke
│   │   ├── inventory/            # Inventare
│   │   ├── inventoryEntry/       # Inventareinträge
│   │   ├── vendor/                # Lieferanten
│   │   ├── bwDeposit/            # Einzahlungen
│   │   ├── bwBooking/            # Getränkebuchungen
│   │   ├── bwAccountBooking/     # Kontobuchungen
│   │   ├── bwAccountSnapshot/    # Kontostand-Snapshots
│   │   └── bwReports/            # PDF-Berichte
│   ├── enums/              # gemeinsame Enums (AccountType, Semester, Status, ...)
│   └── config/             # z. B. CORS-Konfiguration
├── frontend/src/
│   ├── pages/               # Seiten (Members, Bierwart-Unterseiten, Login, Home)
│   ├── components/          # Formular-/Add-Edit-Komponenten je Domäne
│   ├── utils/                # CRUD-, Lade-, Datums- und Naming-Hilfsfunktionen
│   └── App.jsx
├── docker-compose.yml       # Orchestriert backend, frontend und db
├── Dockerfile.backend       # Multi-Stage-Build des Spring-Boot-Backends
├── Dockerfile.frontend      # Multi-Stage-Build des React-Frontends (Nginx)
├── nginx.conf                # Reverse-Proxy-Konfiguration des Frontend-Containers
└── pom.xml
```
