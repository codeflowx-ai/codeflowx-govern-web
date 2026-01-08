// components/dashboard/RoadmapWidget.tsx
'use client'

import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card'
import Link from 'next/link'

const upcomingFeatures = [
    {
        title: 'Advanced Fine-tuning Pipeline',
        status: 'in-progress',
        completion: 65,
        eta: 'June 2024'
    },
    {
        title: 'Real-time Collaboration',
        status: 'in-progress',
        completion: 40,
        eta: 'July 2024'
    },
    {
        title: 'Mobile App Development',
        status: 'planned',
        completion: 0,
        eta: 'Q3 2024'
    }
]

export function RoadmapWidget() {
    return (
        <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center justify-between mb-4">
                <h3 className="font-semibold text-gray-900 flex items-center gap-2">
                    <span className="text-lg">🗺️</span>
                    Upcoming Features
                </h3>
                <Link href="/roadmap" className="text-sm text-blue-600 hover:text-blue-700">
                    View roadmap →
                </Link>
            </div>

            <div className="space-y-3">
                {upcomingFeatures.map((feature, index) => (
                    <div key={index} className="space-y-2">
                        <div className="flex items-center justify-between">
                            <span className="text-sm font-medium text-gray-900">{feature.title}</span>
                            <span className="text-xs text-gray-500">{feature.eta}</span>
                        </div>
                        {feature.status === 'in-progress' && (
                            <div className="w-full bg-gray-200 rounded-full h-1.5">
                                <div
                                    className="bg-blue-600 h-1.5 rounded-full transition-all duration-300"
                                    style={{ width: `${feature.completion}%` }}
                                ></div>
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    )
}