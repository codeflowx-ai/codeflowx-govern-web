-- Funciones para generar datos de prueba de RAG

-- Función para generar proyectos RAG
CREATE OR REPLACE FUNCTION generate_rag_projects() 
RETURNS void AS $$
DECLARE
    i integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESRAGPROJECT CASCADE;
    
    -- Generar proyectos RAG
    FOR i IN 1..10 LOOP
        INSERT INTO SLESRAGPROJECT (
            idxslesragproject,
            projectname,
            description,
            configuration,
            creationdate
        ) VALUES (
            i,
            'RAG Project ' || i,
            'Description for RAG project ' || i,
            jsonb_build_object(
                'chunk_size', 500 * i,
                'overlap', 50,
                'embedding_model', 'model_' || (1 + mod(i, 3)),
                'similarity_threshold', 0.7,
                'max_tokens', 2000,
                'temperature', 0.7
            ),
            current_timestamp - (i || ' days')::interval
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar documentos RAG
CREATE OR REPLACE FUNCTION generate_rag_documents() 
RETURNS void AS $$
DECLARE
    i integer;
    project_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESRAGDOCUMENT CASCADE;
    
    -- Generar documentos
    FOR i IN 1..50 LOOP
        project_id := 1 + mod(i, 10);
        
        INSERT INTO SLESRAGDOCUMENT (
            idxslesragdocument,
            idslesragproject,
            title,
            content,
            metadata,
            embedding,
            processingdate
        ) VALUES (
            i,
            project_id,
            'Document ' || i,
            'Content for document ' || i || '. This is a sample content with multiple sentences. ' ||
            'It includes various topics and information that will be processed by the RAG system. ' ||
            'The content is designed to be chunked and embedded properly.',
            jsonb_build_object(
                'source', 'source_' || (1 + mod(i, 5)),
                'author', 'author_' || (1 + mod(i, 3)),
                'created_date', current_date - (i || ' days')::interval,
                'tags', array['tag1_' || i, 'tag2_' || i]
            ),
            '[' || array_to_string(ARRAY(
                SELECT random() 
                FROM generate_series(1, 384)
            ), ',') || ']',
            current_timestamp - (i || ' hours')::interval
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar chunks
CREATE OR REPLACE FUNCTION generate_rag_chunks() 
RETURNS void AS $$
DECLARE
    i integer;
    doc_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESRAGCHUNK CASCADE;
    
    -- Generar chunks
    FOR i IN 1..200 LOOP
        doc_id := 1 + mod(i, 50);
        
        INSERT INTO SLESRAGCHUNK (
            idxslesragchunk,
            idslesragdocument,
            content,
            embedding,
            metadata,
            chunknumber
        ) VALUES (
            i,
            doc_id,
            'Chunk ' || i || ' of document ' || doc_id || '. This is a segment of the original content. ' ||
            'It contains specific information that can be retrieved based on similarity search.',
            '[' || array_to_string(ARRAY(
                SELECT random() 
                FROM generate_series(1, 384)
            ), ',') || ']',
            jsonb_build_object(
                'start_index', (i - 1) * 500,
                'end_index', i * 500,
                'similarity_score', random(),
                'relevance_score', random()
            ),
            1 + mod(i, 5)
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar conversaciones RAG
CREATE OR REPLACE FUNCTION generate_rag_conversations() 
RETURNS void AS $$
DECLARE
    i integer;
    project_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESRAGCONVERSATION CASCADE;
    
    -- Generar conversaciones
    FOR i IN 1..100 LOOP
        project_id := 1 + mod(i, 10);
        
        INSERT INTO SLESRAGCONVERSATION (
            idxslesragconversation,
            idslesragproject,
            sessionid,
            question,
            answer,
            context,
            conversationdate
        ) VALUES (
            i,
            project_id,
            'session_' || (1 + mod(i, 20)),
            'Question ' || i || '? This is a sample question about the content.',
            'Answer ' || i || '. This is a generated answer based on the retrieved context.',
            jsonb_build_object(
                'retrieved_chunks', ARRAY[
                    1 + mod(i, 200),
                    2 + mod(i, 200),
                    3 + mod(i, 200)
                ],
                'similarity_scores', ARRAY[
                    random(),
                    random(),
                    random()
                ],
                'processing_time', random() * 1000,
                'tokens_used', floor(random() * 500)
            ),
            current_timestamp - (i || ' minutes')::interval
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar evaluaciones RAG
CREATE OR REPLACE FUNCTION generate_rag_evaluations() 
RETURNS void AS $$
DECLARE
    i integer;
    conversation_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESRAGEVALUATION CASCADE;
    
    -- Generar evaluaciones
    FOR i IN 1..100 LOOP
        conversation_id := i;
        
        INSERT INTO SLESRAGEVALUATION (
            idxslesragevaluation,
            idslesragconversation,
            evaluationdate,
            relevancescore,
            accuracyscore,
            completenesscore,
            evaluationdetails,
            improvements
        ) VALUES (
            i,
            conversation_id,
            current_timestamp - (i || ' minutes')::interval,
            random(),
            random(),
            random(),
            jsonb_build_object(
                'context_relevance', random(),
                'answer_coherence', random(),
                'factual_accuracy', random(),
                'response_quality', random()
            ),
            jsonb_build_object(
                'suggestions', array[
                    'Improve context retrieval',
                    'Enhance answer generation',
                    'Optimize chunk size'
                ],
                'priority', mod(i, 3) + 1
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar optimizaciones RAG
CREATE OR REPLACE FUNCTION generate_rag_optimizations() 
RETURNS void AS $$
DECLARE
    i integer;
    project_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESRAGOPTIMIZATION CASCADE;
    
    -- Generar optimizaciones
    FOR i IN 1..50 LOOP
        project_id := 1 + mod(i, 10);
        
        INSERT INTO SLESRAGOPTIMIZATION (
            idxslesragoptimization,
            idslesragproject,
            optimizationdate,
            chunkingconfig,
            embeddingconfig,
            retrievalconfig,
            rerankerconfig,
            optimizationresults
        ) VALUES (
            i,
            project_id,
            current_timestamp - (i || ' hours')::interval,
            jsonb_build_object(
                'chunk_size', 500 + (i * 10),
                'overlap', 50,
                'chunking_strategy', 'paragraph'
            ),
            jsonb_build_object(
                'model', 'embedding_model_' || (1 + mod(i, 3)),
                'dimensions', 384,
                'batch_size', 32
            ),
            jsonb_build_object(
                'similarity_metric', 'cosine',
                'top_k', 3,
                'threshold', 0.7
            ),
            jsonb_build_object(
                'model', 'reranker_' || (1 + mod(i, 2)),
                'threshold', 0.8
            ),
            jsonb_build_object(
                'performance_improvement', random() * 30,
                'accuracy_improvement', random() * 20,
                'latency_reduction', random() * 25
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función principal para generar todos los datos de prueba de RAG
CREATE OR REPLACE FUNCTION generate_all_rag_test_data() 
RETURNS void AS $$
BEGIN
    PERFORM generate_rag_projects();
    PERFORM generate_rag_documents();
    PERFORM generate_rag_chunks();
    PERFORM generate_rag_conversations();
    PERFORM generate_rag_evaluations();
    PERFORM generate_rag_optimizations();
END;
$$ LANGUAGE plpgsql;

-- Para ejecutar la generación completa de datos:
-- SELECT generate_all_rag_test_data();

-- Para ejecutar funciones individuales:
-- SELECT generate_rag_projects();
-- SELECT generate_rag_documents();
-- SELECT generate_rag_chunks();
-- SELECT generate_rag_conversations();
-- SELECT generate_rag_evaluations();
-- SELECT generate_rag_optimizations();

/**
Este script:

Genera proyectos RAG con diferentes configuraciones
Crea documentos de ejemplo con metadatos
Genera chunks con embeddings simulados
Crea conversaciones de ejemplo
Añade evaluaciones de calidad
Genera datos de optimización

Características principales:

Datos realistas y relacionados
Embeddings simulados (384 dimensiones)
Timestamps escalonados
Metadatos en formato JSONB
Relaciones entre entidades mantenidas
Valores aleatorios pero coherentes */