-- ============================================================================
-- MOCK DATA - MODEL PROVIDERS MODULE
-- ============================================================================

-- Inserción de proveedores de modelos de prueba
INSERT INTO MODPROVIDERS (
    MODNAME, MODDISPLAYNAME, MODDESCRIPTION, MODPROVIDERTYPE, MODBASEURL, MODAPIVERSION,
    MODSTATUS, MODPRICINGMODEL, MODPRICINGDETAILS, MODHASFREETIER, MODAUTHTYPE, MODAUTHCONFIG,
    MODSUPPORTSTEXT, MODSUPPORTSEMBEDDINGS, MODSUPPORTSAUDIO, MODSUPPORTSVISION, 
    MODSUPPORTSMULTIMODAL, MODSUPPORTSFUNCTIONCALLING, MODSUPPORTSSTREAMING, MODSUPPORTSFINETUNING,
    MODRATELIMITREQUESTS, MODRATELIMITTOKENS, MODMAXCONTEXTLENGTH, MODMAXOUTPUTTOKENS,
    MODAVAILABLEREGIONS, MODDATARESIDENCY, MODSLAUPTIME, MODAVGRESPONSETIMEMS,
    MODHEALTHCHECKURL, MODDOCUMENTATIONURL, MODCONTACTEMAIL, MODSUPPORTURL,
    MODCREATEDBY, MODUPDATEDBY, MODCREATEDAT, MODUPDATEDAT
) VALUES 
-- OpenAI
('openai', 'OpenAI', 'Official OpenAI API provider for GPT models and embeddings', ARRAY['OPENAI'], 'https://api.openai.com', 'v1', ARRAY['ACTIVE'], 'Pay-as-you-go', '{"input_tokens": 0.0001, "output_tokens": 0.0002, "currency": "USD"}', true, 'API_KEY', '{"header": "Authorization", "prefix": "Bearer"}', true, true, true, true, true, true, true, true, 3500, 90000, 128000, 4096, '["us-east", "eu-west", "asia-pacific"]', '{"regions": ["US", "EU"]}', 99.9, 250, 'https://status.openai.com', 'https://platform.openai.com/docs', 'support@openai.com', 'https://help.openai.com', 'admin', 'admin', NOW() - INTERVAL '180 days', NOW() - INTERVAL '10 days'),

-- Anthropic
('anthropic', 'Anthropic', 'Anthropic Claude models provider', ARRAY['ANTHROPIC'], 'https://api.anthropic.com', 'v1', ARRAY['ACTIVE'], 'Pay-as-you-go', '{"input_tokens": 0.00008, "output_tokens": 0.00024, "currency": "USD"}', false, 'API_KEY', '{"header": "x-api-key", "prefix": ""}', true, false, false, true, true, true, true, false, 4000, 100000, 200000, 8192, '["us-east", "us-west"]', '{"regions": ["US"]}', 99.95, 180, 'https://status.anthropic.com', 'https://docs.anthropic.com', 'support@anthropic.com', 'https://support.anthropic.com', 'admin', 'admin', NOW() - INTERVAL '150 days', NOW() - INTERVAL '15 days'),

-- Azure OpenAI
('azure-openai', 'Azure OpenAI Service', 'Microsoft Azure OpenAI Service with enterprise features', ARRAY['AZURE'], 'https://{resource}.openai.azure.com', '2023-05-15', ARRAY['ACTIVE'], 'Subscription', '{"monthly_base": 500, "overage_per_1k": 0.002, "currency": "USD"}', false, 'API_KEY', '{"header": "api-key", "prefix": ""}', true, true, true, true, true, true, true, true, 6000, 120000, 128000, 4096, '["us-east-2", "eu-west-1", "asia-southeast"]', '{"regions": ["US", "EU", "ASIA"]}', 99.99, 120, 'https://status.azure.com/openai', 'https://learn.microsoft.com/azure/openai', 'azure-support@microsoft.com', 'https://support.azure.com', 'admin', 'admin', NOW() - INTERVAL '200 days', NOW() - INTERVAL '5 days'),

