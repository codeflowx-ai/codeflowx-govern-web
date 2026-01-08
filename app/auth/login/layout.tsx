export default function AuthLoginLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="es">
      <head>
        <meta httpEquiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
        <meta httpEquiv="Pragma" content="no-cache" />
        <meta httpEquiv="Expires" content="0" />
        <meta httpEquiv="Last-Modified" content={new Date().toUTCString()} />
        <title>Login - CodeflowX AI OS</title>
      </head>
      <body>
        {children}
      </body>
    </html>
  )
}
