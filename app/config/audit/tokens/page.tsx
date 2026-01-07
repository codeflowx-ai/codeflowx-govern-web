// app/config/audit/tokens/page.tsx
"use client";

import DevelopmentBanner from "@/components/ui/development-banner";
import { useState } from "react";

interface TokenUsage {
  id: string;
  timestamp: string;
  user: string;
  model: string;
  operation: string;
  tokensUsed: number;
  responseTime: number;
  status: "success" | "error" | "timeout";
  project?: string;
}

const mockTokenUsage: TokenUsage[] = [
  {
    id: "1",
    timestamp: "2024-01-15T10:30:00Z",
    user: "Developer User",
    model: "leka-angular-v2",
    operation: "code_generation",
    tokensUsed: 1250,
    responseTime: 2.3,
    status: "success",
    project: "E-commerce Platform",
  },
  {
    id: "2",
    timestamp: "2024-01-15T10:25:00Z",
    user: "Admin User",
    model: "leka-springboot-v2",
    operation: "fine_tuning",
    tokensUsed: 15000,
    responseTime: 45.2,
    status: "success",
    project: "Healthcare API",
  },
  {
    id: "3",
    timestamp: "2024-01-15T10:20:00Z",
    user: "Developer User",
    model: "leka-react-v2",
    operation: "code_generation",
    tokensUsed: 890,
    responseTime: 1.8,
    status: "success",
    project: "Dashboard Components",
  },
  {
    id: "4",
    timestamp: "2024-01-15T10:15:00Z",
    user: "Developer User",
    model: "leka-custom-model",
    operation: "training",
    tokensUsed: 0,
    responseTime: 0,
    status: "error",
    project: "Custom Training",
  },
];

