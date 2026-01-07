"use client";

import { useTranslation } from "@/app/config/i18n";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  BarChart3,
  Upload,
  FileText,
  Loader2,
  CheckCircle2,
  XCircle,
  ArrowLeft,
  Brain,
  Cpu,
  FileSpreadsheet,
  Play,
  Download,
} from "lucide-react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

interface Prompt {
  idxprompt: number;
  prmname: string;
  prmversion: string;
  prmstatus: string;
  prmcontent: string;
  prmparameters: string;
}

interface PromptVersion {
  idxpromptversion: number;
  prmversion: string;
  prmcontent: string;
  prmparameters: string;
}

export default function PromptTestingPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [activeTab, setActiveTab] = useState("single");

  // Estados para selección
  const [prompts, setPrompts] = useState<Prompt[]>([]);
  const [selectedPromptId, setSelectedPromptId] = useState<string>("");
  const [selectedPrompt, setSelectedPrompt] = useState<Prompt | null>(null);
  const [versions, setVersions] = useState<PromptVersion[]>([]);
  const [selectedVersion, setSelectedVersion] = useState<string>("");
  const [loadingPrompts, setLoadingPrompts] = useState(false);

  // Estados para modelos
  const [models, setModels] = useState<Array<{ id: number; name: string }>>([]);
  const [selectedModelId, setSelectedModelId] = useState<string>("");
  const [judgeModelId, setJudgeModelId] = useState<string>("");

  // Estados para prueba individual
  const [testInput, setTestInput] = useState<string>("");
  const [expectedResponse, setExpectedResponse] = useState<string>("");
  const [testing, setTesting] = useState(false);
  const [testResult, setTestResult] = useState<any>(null);

  // Estados para batch testing
  const [csvFile, setCsvFile] = useState<File | null>(null);
  const [parsedTestCases, setParsedTestCases] = useState<any[]>([]);
  const [batchTesting, setBatchTesting] = useState(false);
  const [batchResults, setBatchResults] = useState<any[]>([]);

  // Estados para A/B testing
  const [promptAId, setPromptAId] = useState<string>("");
  const [promptAVersion, setPromptAVersion] = useState<string>("");
  const [promptBId, setPromptBId] = useState<string>("");
  const [promptBVersion, setPromptBVersion] = useState<string>("");
  const [promptA, setPromptA] = useState<Prompt | null>(null);
  const [promptB, setPromptB] = useState<Prompt | null>(null);
  const [versionsA, setVersionsA] = useState<PromptVersion[]>([]);
  const [versionsB, setVersionsB] = useState<PromptVersion[]>([]);
  const [abCsvFile, setAbCsvFile] = useState<File | null>(null);
  const [abParsedTestCases, setAbParsedTestCases] = useState<any[]>([]);
  const [abTesting, setAbTesting] = useState(false);
  const [abResults, setAbResults] = useState<any>(null);

  useEffect(() => {
    loadPrompts();
    loadModels();
  }, []);

  useEffect(() => {
    if (selectedPromptId) {
      loadPromptDetails();
      loadVersions();
    }
  }, [selectedPromptId]);

  useEffect(() => {
    if (selectedVersion && versions.length > 0) {
      const version = versions.find(v => v.prmversion === selectedVersion);
      if (version && selectedPrompt) {
        setSelectedPrompt({
          ...selectedPrompt,
          prmcontent: version.prmcontent,
          prmparameters: version.prmparameters,
        });
      }
    }
  }, [selectedVersion, versions]);

  // Efectos para A/B testing
  useEffect(() => {
    if (promptAId) {
      loadPromptForAB(promptAId, 'A');
      loadVersionsForAB(promptAId, 'A');
    }
  }, [promptAId]);

  useEffect(() => {
    if (promptBId) {
      loadPromptForAB(promptBId, 'B');
      loadVersionsForAB(promptBId, 'B');
    }
  }, [promptBId]);

  useEffect(() => {
    if (promptAVersion && versionsA.length > 0) {
      const version = versionsA.find(v => v.prmversion === promptAVersion);
      if (version && promptA) {
        setPromptA({
          ...promptA,
          prmcontent: version.prmcontent,
          prmparameters: version.prmparameters,
        });
      }
    }
  }, [promptAVersion, versionsA]);

  useEffect(() => {
    if (promptBVersion && versionsB.length > 0) {
      const version = versionsB.find(v => v.prmversion === promptBVersion);
      if (version && promptB) {
        setPromptB({
          ...promptB,
          prmcontent: version.prmcontent,
          prmparameters: version.prmparameters,
        });
      }
    }
  }, [promptBVersion, versionsB]);

  const loadPrompts = async () => {
    try {
      setLoadingPrompts(true);
      const response = await fetch("/api/prompts");
      if (response.ok) {
        const data = await response.json();
        setPrompts(Array.isArray(data) ? data : data.content || []);
      }
    } catch (error) {
      console.error("Error loading prompts:", error);
    } finally {
      setLoadingPrompts(false);
    }
  };

  const loadPromptDetails = async () => {
    try {
      const response = await fetch(`/api/prompts/${selectedPromptId}`);
      if (response.ok) {
        const data = await response.json();
        setSelectedPrompt(data);
        setSelectedVersion(data.prmversion || "");
      }
    } catch (error) {
      console.error("Error loading prompt details:", error);
    }
  };

  const loadVersions = async () => {
    try {
      const response = await fetch(`/api/prompts/${selectedPromptId}/versions`);
      if (response.ok) {
        const data = await response.json();
        setVersions(data);
      }
    } catch (error) {
      console.error("Error loading versions:", error);
    }
  };

  const loadModels = async () => {
    try {
      const response = await fetch("/api/governance/models");
      if (response.ok) {
        const data = await response.json();
        setModels(Array.isArray(data) ? data : data.content || []);
      }
    } catch (error) {
      console.error("Error loading models:", error);
    }
  };

  const loadPromptForAB = async (id: string, variant: 'A' | 'B') => {
    try {
      const response = await fetch(`/api/prompts/${id}`);
      if (response.ok) {
        const data = await response.json();
        if (variant === 'A') {
          setPromptA(data);
          setPromptAVersion(data.prmversion || "");
        } else {
          setPromptB(data);
          setPromptBVersion(data.prmversion || "");
        }
      }
    } catch (error) {
      console.error(`Error loading prompt ${variant}:`, error);
    }
  };

  const loadVersionsForAB = async (id: string, variant: 'A' | 'B') => {
    try {
      const response = await fetch(`/api/prompts/${id}/versions`);
      if (response.ok) {
        const data = await response.json();
        if (variant === 'A') {
          setVersionsA(data);
        } else {
          setVersionsB(data);
        }
      }
    } catch (error) {
      console.error(`Error loading versions ${variant}:`, error);
    }
  };

  const parseCSV = (csvText: string): any[] => {
    const lines = csvText.split('\n').filter(line => line.trim());
    if (lines.length === 0) return [];

    const headers = lines[0].split(',').map(h => h.trim().toLowerCase());
    const testCases: any[] = [];

    for (let i = 1; i < lines.length; i++) {
      const values = lines[i].split(',').map(v => v.trim());
      const testCase: any = {};

      headers.forEach((header, index) => {
        if (header.includes('input') || header.includes('entrada')) {
          testCase.input = values[index] || '';
        } else if (header.includes('expected') || header.includes('esperado') || header.includes('output')) {
          testCase.expectedResponse = values[index] || '';
        } else if (header.includes('id') || header.includes('test')) {
          testCase.id = values[index] || `test-${i}`;
        } else {
          testCase[header] = values[index] || '';
        }
      });

      if (testCase.input || testCase.expectedResponse) {
        testCases.push(testCase);
      }
    }

    return testCases;
  };

  const handleCSVUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    if (!file.name.endsWith('.csv')) {
      alert(t("prompts.testing.csv.invalid", "Por favor, seleccione un archivo CSV válido"));
      return;
    }

    setCsvFile(file);
    const reader = new FileReader();

    reader.onload = (e) => {
      const text = e.target?.result as string;
      const parsed = parseCSV(text);
      setParsedTestCases(parsed);

      if (parsed.length === 0) {
        alert(t("prompts.testing.csv.empty", "El archivo CSV no contiene test cases válidos"));
      } else {
        alert(t("prompts.testing.csv.parsed", "{count} test cases cargados", { count: parsed.length.toString() }));
      }
    };

    reader.readAsText(file);
  };

  const handleABCSVUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    if (!file.name.endsWith('.csv')) {
      alert(t("prompts.testing.abtest.csv.invalid", "Por favor, seleccione un archivo CSV válido"));
      return;
    }

    setAbCsvFile(file);
    const reader = new FileReader();

    reader.onload = (e) => {
      const text = e.target?.result as string;
      const parsed = parseCSV(text);
      setAbParsedTestCases(parsed);

      if (parsed.length === 0) {
        alert(t("prompts.testing.abtest.csv.empty", "El archivo CSV no contiene test cases válidos"));
      } else {
        alert(t("prompts.testing.abtest.csv.parsed", "{count} test cases cargados", { count: parsed.length.toString() }));
      }
    };

    reader.readAsText(file);
  };

  const handleSingleTest = async () => {
    if (!selectedPromptId || !selectedModelId) {
      alert(t("prompts.testing.selectPromptAndModel", "Por favor, seleccione un prompt y un modelo"));
      return;
    }

    try {
      setTesting(true);
      setTestResult(null);

      const response = await fetch(`/api/governance/prompts/${selectedPromptId}/test`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          modelId: selectedModelId,
          input: testInput || undefined,
          expectedResponse: expectedResponse || undefined,
        }),
      });

      if (response.ok) {
        const result = await response.json();
        setTestResult(result);

        // Si hay modelo juez y respuesta esperada, evaluar
        if (judgeModelId && expectedResponse) {
          try {
            const judgeResponse = await fetch(`/api/governance/prompts/${selectedPromptId}/test/judge`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                judgeModelId: judgeModelId,
                prompt: selectedPrompt?.prmcontent || '',
                input: testInput || '',
                actualResponse: result.response || result.text,
                expectedResponse: expectedResponse,
              }),
            });

            if (judgeResponse.ok) {
              const judgeResult = await judgeResponse.json();
              setTestResult({
                ...result,
                judgeScore: judgeResult.score,
                judgeFeedback: judgeResult.feedback,
              });
            }
          } catch (error) {
            console.error("Error en evaluación del juez:", error);
          }
        }
      } else {
        const error = await response.json();
        alert(error.error || t("prompts.testing.error", "Error al ejecutar el test"));
      }
    } catch (error) {
      console.error("Error running test:", error);
      alert(t("prompts.testing.error", "Error al ejecutar el test"));
    } finally {
      setTesting(false);
    }
  };

  const handleBatchTest = async () => {
    if (!selectedPromptId || !selectedModelId) {
      alert(t("prompts.testing.selectPromptAndModel", "Por favor, seleccione un prompt y un modelo"));
      return;
    }

    if (parsedTestCases.length === 0) {
      alert(t("prompts.testing.csv.noCases", "Por favor, cargue un archivo CSV con test cases"));
      return;
    }

    try {
      setBatchTesting(true);
      setBatchResults([]);

      const results: any[] = [];

      for (let i = 0; i < parsedTestCases.length; i++) {
        const testCase = parsedTestCases[i];

        try {
          const testResponse = await fetch(`/api/governance/prompts/${selectedPromptId}/test`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              modelId: selectedModelId,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
            }),
          });

          if (testResponse.ok) {
            const testResult = await testResponse.json();
            const actualResponse = testResult.response || testResult.text || testResult.content || '';

            let judgeScore = null;
            let judgeFeedback = null;

            if (judgeModelId && testCase.expectedResponse) {
              try {
                const judgeResponse = await fetch(`/api/governance/prompts/${selectedPromptId}/test/judge`, {
                  method: "POST",
                  headers: {
                    "Content-Type": "application/json",
                  },
                  body: JSON.stringify({
                    judgeModelId: judgeModelId,
                    prompt: selectedPrompt?.prmcontent || '',
                    input: testCase.input || '',
                    actualResponse: actualResponse,
                    expectedResponse: testCase.expectedResponse,
                  }),
                });

                if (judgeResponse.ok) {
                  const judgeResult = await judgeResponse.json();
                  judgeScore = judgeResult.score;
                  judgeFeedback = judgeResult.feedback;
                }
              } catch (error) {
                console.error("Error en evaluación del juez:", error);
              }
            }

            let similarityScore = null;
            if (testCase.expectedResponse) {
              try {
                const compareResponse = await fetch(`/api/governance/prompts/${selectedPromptId}/test/compare`, {
                  method: "POST",
                  headers: {
                    "Content-Type": "application/json",
                  },
                  body: JSON.stringify({
                    actualResponse: actualResponse,
                    expectedResponse: testCase.expectedResponse,
                  }),
                });

                if (compareResponse.ok) {
                  const compareResult = await compareResponse.json();
                  similarityScore = compareResult.similarityScore;
                }
              } catch (error) {
                console.error("Error comparando respuestas:", error);
              }
            }

            results.push({
              id: testCase.id || `test-${i + 1}`,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
              actualResponse: actualResponse,
              similarityScore: similarityScore,
              judgeScore: judgeScore,
              judgeFeedback: judgeFeedback,
              tokensUsed: testResult.tokensUsed,
              latency: testResult.latency,
              cost: testResult.cost,
              success: similarityScore !== null ? similarityScore >= 0.8 : judgeScore !== null ? judgeScore >= 0.8 : null,
              error: null,
            });
          } else {
            const error = await testResponse.json();
            results.push({
              id: testCase.id || `test-${i + 1}`,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
              actualResponse: '',
              error: error.error || t("prompts.testing.batch.error", "Error al ejecutar test"),
              success: false,
            });
          }
        } catch (error: any) {
          results.push({
            id: testCase.id || `test-${i + 1}`,
            input: testCase.input || '',
            expectedResponse: testCase.expectedResponse || '',
            actualResponse: '',
            error: error.message || t("prompts.testing.batch.error", "Error al ejecutar test"),
            success: false,
          });
        }
      }

      setBatchResults(results);
    } catch (error) {
      console.error("Error en batch testing:", error);
      alert(t("prompts.testing.batch.error", "Error al ejecutar batch de tests"));
    } finally {
      setBatchTesting(false);
    }
  };

  const exportResults = () => {
    if (batchResults.length === 0) return;

    const csv = [
      ['ID', 'Input', 'Expected', 'Actual', 'Similarity', 'Judge Score', 'Success', 'Error'].join(','),
      ...batchResults.map(r => [
        r.id,
        `"${(r.input || '').replace(/"/g, '""')}"`,
        `"${(r.expectedResponse || '').replace(/"/g, '""')}"`,
        `"${(r.actualResponse || '').replace(/"/g, '""')}"`,
        r.similarityScore !== null ? r.similarityScore.toFixed(3) : '',
        r.judgeScore !== null ? r.judgeScore.toFixed(3) : '',
        r.success !== null ? (r.success ? 'Yes' : 'No') : '',
        r.error ? `"${r.error.replace(/"/g, '""')}"` : '',
      ].join(','))
    ].join('\n');

    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `test-results-${Date.now()}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const handleABTest = async () => {
    if (!promptAId || !promptBId || !selectedModelId) {
      alert(t("prompts.testing.abtest.selectPrompts", "Por favor, seleccione ambos prompts y un modelo"));
      return;
    }

    if (abParsedTestCases.length === 0) {
      alert(t("prompts.testing.abtest.noCases", "Por favor, cargue un archivo CSV con test cases"));
      return;
    }

    try {
      setAbTesting(true);
      setAbResults(null);

      // Ejecutar ambos prompts contra los mismos test cases
      const resultsA: any[] = [];
      const resultsB: any[] = [];

      for (let i = 0; i < abParsedTestCases.length; i++) {
        const testCase = abParsedTestCases[i];

        // Ejecutar Prompt A
        try {
          const responseA = await fetch(`/api/governance/prompts/${promptAId}/test`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              modelId: selectedModelId,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
            }),
          });

          if (responseA.ok) {
            const resultA = await responseA.json();
            resultsA.push({
              id: testCase.id || `test-${i + 1}`,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
              actualResponse: resultA.response || resultA.text || resultA.content || '',
              tokensUsed: resultA.tokensUsed,
              latency: resultA.latency,
              cost: resultA.cost,
              error: null,
            });
          } else {
            resultsA.push({
              id: testCase.id || `test-${i + 1}`,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
              actualResponse: '',
              error: t("prompts.testing.abtest.error", "Error al ejecutar"),
            });
          }
        } catch (error: any) {
          resultsA.push({
            id: testCase.id || `test-${i + 1}`,
            input: testCase.input || '',
            expectedResponse: testCase.expectedResponse || '',
            actualResponse: '',
            error: error.message || t("prompts.testing.abtest.error", "Error al ejecutar"),
          });
        }

        // Ejecutar Prompt B
        try {
          const responseB = await fetch(`/api/governance/prompts/${promptBId}/test`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              modelId: selectedModelId,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
            }),
          });

          if (responseB.ok) {
            const resultB = await responseB.json();
            resultsB.push({
              id: testCase.id || `test-${i + 1}`,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
              actualResponse: resultB.response || resultB.text || resultB.content || '',
              tokensUsed: resultB.tokensUsed,
              latency: resultB.latency,
              cost: resultB.cost,
              error: null,
            });
          } else {
            resultsB.push({
              id: testCase.id || `test-${i + 1}`,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
              actualResponse: '',
              error: t("prompts.testing.abtest.error", "Error al ejecutar"),
            });
          }
        } catch (error: any) {
          resultsB.push({
            id: testCase.id || `test-${i + 1}`,
            input: testCase.input || '',
            expectedResponse: testCase.expectedResponse || '',
            actualResponse: '',
            error: error.message || t("prompts.testing.abtest.error", "Error al ejecutar"),
          });
        }
      }

      // Comparar resultados y calcular métricas
      let totalTests = abParsedTestCases.length;
      let successA = 0;
      let successB = 0;
      let totalTokensA = 0;
      let totalTokensB = 0;
      let totalLatencyA = 0;
      let totalLatencyB = 0;
      let totalCostA = 0;
      let totalCostB = 0;
      let avgSimilarityA = 0;
      let avgSimilarityB = 0;
      let similarityCountA = 0;
      let similarityCountB = 0;

      // Calcular similitud si hay respuestas esperadas
      for (let i = 0; i < resultsA.length; i++) {
        const resultA = resultsA[i];
        const resultB = resultsB[i];

        if (resultA.expectedResponse && resultA.actualResponse && !resultA.error) {
          try {
            const compareA = await fetch(`/api/governance/prompts/${promptAId}/test/compare`, {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({
                actualResponse: resultA.actualResponse,
                expectedResponse: resultA.expectedResponse,
              }),
            });
            if (compareA.ok) {
              const compareResultA = await compareA.json();
              resultA.similarityScore = compareResultA.similarityScore;
              avgSimilarityA += compareResultA.similarityScore;
              similarityCountA++;
              if (compareResultA.similarityScore >= 0.8) successA++;
            }
          } catch (error) {
            console.error("Error comparing A:", error);
          }
        }

        if (resultB.expectedResponse && resultB.actualResponse && !resultB.error) {
          try {
            const compareB = await fetch(`/api/governance/prompts/${promptBId}/test/compare`, {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({
                actualResponse: resultB.actualResponse,
                expectedResponse: resultB.expectedResponse,
              }),
            });
            if (compareB.ok) {
              const compareResultB = await compareB.json();
              resultB.similarityScore = compareResultB.similarityScore;
              avgSimilarityB += compareResultB.similarityScore;
              similarityCountB++;
              if (compareResultB.similarityScore >= 0.8) successB++;
            }
          } catch (error) {
            console.error("Error comparing B:", error);
          }
        }

        // Acumular métricas
        if (resultA.tokensUsed && typeof resultA.tokensUsed === 'object') {
          totalTokensA += (resultA.tokensUsed.input || 0) + (resultA.tokensUsed.output || 0);
        }
        if (resultB.tokensUsed && typeof resultB.tokensUsed === 'object') {
          totalTokensB += (resultB.tokensUsed.input || 0) + (resultB.tokensUsed.output || 0);
        }
        if (resultA.latency !== undefined && typeof resultA.latency === 'number') {
          totalLatencyA += resultA.latency;
        }
        if (resultB.latency !== undefined && typeof resultB.latency === 'number') {
          totalLatencyB += resultB.latency;
        }
        if (resultA.cost !== undefined && typeof resultA.cost === 'number') {
          totalCostA += resultA.cost;
        }
        if (resultB.cost !== undefined && typeof resultB.cost === 'number') {
          totalCostB += resultB.cost;
        }
      }

      avgSimilarityA = similarityCountA > 0 ? avgSimilarityA / similarityCountA : 0;
      avgSimilarityB = similarityCountB > 0 ? avgSimilarityB / similarityCountB : 0;

      // Determinar ganador
      let winner = 'TIE';
      let winnerReason = '';
      if (avgSimilarityA > avgSimilarityB + 0.05) {
        winner = 'A';
        winnerReason = t("prompts.testing.abtest.winner.reason.similarity", "Mayor similitud promedio");
      } else if (avgSimilarityB > avgSimilarityA + 0.05) {
        winner = 'B';
        winnerReason = t("prompts.testing.abtest.winner.reason.similarity", "Mayor similitud promedio");
      } else if (totalCostA < totalCostB * 0.9) {
        winner = 'A';
        winnerReason = t("prompts.testing.abtest.winner.reason.cost", "Menor costo");
      } else if (totalCostB < totalCostA * 0.9) {
        winner = 'B';
        winnerReason = t("prompts.testing.abtest.winner.reason.cost", "Menor costo");
      } else if (totalLatencyA < totalLatencyB * 0.9) {
        winner = 'A';
        winnerReason = t("prompts.testing.abtest.winner.reason.latency", "Menor latencia");
      } else if (totalLatencyB < totalLatencyA * 0.9) {
        winner = 'B';
        winnerReason = t("prompts.testing.abtest.winner.reason.latency", "Menor latencia");
      }

      setAbResults({
        resultsA,
        resultsB,
        metrics: {
          totalTests,
          successA,
          successB,
          avgSimilarityA,
          avgSimilarityB,
          totalTokensA,
          totalTokensB,
          avgLatencyA: totalLatencyA / totalTests,
          avgLatencyB: totalLatencyB / totalTests,
          totalCostA,
          totalCostB,
        },
        winner,
        winnerReason,
      });
    } catch (error) {
      console.error("Error en A/B testing:", error);
      alert(t("prompts.testing.abtest.error", "Error al ejecutar A/B testing"));
    } finally {
      setAbTesting(false);
    }
  };

  const exportABResults = () => {
    if (!abResults) return;

    const csv = [
      ['ID', 'Input', 'Expected', 'Response A', 'Response B', 'Similarity A', 'Similarity B', 'Tokens A', 'Tokens B', 'Latency A', 'Latency B', 'Cost A', 'Cost B', 'Winner'].join(','),
      ...abResults.resultsA.map((rA: any, index: number) => {
        const rB = abResults.resultsB[index];
        const winner = rA.similarityScore !== null && rB.similarityScore !== null
          ? (rA.similarityScore > rB.similarityScore ? 'A' : rB.similarityScore > rA.similarityScore ? 'B' : 'TIE')
          : 'N/A';
        return [
          rA.id,
          `"${(rA.input || '').replace(/"/g, '""')}"`,
          `"${(rA.expectedResponse || '').replace(/"/g, '""')}"`,
          `"${(rA.actualResponse || '').replace(/"/g, '""')}"`,
          `"${(rB?.actualResponse || '').replace(/"/g, '""')}"`,
          rA.similarityScore !== null ? rA.similarityScore.toFixed(3) : '',
          rB?.similarityScore !== null ? rB.similarityScore.toFixed(3) : '',
          typeof rA.tokensUsed === 'object' ? ((rA.tokensUsed.input || 0) + (rA.tokensUsed.output || 0)) : '',
          typeof rB?.tokensUsed === 'object' ? ((rB.tokensUsed.input || 0) + (rB.tokensUsed.output || 0)) : '',
          rA.latency !== undefined ? rA.latency.toFixed(2) : '',
          rB?.latency !== undefined ? rB.latency.toFixed(2) : '',
          rA.cost !== undefined ? rA.cost.toFixed(4) : '',
          rB?.cost !== undefined ? rB.cost.toFixed(4) : '',
          winner,
        ].join(',');
      })
    ].join('\n');

    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `ab-test-results-${Date.now()}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const successCount = batchResults.filter(r => r.success === true).length;
  const failCount = batchResults.filter(r => r.success === false).length;
  const avgSimilarity = batchResults.length > 0
    ? batchResults
        .filter(r => r.similarityScore !== null)
        .reduce((sum, r) => sum + (r.similarityScore || 0), 0) / batchResults.filter(r => r.similarityScore !== null).length
    : 0;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-6 py-6 space-y-6 pb-20">
        {/* Header */}
        <div className="space-y-4">
          {/* Primera línea: Título y Subtítulo */}
          <div className="flex items-center gap-3">
            <BarChart3 className="w-6 h-6 text-primary flex-shrink-0" />
            <div className="flex-1">
              <h1 className="text-2xl font-bold text-foreground">
                {t("prompts.testing.title", "Testing de Prompts")}
              </h1>
              <p className="text-sm text-muted-foreground mt-1">
                {t("prompts.testing.subtitle", "Ejecute pruebas individuales o en batch contra diferentes modelos")}
              </p>
            </div>
          </div>

          {/* Segunda línea: Botón Volver (izquierda) */}
          <div className="flex items-center justify-between">
            <Button
              variant="ghost"
              onClick={() => router.push("/governance/prompts")}
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              {t("common.back", "Volver")}
            </Button>
            <div></div>
          </div>
        </div>

        {/* Selectores */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("prompts.testing.selection.title", "Selección de Prompt y Modelo")}</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {/* Selector de Prompt */}
              <div className="space-y-2">
                <Label>{t("prompts.testing.selection.prompt", "Prompt")}</Label>
                <Select
                  value={selectedPromptId}
                  onValueChange={setSelectedPromptId}
                  disabled={loadingPrompts}
                >
                  <SelectTrigger>
                    <SelectValue placeholder={t("prompts.testing.selection.promptPlaceholder", "Seleccione un prompt")} />
                  </SelectTrigger>
                  <SelectContent>
                    {prompts.map((prompt) => (
                      <SelectItem key={prompt.idxprompt} value={prompt.idxprompt.toString()}>
                        {prompt.prmname} (v{prompt.prmversion})
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              {/* Selector de Versión */}
              {selectedPromptId && (
                <div className="space-y-2">
                  <Label>{t("prompts.testing.selection.version", "Versión")}</Label>
                  <Select
                    value={selectedVersion}
                    onValueChange={setSelectedVersion}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder={t("prompts.testing.selection.versionPlaceholder", "Seleccione una versión")} />
                    </SelectTrigger>
                    <SelectContent>
                      {versions.map((version) => (
                        <SelectItem key={version.idxpromptversion} value={version.prmversion}>
                          {version.prmversion}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              )}

              {/* Selector de Modelo */}
              <div className="space-y-2">
                <Label>{t("prompts.testing.selection.model", "Modelo a Probar")}</Label>
                <Select
                  value={selectedModelId}
                  onValueChange={setSelectedModelId}
                >
                  <SelectTrigger>
                    <SelectValue placeholder={t("prompts.testing.selection.modelPlaceholder", "Seleccione un modelo")} />
                  </SelectTrigger>
                  <SelectContent>
                    {models.map((model) => (
                      <SelectItem key={model.id} value={model.id.toString()}>
                        {model.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            </div>

            {/* Selector de Modelo Juez (Opcional) */}
            <div className="space-y-2">
              <Label>
                {t("prompts.testing.selection.judge", "Modelo Juez")} <span className="text-muted-foreground">({t("common.optional", "Opcional")})</span>
              </Label>
              <Select
                value={judgeModelId}
                onValueChange={setJudgeModelId}
              >
                <SelectTrigger>
                  <SelectValue placeholder={t("prompts.testing.selection.judgePlaceholder", "Seleccione un modelo juez para evaluar respuestas")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="">{t("common.none", "Ninguno")}</SelectItem>
                  {models.map((model) => (
                    <SelectItem key={model.id} value={model.id.toString()}>
                      {model.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <p className="text-xs text-muted-foreground">
                {t("prompts.testing.selection.judgeInfo", "El modelo juez evaluará la calidad de las respuestas generadas")}
              </p>
            </div>
          </CardContent>
        </Card>

        {/* Tabs para diferentes tipos de pruebas */}
        <Tabs value={activeTab} onValueChange={setActiveTab} defaultValue="single">
        <TabsList>
          <TabsTrigger value="single">
            {t("prompts.testing.tabs.single", "Prueba Individual")}
          </TabsTrigger>
          <TabsTrigger value="batch">
            {t("prompts.testing.tabs.batch", "Pruebas en Batch")}
          </TabsTrigger>
          <TabsTrigger value="abtest">
            {t("prompts.testing.tabs.abtest", "A/B Testing")}
          </TabsTrigger>
        </TabsList>

          {/* Prueba Individual */}
          <TabsContent value="single" className="space-y-4">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle>{t("prompts.testing.single.title", "Prueba Individual")}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <Label>{t("prompts.testing.single.input", "Input")} <span className="text-muted-foreground">({t("common.optional", "Opcional")})</span></Label>
                  <Textarea
                    placeholder={t("prompts.testing.single.inputPlaceholder", "Ingrese el input si el prompt requiere variables")}
                    value={testInput}
                    onChange={(e) => setTestInput(e.target.value)}
                    disabled={testing}
                    rows={3}
                  />
                </div>

                <div className="space-y-2">
                  <Label>{t("prompts.testing.single.expected", "Respuesta Esperada")} <span className="text-muted-foreground">({t("common.optional", "Opcional")})</span></Label>
                  <Textarea
                    placeholder={t("prompts.testing.single.expectedPlaceholder", "Ingrese la respuesta esperada para comparar")}
                    value={expectedResponse}
                    onChange={(e) => setExpectedResponse(e.target.value)}
                    disabled={testing}
                    rows={3}
                  />
                </div>

                <Button
                  onClick={handleSingleTest}
                  disabled={testing || !selectedPromptId || !selectedModelId}
                  className="w-full"
                >
                  {testing ? (
                    <>
                      <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                      {t("prompts.testing.single.executing", "Ejecutando...")}
                    </>
                  ) : (
                    <>
                      <Play className="w-4 h-4 mr-2" />
                      {t("prompts.testing.single.run", "Ejecutar Prueba")}
                    </>
                  )}
                </Button>

                {testResult && (
                  <div className="space-y-4 mt-6 p-4 rounded-lg bg-muted/50 border">
                    <h3 className="font-semibold">{t("prompts.testing.single.result", "Resultado")}</h3>
                    <div className="space-y-2">
                      <Label>{t("prompts.testing.single.response", "Respuesta del Modelo")}</Label>
                      <div className="p-4 rounded-lg bg-background border min-h-[100px] whitespace-pre-wrap">
                        {testResult.response || testResult.text || testResult.content || t("prompts.testing.single.noResponse", "No se recibió respuesta")}
                      </div>
                    </div>

                    {(testResult.tokensUsed || testResult.latency !== undefined || testResult.cost !== undefined) && (
                      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {testResult.tokensUsed && (
                          <div className="p-3 rounded-lg bg-background/50 border">
                            <p className="text-sm text-muted-foreground mb-1">{t("prompts.testing.single.tokens", "Tokens")}</p>
                            <p className="font-semibold">
                              {typeof testResult.tokensUsed === 'object'
                                ? `${testResult.tokensUsed.input || 0} / ${testResult.tokensUsed.output || 0}`
                                : testResult.tokensUsed}
                            </p>
                          </div>
                        )}
                        {testResult.latency !== undefined && (
                          <div className="p-3 rounded-lg bg-background/50 border">
                            <p className="text-sm text-muted-foreground mb-1">{t("prompts.testing.single.latency", "Latencia")}</p>
                            <p className="font-semibold">
                              {typeof testResult.latency === 'number' ? `${testResult.latency.toFixed(2)}s` : testResult.latency}
                            </p>
                          </div>
                        )}
                        {testResult.cost !== undefined && (
                          <div className="p-3 rounded-lg bg-background/50 border">
                            <p className="text-sm text-muted-foreground mb-1">{t("prompts.testing.single.cost", "Costo")}</p>
                            <p className="font-semibold">
                              ${typeof testResult.cost === 'number' ? testResult.cost.toFixed(4) : testResult.cost}
                            </p>
                          </div>
                        )}
                      </div>
                    )}

                    {testResult.judgeScore !== undefined && (
                      <div className="p-3 rounded-lg bg-background/50 border">
                        <p className="text-sm text-muted-foreground mb-1">{t("prompts.testing.single.judgeScore", "Score del Juez")}</p>
                        <p className="font-semibold">{(testResult.judgeScore * 100).toFixed(1)}%</p>
                        {testResult.judgeFeedback && (
                          <p className="text-sm mt-2">{testResult.judgeFeedback}</p>
                        )}
                      </div>
                    )}
                  </div>
                )}
              </CardContent>
            </Card>
          </TabsContent>

          {/* Pruebas en Batch */}
          <TabsContent value="batch" className="space-y-4">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle>{t("prompts.testing.batch.title", "Pruebas en Batch")}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <Label>{t("prompts.testing.batch.csv", "Subir CSV con Test Cases")}</Label>
                  <div className="flex items-center gap-2">
                    <Input
                      type="file"
                      accept=".csv"
                      onChange={handleCSVUpload}
                      className="flex-1"
                    />
                    {csvFile && (
                      <Badge variant="secondary">
                        <FileSpreadsheet className="w-3 h-3 mr-1" />
                        {csvFile.name}
                      </Badge>
                    )}
                  </div>
                  <p className="text-xs text-muted-foreground">
                    {t("prompts.testing.batch.csvFormat", "Formato CSV: input,expectedResponse (opcional: id)")}
                  </p>
                  {parsedTestCases.length > 0 && (
                    <p className="text-sm text-primary">
                      {t("prompts.testing.batch.loaded", "{count} test cases cargados", { count: parsedTestCases.length.toString() })}
                    </p>
                  )}
                </div>

                <Button
                  onClick={handleBatchTest}
                  disabled={batchTesting || !selectedPromptId || !selectedModelId || parsedTestCases.length === 0}
                  className="w-full"
                >
                  {batchTesting ? (
                    <>
                      <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                      {t("prompts.testing.batch.executing", "Ejecutando batch...")}
                    </>
                  ) : (
                    <>
                      <Play className="w-4 h-4 mr-2" />
                      {t("prompts.testing.batch.run", "Ejecutar Batch de Pruebas")}
                    </>
                  )}
                </Button>

                {batchResults.length > 0 && (
                  <div className="space-y-4 mt-6">
                    <div className="flex items-center justify-between">
                      <h3 className="font-semibold">{t("prompts.testing.batch.results", "Resultados")}</h3>
                      <Button variant="outline" size="sm" onClick={exportResults}>
                        <Download className="w-4 h-4 mr-2" />
                        {t("prompts.testing.batch.export", "Exportar CSV")}
                      </Button>
                    </div>

                    {/* Métricas */}
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                      <Card>
                        <CardContent className="pt-6">
                          <p className="text-sm text-muted-foreground mb-1">{t("prompts.testing.batch.total", "Total")}</p>
                          <p className="text-2xl font-bold">{batchResults.length}</p>
                        </CardContent>
                      </Card>
                      <Card>
                        <CardContent className="pt-6">
                          <p className="text-sm text-muted-foreground mb-1">{t("prompts.testing.batch.success", "Exitosos")}</p>
                          <p className="text-2xl font-bold text-green-600">{successCount}</p>
                        </CardContent>
                      </Card>
                      <Card>
                        <CardContent className="pt-6">
                          <p className="text-sm text-muted-foreground mb-1">{t("prompts.testing.batch.failed", "Fallidos")}</p>
                          <p className="text-2xl font-bold text-red-600">{failCount}</p>
                        </CardContent>
                      </Card>
                      <Card>
                        <CardContent className="pt-6">
                          <p className="text-sm text-muted-foreground mb-1">{t("prompts.testing.batch.avgSimilarity", "Similitud Promedio")}</p>
                          <p className="text-2xl font-bold">{(avgSimilarity * 100).toFixed(1)}%</p>
                        </CardContent>
                      </Card>
                    </div>

                    {/* Tabla de Resultados */}
                    <div className="border rounded-lg overflow-hidden">
                      <Table>
                        <TableHeader>
                          <TableRow>
                            <TableHead>{t("prompts.testing.batch.table.id", "ID")}</TableHead>
                            <TableHead>{t("prompts.testing.batch.table.input", "Input")}</TableHead>
                            <TableHead>{t("prompts.testing.batch.table.expected", "Esperado")}</TableHead>
                            <TableHead>{t("prompts.testing.batch.table.actual", "Actual")}</TableHead>
                            <TableHead>{t("prompts.testing.batch.table.similarity", "Similitud")}</TableHead>
                            <TableHead>{t("prompts.testing.batch.table.judge", "Juez")}</TableHead>
                            <TableHead>{t("prompts.testing.batch.table.status", "Estado")}</TableHead>
                          </TableRow>
                        </TableHeader>
                        <TableBody>
                          {batchResults.map((result, index) => (
                            <TableRow key={result.id || index}>
                              <TableCell>
                                <div className="font-medium">{result.id}</div>
                              </TableCell>
                              <TableCell>
                                <div className="max-w-[200px] truncate">{result.input || '-'}</div>
                              </TableCell>
                              <TableCell>
                                <div className="max-w-[200px] truncate">{result.expectedResponse || '-'}</div>
                              </TableCell>
                              <TableCell>
                                <div className="max-w-[200px] truncate">{result.actualResponse || '-'}</div>
                              </TableCell>
                              <TableCell>
                                {result.similarityScore !== null ? (
                                  <Badge variant={result.similarityScore >= 0.8 ? "primary" : result.similarityScore >= 0.6 ? "secondary" : "danger"}>
                                    {(result.similarityScore * 100).toFixed(1)}%
                                  </Badge>
                                ) : (
                                  '-'
                                )}
                              </TableCell>
                              <TableCell>
                                {result.judgeScore !== null ? (
                                  <Badge variant={result.judgeScore >= 0.8 ? "primary" : result.judgeScore >= 0.6 ? "secondary" : "danger"}>
                                    {(result.judgeScore * 100).toFixed(1)}%
                                  </Badge>
                                ) : (
                                  '-'
                                )}
                              </TableCell>
                              <TableCell>
                                {result.error ? (
                                  <Badge variant="danger">
                                    <XCircle className="w-3 h-3 mr-1" />
                                    {t("prompts.testing.batch.table.error", "Error")}
                                  </Badge>
                                ) : result.success === true ? (
                                  <Badge variant="primary">
                                    <CheckCircle2 className="w-3 h-3 mr-1" />
                                    {t("prompts.testing.batch.table.success", "Éxito")}
                                  </Badge>
                                ) : result.success === false ? (
                                  <Badge variant="danger">
                                    <XCircle className="w-3 h-3 mr-1" />
                                    {t("prompts.testing.batch.table.failed", "Fallido")}
                                  </Badge>
                                ) : (
                                  <Badge variant="secondary">{t("prompts.testing.batch.table.pending", "Pendiente")}</Badge>
                                )}
                              </TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </div>
                  </div>
                )}
              </CardContent>
            </Card>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
}
