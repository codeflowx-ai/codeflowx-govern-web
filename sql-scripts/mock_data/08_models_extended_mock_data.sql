-- ============================================================================
-- MOCK DATA - MODELS MODULE (40+ modelos adicionales)
-- ============================================================================

-- Insertar 40+ modelos de prueba con variedad de proveedores, tipos y estados

-- MODELOS OPENAI (10 modelos)
INSERT INTO MODMODELS (
    MODNAME, MODDESCRIPTION, MODVERSION, MODTYPE, MODFRAMEWORK, 
    MODSTATUS, MODAPPROVALSTATUS, IDMODPROVIDER, 
    MODCREATEDBY, MODCREATEDAT
) VALUES
('GPT-4 Turbo', 'Modelo LLM avanzado con ventana de contexto extendida', '1.0.0', ARRAY['LLM'], ARRAY['OpenAI'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 1, 'admin', NOW() - INTERVAL '30 days'),
 
('GPT-4 Vision', 'Modelo multimodal con capacidad de procesamiento de imágenes', '1.0.0', ARRAY['MULTIMODAL','VISION'], ARRAY['OpenAI'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 1, 'admin', NOW() - INTERVAL '25 days'),
 
('GPT-3.5 Turbo', 'Modelo LLM optimizado para conversación', '0.3.0', ARRAY['LLM'], ARRAY['OpenAI'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 1, 'admin', NOW() - INTERVAL '60 days'),
 
('GPT-3.5 Turbo 16k', 'Versión con contexto extendido de 16k tokens', '0.3.1', ARRAY['LLM'], ARRAY['OpenAI'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 1, 'admin', NOW() - INTERVAL '55 days'),
 
('DALL-E 3', 'Generación de imágenes de alta calidad', '3.0.0', ARRAY['VISION'], ARRAY['OpenAI'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 1, 'admin', NOW() - INTERVAL '20 days'),
 
('Whisper Large', 'Modelo de transcripción de audio multilingüe', '1.0.0', ARRAY['AUDIO'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 1, 'admin', NOW() - INTERVAL '40 days'),
 
('Text Embedding Ada 002', 'Modelo de embeddings para búsqueda semántica', '2.0.0', ARRAY['EMBEDDING'], ARRAY['OpenAI'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 1, 'admin', NOW() - INTERVAL '90 days'),
 
('GPT-4 Base', 'Modelo base sin fine-tuning', '1.0.0', ARRAY['LLM'], ARRAY['OpenAI'], 
 ARRAY['DEVELOPMENT'], ARRAY['PENDING_APPROVAL'], 1, 'developer1', NOW() - INTERVAL '5 days'),
 
('GPT-4 Fine-tuned Legal', 'Modelo fine-tuned para casos legales', '1.1.0', ARRAY['LLM'], ARRAY['OpenAI'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 1, 'data_scientist', NOW() - INTERVAL '15 days'),
 
('Codex Davinci', 'Modelo especializado en generación de código', '1.0.0', ARRAY['LLM'], ARRAY['OpenAI'], 
 ARRAY['RETIRED'], ARRAY['REJECTED'], 1, 'admin', NOW() - INTERVAL '180 days');

-- MODELOS ANTHROPIC (8 modelos)
INSERT INTO MODMODELS (
    MODNAME, MODDESCRIPTION, MODVERSION, MODTYPE, MODFRAMEWORK, 
    MODSTATUS, MODAPPROVALSTATUS, IDMODPROVIDER, 
    MODCREATEDBY, MODCREATEDAT
) VALUES
('Claude 3 Opus', 'Modelo más potente de Anthropic para tareas complejas', '3.0.0', ARRAY['LLM'], ARRAY['Anthropic'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 2, 'admin', NOW() - INTERVAL '10 days'),
 
('Claude 3 Sonnet', 'Balance entre capacidad y velocidad', '3.0.0', ARRAY['LLM'], ARRAY['Anthropic'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 2, 'admin', NOW() - INTERVAL '10 days'),
 
('Claude 3 Haiku', 'Modelo rápido y eficiente para tareas simples', '3.0.0', ARRAY['LLM'], ARRAY['Anthropic'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 2, 'admin', NOW() - INTERVAL '10 days'),
 
('Claude 2.1', 'Versión anterior con ventana de contexto 200k', '2.1.0', ARRAY['LLM'], ARRAY['Anthropic'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 2, 'admin', NOW() - INTERVAL '45 days'),
 
('Claude 2', 'Modelo de segunda generación', '2.0.0', ARRAY['LLM'], ARRAY['Anthropic'], 
 ARRAY['INACTIVE'], ARRAY['APPROVED'], 2, 'admin', NOW() - INTERVAL '90 days'),
 
('Claude Instant', 'Versión ligera y rápida', '1.2.0', ARRAY['LLM'], ARRAY['Anthropic'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 2, 'admin', NOW() - INTERVAL '70 days'),
 
('Claude 3.5 Sonnet', 'Nueva generación mejorada', '3.5.0', ARRAY['LLM'], ARRAY['Anthropic'], 
 ARRAY['DEVELOPMENT'], ARRAY['PENDING_APPROVAL'], 2, 'developer2', NOW() - INTERVAL '2 days'),
 
('Claude Constitutional AI', 'Modelo con énfasis en seguridad', '1.0.0', ARRAY['LLM'], ARRAY['Anthropic'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 2, 'ml_engineer', NOW() - INTERVAL '35 days');

-- MODELOS GOOGLE (8 modelos)
INSERT INTO MODMODELS (
    MODNAME, MODDESCRIPTION, MODVERSION, MODTYPE, MODFRAMEWORK, 
    MODSTATUS, MODAPPROVALSTATUS, IDMODPROVIDER, 
    MODCREATEDBY, MODCREATEDAT
) VALUES
('Gemini Pro', 'Modelo multimodal de Google para tareas complejas', '1.0.0', ARRAY['LLM','MULTIMODAL'], ARRAY['Google'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 3, 'admin', NOW() - INTERVAL '12 days'),
 
('Gemini Ultra', 'Versión más avanzada con capacidades extendidas', '1.0.0', ARRAY['LLM','MULTIMODAL'], ARRAY['Google'], 
 ARRAY['DEVELOPMENT'], ARRAY['UNDER_REVIEW'], 3, 'developer3', NOW() - INTERVAL '3 days'),
 
('PaLM 2', 'Modelo de lenguaje de segunda generación', '2.0.0', ARRAY['LLM'], ARRAY['Google'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 3, 'admin', NOW() - INTERVAL '50 days'),
 
('Bard Pro', 'Modelo conversacional avanzado', '1.5.0', ARRAY['LLM'], ARRAY['Google'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 3, 'admin', NOW() - INTERVAL '22 days'),
 
('Gemini Nano', 'Versión optimizada para dispositivos', '1.0.0', ARRAY['LLM'], ARRAY['TensorFlow'], 
 ARRAY['DEVELOPMENT'], ARRAY['PENDING_APPROVAL'], 3, 'mobile_dev', NOW() - INTERVAL '7 days'),
 
('Imagen 2', 'Generación de imágenes de alta resolución', '2.0.0', ARRAY['VISION'], ARRAY['Google'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 3, 'admin', NOW() - INTERVAL '18 days'),
 
('MusicLM', 'Generación de música a partir de texto', '1.0.0', ARRAY['AUDIO'], ARRAY['Google'], 
 ARRAY['DEVELOPMENT'], ARRAY['PENDING_APPROVAL'], 3, 'researcher', NOW() - INTERVAL '8 days'),
 
('Universal Sentence Encoder', 'Embeddings multilingües', '4.0.0', ARRAY['EMBEDDING'], ARRAY['TensorFlow'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 3, 'ml_engineer', NOW() - INTERVAL '100 days');

-- MODELOS META (6 modelos)
INSERT INTO MODMODELS (
    MODNAME, MODDESCRIPTION, MODVERSION, MODTYPE, MODFRAMEWORK, 
    MODSTATUS, MODAPPROVALSTATUS, IDMODPROVIDER, 
    MODCREATEDBY, MODCREATEDAT
) VALUES
('Llama 3 70B', 'Modelo open-source de 70 billones de parámetros', '3.0.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 4, 'admin', NOW() - INTERVAL '14 days'),
 
('Llama 3 8B', 'Versión compacta para deployment eficiente', '3.0.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 4, 'admin', NOW() - INTERVAL '14 days'),
 
('Llama 2 Chat', 'Modelo optimizado para conversación', '2.0.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 4, 'admin', NOW() - INTERVAL '120 days'),
 
('CodeLlama 34B', 'Especializado en generación de código', '1.0.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 4, 'backend_dev', NOW() - INTERVAL '80 days'),
 
('Llama Guard', 'Modelo de moderación de contenido', '1.0.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 4, 'security_team', NOW() - INTERVAL '25 days'),
 
('Llama 3 Fine-tuned Medical', 'Fine-tuning para diagnósticos médicos', '3.1.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['DEVELOPMENT'], ARRAY['UNDER_REVIEW'], 4, 'healthcare_team', NOW() - INTERVAL '4 days');

-- MODELOS MISTRAL (6 modelos)
INSERT INTO MODMODELS (
    MODNAME, MODDESCRIPTION, MODVERSION, MODTYPE, MODFRAMEWORK, 
    MODSTATUS, MODAPPROVALSTATUS, IDMODPROVIDER, 
    MODCREATEDBY, MODCREATEDAT
) VALUES
('Mistral 7B', 'Modelo eficiente de 7B parámetros', '0.1.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 5, 'admin', NOW() - INTERVAL '28 days'),
 
('Mistral 8x7B MoE', 'Mixture of Experts con 8 expertos', '0.1.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 5, 'admin', NOW() - INTERVAL '26 days'),
 
('Mistral Medium', 'Versión optimizada para producción', '1.0.0', ARRAY['LLM'], ARRAY['Mistral'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 5, 'admin', NOW() - INTERVAL '20 days'),
 
('Mistral Small', 'Modelo compacto para bajo costo', '1.0.0', ARRAY['LLM'], ARRAY['Mistral'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 5, 'admin', NOW() - INTERVAL '20 days'),
 
('Mistral Large', 'Modelo más potente de la suite', '1.0.0', ARRAY['LLM'], ARRAY['Mistral'], 
 ARRAY['DEVELOPMENT'], ARRAY['PENDING_APPROVAL'], 5, 'developer4', NOW() - INTERVAL '6 days'),
 
('Mistral Code', 'Especializado en programación', '0.2.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 5, 'devops', NOW() - INTERVAL '15 days');

-- MODELOS COHERE (5 modelos)
INSERT INTO MODMODELS (
    MODNAME, MODDESCRIPTION, MODVERSION, MODTYPE, MODFRAMEWORK, 
    MODSTATUS, MODAPPROVALSTATUS, IDMODPROVIDER, 
    MODCREATEDBY, MODCREATEDAT
) VALUES
('Command R Plus', 'Modelo para comandos y retrieval', '1.0.0', ARRAY['LLM'], ARRAY['Cohere'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 6, 'admin', NOW() - INTERVAL '18 days'),
 
('Command R', 'Versión estándar para producción', '1.0.0', ARRAY['LLM'], ARRAY['Cohere'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 6, 'admin', NOW() - INTERVAL '18 days'),
 
('Embed v3 English', 'Embeddings optimizados para inglés', '3.0.0', ARRAY['EMBEDDING'], ARRAY['Cohere'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 6, 'ml_team', NOW() - INTERVAL '35 days'),
 
('Embed v3 Multilingual', 'Embeddings multilingües', '3.0.0', ARRAY['EMBEDDING'], ARRAY['Cohere'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 6, 'ml_team', NOW() - INTERVAL '35 days'),
 
('Rerank v3', 'Modelo de re-ranking para búsqueda', '3.0.0', ARRAY['LLM'], ARRAY['Cohere'], 
 ARRAY['DEVELOPMENT'], ARRAY['PENDING_APPROVAL'], 6, 'search_team', NOW() - INTERVAL '9 days');

-- MODELOS PERSONALIZADOS / OPEN SOURCE (11 modelos)
INSERT INTO MODMODELS (
    MODNAME, MODDESCRIPTION, MODVERSION, MODTYPE, MODFRAMEWORK, 
    MODSTATUS, MODAPPROVALSTATUS, 
    MODCREATEDBY, MODCREATEDAT
) VALUES
('BERT Base Uncased', 'Modelo de lenguaje bidireccional', '1.0.0', ARRAY['LLM'], ARRAY['TensorFlow','PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 'research_team', NOW() - INTERVAL '200 days'),
 
('RoBERTa Large', 'BERT optimizado con robustez', '1.0.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 'research_team', NOW() - INTERVAL '180 days'),
 
('T5 Base', 'Text-to-Text Transfer Transformer', '1.1.0', ARRAY['LLM'], ARRAY['TensorFlow'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 'ml_engineer', NOW() - INTERVAL '150 days'),
 
('FLAN-T5 XXL', 'Versión grande fine-tuned', '1.0.0', ARRAY['LLM'], ARRAY['TensorFlow'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 'ml_engineer', NOW() - INTERVAL '60 days'),
 
('Falcon 40B', 'Modelo open-source de alta capacidad', '1.0.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 'ai_team', NOW() - INTERVAL '45 days'),
 
('Stable Diffusion XL', 'Generación de imágenes de alta resolución', '1.0.0', ARRAY['VISION'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 'creative_team', NOW() - INTERVAL '30 days'),
 
('Stable Diffusion 2.1', 'Versión estable anterior', '2.1.0', ARRAY['VISION'], ARRAY['PyTorch'], 
 ARRAY['INACTIVE'], ARRAY['APPROVED'], 'creative_team', NOW() - INTERVAL '120 days'),
 
('CLIP ViT-L/14', 'Modelo vision-language para embeddings', '1.0.0', ARRAY['MULTIMODAL','EMBEDDING'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 'vision_team', NOW() - INTERVAL '90 days'),
 
('Wav2Vec 2.0 Large', 'Reconocimiento de voz auto-supervisado', '2.0.0', ARRAY['AUDIO'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 'audio_team', NOW() - INTERVAL '110 days'),
 
('MPNet Base v2', 'Sentence embeddings de alta calidad', '2.0.0', ARRAY['EMBEDDING'], ARRAY['PyTorch'], 
 ARRAY['ACTIVE'], ARRAY['APPROVED'], 'nlp_team', NOW() - INTERVAL '75 days'),
 
('Custom Sentiment Analyzer', 'Modelo propio para análisis de sentimiento', '1.0.0', ARRAY['LLM'], ARRAY['TensorFlow'], 
 ARRAY['DEVELOPMENT'], ARRAY['PENDING_APPROVAL'], 'analytics_team', NOW() - INTERVAL '11 days');

-- MODELOS EN DIFERENTES ESTADOS (5 modelos para testing)
INSERT INTO MODMODELS (
    MODNAME, MODDESCRIPTION, MODVERSION, MODTYPE, MODFRAMEWORK, 
    MODSTATUS, MODAPPROVALSTATUS, IDMODPROVIDER, 
    MODCREATEDBY, MODCREATEDAT
) VALUES
('Test Model Alpha', 'Modelo experimental en desarrollo', '0.1.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['DEVELOPMENT'], ARRAY['PENDING_APPROVAL'], 1, 'test_user', NOW() - INTERVAL '1 days'),
 
('Test Model Beta', 'Modelo en revisión de compliance', '0.2.0', ARRAY['LLM'], ARRAY['TensorFlow'], 
 ARRAY['DEVELOPMENT'], ARRAY['UNDER_REVIEW'], 2, 'test_user', NOW() - INTERVAL '3 days'),
 
('Deprecated Model X', 'Modelo obsoleto rechazado', '0.5.0', ARRAY['LLM'], ARRAY['Custom'], 
 ARRAY['RETIRED'], ARRAY['REJECTED'], 'old_dev', NOW() - INTERVAL '365 days'),
 
('Production Ready Model', 'Modelo listo para producción', '2.0.0', ARRAY['LLM'], ARRAY['PyTorch'], 
 ARRAY['PRODUCTION'], ARRAY['APPROVED'], 3, 'prod_team', NOW() - INTERVAL '5 days'),
 
('Archived Legacy Model', 'Modelo legacy archivado', '1.0.0', ARRAY['LLM'], ARRAY['Legacy'], 
 ARRAY['INACTIVE'], ARRAY['APPROVED'], 'legacy_team', NOW() - INTERVAL '500 days');

-- Verificación
SELECT 
    COUNT(*) as total_inserted,
    COUNT(*) FILTER (WHERE MODSTATUS = ANY(ARRAY['ACTIVE'])) as active_count,
    COUNT(*) FILTER (WHERE MODAPPROVALSTATUS = ANY(ARRAY['APPROVED'])) as approved_count,
    COUNT(*) FILTER (WHERE IDMODPROVIDER IS NOT NULL) as with_provider
FROM MODMODELS
WHERE MODCREATEDBY IN ('admin', 'developer1', 'developer2', 'developer3', 'developer4', 
                       'data_scientist', 'ml_engineer', 'research_team', 'backend_dev', 
                       'security_team', 'healthcare_team', 'devops', 'ml_team', 'search_team',
                       'ai_team', 'creative_team', 'vision_team', 'audio_team', 'nlp_team',
                       'analytics_team', 'test_user', 'old_dev', 'prod_team', 'legacy_team', 'mobile_dev');

-- Resultado esperado: 45 modelos insertados