export default function TokenAuditPage() {
  const [usage] = useState<TokenUsage[]>(mockTokenUsage);
  const [filter, setFilter] = useState("all");
  const [timeRange, setTimeRange] = useState("24h");

  const filteredUsage = usage.filter((item) => {
    if (filter === "all") return true;
    return item.operation === filter;
  });

  const getStatusColor = (status: string) => {
    switch (status) {
      case "success":
        return "bg-green-100 text-green-700";
      case "error":
        return "bg-red-100 text-red-700";
      case "timeout":
        return "bg-yellow-100 text-yellow-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  const getOperationIcon = (operation: string) => {
    switch (operation) {
      case "code_generation":
        return "⚡";
      case "fine_tuning":
        return "🎯";
      case "training":
        return "🧠";
      case "chat":
        return "💬";
      default:
        return "📝";
    }
  };

  const stats = {
    totalTokens: usage.reduce((sum, item) => sum + item.tokensUsed, 0),
    totalRequests: usage.length,
    avgResponseTime:
      usage.reduce((sum, item) => sum + item.responseTime, 0) / usage.length,
    successRate:
      (usage.filter((item) => item.status === "success").length /
        usage.length) *
      100,
  };

  const topUsers = usage.reduce((acc, item) => {
    acc[item.user] = (acc[item.user] || 0) + item.tokensUsed;
    return acc;
  }, {} as Record<string, number>);

  const topModels = usage.reduce((acc, item) => {
    acc[item.model] = (acc[item.model] || 0) + item.tokensUsed;
    return acc;
  }, {} as Record<string, number>);

  return (
    <div className="p-6 space-y-6">
      
      {/* Header */}
      <div className="space-y-2">
        <h1 className="text-2xl font-bold text-gray-900">
          🔢 Token Usage Audit
        </h1>
        <p className="text-gray-600">Monitor AI model usage and performance</p>
        {/* Banner de Desarrollo - Versión 1.0.0 Operativa */}
        <DevelopmentBanner
          type="operational"
          customText="✅ Versión 1.0.0 - Operativa"
          showEarlyAdopterButton={false}
          className="justify-start"
        />
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-white rounded-lg border border-gray-200 p-4">
          <div className="flex items-center gap-3">
            <span className="text-2xl">🔢</span>
            <div>
              <p className="text-sm text-gray-600">Total Tokens</p>
              <p className="text-xl font-bold">
                {stats.totalTokens.toLocaleString()}
              </p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg border border-gray-200 p-4">
          <div className="flex items-center gap-3">
            <span className="text-2xl">📊</span>
            <div>
              <p className="text-sm text-gray-600">Requests</p>
              <p className="text-xl font-bold">{stats.totalRequests}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg border border-gray-200 p-4">
          <div className="flex items-center gap-3">
            <span className="text-2xl">⏱️</span>
            <div>
              <p className="text-sm text-gray-600">Avg Response</p>
              <p className="text-xl font-bold">
                {stats.avgResponseTime.toFixed(1)}s
              </p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg border border-gray-200 p-4">
          <div className="flex items-center gap-3">
            <span className="text-2xl">✅</span>
            <div>
              <p className="text-sm text-gray-600">Success Rate</p>
              <p className="text-xl font-bold">
                {stats.successRate.toFixed(1)}%
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Top Usage */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white rounded-lg border border-gray-200 p-4">
          <h3 className="font-semibold text-gray-900 mb-4">
            👥 Top Users by Tokens
          </h3>
          <div className="space-y-2">
            {Object.entries(topUsers)
              .sort(([, a], [, b]) => b - a)
              .slice(0, 5)
              .map(([user, tokens]) => (
                <div key={user} className="flex justify-between items-center">
                  <span className="text-sm text-gray-700">{user}</span>
                  <span className="font-medium">{tokens.toLocaleString()}</span>
                </div>
              ))}
          </div>
        </div>

        <div className="bg-white rounded-lg border border-gray-200 p-4">
          <h3 className="font-semibold text-gray-900 mb-4">
            🧠 Top Models by Usage
          </h3>
          <div className="space-y-2">
            {Object.entries(topModels)
              .sort(([, a], [, b]) => b - a)
              .slice(0, 5)
              .map(([model, tokens]) => (
                <div key={model} className="flex justify-between items-center">
                  <span className="text-sm text-gray-700">{model}</span>
                  <span className="font-medium">{tokens.toLocaleString()}</span>
                </div>
              ))}
          </div>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg border border-gray-200 p-4">
        <div className="flex flex-wrap gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Operation
            </label>
            <select
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-lg text-sm"
            >
              <option value="all">All Operations</option>
              <option value="code_generation">Code Generation</option>
              <option value="fine_tuning">Fine-tuning</option>
              <option value="training">Training</option>
              <option value="chat">Chat</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Time Range
            </label>
            <select
              value={timeRange}
              onChange={(e) => setTimeRange(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-lg text-sm"
            >
              <option value="1h">Last Hour</option>
              <option value="24h">Last 24 Hours</option>
              <option value="7d">Last 7 Days</option>
              <option value="30d">Last 30 Days</option>
            </select>
          </div>

          <div className="flex items-end">
            <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 text-sm">
              📊 Generate Report
            </button>
          </div>
        </div>
      </div>

      {/* Usage Table */}
      <div className="bg-white rounded-lg border border-gray-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  Timestamp
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  User
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  Operation
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  Model
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  Tokens
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  Response Time
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  Status
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {filteredUsage.map((item) => (
                <tr key={item.id} className="hover:bg-gray-50">
                  <td className="px-4 py-3 text-sm text-gray-900">
                    {new Date(item.timestamp).toLocaleString()}
                  </td>
                  <td className="px-4 py-3 text-sm text-gray-900">
                    {item.user}
                  </td>
                  <td className="px-4 py-3">
                    <div className="flex items-center gap-2">
                      <span>{getOperationIcon(item.operation)}</span>
                      <span className="text-sm text-gray-900">
                        {item.operation}
                      </span>
                    </div>
                  </td>
                  <td className="px-4 py-3 text-sm text-gray-900 font-mono">
                    {item.model}
                  </td>
                  <td className="px-4 py-3 text-sm font-medium text-gray-900">
                    {item.tokensUsed.toLocaleString()}
                  </td>
                  <td className="px-4 py-3 text-sm text-gray-900">
                    {item.responseTime}s
                  </td>
                  <td className="px-4 py-3">
                    <span
                      className={`inline-flex items-center px-2 py-1 text-xs rounded-full font-medium ${getStatusColor(
                        item.status
                      )}`}
                    >
                      {item.status}
                    </span>
                    {item.project && (
                      <div className="text-xs text-gray-500 mt-1">
                        {item.project}
                      </div>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
