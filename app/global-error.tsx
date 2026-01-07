// app/global-error.tsx
'use client'

export default function GlobalError({
                                        error,
                                        reset,
                                    }: {
    error: Error & { digest?: string }
    reset: () => void
}) {
    const isDevelopment = process.env.NODE_ENV === 'development'

    return (
        <html>
        <body>
        <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
            <div className="max-w-md w-full text-center">
                <div className="text-6xl mb-4">🚨</div>
                <h1 className="text-2xl font-bold text-gray-900 mb-2">
                    Critical Error
                </h1>
                <p className="text-gray-600 mb-6">
                    A critical error occurred. Please refresh the page or contact support.
                </p>

                {isDevelopment && (
                    <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6 text-left">
                        <h3 className="font-medium text-red-900 mb-2">Global Error:</h3>
                        <div className="text-sm text-red-700 font-mono bg-red-100 p-2 rounded overflow-auto max-h-32">
                            {error.message}
                        </div>
                    </div>
                )}

                <div className="space-y-3">
                    <button
                        onClick={reset}
                        className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium"
                    >
                        🔄 Reset Application
                    </button>

                    <button
                        onClick={() => window.location.reload()}
                        className="w-full px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 font-medium"
                    >
                        🔄 Refresh Page
                    </button>
                </div>
            </div>
        </div>
        </body>
        </html>
    )
}
