-- Funciones para generar datos de prueba del Marketplace

-- Función para generar datos de items del marketplace
CREATE OR REPLACE FUNCTION generate_marketplace_items() 
RETURNS void AS $$
DECLARE
    item_types text[] := ARRAY['MODEL', 'PROMPT', 'CHAIN', 'AGENT', 'RAG_TEMPLATE'];
    categories text[] := ARRAY['NLP', 'VISION', 'AUDIO', 'MULTIMODAL', 'SPECIALIZED'];
    industries text[] := ARRAY['HEALTHCARE', 'FINANCE', 'RETAIL', 'MANUFACTURING', 'TECHNOLOGY'];
    i integer;
    model_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESMARKETPLACEITEM CASCADE;
    
    -- Generar items del marketplace
    FOR i IN 1..100 LOOP
        model_id := 1 + mod(i, 20); -- Asumiendo 20 modelos base
        
        INSERT INTO SLESMARKETPLACEITEM (
            idxslesmarketplaceitem,
            idslesmodel,
            itemname,
            itemtype,
            price,
            licensing,
            usagemetrics,
            reviews,
            featured,
            metadata
        ) VALUES (
            i,
            model_id,
            'Marketplace Item ' || i,
            item_types[1 + mod(i, 5)],
            (random() * 1000)::numeric(10,2),
            jsonb_build_object(
                'type', 'SUBSCRIPTION',
                'duration', '12 MONTHS',
                'restrictions', array['API_LIMIT', 'USER_LIMIT']
            ),
            jsonb_build_object(
                'total_uses', floor(random() * 10000),
                'avg_response_time', random() * 100,
                'success_rate', random() * 100
            ),
            jsonb_build_object(
                'average_rating', 3 + random() * 2,
                'total_reviews', floor(random() * 100),
                'review_details', array[
                    jsonb_build_object(
                        'user_id', 'user_' || (floor(random() * 100)::text),
                        'rating', floor(random() * 5 + 1),
                        'comment', 'Review comment ' || i,
                        'date', now() - (floor(random() * 30) || ' days')::interval
                    )
                ]
            ),
            random() < 0.2, -- 20% de probabilidad de ser featured
            jsonb_build_object(
                'category', categories[1 + mod(i, 5)],
                'industry', industries[1 + mod(i, 5)],
                'capabilities', array['capability1', 'capability2'],
                'requirements', jsonb_build_object(
                    'min_tokens', 1000,
                    'max_tokens', 10000,
                    'supported_languages', array['EN', 'ES', 'FR']
                ),
                'demo_available', true,
                'documentation_url', 'https://docs.example.com/item' || i
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de precios y descuentos
CREATE OR REPLACE FUNCTION generate_marketplace_pricing() 
RETURNS void AS $$
DECLARE
    i integer;
    item_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESMARKETPRICING CASCADE;
    
    -- Generar datos de precios
    FOR i IN 1..200 LOOP
        item_id := 1 + mod(i, 100); -- Relacionado con los items del marketplace
        
        INSERT INTO SLESMARKETPRICING (
            idxslesmarketpricing,
            idxslesmarketplaceitem,
            pricingdate,
            baseprice,
            discounts,
            tieredpricing,
            promotions
        ) VALUES (
            i,
            item_id,
            current_date - (mod(i, 30) || ' days')::interval,
            (random() * 1000)::numeric(10,2),
            jsonb_build_object(
                'volume_discount', jsonb_build_object(
                    'threshold', 1000,
                    'percentage', random() * 30
                ),
                'early_bird', random() < 0.3,
                'loyalty_discount', random() * 15
            ),
            jsonb_build_object(
                'tiers', array[
                    jsonb_build_object('usage', 1000, 'price', random() * 0.1),
                    jsonb_build_object('usage', 5000, 'price', random() * 0.08),
                    jsonb_build_object('usage', 10000, 'price', random() * 0.05)
                ]
            ),
            jsonb_build_object(
                'active_promotions', array[
                    jsonb_build_object(
                        'name', 'Promo ' || i,
                        'discount', random() * 25,
                        'valid_until', current_date + '30 days'::interval
                    )
                ]
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de uso y analytics
CREATE OR REPLACE FUNCTION generate_marketplace_analytics() 
RETURNS void AS $$
DECLARE
    i integer;
    item_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESMARKETANALYTICS CASCADE;
    
    -- Generar datos de analytics
    FOR i IN 1..300 LOOP
        item_id := 1 + mod(i, 100);
        
        INSERT INTO SLESMARKETANALYTICS (
            idxslesmarketanalytics,
            idxslesmarketplaceitem,
            analyticdate,
            usagemetrics,
            performancemetrics,
            usermetrics,
            revenuedata
        ) VALUES (
            i,
            item_id,
            current_timestamp - (i || ' hours')::interval,
            jsonb_build_object(
                'api_calls', floor(random() * 1000),
                'tokens_used', floor(random() * 100000),
                'unique_users', floor(random() * 100)
            ),
            jsonb_build_object(
                'response_time', random() * 100,
                'error_rate', random() * 5,
                'availability', 95 + random() * 5
            ),
            jsonb_build_object(
                'new_users', floor(random() * 10),
                'active_users', floor(random() * 50),
                'user_satisfaction', random() * 5
            ),
            jsonb_build_object(
                'daily_revenue', random() * 1000,
                'mrr', random() * 10000,
                'churn_rate', random() * 5
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de recomendaciones
CREATE OR REPLACE FUNCTION generate_marketplace_recommendations() 
RETURNS void AS $$
DECLARE
    i integer;
    item_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESMARKETRECOMMENDATION CASCADE;
    
    -- Generar datos de recomendaciones
    FOR i IN 1..100 LOOP
        item_id := 1 + mod(i, 100);
        
        INSERT INTO SLESMARKETRECOMMENDATION (
            idxslesmarketrecommendation,
            idxslesmarketplaceitem,
            recommendationdate,
            recommendationtype,
            targetaudience,
            relevancescore,
            context
        ) VALUES (
            i,
            item_id,
            current_timestamp,
            CASE mod(i, 3)
                WHEN 0 THEN 'SIMILAR_ITEMS'
                WHEN 1 THEN 'FREQUENTLY_BOUGHT_TOGETHER'
                ELSE 'BASED_ON_HISTORY'
            END,
            jsonb_build_object(
                'industry', 'Industry ' || mod(i, 5),
                'use_case', 'Use Case ' || mod(i, 4),
                'user_segment', 'Segment ' || mod(i, 3)
            ),
            random(),
            jsonb_build_object(
                'user_history', array['item1', 'item2'],
                'current_session', jsonb_build_object(
                    'search_terms', array['term1', 'term2'],
                    'viewed_items', array[1, 2, 3]
                ),
                'similarity_factors', array['factor1', 'factor2']
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función principal para generar todos los datos de prueba del marketplace
CREATE OR REPLACE FUNCTION generate_all_marketplace_data() 
RETURNS void AS $$
BEGIN
    PERFORM generate_marketplace_items();
    PERFORM generate_marketplace_pricing();
    PERFORM generate_marketplace_analytics();
    PERFORM generate_marketplace_recommendations();
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de comparativas
CREATE OR REPLACE FUNCTION generate_marketplace_comparisons() 
RETURNS void AS $$
DECLARE
    i integer;
    item_id1 integer;
    item_id2 integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESMARKETCOMPARISON CASCADE;
    
    -- Generar datos de comparativas
    FOR i IN 1..50 LOOP
        item_id1 := 1 + mod(i, 50);
        item_id2 := 51 + mod(i, 50);
        
        INSERT INTO SLESMARKETCOMPARISON (
            idxslesmarketcomparison,
            idxslesmarketplaceitem1,
            idxslesmarketplaceitem2,
            comparisondate,
            metrics,
            features,
            performance,
            recommendations
        ) VALUES (
            i,
            item_id1,
            item_id2,
            current_timestamp - (i || ' hours')::interval,
            jsonb_build_object(
                'accuracy_diff', random() * 10 - 5,
                'latency_diff', random() * 100 - 50,
                'cost_efficiency', random() * 2 - 1
            ),
            jsonb_build_object(
                'common_features', array['feature1', 'feature2'],
                'unique_features', jsonb_build_object(
                    'item1', array['unique1', 'unique2'],
                    'item2', array['unique3', 'unique4']
                )
            ),
            jsonb_build_object(
                'benchmark_results', array[
                    jsonb_build_object('test', 'test1', 'score1', random(), 'score2', random()),
                    jsonb_build_object('test', 'test2', 'score1', random(), 'score2', random())
                ]
            ),
            jsonb_build_object(
                'preferred_option', CASE WHEN random() > 0.5 THEN 'item1' ELSE 'item2' END,
                'decision_factors', array['factor1', 'factor2'],
                'use_case_fit', jsonb_build_object(
                    'item1', array['usecase1', 'usecase2'],
                    'item2', array['usecase3', 'usecase4']
                )
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Ejecutar todo
SELECT generate_all_marketplace_data();
SELECT generate_marketplace_comparisons();

-- Generar todos los datos de prueba del marketplace
SELECT generate_all_marketplace_data();

-- O generar datos específicos
SELECT generate_marketplace_items();
SELECT generate_marketplace_pricing();
SELECT generate_marketplace_analytics();
SELECT generate_marketplace_recommendations();
SELECT generate_marketplace_comparisons();

-- Verificar los datos generados
SELECT COUNT(*) FROM SLESMARKETPLACEITEM;
SELECT COUNT(*) FROM SLESMARKETPRICING;
SELECT COUNT(*) FROM SLESMARKETANALYTICS;
SELECT COUNT(*) FROM SLESMARKETRECOMMENDATION;
SELECT COUNT(*) FROM SLESMARKETCOMPARISON;