-- AWS Bedrock
('aws-bedrock', 'AWS Bedrock', 'Amazon Web Services Bedrock foundation models', ARRAY['AWS'], 'https://bedrock-runtime.{region}.amazonaws.com', '2023-09-30', ARRAY['ACTIVE'], 'Pay-as-you-go', '{"pricing_varies": true, "currency": "USD"}', true, 'AWS_SIGNATURE', '{"signature_version": "v4", "service": "bedrock"}', true, true, false, true, true, false, true, false, 10000, 200000, 100000, 4096, '["us-east-1", "us-west-2", "eu-central-1", "ap-southeast-1"]', '{"regions": ["US", "EU", "ASIA"]}', 99.99, 200, 'https://status.aws.amazon.com', 'https://docs.aws.amazon.com/bedrock', 'aws-support@amazon.com', 'https://support.aws.amazon.com', 'admin', 'admin', NOW() - INTERVAL '120 days', NOW() - INTERVAL '20 days'),

-- Google Vertex AI
('google-vertex', 'Google Vertex AI', 'Google Cloud Vertex AI platform for Gemini and PaLM models', ARRAY['GOOGLE'], 'https://{region}-aiplatform.googleapis.com', 'v1', ARRAY['ACTIVE'], 'Pay-as-you-go', '{"input_characters": 0.000125, "output_characters": 0.000375, "currency": "USD"}', true, 'OAUTH2', '{"oauth_endpoint": "https://oauth2.googleapis.com/token"}', true, true, true, true, true, true, true, false, 6000, 120000, 32768, 8192, '["us-central1", "europe-west4", "asia-northeast1"]', '{"regions": ["US", "EU", "ASIA"]}', 99.95, 180, 'https://status.cloud.google.com', 'https://cloud.google.com/vertex-ai/docs', 'vertex-support@google.com', 'https://cloud.google.com/support', 'admin', 'admin', NOW() - INTERVAL '160 days', NOW() - INTERVAL '12 days'),

-- HuggingFace Inference API
('huggingface', 'HuggingFace', 'HuggingFace Inference API for open-source models', ARRAY['HUGGINGFACE'], 'https://api-inference.huggingface.co', 'v1', ARRAY['ACTIVE'], 'Freemium', '{"free_tier": 30000, "pro_monthly": 9, "enterprise_custom": true, "currency": "USD"}', true, 'API_KEY', '{"header": "Authorization", "prefix": "Bearer"}', true, true, true, true, true, false, true, true, 1000, 10000, 8192, 2048, '["us-east", "eu-west"]', '{"regions": ["US", "EU"]}', 99.5, 350, 'https://status.huggingface.co', 'https://huggingface.co/docs', 'support@huggingface.co', 'https://huggingface.co/support', 'admin', 'admin', NOW() - INTERVAL '250 days', NOW() - INTERVAL '30 days'),

-- Custom Local LLM
('local-llama', 'Local LLaMA Server', 'Self-hosted LLaMA model server for internal use', ARRAY['CUSTOM'], 'http://llama-server.internal.company.com:8080', 'v1', ARRAY['ACTIVE'], 'Fixed Cost', '{"monthly_infrastructure": 500, "currency": "USD"}', false, 'API_KEY', '{"header": "X-API-Key", "prefix": ""}', true, true, false, false, false, false, true, true, NULL, NULL, 4096, 2048, '["on-premise"]', '{"regions": ["ON_PREMISE"]}', 99.0, 450, 'http://llama-server.internal.company.com:8080/health', 'http://wiki.company.com/llama-docs', 'ai-team@company.com', 'http://wiki.company.com/llama-support', 'ai.team', 'ai.team', NOW() - INTERVAL '90 days', NOW() - INTERVAL '7 days'),

-- Cohere
('cohere', 'Cohere', 'Cohere AI platform for embeddings and generation', ARRAY['CUSTOM'], 'https://api.cohere.ai', 'v1', ARRAY['ACTIVE'], 'Pay-as-you-go', '{"input_tokens": 0.0001, "output_tokens": 0.0002, "currency": "USD"}', true, 'API_KEY', '{"header": "Authorization", "prefix": "Bearer"}', true, true, false, false, false, false, true, true, 10000, 40000, 2048, 1024, '["us-east", "eu-west"]', '{"regions": ["US", "EU"]}', 99.8, 200, 'https://status.cohere.ai', 'https://docs.cohere.ai', 'support@cohere.ai', 'https://cohere.ai/support', 'admin', 'admin', NOW() - INTERVAL '100 days', NOW() - INTERVAL '18 days'),

