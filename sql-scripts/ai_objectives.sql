-- Tabla de Objetivos de IA (ISO/IEC 42001 Clause 6.2)
CREATE TABLE IF NOT EXISTS AIMOBJECTIVES (
    IDXAIOBJECTIVE      BIGSERIAL PRIMARY KEY,
    AIODESCRIPTION      TEXT NOT NULL,
    AIOCATEGORY         VARCHAR(50) NOT NULL,
    AIOMETRIC           VARCHAR(100) NOT NULL UNIQUE,
    AIOTARGETVALUE      NUMERIC(8,2) NOT NULL,
    AIOCURRENTVALUE     NUMERIC(8,2) DEFAULT 0,
    AIOSTATUS           VARCHAR(20) NOT NULL,
    AIORESPONSIBLE      VARCHAR(100) NOT NULL,
    AIOREVIEWFREQUENCY  VARCHAR(20) NOT NULL,
    AIOLASTREVIEWED     TIMESTAMP,
    AIONEXTREVIEW       TIMESTAMP,
    AIORELATEDPOLICY    TEXT,
    AIOCREATEDAT        TIMESTAMP NOT NULL DEFAULT NOW(),
    AIOUPDATEDAT        TIMESTAMP
);

CREATE INDEX IF NOT EXISTS IDX_AIO_CATEGORY ON AIMOBJECTIVES (AIOCATEGORY);
CREATE INDEX IF NOT EXISTS IDX_AIO_STATUS ON AIMOBJECTIVES (AIOSTATUS);
CREATE INDEX IF NOT EXISTS IDX_AIO_REVIEW ON AIMOBJECTIVES (AIONEXTREVIEW);

INSERT INTO AIMOBJECTIVES (
    AIODESCRIPTION,
    AIOCATEGORY,
    AIOMETRIC,
    AIOTARGETVALUE,
    AIOCURRENTVALUE,
    AIOSTATUS,
    AIORESPONSIBLE,
    AIOREVIEWFREQUENCY,
    AIOLASTREVIEWED,
    AIONEXTREVIEW,
    AIORELATEDPOLICY
) VALUES 
(
    'Cobertura de explicabilidad en modelos en producción >95%',
    'TRANSPARENCY',
    'Explainability coverage >95%',
    95.00,
    92.00,
    'ACTIVE',
    'AI Governance Officer',
    'MONTHLY',
    NOW() - INTERVAL '15 days',
    NOW() + INTERVAL '15 days',
    'Sección 2.2 Transparencia y Explicabilidad'
),
(
    'Tasa de detección de sesgos en modelos crítica >98%',
    'FAIRNESS',
    'Bias detection rate >98%',
    98.00,
    96.00,
    'ACTIVE',
    'Head of Responsible AI',
    'MONTHLY',
    NOW() - INTERVAL '20 days',
    NOW() + INTERVAL '10 days',
    'Sección 2.3 Equidad y No Discriminación'
),
(
    'Detección de ataques adversarios efectivos >99%',
    'ROBUSTNESS',
    'Adversarial attacks detected >99%',
    99.00,
    97.00,
    'ACTIVE',
    'Security Engineering Lead',
    'QUARTERLY',
    NOW() - INTERVAL '40 days',
    NOW() + INTERVAL '50 days',
    'Sección 2.4 Seguridad y Robustez'
),
(
    'Precisión en detección de PII en datasets >=99.5%',
    'PRIVACY',
    'PII detection accuracy >99.5%',
    99.50,
    99.20,
    'ACTIVE',
    'Data Protection Officer',
    'MONTHLY',
    NOW() - INTERVAL '12 days',
    NOW() + INTERVAL '18 days',
    'Sección 2.6 Privacidad y Protección de Datos'
),
(
    'Intervenciones humanas por falsos positivos <2%',
    'SAFETY',
    'Human oversight interventions <2%',
    2.00,
    3.10,
    'ACTIVE',
    'Safety Operations Lead',
    'MONTHLY',
    NOW() - INTERVAL '25 days',
    NOW() + INTERVAL '5 days',
    'Sección 2.1 Supervisión Humana'
)
ON CONFLICT (AIOMETRIC) DO NOTHING;


