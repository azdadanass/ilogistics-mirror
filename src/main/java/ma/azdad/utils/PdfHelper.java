package ma.azdad.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfHelper  extends PdfPageEventHelper {
	private PdfPTable headerTable;
	 private PdfTemplate totalPageTemplate;
	 private BaseFont baseFont;

	    @Override
	    public void onOpenDocument(PdfWriter writer, Document document) {
	        totalPageTemplate = writer.getDirectContent().createTemplate(50, 50);
	        try {
	            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	
	

	 public PdfHelper(PdfPTable headerTable) {
			this.headerTable = headerTable;
		}



	@Override
	    public void onEndPage(PdfWriter writer, Document document) {
	        PdfContentByte canvas = writer.getDirectContent();
            headerTable.setTotalWidth(document.right() - document.left());
            float headerY = document.getPageSize().getTop() - 20;  
            headerTable.writeSelectedRows(0, -1, document.left(), headerY, canvas);
           
	        int currentPage = writer.getPageNumber();
	        float x = (document.right() + document.left()) / 2;
	        float y = document.bottom() - 20;
	        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, 
	            new Phrase("Page " + currentPage + "/ ", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL,new BaseColor(70, 73, 74))), x, y, 0);
	        
	        canvas.setColorFill(new BaseColor(70, 73, 74)); // Set fill color to gray
	        canvas.addTemplate(totalPageTemplate, x + 18, y);
	        canvas.setColorFill(BaseColor.BLACK); // Reset color to default after drawing

	    }

	    @Override
	    public void onCloseDocument(PdfWriter writer, Document document) {
	        totalPageTemplate.beginText();
	        totalPageTemplate.setFontAndSize(baseFont, 10);
	        totalPageTemplate.setTextMatrix(0, 0);
	        totalPageTemplate.showText(String.valueOf(writer.getPageNumber() - 1)); // Total pages
	        totalPageTemplate.endText();
	    }
}
