-- ============================================================================
-- MOCK DATA - INFRASTRUCTURE MODULE
-- ============================================================================

-- Proveedores Cloud
INSERT INTO INFCLOUDPROVIDERS (
    INFNAME, INFPROVIDERTYPE, INFDESCRIPTION, INFREGION, INFACCESSKEY, INFSECRETKEY,
    INFPROJECTID, INFTENANTID, INFSTATUS, INFDEFAULTREGION, INFBILLINGACCOUNT,
    INFCOSTCENTER, INFCREATEDBY, INFCREATEDAT
) VALUES
('AWS Production', ARRAY['AWS'], 'Amazon Web Services production account', 'us-east-1', 'ENCRYPTED:AKIA...', 'ENCRYPTED:secret...', 'prod-123', NULL, 'ACTIVE', 'us-east-1', 'billing-prod', 'MLOps', 'admin', NOW() - INTERVAL '180 days'),
('Azure ML Platform', ARRAY['AZURE'], 'Microsoft Azure for ML workloads', 'eastus', NULL, NULL, 'ml-platform-sub', 'tenant-abc-123', 'ACTIVE', 'eastus', 'azure-billing-001', 'AI', 'admin', NOW() - INTERVAL '150 days'),
('GCP AI Research', ARRAY['GCP'], 'Google Cloud Platform for AI research', 'us-central1', NULL, NULL, 'ai-research-project', NULL, 'ACTIVE', 'us-central1', 'gcp-billing-ai', 'Research', 'admin', NOW() - INTERVAL '200 days'),
('On-Premise DC1', ARRAY['ON_PREMISE'], 'Local datacenter 1', 'datacenter-1', NULL, NULL, NULL, NULL, 'ACTIVE', NULL, 'internal', 'Infrastructure', 'ops.team', NOW() - INTERVAL '365 days');

-- Clusters Kubernetes
INSERT INTO INFKUBERNETESCLUSTERS (
    INFK8SPROVIDERID, INFK8SNAME, INFK8SDESCRIPTION, INFK8SVERSION, INFK8SENDPOINT,
    INFK8SREGION, INFK8SNODECOUNT, INFK8STOTALNODES, INFK8STOTALCPU, INFK8STOTALMEMORY,
    INFK8SSTATUS, INFK8SNETWORKPLUGIN, INFK8SINGRESS, INFK8SAUTOSCALING, INFK8SMINODES,
    INFK8SMAXNODES, INFK8SCREATEDBY, INFK8SCREATEDAT
) VALUES
(1, 'eks-prod-ml-cluster', 'EKS cluster for ML training workloads', 'v1.28.3', 
 'https://EKS-ABC123.eks.us-east-1.amazonaws.com', 'us-east-1', 8, 8, 64, 512.0,
 'RUNNING', 'AWS VPC CNI', 'ALB Ingress Controller', true, 3, 20,
 'admin', NOW() - INTERVAL '120 days'),
(2, 'aks-ml-inference', 'AKS cluster for model inference', 'v1.27.5', 
 'https://aks-ml-inference-dns.eastus.azmk8s.io', 'eastus', 5, 5, 40, 320.0,
 'RUNNING', 'Azure CNI', 'NGINX Ingress', true, 2, 15,
 'admin', NOW() - INTERVAL '90 days'),
(3, 'gke-ai-research', 'GKE cluster for AI research experiments', 'v1.28.0', 
 'https://gke-ai-research.googleapis.com', 'us-central1', 6, 6, 48, 384.0,
 'RUNNING', 'GKE CNI', 'GKE Ingress', true, 2, 12,
 'admin', NOW() - INTERVAL '100 days');

-- Recursos Cloud
INSERT INTO INFCLOUDRESOURCES (
    INFRESPROVIDERID, INFRESCLUSTERID, INFRESNAME, INFRESTYPE, INFRESSUBTYPE,
    INFRESREGION, INFRESZONE, INFRESINSTANCETYPE, INFRESINSTANCESIZE, INFRESCPU,
    INFRESMEMORY, INFRESSTORAGE, INFRESNETWORKBANDWIDTH, INFRESACCELERATION,
    INFRESSTATUS, INFRESHEALTH, INFRESPURPOSE, INFRESENVIRONMENT, INFRESTAGS,
    INFRESCURRENTCOST, INFRESESTIMATEDCOST, INFRESUTILIZATION, INFRESLAUNCHEDAT,
    INFRESCREATEDBY, INFRESCREATEDAT, INFRESLASTREPORTDAT
) VALUES
-- AWS EC2 Instances
(1, 1, 'ml-training-node-01', 'COMPUTE', 'EC2', 'us-east-1', 'us-east-1a',
 'p3.8xlarge', 'xlarge', 32, 244.0, 1000.0, '10 Gbps', 'Tesla V100 x4',
 'RUNNING', 'HEALTHY', 'ML Training', 'PRODUCTION', '{"team": "ml", "project": "nlp"}',
 24.50, 588.00, 85.5, NOW() - INTERVAL '30 days',
 'ml.team', NOW() - INTERVAL '30 days', NOW() - INTERVAL '1 hour'),

