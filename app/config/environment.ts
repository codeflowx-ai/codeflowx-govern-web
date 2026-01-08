// app/config/environment.ts
export interface EnvironmentConfig {
  useMock: boolean;
  mockDelay: number;
  backendUrl: string;
  enableMockToggle: boolean;
  fallbackToMock: boolean;
}

const environments: Record<string, EnvironmentConfig> = {
  development: {
    useMock: true,
    mockDelay: 1000,
    backendUrl: 'http://localhost:8080',
    enableMockToggle: true,
    fallbackToMock: true
  },
  staging: {
    useMock: false,
    mockDelay: 500,
    backendUrl: 'https://staging-api.videcodeweb.com',
    enableMockToggle: true,
    fallbackToMock: true
  },
  production: {
    useMock: false,
    mockDelay: 0,
    backendUrl: 'https://api.videcodeweb.com',
    enableMockToggle: false,
    fallbackToMock: false
  }
};

export const getEnvironmentConfig = (): EnvironmentConfig => {
  const env = process.env.NEXT_PUBLIC_ENV || 'development';
  return environments[env] || environments.development;
};

export const config = getEnvironmentConfig();
