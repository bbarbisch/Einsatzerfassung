# Einsatzerfassung

Die Einsatzerfassung ermöglicht es für Einsatzdaten eine chronologisch geordnete Ordnerstruktur im Dateisystem zu erstellen um dort einem Einsatz zugehörige Daten (Protokolle, Bilder,...) abzulegen. Zusätzlich werden alle Inhalte aus einem definierten Vorlagen-Ordner nach dem Anlegen der Ordnerstruktur in diese kopiert.


# Erzeugte Ordnerstruktur
Die erzeugte Ordnerstruktur hat folgendes format
* Zielverzeichnis
  * Jahr
    * Ereignisverzeichnis
      * Datei/Verzeichnisstruktur aus Vorlagenordner

## Erleuterung
* Zielverzeichnis:
  * Das Verzeichnis in welches bestimmte Ereignistypen abgelegt werden
  * Wird in der Konfiguration definiert
  * Es können mehrere Zielverzeichnisse angegeben werden
* Jahr
  * Das Jahr in welchem das Ereignis stattfindet
  * Format des Orndernamens: 'YYYY' 
* Ereignisverzeichnis
  * Das Verzeichnis des jeweiligen Ereignisses
  * In das Verzeichnis werden alle Inhalte des Vorlagenverzeichnisses hineinkopiert
  * Format des Ordnernamens: 'Datum - Ereignistyp - Beschreibung'
* Datum
  * Das Datum des jeweiligen Ereignisses
  * Wird im Tool ausgewählt
  * Format der Ausgabe: 'YYYYmmdd', bspw. 20210521
* Ereignistyp
  * Der Typ es Ereignisses
  * bspw. Lawineneinsatz, Sucheinsatz, Übung,...
  * Wird im Tool ausgewählt 
* Beschreibung
  * Eine Kurzbeschreibung (wenige Worte) des Ereignisses
  * Wird im Tool eingegeben
 
## Beispielordnerstruktur
* Wurzelverzeichnis
  * 2005
    * Einsätze
      * 20050513-Sucheinsatz-aaa bbb ccc
      * 20050705-Bergeeinsatz-ddd eee fff
    * Übungen
      * 20051026-Übung-aaa bbb ccc
  * 2010
    * Übungen
      * 20100107-Übung-aaa bbb ccc
  * 2015
    * Einsätze
      * 20150706-Sucheinsatz-aaa bbb ccc
# Konfiguration des Tools
Die Konfigurationsdatei hat folgende Struktur:
```xml
<config>
	<templateDir></templateDir>
	<targetPaths>
		<targetPath path="">
			<type></type>
		</targetPath>
	</targetPaths>
</config>
```
## Konfigurationselemente
### templateDir
Der Wert definiert den Pfad zum Vorlagenverzeichnis.

### targetPath
Das Attribut *path* definiert den Pfad zum Ordner zu welchem die in *type* definierten Ereignistypen gehören.
Es können mehrere targetPath Elemente angegeben werden.

### type
Definiert einen Ereignistyp (bspw. Sucheinsatz, Übung,...). Ereignistypen müssen eindeudig sein, es dürfen nicht die selben Typen für mehrere targetPaths verwendet werden (Dies führt zu einem Fehler).

## Beispielkonfiguration
```xml
<config>
	<templateDir>C:\Pfad\zum\Vorlagenverzeichnis</templateDir>
	<targetPaths>
		<targetPath path="C:\Pfad\zum\Einsatzverzeichnis">
			<type>Sucheinsatz</type>
			<type>Lawineneinsatz</type>
			<type>Bergeeinsatz</type>
		</targetPath>
		<targetPath path="C:\Pfad\zum\Übungsverzeichnis">
			<type>Übung</type>
		</targetPath>
	</targetPaths>
</config>
```
# Installation
Um das Tool zu installieren, müssen folgende Schritte ausgeführt werden:
1. Ordner erstellen in welchem die Ausführbare Datei liegt
2. Erzeugte oder heruntergeladene jar Datei in diesem Ordner ablegen
3. jar Datei ausführen
   * java -jar \<pfad zur datei\>
   * Doppelklick
   * Beim erstmaligen Ausführen wird eine leere Konfigurationsdatei im selben Ordner erstellt und das tool anschließend beendet
4. Konfigurationsdatei in einem Texteditor der Wahl (notepad,...) öffnen und die Konfiguration entsprechend den Anforderungen editieren

# Systemvoraussetzungen
Folgende Voraussetzungen müssen zutreffen um das Tool auszuführen:
* mindestens Java 11

# Download
Derzeit wird kein Download angeboten, das Projekt muss selber gebaut werden.

# Lizenz
GPL V3