// app/config/demo-users/page.tsx
"use client";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import { SimpleModal } from "@/components/ui/SimpleModal";
import {
  ArrowLeft,
  Brain,
  CheckCircle,
  Crown,
  Edit,
  Settings,
  Shield,
  Trash2,
  User,
  Users,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";
import { demoUsers } from "../demo-users";

interface DemoUser {
  id: string;
  name: string;
  email: string;
  password: string;
  roles: string[];
  avatar?: string;
  department?: string;
  lastLogin?: string;
}

export default function DemoUsersPage() {
  const [searchQuery, setSearchQuery] = useState("");
  const [showEditModal, setShowEditModal] = useState(false);
  const [editingUser, setEditingUser] = useState<DemoUser | null>(null);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    roles: [] as string[],
    department: "",
  });

  const ALL_ROLES = [
    "admin",
    "developer",
    "viewer",
    "business_analytics",
    "ai_analytics",
    "ai_developer",
    "devops",
    "project_manager",
    "architect",
    "ai_architect",
  ];

  const getRoleColor = (role: string) => {
    switch (role) {
      case "admin":
        return "bg-red-100 text-red-700";
      case "developer":
        return "bg-blue-100 text-blue-700";
      case "viewer":
        return "bg-gray-100 text-gray-700";
      case "business_analytics":
        return "bg-purple-100 text-purple-700";
      case "ai_analytics":
        return "bg-indigo-100 text-indigo-700";
      case "ai_developer":
        return "bg-cyan-100 text-cyan-700";
      case "devops":
        return "bg-orange-100 text-orange-700";
      case "project_manager":
        return "bg-green-100 text-green-700";
      case "architect":
        return "bg-emerald-100 text-emerald-700";
      case "ai_architect":
        return "bg-pink-100 text-pink-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  const getRoleIcon = (role: string) => {
    switch (role) {
      case "admin":
        return <Crown className="w-4 h-4" />;
      case "developer":
        return <User className="w-4 h-4" />;
      case "viewer":
        return <User className="w-4 h-4" />;
      case "business_analytics":
        return <User className="w-4 h-4" />;
      case "ai_analytics":
        return <Brain className="w-4 h-4" />;
      case "ai_developer":
        return <Brain className="w-4 h-4" />;
      case "devops":
        return <User className="w-4 h-4" />;
      case "project_manager":
        return <User className="w-4 h-4" />;
      case "architect":
        return <Settings className="w-4 h-4" />;
      case "ai_architect":
        return <Brain className="w-4 h-4" />;
      default:
        return <User className="w-4 h-4" />;
    }
  };

  const filteredUsers = demoUsers.filter(
    (user) =>
      user.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
      user.roles.some((role) =>
        role.toLowerCase().includes(searchQuery.toLowerCase())
      )
  );

  const handleEdit = (user: DemoUser) => {
    setEditingUser(user);
    setFormData({
      name: user.name,
      email: user.email,
      password: user.password,
      roles: user.roles,
      department: user.department || "",
    });
    setShowEditModal(true);
  };

  const handleSave = () => {
    // TODO: Implement save logic
    console.log("Saving user:", formData);
    setShowEditModal(false);
    setEditingUser(null);
  };

  const toggleRole = (role: string) => {
    setFormData((prev) => ({
      ...prev,
      roles: prev.roles.includes(role)
        ? prev.roles.filter((r) => r !== role)
        : [...prev.roles, role],
    }));
  };

  return (
    <div className="min-h-screen bg-gray-50">
      
      {/* Header */}
      <header className="bg-white border-b border-gray-200">
        <div className="px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <Link href="/admin">
                <Button variant="ghost" size="sm">
                  <ArrowLeft className="w-4 h-4 mr-2" />
                  Back to Admin
                </Button>
              </Link>
              <div className="flex items-center space-x-2">
                <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center">
                  <Users className="w-5 h-5 text-white" />
                </div>
                <div className="space-y-2">
                  <h1 className="text-xl font-bold">
                    Demo Users Configuration
                  </h1>
                  <p className="text-sm text-gray-600">
                    Manage demo users for testing and development
                  </p>
                  {/* Banner de Desarrollo - Versión 1.0.0 Operativa */}
                  <DevelopmentBanner
                    type="operational"
                    customText="✅ Versión 1.0.0 - Operativa"
                    showEarlyAdopterButton={false}
                    className="justify-start"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="px-6 py-8">
        <div className="w-full">
          {/* Stats */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
            <Card>
              <CardBody>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-600">
                      Total Demo Users
                    </p>
                    <p className="text-2xl font-bold text-gray-900">
                      {demoUsers.length}
                    </p>
                  </div>
                  <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                    <Users className="w-6 h-6 text-blue-600" />
                  </div>
                </div>
              </CardBody>
            </Card>

            <Card>
              <CardBody>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-600">
                      Active Users
                    </p>
                    <p className="text-2xl font-bold text-green-600">
                      {
                        demoUsers.filter(
                          (u) => u.lastLogin && u.lastLogin !== "Never"
                        ).length
                      }
                    </p>
                  </div>
                  <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                    <CheckCircle className="w-6 h-6 text-green-600" />
                  </div>
                </div>
              </CardBody>
            </Card>

            <Card>
              <CardBody>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-600">
                      Unique Roles
                    </p>
                    <p className="text-2xl font-bold text-purple-600">
                      {new Set(demoUsers.flatMap((u) => u.roles)).size}
                    </p>
                  </div>
                  <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                    <Shield className="w-6 h-6 text-purple-600" />
                  </div>
                </div>
              </CardBody>
            </Card>

            <Card>
              <CardBody>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-600">
                      Departments
                    </p>
                    <p className="text-2xl font-bold text-orange-600">
                      {
                        new Set(
                          demoUsers.map((u) => u.department).filter(Boolean)
                        ).size
                      }
                    </p>
                  </div>
                  <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
                    <Settings className="w-6 h-6 text-orange-600" />
                  </div>
                </div>
              </CardBody>
            </Card>
          </div>

          {/* Search */}
          <Card className="mb-6">
            <CardBody>
              <Input
                placeholder="Search demo users..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="max-w-md"
              />
            </CardBody>
          </Card>

          {/* Users List */}
          <Card>
            <CardHeader>
              <CardTitle>Demo Users ({filteredUsers.length})</CardTitle>
            </CardHeader>
            <CardBody className="p-0">
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50 border-b border-gray-200">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        User
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Roles
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Department
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Last Login
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Actions
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {filteredUsers.map((user) => (
                      <tr key={user.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex items-center">
                            <div className="w-10 h-10 bg-primary-100 rounded-full flex items-center justify-center mr-3">
                              <span className="text-sm font-medium text-primary-600">
                                {user.name
                                  .split(" ")
                                  .map((n) => n[0])
                                  .join("")}
                              </span>
                            </div>
                            <div>
                              <div className="text-sm font-medium text-gray-900">
                                {user.name}
                              </div>
                              <div className="text-sm text-gray-500">
                                {user.email}
                              </div>
                            </div>
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex flex-wrap gap-1">
                            {user.roles.map((role) => (
                              <Badge
                                key={role}
                                className={`${getRoleColor(
                                  role
                                )} flex items-center space-x-1`}
                              >
                                {getRoleIcon(role)}
                                <span className="capitalize">
                                  {role.replace("_", " ")}
                                </span>
                              </Badge>
                            ))}
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {user.department || "-"}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {user.lastLogin || "Never"}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <div className="flex items-center space-x-2">
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => handleEdit(user)}
                            >
                              <Edit className="w-4 h-4" />
                            </Button>
                            <Button
                              variant="outline"
                              size="sm"
                              className="text-red-600 hover:text-red-700"
                            >
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </CardBody>
          </Card>
        </div>
      </main>

      {/* Edit Modal */}
      <SimpleModal
        isOpen={showEditModal}
        onClose={() => setShowEditModal(false)}
        title="Edit Demo User"
        maxWidth="max-w-md"
      >
        <div className="space-y-4">
          <Input
            label="Name"
            value={formData.name}
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, name: e.target.value }))
            }
          />

          <Input
            label="Email"
            type="email"
            value={formData.email}
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, email: e.target.value }))
            }
          />

          <Input
            label="Password"
            type="password"
            value={formData.password}
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, password: e.target.value }))
            }
          />

          <Input
            label="Department"
            value={formData.department}
            onChange={(e) =>
              setFormData((prev) => ({
                ...prev,
                department: e.target.value,
              }))
            }
          />

          <div className="space-y-2">
            <label className="block text-sm font-medium text-gray-700">
              Roles
            </label>
            <div className="grid grid-cols-2 gap-2">
              {ALL_ROLES.map((role) => (
                <label key={role} className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    checked={formData.roles.includes(role)}
                    onChange={() => toggleRole(role)}
                    className="rounded border-gray-300"
                  />
                  <span className="text-sm capitalize">
                    {role.replace("_", " ")}
                  </span>
                </label>
              ))}
            </div>
          </div>
        </div>

        <div className="flex space-x-3 mt-6">
          <Button
            variant="outline"
            className="flex-1"
            onClick={() => setShowEditModal(false)}
          >
            Cancel
          </Button>
          <Button className="flex-1" onClick={handleSave}>
            Save Changes
          </Button>
        </div>
      </SimpleModal>
    </div>
  );
}
