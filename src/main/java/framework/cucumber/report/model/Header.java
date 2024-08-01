package framework.cucumber.report.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Header {

	private final String title;

	private final Map<String, Map<String, String>> sections;

	public Header(String title) {
		sections = new LinkedHashMap<>();
		this.title = title;
	}

	public String getTitle() {
		return Optional.of(title).orElse("Test Report");
	}

	private void addSection(String sectionName) {
		sections.put(sectionName, new LinkedHashMap<>());
	}

	public void addFieldToSection(String sectionName, String key, String info) {
		// if replace null with empty string
		info = info == null || info.isEmpty() ? "" : info;

		if (sections.isEmpty() || !sections.containsKey(sectionName)) {
			addSection(sectionName);
		}

		sections.get(sectionName).put(key, info);
	}

	public Map<String, Map<String, String>> getSections() {
		return Collections.unmodifiableMap(this.sections);
	}

	public Integer getTotalLinesOfSections() {
		AtomicInteger totalSize = new AtomicInteger(0);
		sections.forEach((sectionKey, sectionValueMap) -> {
			totalSize.getAndIncrement();
			sectionValueMap.forEach((sectionFieldKey, sectionFieldValue) -> totalSize.getAndIncrement());
		});

		return totalSize.get();
	}

}