-- Custom On-Premise (Inactive)
('internal-gpt', 'Internal GPT Clone', 'Internal experimental GPT implementation - deprecated', ARRAY['CUSTOM'], 'http://internal-gpt.company.com', 'v0.1', ARRAY['DEPRECATED'], 'Free', '{"cost": 0}', true, 'NONE', '{}', true, false, false, false, false, false, false, false, 100, 1000, 2048, 512, '["on-premise"]', '{"regions": ["ON_PREMISE"]}', 95.0, 800, NULL, NULL, 'deprecated@company.com', NULL, 'research.team', 'research.team', NOW() - INTERVAL '400 days', NOW() - INTERVAL '200 days'),

-- Mistral AI
('mistral', 'Mistral AI', 'Mistral AI models provider', ARRAY['CUSTOM'], 'https://api.mistral.ai', 'v1', ARRAY['ACTIVE'], 'Pay-as-you-go', '{"input_tokens": 0.00007, "output_tokens": 0.00021, "currency": "EUR"}', false, 'API_KEY', '{"header": "Authorization", "prefix": "Bearer"}', true, true, false, false, false, true, true, false, 5000, 80000, 32768, 4096, '["eu-west", "us-east"]', '{"regions": ["EU", "US"]}', 99.7, 220, 'https://status.mistral.ai', 'https://docs.mistral.ai', 'contact@mistral.ai', 'https://mistral.ai/support', 'admin', 'admin', NOW() - INTERVAL '60 days', NOW() - INTERVAL '8 days');

-- Credenciales de proveedores (ejemplos ficticios)
INSERT INTO MODPROVIDERCREDENTIALS (
    IDMODPROVIDERS0, MODCREDENTIALNAME, MODCREDENTIALTYPE, MODENCRYPTEDVALUE,
    MODENCRYPTIONMETHOD, MODENVIRONMENT, MODSTATUS, MODEXPIRESAT, MODLASTUSEDAT,
    MODUSAGECOUNT, MODROTATIONENABLED, MODROTATIONDAYS, MODNEXTROTATION,
    MODDAILYQUOTATOKENS, MODTOKENSUSEDTODAY, MODMONTHLYBUDGET, MODCURRENTMONTHCOST,
    MODCREATEDBY, MODUPDATEDBY, MODLASTROTATEDBY, MODCREATEDAT, MODUPDATEDAT
) VALUES
-- OpenAI Production Key
(1, 'OpenAI Production Key', 'API_KEY', 'ENCRYPTED:sk-proj-abc123...xyz789',
 'AES-256-GCM', 'PRODUCTION', 'ACTIVE', NULL, NOW() - INTERVAL '2 hours',
 45678, true, 90, NOW() + INTERVAL '60 days',
 1000000, 234567, 5000.00, 3421.50,
 'admin', 'admin', 'admin', NOW() - INTERVAL '90 days', NOW() - INTERVAL '10 days'),

-- OpenAI Development Key
(1, 'OpenAI Development Key', 'API_KEY', 'ENCRYPTED:sk-dev-def456...uvw123',
 'AES-256-GCM', 'DEVELOPMENT', 'ACTIVE', NULL, NOW() - INTERVAL '5 hours',
 12345, true, 90, NOW() + INTERVAL '70 days',
 100000, 23456, 500.00, 123.45,
 'admin', 'admin', 'admin', NOW() - INTERVAL '85 days', NOW() - INTERVAL '8 days'),

-- Anthropic Production Key
(2, 'Anthropic Main Key', 'API_KEY', 'ENCRYPTED:sk-ant-abc789...def456',
 'AES-256-GCM', 'PRODUCTION', 'ACTIVE', NULL, NOW() - INTERVAL '1 hour',
 23456, false, NULL, NULL,
 500000, 123456, 3000.00, 1890.75,
 'admin', 'admin', NULL, NOW() - INTERVAL '60 days', NOW() - INTERVAL '15 days'),

