CREATE TABLE IF NOT EXISTS aimprovement (
    idxaimprovement BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    aimtitle VARCHAR(180) NOT NULL,
    aimdescription TEXT NOT NULL,
    aimcategory VARCHAR(50),
    aimstatus VARCHAR(20) NOT NULL,
    aimpriority VARCHAR(20),
    aimowner VARCHAR(120),
    aimstartdate TIMESTAMP,
    aimduedate TIMESTAMP,
    aimcompletiondate TIMESTAMP,
    aimexpectedbenefit TEXT,
    aimactualbenefit TEXT,
    aimcreatedat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    aimupdatedat TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_aim_status ON aimprovement (aimstatus);
CREATE INDEX IF NOT EXISTS idx_aim_priority ON aimprovement (aimpriority);
CREATE INDEX IF NOT EXISTS idx_aim_owner ON aimprovement (aimowner);
