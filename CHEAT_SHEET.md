# ASZ Management Suite - Cheat Sheet

## Zweck dieser Datei

Diese Cheat Sheet soll dir helfen, **ein ähnliches Programm von Grund auf selbst zu bauen**:

- **Backend mit Java + Spring Boot**
- **Frontend mit React + Vite**
- **REST-API zwischen Frontend und Backend**
- **CSV-Dateien als einfache Persistenz**
- **Docker + Docker Compose für Transport und Deployment**

Sie basiert auf der Architektur und den Techniken dieses Projekts.

---

## 1. Verwendeter Stack

| Bereich | Technologie | Zweck |
|---|---|---|
| IDE | IntelliJ IDEA | Java/Spring entwickeln |
| Editor/IDE | WebStorm / VS Code / IntelliJ | React-Frontend entwickeln |
| Sprache Backend | Java 17 | API und Business-Logik |
| Framework Backend | Spring Boot | Webserver, REST, Konfiguration, DI |
| Build Backend | Maven | Abhängigkeiten, Build, JAR |
| Sprache Frontend | JavaScript | UI-Logik |
| Framework Frontend | React | Komponenten, State, Routing |
| Dev-Server Frontend | Vite | schnelles lokales Frontend |
| Styling | Tailwind CSS | UI-Styling |
| Routing Frontend | react-router-dom | Seiten im SPA-Frontend |
| Persistenz | CSV-Dateien | einfache Datenspeicherung ohne Datenbank |
| Containerisierung | Docker | portable Laufumgebung |
| Orchestrierung | Docker Compose | mehrere Container zusammen starten |
| Webserver im Frontend-Container | Nginx | React-Build ausliefern + API weiterleiten |

---

## 2. Werkzeuge, die du installiert brauchst

## Für lokale Entwicklung

### Backend

- Java 17
- IntelliJ IDEA
- Maven Wrapper oder Maven lokal

### Frontend

- Node.js
- npm

### Versionsverwaltung

- Git

## Für Docker-Betrieb

- Docker
- Docker Compose

Unter Windows normalerweise über **Docker Desktop**.  
Wichtig: **Docker Desktop muss wirklich laufen** - nur installiert reicht nicht.

---

## 3. Grundidee der Architektur

Dieses Projekt ist eine **klassische 3-Schichten-Anwendung** im Backend plus **SPA-Frontend**.

## Backend-Schichten

### 1. Model
Reine Datenobjekte, z. B.:

- `Member`
- `Drink`
- `BwBooking`
- `BwDeposit`

Diese Klassen beschreiben nur Daten:

- Attribute
- Getter/Setter
- evtl. `updateFrom(...)`

### 2. Repository
Kapselt den Datenzugriff.

Hier konkret:

- CSV-Datei lesen
- CSV-Datei schreiben
- Datei anlegen, wenn sie fehlt
- Header schreiben, wenn Datei leer ist

### 3. Service
Enthält die **Business-Logik**.

Beispiele in diesem Projekt:

- nächste freie ID berechnen
- Preise berechnen
- Gesamtwert berechnen
- Kontostand eines Members ändern
- Getränkebestand bei Buchungen verändern

### 4. Controller
Stellt die **HTTP-Endpunkte** bereit.

Beispiele:

- `GET /members`
- `POST /members`
- `PUT /members/{id}`
- `DELETE /members/{id}`

Das ist die **REST-API**.

---

## 4. Was ist eine REST-API?

REST bedeutet vereinfacht:

- Daten werden über **HTTP-Endpunkte** bereitgestellt
- das Frontend spricht diese Endpunkte per `fetch(...)` an
- Daten werden meist als **JSON** übertragen

## Typische HTTP-Methoden

| Methode | Zweck |
|---|---|
| GET | Daten lesen |
| POST | neuen Datensatz anlegen |
| PUT | bestehenden Datensatz aktualisieren |
| DELETE | Datensatz löschen |

## Beispiel

### Members lesen

```http
GET /members
```

Antwort:

```json
[
  {
    "id": 1,
    "firstName": "Tobias",
    "lastName": "Bittermann",
    "email": "tobi@example.com",
    "balance": 50.0
  }
]
```

### Neues Member anlegen

```http
POST /members
Content-Type: application/json
```

```json
{
  "firstName": "Max",
  "lastName": "Mustermann",
  "email": "max@example.com",
  "balance": 10.0
}
```

---

## 5. Projektstruktur - Denkmodell

