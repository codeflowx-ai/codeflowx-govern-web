"use client";

import { authenticateUser, demoUsers } from "@/app/config/demo-users";
import { brandingConfig } from "@/app/config/branding";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  AlertCircle,
  Eye,
  EyeOff,
  FingerprintIcon,
  LockIcon,
  Mail,
  RefreshCw,
  Users,
  Zap,
} from "lucide-react";
import Image from "next/image";
import { useState } from "react";

export default function LekaFederationLogin() {
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [showDemoUsers, setShowDemoUsers] = useState(false);
  const [error, setError] = useState("");
  const [selectedRole, setSelectedRole] = useState<string>("all");
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  // Filtrar usuarios por rol
  const filteredUsers =
    selectedRole === "all"
      ? demoUsers
      : demoUsers.filter((user) => user.roles.includes(selectedRole));

  // Obtener roles únicos para el filtro
  const uniqueRoles = Array.from(
    new Set(demoUsers.flatMap((user) => user.roles))
  ).sort();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    // Simular delay de autenticación
    setTimeout(() => {
      const user = authenticateUser(formData.email, formData.password);

      if (user) {
        // Guardar datos del usuario en localStorage
        localStorage.setItem(
          "user",
          JSON.stringify({
            id: user.id,
            name: user.name,
            email: user.email,
            roles: user.roles,
            department: user.department,
          })
        );
        localStorage.setItem("isAuthenticated", "true");

        // Redirigir al dashboard
        window.location.href = "/dashboard";
      } else {
        setError(
          "Credenciales inválidas. Por favor, verifica tu email y contraseña."
        );
        setIsLoading(false);
      }
    }, 1000);
  };

  const handleDemoLogin = (email: string, password: string) => {
    setFormData({ email, password });
    setShowDemoUsers(false);
  };

  const handleHardRefresh = () => {
    window.location.reload();
  };

  return (
    <>
      <script
        defer
        src="https://analytics.codeflowx.cloud/script.js"
        data-website-id="edd21c8e-c20f-4bfe-b7f4-b5a7533fb722"
      ></script>
      <div className="min-h-screen relative overflow-hidden bg-gradient-to-br from-slate-900 via-blue-900 to-slate-900">

      {/* Partículas flotantes de fondo */}
      <div className="absolute inset-0 pointer-events-none">
        {/* Estrellas distantes */}
        <div
          className="absolute top-20 left-10 w-1 h-1 bg-blue-400/60 rounded-full animate-pulse"
          style={{ animationDelay: "0s" }}
        />
        <div
          className="absolute top-40 right-20 w-1.5 h-1.5 bg-blue-500/40 rounded-full animate-pulse"
          style={{ animationDelay: "1s" }}
        />
        <div
          className="absolute top-60 left-1/4 w-1 h-1 bg-blue-300/50 rounded-full animate-pulse"
          style={{ animationDelay: "2s" }}
        />
        <div
          className="absolute top-80 right-1/3 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse"
          style={{ animationDelay: "3s" }}
        />
        <div
          className="absolute top-32 left-1/2 w-1 h-1 bg-blue-500/40 rounded-full animate-pulse"
          style={{ animationDelay: "4s" }}
        />
        <div
          className="absolute top-96 left-1/3 w-1.5 h-1.5 bg-blue-600/50 rounded-full animate-pulse"
          style={{ animationDelay: "5s" }}
        />

        {/* Nebulosas flotantes */}
        <div className="absolute top-1/4 left-1/6 w-32 h-32 bg-gradient-radial from-blue-500/10 to-transparent rounded-full animate-pulse-slow" />
        <div
          className="absolute top-3/4 right-1/6 w-24 h-24 bg-gradient-radial from-blue-600/10 to-transparent rounded-full animate-pulse-slow"
          style={{ animationDelay: "2s" }}
        />
        <div
          className="absolute top-1/2 left-3/4 w-20 h-20 bg-gradient-radial from-blue-400/10 to-transparent rounded-full animate-pulse-slow"
          style={{ animationDelay: "4s" }}
        />

        {/* Cometas */}
        <div
          className="absolute top-1/3 left-0 w-2 h-2 bg-gradient-to-r from-blue-400 to-transparent rounded-full animate-bounce"
          style={{ animationDelay: "1s" }}
        />
        <div
          className="absolute top-2/3 right-0 w-1.5 h-1.5 bg-gradient-to-l from-blue-500 to-transparent rounded-full animate-bounce"
          style={{ animationDelay: "3s" }}
        />
      </div>

      {/* Grid de fondo futurista */}
      <div className="absolute inset-0 bg-[linear-gradient(rgba(59,130,246,0.1)_1px,transparent_1px),linear-gradient(90deg,rgba(59,130,246,0.1)_1px,transparent_1px)] bg-[size:50px_50px] opacity-20" />

      {/* Contenido principal - Pantalla dividida en dos áreas */}
      <div className="relative z-10 min-h-screen flex">
        {/* Área Izquierda - Mensaje de Información */}
        <div className="w-1/2 flex items-center justify-center px-8">
          <div className="text-center max-w-md">
            <div className="mb-8">
              <div className="w-24 h-24 bg-gradient-to-br from-blue-600 via-blue-500 to-blue-700 rounded-3xl flex items-center justify-center mx-auto mb-6 shadow-2xl shadow-blue-500/25">
                <Image
                  src={brandingConfig.logoPath}
                  alt={brandingConfig.logoAlt}
                  width={80}
                  height={80}
                  className="w-20 h-20 object-contain"
                />
              </div>
              <h1 className="text-4xl font-bold text-white mb-4">
                {brandingConfig.useGradientName ? (
                  <span className="bg-gradient-to-r from-blue-600 via-blue-500 to-blue-600 bg-clip-text text-transparent">
                    {brandingConfig.companyName}
                  </span>
                ) : (
                  <span>{brandingConfig.companyName}</span>
                )}
              </h1>
              <p className="text-xl text-blue-200/80 mb-6">
                Plataforma de Inteligencia Artificial
              </p>
            </div>

            {/* Aquí puedes poner tu mensaje de información importante */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20">
              <h2 className="text-xl font-semibold text-white mb-4">
                🚀 Demo de CodeflowX
              </h2>
              <div className="space-y-4 text-sm text-blue-200/80 leading-relaxed">
                <p>
                  <strong className="text-white">Bienvenido a la demo</strong>{" "}
                  de nuestra plataforma de Inteligencia Artificial. Esta es una
                  versión con{" "}
                  <strong className="text-blue-300">datos sintéticos</strong>{" "}
                  que te permitirá explorar todas las funcionalidades sin
                  conexión a la plataforma real.
                </p>

                <div className="space-y-3">
                  <div className="flex items-start gap-2">
                    <div className="w-2 h-2 bg-blue-400 rounded-full mt-2 flex-shrink-0"></div>
                    <p>
                      <strong className="text-white">
                        Explora cada módulo
                      </strong>{" "}
                      para ver la versión disponible, estado actual y roadmap de
                      los próximos 6-12 meses.
                    </p>
                  </div>

                  <div className="flex items-start gap-2">
                    <div className="w-2 h-2 bg-blue-400 rounded-full mt-2 flex-shrink-0"></div>
                    <p>
                      <strong className="text-white">
                        Perfiles demo disponibles:
                      </strong>{" "}
                      Analista, DevOps, Arquitecto, Desarrollador y más roles
                      especializados.
                    </p>
                  </div>
                </div>

                <div className="bg-blue-600/20 border border-blue-500/30 rounded-lg p-4 mt-4">
                  <h3 className="text-white font-semibold mb-2">
                    💎 Únete al Ecosistema CodeflowX
                  </h3>
                  <p className="text-blue-200/80 mb-3">
                    Sé parte de nuestra comunidad como{" "}
                    <strong className="text-white">Early Adopter</strong>,
                    <strong className="text-white"> Startup</strong> o{" "}
                    <strong className="text-white">Partner</strong>.
                  </p>
                  <a
                    href="https://codeflowx.com"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="inline-flex items-center gap-2 bg-gradient-to-r from-blue-500 to-blue-600 hover:from-blue-600 hover:to-blue-700 text-white px-4 py-2 rounded-lg font-medium transition-all duration-300 hover:scale-105 shadow-lg hover:shadow-xl"
                  >
                    <span>Visitar CodeflowX.com</span>
                    <svg
                      className="w-4 h-4"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"
                      />
                    </svg>
                  </a>
                  <p className="text-xs text-blue-300/70 mt-2">
                    Te avisaremos cuando la plataforma esté lista para
                    producción
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Área Derecha - Formulario de Login */}
        <div className="w-1/2 flex items-center justify-center px-8">
          <div className="w-full max-w-md">
            {/* Header del formulario */}
            <div className="text-center mb-8">
              <h2 className="text-2xl font-bold text-white mb-2">
                Acceso a la Plataforma
              </h2>
              <p className="text-blue-200/80">
                Ingresa tus credenciales para continuar
              </p>
            </div>

            {/* Card de login */}
            <Card className="backdrop-blur-xl bg-white/5 border border-white/20 shadow-2xl shadow-blue-500/25 overflow-hidden group hover:bg-white/10 transition-all duration-700">
              {/* Efecto de brillo en hover */}
              <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-700 transform -skew-x-12 translate-x-[-100%] group-hover:translate-x-[100%]" />

              {/* Borde brillante */}
              <div
                className="absolute inset-0 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity duration-500"
                style={{
                  background:
                    "linear-gradient(45deg, rgba(59,130,246,0.3), transparent, rgba(59,130,246,0.3))",
                  backgroundSize: "200% 200%",
                  animation: "shimmer 2s ease-in-out infinite",
                }}
              />

              <CardHeader className="text-center relative z-10 pb-6">
                <div className="flex items-center justify-center gap-2 mb-4">
                  <div className="w-8 h-8 bg-gradient-to-br from-blue-500 to-blue-600 rounded-lg flex items-center justify-center">
                    <FingerprintIcon className="w-4 h-4 text-white" />
                  </div>
                  <CardTitle className="text-xl text-white">
                    Autenticación
                  </CardTitle>
                </div>
                <CardDescription className="text-blue-200/80 text-sm">
                  Ingresa tu email y contraseña
                </CardDescription>

                {/* Botón de hard refresh para desarrollo */}
                <button
                  onClick={handleHardRefresh}
                  className="mt-4 text-xs text-blue-400 hover:text-blue-300 underline flex items-center justify-center mx-auto transition-colors duration-300"
                >
                  <RefreshCw className="w-3 h-3 mr-1" />
                  Hard Refresh (DEV)
                </button>
              </CardHeader>

              <CardContent className="relative z-10 space-y-6">
                {/* Formulario */}
                <form onSubmit={handleSubmit} className="space-y-4">
                  {/* Campo Email */}
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-blue-200/90 flex items-center gap-2">
                      <Mail className="w-4 h-4" />
                      Email
                    </label>
                    <div className="relative">
                      <Input
                        type="email"
                        placeholder="Ingresa tu email"
                        value={formData.email}
                        onChange={(e) =>
                          setFormData({ ...formData, email: e.target.value })
                        }
                        className="backdrop-blur-sm bg-white/10 border-white/20 text-white placeholder:text-blue-200/50 focus:border-blue-400/50 focus:ring-blue-400/20 transition-all duration-300"
                        required
                      />
                      <div className="absolute inset-y-0 right-0 w-10 flex items-center justify-center">
                        <div className="w-2 h-2 bg-blue-400 rounded-full animate-pulse" />
                      </div>
                    </div>
                  </div>

                  {/* Campo Password */}
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-blue-200/90 flex items-center gap-2">
                      <LockIcon className="w-4 h-4" />
                      Contraseña
                    </label>
                    <div className="relative">
                      <Input
                        type={showPassword ? "text" : "password"}
                        placeholder="Ingresa tu contraseña"
                        value={formData.password}
                        onChange={(e) =>
                          setFormData({ ...formData, password: e.target.value })
                        }
                        className="backdrop-blur-sm bg-white/10 border-white/20 text-white placeholder:text-blue-200/50 focus:border-blue-400/50 focus:ring-blue-400/20 transition-all duration-300 pr-12"
                        required
                      />
                      <button
                        type="button"
                        onClick={() => setShowPassword(!showPassword)}
                        className="absolute inset-y-0 right-0 w-10 flex items-center justify-center text-blue-200/70 hover:text-blue-200 transition-colors duration-300"
                      >
                        {showPassword ? (
                          <EyeOff className="w-4 h-4" />
                        ) : (
                          <Eye className="w-4 h-4" />
                        )}
                      </button>
                    </div>
                  </div>

                  {/* Botón de login */}
                  <Button
                    type="submit"
                    disabled={isLoading}
                    className="w-full bg-gradient-to-r from-blue-600 via-blue-500 to-blue-700 hover:from-blue-700 hover:via-blue-600 hover:to-blue-800 text-white font-semibold py-3 rounded-xl shadow-2xl shadow-blue-500/25 hover:shadow-blue-500/40 transition-all duration-500 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    {isLoading ? (
                      <div className="flex items-center gap-2">
                        <div className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                        <span>Autenticando...</span>
                      </div>
                    ) : (
                      <div className="flex items-center gap-2">
                        <Zap className="w-4 h-4" />
                        <span>Acceder</span>
                      </div>
                    )}
                  </Button>
                </form>

                {/* Mensaje de error */}
                {error && (
                  <div className="flex items-center gap-2 p-3 bg-red-500/10 border border-red-500/20 rounded-lg text-red-300 text-sm">
                    <AlertCircle className="w-4 h-4" />
                    {error}
                  </div>
                )}

                {/* Demo Users Section */}
                <div className="space-y-3">
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => setShowDemoUsers(!showDemoUsers)}
                    className="w-full border-white/20 text-blue-200 hover:bg-white/10 hover:border-white/30 transition-all duration-300"
                  >
                    <Users className="w-4 h-4 mr-2" />
                    {showDemoUsers ? "Ocultar" : "Mostrar"} Usuarios Demo
                  </Button>

                  {showDemoUsers && (
                    <div className="space-y-3">
                      {/* Filtro de roles */}
                      <div className="flex items-center gap-2">
                        <label className="text-xs text-blue-200/70">
                          Filtrar por rol:
                        </label>
                        <select
                          value={selectedRole}
                          onChange={(e) => setSelectedRole(e.target.value)}
                          className="text-xs bg-white/10 border border-white/20 text-blue-200 rounded px-2 py-1 focus:border-blue-400/50 focus:outline-none"
                        >
                          <option value="all">Todos los roles</option>
                          {uniqueRoles.map((role) => (
                            <option key={role} value={role}>
                              {role.replace("_", " ")}
                            </option>
                          ))}
                        </select>
                      </div>

                      {/* Lista de usuarios demo */}
                      <div className="space-y-2 max-h-48 overflow-y-auto">
                        {filteredUsers.map((user) => (
                          <div
                            key={user.id}
                            className="flex items-center justify-between p-2 bg-white/5 border border-white/10 rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer"
                            onClick={() =>
                              handleDemoLogin(user.email, user.password)
                            }
                          >
                            <div className="flex items-center gap-2">
                              <div className="w-6 h-6 bg-gradient-to-br from-blue-500 to-blue-600 rounded-full flex items-center justify-center">
                                <span className="text-xs text-white font-bold">
                                  {user.name.charAt(0).toUpperCase()}
                                </span>
                              </div>
                              <div>
                                <div className="text-sm text-white font-medium">
                                  {user.name}
                                </div>
                                <div className="text-xs text-blue-200/70">
                                  {user.email}
                                </div>
                              </div>
                            </div>
                            <div className="flex gap-1">
                              {user.roles.slice(0, 2).map((role, idx) => (
                                <Badge
                                  key={idx}
                                  variant="secondary"
                                  className="text-xs bg-blue-500/20 text-blue-200 border-blue-500/30"
                                >
                                  {role}
                                </Badge>
                              ))}
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>

            {/* Footer */}
            <div className="text-center mt-6 text-blue-300/40 text-xs">
              CodeflowX v2.0.1 • Plataforma de IA
            </div>
          </div>
        </div>
      </div>

      {/* Efectos de partículas adicionales */}
      <div className="absolute bottom-0 left-0 w-full h-32 bg-gradient-to-t from-blue-500/10 to-transparent" />
      <div className="absolute top-0 right-0 w-32 h-full bg-gradient-to-l from-blue-600/10 to-transparent" />
      </div>
    </>
  );
}
