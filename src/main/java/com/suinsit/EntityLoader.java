
package com.suinsit;

import org.enartframework.suinsit.config.SchemaBD;

public class EntityLoader {
	SchemaBD schemaBD = new SchemaBD();

	public static void main(String[] args) {
		String[] entityClasses = {
				// Core Entities
				"com.suinsit.compliance.entity.Cmpaccess", "com.suinsit.compliance.entity.Cmpadapt",
				"com.suinsit.compliance.entity.Cmpai", "com.suinsit.compliance.entity.Cmpalert",
				"com.suinsit.compliance.entity.Cmpaudit", "com.suinsit.compliance.entity.Cmpautomation",
				"com.suinsit.compliance.entity.Cmpbias", "com.suinsit.compliance.entity.Cmpcert",
				"com.suinsit.compliance.entity.Cmpchange", "com.suinsit.compliance.entity.Cmpcomm",
				"com.suinsit.compliance.entity.Cmpconsent", "com.suinsit.compliance.entity.Cmpdata",
				"com.suinsit.compliance.entity.Cmpdataquality", "com.suinsit.compliance.entity.Cmpdecision",
				"com.suinsit.compliance.entity.Cmpdoc", "com.suinsit.compliance.entity.Cmpdoclegal",
				"com.suinsit.compliance.entity.Cmpeffect", "com.suinsit.compliance.entity.Cmpevent",
				"com.suinsit.compliance.entity.Cmpexception", "com.suinsit.compliance.entity.Cmpexplain",
				"com.suinsit.compliance.entity.Cmpflow", "com.suinsit.compliance.entity.Cmpforecast",
				"com.suinsit.compliance.entity.Cmpimpact", "com.suinsit.compliance.entity.Cmpimpactpred",
				"com.suinsit.compliance.entity.Cmpimprove", "com.suinsit.compliance.entity.Cmpincident",
				"com.suinsit.compliance.entity.Cmpinsight", "com.suinsit.compliance.entity.Cmpintegration",
				"com.suinsit.compliance.entity.Cmpkpi", "com.suinsit.compliance.entity.Cmplearn",
				"com.suinsit.compliance.entity.Cmpmetric", "com.suinsit.compliance.entity.Cmpml",
				"com.suinsit.compliance.entity.Cmpmonitor", "com.suinsit.compliance.entity.Cmpoptctrl",
				"com.suinsit.compliance.entity.Cmporch", "com.suinsit.compliance.entity.Cmppattern",
				"com.suinsit.compliance.entity.Cmpperformance", "com.suinsit.compliance.entity.Cmppolicy",
				"com.suinsit.compliance.entity.Cmpprocess", "com.suinsit.compliance.entity.Cmpquality",
				"com.suinsit.compliance.entity.Cmprecommend", "com.suinsit.compliance.entity.Cmpremedy",
				"com.suinsit.compliance.entity.Cmpreport", "com.suinsit.compliance.entity.Cmpreq",
				"com.suinsit.compliance.entity.Cmpright", "com.suinsit.compliance.entity.Cmprisk",
				"com.suinsit.compliance.entity.Cmpriskemerg", "com.suinsit.compliance.entity.Cmpscenario",
				"com.suinsit.compliance.entity.Cmpsim", "com.suinsit.compliance.entity.Cmptraining",
				"com.suinsit.compliance.entity.Cmptrend", "com.suinsit.compliance.entity.Cmpvalidation",
				"com.suinsit.compliance.entity.Cmpvendor", "com.suinsit.compliance.entity.Cmpvendoreval",
				"com.suinsit.compliance.entity.Cmpversion", "com.suinsit.compliance.entity.Cmpwhatif" };

		EntityLoader el = new EntityLoader();
		el.generate(entityClasses);
//		BuilderDataBase builder = new BuilderDataBase(
//				"C:\\Users\\ManuelGonzalez\\git\\suinsit-platform-projects\\suinlesbasic\\application-source\\data\\model");
//		Arrays.stream(entityClasses).forEach(className -> {
//			try {
//				Configuration configuration = new Configuration(new File(
//						"C:\\Users\\ManuelGonzalez\\git\\suinsit-platform-source\\architectures\\suinsit-nocode"));
//				// configuration.loadCharts(true);
//				configuration.loadDatabase(true);
//				// configuration.loadDesktopBean(true);
//				configuration.loadLyout(true);
//				configuration.loadToolkit(true);
//				// configuration.loadUIDesigner(true);
//				configuration.loadGenerador(true);
//				System.out.println("Loading class: " + className.trim());
//				builder.storeEntityFromClass(Class.forName(className.trim()), configuration);
//				System.out.println("Successfully loaded: " + className.trim());
//			} catch (Exception e) {
//				System.err.println("Failed to load class: " + className);
//				e.printStackTrace();
//			}
//		});
	}

	public void generate(String[] entityClasses) {

//		NoCodeAutoGenerator ncg = new NoCodeAutoGenerator();
//		schemaBD.setDriver("org.postgresql.Driver");
//		schemaBD.setUrl("jdbc:postgresql://localhost:61931/leka_db");
//		schemaBD.setUser("leka_user");
//		schemaBD.setPassword("MWYyZDFlMmU2N2Rm");
//		Configuration configuration = new Configuration(
//				new File("C:\\Users\\ManuelGonzalez\\git\\suinsit-platform-source\\architectures\\suinsit-nocode"));
//		configuration.loadDatabase(true);
//		// configuration.loadDesktopBean(true);
//		configuration.loadLyout(true);
//		configuration.loadToolkit(true);
//		// configuration.loadUIDesigner(true);
//		configuration.loadGenerador(true);
//		ncg.init(configuration, schemaBD,
//				"C:\\Users\\ManuelGonzalez\\git\\suinsit-platform-projects\\suinlesbasic\\application-source\\data\\model");
//		ncg.testImport(entityClasses);
		
		//ncg.processImport();
	}

}