Die konkrete Ordnerstruktur kann variieren, aber dieses Muster ist sehr gut:

```text
repo-root/
├─ CSV/
│  ├─ members.csv
│  ├─ drinks.csv
│  ├─ bwbookings.csv
│  └─ bwdeposits.csv
├─ frontend/
│  ├─ package.json
│  ├─ vite.config.js
│  └─ src/
│     ├─ components/
│     ├─ pages/
│     ├─ App.jsx
│     └─ main.jsx
├─ src/
│  └─ main/
│     ├─ java/
│     │  └─ de/tobi/asz_inventory_api/
│     │     ├─ config/
│     │     ├─ member/
│     │     ├─ drink/
│     │     ├─ bwBooking/
│     │     ├─ bwDeposit/
│     │     └─ AszInventoryApiApplication.java
│     └─ resources/
│        └─ application.properties
├─ pom.xml
├─ Dockerfile.backend
├─ Dockerfile.frontend
├─ docker-compose.yml
└─ nginx.conf
```

---

## 6. Datenmodell zuerst planen

Bevor du Code schreibst, plane zuerst:

1. Welche Objekte gibt es?
2. Welche Attribute haben sie?
3. Welche Beziehungen haben sie?
4. Welche Regeln gibt es?

## In diesem Projekt

### Member

- `id`
- `firstName`
- `lastName`
- `email`
- `balance`

### Drink

- `id`
- `name`
- `purchasePrice`
- `sellingPrice`
- `factor`
- `amount`
- `totalValue`

### BwBooking

- `id`
- `memberId`
- `drinkId`
- `amountDrink`
- `bookingDate`
- `bookingCost`

### BwDeposit

- `id`
- `memberId`
- `deposit`
- `depositDate`
- `description`

## Wichtige Regel

**Erst Datenmodell und Fachlogik verstehen, dann UI bauen.**

---

## 7. Backend von Grund auf bauen

## 7.1 Spring Boot Projekt anlegen

Uebliche Wahl:

- Project: **Maven**
- Language: **Java**
- Java Version: **17**
- Dependency: **Spring Web**

In diesem Projekt ist das Backend ein **Spring Boot Web MVC**-Projekt.

---

## 7.2 Model-Klassen anlegen

Eine Model-Klasse ist ein einfacher Datenträger.

### Typisches Muster

```java
public class Member {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private double balance;

    public Member() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
```

### Gute Praxis

- leeren Konstruktor haben
- Getter/Setter sauber schreiben
- `updateFrom(...)` kann hilfreich sein
- Logik nicht in die DTO/Model-Klasse stopfen

---

## 7.3 Repository-Schicht

Das Repository kümmert sich um Speicherzugriff.

In diesem Projekt:

- `MemberCsvRepository`
- `DrinkCsvRepository`
- `BwBookingCsvRepository`
- `BwDepositCsvRepository`

## Typisches Vorgehen beim CSV-Ansatz

1. Dateipfad prüfen
2. Datei anlegen, wenn sie fehlt
3. Header schreiben, wenn sie leer ist
4. komplette Datei lesen
5. Liste im Speicher ändern
6. komplette Datei wieder speichern

## Wichtig

CSV wird hier **nicht direkt zeilenweise aktualisiert**, sondern:

1. alles laden
2. im RAM ändern
3. alles zurückschreiben

Das ist für kleine Tools vollkommen okay.

---

## 7.4 Service-Schicht

Die Service-Schicht enthält **fachliche Logik**.

### Beispiele aus diesem Projekt

#### MemberService

- alle Members laden
- neu ID berechnen
- Member speichern

#### DrinkService

- `sellingPrice = purchasePrice * factor`
- `totalValue = purchasePrice * amount`

#### BwDepositService

- Einzahlung speichern
- anschliessend `Member.balance` erhöhen

#### BwBookingService

- Buchung speichern
- `Member.balance` reduzieren
- `Drink.amount` reduzieren

## Gute Regel

**Controller = HTTP**
  
**Repository = Speicher**
  
**Service = Fachlogik**

---

## 7.5 Controller-Schicht

Ein Controller nimmt HTTP-Requests an und ruft Services auf.

### Beispiel

```java
@RestController
public class MemberController {

    @GetMapping("/members")
    public List<Member> getAllMembers() throws IOException {
        ...
    }

    @PostMapping("/members")
    public void addMember(@RequestBody Member member) throws IOException {
        ...
    }
}
```

## Typische Annotationen

