package ma.azdad;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ma.azdad.service.UtilsFunctions;

public class GenerateBarAndQrode {

	@Test
	public void pn() throws InterruptedException, IOException, DocumentException {

		// Document document = new Document(PageSize.A7.rotate(), 10, 10, 10, 10);
		Document document = new Document(new RectangleReadOnly(171, 284), 5, 5, 5, 5); // 100mm * 60mm
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
		Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
		Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
		PdfPTable table1 = new PdfPTable(1);
		table1.setTotalWidth(171);
		table1.setLockedWidth(true);
		PdfPCell cell1, cell2;
		Phrase phrase;
		Paragraph paragraph;
		PdfPTable table2 = new PdfPTable(2);

		PdfWriter.getInstance(document, new FileOutputStream("/home/azdad/Bureau/test.pdf"));
		document.open();

		// qrcode Cell
		BarcodeQRCode barcodeQrcode = new BarcodeQRCode("http://qr.3gcom.ma/45496/dfcae9a98d1db2ca389ed075a70494d5", 300, 300, null);
		Image qrcodeImage = barcodeQrcode.getImage();
		qrcodeImage.scaleToFit(100, 100);
		cell1 = new PdfPCell();
		cell1.setBorder(Rectangle.BOTTOM);
		qrcodeImage.setAlignment(Element.ALIGN_CENTER);
		cell1.addElement(qrcodeImage);
		table1.addCell(cell1);

		paragraph = new Paragraph();
		paragraph.setLeading(0, 1f);
		paragraph.add(new Chunk("Part Number\n", boldFont));
		paragraph.add(new Chunk("XDGDD45454", normalFont));
		paragraph.add(new Chunk("\nDescription\n", boldFont));
		paragraph.add(new Chunk("pn_description", normalFont));
		paragraph.add(new Chunk("\nPacking ID\n", boldFont));
		paragraph.add(new Chunk("1654654", normalFont));
		paragraph.add(new Chunk("\nQuantity\n", boldFont));
		paragraph.add(new Chunk("23", normalFont));
		paragraph.add(new Chunk("\nPackaging Type\n", boldFont));
		paragraph.add(new Chunk("Box", normalFont));
		paragraph.add(new Chunk("\nTotal Of Items\n", boldFont));
		paragraph.add(new Chunk("12", normalFont));
		paragraph.add(new Chunk("\nGross Weight\n", boldFont));
		paragraph.add(new Chunk("1234 Kg", normalFont));
		paragraph.add(new Chunk("\nCBM\n", boldFont));
		paragraph.add(new Chunk("1234 m3", normalFont));

		cell1 = new PdfPCell();
		cell1.setBorder(Rectangle.NO_BORDER);
		cell1.addElement(paragraph);
		table1.addCell(cell1);
		document.add(table1);

		Image logo = Image.getInstance("/home/anass/Bureau/logo.png");
		logo.scaleToFit(50, 60);
		logo.setAbsolutePosition(120, 0);
		document.add(logo);

		document.close();
	}

	@Test
	public void dn() throws InterruptedException, IOException, DocumentException {

		// Document document = new Document(PageSize.A7.rotate(), 10, 10, 10, 10);
		Document document = new Document(new RectangleReadOnly(284, 171), 5, 5, 5, 5); // 100mm * 60mm
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
		Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
		Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
		float[] pointColumnWidths = { 158F, 300F };
		PdfPTable table1 = new PdfPTable(pointColumnWidths);
		table1.setTotalWidth(290);
		table1.setLockedWidth(true);
		PdfPCell cell1, cell2;
		Phrase phrase;
		Paragraph paragraph;
		PdfPTable table2 = new PdfPTable(2);

		PdfWriter.getInstance(document, new FileOutputStream("/home/anass/Bureau/test.pdf"));
		document.open();

		paragraph = new Paragraph("DN123456 | Delivery Date : 2018-01-01", titleFont);
		paragraph.setAlignment(Element.ALIGN_CENTER);
		paragraph.setSpacingAfter(10f);
		document.add(paragraph);

		// qrcode Cell
		BarcodeQRCode barcodeQrcode = new BarcodeQRCode("DN10234;PN: XDGDD45454", 100, 100, null);
		Image qrcodeImage = barcodeQrcode.getImage();
		qrcodeImage.scaleToFit(95, 95);
		//		qrcodeImage.scalePercent(100);
		Image logo = Image.getInstance("/home/anass/Bureau/logo.png");
		logo.scaleToFit(50, 60);
		logo.setAlignment(Element.ALIGN_CENTER);
		cell1 = new PdfPCell();
		cell1.setBorder(Rectangle.NO_BORDER);
		cell1.addElement(qrcodeImage);
		cell1.addElement(logo);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table1.addCell(cell1);

		// owner/project/ref/g weight/volume cell
		phrase = new Phrase();
		phrase.add(new Chunk("# Of Items : 15", boldFont));
		phrase.add(new Chunk("\nOwner : ", boldFont));
		phrase.add(new Chunk(UtilsFunctions.cutText("Agence pour la Promotion et le développement des préfecture et Provinces du nord du royaume", 70), normalFont));
		phrase.add(new Chunk("\nProject : ", boldFont));
		phrase.add(new Chunk(UtilsFunctions.cutText("E_Medical_SPL_AO_10LOT2_02LOT1_Demnate_2017", 25), normalFont));
		phrase.add(new Chunk("\nRef : ", boldFont));
		phrase.add(new Chunk(UtilsFunctions.cutText("Chison inbound", 25), normalFont));
		cell1 = new PdfPCell();
		cell1.setPadding(3f);
		cell1.setLeading(0, 1f);
		cell1.addElement(phrase);

		phrase = new Phrase();
		phrase.add(new Chunk("Gross Weight\n", boldFont));
		phrase.add(new Chunk("5545 Kg", boldFont));
		cell2 = new PdfPCell();
		cell2.setBorder(0);
		cell2.addElement(phrase);
		table2.addCell(cell2);
		phrase = new Phrase();
		phrase.add(new Chunk("Volume\n", boldFont));
		phrase.add(new Chunk("11.3m3", boldFont));
		cell2 = new PdfPCell();
		cell2.setBorder(0);
		cell2.addElement(phrase);
		table2.addCell(cell2);
		cell1.addElement(table2);
		cell1.setBorder(Rectangle.LEFT);
		table1.addCell(cell1);
		document.add(table1);

		//		document.add(new Chunk("00", normalFont));
		//
		//		document.add(new Chunk("\nPN:", normalFont));
		//		document.add(new Chunk("EAB-7M7MRS120350FF", boldFont));
		//
		//		document.add(new Chunk("\nDelivery Date:", normalFont));
		//		document.add(new Chunk("2018-01-01", boldFont));

		document.close();
	}

