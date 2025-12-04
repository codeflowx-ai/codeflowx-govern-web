package com.codeflowx.govern.business.catalogs;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.enartframework.nocode.datamodel.dao.DAO;
import lombok.extern.slf4j.Slf4j;
import com.codeflowx.govern.entity.catalogs.AnnexIIICategory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * BusinessService para catálogo Anexo III - Categorías de sistemas alto riesgo
 * 
 * Gestiona las 8 categorías principales y subcategorías del Anexo III
 */
@Service
@Slf4j
public class AnnexIIICategoryBusinessService {
    
    @Autowired
    private DAO dao;
    
    /**
     * Obtiene 8 categorías principales (Level 1)
     */
    public List<AnnexIIICategory> getMainCategories() {
        log.info("Retrieving main Annex III categories");
        
        String query = "SELECT * FROM ANNANNEXIIICATEGORIES WHERE ANNISLEVEL1 = TRUE ORDER BY ANNDISPLAYORDER ASC";
        List<AnnexIIICategory> categories = dao.findListBySQL(AnnexIIICategory.class, query);
        
        log.info("Retrieved {} main categories", categories.size());
        return categories;
    }
    
    /**
     * Obtiene subcategorías de una categoría
     */
    public List<AnnexIIICategory> getSubcategories(String categoryCode) {
        log.info("Retrieving subcategories for category: {}", categoryCode);
        
        // Primero obtener la categoría padre
        String parentQuery = "SELECT * FROM ANNANNEXIIICATEGORIES WHERE ANNCATEGORYCODE = ?";
        AnnexIIICategory parentCategory = dao.findBySQL(AnnexIIICategory.class, parentQuery, categoryCode);
        
        if (parentCategory == null) {
            log.warn("Parent category not found: {}", categoryCode);
            return List.of();
        }
        
        // Obtener subcategorías
        String query = "SELECT * FROM ANNANNEXIIICATEGORIES WHERE ANNPARENTCATEGORY = ? ORDER BY ANNDISPLAYORDER ASC";
        List<AnnexIIICategory> subcategories = dao.findListBySQL(AnnexIIICategory.class, 
            query, parentCategory.getIdxannexiiicategory());
        
        log.info("Retrieved {} subcategories for {}", subcategories.size(), categoryCode);
        return subcategories;
    }
    
    /**
     * Obtiene categoría por código
     */
    public AnnexIIICategory getCategoryByCode(String code) {
        String query = "SELECT * FROM ANNANNEXIIICATEGORIES WHERE ANNCATEGORYCODE = ? OR ANNSUBCATEGORYCODE = ?";
        return dao.findBySQL(AnnexIIICategory.class, query, code, code);
    }
    
    /**
     * Sugiere categorías basado en descripción proyecto
     * Usa keywords para clasificación automática por IA
     */
    public List<CategorySuggestion> suggestCategories(String projectDescription) {
        log.info("Suggesting Annex III categories for project description");
        
        if (projectDescription == null || projectDescription.trim().isEmpty()) {
            return List.of();
        }
        
        String descLower = projectDescription.toLowerCase();
        
        // Obtener todas las categorías activas
        String query = "SELECT * FROM ANNANNEXIIICATEGORIES WHERE ANNACTIVE = TRUE";
        List<AnnexIIICategory> allCategories = dao.findListBySQL(AnnexIIICategory.class, query);
        
        // Analizar keywords y calcular relevancia
        return allCategories.stream()
            .map(category -> {
                CategorySuggestion suggestion = new CategorySuggestion();
                suggestion.setCategory(category);
                suggestion.setRelevanceScore(calculateRelevance(category, descLower));
                return suggestion;
            })
            .filter(s -> s.getRelevanceScore() > 0)
            .sorted((a, b) -> Double.compare(b.getRelevanceScore(), a.getRelevanceScore()))
            .limit(3) // Top 3 sugerencias
            .collect(Collectors.toList());
    }
    
    /**
     * Calcula relevancia de una categoría para una descripción
     */
    private double calculateRelevance(AnnexIIICategory category, String descLower) {
        double score = 0.0;
        
        // Verificar keywords en el JSON
        String keywordsJson = category.getAnnkeywords();
        if (keywordsJson != null) {
            // Parse keywords JSON y buscar coincidencias
            // Simplificado: buscar en nombre y descripción
            if (descLower.contains(category.getAnncategoryname().toLowerCase())) {
                score += 10.0;
            }
            if (category.getAnncategorydescription() != null && 
                descLower.contains(category.getAnncategorydescription().toLowerCase().substring(0, 
                    Math.min(50, category.getAnncategorydescription().length())))) {
                score += 5.0;
            }
        }
        
        // Categorías específicas por keywords comunes
        if (category.getAnncategorycode().equals("III.1")) {
            if (descLower.contains("biometr") || descLower.contains("facial") || 
                descLower.contains("reconocimiento") || descLower.contains("identificación")) {
                score += 20.0;
            }
        } else if (category.getAnncategorycode().equals("III.4")) {
            if (descLower.contains("reclutamiento") || descLower.contains("cv") || 
                descLower.contains("selección") || descLower.contains("empleo")) {
                score += 20.0;
            }
        }
        // Agregar más reglas según necesidad
        
        return score;
    }
    
    /**
     * Obtiene todas las categorías activas
     */
    public List<AnnexIIICategory> getAllActiveCategories() {
        String query = "SELECT * FROM ANNANNEXIIICATEGORIES WHERE ANNACTIVE = TRUE ORDER BY ANNDISPLAYORDER ASC";
        return dao.findListBySQL(AnnexIIICategory.class, query);
    }
    
    /**
     * Clase para sugerencias de categorías
     */
    @Data
    public static class CategorySuggestion {
        private AnnexIIICategory category;
        private double relevanceScore;
        private String reason;
    }
}


