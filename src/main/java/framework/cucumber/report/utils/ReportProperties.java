package framework.cucumber.report.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import framework.dataProviders.ConfigFileReader;

public class ReportProperties {
	private static final String DEFAULT_REPORT_FORMAT = "pdf";
	private static final String EVIDENCES_DEFAULT_DIRECTORY = "evidences";	
	private static final String DATE_FORMAT_FOLDER = "yyyy_MM_dd";
	
	private final ConfigFileReader reader;

	public ReportProperties() throws Exception {
		reader = new ConfigFileReader("configs/report.properties");
	}

	public String getProperty(String propKey) {
		return Optional.ofNullable(reader.getPropertyByKey(propKey)).orElse("");
	}
	
	public boolean reportIsActive() {
		return Boolean.parseBoolean(getProperty("report.active"));
	}

	public String getProject() {
		return getProperty("report.project");
	}

	public String getTester() {
		return getProperty("report.tester");
	}

	public String getReportDirectory() {
		String reportDir = getProperty("report.directory");
		return ((reportDir.isEmpty()) ? EVIDENCES_DEFAULT_DIRECTORY : reportDir) + File.separator + DateTimeUtils.getFormattedDate(DATE_FORMAT_FOLDER);
	}

	public List<String> getReportFileTypes() {
		String property = getProperty("report.fileTypes");
		if ("".equals(property)) {
			return Collections.singletonList(DEFAULT_REPORT_FORMAT);
		}

		return Arrays.asList(property.replace(" ", "").split(","));
	}
}