-- Azure OpenAI Key 1
(3, 'Azure OpenAI Key 1', 'API_KEY', 'ENCRYPTED:azure-key-123abc...xyz789',
 'AES-256-GCM', 'PRODUCTION', 'ACTIVE', NULL, NOW() - INTERVAL '30 minutes',
 67890, true, 60, NOW() + INTERVAL '45 days',
 2000000, 456789, 10000.00, 7654.32,
 'admin', 'admin', 'admin', NOW() - INTERVAL '120 days', NOW() - INTERVAL '5 days'),

-- AWS Bedrock Credentials
(4, 'AWS Bedrock Prod', 'AWS_SIGNATURE', 'ENCRYPTED:AKIAIOSFODNN7EXAMPLE',
 'AES-256-GCM', 'PRODUCTION', 'ACTIVE', NOW() + INTERVAL '180 days', NOW() - INTERVAL '3 hours',
 34567, true, 90, NOW() + INTERVAL '80 days',
 1500000, 345678, 8000.00, 5432.10,
 'admin', 'admin', 'admin', NOW() - INTERVAL '100 days', NOW() - INTERVAL '20 days'),

-- Google Vertex AI Service Account
(5, 'Vertex AI Service Account', 'OAUTH2', 'ENCRYPTED:{"type":"service_account",...}',
 'AES-256-GCM', 'PRODUCTION', 'ACTIVE', NULL, NOW() - INTERVAL '4 hours',
 28901, false, NULL, NULL,
 1200000, 289012, 6000.00, 4123.67,
 'admin', 'admin', NULL, NOW() - INTERVAL '110 days', NOW() - INTERVAL '12 days'),

-- HuggingFace Free Tier
(6, 'HuggingFace Free API', 'API_KEY', 'ENCRYPTED:hf_abc123...xyz789',
 'AES-256-GCM', 'DEVELOPMENT', 'ACTIVE', NULL, NOW() - INTERVAL '6 hours',
 5678, false, NULL, NULL,
 30000, 12345, 0.00, 0.00,
 'dev.team', 'dev.team', NULL, NOW() - INTERVAL '200 days', NOW() - INTERVAL '30 days'),

-- HuggingFace Pro
(6, 'HuggingFace Pro Key', 'API_KEY', 'ENCRYPTED:hf-pro-def456...uvw123',
 'AES-256-GCM', 'PRODUCTION', 'ACTIVE', NOW() + INTERVAL '365 days', NOW() - INTERVAL '1 hour',
 15678, true, 365, NOW() + INTERVAL '300 days',
 NULL, NULL, 9.00, 9.00,
 'admin', 'admin', NULL, NOW() - INTERVAL '150 days', NOW() - INTERVAL '25 days'),

-- Local LLaMA (no real credentials)
(7, 'Local LLaMA Access', 'API_KEY', 'ENCRYPTED:local-key-12345',
 'AES-256-GCM', 'PRODUCTION', 'ACTIVE', NULL, NOW() - INTERVAL '10 minutes',
 89012, false, NULL, NULL,
 NULL, NULL, 0.00, 500.00,
 'ai.team', 'ai.team', NULL, NOW() - INTERVAL '90 days', NOW() - INTERVAL '7 days'),

-- Cohere Production
(8, 'Cohere Production Key', 'API_KEY', 'ENCRYPTED:co-abc789...xyz456',
 'AES-256-GCM', 'PRODUCTION', 'ACTIVE', NULL, NOW() - INTERVAL '8 hours',
 18901, true, 90, NOW() + INTERVAL '55 days',
 800000, 178901, 4000.00, 2876.54,
 'admin', 'admin', 'admin', NOW() - INTERVAL '80 days', NOW() - INTERVAL '18 days'),

-- Mistral AI Production
(10, 'Mistral Production Key', 'API_KEY', 'ENCRYPTED:mst-abc123...def789',
 'AES-256-GCM', 'PRODUCTION', 'ACTIVE', NULL, NOW() - INTERVAL '2 hours',
 9876, false, NULL, NULL,
 600000, 98765, 2500.00, 1432.89,
 'admin', 'admin', NULL, NOW() - INTERVAL '50 days', NOW() - INTERVAL '8 days');

-- Comentarios
COMMENT ON TABLE MODPROVIDERS IS 'Proveedores de modelos de prueba con diferentes tipos y configuraciones';
COMMENT ON TABLE MODPROVIDERCREDENTIALS IS 'Credenciales de acceso a proveedores (valores encriptados ficticios)';

