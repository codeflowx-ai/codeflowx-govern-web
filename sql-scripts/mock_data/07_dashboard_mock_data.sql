-- =====================================================
-- DATOS MOCK - MÓDULO DASHBOARD
-- =====================================================
-- Dashboard general del sistema
-- Convención EnArt: UPPERCASE
-- Generado: 2025-10-14 13:58:15
-- =====================================================

-- Limpiar datos existentes
TRUNCATE TABLE DSHMODULESTATS, DSHDISTRIBUTIONS, DSHMODULEACTIVITY, 
               DSHTIMESERIES, DSHTOKENMETRICS, DSHCOSTMETRICS, 
               DSHQUICKACTIONS RESTART IDENTITY CASCADE;

-- =====================================================
-- 1. ESTADÍSTICAS DE MÓDULOS (DSHMODULESTATS)
-- =====================================================
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'models', 247, 156, 12, 
    79, 92.5, 87.5, 3, 
    '{"productionModels": 156, "complianceScore": 87.5, "biasDetected": 23, "avgPerformance": 92.3}'::jsonb, NOW(), NOW()
);
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'governance', 178, 98, 8, 
    72, 89.2, 91.3, 5, 
    '{"activePolicies": 45, "criticalRisks": 8, "pendingAudits": 12, "complianceRate": 95.2}'::jsonb, NOW(), NOW()
);
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'agents', 312, 189, 15, 
    108, 94.8, 88.7, 2, 
    '{"activeAgents": 89, "interactions": 15420, "decisions": 8932, "learningRate": 87.5}'::jsonb, NOW(), NOW()
);
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'analytics', 234, 145, 6, 
    83, 91.5, 89.2, 1, 
    '{"fairnessScore": 91.2, "biasLevel": "Low", "transparencyScore": 88.5, "accountabilityScore": 93.1}'::jsonb, NOW(), NOW()
);
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'evaluation', 398, 28, 12, 
    342, 93.7, 85.7, 4, 
    '{"completedTests": 342, "activeTests": 28, "avgScore": 85.7, "failedTests": 14}'::jsonb, NOW(), NOW()
);
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'prompts', 456, 234, 18, 
    204, 95.2, 92.4, 2, 
    '{"templates": 78, "activePrompts": 234, "validationRate": 94.2, "securityScore": 96.5}'::jsonb, NOW(), NOW()
);
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'rag', 289, 167, 8, 
    114, 88.9, 86.5, 6, 
    '{"dataSources": 45, "documentsIndexed": 125000, "queriesProcessed": 45200, "avgRelevance": 88.9}'::jsonb, NOW(), NOW()
);
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'training', 523, 156, 24, 
    343, 90.3, 84.2, 3, 
    '{"experiments": 156, "runs": 2340, "artifacts": 890, "syncSuccess": 98.5}'::jsonb, NOW(), NOW()
);
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'compliance', 198, 123, 9, 
    66, 96.5, 92.5, 2, 
    '{"frameworks": 8, "audits": 45, "complianceRate": 92.5, "aiActCompliance": 94.2}'::jsonb, NOW(), NOW()
);
INSERT INTO DSHMODULESTATS (
    DSHMODULENAME, DSHTOTALITEMS, DSHACTIVEITEMS, DSHPENDINGITEMS, 
    DSHCOMPLETEDITEMS, DSHSUCCESSRATE, DSHAVGSCORE, DSHACTIVEALERTS, 
    DSHCUSTOMMETRICS, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'projects', 89, 34, 8, 
    47, 88.7, 90.1, 3, 
    '{"activeProjects": 34, "teamMembers": 127, "avgBudgetCompliance": 94.3, "onTimeDelivery": 88.7}'::jsonb, NOW(), NOW()
);

