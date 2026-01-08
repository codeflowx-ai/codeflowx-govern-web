// next.config.js - Optimizado para performance
/** @type {import('next').NextConfig} */
const nextConfig = {
  // Habilitar modo standalone para Docker
  output: 'standalone',

  // Habilitar caché en desarrollo para compilaciones más rápidas
  generateEtags: true,

  // Orígenes permitidos en desarrollo para solicitudes cross-origin
  allowedDevOrigins: [
    'http://localhost:3000',
    'http://localhost:3001',
    'http://127.0.0.1:3000',
    'http://127.0.0.1:3001',
    'http://192.168.1.11:3000',
    'http://192.168.1.11:3001',
  ],

  // Optimización de compilación en desarrollo
  swcMinify: true,

  // Headers para evitar caché
  async headers() {
    return [
      {
        source: '/login',
        headers: [
          {
            key: 'Cache-Control',
            value: 'no-cache, no-store, must-revalidate',
          },
          {
            key: 'Pragma',
            value: 'no-cache',
          },
          {
            key: 'Expires',
            value: '0',
          },
        ],
      },
    ]
  },

  // Configuración de desarrollo
  experimental: {
    // Forzar recarga en desarrollo
    forceSwcTransforms: true,
    // Optimizaciones de performance
    optimizeCss: true,
    optimizePackageImports: [
      '@monaco-editor/react',
      '@craftjs/core',
      'fabric',
      'konva',
      'react-konva',
      'react-dnd',
      'react-dnd-html5-backend'
    ],
  },

  // Optimización de webpack para code splitting
  webpack: (config, { dev, isServer }) => {
    // Configuración de watch para hot reload (optimizado para Windows)
    if (dev) {
      config.watchOptions = {
        poll: false, // No usar polling en Windows nativo
        aggregateTimeout: 200, // Tiempo de espera para agrupar cambios
        ignored: [
          '**/node_modules/**',
          '**/.next/**',
          '**/.git/**',
          '**/dist/**',
          '**/build/**',
          '**/backup/**', // Ignorar directorio backup
          '**/*.md', // Ignorar archivos markdown
        ],
      }

      // Optimizaciones de caché en desarrollo
      config.cache = {
        type: 'filesystem',
        buildDependencies: {
          config: [__filename],
        },
        compression: 'gzip',
        maxMemoryGenerations: 1,
      }
    }

    if (!dev && !isServer) {
      config.optimization.splitChunks = {
        chunks: 'all',
        minSize: 20000,
        maxSize: 244000,
        cacheGroups: {
          // Monaco Editor - Editor de código
          monaco: {
            test: /[\\/]node_modules[\\/]@monaco-editor[\\/]/,
            name: 'monaco',
            chunks: 'all',
            priority: 30,
            reuseExistingChunk: true,
          },
          // CraftJS - Form Designer
          craftjs: {
            test: /[\\/]node_modules[\\/]@craftjs[\\/]/,
            name: 'craftjs',
            chunks: 'all',
            priority: 30,
            reuseExistingChunk: true,
          },
          // Fabric.js - Canvas manipulation
          fabric: {
            test: /[\\/]node_modules[\\/]fabric[\\/]/,
            name: 'fabric',
            chunks: 'all',
            priority: 30,
            reuseExistingChunk: true,
          },
          // Konva - 2D Graphics
          konva: {
            test: /[\\/]node_modules[\\/]konva[\\/]/,
            name: 'konva',
            chunks: 'all',
            priority: 30,
            reuseExistingChunk: true,
          },
          // React DnD - Drag and Drop
          reactDnd: {
            test: /[\\/]node_modules[\\/]react-dnd[\\/]/,
            name: 'react-dnd',
            chunks: 'all',
            priority: 25,
            reuseExistingChunk: true,
          },
          // CodeMirror - Text editors
          codemirror: {
            test: /[\\/]node_modules[\\/]@codemirror[\\/]/,
            name: 'codemirror',
            chunks: 'all',
            priority: 25,
            reuseExistingChunk: true,
          },
          // Bootstrap - UI Framework
          bootstrap: {
            test: /[\\/]node_modules[\\/]bootstrap[\\/]/,
            name: 'bootstrap',
            chunks: 'all',
            priority: 20,
            reuseExistingChunk: true,
          },
          // Radix UI - Components
          radix: {
            test: /[\\/]node_modules[\\/]@radix-ui[\\/]/,
            name: 'radix-ui',
            chunks: 'all',
            priority: 20,
            reuseExistingChunk: true,
          },
          // Otros vendor libraries
          vendor: {
            test: /[\\/]node_modules[\\/]/,
            name: 'vendors',
            chunks: 'all',
            priority: 10,
            reuseExistingChunk: true,
          },
        },
      }
    }

    // Optimización de resolvers
    config.resolve.alias = {
      ...config.resolve.alias,
      '@': require('path').resolve(__dirname, '.'),
    }

    return config
  },
}

module.exports = nextConfig
