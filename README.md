# PuF_Wizard
Kurze Beschreibung des Projektes


## Inhaltsverzeichnis
1. [Allgemeine Info](#allgemeine-info)
2. [Technologien](#technologien)
3. [Installation](#installation)


<a name="allgemeine-info"></a>
## Allgemeine Info
Allgemeine Infos


<a name="technologien"></a>
## Technologien
Die Anwendung haben wir in der Programmiersprache Java in der Version 19 entwickelt. Als JDK haben wir uns für das 
[Oracle OpenJDK version 19.0.1](https://jdk.java.net/19/) entschieden. Zur Erstellung der Java-App wurde das JavaFX-Framework, 
ebenfalls in der Version 19 verwendet.


<a name="installation"></a>
## Installation
Für eine einfache Installation des Projektes haben wir eine Schritt-für-Schritt Anleitung verfasst.

Wir haben unser Projekt mit Maven gebaut. Sobald das Repo geklont und in einer IDE als Projekt erstellt wurde, kann das Projekt durchgebaut werden. Für unsere Server-Schnittstelle haben wir eine Spring Application eingebunden. Diese muss gestartet werden, damit unser Spiel korrekt laufen kann. Die Schritt-für-Schritt Anleitung haben wir für die IDE IntelliJ erstellt. Da wir das Projekt mit der IDE IntelliJ erstellt und weiterentwickelt haben empfehlen wir diese zu benutzten.  Es kann jedoch jede andere IDE verwendet werden, die ein Maven Projekt bauen kann.

Für die Ausführung der Spring App wird eine MySQL-Instanz benötigt. Wir empfehlen dafür "XAMPP". Die URL, Nutzername und Passwort müssen mit dem übereinstimmen, 
was in der Datei application.properties steht. 

Nutzername:   **root**\
**kein Passwort**

### IntelliJ (Unlimited Version)
Zuerst das Repository clonen und als Projekt erstellen. Dafür auf den grünen Button "Clone" klicken und in IntelliJ ein Projekt aus einer Versionskontrolle erstellen "Get from VCS".

1. Neues Projekt in IntelliJ öffnen "File > New > Project from Version Control"
2. Die GitHub URL klonen und eingeben
3. Projekt erstellen
4. Spring Application starten\
              1. [xampp](https://www.apachefriends.org/de/download.html) herunterladen & installieren\
              2. Die Module "Apache" & "MySQL" starten\
              3. in der MySQL-Instanz Datenbank "wizard" erstellen\
              4. Terminal öffnen und in den Ordner des Repos navigieren oder in IntelliJ Terminal öffnen\
              5. Server starten mit dem Befehl **./mvnw spring-boot:run**\
              6. sollte es hier zu einem JAVA_HOME not found Error kommen: File -> Settings -> Tools -> Terminal Environment Variable eintragen: 
              JAVA_HOME=**"JDK-Pfad"**
5. Build Project (Strg + F9) 
6. Run Application (WizardMainApplication) (Strg + Umschalt + F10)
7. Viel Spaß