(1, 1, 'ml-training-node-02', 'COMPUTE', 'EC2', 'us-east-1', 'us-east-1b',
 'p3.8xlarge', 'xlarge', 32, 244.0, 1000.0, '10 Gbps', 'Tesla V100 x4',
 'RUNNING', 'HEALTHY', 'ML Training', 'PRODUCTION', '{"team": "ml", "project": "cv"}',
 24.50, 588.00, 92.3, NOW() - INTERVAL '28 days',
 'ml.team', NOW() - INTERVAL '28 days', NOW() - INTERVAL '30 minutes'),

(1, NULL, 'inference-api-01', 'COMPUTE', 'EC2', 'us-east-1', 'us-east-1a',
 't3.large', 'large', 2, 8.0, 100.0, '5 Gbps', NULL,
 'RUNNING', 'HEALTHY', 'API Server', 'PRODUCTION', '{"team": "backend", "service": "inference"}',
 0.12, 2.88, 45.2, NOW() - INTERVAL '60 days',
 'backend.team', NOW() - INTERVAL '60 days', NOW() - INTERVAL '2 hours'),

(1, NULL, 'dev-ml-workspace-01', 'COMPUTE', 'EC2', 'us-east-1', 'us-east-1a',
 'm5.2xlarge', 'medium', 8, 32.0, 500.0, '10 Gbps', NULL,
 'STOPPED', 'HEALTHY', 'Development', 'DEVELOPMENT', '{"team": "ml", "user": "john.doe"}',
 0.00, 0.00, 0.0, NOW() - INTERVAL '90 days',
 'john.doe', NOW() - INTERVAL '90 days', NOW() - INTERVAL '1 day'),

-- Azure VMs
(2, 2, 'azure-inference-vm-01', 'COMPUTE', 'VM', 'eastus', 'zone-1',
 'Standard_NC6s_v3', 'medium', 6, 112.0, 736.0, '10 Gbps', 'Tesla V100',
 'RUNNING', 'HEALTHY', 'Model Inference', 'PRODUCTION', '{"environment": "prod", "app": "inference"}',
 3.06, 73.44, 78.9, NOW() - INTERVAL '45 days',
 'ml.team', NOW() - INTERVAL '45 days', NOW() - INTERVAL '45 minutes'),

(2, 2, 'azure-api-gateway', 'COMPUTE', 'VM', 'eastus', 'zone-2',
 'Standard_D4s_v3', 'small', 4, 16.0, 128.0, '5 Gbps', NULL,
 'RUNNING', 'DEGRADED', 'API Gateway', 'PRODUCTION', '{"service": "gateway"}',
 0.25, 6.00, 55.1, NOW() - INTERVAL '50 days',
 'backend.team', NOW() - INTERVAL '50 days', NOW() - INTERVAL '15 minutes'),

-- GCP Compute Engine
(3, 3, 'gcp-research-gpu-01', 'COMPUTE', 'Compute Engine', 'us-central1', 'us-central1-a',
 'n1-highmem-8', 'large', 8, 52.0, 500.0, '10 Gbps', 'Tesla T4 x2',
 'RUNNING', 'HEALTHY', 'AI Research', 'DEVELOPMENT', '{"team": "research", "project": "rl"}',
 2.85, 68.40, 65.3, NOW() - INTERVAL '20 days',
 'research.team', NOW() - INTERVAL '20 days', NOW() - INTERVAL '3 hours'),

-- Storage Resources
(1, NULL, 'ml-datasets-bucket', 'STORAGE', 'S3', 'us-east-1', 'N/A',
 'S3 Standard', 'N/A', NULL, NULL, 5000.0, NULL, NULL,
 'RUNNING', 'HEALTHY', 'ML Datasets', 'PRODUCTION', '{"type": "datasets", "versioned": true}',
 115.00, 115.00, NULL, NOW() - INTERVAL '200 days',
 'ml.team', NOW() - INTERVAL '200 days', NOW() - INTERVAL '4 hours'),

(2, NULL, 'model-artifacts-storage', 'STORAGE', 'Blob Storage', 'eastus', 'N/A',
 'Hot Tier', 'N/A', NULL, NULL, 2000.0, NULL, NULL,
 'RUNNING', 'HEALTHY', 'Model Artifacts', 'PRODUCTION', '{"retention": "90days"}',
 45.00, 45.00, NULL, NOW() - INTERVAL '150 days',
 'ml.team', NOW() - INTERVAL '150 days', NOW() - INTERVAL '6 hours'),

