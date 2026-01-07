# 📘 FUNKTIONALE ANLEITUNG - TELEMETRIE UND ANALYTIK

**Version:** 1.0
**Datum:** Dezember 2025
**Zielgruppe:** Endbenutzer, DevOps, Datenanalysten, Projektmanager

---

## 🎯 WAS IST TELEMETRIE UND ANALYTIK?

Telemetrie und Analytik ist ein System zur **Überwachung und Analyse von Ereignissen**, die von AI OS (Artificial Intelligence Operating System) Komponenten generiert werden. Das Ziel ist es, vollständige Transparenz über das Verhalten, die Leistung und Nutzung von KI-Systemen in der Produktion zu bieten.

### 🎯 Hauptzweck

Das Telemetrie-System ermöglicht:

1. **Echtzeitüberwachung:** Visualisierung von Ereignissen und Metriken von KI-Komponenten
2. **Leistungsanalyse:** Aggregierte KPIs zu Latenz, Kosten, Tokens und Ereignissen
3. **Erweiterte Suche:** Ereignissuche nach mehreren Kriterien (Inhalt, Komponente, Projekt, Daten)
4. **Komponentenanalyse:** Detaillierte Statistiken pro KI-Komponente
5. **Zeitliche Analyse:** Visualisierung monatlicher Metriken und Trends
6. **Governance:** Erkennung von Bias, Toxizität, PII und Geheimnissen in Ereignissen

---

## 🚀 WOFÜR WIRD TELEMETRIE VERWENDET?

### 1. **Für Betriebsüberwachung**

- **Vollständige Transparenz:** Alle von KI-Komponenten generierten Ereignisse anzeigen
- **Aggregierte KPIs:** Konsolidierte Metriken (Gesamtereignisse, Kosten, Tokens, durchschnittliche Latenz)
- **Komponentenanalyse:** Leistung zwischen verschiedenen Komponenten vergleichen
- **Problemerkennung:** Komponenten mit hoher Latenz oder erhöhten Kosten identifizieren

### 2. **Für Kostensenkung**

- **Kostenanalyse:** Gesamtkosten und pro Komponente visualisieren
- **Token-Nutzung:** Token-Verbrauch pro Komponente überwachen
- **Chancenerkennung:** Komponenten mit ungewöhnlich hohen Kosten erkennen
- **Trends:** Kostenentwicklung über die Zeit analysieren

### 3. **Für Leistungsanalyse**

- **Latenz:** Antwortzeiten von Komponenten überwachen
- **Durchsatz:** Volumen verarbeiteter Ereignisse analysieren
- **Vergleich:** Leistung zwischen Komponenten vergleichen
- **Trends:** Leistungsverschlechterung über die Zeit identifizieren

### 4. **Für Governance und Compliance**