| Annotation | Bedeutung |
|---|---|
| `@RestController` | Klasse liefert JSON/HTTP-Antworten |
| `@GetMapping` | GET-Endpunkt |
| `@PostMapping` | POST-Endpunkt |
| `@PutMapping` | PUT-Endpunkt |
| `@DeleteMapping` | DELETE-Endpunkt |
| `@PathVariable` | Wert aus URL lesen |
| `@RequestBody` | JSON aus Request lesen |

---

## 7.6 Dependency Injection

Spring erstellt Objekte für dich und injiziert sie in Konstruktoren.

### Beispiel

```java
public MemberService(MemberCsvRepository repository,
                     @Value("${app.members.csv-path}") String filePath) {
    this.repository = repository;
    this.filePath = filePath;
}
```

## Was hier passiert

- Spring kennt das `MemberCsvRepository`, weil es mit `@Repository` markiert ist
- Spring liest den Property-Wert aus `application.properties`
- Spring übergibt beides an den Konstruktor

## Vorteile

- weniger `new ...`
- Konfiguration zentral
- besser testbar

---

## 7.7 Konfiguration mit `application.properties`

In diesem Projekt werden dort u. a. konfiguriert:

- CORS-Origin
- CSV-Dateipfade

### Beispiel

```properties
app.frontend.origin=http://localhost:5173
app.members.csv-path=CSV/members.csv
```

## Wichtig

Den Wert liest du in Java so:

```java
@Value("${app.members.csv-path}")
```

### Häufiger Fehler

Falsch:

```java
@Value("{app.members.csv-path}")
```

Richtig:

```java
@Value("${app.members.csv-path}")
```

---

## 8. CORS verstehen

CORS betrifft Browser-Sicherheit.

Wenn Frontend und Backend auf **verschiedenen Origins** laufen, braucht das Backend eine Erlaubnis.

## Beispiel lokal

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`

Das ist **Cross-Origin**.

Darum gibt es in diesem Projekt:

```java
registry.addMapping("/**")
        .allowedOrigins(frontendOrigin)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
```

## Wichtig

CORS ist **kein Docker-Thema**, sondern ein **Browser-Thema**.

---

## 9. Frontend von Grund auf bauen

## 9.1 React + Vite anlegen

```bash
npm create vite@latest frontend
cd frontend
npm install
npm run dev
```

## Was ist Vite?

Vite ist nicht React selbst, sondern:

- Dev-Server
- Build-Tool
- schneller Frontend-Builder

**React** baut die UI.  
**Vite** startet und baut das Frontend.

---

## 9.2 Routing im Frontend

In diesem Projekt wird `react-router-dom` verwendet.

### Grundmuster

```jsx
<BrowserRouter>
  <Routes>
    <Route path="/" element={<LoginPage />} />
    <Route path="/member" element={<MembersPage />} />
    <Route path="/bierwart" element={<BierwartPage />} />
  </Routes>
</BrowserRouter>
```

## Merke

- `Route path=...` definiert Seiten
- `Link to=...` navigiert im Frontend
- API-Pfade wie `/members` sind etwas **anderes** als Seiten wie `/member`

---

## 9.3 React-Grundmuster in diesem Projekt

### State

```jsx
const [members, setMembers] = useState([]);
```

### Daten beim Laden holen

```jsx
useEffect(() => {
    loadMembers();
}, []);
```

### API-Aufruf

```jsx
const response = await fetch("/members");
const data = await response.json();
setMembers(data);
```

### Modal öffnen

```jsx
const [isModalOpen, setIsModalOpen] = useState(false);
```

### Ausgewählten Datensatz speichern

```jsx
const [selectedMember, setSelectedMember] = useState(null);
```

---

## 9.4 Controlled Components

Die Formulare in diesem Projekt sind **controlled components**.

### Beispiel

```jsx
const [firstName, setFirstName] = useState("");

<input
  valü={firstName}
  onChange={(event) => setFirstName(event.target.valü)}