-- =====================================================
-- 2. DISTRIBUCIONES (DSHDISTRIBUTIONS)
-- =====================================================
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'models', 'status', 'Production', 156, 63.16, '#10B981', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'models', 'status', 'Staging', 45, 18.22, '#F59E0B', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'models', 'status', 'Development', 34, 13.77, '#3B82F6', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'models', 'status', 'Deprecated', 12, 4.86, '#EF4444', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'agents', 'type', 'Conversational', 89, 28.53, '#3B82F6', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'agents', 'type', 'Analytical', 67, 21.47, '#10B981', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'agents', 'type', 'Specialized', 78, 25.0, '#F59E0B', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'agents', 'type', 'Operational', 45, 14.42, '#8B5CF6', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'agents', 'type', 'Integration', 33, 10.58, '#EC4899', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'governance', 'risk_level', 'Low', 98, 55.06, '#10B981', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'governance', 'risk_level', 'Medium', 52, 29.21, '#F59E0B', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'governance', 'risk_level', 'High', 20, 11.24, '#EF4444', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'governance', 'risk_level', 'Critical', 8, 4.49, '#7F1D1D', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'prompts', 'category', 'Code Generation', 98, 21.49, '#3B82F6', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'prompts', 'category', 'Data Analysis', 87, 19.08, '#10B981', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'prompts', 'category', 'Documentation', 76, 16.67, '#F59E0B', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'prompts', 'category', 'Customer Support', 89, 19.52, '#8B5CF6', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'prompts', 'category', 'Security', 56, 12.28, '#EF4444', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'prompts', 'category', 'DevOps', 50, 10.96, '#14B8A6', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'compliance', 'framework', 'EU AI Act', 56, 28.28, '#3B82F6', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'compliance', 'framework', 'GDPR', 45, 22.73, '#10B981', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'compliance', 'framework', 'ISO 27001', 34, 17.17, '#F59E0B', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'compliance', 'framework', 'SOC 2', 32, 16.16, '#8B5CF6', NOW(), NOW()
);
INSERT INTO DSHDISTRIBUTIONS (
    DSHMODULENAME, DSHCATEGORY, DSHLABEL, DSHVALUE, DSHPERCENTAGE, DSHCOLOR, DSHCREATEDAT, DSHUPDATEDAT
) VALUES (
    'compliance', 'framework', 'NIST', 31, 15.66, '#14B8A6', NOW(), NOW()
);

