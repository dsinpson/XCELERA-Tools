package framework.cucumber.report.generator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBrType;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import framework.cucumber.report.ReportGenerator;
import framework.cucumber.report.model.Header;
import framework.cucumber.report.model.Report;
import framework.cucumber.report.model.ReportFileStructure;
import framework.cucumber.report.model.Step;

/**
 * Implementation of Generator to docx files
 */
public class GeneratorDocx extends Generator {

	private MainDocumentPart doc;
	private WordprocessingMLPackage wordPackage;
	private String scenarioName;
	private String id;
	private boolean scenarioStatus;

	public GeneratorDocx(Report report) {
		super(report);
	}
	
	private byte[] getResizeImage(byte[] image) {

		try {
			int widthPage = (int) (wordPackage.getDocumentModel().getSections().get(0).getPageDimensions().getPgSz().getW().intValue() * 0.05);
			int heightPage = (int) (wordPackage.getDocumentModel().getSections().get(0).getPageDimensions().getPgSz().getH().intValue() * 0.05);

			BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(image));
			Dimension dimension = getDimension(buffImg.getWidth(), buffImg.getHeight(), widthPage, heightPage);

			int width = (int) (dimension.getWidth());
			int height = (int) (dimension.getHeight());

			BufferedImage newImg = new BufferedImage(width, height, buffImg.getType());
			Graphics2D g = newImg.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(buffImg, 0, 0, width, height, null);
			g.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(newImg, "png", baos);
			return baos.toByteArray();
		} catch (Exception e) {
			return image;
		}
	}

	@Override
	public void addSteps() throws Exception {
		if (report.getSteps() == null || report.getSteps().isEmpty()) {
			return;
		}

		Integer i = 0;
		for (Step step : report.getSteps()) {
			i++;
			//byte[] image = step.getImage();				
			byte[] image = getResizeImage(step.getImage());				
						
			String sub = step.getSub();

			BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordPackage, image);
			Inline inline = imagePart.createImageInline("Step " + i + " image", "Step " + i + " image", i, i, false);
			P imageParagraph = addImageToParagraph(inline);
			addBr();
			doc.getContent().add(imageParagraph);
			doc.addParagraphOfText(sub);
		}
	}

	private void addBr() {
		ObjectFactory factory = new ObjectFactory();

		Br breakObj = new Br();
		breakObj.setType(STBrType.PAGE);

		P paragraph = factory.createP();
		paragraph.getContent().add(breakObj);
		doc.getJaxbElement().getBody().getContent().add(paragraph);
	}

	private P addImageToParagraph(Inline inline) {
		// TODO add frame to image
		ObjectFactory factory = new ObjectFactory();
		P p = factory.createP();
		R r = factory.createR();

		p.getContent().add(r);
		Drawing drawing = factory.createDrawing();

		r.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);

		return p;
	}

	@Override
	public void addHeader() throws Exception {
		Header header = report.getHeader();
		// TODO check how to take off the extra line generated in table

		// create table object
		int rowNumber = header.getTotalLinesOfSections();
		int colNumber = 1;
		Tbl tbl = TblFactory.createTable(rowNumber, colNumber,
				wordPackage.getDocumentModel().getSections().get(0).getPageDimensions().getWritableWidthTwips()
						/ colNumber);
		// add title cell
		createTc(tbl, 0, tableTitleParagraph(report.getHeader().getTitle()),
				convertRGBtoHex(ReportGenerator.LIGHT_GRAY));

		// add header content
		AtomicInteger i = new AtomicInteger(1);
		header.getSections().forEach((sectionTitle, sectionMap) -> sectionMap.forEach((key, value) -> {
			createTc(tbl, i.get(), key, value);
			i.getAndIncrement();
		}));

		wordPackage.getMainDocumentPart().addObject(tbl);
	}

	private String convertRGBtoHex(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}

	public Tc createTc(Tbl tbl, int index, String key, String value) {
		P text = doc.createParagraphOfText(key + ": " + value);
		if (key.equalsIgnoreCase(ReportGenerator.STATUS_FIELD_NAME)) {
			if (value.equalsIgnoreCase(ReportGenerator.STATUS_PASSED)) {
				return createTc(tbl, index, text, convertRGBtoHex(ReportGenerator.GREEN));
			} else if (value.equalsIgnoreCase(ReportGenerator.STATUS_FAILED)) {
				return createTc(tbl, index, text, convertRGBtoHex(ReportGenerator.RED));
			}
		}
		return createTc(tbl, index, text, null);
	}

	public Tc createTc(Tbl tbl, int index, P text, String color) {

		org.docx4j.wml.ObjectFactory wmlObjectFactory = new org.docx4j.wml.ObjectFactory();

		Tc tc = ((Tc) ((Tr) tbl.getContent().get(index)).getContent().get(0));
		// Create object for tcPr
		TcPr tcpr = wmlObjectFactory.createTcPr();
		tc.setTcPr(tcpr);
		// Create object for tcW
		TblWidth tblwidth = wmlObjectFactory.createTblWidth();
		tcpr.setTcW(tblwidth);
		tblwidth.setType("dxa");
		tblwidth.setW(BigInteger.valueOf(
				wordPackage.getDocumentModel().getSections().get(0).getPageDimensions().getWritableWidthTwips()));

		if (color != null) {
			// Create object for shd
			CTShd shd = wmlObjectFactory.createCTShd();
			tcpr.setShd(shd);
			shd.setVal(org.docx4j.wml.STShd.CLEAR);
			shd.setColor("auto");
			shd.setFill(color);
		}

		tc.getContent().add(text);
		return tc;
	}

	private P tableTitleParagraph(String title) {
		ObjectFactory factory = Context.getWmlObjectFactory();
		P p = factory.createP();
		R r = factory.createR();
		Text t = factory.createText();
		t.setValue(title);

		r.getContent().add(t);
		p.getContent().add(r);

		RPr rpr = factory.createRPr();
		rpr.setB(new BooleanDefaultTrue());

		r.setRPr(rpr);

		return p;
	}

	@Override
	public void initDocument(String id, String scenarioName, boolean scenarioStatus, ReportFileStructure fileStructure)
			throws Exception {
		this.scenarioStatus = scenarioStatus;
		this.scenarioName = scenarioName;
		this.id = id;

		wordPackage = WordprocessingMLPackage.createPackage();
		doc = wordPackage.getMainDocumentPart();
	}

	@Override
	public File saveFile() {
		File reportFile = report.createReportFile(id, scenarioName, scenarioStatus, "docx");
		try {
			wordPackage.save(reportFile);
		} catch (Docx4JException e) {
			throw new RuntimeException("Error saving doc evidence.", e);
		}
		return reportFile;
	}

}
