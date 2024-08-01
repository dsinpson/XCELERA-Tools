package framework.cucumber.report.model;

public class Step {
	private final boolean isNewPage;
	private final String sub;
	private final byte[] image;
	
	public Step(String sub, byte[] image, boolean isNewPage) {
		this.sub = sub;
		this.image = image;
		this.isNewPage = isNewPage;
	}
	
	public String getSub() {
		return this.sub;
	}
	
	public byte[] getImage() {
		return this.image;
	}
	
	public boolean getIsNewPage() {
		return this.isNewPage;
	}
}
