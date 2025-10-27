CREATE TABLE SLESOPTIMIZATIONHISTORY (
    idxslesoptimizationhistory BIGINT PRIMARY KEY,
    idslesmodel BIGINT NOT NULL,
    optimizationdate TIMESTAMP NOT NULL,
    optimizationtypes TEXT NOT NULL,
    success BOOLEAN NOT NULL,
    details JSONB,
    metrics_before JSONB,
    metrics_after JSONB,
    performance_impact JSONB,
    optimization_status VARCHAR(50),
    execution_log TEXT,
    FOREIGN KEY (idslesmodel) REFERENCES SLESMODEL(idxslesmodel)
);

CREATE SEQUENCE SEQ_SLESOPTIMIZATIONHISTORY START 1;
CREATE INDEX idx_opthistory_model ON SLESOPTIMIZATIONHISTORY(idslesmodel);
CREATE INDEX idx_opthistory_date ON SLESOPTIMIZATIONHISTORY(optimizationdate);