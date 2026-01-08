# 📘 GUIDA FUNZIONALE - TELEMETRIA E ANALISI

**Versione:** 1.0
**Data:** Dicembre 2025
**Destinatari:** Utenti finali, DevOps, Analisti di Dati, Project Manager

---

## 🎯 COS'È LA TELEMETRIA E ANALISI?

La Telemetria e Analisi è un sistema per il **monitoraggio e l'analisi di eventi** generati dai componenti di AI OS (Artificial Intelligence Operating System). Il suo obiettivo è fornire visibilità completa sul comportamento, le prestazioni e l'uso dei sistemi di IA in produzione.

### 🎯 Scopo Principale

Il sistema di Telemetria consente:

1. **Monitoraggio in Tempo Reale:** Visualizzazione di eventi e metriche dai componenti di IA
2. **Analisi delle Prestazioni:** KPI aggregati su latenza, costi, token ed eventi
3. **Ricerca Avanzata:** Ricerca di eventi per più criteri (contenuto, componente, progetto, date)
4. **Analisi dei Componenti:** Statistiche dettagliate per componente di IA
5. **Analisi Temporale:** Visualizzazione di metriche mensili e tendenze
6. **Governance:** Rilevamento di bias, tossicità, PII e segreti negli eventi

---

## 🚀 A COSA SERVE LA TELEMETRIA?

### 1. **Per il Monitoraggio Operativo**

- **Visibilità Completa:** Visualizzare tutti gli eventi generati dai componenti di IA
- **KPI Aggregati:** Metriche consolidate (eventi totali, costi, token, latenza media)
- **Analisi dei Componenti:** Confrontare le prestazioni tra diversi componenti
- **Rilevamento Problemi:** Identificare componenti con alta latenza o costi elevati

### 2. **Per l'Ottimizzazione dei Costi**

- **Analisi dei Costi:** Visualizzare costi totali e per componente
- **Uso dei Token:** Monitorare il consumo di token per componente
- **Identificazione Opportunità:** Rilevare componenti con costi insolitamente alti
- **Tendenze:** Analizzare l'evoluzione dei costi nel tempo

### 3. **Per l'Analisi delle Prestazioni**

- **Latenza:** Monitorare i tempi di risposta dei componenti
- **Throughput:** Analizzare il volume di eventi elaborati
- **Confronto:** Confrontare le prestazioni tra componenti
- **Tendenze:** Identificare il degrado delle prestazioni nel tempo

### 4. **Per Governance e Compliance**