/>
```

## Vorteil

React kontrolliert den Formwert.  
Dadurch kannst du:

- Werte initial setzen
- editieren
- validieren
- beim Submit gesammelt senden

---

## 9.5 Add/Edit-Modal-Muster

Dieses Projekt nutzt wiederverwendbare Form-Komponenten wie:

- `MemberAddEdit`
- `DrinkAddEdit`
- `BwBookingAddEdit`
- `BwDepositAddEdit`

## Typischer Ablauf

1. Parent-Komponente öffnet Modal
2. übergibt ausgewähltes Objekt oder `null`
3. Formular füllt Felder via `useEffect`
4. `handleSubmit(...)` baut ein Objekt
5. `onSave(...)` geht zur Parent-Komponente
6. Parent sendet Request an Backend
7. Liste wird neu geladen

---

## 9.6 CRUD-Fluss im Frontend

### Create

```jsx
fetch("/members", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify(member)
});
```

### Update

```jsx
fetch(`/members/${member.id}`, {
  method: "PUT",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify(member)
});
```

### Delete

```jsx
fetch(`/members/${id}`, {
  method: "DELETE"
});
```

### Danach

```jsx
await loadMembers();
```

Das ist das Standardmuster in diesem Projekt.

---

## 9.7 Vite-Proxy für lokale Entwicklung

Damit relative API-Calls wie `/members` lokal funktionieren, nutzt dieses Projekt in `vite.config.js`:

```js
server: {
  proxy: {
    "/members": "http://localhost:8080",
    "/drinks": "http://localhost:8080",
    "/bwbookings": "http://localhost:8080",
    "/bwdeposits": "http://localhost:8080",
  }
}
```

## Warum?

Damit kann das Frontend lokal auf `localhost:5173` laufen, während Requests intern ans Backend auf `localhost:8080` gehen.

---

## 10. Styling

Das Projekt nutzt:

- Tailwind CSS
- React Icons
- React Tooltip

## Was du daraus lernen solltest

- Styling lieber komponentennah halten
- wiederkehrende Button-Muster erkennen
- UI verbessern, ohne die Logik zu vermischen

---

## 11. Fachlogik in diesem Projekt

Dieses Projekt ist nicht nur CRUD.

Es enthält bereits **abgeleitete Fachlogik**:

### Drinks

- Verkaufspreis wird berechnet
- Gesamtwert wird berechnet

### Bookings

- Buchung reduziert Member-Guthaben
- Buchung reduziert Getränkebestand
- Update/Löschen muss alte Werte rückgängig machen

### Deposits

- Einzahlung erhöht Member-Guthaben
- Update muss nur die Differenz verrechnen

## Wichtige Lektion

Sobald Entitäten sich gegenseitig beeinflussen, gehört das in die **Service-Schicht**, nicht in den Controller und nicht ins Frontend.

---

## 12. CSV statt Datenbank

Dieses Projekt verwendet bewusst CSV-Dateien.

## Vorteile

- sehr einfach
- leicht lesbar
- schnell für kleine Tools
- keine DB-Einrichtung

## Nachteile

- keine echten Relationen
- keine komplexen Abfragen
- bei wachsender Datenmenge unpraktisch
- konkurrierende Schreibzugriffe problematisch

## Wann okay?

- kleine interne Tools
- Lernprojekte
- sehr einfache Single-User- oder Small-Team-Anwendungen

## Wann später Datenbank?

Wenn du brauchst:

- Benutzerverwaltung
- viele Datensätze
- Suchfunktionen
- parallele Nutzer
- sichere Datenintegrität

Dann ist z. B. PostgreSQL sinnvoller.

---

## 13. Lokaler Entwicklungsablauf

## Backend starten

In IntelliJ oder per Maven:

```bash
mvn spring-boot:run
```

## Frontend starten

```bash
cd frontend
npm install
npm run dev
```

## Dann

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`

---

## 14. Docker verstehen

## Was Docker hier löst

- gleiche Laufumgebung auf anderen PCs
- Java/Node nicht manüll lokal einrichten
- Frontend und Backend als reproduzierbare Pakete

## Was Docker **nicht automatisch** löst

- Firewall
- Netzwerkerreichbarkeit
- Domain
- Browser-CORS-Verhalten

---

## 15. Dockerfiles in diesem Projekt

## Backend

`Dockerfile.backend`

Muster:

1. Maven + Java 17 zum Baün
2. JAR erzeugen
3. schlankes Runtime-Image mit JRE
4. JAR starten

### Prinzip

```dockerfile
FROM maven:... AS build
...
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
COPY --from=build ...
CMD ["java", "-jar", "app.jar"]
```

## Frontend

`Dockerfile.frontend`

Muster:

1. Node-Image zum Build
2. `npm install`
3. `npm run build`
4. fertiges `dist/` in Nginx-Image kopieren

### Prinzip

```dockerfile
FROM node:... AS build
...
RUN npm run build

FROM nginx:alpine
COPY nginx.conf ...
COPY --from=build /app/dist ...
```