-- Database Resources
(1, NULL, 'ml-metadata-rds', 'DATABASE', 'RDS PostgreSQL', 'us-east-1', 'us-east-1a',
 'db.r5.xlarge', 'medium', 4, 32.0, 500.0, 'Up to 10 Gbps', NULL,
 'RUNNING', 'HEALTHY', 'ML Metadata', 'PRODUCTION', '{"version": "14.7", "encrypted": true}',
 0.45, 10.80, 42.1, NOW() - INTERVAL '180 days',
 'admin', NOW() - INTERVAL '180 days', NOW() - INTERVAL '5 hours'),

(3, NULL, 'analytics-bigquery', 'DATABASE', 'BigQuery', 'us-central1', 'N/A',
 'Serverless', 'N/A', NULL, NULL, 10000.0, NULL, NULL,
 'RUNNING', 'HEALTHY', 'Analytics', 'PRODUCTION', '{"dataset": "ml_metrics"}',
 250.00, 250.00, NULL, NOW() - INTERVAL '160 days',
 'analytics.team', NOW() - INTERVAL '160 days', NOW() - INTERVAL '8 hours'),

-- GPU Instances
(1, 1, 'gpu-training-p4d-01', 'GPU', 'EC2 P4d', 'us-east-1', 'us-east-1a',
 'p4d.24xlarge', 'xxlarge', 96, 1152.0, 8000.0, '400 Gbps', 'A100 x8',
 'RUNNING', 'HEALTHY', 'Large Model Training', 'PRODUCTION', '{"priority": "high", "preemptible": false}',
 32.77, 786.48, 95.8, NOW() - INTERVAL '10 days',
 'ml.team', NOW() - INTERVAL '10 days', NOW() - INTERVAL '20 minutes'),

(2, 2, 'azure-nda100-v4-01', 'GPU', 'Azure ND A100 v4', 'eastus', 'zone-1',
 'Standard_ND96amsr_A100_v4', 'large', 96, 1900.0, 6400.0, '200 Gbps', 'A100 80GB x8',
 'RUNNING', 'HEALTHY', 'Foundation Model Training', 'PRODUCTION', '{"model": "llm", "distributed": true}',
 27.20, 652.80, 88.6, NOW() - INTERVAL '15 days',
 'ml.team', NOW() - INTERVAL '15 days', NOW() - INTERVAL '1 hour'),

-- Network Resources
(1, NULL, 'vpc-ml-prod', 'NETWORK', 'VPC', 'us-east-1', 'N/A',
 'VPC', 'N/A', NULL, NULL, NULL, '100 Gbps', NULL,
 'RUNNING', 'HEALTHY', 'ML Production Network', 'PRODUCTION', '{"cidr": "10.0.0.0/16"}',
 0.00, 0.00, NULL, NOW() - INTERVAL '200 days',
 'network.team', NOW() - INTERVAL '200 days', NOW() - INTERVAL '12 hours'),

(2, NULL, 'vnet-ml-platform', 'NETWORK', 'Virtual Network', 'eastus', 'N/A',
 'VNet', 'N/A', NULL, NULL, NULL, '100 Gbps', NULL,
 'RUNNING', 'HEALTHY', 'ML Platform Network', 'PRODUCTION', '{"address_space": "172.16.0.0/12"}',
 0.00, 0.00, NULL, NOW() - INTERVAL '170 days',
 'network.team', NOW() - INTERVAL '170 days', NOW() - INTERVAL '10 hours'),

-- Terminated/Testing Resources
(1, NULL, 'test-instance-abc', 'COMPUTE', 'EC2', 'us-east-1', 'us-east-1c',
 't3.micro', 'micro', 2, 1.0, 30.0, '5 Gbps', NULL,
 'TERMINATED', 'HEALTHY', 'Testing', 'TESTING', '{"temporary": true}',
 0.00, 0.00, 0.0, NOW() - INTERVAL '5 days',
 'test.team', NOW() - INTERVAL '5 days', NOW() - INTERVAL '2 days'),

(4, NULL, 'onprem-gpu-server-01', 'GPU', 'Physical Server', 'datacenter-1', 'rack-A',
 'Custom Build', 'N/A', 64, 512.0, 4000.0, '25 Gbps', 'RTX 3090 x4',
 'RUNNING', 'HEALTHY', 'Research Computing', 'DEVELOPMENT', '{"location": "building-3"}',
 0.00, 500.00, 72.4, NOW() - INTERVAL '300 days',
 'ops.team', NOW() - INTERVAL '300 days', NOW() - INTERVAL '7 hours');

-- Comentarios
COMMENT ON TABLE INFCLOUDPROVIDERS IS 'Proveedores cloud configurados para la plataforma';
COMMENT ON TABLE INFKUBERNETESCLUSTERS IS 'Clusters Kubernetes para orquestación de workloads ML';
COMMENT ON TABLE INFCLOUDRESOURCES IS 'Recursos cloud diversos (compute, storage, network, GPU, database)';

