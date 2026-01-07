# 📘 GUIA FUNCIONAL - TELEMETRIA E ANÁLISE

**Versão:** 1.0
**Data:** Dezembro 2025
**Audiência:** Usuários finais, DevOps, Analistas de Dados, Gerentes de Projeto

---

## 🎯 O QUE É TELEMETRIA E ANÁLISE?

Telemetria e Análise é um sistema de **monitoramento e análise de eventos** gerados pelos componentes de AI OS (Artificial Intelligence Operating System). Seu objetivo é fornecer visibilidade completa sobre o comportamento, desempenho e uso dos sistemas de IA em produção.

### 🎯 Propósito Principal

O sistema de Telemetria permite:

1. **Monitoramento em Tempo Real:** Visualização de eventos e métricas dos componentes de IA
2. **Análise de Desempenho:** KPIs agregados sobre latência, custos, tokens e eventos
3. **Busca Avançada:** Busca de eventos por múltiplos critérios (conteúdo, componente, projeto, datas)
4. **Análise de Componentes:** Estatísticas detalhadas por componente de IA
5. **Análise Temporal:** Visualização de métricas mensais e tendências
6. **Governança:** Detecção de viés, toxicidade, PII e segredos em eventos

---

## 🚀 PARA QUE SERVE A TELEMETRIA?

### 1. **Para Monitoramento Operacional**

- **Visibilidade Completa:** Ver todos os eventos gerados pelos componentes de IA
- **KPIs Agregados:** Métricas consolidadas (total de eventos, custos, tokens, latência média)
- **Análise de Componentes:** Comparar desempenho entre diferentes componentes
- **Detecção de Problemas:** Identificar componentes com alta latência ou custos elevados

### 2. **Para Otimização de Custos**

- **Análise de Custos:** Visualizar custos totais e por componente
- **Uso de Tokens:** Monitorar consumo de tokens por componente
- **Identificação de Oportunidades:** Detectar componentes com custos incomumente altos
- **Tendências:** Analisar evolução de custos ao longo do tempo

### 3. **Para Análise de Desempenho**

- **Latência:** Monitorar tempos de resposta dos componentes
- **Throughput:** Analisar volume de eventos processados
- **Comparação:** Comparar desempenho entre componentes
- **Tendências:** Identificar degradação de desempenho ao longo do tempo

### 4. **Para Governança e Conformidade**

