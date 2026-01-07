import { useCallback } from 'react';

interface ScrapingJob {
  id: string;
  url: string;
  status: 'pending' | 'processing' | 'completed' | 'failed';
  documents: number;
  createdAt: Date;
  completedAt?: Date;
  error?: string;
  config: ScrapingConfig;
}

interface ScrapingConfig {
  maxPages: number;
  includeImages: boolean;
  followLinks: boolean;
  depth: number;
  delay: number;
  userAgent?: string;
  timeout: number;
  retryAttempts: number;
}

interface ScrapingRequest {
  url: string;
  maxPages?: number;
  includeImages?: boolean;
  followLinks?: boolean;
  depth?: number;
  delay?: number;
  userAgent?: string;
  timeout?: number;
  retryAttempts?: number;
}

interface ScrapingResponse {
  jobId: string;
  status: 'started' | 'failed';
  message?: string;
}

interface ScrapingStatus {
  jobId: string;
  status: 'pending' | 'processing' | 'completed' | 'failed';
  progress: number;
  pagesScraped: number;
  documentsCreated: number;
  error?: string;
  estimatedTimeRemaining?: number;
}

interface ScrapedPage {
  id: string;
  url: string;
  title: string;
  content: string;
  metadata: {
    description?: string;
    keywords?: string[];
    author?: string;
    publishedDate?: string;
    lastModified?: string;
    language?: string;
  };
  links: string[];
  images: string[];
  scrapedAt: Date;
  jobId: string;
}

interface ScrapingStats {
  totalJobs: number;
  completedJobs: number;
  failedJobs: number;
  totalPagesScraped: number;
  totalDocumentsCreated: number;
  averageScrapingTime: number;
  activeJobs: number;
}

export function useWebScrapingService() {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

  const startScraping = useCallback(async (request: ScrapingRequest): Promise<ScrapingResponse> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getScrapingJobs = useCallback(async (): Promise<ScrapingJob[]> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getScrapingJob = useCallback(async (jobId: string): Promise<ScrapingJob> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/${jobId}`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getScrapingStatus = useCallback(async (jobId: string): Promise<ScrapingStatus> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/${jobId}/status`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const cancelScrapingJob = useCallback(async (jobId: string): Promise<void> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/${jobId}/cancel`, {
      method: 'POST',
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
  }, [baseUrl]);

  const deleteScrapingJob = useCallback(async (jobId: string): Promise<void> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/${jobId}`, {
      method: 'DELETE',
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
  }, [baseUrl]);

  const retryScrapingJob = useCallback(async (jobId: string): Promise<ScrapingResponse> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/${jobId}/retry`, {
      method: 'POST',
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getScrapedPages = useCallback(async (jobId: string): Promise<ScrapedPage[]> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/${jobId}/pages`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getScrapedPage = useCallback(async (pageId: string): Promise<ScrapedPage> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/pages/${pageId}`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const updateScrapedPage = useCallback(async (pageId: string, updates: Partial<ScrapedPage>): Promise<ScrapedPage> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/pages/${pageId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updates),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const deleteScrapedPage = useCallback(async (pageId: string): Promise<void> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/pages/${pageId}`, {
      method: 'DELETE',
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
  }, [baseUrl]);

  const searchScrapedPages = useCallback(async (query: string, jobId?: string): Promise<ScrapedPage[]> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/pages/search`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ query, jobId }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getScrapingConfiguration = useCallback(async (): Promise<ScrapingConfig> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/config`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const updateScrapingConfiguration = useCallback(async (config: Partial<ScrapingConfig>): Promise<ScrapingConfig> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/config`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(config),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const validateUrl = useCallback(async (url: string): Promise<{ valid: boolean; message?: string }> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/validate-url`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ url }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getUrlInfo = useCallback(async (url: string) => {
    const response = await fetch(`${baseUrl}/api/web-scraping/url-info`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ url }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getScrapingStats = useCallback(async (): Promise<ScrapingStats> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/stats`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getJobStats = useCallback(async (jobId: string) => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/${jobId}/stats`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const startBatchScraping = useCallback(async (requests: ScrapingRequest[]): Promise<ScrapingResponse[]> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/batch`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ requests }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const cancelAllActiveJobs = useCallback(async (): Promise<void> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/cancel-all`, {
      method: 'POST',
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
  }, [baseUrl]);

  const cleanupCompletedJobs = useCallback(async (): Promise<{ deleted: number }> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/cleanup`, {
      method: 'POST',
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const exportScrapedPages = useCallback(async (jobId: string, format: 'json' | 'csv' | 'txt' = 'json'): Promise<Blob> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/${jobId}/export?format=${format}`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.blob();
  }, [baseUrl]);

  const getActiveJobs = useCallback(async (): Promise<ScrapingJob[]> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/active`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getJobLogs = useCallback(async (jobId: string): Promise<string[]> => {
    const response = await fetch(`${baseUrl}/api/web-scraping/jobs/${jobId}/logs`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getRealTimeMetrics = useCallback(async () => {
    const response = await fetch(`${baseUrl}/api/web-scraping/metrics/realtime`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const healthCheck = useCallback(async () => {
    const response = await fetch(`${baseUrl}/api/web-scraping/health`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  return {
    startScraping,
    getScrapingJobs,
    getScrapingJob,
    getScrapingStatus,
    cancelScrapingJob,
    deleteScrapingJob,
    retryScrapingJob,
    getScrapedPages,
    getScrapedPage,
    updateScrapedPage,
    deleteScrapedPage,
    searchScrapedPages,
    getScrapingConfiguration,
    updateScrapingConfiguration,
    validateUrl,
    getUrlInfo,
    getScrapingStats,
    getJobStats,
    startBatchScraping,
    cancelAllActiveJobs,
    cleanupCompletedJobs,
    exportScrapedPages,
    getActiveJobs,
    getJobLogs,
    getRealTimeMetrics,
    healthCheck,
  };
} 