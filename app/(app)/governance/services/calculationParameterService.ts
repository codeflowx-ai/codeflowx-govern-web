/**
 * Servicio API para gestión de parámetros de cálculo
 */

import {
  CalculationParameter,
  CalculationParameterRequest,
  ParameterHierarchy,
  ValidationResult
} from '@/types/calculation-parameter';

const API_BASE = '/api/governance/calculation-parameters';

export const calculationParameterService = {
  /**
   * Listar parámetros
   */
  async list(filters?: {
    microservice?: string;
    parameterKey?: string;
    organizationId?: string;
  }): Promise<CalculationParameter[]> {
    const params = new URLSearchParams();
    if (filters?.microservice) params.append('microservice', filters.microservice);
    if (filters?.parameterKey) params.append('parameterKey', filters.parameterKey);
    if (filters?.organizationId) params.append('organizationId', filters.organizationId);

    const url = `${API_BASE}${params.toString() ? '?' + params.toString() : ''}`;
    const response = await fetch(url, { cache: 'no-store' });

    if (!response.ok) {
      throw new Error('Error al obtener parámetros');
    }

    return response.json();
  },

  /**
   * Obtener por ID
   */
  async getById(id: number): Promise<CalculationParameter> {
    const response = await fetch(`${API_BASE}/${id}`, { cache: 'no-store' });

    if (!response.ok) {
      throw new Error('Parámetro no encontrado');
    }

    return response.json();
  },

  /**
   * Crear
   */
  async create(request: CalculationParameterRequest): Promise<CalculationParameter> {
    const response = await fetch(API_BASE, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      const error = await response.json().catch(() => ({}));
      throw new Error(error.error || 'Error al crear parámetro');
    }

    return response.json();
  },

  /**
   * Actualizar
   */
  async update(id: number, request: CalculationParameterRequest): Promise<CalculationParameter> {
    const response = await fetch(`${API_BASE}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      const error = await response.json().catch(() => ({}));
      throw new Error(error.error || 'Error al actualizar parámetro');
    }

    return response.json();
  },

  /**
   * Activar/Desactivar
   */
  async toggleActive(id: number, active: boolean): Promise<CalculationParameter> {
    const response = await fetch(`${API_BASE}/${id}/toggle-active?active=${active}`, {
      method: 'PATCH',
    });

    if (!response.ok) {
      throw new Error('Error al cambiar estado');
    }

    return response.json();
  },

  /**
   * Obtener jerarquía
   */
  async getHierarchy(
    microservice: string,
    parameterKey: string
  ): Promise<ParameterHierarchy> {
    const params = new URLSearchParams({ microservice, parameterKey });
    const response = await fetch(`${API_BASE}/hierarchy?${params.toString()}`, {
      cache: 'no-store',
    });

    if (!response.ok) {
      throw new Error('Error al obtener jerarquía');
    }

    return response.json();
  },

  /**
   * Validar
   */
  async validate(request: {
    microservice: string;
    parameterKey: string;
    parameters: Record<string, any>;
  }): Promise<ValidationResult> {
    const response = await fetch(`${API_BASE}/validate`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      throw new Error('Error al validar parámetros');
    }

    return response.json();
  },

  /**
   * Eliminar
   */
  async delete(id: number): Promise<void> {
    const response = await fetch(`${API_BASE}/${id}`, {
      method: 'DELETE',
    });

    if (!response.ok) {
      throw new Error('Error al eliminar parámetro');
    }
  }
};
