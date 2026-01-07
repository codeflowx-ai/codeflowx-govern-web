// lib/xml-generators/page-xml-generator.ts
export class PageXMLGenerator {
    static generateUniversalXML(pageStructure: any): string {
        return `<?xml version="1.0" encoding="UTF-8"?>
<Page>
  <Metadata>
    <Name>${pageStructure.name}</Name>
    <Type>${pageStructure.type}</Type>
    <Created>${new Date().toISOString()}</Created>
  </Metadata>
  <Layout>
    <Container>
      ${this.generateComponentsXML(pageStructure.components)}
    </Container>
  </Layout>
  <Styles>
    ${this.generateStylesXML(pageStructure.styles)}
  </Styles>
  <Logic>
    ${this.generateLogicXML(pageStructure.logic)}
  </Logic>
</Page>`;
    }

    static generateReactCode(xml: string): string {
        // Convertir XML a código React
        return `
import React from 'react';

export default function GeneratedPage() {
  return (
    <div className="container mx-auto p-4">
      {/* Código generado desde XML */}
    </div>
  );
}`;
    }

    static generateAngularCode(xml: string): string {
        // Convertir XML a código Angular
        return `
import { Component } from '@angular/core';

@Component({
  selector: 'app-generated-page',
  template: \`
    <div class="container">
      <!-- Código generado desde XML -->
    </div>
  \`
})
export class GeneratedPageComponent {}`;
    }

    private static generateComponentsXML(components: any[]): string {
        return components.map(comp => `
      <Component type="${comp.type}" id="${comp.id}">
        <Props>${JSON.stringify(comp.props)}</Props>
        <Children>
          ${comp.children ? this.generateComponentsXML(comp.children) : ''}
        </Children>
      </Component>
    `).join('\n');
    }

    private static generateStylesXML(styles: any): string {
        return Object.entries(styles || {}).map(([selector, rules]) => `
      <Style selector="${selector}">
        ${JSON.stringify(rules)}
      </Style>
    `).join('\n');
    }

    private static generateLogicXML(logic: any): string {
        return `
      <Events>
        ${(logic?.events || []).map((event: any) => `
          <Event type="${event.type}" handler="${event.handler}" />
        `).join('\n')}
      </Events>
      <State>
        ${JSON.stringify(logic?.state || {})}
      </State>
    `;
    }
}
