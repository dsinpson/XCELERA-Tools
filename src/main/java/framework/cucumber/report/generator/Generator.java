package framework.cucumber.report.generator;

import java.awt.Dimension;
import java.io.File;

import framework.cucumber.report.model.ReportFileStructure;
import framework.cucumber.report.model.Report;

/**
 * Class describing a test report generator
 *
 */
public abstract class Generator {

	protected Report report;

	public Generator(Report report) {
		this.report = report;
	}

	public abstract void addSteps() throws Exception;

	public abstract void addHeader() throws Exception;
	
	public abstract void initDocument(String id, String scenarioName, boolean scenarioStatus, ReportFileStructure fileStructure) throws Exception;

	public abstract File saveFile() throws Exception;
	
	public Dimension getDimension(float widthImg, float heightImg, float widthPage, float heightPage) {
		float percentageScaleWidth = 1;
		float percentageScaleHeight = 1;
		
		if (widthImg > widthPage) {
			percentageScaleWidth = widthPage / widthImg;
		}
		
		if (heightImg > heightPage) {
			percentageScaleHeight = heightPage / heightImg;
		}
		
		if (percentageScaleWidth < percentageScaleHeight) {
			percentageScaleHeight = percentageScaleWidth;
		} else if (percentageScaleHeight < percentageScaleWidth) {
			percentageScaleWidth = percentageScaleHeight;
		}
		
		widthImg = widthImg * percentageScaleWidth;
		heightImg = heightImg * percentageScaleHeight;
		
		return new Dimension((int) widthImg, (int) heightImg);
	}

}