- **Bias-Erkennung:** Ereignisse mit erkannten Verzerrungen identifizieren
- **Toxizität:** Toxische Inhalte in Ereignissen erkennen
- **PII (Personenbezogene Daten):** Personendaten in Ereignissen identifizieren
- **Geheimnisse:** Geheimnisse oder offengelegte Anmeldedaten in Ereignissen erkennen
- **Automatische Kategorisierung:** Das System berechnet automatisch:
  - **Compliance-Status (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Risikoniveau (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
  - **Compliance-Kategorie (complianceCategory):** GDPR, SECURITY, LEGAL
  - **Problem-Tags (issueTags):** bias_critical, pii_exposure, secret_leak, toxicity_high, etc.

### 5. **Für Suche und Audit**

- **Inhaltssuche:** Nach Ereignissen mit spezifischem Text in der Nutzlast suchen
- **Komponentensuche:** Ereignisse nach spezifischer Komponente filtern
- **Projektsuche:** Ereignisse aller Komponenten eines Projekts anzeigen
- **Zeitfilter:** Ereignisse in spezifischen Datumsbereichen analysieren

---

## 📊 HAUPTKOMPONENTEN DER TELEMETRIE

### 1. **Telemetrie-Dashboard**

Zentrale Ansicht mit:
- **Haupt-KPIs:** Gesamtereignisse, Gesamtkosten, Gesamttokens, durchschnittliche Latenz, Anzahl der Komponenten
- **Datumsfilter:** Datumsbereich für Analyse auswählen
- **Statistiken nach Komponente:** Tabelle mit aggregierten Metriken pro Komponente
- **Diagramme:** Visualisierung von Trends und Verteilungen
- **Navigation:** Schnellzugriff auf Suche und monatliche Analyse

### 2. **Ereignissuche**

Vollständiges System zum Suchen und Filtern von Ereignissen:
- **Inhaltssuche:** Text in Nutzlast und Metriken suchen
- **Mehrfachfilter:** Nach Komponenten-UUID, Agent, Ereignistyp, Schweregrad
- **Zeitfilter:** Start- und Enddatumsbereich
- **Governance-Filter:** Bias, Toxizität, PII, erkannte Geheimnisse
- **Compliance/Sicherheitsfilter:**
  - **Compliance-Status:** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Risikoniveau:** LOW, MEDIUM, HIGH, CRITICAL
  - **Compliance-Kategorie:** GDPR, SECURITY, LEGAL
  - **Problem-Tags:** Nach spezifischen Tags filtern (bias_critical, pii_exposure, etc.)
- **Seitenaufteilung:** Navigation durch Ergebnis-Seiten
- **Detaillierte Visualisierung:** Vollständige Detailseite für jedes Ereignis mit vollständiger Nutzlast, Metriken und Analyse

### 3. **Monatliche Analyse**

Zeitliches Analyse-Dashboard:
- **Monatliche Metriken:** Gesamtereignisse, Kosten, Tokens, durchschnittliche Latenz pro Monat
- **Trend-Diagramme:** Visualisierung der monatlichen Entwicklung
- **Jahresauswahl:** Verschiedene Jahre analysieren
- **Vergleich:** Metriken zwischen Monaten vergleichen

### 4. **Statistiken nach Komponente**

Detaillierte Analyse pro Komponente:
- **Aggregierte Metriken:** Gesamtereignisse, durchschnittliche Latenz, Gesamttokens, Gesamtkosten
- **Vergleich:** Alle Komponenten in einer Tabelle anzeigen
- **Zeitfilter:** Komponenten in spezifischen Datumsbereichen analysieren

---

## 🔄 TYPISCHER ARBEITSABLAUF

### Szenario 1: Tägliche Überwachung

1. **Dashboard-Zugriff**
   - Navigieren zu Governance → Telemetrie → Dashboard
   - Haupt-KPIs für den Zeitraum anzeigen (letzte 30 Tage standardmäßig)

2. **Komponentenanalyse**
   - Komponentenstatistik-Tabelle überprüfen
   - Komponenten mit hoher Latenz oder erhöhten Kosten identifizieren
   - Leistung zwischen Komponenten vergleichen

3. **Trendanalyse**
   - Verteilungsdiagramme überprüfen
   - Muster oder Anomalien identifizieren

### Szenario 2: Problemanalyse

1. **Problemidentifikation**
   - Komponente mit anomalen Metriken im Dashboard erkennen
   - Oder Warnung zu spezifischem Ereignis erhalten

2. **Ereignissuche**
   - Zur Suchseite gehen
   - Nach Komponenten-UUID oder Projekt filtern
   - Datumsfilter für den Problemzeitraum anwenden
   - Nach bekanntem spezifischem Inhalt suchen

3. **Ereignisanalyse**
   - Gefundene Ereignisse überprüfen
   - Zur Detailseite relevanter Ereignisse navigieren
   - Vollständige Nutzlast, Metriken und Analyseergebnisse anzeigen
   - Automatisch berechnete Compliance/Sicherheitsfelder überprüfen:
     - Compliance-Status und Risikoniveau
     - Compliance-Kategorie und Problem-Tags
   - Governance-Erkennungen verifizieren (Bias, Toxizität, PII, Geheimnisse)

4. **Entscheidungsfindung**
   - Ursache des Problems identifizieren
   - Notwendige Korrekturmaßnahmen bestimmen

### Szenario 3: Kostenanalyse

1. **Gesamtkostenanalyse**
   - Zum Dashboard gehen
   - Gesamtkosten-KPI anzeigen
   - Datumsbereich bei Bedarf anpassen

2. **Analyse nach Komponente**
   - Komponentenstatistik-Tabelle überprüfen
   - Komponenten mit höchsten Kosten identifizieren
   - Kosten/Tokens/Ereignisse-Beziehung analysieren

3. **Zeitliche Analyse**
   - Zur monatlichen Analyse gehen
   - Monat-zu-Monat Kostenentwicklung anzeigen
   - Trends und Spitzen identifizieren

4. **Optimierung**
   - Optimierungsmöglichkeiten identifizieren
   - Kosten zwischen ähnlichen Komponenten vergleichen
   - Kostenreduktionsmaßnahmen planen

### Szenario 4: Governance- und Compliance-Audit

1. **Suche nach Ereignissen mit Problemen**
   - Zur Suchseite gehen
   - Compliance/Sicherheitsfilter verwenden:
     - **Compliance-Status:** VIOLATION oder CRITICAL_VIOLATION
     - **Risikoniveau:** HIGH oder CRITICAL
     - **Kategorie:** GDPR, SECURITY oder LEGAL je nach Bedarf
     - **Tags:** Nach spezifischen Tags filtern (pii_exposure, secret_leak, bias_critical, etc.)
   - Oder traditionelle Governance-Filter verwenden:
     - Bias erkannt
     - Toxizität erkannt
     - PII erkannt
     - Geheimnisse erkannt

2. **Ereignisanalyse**
   - Ereignisse überprüfen, die Erkennungen auslösten
   - Zur Detailseite jedes Ereignisses navigieren
   - Vollständige Nutzlast, Metriken und Analyseergebnisse anzeigen
   - Automatisch berechnete Felder überprüfen (complianceStatus, riskLevel, complianceCategory, issueTags)

3. **Dokumentation**
   - Relevante Ereignisse exportieren oder dokumentieren
   - Bei Bedarf Berichte generieren
   - Compliance/Sicherheitsfelder für Klassifizierung und Priorisierung verwenden

---

## 📈 METRIKEN UND KPIS

### Haupt-KPIs

1. **Gesamtereignisse**
   - Gesamtanzahl der Telemetrieereignisse im Zeitraum
   - Indikator für Aktivitätsvolumen

2. **Gesamtkosten (USD)**
   - Summe der Kosten aller Ereignisse
   - Berechnet aus Kostenmetriken pro Ereignis

3. **Gesamttokens**
   - Summe der in allen Ereignissen verbrauchten Tokens
   - Indikator für KI-Modellnutzung

4. **Durchschnittliche Latenz (ms)**
   - Durchschnittliche Antwortzeit der Komponenten
   - Leistungsindikator

5. **Anzahl der Komponenten**
   - Anzahl eindeutiger Komponenten, die Ereignisse generierten
   - Abdeckungsindikator

### Metriken pro Komponente

Für jede Komponente werden folgende Werte berechnet:
- **Gesamtereignisse:** Anzahl generierter Ereignisse
- **Durchschnittliche Latenz:** Durchschnittliche Antwortzeit
- **Gesamttokens:** Summe verbrauchter Tokens
- **Gesamtkosten:** Summe der Kosten in USD

---

## 🔍 SUCHFUNKTIONEN

### Verfügbare Suchkriterien

1. **Inhaltssuche**
   - Sucht Text in JSONB-Nutzlast und Metriken
   - Nützlich zum Finden spezifischer Ereignisse

2. **Filter nach Komponenten-UUID**
   - Ereignisse einer spezifischen Komponente filtern
   - Komponente nach UUID identifizieren

3. **Filter nach Agent**
   - Nach `agentExternalId` filtern
   - Nützlich zum Verfolgen von Ereignissen spezifischer Agenten

4. **Filter nach Ereignistyp**
   - Nach `eventType` filtern (z.B. INTERACTION_COMPLETED, ERROR, etc.)

5. **Filter nach Schweregrad**
   - INFO, WARN, ERROR, DEBUG
   - Nützlich zum Finden von Fehlerereignissen

6. **Zeitfilter**
   - Startdatum
   - Enddatum
   - Ermöglicht Analyse in spezifischen Bereichen

7. **Governance-Filter**
   - **Bias geprüft:** Ereignisse, bei denen Bias geprüft wurde
   - **Toxizität geprüft:** Ereignisse, bei denen Toxizität geprüft wurde
   - **PII erkannt:** Ereignisse mit erkannten Personendaten
   - **Geheimnis erkannt:** Ereignisse mit erkannten Geheimnissen

8. **Compliance/Sicherheitsfilter**
   - **Compliance-Status (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
   - **Risikoniveau (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
   - **Compliance-Kategorie (complianceCategory):** GDPR, SECURITY, LEGAL
   - **Problem-Tag (issueTag):** bias_critical, bias_high, bias_medium, pii_exposure, secret_leak, toxicity_critical, toxicity_high, toxicity_medium, compliance_issue

### Filterkombination

Alle Filter können für präzise Suchen kombiniert werden:
- Beispiel: Ereignisse einer spezifischen Komponente in einem Datumsbereich mit erkanntem PII
- Beispiel: Fehlerereignisse eines Projekts im letzten Monat

---

## 📊 VISUALISIERUNGEN

### Haupt-Dashboard

1. **KPI-Karten**
   - 5 Karten mit Hauptmetriken
   - Symbole und Farben zur schnellen Identifikation

2. **Ereignisverteilungsdiagramm**
   - Balkendiagramm mit Ereignisverteilung nach Komponente
   - Ermöglicht Identifikation der aktivsten Komponenten

3. **Kostendiagramm nach Komponente**
   - Balkendiagramm mit Kosten nach Komponente
   - Nützlich für Kostenanalyse

4. **Komponentenstatistik-Tabelle**
   - Vollständige Tabelle mit allen Metriken pro Komponente
   - Sortierbar und filterbar

### Monatliche Analyse

1. **Monatliches Ereignisdiagramm**
   - Zeitlinie mit Gesamtereignissen pro Monat
   - Ermöglicht Anzeigen von Trends

2. **Monatliches Kostendiagramm**
   - Zeitlinie mit Gesamtkosten pro Monat
   - Nützlich für Finanzanalyse

3. **Monatliches Token-Diagramm**
   - Zeitlinie mit Gesamttokens pro Monat
   - Nutzungsindikator

4. **Monatliches Durchschnittslatenz-Diagramm**
   - Zeitlinie mit durchschnittlicher Latenz pro Monat
   - Ermöglicht Erkennung von Leistungsverschlechterung

---

## 🎯 HÄUFIGE ANWENDUNGSFÄLLE

### Fall 1: "Wie viel kostet mein KI-System diesen Monat?"

1. Zum Telemetrie-Dashboard gehen
2. Datumsfilter auf aktuellen Monat anpassen
3. "Gesamtkosten (USD)" KPI anzeigen

### Fall 2: "Welche Komponente hat die höchste Latenz?"

1. Zum Telemetrie-Dashboard gehen
2. Komponentenstatistik-Tabelle überprüfen
3. Nach durchschnittlicher Latenz sortieren (absteigend)
4. Komponente mit höchster Latenz identifizieren

### Fall 3: "Alle Fehler des letzten Tages suchen"

1. Zur Ereignissuche gehen
2. Schweregrad "ERROR" auswählen
3. Startdatum festlegen: gestern
4. Enddatum festlegen: heute
5. Auf "Suchen" klicken

### Fall 4: "Trends des letzten Jahres analysieren"

1. Zur monatlichen Analyse gehen
2. Aktuelles Jahr auswählen
3. Trend-Diagramme überprüfen
4. Monate vergleichen

### Fall 5: "Ereignisse mit Personendaten finden"

1. Zur Ereignissuche gehen
2. Filter "Compliance-Kategorie" = "GDPR" verwenden oder "PII" Checkbox aktivieren
3. Datumsbereich festlegen
4. Auf "Suchen" klicken
5. Gefundene Ereignisse überprüfen
6. Zur Detailseite relevanter Ereignisse navigieren, um vollständige Informationen anzuzeigen

### Fall 6: "Kritische Sicherheitsereignisse identifizieren"

1. Zur Ereignissuche gehen
2. Filter festlegen:
   - **Compliance-Status:** CRITICAL_VIOLATION
   - **Risikoniveau:** CRITICAL
   - **Compliance-Kategorie:** SECURITY
3. Datumsbereich festlegen
4. Auf "Suchen" klicken
5. Gefundene Ereignisse überprüfen
6. Zur Detailseite für vollständige Analyse navigieren

---

## 🔐 SICHERHEITS- UND DATENSCHUTZÜBERLEGUNGEN

### Sensible Daten

- Ereignisse können sensible Informationen in Nutzlasten enthalten
- Das System erkennt automatisch PII und Geheimnisse
- Es wird empfohlen, Ereignisse mit Erkennungen vor der Weitergabe zu überprüfen

### Datenzugriff

- Nur autorisierte Benutzer können auf Telemetrie zugreifen
- Daten sind durch Authentifizierung und Autorisierung geschützt
- Es wird empfohlen, den Zugriff auf sensible Daten zu beschränken

---

## 📚 REFERENZEN

### Verwandte Bildschirme

- **Dashboard:** `/governance/telemetry/dashboard`
- **Suche:** `/governance/telemetry/search`
- **Monatliche Analyse:** `/governance/telemetry/analytics`
- **Ereignisdetail:** `/governance/telemetry/events/{id}` (vollständige Detailseite)

### Technische Dokumentation

- Siehe Entwickleranleitungen für technische Details
- Siehe Bildschirmnutzungsanleitung für Schritt-für-Schritt-Anweisungen

---

**Letzte Aktualisierung:** Dezember 2025

**Letzte Änderungen:**
- Automatisch berechnete Compliance/Sicherheitsfelder hinzugefügt (complianceStatus, riskLevel, complianceCategory, issueTags)
- Neue Suchfilter nach Compliance/Sicherheit
- Separate Detailseite zur vollständigen Ereignisvisualisierung
- Verbesserungen in Mock-Daten mit realistischen Variationen nach Monat und Jahr
