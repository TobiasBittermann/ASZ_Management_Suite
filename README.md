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

## Vorassetzungen

Vor der installation müssen folgende Programme installiert sein:

- Java (Version)
- Docker
- Docker Compose

## Installation 

Repository klonen:

'git clone [...]'

Danach ist die Anwendung erreichbar unter:

'http://localhost:5173'

Backend:

'http://localhost:8080'

## Projektstruktur
