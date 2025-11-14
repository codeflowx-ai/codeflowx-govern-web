ALTER TABLE prjprojects
    ADD COLUMN IF NOT EXISTS prjaisystemtype VARCHAR(50),
    ADD COLUMN IF NOT EXISTS prjailifecyclestage VARCHAR(50) DEFAULT 'DESIGN',
    ADD COLUMN IF NOT EXISTS prjairisklevel VARCHAR(20),
    ADD COLUMN IF NOT EXISTS prjaipurpose TEXT,
    ADD COLUMN IF NOT EXISTS prjaiintendeduse TEXT,
    ADD COLUMN IF NOT EXISTS prjaiusers TEXT,
    ADD COLUMN IF NOT EXISTS prjaicontrolsapplied TEXT,
    ADD COLUMN IF NOT EXISTS prjaiperformancemetrics TEXT,
    ADD COLUMN IF NOT EXISTS prjailastinventoryreview TIMESTAMP,
    ADD COLUMN IF NOT EXISTS prjainextinventoryreview TIMESTAMP;

CREATE INDEX IF NOT EXISTS idx_prj_aisystemtype ON prjprojects (prjaisystemtype);
CREATE INDEX IF NOT EXISTS idx_prj_ailifecyclestage ON prjprojects (prjailifecyclestage);
CREATE INDEX IF NOT EXISTS idx_prj_airisklevel ON prjprojects (prjairisklevel);
