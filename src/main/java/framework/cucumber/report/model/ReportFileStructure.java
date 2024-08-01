package framework.cucumber.report.model;

public class ReportFileStructure {
	private String userInfo;
	private String functionality;
	private String group;
	private String idScenario;
	private String idUser;

	public String getUserInfo() {
		return this.userInfo;
	}
	
	public String getFunctionality() {
		return this.functionality;
	}
	
	public String getGroup() {
		return this.group;
	}
	
	public String getIdScenario() {
		return this.idScenario;
	}
	
	public String getIdUser() {
		return this.idUser;
	}
	
	public String getScenarioName(String scenarioNameFeature) {
		if (idScenario != null && !idScenario.isEmpty() && idUser != null && !idUser.isEmpty()) {
			return idScenario + "_" + idUser;
		}
		return scenarioNameFeature;
	}
}