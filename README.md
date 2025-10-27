### Opción 1: CSS + Prism.js (Recomendado)
1. Incluir Prism.js en tu página ZUL

´´´
<?page title="Chat" contentType="text/html;charset=UTF-8"?>
<zk>
    <style>
        /* Incluir CSS de Prism */
        @import url("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism.min.css");
        
        /* Estilos personalizados para el chat */
        .code-block {
            background: #f5f5f5;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin: 10px 0;
            overflow: auto;
        }
        
        .code-block pre {
            margin: 0;
            padding: 15px;
        }
    </style>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js"></script>
</zk>

´´´
### 2. Componente Java para formatear código

public class CodeFormatter {
    
    public static String formatCodeBlock(String code, String language) {
        return String.format(
            "<div class=\"code-block\">" +
            "<pre><code class=\"language-%s\">%s</code></pre>" +
            "</div>", 
            language.toLowerCase(), 
            escapeHtml(code)
        );
    }
    
    public static String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}

### 3. Uso en el controlador ZKoss

@Component
public class ChatController extends SelectorComposer<Component> {
    
    @Wire
    private Div chatContainer;
    
    public void addCodeMessage(String code, String language) {
        String formattedCode = CodeFormatter.formatCodeBlock(code, language);
        
        Html codeHtml = new Html();
        codeHtml.setContent(formattedCode);
        codeHtml.setParent(chatContainer);
        
        // Ejecutar Prism.js para highlighting
        Clients.evalJavaScript("Prism.highlightAll();");
    }
    
    public void addMessage(String message) {
        // Detectar bloques de código usando regex
        String processedMessage = processCodeBlocks(message);
        
        Html messageHtml = new Html();
        messageHtml.setContent(processedMessage);
        messageHtml.setParent(chatContainer);
        
        Clients.evalJavaScript("Prism.highlightAll();");
    }
    
    private String processCodeBlocks(String message) {
        // Buscar patrones ```language\ncode\n```
        Pattern pattern = Pattern.compile("```(\\w+)\\n([\\s\\S]*?)```");
        Matcher matcher = pattern.matcher(message);
        
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String language = matcher.group(1);
            String code = matcher.group(2);
            String replacement = CodeFormatter.formatCodeBlock(code, language);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
}
