// lib/api/designers-api.ts
// Integración con Spring Boot - diseñadores

import axios, { type AxiosInstance } from "axios";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

export class DesignersAPI {
  private client: AxiosInstance;

  constructor(client?: AxiosInstance) {
    this.client =
      client ??
      axios.create({
        baseURL: API_BASE_URL,
        headers: {
          "Content-Type": "application/json",
        },
      });
  }
  // Page Designer
  async savePageDesign(design: any) {
    return this.client.post("/api/v1/designs/page", design);
  }

  async generateCodeFromDesign(designId: string, techStack: string) {
    return this.client.post(`/api/v1/generate/from-design/${designId}`, {
      techStack,
    });
  }

  // BPMN Designer
  async saveBPMNProcess(bpmnXML: string) {
    return this.client.post("/api/v1/processes/bpmn", { xml: bpmnXML });
  }

  // Workflow Designer
  async saveWorkflow(workflow: any) {
    return this.client.post("/api/v1/workflows/ai", workflow);
  }

  async executeWorkflow(workflowId: string, input: any) {
    return this.client.post(`/api/v1/workflows/execute/${workflowId}`, input);
  }
}
