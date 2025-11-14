CREATE TABLE IF NOT EXISTS ancnonconformity (
    idxancnonconformity BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    anctitle VARCHAR(180) NOT NULL,
    ancdescription TEXT NOT NULL,
    ancseverity VARCHAR(20) NOT NULL,
    ancstatus VARCHAR(20) NOT NULL,
    ancdetectiondate TIMESTAMP,
    ancresponsible VARCHAR(120),
    ancrootcause TEXT,
    anccontainmentactions TEXT,
    anccorrectiveactions TEXT,
    ancduedate TIMESTAMP,
    ancclosuredate TIMESTAMP,
    ancevidenceurl VARCHAR(500),
    anccreatedat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ancupdatedat TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_anc_status ON ancnonconformity (ancstatus);
CREATE INDEX IF NOT EXISTS idx_anc_severity ON ancnonconformity (ancseverity);
CREATE INDEX IF NOT EXISTS idx_anc_due ON ancnonconformity (ancduedate);