-- =====================================================
-- 3. SERIES TEMPORALES (DSHTIMESERIES)
-- =====================================================
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 151.1, '2025-09-14 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 150.54, '2025-09-15 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 153.01, '2025-09-16 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 157.23, '2025-09-17 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 159.7, '2025-09-18 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 158.48, '2025-09-19 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 159.99, '2025-09-20 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 164.46, '2025-09-21 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 160.48, '2025-09-22 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 164.6, '2025-09-23 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 163.64, '2025-09-24 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 170.25, '2025-09-25 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 172.9, '2025-09-26 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 174.52, '2025-09-27 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 177.36, '2025-09-28 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 176.38, '2025-09-29 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 178.78, '2025-09-30 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 175.86, '2025-10-01 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 175.45, '2025-10-02 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 182.13, '2025-10-03 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 186.5, '2025-10-04 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 185.58, '2025-10-05 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 184.41, '2025-10-06 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 185.29, '2025-10-07 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 194.83, '2025-10-08 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 195.72, '2025-10-09 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 194.52, '2025-10-10 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 195.97, '2025-10-11 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 199.1, '2025-10-12 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'models_deployed', 199.21, '2025-10-13 13:58:15', 
    ARRAY['count'], 'Modelos', '{"category": "models_deployed", "unit": "Modelos"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 45822.19, '2025-09-14 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 53264.18, '2025-09-15 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 54666.12, '2025-09-16 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 57628.93, '2025-09-17 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 57400.16, '2025-09-18 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 54830.81, '2025-09-19 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 58233.05, '2025-09-20 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 54708.6, '2025-09-21 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 60909.75, '2025-09-22 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 56321.78, '2025-09-23 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 63172.08, '2025-09-24 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 58738.68, '2025-09-25 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 57837.86, '2025-09-26 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 64193.84, '2025-09-27 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 59393.48, '2025-09-28 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 69945.15, '2025-09-29 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 62611.65, '2025-09-30 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 69998.02, '2025-10-01 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 72285.37, '2025-10-02 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 65905.49, '2025-10-03 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 67460.43, '2025-10-04 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 73459.41, '2025-10-05 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 67421.32, '2025-10-06 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 72515.7, '2025-10-07 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 72178.66, '2025-10-08 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 74910.63, '2025-10-09 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 73456.34, '2025-10-10 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 75941.59, '2025-10-11 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 78338.05, '2025-10-12 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'api_requests', 83238.51, '2025-10-13 13:58:15', 
    ARRAY['count'], 'Requests', '{"category": "api_requests", "unit": "Requests"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 85.46, '2025-09-14 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 85.3, '2025-09-15 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 85.53, '2025-09-16 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 86.52, '2025-09-17 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 86.91, '2025-09-18 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 87.27, '2025-09-19 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 88.11, '2025-09-20 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 87.86, '2025-09-21 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 89.09, '2025-09-22 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 89.64, '2025-09-23 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 89.67, '2025-09-24 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 90.37, '2025-09-25 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 90.15, '2025-09-26 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 91.53, '2025-09-27 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 91.2, '2025-09-28 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 91.35, '2025-09-29 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 92.7, '2025-09-30 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 93.22, '2025-10-01 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 92.46, '2025-10-02 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 93.21, '2025-10-03 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 94.43, '2025-10-04 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 93.52, '2025-10-05 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 95.47, '2025-10-06 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 95.06, '2025-10-07 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 94.98, '2025-10-08 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 95.5, '2025-10-09 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 97.09, '2025-10-10 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 96.41, '2025-10-11 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 96.18, '2025-10-12 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'success_rate', 97.23, '2025-10-13 13:58:15', 
    ARRAY['percentage'], 'Success Rate', '{"category": "success_rate", "unit": "Success Rate"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 92.71, '2025-09-14 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 105.99, '2025-09-15 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 116.32, '2025-09-16 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 123.75, '2025-09-17 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 131.68, '2025-09-18 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 130.43, '2025-09-19 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 138.57, '2025-09-20 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 153.18, '2025-09-21 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 155.04, '2025-09-22 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 153.01, '2025-09-23 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 176.46, '2025-09-24 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 169.11, '2025-09-25 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 184.85, '2025-09-26 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 188.72, '2025-09-27 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 200.49, '2025-09-28 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 209.89, '2025-09-29 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 203.95, '2025-09-30 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 221.44, '2025-10-01 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 221.54, '2025-10-02 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 221.92, '2025-10-03 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 238.4, '2025-10-04 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 231.54, '2025-10-05 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 243.79, '2025-10-06 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 255.04, '2025-10-07 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 251.76, '2025-10-08 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 266.76, '2025-10-09 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 264.8, '2025-10-10 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 271.74, '2025-10-11 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 289.79, '2025-10-12 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'avg_latency', 297.56, '2025-10-13 13:58:15', 
    ARRAY['milliseconds'], 'Latency', '{"category": "avg_latency", "unit": "Latency"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 180.81, '2025-09-14 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 180.09, '2025-09-15 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 179.36, '2025-09-16 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 186.27, '2025-09-17 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 181.54, '2025-09-18 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 179.76, '2025-09-19 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 182.68, '2025-09-20 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 182.32, '2025-09-21 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 186.8, '2025-09-22 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 185.26, '2025-09-23 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 182.34, '2025-09-24 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 184.15, '2025-09-25 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 188.7, '2025-09-26 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 185.04, '2025-09-27 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 193.91, '2025-09-28 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 193.24, '2025-09-29 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 193.58, '2025-09-30 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 194.11, '2025-10-01 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 191.46, '2025-10-02 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 191.45, '2025-10-03 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 197.25, '2025-10-04 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 194.33, '2025-10-05 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 197.71, '2025-10-06 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 192.51, '2025-10-07 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 192.56, '2025-10-08 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 200.36, '2025-10-09 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 199.02, '2025-10-10 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 193.9, '2025-10-11 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 199.03, '2025-10-12 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'active_agents', 199.71, '2025-10-13 13:58:15', 
    ARRAY['count'], 'Agentes', '{"category": "active_agents", "unit": "Agentes"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 87.86, '2025-09-14 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 88.01, '2025-09-15 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 88.21, '2025-09-16 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 88.82, '2025-09-17 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 89.2, '2025-09-18 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 89.47, '2025-09-19 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 89.63, '2025-09-20 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 90.2, '2025-09-21 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 90.49, '2025-09-22 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 90.79, '2025-09-23 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 91.16, '2025-09-24 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 91.3, '2025-09-25 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 90.88, '2025-09-26 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 91.02, '2025-09-27 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 91.34, '2025-09-28 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 91.74, '2025-09-29 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 92.48, '2025-09-30 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 92.68, '2025-10-01 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 92.4, '2025-10-02 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 93.18, '2025-10-03 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 93.28, '2025-10-04 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 94.09, '2025-10-05 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 94.04, '2025-10-06 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 94.24, '2025-10-07 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 94.76, '2025-10-08 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 94.56, '2025-10-09 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 95.34, '2025-10-10 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 95.66, '2025-10-11 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 95.74, '2025-10-12 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);