- **Detecção de Viés:** Identificar eventos com vieses detectados
- **Toxicidade:** Detectar conteúdo tóxico em eventos
- **PII (Informações de Identificação Pessoal):** Identificar dados pessoais em eventos
- **Segredos:** Detectar segredos ou credenciais expostas em eventos
- **Categorização Automática:** O sistema calcula automaticamente:
  - **Status de Conformidade (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Nível de Risco (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
  - **Categoria de Conformidade (complianceCategory):** GDPR, SECURITY, LEGAL
  - **Tags de Problemas (issueTags):** bias_critical, pii_exposure, secret_leak, toxicity_high, etc.

### 5. **Para Busca e Auditoria**

- **Busca por Conteúdo:** Buscar eventos contendo texto específico no payload
- **Busca por Componente:** Filtrar eventos por componente específico
- **Busca por Projeto:** Ver eventos de todos os componentes de um projeto
- **Filtros Temporais:** Analisar eventos em intervalos de datas específicos

---

## 📊 COMPONENTES PRINCIPAIS DA TELEMETRIA

### 1. **Dashboard de Telemetria**

Visão centralizada que mostra:
- **KPIs Principais:** Total de eventos, custos totais, tokens totais, latência média, número de componentes
- **Filtros de Data:** Selecionar intervalo de datas para análise
- **Estatísticas por Componente:** Tabela com métricas agregadas por componente
- **Gráficos:** Visualização de tendências e distribuições
- **Navegação:** Acesso rápido a busca e análise mensal

### 2. **Busca de Eventos**

Sistema completo para buscar e filtrar eventos:
- **Busca por Conteúdo:** Buscar texto no payload e métricas
- **Filtros Múltiplos:** Por UUID de componente, agente, tipo de evento, severidade
- **Filtros Temporais:** Intervalo de datas de início e fim
- **Filtros de Governança:** Viés, toxicidade, PII, segredos detectados
- **Filtros de Conformidade/Segurança:**
  - **Status de Conformidade:** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Nível de Risco:** LOW, MEDIUM, HIGH, CRITICAL
  - **Categoria de Conformidade:** GDPR, SECURITY, LEGAL
  - **Tags de Problemas:** Filtrar por tags específicos (bias_critical, pii_exposure, etc.)
- **Paginação:** Navegação por páginas de resultados
- **Visualização Detalhada:** Página de detalhe completa para cada evento com payload completo, métricas e análise

### 3. **Análise Mensal**

Dashboard de análise temporal:
- **Métricas Mensais:** Total de eventos, custos, tokens, latência média por mês
- **Gráficos de Tendências:** Visualização da evolução mensal
- **Seletor de Ano:** Analisar diferentes anos
- **Comparação:** Comparar métricas entre meses

### 4. **Estatísticas por Componente**

Análise detalhada por componente:
- **Métricas Agregadas:** Total de eventos, latência média, tokens totais, custos totais
- **Comparação:** Ver todos os componentes em uma tabela
- **Filtros Temporais:** Analisar componentes em intervalos de datas específicos

---

## 🔄 FLUXO DE TRABALHO TÍPICO

### Cenário 1: Monitoramento Diário

1. **Acesso ao Dashboard**
   - Navegar para Governança → Telemetria → Dashboard
   - Ver KPIs principais do período (últimos 30 dias por padrão)

2. **Análise de Componentes**
   - Revisar tabela de estatísticas por componente
   - Identificar componentes com alta latência ou custos elevados
   - Comparar desempenho entre componentes

3. **Análise de Tendências**
   - Revisar gráficos de distribuição
   - Identificar padrões ou anomalias

### Cenário 2: Investigação de Problemas

1. **Identificação do Problema**
   - Detectar componente com métricas anômalas no dashboard
   - Ou receber alerta sobre evento específico

2. **Busca de Eventos**
   - Ir para a página de Busca
   - Filtrar por UUID de componente ou projeto
   - Aplicar filtros de data para o período do problema
   - Buscar por conteúdo específico se conhecido

3. **Análise de Eventos**
   - Revisar eventos encontrados
   - Navegar para a página de detalhe de eventos relevantes
   - Ver payload completo, métricas e resultados da análise
   - Revisar campos de conformidade/segurança calculados automaticamente:
     - Status de conformidade e nível de risco
     - Categoria de conformidade e tags de problemas
   - Verificar detecções de governança (viés, toxicidade, PII, segredos)

4. **Tomada de Decisão**
   - Identificar causa raiz do problema
   - Determinar ações corretivas necessárias

### Cenário 3: Análise de Custos

1. **Análise de Custos Totais**
   - Ir para o Dashboard
   - Ver KPI de custos totais
   - Ajustar intervalo de datas se necessário

2. **Análise por Componente**
   - Revisar tabela de estatísticas por componente
   - Identificar componentes com maior custo
   - Analisar relação custo/tokens/eventos

3. **Análise Temporal**
   - Ir para Análise Mensal
   - Ver evolução de custos mês a mês
   - Identificar tendências e picos

4. **Otimização**
   - Identificar oportunidades de otimização
   - Comparar custos entre componentes semelhantes
   - Planejar ações de redução de custos

### Cenário 4: Auditoria de Governança e Conformidade

1. **Busca de Eventos com Problemas**
   - Ir para a página de Busca
   - Usar filtros de conformidade/segurança:
     - **Status de Conformidade:** VIOLATION ou CRITICAL_VIOLATION
     - **Nível de Risco:** HIGH ou CRITICAL
     - **Categoria:** GDPR, SECURITY ou LEGAL conforme necessário
     - **Tags:** Filtrar por tags específicos (pii_exposure, secret_leak, bias_critical, etc.)
   - Ou usar filtros de governança tradicionais:
     - Viés detectado
     - Toxicidade detectada
     - PII detectado
     - Segredos detectados

2. **Análise de Eventos**
   - Revisar eventos que acionaram detecções
   - Navegar para a página de detalhe de cada evento
   - Ver payload completo, métricas e resultados da análise
   - Revisar campos calculados automaticamente (complianceStatus, riskLevel, complianceCategory, issueTags)

3. **Documentação**
   - Exportar ou documentar eventos relevantes
   - Gerar relatórios se necessário
   - Usar campos de conformidade/segurança para classificação e priorização

---

## 📈 MÉTRICAS E KPIS

### KPIs Principais

1. **Total de Eventos**
   - Número total de eventos de telemetria no período
   - Indicador de volume de atividade

2. **Custo Total (USD)**
   - Soma dos custos de todos os eventos
   - Calculado a partir de métricas de custo por evento

3. **Tokens Totais**
   - Soma de tokens consumidos em todos os eventos
   - Indicador de uso de modelos de IA

4. **Latência Média (ms)**
   - Tempo médio de resposta dos componentes
   - Indicador de desempenho

5. **Número de Componentes**
   - Quantidade de componentes únicos que geraram eventos
   - Indicador de cobertura

### Métricas por Componente

Para cada componente são calculados:
- **Total de Eventos:** Número de eventos gerados
- **Latência Média:** Tempo médio de resposta
- **Tokens Totais:** Soma de tokens consumidos
- **Custo Total:** Soma dos custos em USD

---

## 🔍 CAPACIDADES DE BUSCA

### Critérios de Busca Disponíveis

1. **Busca por Conteúdo**
   - Busca texto no payload JSONB e métricas
   - Útil para encontrar eventos específicos

2. **Filtro por UUID de Componente**
   - Filtrar eventos de um componente específico
   - Identificar componente por UUID

3. **Filtro por Agente**
   - Filtrar por `agentExternalId`
   - Útil para rastrear eventos de agentes específicos

4. **Filtro por Tipo de Evento**
   - Filtrar por `eventType` (ex: INTERACTION_COMPLETED, ERROR, etc.)

5. **Filtro por Severidade**
   - INFO, WARN, ERROR, DEBUG
   - Útil para encontrar eventos de erro

6. **Filtros Temporais**
   - Data de início
   - Data de fim
   - Permite análise em intervalos específicos

7. **Filtros de Governança**
   - **Viés Verificado:** Eventos onde foi verificado viés
   - **Toxicidade Verificada:** Eventos onde foi verificada toxicidade
   - **PII Detectado:** Eventos com dados pessoais detectados
   - **Segredo Detectado:** Eventos com segredos detectados

8. **Filtros de Conformidade/Segurança**
   - **Status de Conformidade (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
   - **Nível de Risco (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
   - **Categoria de Conformidade (complianceCategory):** GDPR, SECURITY, LEGAL
   - **Tag de Problema (issueTag):** bias_critical, bias_high, bias_medium, pii_exposure, secret_leak, toxicity_critical, toxicity_high, toxicity_medium, compliance_issue

### Combinação de Filtros

Todos os filtros podem ser combinados para buscas precisas:
- Exemplo: Eventos de um componente específico em um intervalo de datas com PII detectado
- Exemplo: Eventos de erro de um projeto no último mês

---

## 📊 VISUALIZAÇÕES

### Dashboard Principal

1. **Cards de KPIs**
   - 5 cards com métricas principais
   - Ícones e cores para identificação rápida

2. **Gráfico de Distribuição de Eventos**
   - Gráfico de barras mostrando distribuição de eventos por componente
   - Permite identificar componentes mais ativos

3. **Gráfico de Custos por Componente**
   - Gráfico de barras mostrando custos por componente
   - Útil para análise de custos

4. **Tabela de Estatísticas por Componente**
   - Tabela completa com todas as métricas por componente
   - Ordenável e filtrável

### Análise Mensal

1. **Gráfico de Eventos Mensais**
   - Timeline mostrando total de eventos por mês
   - Permite ver tendências

2. **Gráfico de Custos Mensais**
   - Timeline mostrando custos totais por mês
   - Útil para análise financeira

3. **Gráfico de Tokens Mensais**
   - Timeline mostrando tokens totais por mês
   - Indicador de uso

4. **Gráfico de Latência Média Mensal**
   - Timeline mostrando latência média por mês
   - Permite detectar degradação de desempenho

---

## 🎯 CASOS DE USO COMUNS

### Caso 1: "Quanto custa meu sistema de IA este mês?"

1. Ir para Dashboard de Telemetria
2. Ajustar filtros de data ao mês atual
3. Ver KPI "Custo Total (USD)"

### Caso 2: "Qual componente tem a maior latência?"

1. Ir para Dashboard de Telemetria
2. Revisar tabela de estatísticas por componente
3. Ordenar por latência média (decrescente)
4. Identificar componente com maior latência

### Caso 3: "Buscar todos os erros do último dia"

1. Ir para Busca de Eventos
2. Selecionar severidade "ERROR"
3. Estabelecer data de início: ontem
4. Estabelecer data de fim: hoje
5. Clicar em "Buscar"

### Caso 4: "Analisar tendências do último ano"

1. Ir para Análise Mensal
2. Selecionar ano atual
3. Revisar gráficos de tendências
4. Comparar meses

### Caso 5: "Encontrar eventos com dados pessoais"

1. Ir para Busca de Eventos
2. Usar filtro "Categoria de Conformidade" = "GDPR" ou ativar checkbox "PII"
3. Estabelecer intervalo de datas
4. Clicar em "Buscar"
5. Revisar eventos encontrados
6. Navegar para a página de detalhe de eventos relevantes para ver informações completas

### Caso 6: "Identificar eventos críticos de segurança"

1. Ir para Busca de Eventos
2. Estabelecer filtros:
   - **Status de Conformidade:** CRITICAL_VIOLATION
   - **Nível de Risco:** CRITICAL
   - **Categoria de Conformidade:** SECURITY
3. Estabelecer intervalo de datas
4. Clicar em "Buscar"
5. Revisar eventos encontrados
6. Navegar para a página de detalhe para análise completa

---

## 🔐 CONSIDERAÇÕES DE SEGURANÇA E PRIVACIDADE

### Dados Sensíveis

- Os eventos podem conter informações sensíveis nos payloads
- O sistema detecta automaticamente PII e segredos
- É recomendado revisar eventos com detecções antes de compartilhar

### Acesso aos Dados

- Apenas usuários autorizados podem acessar telemetria
- Os dados estão protegidos por autenticação e autorização
- É recomendado limitar o acesso a dados sensíveis

---

## 📚 REFERÊNCIAS

### Telas Relacionadas

- **Dashboard:** `/governance/telemetry/dashboard`
- **Busca:** `/governance/telemetry/search`
- **Análise Mensal:** `/governance/telemetry/analytics`
- **Detalhe de Evento:** `/governance/telemetry/events/{id}` (página de detalhe completa)

### Documentação Técnica

- Ver guias de desenvolvedores para detalhes técnicos
- Ver guia de uso de telas para instruções passo a passo

---

**Última Atualização:** Dezembro 2025

**Mudanças Recentes:**
- Adicionados campos de conformidade/segurança calculados automaticamente (complianceStatus, riskLevel, complianceCategory, issueTags)
- Novos filtros de busca por conformidade/segurança
- Página de detalhe separada para visualização completa de eventos
- Melhorias em dados mock com variações realistas por mês e ano
