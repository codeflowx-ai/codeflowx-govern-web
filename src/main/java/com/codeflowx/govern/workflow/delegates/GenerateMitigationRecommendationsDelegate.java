package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Generate Mitigation Recommendations
 * Genera recomendaciones de mitigación para sesgos/problemas detectados
 * Proceso: 08_BIAS_DETECTION
 */
@Slf4j
@Component("generateMitigationRecommendationsDelegate")
public class GenerateMitigationRecommendationsDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("💡 Generating Mitigation Recommendations");
        
        Boolean biasDetected = (Boolean) execution.getVariable("biasDetected");
        Double disparateImpact = (Double) execution.getVariable("disparateImpact");
        String biasType = (String) execution.getVariable("biasType");
        
        StringBuilder recommendations = new StringBuilder();
        
        if (Boolean.TRUE.equals(biasDetected)) {
            recommendations.append("BIAS MITIGATION RECOMMENDATIONS:\n");
            recommendations.append("1. Review training data for balanced representation\n");
            recommendations.append("2. Apply fairness constraints during model training\n");
            recommendations.append("3. Use bias mitigation techniques (reweighing, preprocessing)\n");
            
            if (disparateImpact != null && disparateImpact > 0.30) {
                recommendations.append("4. CRITICAL: Disparate impact exceeds 30% - Consider model redesign\n");
            }
            
            recommendations.append("5. Implement continuous fairness monitoring\n");
        } else {
            recommendations.append("No significant bias detected. Continue monitoring.");
        }
        
        String recommendationsText = recommendations.toString();
        
        log.info("📋 Recommendations Generated:");
        log.info("{}", recommendationsText);
        
        execution.setVariable("mitigationRecommendations", recommendationsText);
        execution.setVariable("recommendationsGenerated", true);
    }
}
