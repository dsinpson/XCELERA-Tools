package framework.cucumber.report.generator;

import framework.cucumber.report.model.Report;


public class GeneratorFactory {
	private GeneratorFactory() {
	}

	// TODO melhorar extrair para enum
	public static Generator getGenerator(String type, Report report) {
		switch (type) {
		case "pdf":
			return new GeneratorPdf(report);
		case "docx":
			return new GeneratorDocx(report);
		default:			
			return new GeneratorPdf(report);
		}
	}
}
