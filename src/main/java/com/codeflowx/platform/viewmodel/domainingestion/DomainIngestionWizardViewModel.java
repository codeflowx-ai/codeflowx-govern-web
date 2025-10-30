package com.codeflowx.platform.viewmodel.domainingestion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.domainingestion.Domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class DomainIngestionWizardViewModel extends BaseFront<DomainIngestionWizardViewModel> {

	private static final long serialVersionUID = 1L;

	@Override
	public void setBeans(Object bean) {}

	// Steps control
	private int currentStepIndex = 0; // 0..5
	private List<WizardStep> stepsList = new ArrayList<>();

	// Template selection
	private List<DomainTemplate> templatesList = new ArrayList<>();
	private String selectedTemplateId;

	// Basic info
	private String domainName;
	private String domainDescription;
	private String industry;
	private String businessArea;
	private String domainIcon;
	private String domainColor;

	// Data sources
	private Set<String> selectedDataSources = new HashSet<>(); // database, api, file, web

	// Compliance
	private String regulatoryFramework;
	private String privacyLevel;
	private Integer dataRetentionYears = 1;
	private boolean complianceChecks = true;

	// Advanced features
	private boolean ragEnabled = false;
	private boolean trainingEnabled = false;
	private boolean autoIngestion = false;
	private Integer qualityThreshold = 70;
	private Integer retentionDays = 365;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);
		super.doAfterCompose(view);
		initSteps();
		initTemplates();
	}

	private void initSteps() {
		stepsList = Arrays.asList(
			new WizardStep(1, "Selección de Plantilla", "Elige una plantilla de inicio", false, false),
			new WizardStep(2, "Información Básica", "Define nombre e industria", false, true),
			new WizardStep(3, "Fuentes de Datos", "Selecciona tipos de fuentes", false, true),
			new WizardStep(4, "Cumplimiento", "Configura marcos regulatorios", false, true),
			new WizardStep(5, "Características Avanzadas", "Activa RAG/Training/Auto", false, false),
			new WizardStep(6, "Revisión", "Confirma y crea el dominio", false, true)
		);
	}

	private void initTemplates() {
		templatesList = Arrays.asList(
			new DomainTemplate("fintech", "Fintech Banking", "Banca digital y tradicional", "🏦", "financial", "medium", "~10m",
				Arrays.asList("RAG Ready", "Compliance Pack", "API + Web + Docs")),
			new DomainTemplate("healthcare", "Healthcare Analytics", "Analítica de salud", "🏥", "health", "advanced", "~20m",
				Arrays.asList("PII Aware", "HIPAA", "Docs + APIs")),
			new DomainTemplate("ecommerce", "E-commerce", "Retail y marketplaces", "🛒", "commerce", "simple", "~5m",
				Arrays.asList("Web + Docs", "GDPR", "RAG"))
		);
	}

	// Commands
	@Command
	@NotifyChange("*")
	public void selectTemplate(@BindingParam("template") DomainTemplate template) {
		if (template == null) return;
		selectedTemplateId = template.getId();
		// Autoconfiguración mínima
		if ("fintech".equals(template.getId())) {
			industry = "Financial Services";
			privacyLevel = "internal";
			regulatoryFramework = "GDPR";
			selectedDataSources.clear();
			selectedDataSources.addAll(Arrays.asList("api", "web", "file"));
		}
		if ("healthcare".equals(template.getId())) {
			industry = "Healthcare";
			privacyLevel = "confidential";
			regulatoryFramework = "HIPAA";
			selectedDataSources.clear();
			selectedDataSources.addAll(Arrays.asList("api", "file"));
		}
	}

	@Command
	@NotifyChange("*")
	public void toggleDataSource(@BindingParam("type") String type) {
		if (type == null) return;
		if (selectedDataSources.contains(type)) {
			selectedDataSources.remove(type);
		} else {
			selectedDataSources.add(type);
		}
	}

	@Command
	@NotifyChange("*")
	public void nextStep() {
		if (!validateStep(currentStepIndex)) return;
		if (currentStepIndex < 5) currentStepIndex++;
	}

	@Command
	@NotifyChange("*")
	public void previousStep() {
		if (currentStepIndex > 0) currentStepIndex--;
	}

	@Command
	@NotifyChange("*")
	public void cancelWizard() {
		Messagebox.show("¿Cancelar la creación del dominio?", "Cancelar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, evt -> {
			if (Messagebox.ON_OK.equals(evt.getName())) {
				clear();
			}
		});
	}

	@Command
	@NotifyChange("*")
	public void createDomain() {
		try {
			if (!validateAll()) return;

			Domain domain = new Domain();
			domain.setDindomainname(domainName);
			domain.setDindomaindescription(domainDescription);
			domain.setDinindustry(industry);
			domain.setDinbusinessarea(businessArea);
			domain.setDindomainicon(domainIcon);
			domain.setDindomaincolor(domainColor);
			domain.setDinstatus("draft");
			domain.setDinprogress(0);
			domain.setDintotaldocuments(0);
			domain.setDintotalwebpages(0);
			domain.setDintotalapis(0);
			domain.setDinregulatoryframework(regulatoryFramework);
			domain.setDinprivacylevel(privacyLevel);
			domain.setDindataretentionyears(dataRetentionYears);
			domain.setDincompliancechecks(complianceChecks);
			domain.setDinragenabled(ragEnabled);
			domain.setDintrainingenabled(trainingEnabled);
			domain.setDinautoingestion(autoIngestion);
			domain.setDinqualitythreshold(qualityThreshold);
			domain.setDinretentiondays(retentionDays);
			domain.setDinlastupdate(new Timestamp(System.currentTimeMillis()));

			businessService.save(domain);

			logActivity("CREAR", "DOMAINS", domain.getIdxdomain(), "Dominio creado: " + domain.getDindomainname());

			Messagebox.show("Dominio creado exitosamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
			clear();
		} catch (Exception e) {
			log.error("Error al crear dominio", e);
			Messagebox.show("Error al crear dominio: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}

	private boolean validateStep(int step) {
		// step mapping: 0 template, 1 basic, 2 sources, 3 compliance, 4 advanced, 5 review
		switch (step) {
			case 1:
				if (isEmpty(domainName) || isEmpty(industry) || isEmpty(businessArea)) {
					Messagebox.show("Complete los campos requeridos (nombre, industria, área)", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
					return false;
				}
				break;
			case 2:
				if (selectedDataSources.isEmpty()) {
					Messagebox.show("Seleccione al menos una fuente de datos", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
					return false;
				}
				break;
			default:
				break;
		}
		return true;
	}

	private boolean validateAll() {
		if (!validateStep(1)) return false;
		if (!validateStep(2)) return false;
		return true;
	}

	private void clear() {
		// Reset step
		currentStepIndex = 0;
		initSteps();
		initTemplates();

		// Reset basic info
		domainName = null;
		domainDescription = null;
		industry = null;
		businessArea = null;
		domainIcon = null;
		domainColor = null;

		// Reset data sources
		selectedDataSources = new java.util.HashSet<>();

		// Reset compliance
		regulatoryFramework = null;
		privacyLevel = null;
		dataRetentionYears = 1;
		complianceChecks = true;

		// Reset advanced features
		ragEnabled = false;
		trainingEnabled = false;
		autoIngestion = false;
		qualityThreshold = 70;
		retentionDays = 365;
	}

	private boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }

	public boolean isStepCompleted(WizardStep step) { return step != null && step.getOrder() - 1 < currentStepIndex; }
	public String getStepNumber(WizardStep step) { return String.valueOf(step.getOrder()); }
	public String getStepNumberClass(WizardStep step, int current) { return isStepCompleted(step) ? "step-number completed" : (step.getOrder()-1==current ? "step-number active" : "step-number"); }
	public boolean isDataSourceSelected(String type, Set<String> selected) { return selected != null && selected.contains(type); }
	public String getDataSourceCardClass(String type, Set<String> selected) { return isDataSourceSelected(type, selected) ? "border-success" : "border-light"; }
	public String getTemplateCardClass(DomainTemplate t, String selectedId) { return t != null && t.getId().equals(selectedId) ? "border-primary" : "border-light"; }
	public String getComplexityColor(String c) { if (c==null) return "badge bg-secondary"; switch(c){case "simple":return "badge bg-success";case "medium":return "badge bg-warning";case "advanced":return "badge bg-danger";default:return "badge bg-secondary";} }

	@Destroy
	public void destroy() {
		stepsList = null;
		templatesList = null;
		selectedDataSources = null;
		businessService = null;
	}

	// Inner DTOs (UI)
	@Getter @Setter
	public static class WizardStep {
		private int order; private String title; private String description; private boolean completed; private boolean last;
		public WizardStep(int order, String title, String description, boolean completed, boolean last){this.order=order;this.title=title;this.description=description;this.completed=completed;this.last=last;}
	}
	@Getter @Setter
	public static class DomainTemplate {
		private String id; private String name; private String description; private String icon; private String category; private String complexity; private String estimatedTime; private List<String> features;
		public DomainTemplate(String id,String name,String description,String icon,String category,String complexity,String estimatedTime,List<String>features){this.id=id;this.name=name;this.description=description;this.icon=icon;this.category=category;this.complexity=complexity;this.estimatedTime=estimatedTime;this.features=features;}
	}
}
