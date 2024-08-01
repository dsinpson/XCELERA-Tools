package framework.cucumber.report.generator;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import framework.cucumber.report.ReportGenerator;
import framework.cucumber.report.model.Header;
import framework.cucumber.report.model.Report;
import framework.cucumber.report.model.ReportFileStructure;
import framework.cucumber.report.model.Step;

/**
 * Implementation of Generator to pdf files
 */
public class GeneratorPdf extends Generator {

	private Document doc;
	private File reportFile;
	
	public GeneratorPdf(Report report) {
		super(report);
	}

	@Override
	public void addSteps() throws Exception {
		if (report.getSteps() == null || report.getSteps().isEmpty()) {
			return;
		}

		Integer i = 0;
		for (Step step : report.getSteps()) {
			i++;	
			if(step.getIsNewPage())
				doc.newPage();
			
			Paragraph sub = createParagraph(step.getSub());
			doc.add(sub);
			
			if(step.getImage() != null) {				
				Image image = createImage(step.getImage());
				doc.add(image);
			}
		}
	}

	private Paragraph createParagraph(String description) {
		Font font = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);
		Paragraph p = new Paragraph(description, font);
		p.setAlignment(Element.ALIGN_LEFT);
		return p;
	}

	private Image createImage(byte[] imageBase64) throws Exception {
		Image img = Image.getInstance(imageBase64);		
		Dimension dimension = getDimension(img.getWidth(), img.getHeight(), doc.getPageSize().getWidth(), doc.getPageSize().getHeight());
		
		float widthImg = (float) (dimension.getWidth() * 0.87);
		float heightImg = (float) (dimension.getHeight() * 0.87);
		
		img.setAlignment(Image.MIDDLE);			
		img.scaleAbsolute(widthImg, heightImg);
		
		return img;
	}

	@Override
	public void addHeader() throws Exception {
		Header header = report.getHeader();

		// create table
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);

		// title
		table.addCell(createHeaderCell());

		// body
		if (header.getSections() == null || header.getSections().isEmpty()) {
			throw new RuntimeException("No information was found on header");
		}

		header.getSections().forEach((sectionTitle, sectionMap) -> sectionMap
				.forEach((key, value) -> table.addCell(createInfoCell(key, value))));

		// adding header to doc
		doc.add(table);
		doc.add(new Paragraph("\n\n"));
	}

	private PdfPCell createInfoCell(String name, String info) {
		PdfPCell cell = new PdfPCell();
		if(name.equalsIgnoreCase(ReportGenerator.STATUS_FIELD_NAME)) {
			if(info.equalsIgnoreCase(ReportGenerator.STATUS_PASSED)) {
				cell.setBackgroundColor(new BaseColor(ReportGenerator.GREEN.getRGB()));
			}
			else if(info.equalsIgnoreCase(ReportGenerator.STATUS_FAILED)) {
				cell.setBackgroundColor(new BaseColor(ReportGenerator.RED.getRGB()));
			}
		}
		cell.setPhrase(new Phrase(name + ": " + info));
		return cell;
	}

	private PdfPCell createHeaderCell() {
		PdfPCell headerTitle = new PdfPCell();
		headerTitle.setBackgroundColor(new BaseColor(ReportGenerator.LIGHT_GRAY.getRGB()));
		headerTitle.setBorderWidth(2);
		headerTitle.setPhrase(new Phrase("Test Report"));
		headerTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerTitle.setVerticalAlignment(Element.ALIGN_CENTER);
		return headerTitle;
	}

	@Override
	public void initDocument(String id, String scenarioName, boolean scenarioStatus, ReportFileStructure fileStructure)
			throws Exception {
		reportFile = report.createReportFile(id, scenarioName, scenarioStatus, "pdf");

		if (doc == null) {
			doc = new Document();

			PdfWriter.getInstance(doc, new FileOutputStream(reportFile));
			doc.open();
		}
	}

	@Override
	public File saveFile() throws Exception {

		if (doc != null) {
			doc.close();
			doc = null;
		}

		return reportFile;
	}
}