INSERT INTO DSHTIMESERIES (
    DSHSERIESNAME, DSHVALUE, DSHTIMESTAMP, DSHMETRICTYPE, DSHUNIT, DSHLABELS, DSHCREATEDAT
) VALUES (
    'compliance_score', 96.07, '2025-10-13 13:58:15', 
    ARRAY['percentage'], 'Compliance', '{"category": "compliance_score", "unit": "Compliance"}'::jsonb, NOW()
);

-- =====================================================
-- 4. ACTIVIDAD RECIENTE (DSHMODULEACTIVITY)
-- =====================================================
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'models', ARRAY['deployment'], 'GPT-4-Turbo desplegado en producción', 'GPT-4-Turbo', 
    'john.doe@codeflowx.com', ARRAY['completed'], '2025-10-14 12:58:15', NOW()
);
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'models', ARRAY['evaluation'], 'Evaluación de bias completada para Sentiment-BERT', 'Sentiment-BERT', 
    'jane.smith@codeflowx.com', ARRAY['completed'], '2025-10-14 11:58:15', NOW()
);
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'governance', ARRAY['policy_check'], 'Política de compliance validada', 'EU AI Act Policy', 
    'admin@codeflowx.com', ARRAY['completed'], '2025-10-14 10:58:15', NOW()
);
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'agents', ARRAY['interaction'], 'Agente de soporte atendió 245 consultas', 'Customer Support Bot', 
    'system', ARRAY['completed'], '2025-10-14 09:58:15', NOW()
);
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'prompts', ARRAY['validation'], 'Nuevo prompt validado: Code Review Assistant', 'Code Review v2.0', 
    'mike.johnson@codeflowx.com', ARRAY['completed'], '2025-10-14 08:58:15', NOW()
);
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'training', ARRAY['experiment'], 'Experimento de sentiment analysis finalizado', 'Sentiment v1', 
    'sarah.williams@codeflowx.com', ARRAY['completed'], '2025-10-14 07:58:15', NOW()
);
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'compliance', ARRAY['audit'], 'Auditoría GDPR completada exitosamente', 'GDPR Audit Q4', 
    'governance.officer@codeflowx.com', ARRAY['completed'], '2025-10-14 05:58:15', NOW()
);
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'rag', ARRAY['indexing'], '12,500 documentos indexados en knowledge base', 'Technical Docs', 
    'system', ARRAY['completed'], '2025-10-14 03:58:15', NOW()
);
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'infrastructure', ARRAY['scaling'], 'Cluster de Kubernetes escalado a 15 nodos', 'Production Cluster', 
    'devops@codeflowx.com', ARRAY['completed'], '2025-10-14 01:58:15', NOW()
);
INSERT INTO DSHMODULEACTIVITY (
    DSHMODULENAME, DSHACTIVITYTYPE, DSHDESCRIPTION, DSHRELATEDITEM, 
    DSHUSER, DSHSTATUS, DSHTIMESTAMP, DSHCREATEDAT
) VALUES (
    'monitoring', ARRAY['alert'], 'Alerta de latencia alta resuelta', 'API Latency', 
    'sre@codeflowx.com', ARRAY['completed'], '2025-10-13 22:58:15', NOW()
);

