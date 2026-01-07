// app/not-found.tsx
import Link from 'next/link'

export default function NotFound() {
    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
            <div className="max-w-md w-full text-center">
                <div className="text-6xl mb-4">🔍</div>
                <h1 className="text-2xl font-bold text-gray-900 mb-2">
                    Page Not Found
                </h1>
                <p className="text-gray-600 mb-6">
                    The page you're looking for doesn't exist or has been moved.
                </p>

                <div className="space-y-3">
                    <Link
                        href="/dashboard"
                        className="block w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium"
                    >
                        🏠 Go to Dashboard
                    </Link>

                    <Link
                        href="/"
                        className="block w-full px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 font-medium"
                    >
                        🔐 Back to Login
                    </Link>
                </div>
            </div>
        </div>
    )
}
