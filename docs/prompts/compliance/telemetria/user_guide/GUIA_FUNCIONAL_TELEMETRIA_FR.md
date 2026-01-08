# 📘 GUIDE FONCTIONNEL - TÉLÉMÉTRIE ET ANALYTIQUE

**Version:** 1.0
**Date:** Décembre 2025
**Audience:** Utilisateurs finaux, DevOps, Analystes de Données, Chefs de Projet

---

## 🎯 QU'EST-CE QUE LA TÉLÉMÉTRIE ET ANALYTIQUE?

La Télémétrie et Analytique est un système de **surveillance et d'analyse d'événements** générés par les composants de AI OS (Artificial Intelligence Operating System). Son objectif est de fournir une visibilité complète sur le comportement, les performances et l'utilisation des systèmes d'IA en production.

### 🎯 Objectif Principal

Le système de Télémétrie permet:

1. **Surveillance en Temps Réel:** Visualisation d'événements et métriques des composants d'IA
2. **Analyse des Performances:** KPIs agrégés sur latence, coûts, tokens et événements
3. **Recherche Avancée:** Recherche d'événements par plusieurs critères (contenu, composant, projet, dates)
4. **Analyse des Composants:** Statistiques détaillées par composant d'IA
5. **Analyse Temporelle:** Visualisation de métriques mensuelles et tendances
6. **Gouvernance:** Détection de biais, toxicité, PII et secrets dans les événements

---

## 🚀 À QUOI SERT LA TÉLÉMÉTRIE?

### 1. **Pour la Surveillance Opérationnelle**

- **Visibilité Complète:** Voir tous les événements générés par les composants d'IA
- **KPIs Agrégés:** Métriques consolidées (total événements, coûts, tokens, latence moyenne)
- **Analyse des Composants:** Comparer les performances entre différents composants
- **Détection de Problèmes:** Identifier les composants avec latence élevée ou coûts élevés

### 2. **Pour l'Optimisation des Coûts**

- **Analyse des Coûts:** Visualiser les coûts totaux et par composant
- **Utilisation des Tokens:** Surveiller la consommation de tokens par composant
- **Identification d'Opportunités:** Détecter les composants avec coûts inhabituellement élevés
- **Tendances:** Analyser l'évolution des coûts dans le temps

### 3. **Pour l'Analyse des Performances**

- **Latence:** Surveiller les temps de réponse des composants
- **Throughput:** Analyser le volume d'événements traités
- **Comparaison:** Comparer les performances entre composants
- **Tendances:** Identifier la dégradation des performances dans le temps

### 4. **Pour la Gouvernance et la Conformité**