-- =====================================================
-- 5. MÉTRICAS DE TOKENS (DSHTOKENMETRICS)
-- =====================================================
INSERT INTO DSHTOKENMETRICS (
    DSHTOKENTYPE, DSHTOTALTOKENS, DSHTOKENSTODAY, DSHTOKENSWEEK, DSHTOKENSMONTH,
    DSHAVGTOKENSREQUEST, DSHCOSTPER1K, DSHTOTALCOST, DSHCURRENCY, DSHMODELNAME,
    DSHPROVIDER, DSHTREND, DSHCHANGEPERCENTAGE, DSHPERIOD,
    DSHPERIODSTART, DSHPERIODEND, DSHMEASUREMENTDATE
) VALUES (
    ARRAY['total'], 4238736, 584772, 4238736, 16954944,
    817, 0.015, 63.58, 'USD', 'GPT-4-Turbo',
    'OpenAI', 'up', 8.49, ARRAY['daily'],
    '2025-10-07 00:00:00', '2025-10-07 23:59:59',
    '2025-10-07 00:00:00'
);
INSERT INTO DSHTOKENMETRICS (
    DSHTOKENTYPE, DSHTOTALTOKENS, DSHTOKENSTODAY, DSHTOKENSWEEK, DSHTOKENSMONTH,
    DSHAVGTOKENSREQUEST, DSHCOSTPER1K, DSHTOTALCOST, DSHCURRENCY, DSHMODELNAME,
    DSHPROVIDER, DSHTREND, DSHCHANGEPERCENTAGE, DSHPERIOD,
    DSHPERIODSTART, DSHPERIODEND, DSHMEASUREMENTDATE
) VALUES (
    ARRAY['total'], 3290217, 456515, 3290217, 13160868,
    815, 0.015, 49.35, 'USD', 'GPT-4-Turbo',
    'OpenAI', 'up', 6.98, ARRAY['daily'],
    '2025-10-08 00:00:00', '2025-10-08 23:59:59',
    '2025-10-08 00:00:00'
);
INSERT INTO DSHTOKENMETRICS (
    DSHTOKENTYPE, DSHTOTALTOKENS, DSHTOKENSTODAY, DSHTOKENSWEEK, DSHTOKENSMONTH,
    DSHAVGTOKENSREQUEST, DSHCOSTPER1K, DSHTOTALCOST, DSHCURRENCY, DSHMODELNAME,
    DSHPROVIDER, DSHTREND, DSHCHANGEPERCENTAGE, DSHPERIOD,
    DSHPERIODSTART, DSHPERIODEND, DSHMEASUREMENTDATE
) VALUES (
    ARRAY['total'], 4095156, 449238, 4095156, 16380624,
    1212, 0.015, 61.43, 'USD', 'GPT-4-Turbo',
    'OpenAI', 'up', 7.96, ARRAY['daily'],
    '2025-10-09 00:00:00', '2025-10-09 23:59:59',
    '2025-10-09 00:00:00'
);
INSERT INTO DSHTOKENMETRICS (
    DSHTOKENTYPE, DSHTOTALTOKENS, DSHTOKENSTODAY, DSHTOKENSWEEK, DSHTOKENSMONTH,
    DSHAVGTOKENSREQUEST, DSHCOSTPER1K, DSHTOTALCOST, DSHCURRENCY, DSHMODELNAME,
    DSHPROVIDER, DSHTREND, DSHCHANGEPERCENTAGE, DSHPERIOD,
    DSHPERIODSTART, DSHPERIODEND, DSHMEASUREMENTDATE
) VALUES (
    ARRAY['total'], 3237747, 539800, 3237747, 12950988,
    1173, 0.015, 48.57, 'USD', 'GPT-4-Turbo',
    'OpenAI', 'up', 7.57, ARRAY['daily'],
    '2025-10-10 00:00:00', '2025-10-10 23:59:59',
    '2025-10-10 00:00:00'
);
INSERT INTO DSHTOKENMETRICS (
    DSHTOKENTYPE, DSHTOTALTOKENS, DSHTOKENSTODAY, DSHTOKENSWEEK, DSHTOKENSMONTH,
    DSHAVGTOKENSREQUEST, DSHCOSTPER1K, DSHTOTALCOST, DSHCURRENCY, DSHMODELNAME,
    DSHPROVIDER, DSHTREND, DSHCHANGEPERCENTAGE, DSHPERIOD,
    DSHPERIODSTART, DSHPERIODEND, DSHMEASUREMENTDATE
) VALUES (
    ARRAY['total'], 4320436, 598832, 4320436, 17281744,
    1081, 0.015, 64.81, 'USD', 'GPT-4-Turbo',
    'OpenAI', 'up', 7.58, ARRAY['daily'],
    '2025-10-11 00:00:00', '2025-10-11 23:59:59',
    '2025-10-11 00:00:00'
);
INSERT INTO DSHTOKENMETRICS (
    DSHTOKENTYPE, DSHTOTALTOKENS, DSHTOKENSTODAY, DSHTOKENSWEEK, DSHTOKENSMONTH,
    DSHAVGTOKENSREQUEST, DSHCOSTPER1K, DSHTOTALCOST, DSHCURRENCY, DSHMODELNAME,
    DSHPROVIDER, DSHTREND, DSHCHANGEPERCENTAGE, DSHPERIOD,
    DSHPERIODSTART, DSHPERIODEND, DSHMEASUREMENTDATE
) VALUES (
    ARRAY['total'], 2655842, 518802, 2655842, 10623368,
    1215, 0.015, 39.84, 'USD', 'GPT-4-Turbo',
    'OpenAI', 'up', 5.01, ARRAY['daily'],
    '2025-10-12 00:00:00', '2025-10-12 23:59:59',
    '2025-10-12 00:00:00'
);
INSERT INTO DSHTOKENMETRICS (
    DSHTOKENTYPE, DSHTOTALTOKENS, DSHTOKENSTODAY, DSHTOKENSWEEK, DSHTOKENSMONTH,
    DSHAVGTOKENSREQUEST, DSHCOSTPER1K, DSHTOTALCOST, DSHCURRENCY, DSHMODELNAME,
    DSHPROVIDER, DSHTREND, DSHCHANGEPERCENTAGE, DSHPERIOD,
    DSHPERIODSTART, DSHPERIODEND, DSHMEASUREMENTDATE
) VALUES (
    ARRAY['total'], 3870023, 583147, 3870023, 15480092,
    874, 0.015, 58.05, 'USD', 'GPT-4-Turbo',
    'OpenAI', 'up', 8.35, ARRAY['daily'],
    '2025-10-13 00:00:00', '2025-10-13 23:59:59',
    '2025-10-13 00:00:00'
);