## Wichtige Erkenntnis

Im Docker-Betrieb läuft **nicht `npm run dev`**, sondern das **fertig gebaute Frontend**.

---

## 16. Nginx im Frontend-Container

Nginx hat hier zwei Aufgaben:

1. React-Build ausliefern
2. API-Requests ans Backend weiterleiten

### React-Fallback

```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

Das ist wichtig für Frontend-Routing.

### API-Proxy

```nginx
location /members {
    proxy_pass http://backend:8080;
}
```

## Warum `backend`?

Weil Docker Compose einen internen Service-Namen bereitstellt.

---

## 17. Docker Compose

Compose startet **mehrere Container als Gesamtsystem**.

In diesem Projekt:

- `backend`
- `frontend`

## Compose übernimmt

- Images bauen
- Container starten
- Netzwerk erstellen
- Service-Namen verfügbar machen
- Volumes mounten

---

## 18. Persistente Daten mit Volumes

In Compose:

```yaml
volumes:
  - ./CSV:/data
```

## Bedeutung

- links: Ordner auf deinem Rechner
- rechts: Ordner **im Container**

Also:

- Host: `./CSV`
- Container: `/data`

Die Umgebungsvariablen zeigen dann auf:

```yaml
APP_MEMBERS_CSV_PATH: /data/members.csv
```

## Vorteil

Container können gelöscht werden, die Daten bleiben erhalten.

---

## 19. Umgebungsvariablen

Spring kann Properties durch Environment Variables überschreiben.

### In Compose

```yaml
environment:
  APP_FRONTEND_ORIGIN: http://localhost:8081
```

## Vorteil

Du kannst:

- lokal mit `application.properties` arbeiten
- in Docker andere Werte setzen
- später auf Servern wieder andere Werte setzen

---

## 20. CORS im Docker-Setup

Die saubere Lösung dieses Projekts ist:

- CORS wird **im Backend** konfiguriert
- die erlaubte Frontend-Origin wird über `APP_FRONTEND_ORIGIN` gesetzt

### Lokal mit Docker

```yaml
APP_FRONTEND_ORIGIN: http://localhost:8081
```

### Im LAN über einen anderen PC

```yaml
APP_FRONTEND_ORIGIN: http://192.168.x.x:8081
```

### Später auf echter Domain

```yaml
APP_FRONTEND_ORIGIN: https://app.meine-domain.de
```

## Wichtige Lektion

Du erlaubst **nicht die Benutzer-IP**, sondern die **Origin deiner Web-App**.

---

## 21. Deployment-Denkweise

## Lokal

- Frontend: `localhost:5173` oder `localhost:8081`
- Backend: `localhost:8080`

## Im LAN

- App läuft auf einem Rechner
- andere Geräte greifen über `http://<IP>:8081` zu

## Auf Server

Am besten:

- eine feste Domain
- Frontend und API unter derselben Domain
- Nginx liefert Frontend aus und proxyt intern ans Backend

Dann bleibt CORS einfach.

---

## 22. Auf einen anderen PC übertragen

## Was du brauchst

- komplettes Repo
- Docker
- Docker Compose

## Wege

- Git clone
- ZIP
- USB

## Dann

```bash
docker compose up --build
```

Unter Windows:

- Docker Desktop muss laufen
- erst dann funktionieren `docker`-Befehle

---

## 23. Wichtige Docker-Befehle

## Images bauen

```bash
docker build -f Dockerfile.backend -t asz-backend .
docker build -f Dockerfile.frontend -t asz-frontend .
```

## Einzelne Container testen

```bash
docker run --name asz-backend-test -p 8080:8080 asz-backend
docker run --name asz-frontend-test -p 8081:80 asz-frontend
```

## Container anzeigen

```bash
docker ps
docker ps -a
docker compose ps
```

## Container stoppen / löschen

```bash
docker stop asz-backend-test
docker rm asz-backend-test
docker rm -f asz-frontend-test
```

## Compose

```bash
docker compose up --build
docker compose down
docker compose logs -f backend
docker compose logs -f frontend
```

---

## 24. Typische Fehler und ihre Bedeutung

## `docker command not found`

- Docker nicht installiert
- Docker Desktop nicht gestartet
- Terminal neu öffnen

## `failed to connect to docker api`

- Docker Engine läuft nicht
- Docker Desktop noch nicht fertig gestartet

## `vite: not found` im Frontend-Build