- **Détection de Biais:** Identifier les événements avec biais détectés
- **Toxicité:** Détecter le contenu toxique dans les événements
- **PII (Informations Personnellement Identifiables):** Identifier les données personnelles dans les événements
- **Secrets:** Détecter les secrets ou identifiants exposés dans les événements
- **Catégorisation Automatique:** Le système calcule automatiquement:
  - **Statut de Conformité (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Niveau de Risque (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
  - **Catégorie de Conformité (complianceCategory):** GDPR, SECURITY, LEGAL
  - **Tags de Problèmes (issueTags):** bias_critical, pii_exposure, secret_leak, toxicity_high, etc.

### 5. **Pour la Recherche et l'Audit**

- **Recherche par Contenu:** Rechercher des événements contenant du texte spécifique dans le payload
- **Recherche par Composant:** Filtrer les événements par composant spécifique
- **Recherche par Projet:** Voir les événements de tous les composants d'un projet
- **Filtres Temporels:** Analyser les événements dans des plages de dates spécifiques

---

## 📊 COMPOSANTS PRINCIPAUX DE LA TÉLÉMÉTRIE

### 1. **Tableau de Bord de Télémétrie**

Vue centralisée affichant:
- **KPIs Principaux:** Total événements, coûts totaux, tokens totaux, latence moyenne, nombre de composants
- **Filtres de Date:** Sélectionner une plage de dates pour l'analyse
- **Statistiques par Composant:** Tableau avec métriques agrégées par composant
- **Graphiques:** Visualisation de tendances et distributions
- **Navigation:** Accès rapide à la recherche et analyse mensuelle

### 2. **Recherche d'Événements**

Système complet pour rechercher et filtrer les événements:
- **Recherche par Contenu:** Rechercher du texte dans le payload et métriques
- **Filtres Multiples:** Par UUID de composant, agent, type d'événement, sévérité
- **Filtres Temporels:** Plage de dates de début et fin
- **Filtres de Gouvernance:** Biais, toxicité, PII, secrets détectés
- **Filtres de Conformité/Sécurité:**
  - **Statut de Conformité:** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Niveau de Risque:** LOW, MEDIUM, HIGH, CRITICAL
  - **Catégorie de Conformité:** GDPR, SECURITY, LEGAL
  - **Tags de Problèmes:** Filtrer par tags spécifiques (bias_critical, pii_exposure, etc.)
- **Pagination:** Navigation à travers les pages de résultats
- **Visualisation Détaillée:** Page de détail complète pour chaque événement avec payload complet, métriques et analyse

### 3. **Analyse Mensuelle**

Tableau de bord d'analyse temporelle:
- **Métriques Mensuelles:** Total événements, coûts, tokens, latence moyenne par mois
- **Graphiques de Tendances:** Visualisation de l'évolution mensuelle
- **Sélecteur d'Année:** Analyser différentes années
- **Comparaison:** Comparer les métriques entre mois

### 4. **Statistiques par Composant**

Analyse détaillée par composant:
- **Métriques Agrégées:** Total événements, latence moyenne, tokens totaux, coûts totaux
- **Comparaison:** Voir tous les composants dans un tableau
- **Filtres Temporels:** Analyser les composants dans des plages de dates spécifiques

---

## 🔄 FLUX DE TRAVAIL TYPIQUE

### Scénario 1: Surveillance Quotidienne

1. **Accès au Tableau de Bord**
   - Naviguer vers Gouvernance → Télémétrie → Tableau de Bord
   - Voir les KPIs principaux de la période (30 derniers jours par défaut)

2. **Analyse des Composants**
   - Réviser le tableau de statistiques par composant
   - Identifier les composants avec latence élevée ou coûts élevés
   - Comparer les performances entre composants

3. **Analyse des Tendances**
   - Réviser les graphiques de distribution
   - Identifier les modèles ou anomalies

### Scénario 2: Investigation de Problèmes

1. **Identification du Problème**
   - Détecter un composant avec métriques anormales dans le tableau de bord
   - Ou recevoir une alerte sur un événement spécifique

2. **Recherche d'Événements**
   - Aller à la page de Recherche
   - Filtrer par UUID de composant ou projet
   - Appliquer des filtres de date pour la période du problème
   - Rechercher par contenu spécifique si connu

3. **Analyse d'Événements**
   - Réviser les événements trouvés
   - Naviguer vers la page de détail des événements pertinents
   - Voir le payload complet, métriques et résultats de l'analyse
   - Réviser les champs de conformité/sécurité calculés automatiquement:
     - Statut de conformité et niveau de risque
     - Catégorie de conformité et tags de problèmes
   - Vérifier les détections de gouvernance (biais, toxicité, PII, secrets)

4. **Prise de Décision**
   - Identifier la cause racine du problème
   - Déterminer les actions correctives nécessaires

### Scénario 3: Analyse des Coûts

1. **Analyse des Coûts Totaux**
   - Aller au Tableau de Bord
   - Voir le KPI de coûts totaux
   - Ajuster la plage de dates si nécessaire

2. **Analyse par Composant**
   - Réviser le tableau de statistiques par composant
   - Identifier les composants avec le coût le plus élevé
   - Analyser la relation coût/tokens/événements

3. **Analyse Temporelle**
   - Aller à l'Analyse Mensuelle
   - Voir l'évolution des coûts mois par mois
   - Identifier les tendances et pics

4. **Optimisation**
   - Identifier les opportunités d'optimisation
   - Comparer les coûts entre composants similaires
   - Planifier des actions de réduction des coûts

### Scénario 4: Audit de Gouvernance et Conformité

1. **Recherche d'Événements avec Problèmes**
   - Aller à la page de Recherche
   - Utiliser les filtres de conformité/sécurité:
     - **Statut de Conformité:** VIOLATION ou CRITICAL_VIOLATION
     - **Niveau de Risque:** HIGH ou CRITICAL
     - **Catégorie:** GDPR, SECURITY ou LEGAL selon besoin
     - **Tags:** Filtrer par tags spécifiques (pii_exposure, secret_leak, bias_critical, etc.)
   - Ou utiliser les filtres de gouvernance traditionnels:
     - Biais détecté
     - Toxicité détectée
     - PII détecté
     - Secrets détectés

2. **Analyse d'Événements**
   - Réviser les événements qui ont déclenché des détections
   - Naviguer vers la page de détail de chaque événement
   - Voir le payload complet, métriques et résultats de l'analyse
   - Réviser les champs calculés automatiquement (complianceStatus, riskLevel, complianceCategory, issueTags)

3. **Documentation**
   - Exporter ou documenter les événements pertinents
   - Générer des rapports si nécessaire
   - Utiliser les champs de conformité/sécurité pour classification et priorisation

---

## 📈 MÉTRIQUES ET KPIS

### KPIs Principaux

1. **Total d'Événements**
   - Nombre total d'événements de télémétrie dans la période
   - Indicateur du volume d'activité

2. **Coût Total (USD)**
   - Somme des coûts de tous les événements
   - Calculé à partir des métriques de coût par événement

3. **Tokens Totaux**
   - Somme des tokens consommés dans tous les événements
   - Indicateur de l'utilisation des modèles d'IA

4. **Latence Moyenne (ms)**
   - Temps de réponse moyen des composants
   - Indicateur de performance

5. **Nombre de Composants**
   - Quantité de composants uniques qui ont généré des événements
   - Indicateur de couverture

### Métriques par Composant

Pour chaque composant sont calculés:
- **Total d'Événements:** Nombre d'événements générés
- **Latence Moyenne:** Temps de réponse moyen
- **Tokens Totaux:** Somme des tokens consommés
- **Coût Total:** Somme des coûts en USD

---

## 🔍 CAPACITÉS DE RECHERCHE

### Critères de Recherche Disponibles

1. **Recherche par Contenu**
   - Recherche du texte dans le payload JSONB et métriques
   - Utile pour trouver des événements spécifiques

2. **Filtre par UUID de Composant**
   - Filtrer les événements d'un composant spécifique
   - Identifier le composant par UUID

3. **Filtre par Agent**
   - Filtrer par `agentExternalId`
   - Utile pour suivre les événements d'agents spécifiques

4. **Filtre par Type d'Événement**
   - Filtrer par `eventType` (ex: INTERACTION_COMPLETED, ERROR, etc.)

5. **Filtre par Sévérité**
   - INFO, WARN, ERROR, DEBUG
   - Utile pour trouver les événements d'erreur

6. **Filtres Temporels**
   - Date de début
   - Date de fin
   - Permet l'analyse dans des plages spécifiques

7. **Filtres de Gouvernance**
   - **Biais Vérifié:** Événements où le biais a été vérifié
   - **Toxicité Vérifiée:** Événements où la toxicité a été vérifiée
   - **PII Détecté:** Événements avec données personnelles détectées
   - **Secret Détecté:** Événements avec secrets détectés

8. **Filtres de Conformité/Sécurité**
   - **Statut de Conformité (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
   - **Niveau de Risque (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
   - **Catégorie de Conformité (complianceCategory):** GDPR, SECURITY, LEGAL
   - **Tag de Problème (issueTag):** bias_critical, bias_high, bias_medium, pii_exposure, secret_leak, toxicity_critical, toxicity_high, toxicity_medium, compliance_issue

### Combinaison de Filtres

Tous les filtres peuvent être combinés pour des recherches précises:
- Exemple: Événements d'un composant spécifique dans une plage de dates avec PII détecté
- Exemple: Événements d'erreur d'un projet dans le dernier mois

---

## 📊 VISUALISATIONS

### Tableau de Bord Principal

1. **Cartes KPIs**
   - 5 cartes avec métriques principales
   - Icônes et couleurs pour identification rapide

2. **Graphique de Distribution d'Événements**
   - Graphique en barres montrant la distribution des événements par composant
   - Permet d'identifier les composants les plus actifs

3. **Graphique de Coûts par Composant**
   - Graphique en barres montrant les coûts par composant
   - Utile pour l'analyse des coûts

4. **Tableau de Statistiques par Composant**
   - Tableau complet avec toutes les métriques par composant
   - Triable et filtrable

### Analyse Mensuelle

1. **Graphique d'Événements Mensuels**
   - Timeline montrant le total d'événements par mois
   - Permet de voir les tendances

2. **Graphique de Coûts Mensuels**
   - Timeline montrant les coûts totaux par mois
   - Utile pour l'analyse financière

3. **Graphique de Tokens Mensuels**
   - Timeline montrant les tokens totaux par mois
   - Indicateur d'utilisation

4. **Graphique de Latence Moyenne Mensuelle**
   - Timeline montrant la latence moyenne par mois
   - Permet de détecter la dégradation des performances

---

## 🎯 CAS D'USAGE COURANTS

### Cas 1: "Combien coûte mon système d'IA ce mois-ci?"

1. Aller au Tableau de Bord de Télémétrie
2. Ajuster les filtres de date au mois actuel
3. Voir le KPI "Coût Total (USD)"

### Cas 2: "Quel composant a la latence la plus élevée?"

1. Aller au Tableau de Bord de Télémétrie
2. Réviser le tableau de statistiques par composant
3. Trier par latence moyenne (décroissant)
4. Identifier le composant avec la latence la plus élevée

### Cas 3: "Rechercher toutes les erreurs du dernier jour"

1. Aller à la Recherche d'Événements
2. Sélectionner la sévérité "ERROR"
3. Établir la date de début: hier
4. Établir la date de fin: aujourd'hui
5. Cliquer sur "Rechercher"

### Cas 4: "Analyser les tendances de l'année dernière"

1. Aller à l'Analyse Mensuelle
2. Sélectionner l'année actuelle
3. Réviser les graphiques de tendances
4. Comparer les mois

### Cas 5: "Trouver des événements avec données personnelles"

1. Aller à la Recherche d'Événements
2. Utiliser le filtre "Catégorie de Conformité" = "GDPR" ou activer la case "PII"
3. Établir la plage de dates
4. Cliquer sur "Rechercher"
5. Réviser les événements trouvés
6. Naviguer vers la page de détail des événements pertinents pour voir les informations complètes

### Cas 6: "Identifier les événements critiques de sécurité"

1. Aller à la Recherche d'Événements
2. Établir les filtres:
   - **Statut de Conformité:** CRITICAL_VIOLATION
   - **Niveau de Risque:** CRITICAL
   - **Catégorie de Conformité:** SECURITY
3. Établir la plage de dates
4. Cliquer sur "Rechercher"
5. Réviser les événements trouvés
6. Naviguer vers la page de détail pour analyse complète

---

## 🔐 CONSIDÉRATIONS DE SÉCURITÉ ET CONFIDENTIALITÉ

### Données Sensibles

- Les événements peuvent contenir des informations sensibles dans les payloads
- Le système détecte automatiquement PII et secrets
- Il est recommandé de réviser les événements avec détections avant partage

### Accès aux Données

- Seuls les utilisateurs autorisés peuvent accéder à la télémétrie
- Les données sont protégées par authentification et autorisation
- Il est recommandé de limiter l'accès aux données sensibles

---

## 📚 RÉFÉRENCES

### Écrans Connexes

- **Tableau de Bord:** `/governance/telemetry/dashboard`
- **Recherche:** `/governance/telemetry/search`
- **Analyse Mensuelle:** `/governance/telemetry/analytics`
- **Détail d'Événement:** `/governance/telemetry/events/{id}` (page de détail complète)

### Documentation Technique

- Voir les guides développeurs pour détails techniques
- Voir le guide d'utilisation des écrans pour instructions étape par étape

---

**Dernière Mise à Jour:** Décembre 2025

**Changements Récents:**
- Ajout de champs de conformité/sécurité calculés automatiquement (complianceStatus, riskLevel, complianceCategory, issueTags)
- Nouveaux filtres de recherche par conformité/sécurité
- Page de détail séparée pour visualisation complète des événements
- Améliorations dans les données mock avec variations réalistes par mois et année