-- =====================================================
-- 6. MÉTRICAS DE COSTOS (DSHCOSTMETRICS)
-- =====================================================
INSERT INTO DSHCOSTMETRICS (
    DSHCOSTTYPE, DSHCOSTCATEGORY, DSHTOTALCOST, DSHCOSTTODAY, DSHCOSTWEEK, DSHCOSTMONTH,
    DSHMONTHLYBUDGET, DSHBUDGETUSEDPCT, DSHCURRENCY, DSHTREND, DSHCHANGEPERCENTAGE,
    DSHPROJECTEDMONTHLY, DSHPERIOD, DSHPERIODSTART, DSHPERIODEND
) VALUES (
    ARRAY['inference'], 'models', 4567.89, 152.26, 1141.97, 4567.89,
    5024.68, 90.91, 'USD', 'up', 13.92,
    4887.64, ARRAY['monthly'], '2025-09-14', '2025-10-14'
);
INSERT INTO DSHCOSTMETRICS (
    DSHCOSTTYPE, DSHCOSTCATEGORY, DSHTOTALCOST, DSHCOSTTODAY, DSHCOSTWEEK, DSHCOSTMONTH,
    DSHMONTHLYBUDGET, DSHBUDGETUSEDPCT, DSHCURRENCY, DSHTREND, DSHCHANGEPERCENTAGE,
    DSHPROJECTEDMONTHLY, DSHPERIOD, DSHPERIODSTART, DSHPERIODEND
) VALUES (
    ARRAY['execution'], 'agents', 2345.67, 78.19, 586.42, 2345.67,
    2580.24, 90.91, 'USD', 'up', 13.50,
    2509.87, ARRAY['monthly'], '2025-09-14', '2025-10-14'
);
INSERT INTO DSHCOSTMETRICS (
    DSHCOSTTYPE, DSHCOSTCATEGORY, DSHTOTALCOST, DSHCOSTTODAY, DSHCOSTWEEK, DSHCOSTMONTH,
    DSHMONTHLYBUDGET, DSHBUDGETUSEDPCT, DSHCURRENCY, DSHTREND, DSHCHANGEPERCENTAGE,
    DSHPROJECTEDMONTHLY, DSHPERIOD, DSHPERIODSTART, DSHPERIODEND
) VALUES (
    ARRAY['storage'], 'rag', 890.12, 29.67, 222.53, 890.12,
    979.13, 90.91, 'USD', 'up', 8.52,
    952.43, ARRAY['monthly'], '2025-09-14', '2025-10-14'
);
INSERT INTO DSHCOSTMETRICS (
    DSHCOSTTYPE, DSHCOSTCATEGORY, DSHTOTALCOST, DSHCOSTTODAY, DSHCOSTWEEK, DSHCOSTMONTH,
    DSHMONTHLYBUDGET, DSHBUDGETUSEDPCT, DSHCURRENCY, DSHTREND, DSHCHANGEPERCENTAGE,
    DSHPROJECTEDMONTHLY, DSHPERIOD, DSHPERIODSTART, DSHPERIODEND
) VALUES (
    ARRAY['compute'], 'training', 6789.34, 226.31, 1697.34, 6789.34,
    7468.27, 90.91, 'USD', 'up', 10.95,
    7264.59, ARRAY['monthly'], '2025-09-14', '2025-10-14'
);
INSERT INTO DSHCOSTMETRICS (
    DSHCOSTTYPE, DSHCOSTCATEGORY, DSHTOTALCOST, DSHCOSTTODAY, DSHCOSTWEEK, DSHCOSTMONTH,
    DSHMONTHLYBUDGET, DSHBUDGETUSEDPCT, DSHCURRENCY, DSHTREND, DSHCHANGEPERCENTAGE,
    DSHPROJECTEDMONTHLY, DSHPERIOD, DSHPERIODSTART, DSHPERIODEND
) VALUES (
    ARRAY['hosting'], 'infrastructure', 3456.78, 115.23, 864.2, 3456.78,
    3802.46, 90.91, 'USD', 'up', 6.70,
    3698.75, ARRAY['monthly'], '2025-09-14', '2025-10-14'
);
INSERT INTO DSHCOSTMETRICS (
    DSHCOSTTYPE, DSHCOSTCATEGORY, DSHTOTALCOST, DSHCOSTTODAY, DSHCOSTWEEK, DSHCOSTMONTH,
    DSHMONTHLYBUDGET, DSHBUDGETUSEDPCT, DSHCURRENCY, DSHTREND, DSHCHANGEPERCENTAGE,
    DSHPROJECTEDMONTHLY, DSHPERIOD, DSHPERIODSTART, DSHPERIODEND
) VALUES (
    ARRAY['observability'], 'monitoring', 567.89, 18.93, 141.97, 567.89,
    624.68, 90.91, 'USD', 'up', 6.61,
    607.64, ARRAY['monthly'], '2025-09-14', '2025-10-14'
);

