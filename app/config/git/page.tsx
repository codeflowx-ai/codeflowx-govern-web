// app/config/git/page.tsx
"use client";

import DevelopmentBanner from "@/components/ui/development-banner";
import { useState } from "react";

interface GitRepository {
  id: string;
  name: string;
  url: string;
  branch: string;
  provider: "github" | "gitlab" | "bitbucket";
  accessToken: string;
  isDefault: boolean;
  status: "connected" | "error" | "pending";
  lastSync: string;
  projects: string[];
}

const mockRepositories: GitRepository[] = [
  {
    id: "1",
    name: "Main Repository",
    url: "https://github.com/company/main-repo.git",
    branch: "main",
    provider: "github",
    accessToken: "ghp_xxxxxxxxxxxx",
    isDefault: true,
    status: "connected",
    lastSync: "2 hours ago",
    projects: ["E-commerce Platform", "Healthcare Dashboard"],
  },
  {
    id: "2",
    name: "Frontend Projects",
    url: "https://gitlab.com/company/frontend-repo.git",
    branch: "develop",
    provider: "gitlab",
    accessToken: "glpat_xxxxxxxxxxxx",
    isDefault: false,
    status: "connected",
    lastSync: "1 day ago",
    projects: ["Angular Components"],
  },
];

export default function GitConfigPage() {
  const [repositories, setRepositories] =
    useState<GitRepository[]>(mockRepositories);
  const [showModal, setShowModal] = useState(false);
  const [editingRepo, setEditingRepo] = useState<GitRepository | null>(null);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState<string | null>(
    null
  );
  const [testingConnection, setTestingConnection] = useState<string | null>(
    null
  );
  const [formData, setFormData] = useState({
    name: "",
    url: "",
    branch: "main",
    provider: "github" as GitRepository["provider"],
    accessToken: "",
    isDefault: false,
  });

  const handleCreate = () => {
    setEditingRepo(null);
    setFormData({
      name: "",
      url: "",
      branch: "main",
      provider: "github",
      accessToken: "",
      isDefault: false,
    });
    setShowModal(true);
  };

  const handleEdit = (repo: GitRepository) => {
    setEditingRepo(repo);
    setFormData({
      name: repo.name,
      url: repo.url,
      branch: repo.branch,
      provider: repo.provider,
      accessToken: repo.accessToken,
      isDefault: repo.isDefault,
    });
    setShowModal(true);
  };

  const handleSave = () => {
    if (editingRepo) {
      // Editar repositorio existente
      setRepositories((prev) =>
        prev.map((repo) =>
          repo.id === editingRepo.id
            ? {
                ...repo,
                ...formData,
                // Si se marca como default, quitar default de otros
                isDefault: formData.isDefault ? true : repo.isDefault,
              }
            : formData.isDefault
            ? { ...repo, isDefault: false }
            : repo
        )
      );
    } else {
      // Crear nuevo repositorio
      const newRepo: GitRepository = {
        id: Date.now().toString(),
        ...formData,
        status: "pending",
        lastSync: "Never",
        projects: [],
      };

      setRepositories((prev) => {
        if (formData.isDefault) {
          // Si es default, quitar default de otros
          return [
            ...prev.map((repo) => ({ ...repo, isDefault: false })),
            newRepo,
          ];
        }
        return [...prev, newRepo];
      });
    }
    setShowModal(false);
  };

  const handleDelete = (repoId: string) => {
    setRepositories((prev) => prev.filter((repo) => repo.id !== repoId));
    setShowDeleteConfirm(null);
  };

  const handleTestConnection = async (repoId: string) => {
    setTestingConnection(repoId);

    // Simular test de conexión
    setTimeout(() => {
      setRepositories((prev) =>
        prev.map((repo) =>
          repo.id === repoId
            ? { ...repo, status: Math.random() > 0.2 ? "connected" : "error" }
            : repo
        )
      );
      setTestingConnection(null);
    }, 2000);
  };

  const handleSetDefault = (repoId: string) => {
    setRepositories((prev) =>
      prev.map((repo) => ({
        ...repo,
        isDefault: repo.id === repoId,
      }))
    );
  };

  const getProviderIcon = (provider: string) => {
    switch (provider) {
      case "github":
        return "🐙";
      case "gitlab":
        return "🦊";
      case "bitbucket":
        return "🪣";
      default:
        return "📁";
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "connected":
        return "bg-green-100 text-green-700";
      case "error":
        return "bg-red-100 text-red-700";
      case "pending":
        return "bg-yellow-100 text-yellow-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  return (
    <div className="p-6 space-y-6">
      
      {/* Header */}
      <div className="flex items-start justify-between">
        <div className="space-y-2">
          <h1 className="text-2xl font-bold text-gray-900">
            🔗 Git Configuration
          </h1>
          <p className="text-gray-600">
            Manage your Git repositories for code deployment
          </p>
          {/* Banner de Desarrollo - Versión 1.0.0 Operativa */}
          <DevelopmentBanner
            type="operational"
            customText="✅ Versión 1.0.0 - Operativa"
            showEarlyAdopterButton={false}
            className="justify-start"
          />
        </div>
        <button
          onClick={handleCreate}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 flex items-center gap-2"
        >
          <span>➕</span>
          Add Repository
        </button>
      </div>

      {/* Repositories Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {repositories.map((repo) => (
          <div
            key={repo.id}
            className="bg-white rounded-lg border border-gray-200 p-6"
          >
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <span className="text-2xl">
                  {getProviderIcon(repo.provider)}
                </span>
                <div>
                  <h3 className="font-semibold text-gray-900 flex items-center gap-2">
                    {repo.name}
                    {repo.isDefault && (
                      <span className="px-2 py-1 text-xs bg-blue-100 text-blue-700 rounded-full">
                        Default
                      </span>
                    )}
                  </h3>
                  <p className="text-sm text-gray-500">{repo.provider}</p>
                </div>
              </div>
              <span
                className={`px-2 py-1 text-xs rounded-full font-medium ${getStatusColor(
                  repo.status
                )}`}
              >
                {repo.status}
              </span>
            </div>

            <div className="space-y-3 mb-4">
              <div>
                <label className="text-xs font-medium text-gray-500">
                  Repository URL
                </label>
                <p className="text-sm text-gray-900 font-mono bg-gray-50 p-2 rounded truncate">
                  {repo.url}
                </p>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-medium text-gray-500">
                    Branch
                  </label>
                  <p className="text-sm text-gray-900">{repo.branch}</p>
                </div>
                <div>
                  <label className="text-xs font-medium text-gray-500">
                    Last Sync
                  </label>
                  <p className="text-sm text-gray-900">{repo.lastSync}</p>
                </div>
              </div>

              {repo.projects.length > 0 && (
                <div>
                  <label className="text-xs font-medium text-gray-500">
                    Connected Projects
                  </label>
                  <div className="flex flex-wrap gap-1 mt-1">
                    {repo.projects.map((project, index) => (
                      <span
                        key={index}
                        className="px-2 py-1 text-xs bg-gray-100 text-gray-700 rounded"
                      >
                        {project}
                      </span>
                    ))}
                  </div>
                </div>
              )}
            </div>

            <div className="flex items-center gap-2">
              <button
                onClick={() => handleTestConnection(repo.id)}
                disabled={testingConnection === repo.id}
                className="px-3 py-1 text-sm bg-green-100 text-green-700 rounded hover:bg-green-200 disabled:opacity-50"
              >
                {testingConnection === repo.id ? "🔄 Testing..." : "🔍 Test"}
              </button>

              {!repo.isDefault && (
                <button
                  onClick={() => handleSetDefault(repo.id)}
                  className="px-3 py-1 text-sm bg-blue-100 text-blue-700 rounded hover:bg-blue-200"
                >
                  ⭐ Set Default
                </button>
              )}

              <button
                onClick={() => handleEdit(repo)}
                className="px-3 py-1 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200"
              >
                ✏️ Edit
              </button>

              <button
                onClick={() => setShowDeleteConfirm(repo.id)}
                className="px-3 py-1 text-sm bg-red-100 text-red-700 rounded hover:bg-red-200"
                disabled={repo.isDefault}
              >
                🗑️ Delete
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Modal para crear/editar repositorio */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-lg">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              {editingRepo ? "Edit Repository" : "Add New Repository"}
            </h3>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Repository Name
                </label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) =>
                    setFormData({ ...formData, name: e.target.value })
                  }
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="My Repository"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Provider
                </label>
                <select
                  value={formData.provider}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      provider: e.target.value as GitRepository["provider"],
                    })
                  }
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                >
                  <option value="github">🐙 GitHub</option>
                  <option value="gitlab">🦊 GitLab</option>
                  <option value="bitbucket">🪣 Bitbucket</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Repository URL
                </label>
                <input
                  type="url"
                  value={formData.url}
                  onChange={(e) =>
                    setFormData({ ...formData, url: e.target.value })
                  }
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="https://github.com/username/repo.git"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Branch
                </label>
                <input
                  type="text"
                  value={formData.branch}
                  onChange={(e) =>
                    setFormData({ ...formData, branch: e.target.value })
                  }
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="main"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Access Token
                </label>
                <input
                  type="password"
                  value={formData.accessToken}
                  onChange={(e) =>
                    setFormData({ ...formData, accessToken: e.target.value })
                  }
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="ghp_xxxxxxxxxxxx"
                />
                <p className="text-xs text-gray-500 mt-1">
                  Personal access token with repository permissions
                </p>
              </div>

              <div className="flex items-center gap-2">
                <input
                  type="checkbox"
                  id="isDefault"
                  checked={formData.isDefault}
                  onChange={(e) =>
                    setFormData({ ...formData, isDefault: e.target.checked })
                  }
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <label htmlFor="isDefault" className="text-sm text-gray-700">
                  Set as default repository
                </label>
              </div>
            </div>

            <div className="flex gap-3 mt-6">
              <button
                onClick={() => setShowModal(false)}
                className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50"
              >
                Cancel
              </button>
              <button
                onClick={handleSave}
                className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
              >
                {editingRepo ? "Save Changes" : "Add Repository"}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal de confirmación de borrado */}
      {showDeleteConfirm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-sm">
            <div className="text-center">
              <div className="text-4xl mb-4">🗑️</div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                Delete Repository?
              </h3>
              <p className="text-sm text-gray-600 mb-6">
                This will remove the repository configuration. Projects using
                this repository will need to be reconfigured.
              </p>
              <div className="flex gap-3">
                <button
                  onClick={() => setShowDeleteConfirm(null)}
                  className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  onClick={() => handleDelete(showDeleteConfirm)}
                  className="flex-1 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