- `npm install` im Dockerfile vergessen

## `rewrite or internal redirection cycle`

- `nginx.conf` falsch
- z. B. falscher `root`-Pfad

## Daten werden gelesen, aber nicht gespeichert

Mögliche Ursachen:

- CORS blockiert POST/PUT/DELETE
- falsche Save-Buttons
- Fehler in `fetch(...)`
- CSV-Pfade falsch
- Backend-Logs prüfen

## Docker liest leere CSVs statt echter Dateien

- `@Value(...)` im Service falsch
- Compose-Pfade falsch
- Dateiname nicht konsistent

---

## 25. Lernreihenfolge für ähnliche Projekte

Wenn du ein neues Verwaltungs-Tool bauen willst, gehe am besten so vor:

1. **Fachproblem verstehen**
2. **Datenmodell entwerfen**
3. **REST-Endpunkte festlegen**
4. **Model-Klassen bauen**
5. **Repository bauen**
6. **Service-Logik bauen**
7. **Controller bauen**
8. **Frontend-Seiten und Tabellen bauen**
9. **Modal-/Form-Komponenten bauen**
10. **Frontend mit API verbinden**
11. **lokal testen**
12. **Dockerfiles schreiben**
13. **Nginx-Proxy schreiben**
14. **Compose bauen**
15. **Deployment auf anderem Gerät testen**

---

## 26. Gute Prinzipien, die du mitnehmen solltest

## Backend

- Controller dünn halten
- Business-Logik in Services
- Speicherlogik in Repositorys
- Konfiguration nicht hart codieren
- Abhängigkeiten per Konstruktor injizieren

## Frontend

- relative API-URLs nutzen
- State lokal und klar halten
- Formulare als controlled components bauen
- nach Speichern Listen neu laden
- Routen und API-Pfade sauber trennen

## Docker

- Backend und Frontend getrennt containerisieren
- Multi-Stage-Builds nutzen
- Daten über Volumes persistent halten
- Compose als Hauptstartpunkt nutzen

---

## 27. Was du mit diesem Projekt schon gelernt hast

Mit diesem Projekt hast du bereits folgende Standardverfahren praktisch genutzt:

- SPA-Frontend
- React Hooks (`useState`, `useEffect`)
- Client-seitiges Routing
- REST-API
- JSON-Kommunikation
- CRUD
- Service/Repository-Architektur
- Konfiguration über Properties
- Dependency Injection
- CORS
- Reverse Proxy
- Docker Multi-Stage-Builds
- Docker Compose
- persistente Volume-Mounts
- Deployment auf einen zweiten Rechner

Das ist für ein erstes eigenes Full-Stack-Verwaltungstool bereits sehr stark.

---

## 28. Nächste sinnvolle Ausbaustufen

Wenn du dein Wissen weiter vertiefen willst, sind diese Schritte sinnvoll:

1. Fehlerbehandlung im Frontend sichtbar machen
2. Form-Validierung verbessern
3. Tests einführen
4. DTOs / Mapper lernen
5. von CSV auf Datenbank wechseln
6. Benutzer/Authentifizierung einführen
7. echtes Production-Deployment mit Domain + HTTPS
8. CI/CD lernen

---

## 29. Merksätze

- **REST ist die Sprache zwischen Frontend und Backend.**
- **Service enthält die Logik, Controller nur die HTTP-Schnittstelle.**
- **Repository kapselt den Datenzugriff.**
- **Relative Frontend-URLs machen Docker und Deployment viel leichter.**
- **Docker macht Software portabel, aber Netzwerk und CORS musst du trotzdem verstehen.**
- **Eine Domain ersetzt später nicht die Logik, sondern gibt deiner App nur eine stabile Adresse.**

---

## 30. Mini-Checkliste für ein neues Projekt

### Backend

- [ ] Spring Boot Projekt
- [ ] Model-Klassen
- [ ] Repository-Schicht
- [ ] Service-Schicht
- [ ] Controller mit REST-Endpunkten
- [ ] `application.properties`

### Frontend

- [ ] React + Vite
- [ ] Routing
- [ ] Tabellenansicht
- [ ] Form-Komponenten
- [ ] `fetch(...)` für CRUD
- [ ] Proxy in Vite

### Docker

- [ ] `Dockerfile.backend`
- [ ] `Dockerfile.frontend`
- [ ] `nginx.conf`
- [ ] `docker-compose.yml`
- [ ] Volume für Daten
- [ ] CORS-Origin sauber setzen