	//	@Test
	public void test1() throws InterruptedException, IOException, DocumentException {

		// Document document = new Document(PageSize.A7.rotate(), 10, 10, 10, 10);
		Document document = new Document(new RectangleReadOnly(200, 290), 10, 10, 10, 10);

		PdfWriter.getInstance(document, new FileOutputStream("/home/azdad/Bureau/test.pdf"));
		document.open();

		BarcodeQRCode barcodeQrcode = new BarcodeQRCode("DN10234;PN: XDGDD45454", 100, 100, null);
		Image qrcodeImage = barcodeQrcode.getImage();
		// qrcodeImage.setAbsolutePosition(10, 10);
		qrcodeImage.setAlignment(Paragraph.ALIGN_CENTER);
		qrcodeImage.scalePercent(100);
		document.add(qrcodeImage);

		Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 12);
		Font normalFont = FontFactory.getFont(FontFactory.COURIER, 10);
		Font boldFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 10);

		Paragraph paragraph = new Paragraph("NSN Maroc", titleFont);
		paragraph.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph);

		document.add(new Chunk("DN101234", normalFont));

		document.add(new Chunk("\nPN:", normalFont));
		document.add(new Chunk("EAB-7M7MRS120350FF", boldFont));

		document.add(new Chunk("\nDelivery Date:", normalFont));
		document.add(new Chunk("2018-01-01", boldFont));

		document.close();
	}

	// @Test
	public void test() throws InterruptedException, IOException {

		try {
			Document document = new Document();
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("/home/azdad/Bureau/HelloWorld.pdf"));

			document.open();
			PdfContentByte pdfContentByte = pdfWriter.getDirectContent();

			Barcode128 barcode128 = new Barcode128();
			barcode128.setCode("examples.javacodegeeks.com/aut.hor/chandan-singh");
			barcode128.setCodeType(Barcode128.CODE128);
			Image code128Image = barcode128.createImageWithBarcode(pdfContentByte, null, null);
			code128Image.setAbsolutePosition(10, 800);
			code128Image.scalePercent(100);
			document.add(code128Image);

			BarcodeEAN barcodeEAN = new BarcodeEAN();
			barcodeEAN.setCodeType(BarcodeEAN.EAN13);
			barcodeEAN.setCode("1234523453323");
			barcodeEAN.setBarHeight(50f);
			barcodeEAN.setX(2f);
			Image codeEANImage = barcodeEAN.createImageWithBarcode(pdfContentByte, null, null);
			codeEANImage.setAbsolutePosition(20, 200);
			codeEANImage.scalePercent(100);
			document.add(codeEANImage);

			BarcodeQRCode barcodeQrcode = new BarcodeQRCode("examples.javacodegeeks.com/author/chandan-singh", 100, 100, null);
			Image qrcodeImage = barcodeQrcode.getImage();
			qrcodeImage.setAbsolutePosition(20, 500);
			qrcodeImage.scalePercent(100);
			document.add(qrcodeImage);

			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void generateImage() throws IOException {
		BarcodeEAN barcodeEAN = new BarcodeEAN();
		barcodeEAN.setCodeType(BarcodeEAN.EAN13);
		barcodeEAN.setCode("1234523453323");
		barcodeEAN.setBarHeight(100f);
		barcodeEAN.setX(1f);
		barcodeEAN.setN(2f);
		java.awt.Image image = barcodeEAN.createAwtImage(Color.BLACK, Color.WHITE);
		BufferedImage buffImg = new BufferedImage(800, 200, BufferedImage.TYPE_4BYTE_ABGR);
		buffImg.getGraphics().drawImage(image, 0, 0, null);
		buffImg.getGraphics().dispose();
		File file = new File("/home/azdad/Bureau/tmp.png");
		ImageIO.write(buffImg, "png", file);
	}

}