- **Rilevamento Bias:** Identificare eventi con bias rilevati
- **Tossicità:** Rilevare contenuti tossici negli eventi
- **PII (Informazioni Identificative Personali):** Identificare dati personali negli eventi
- **Segreti:** Rilevare segreti o credenziali esposte negli eventi
- **Categorizzazione Automatica:** Il sistema calcola automaticamente:
  - **Stato di Conformità (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Livello di Rischio (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
  - **Categoria di Conformità (complianceCategory):** GDPR, SECURITY, LEGAL
  - **Tag dei Problemi (issueTags):** bias_critical, pii_exposure, secret_leak, toxicity_high, ecc.

### 5. **Per Ricerca e Audit**

- **Ricerca per Contenuto:** Cercare eventi contenenti testo specifico nel payload
- **Ricerca per Componente:** Filtrare eventi per componente specifico
- **Ricerca per Progetto:** Visualizzare eventi di tutti i componenti di un progetto
- **Filtri Temporali:** Analizzare eventi in intervalli di date specifici

---

## 📊 COMPONENTI PRINCIPALI DELLA TELEMETRIA

### 1. **Dashboard di Telemetria**

Vista centralizzata che mostra:
- **KPI Principali:** Eventi totali, costi totali, token totali, latenza media, numero di componenti
- **Filtri di Data:** Selezionare intervallo di date per l'analisi
- **Statistiche per Componente:** Tabella con metriche aggregate per componente
- **Grafici:** Visualizzazione di tendenze e distribuzioni
- **Navigazione:** Accesso rapido a ricerca e analisi mensile

### 2. **Ricerca Eventi**

Sistema completo per cercare e filtrare eventi:
- **Ricerca per Contenuto:** Cercare testo nel payload e nelle metriche
- **Filtri Multipli:** Per UUID componente, agente, tipo di evento, severità
- **Filtri Temporali:** Intervallo di date di inizio e fine
- **Filtri di Governance:** Bias, tossicità, PII, segreti rilevati
- **Filtri di Compliance/Sicurezza:**
  - **Stato di Conformità:** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Livello di Rischio:** LOW, MEDIUM, HIGH, CRITICAL
  - **Categoria di Conformità:** GDPR, SECURITY, LEGAL
  - **Tag dei Problemi:** Filtrare per tag specifici (bias_critical, pii_exposure, ecc.)
- **Paginazione:** Navigazione attraverso le pagine dei risultati
- **Visualizzazione Dettagliata:** Pagina di dettaglio completa per ogni evento con payload completo, metriche e analisi

### 3. **Analisi Mensile**

Dashboard di analisi temporale:
- **Metriche Mensili:** Eventi totali, costi, token, latenza media per mese
- **Grafici delle Tendenze:** Visualizzazione dell'evoluzione mensile
- **Selettore Anno:** Analizzare diversi anni
- **Confronto:** Confrontare metriche tra mesi

### 4. **Statistiche per Componente**

Analisi dettagliata per componente:
- **Metriche Aggregate:** Eventi totali, latenza media, token totali, costi totali
- **Confronto:** Visualizzare tutti i componenti in una tabella
- **Filtri Temporali:** Analizzare componenti in intervalli di date specifici

---

## 🔄 FLUSSO DI LAVORO TIPICO

### Scenario 1: Monitoraggio Giornaliero

1. **Accesso al Dashboard**
   - Navigare a Governance → Telemetria → Dashboard
   - Visualizzare KPI principali del periodo (ultimi 30 giorni di default)

2. **Analisi dei Componenti**
   - Rivedere tabella delle statistiche per componente
   - Identificare componenti con alta latenza o costi elevati
   - Confrontare prestazioni tra componenti

3. **Analisi delle Tendenze**
   - Rivedere grafici di distribuzione
   - Identificare pattern o anomalie

### Scenario 2: Indagine Problemi

1. **Identificazione del Problema**
   - Rilevare componente con metriche anomale nel dashboard
   - O ricevere allerta su evento specifico

2. **Ricerca Eventi**
   - Andare alla pagina di Ricerca
   - Filtrare per UUID componente o progetto
   - Applicare filtri di data per il periodo del problema
   - Cercare per contenuto specifico se noto

3. **Analisi Eventi**
   - Rivedere eventi trovati
   - Navigare alla pagina di dettaglio degli eventi rilevanti
   - Visualizzare payload completo, metriche e risultati dell'analisi
   - Rivedere campi di compliance/sicurezza calcolati automaticamente:
     - Stato di conformità e livello di rischio
     - Categoria di conformità e tag dei problemi
   - Verificare rilevamenti di governance (bias, tossicità, PII, segreti)

4. **Decision Making**
   - Identificare causa principale del problema
   - Determinare azioni correttive necessarie

### Scenario 3: Analisi dei Costi

1. **Analisi Costi Totali**
   - Andare al Dashboard
   - Visualizzare KPI costi totali
   - Regolare intervallo di date se necessario

2. **Analisi per Componente**
   - Rivedere tabella delle statistiche per componente
   - Identificare componenti con costo maggiore
   - Analizzare relazione costo/token/eventi

3. **Analisi Temporale**
   - Andare ad Analisi Mensile
   - Visualizzare evoluzione costi mese per mese
   - Identificare tendenze e picchi

4. **Ottimizzazione**
   - Identificare opportunità di ottimizzazione
   - Confrontare costi tra componenti simili
   - Pianificare azioni di riduzione costi

### Scenario 4: Audit Governance e Compliance

1. **Ricerca Eventi con Problemi**
   - Andare alla pagina di Ricerca
   - Usare filtri di compliance/sicurezza:
     - **Stato di Conformità:** VIOLATION o CRITICAL_VIOLATION
     - **Livello di Rischio:** HIGH o CRITICAL
     - **Categoria:** GDPR, SECURITY o LEGAL secondo necessità
     - **Tag:** Filtrare per tag specifici (pii_exposure, secret_leak, bias_critical, ecc.)
   - O usare filtri di governance tradizionali:
     - Bias rilevato
     - Tossicità rilevata
     - PII rilevato
     - Segreti rilevati

2. **Analisi Eventi**
   - Rivedere eventi che hanno attivato rilevamenti
   - Navigare alla pagina di dettaglio di ogni evento
   - Visualizzare payload completo, metriche e risultati dell'analisi
   - Rivedere campi calcolati automaticamente (complianceStatus, riskLevel, complianceCategory, issueTags)

3. **Documentazione**
   - Esportare o documentare eventi rilevanti
   - Generare report se necessario
   - Usare campi di compliance/sicurezza per classificazione e priorizzazione

---

## 📈 METRICHE E KPI

### KPI Principali

1. **Eventi Totali**
   - Numero totale di eventi di telemetria nel periodo
   - Indicatore del volume di attività

2. **Costo Totale (USD)**
   - Somma dei costi di tutti gli eventi
   - Calcolato dalle metriche di costo per evento

3. **Token Totali**
   - Somma dei token consumati in tutti gli eventi
   - Indicatore dell'uso di modelli di IA

4. **Latenza Media (ms)**
   - Tempo medio di risposta dei componenti
   - Indicatore di prestazioni

5. **Numero di Componenti**
   - Quantità di componenti unici che hanno generato eventi
   - Indicatore di copertura

### Metriche per Componente

Per ogni componente vengono calcolati:
- **Eventi Totali:** Numero di eventi generati
- **Latenza Media:** Tempo medio di risposta
- **Token Totali:** Somma dei token consumati
- **Costo Totale:** Somma dei costi in USD

---

## 🔍 CAPACITÀ DI RICERCA

### Criteri di Ricerca Disponibili

1. **Ricerca per Contenuto**
   - Cerca testo nel payload JSONB e nelle metriche
   - Utile per trovare eventi specifici

2. **Filtro per UUID Componente**
   - Filtrare eventi di un componente specifico
   - Identificare componente per UUID

3. **Filtro per Agente**
   - Filtrare per `agentExternalId`
   - Utile per tracciare eventi di agenti specifici

4. **Filtro per Tipo di Evento**
   - Filtrare per `eventType` (es: INTERACTION_COMPLETED, ERROR, ecc.)

5. **Filtro per Severità**
   - INFO, WARN, ERROR, DEBUG
   - Utile per trovare eventi di errore

6. **Filtri Temporali**
   - Data di inizio
   - Data di fine
   - Consente analisi in intervalli specifici

7. **Filtri di Governance**
   - **Bias Verificato:** Eventi dove è stato verificato il bias
   - **Tossicità Verificata:** Eventi dove è stata verificata la tossicità
   - **PII Rilevato:** Eventi con dati personali rilevati
   - **Segreto Rilevato:** Eventi con segreti rilevati

8. **Filtri di Compliance/Sicurezza**
   - **Stato di Conformità (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
   - **Livello di Rischio (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
   - **Categoria di Conformità (complianceCategory):** GDPR, SECURITY, LEGAL
   - **Tag del Problema (issueTag):** bias_critical, bias_high, bias_medium, pii_exposure, secret_leak, toxicity_critical, toxicity_high, toxicity_medium, compliance_issue

### Combinazione di Filtri

Tutti i filtri possono essere combinati per ricerche precise:
- Esempio: Eventi di un componente specifico in un intervallo di date con PII rilevato
- Esempio: Eventi di errore di un progetto nell'ultimo mese

---

## 📊 VISUALIZZAZIONI

### Dashboard Principale

1. **Carte KPI**
   - 5 carte con metriche principali
   - Icone e colori per identificazione rapida

2. **Grafico di Distribuzione Eventi**
   - Grafico a barre che mostra distribuzione degli eventi per componente
   - Consente identificare componenti più attivi

3. **Grafico Costi per Componente**
   - Grafico a barre che mostra costi per componente
   - Utile per analisi dei costi

4. **Tabella Statistiche per Componente**
   - Tabella completa con tutte le metriche per componente
   - Ordinabile e filtrabile

### Analisi Mensile

1. **Grafico Eventi Mensili**
   - Timeline che mostra eventi totali per mese
   - Consente vedere tendenze

2. **Grafico Costi Mensili**
   - Timeline che mostra costi totali per mese
   - Utile per analisi finanziaria

3. **Grafico Token Mensili**
   - Timeline che mostra token totali per mese
   - Indicatore di uso

4. **Grafico Latenza Media Mensile**
   - Timeline che mostra latenza media per mese
   - Consente rilevare degrado delle prestazioni

---

## 🎯 CASI D'USO COMUNI

### Caso 1: "Quanto costa il mio sistema di IA questo mese?"

1. Andare al Dashboard di Telemetria
2. Regolare filtri di data al mese corrente
3. Visualizzare KPI "Costo Totale (USD)"

### Caso 2: "Quale componente ha la latenza maggiore?"

1. Andare al Dashboard di Telemetria
2. Rivedere tabella delle statistiche per componente
3. Ordinare per latenza media (discendente)
4. Identificare componente con latenza maggiore

### Caso 3: "Cercare tutti gli errori dell'ultimo giorno"

1. Andare alla Ricerca Eventi
2. Selezionare severità "ERROR"
3. Impostare data di inizio: ieri
4. Impostare data di fine: oggi
5. Cliccare su "Cerca"

### Caso 4: "Analizzare tendenze dell'ultimo anno"

1. Andare ad Analisi Mensile
2. Selezionare anno corrente
3. Rivedere grafici delle tendenze
4. Confrontare mesi

### Caso 5: "Trovare eventi con dati personali"

1. Andare alla Ricerca Eventi
2. Usare filtro "Categoria di Conformità" = "GDPR" o attivare checkbox "PII"
3. Impostare intervallo di date
4. Cliccare su "Cerca"
5. Rivedere eventi trovati
6. Navigare alla pagina di dettaglio degli eventi rilevanti per visualizzare informazioni complete

### Caso 6: "Identificare eventi critici di sicurezza"

1. Andare alla Ricerca Eventi
2. Impostare filtri:
   - **Stato di Conformità:** CRITICAL_VIOLATION
   - **Livello di Rischio:** CRITICAL
   - **Categoria di Conformità:** SECURITY
3. Impostare intervallo di date
4. Cliccare su "Cerca"
5. Rivedere eventi trovati
6. Navigare alla pagina di dettaglio per analisi completa

---

## 🔐 CONSIDERAZIONI SICUREZZA E PRIVACY

### Dati Sensibili

- Gli eventi possono contenere informazioni sensibili nei payload
- Il sistema rileva automaticamente PII e segreti
- Si raccomanda di rivedere eventi con rilevamenti prima della condivisione

### Accesso ai Dati

- Solo utenti autorizzati possono accedere alla telemetria
- I dati sono protetti da autenticazione e autorizzazione
- Si raccomanda di limitare l'accesso ai dati sensibili

---

## 📚 RIFERIMENTI

### Schermate Correlate

- **Dashboard:** `/governance/telemetry/dashboard`
- **Ricerca:** `/governance/telemetry/search`
- **Analisi Mensile:** `/governance/telemetry/analytics`
- **Dettaglio Evento:** `/governance/telemetry/events/{id}` (pagina di dettaglio completa)

### Documentazione Tecnica

- Vedere guide per sviluppatori per dettagli tecnici
- Vedere guida all'uso delle schermate per istruzioni passo-passo

---

**Ultimo Aggiornamento:** Dicembre 2025

**Modifiche Recenti:**
- Aggiunti campi di compliance/sicurezza calcolati automaticamente (complianceStatus, riskLevel, complianceCategory, issueTags)
- Nuovi filtri di ricerca per compliance/sicurezza
- Pagina di dettaglio separata per visualizzazione completa degli eventi
- Miglioramenti nei dati mock con variazioni realistiche per mese e anno
