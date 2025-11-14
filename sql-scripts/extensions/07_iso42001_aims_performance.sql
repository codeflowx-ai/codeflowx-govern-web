CREATE TABLE IF NOT EXISTS apsaimsperformance (
    idxapsperformance BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    apsmetricname VARCHAR(150) NOT NULL,
    apsmetriccategory VARCHAR(50) NOT NULL,
    apsmetricdefinition TEXT,
    apsmetricvalue NUMERIC(12,4),
    apstargetvalue NUMERIC(12,4),
    apsvariance NUMERIC(12,4),
    apsstatus VARCHAR(20) NOT NULL,
    apsdatasource VARCHAR(100),
    apsperiodstart TIMESTAMP,
    apsperiodend TIMESTAMP,
    apsnotes TEXT,
    apscreatedat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    apsupdatedat TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_aps_category ON apsaimsperformance (apsmetriccategory);
CREATE INDEX IF NOT EXISTS idx_aps_status ON apsaimsperformance (apsstatus);
CREATE INDEX IF NOT EXISTS idx_aps_period ON apsaimsperformance (apsperiodend DESC);
