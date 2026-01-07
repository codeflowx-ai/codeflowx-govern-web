"use client";
import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Brain, CheckCircle, AlertCircle, TrendingUp } from "lucide-react";

interface JudgeEvaluation {
  score: number;
  feedback: string;
  positives?: string[];
  improvements?: string[];
  criteriaScores?: Record<string, number>;
  input: string;
  output: string;
}

interface JudgeEvaluationProps {
  evaluation: JudgeEvaluation;
  showDetails?: boolean;
}

export default function JudgeEvaluationDisplay({ evaluation, showDetails = true }: JudgeEvaluationProps) {
  const { t } = useTranslation();

  const getScoreColor = (score: number) => {
    if (score >= 0.8) return "bg-green-100 text-green-800";
    if (score >= 0.6) return "bg-yellow-100 text-yellow-800";
    return "bg-red-100 text-red-800";
  };

  const getScoreLabel = (score: number) => {
    if (score >= 0.8) return t("governance.testing.judge.excellent", "Excelente");
    if (score >= 0.6) return t("governance.testing.judge.good", "Bueno");
    return t("governance.testing.judge.needsImprovement", "Necesita Mejora");
  };

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="flex items-center gap-2">
            <Brain className="w-5 h-5" />
            {t("governance.testing.judge.evaluation", "Evaluación del Juez")}
          </CardTitle>
          <Badge className={getScoreColor(evaluation.score)}>
            {(evaluation.score * 100).toFixed(1)}% - {getScoreLabel(evaluation.score)}
          </Badge>
        </div>
      </CardHeader>
      <CardBody className="space-y-4">
        {/* Score y Feedback */}
        <div className="space-y-2">
          <div className="flex items-center gap-2">
            <span className="text-sm font-medium">
              {t("governance.testing.judge.score", "Score")}:
            </span>
            <div className="flex-1 bg-muted rounded-full h-2">
              <div
                className={`h-2 rounded-full ${getScoreColor(evaluation.score).split(" ")[0]}`}
                style={{ width: `${evaluation.score * 100}%` }}
              ></div>
            </div>
          </div>
          {evaluation.feedback && (
            <div className="p-3 bg-muted rounded-lg">
              <p className="text-sm">{evaluation.feedback}</p>
            </div>
          )}
        </div>

        {/* Criterios Específicos */}
        {evaluation.criteriaScores && Object.keys(evaluation.criteriaScores).length > 0 && (
          <div className="space-y-2">
            <h4 className="text-sm font-semibold">
              {t("governance.testing.judge.criteriaScores", "Scores por Criterio")}
            </h4>
            <div className="grid grid-cols-2 gap-2">
              {Object.entries(evaluation.criteriaScores).map(([criterion, score]) => (
                <div key={criterion} className="flex items-center justify-between p-2 bg-muted rounded">
                  <span className="text-sm capitalize">{criterion}:</span>
                  <Badge variant="outline" className={getScoreColor(score)}>
                    {(score * 100).toFixed(0)}%
                  </Badge>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Aspectos Positivos */}
        {showDetails && evaluation.positives && evaluation.positives.length > 0 && (
          <div className="space-y-2">
            <h4 className="text-sm font-semibold flex items-center gap-2">
              <CheckCircle className="w-4 h-4 text-green-600" />
              {t("governance.testing.judge.positives", "Aspectos Positivos")}
            </h4>
            <ul className="list-disc list-inside space-y-1 text-sm text-muted-foreground">
              {evaluation.positives.map((positive, index) => (
                <li key={index}>{positive}</li>
              ))}
            </ul>
          </div>
        )}

        {/* Mejoras */}
        {showDetails && evaluation.improvements && evaluation.improvements.length > 0 && (
          <div className="space-y-2">
            <h4 className="text-sm font-semibold flex items-center gap-2">
              <AlertCircle className="w-4 h-4 text-yellow-600" />
              {t("governance.testing.judge.improvements", "Aspectos a Mejorar")}
            </h4>
            <ul className="list-disc list-inside space-y-1 text-sm text-muted-foreground">
              {evaluation.improvements.map((improvement, index) => (
                <li key={index}>{improvement}</li>
              ))}
            </ul>
          </div>
        )}

        {/* Input/Output (si se muestra) */}
        {showDetails && (
          <div className="space-y-2 pt-4 border-t">
            <div>
              <span className="text-xs font-medium text-muted-foreground">
                {t("governance.testing.judge.input", "Input")}:
              </span>
              <p className="text-sm mt-1 p-2 bg-muted rounded">
                {evaluation.input.length > 200
                  ? evaluation.input.substring(0, 200) + "..."
                  : evaluation.input}
              </p>
            </div>
            <div>
              <span className="text-xs font-medium text-muted-foreground">
                {t("governance.testing.judge.output", "Output")}:
              </span>
              <p className="text-sm mt-1 p-2 bg-muted rounded">
                {evaluation.output.length > 200
                  ? evaluation.output.substring(0, 200) + "..."
                  : evaluation.output}
              </p>
            </div>
          </div>
        )}
      </CardBody>
    </Card>
  );
}

