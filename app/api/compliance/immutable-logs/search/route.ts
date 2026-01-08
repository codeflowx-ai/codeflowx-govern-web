import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, GATEWAY_WEB_BASE } from '@/app/config/mock';
import { mockImmutableLogs, type ImmutableLog } from "@/app/(app)/governance/data/mockImmutableLogs";

export interface ImmutableLogSearchCriteria {
  logType?: string;
  entityType?: string;
  entityId?: number;
  userId?: string;
  startDate?: string;
  endDate?: string;
  hash?: string;
  searchText?: string;
  page?: number;
  pageSize?: number;
  sortBy?: "timestamp" | "logType" | "entityType";
  sortOrder?: "asc" | "desc";
}

/**
 * POST /api/compliance/immutable-logs/search
 *
 * Busca logs inmutables según criterios de búsqueda
 *
 * Backend: codeflowx-governance-immutable-logs-service
 * Endpoint: POST /web/api/v1/compliance/immutable-logs/search
 */
export async function POST(request: NextRequest) {
  try {
    const criteria: ImmutableLogSearchCriteria = await request.json();

    if (USE_MOCK) {
      // Filtrar logs según criterios
      let filteredLogs = [...mockImmutableLogs];

      if (criteria.logType) {
        filteredLogs = filteredLogs.filter(log => log.logType === criteria.logType);
      }

      if (criteria.entityType) {
        filteredLogs = filteredLogs.filter(log => log.entityType === criteria.entityType);
      }

      if (criteria.entityId) {
        filteredLogs = filteredLogs.filter(log => log.entityId === criteria.entityId);
      }

      if (criteria.userId) {
        filteredLogs = filteredLogs.filter(log =>
          log.userId.toLowerCase().includes(criteria.userId!.toLowerCase())
        );
      }

      if (criteria.startDate || criteria.endDate) {
        filteredLogs = filteredLogs.filter(log => {
          const logDate = new Date(log.timestamp);
          if (criteria.startDate && logDate < new Date(criteria.startDate)) {
            return false;
          }
          if (criteria.endDate && logDate > new Date(criteria.endDate)) {
            return false;
          }
          return true;
        });
      }

      if (criteria.hash) {
        filteredLogs = filteredLogs.filter(log =>
          log.hash.toLowerCase().includes(criteria.hash!.toLowerCase()) ||
          log.previousHash.toLowerCase().includes(criteria.hash!.toLowerCase())
        );
      }

      if (criteria.searchText) {
        const searchLower = criteria.searchText.toLowerCase();
        filteredLogs = filteredLogs.filter(log =>
          log.logType.toLowerCase().includes(searchLower) ||
          log.entityType.toLowerCase().includes(searchLower) ||
          log.entityName?.toLowerCase().includes(searchLower) ||
          log.userId.toLowerCase().includes(searchLower) ||
          JSON.stringify(log.logData).toLowerCase().includes(searchLower)
        );
      }

      // Ordenar
      const sortBy = criteria.sortBy || "timestamp";
      const sortOrder = criteria.sortOrder || "desc";

      filteredLogs.sort((a, b) => {
        let aValue: any;
        let bValue: any;

        switch (sortBy) {
          case "timestamp":
            aValue = new Date(a.timestamp).getTime();
            bValue = new Date(b.timestamp).getTime();
            break;
          case "logType":
            aValue = a.logType;
            bValue = b.logType;
            break;
          case "entityType":
            aValue = a.entityType;
            bValue = b.entityType;
            break;
          default:
            aValue = new Date(a.timestamp).getTime();
            bValue = new Date(b.timestamp).getTime();
        }

        if (sortOrder === "asc") {
          return aValue > bValue ? 1 : -1;
        } else {
          return aValue < bValue ? 1 : -1;
        }
      });

      // Paginación
      const page = criteria.page || 1;
      const pageSize = criteria.pageSize || 20;
      const startIndex = (page - 1) * pageSize;
      const endIndex = startIndex + pageSize;
      const paginatedLogs = filteredLogs.slice(startIndex, endIndex);

      return NextResponse.json({
        logs: paginatedLogs,
        total: filteredLogs.length,
        page,
        pageSize,
        totalPages: Math.ceil(filteredLogs.length / pageSize)
      });
    }

    // Llamada real al backend
    const response = await fetch(`${GATEWAY_WEB_BASE}/compliance/immutable-logs/search`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(criteria),
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const result = await response.json();
    return NextResponse.json(result);
  } catch (error) {
    console.error("Error searching immutable logs:", error);
    return NextResponse.json(
      { error: "Error searching logs" },
      { status: 500 }
    );
  }
}
