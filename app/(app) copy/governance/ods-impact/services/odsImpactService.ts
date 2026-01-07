/**
 * Servicio para integración con backend de KPIs ODS Impact
 * Medición del impacto de la plataforma en los Objetivos de Desarrollo Sostenible
 *
 * Backend Services:
 * - ODSImpactBusinessService
 * - ODSImpactService
 */

import {
  ODSImpactDashboard,
  ODSImpact,
  type ODSKPI,
} from "../data/mockODSKPIs";
import { mockODSImpactDashboard, getODSData } from "../data/mockODSKPIs";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:3000";
const IS_DEMO_MODE =
  process.env.NEXT_PUBLIC_DEMO_MODE === "true" ||
  !process.env.NEXT_PUBLIC_API_URL;

export interface ODSImpactServiceResponse<T> {
  success: boolean;
  data?: T;
  error?: string;
}

class ODSImpactService {
  private baseUrl = API_BASE_URL;

  private isDemoMode(): boolean {
    return IS_DEMO_MODE;
  }

  /**
   * Obtiene el dashboard principal de impacto ODS
   */
  async getDashboard(): Promise<
    ODSImpactServiceResponse<ODSImpactDashboard>
  > {
    if (this.isDemoMode()) {
      return {
        success: true,
        data: mockODSImpactDashboard,
      };
    }

    try {
      const response = await fetch(
        `${this.baseUrl}/api/v1/compliance/ods-impact`
      );
      if (!response.ok)
        throw new Error("Failed to fetch ODS impact dashboard");
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching ODS impact dashboard: ${error}`,
      };
    }
  }

  /**
   * Obtiene los datos de un ODS específico
   */
  async getODSData(odsNumber: number): Promise<ODSImpactServiceResponse<ODSImpact>> {
    if (this.isDemoMode()) {
      const data = getODSData(odsNumber);
      if (!data) {
        return {
          success: false,
          error: `ODS ${odsNumber} data not found`,
        };
      }
      return {
        success: true,
        data,
      };
    }

    try {
      const response = await fetch(
        `${this.baseUrl}/api/v1/compliance/ods-impact/ods-${odsNumber}`
      );
      if (!response.ok)
        throw new Error(`Failed to fetch ODS ${odsNumber} data`);
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching ODS ${odsNumber} data: ${error}`,
      };
    }
  }

  /**
   * Obtiene un KPI específico de un ODS
   */
  async getKPI(
    odsNumber: number,
    kpiCode: string
  ): Promise<ODSImpactServiceResponse<ODSKPI>> {
    if (this.isDemoMode()) {
      const odsData = getODSData(odsNumber);
      if (!odsData) {
        return {
          success: false,
          error: `ODS ${odsNumber} data not found`,
        };
      }
      const kpi = odsData.kpis.find((k) => k.code === kpiCode);
      if (!kpi) {
        return {
          success: false,
          error: `KPI ${kpiCode} not found in ODS ${odsNumber}`,
        };
      }
      return {
        success: true,
        data: kpi,
      };
    }

    try {
      const response = await fetch(
        `${this.baseUrl}/api/v1/compliance/ods-impact/ods-${odsNumber}/kpis/${kpiCode}`
      );
      if (!response.ok)
        throw new Error(`Failed to fetch KPI ${kpiCode} for ODS ${odsNumber}`);
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching KPI ${kpiCode} for ODS ${odsNumber}: ${error}`,
      };
    }
  }
}

export const odsImpactService = new ODSImpactService();