-- =====================================================
-- 7. ACCIONES RÁPIDAS (DSHQUICKACTIONS)
-- =====================================================
INSERT INTO DSHQUICKACTIONS (DSHTITLE, DSHDESCRIPTION, DSHICON, DSHCOLOR, DSHGRADIENT, DSHHREF, DSHCATEGORY, DSHDISPLAYORDER, DSHENABLED) VALUES ('Deploy New Model', 'Desplegar un nuevo modelo en producción', 'rocket', '#10B981', 'linear-gradient(135deg, #10B981 0%, #10B981CC 100%)', '/models/deploy', 'models', 1, true);
INSERT INTO DSHQUICKACTIONS (DSHTITLE, DSHDESCRIPTION, DSHICON, DSHCOLOR, DSHGRADIENT, DSHHREF, DSHCATEGORY, DSHDISPLAYORDER, DSHENABLED) VALUES ('Create Agent', 'Crear un nuevo agente IA especializado', 'bot', '#3B82F6', 'linear-gradient(135deg, #3B82F6 0%, #3B82F6CC 100%)', '/agents/create', 'agents', 2, true);
INSERT INTO DSHQUICKACTIONS (DSHTITLE, DSHDESCRIPTION, DSHICON, DSHCOLOR, DSHGRADIENT, DSHHREF, DSHCATEGORY, DSHDISPLAYORDER, DSHENABLED) VALUES ('Run Compliance Check', 'Ejecutar auditoría de cumplimiento', 'shield', '#F59E0B', 'linear-gradient(135deg, #F59E0B 0%, #F59E0BCC 100%)', '/governance/audit', 'governance', 3, true);
INSERT INTO DSHQUICKACTIONS (DSHTITLE, DSHDESCRIPTION, DSHICON, DSHCOLOR, DSHGRADIENT, DSHHREF, DSHCATEGORY, DSHDISPLAYORDER, DSHENABLED) VALUES ('Generate Prompt', 'Crear un nuevo prompt template', 'edit', '#8B5CF6', 'linear-gradient(135deg, #8B5CF6 0%, #8B5CF6CC 100%)', '/prompts/new', 'prompts', 4, true);
INSERT INTO DSHQUICKACTIONS (DSHTITLE, DSHDESCRIPTION, DSHICON, DSHCOLOR, DSHGRADIENT, DSHHREF, DSHCATEGORY, DSHDISPLAYORDER, DSHENABLED) VALUES ('Start Experiment', 'Iniciar nuevo experimento de training', 'flask', '#EC4899', 'linear-gradient(135deg, #EC4899 0%, #EC4899CC 100%)', '/experiments/create', 'training', 5, true);
INSERT INTO DSHQUICKACTIONS (DSHTITLE, DSHDESCRIPTION, DSHICON, DSHCOLOR, DSHGRADIENT, DSHHREF, DSHCATEGORY, DSHDISPLAYORDER, DSHENABLED) VALUES ('View Analytics', 'Ver análisis de fairness y bias', 'chart', '#14B8A6', 'linear-gradient(135deg, #14B8A6 0%, #14B8A6CC 100%)', '/analytics/dashboard', 'analytics', 6, true);
INSERT INTO DSHQUICKACTIONS (DSHTITLE, DSHDESCRIPTION, DSHICON, DSHCOLOR, DSHGRADIENT, DSHHREF, DSHCATEGORY, DSHDISPLAYORDER, DSHENABLED) VALUES ('Setup RAG', 'Configurar nuevo sistema RAG', 'database', '#F97316', 'linear-gradient(135deg, #F97316 0%, #F97316CC 100%)', '/rag/setup', 'rag', 7, true);
INSERT INTO DSHQUICKACTIONS (DSHTITLE, DSHDESCRIPTION, DSHICON, DSHCOLOR, DSHGRADIENT, DSHHREF, DSHCATEGORY, DSHDISPLAYORDER, DSHENABLED) VALUES ('Monitor Metrics', 'Ver métricas en tiempo real', 'activity', '#EF4444', 'linear-gradient(135deg, #EF4444 0%, #EF4444CC 100%)', '/monitoring/live', 'monitoring', 8, true);

