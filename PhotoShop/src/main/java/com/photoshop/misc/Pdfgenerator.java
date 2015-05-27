/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.photoshop.misc;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.photoshop.controllers.OrderController;
import com.photoshop.models.order.Order;
import com.photoshop.models.orderrow.OrderRow;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 *
 * @author bart
 */
public class Pdfgenerator {
    private Environment env;
    private static Font catFont;
    private static Font subFont;
    private static Font smallBold;
    private static Font subtitel;
    private Order order;
    
    public Pdfgenerator(Order order,Environment env)
    {
        this.env = env;
        this.order = order;
        String FILE = "c:/order1.pdf"; //order generate moet nog gemaakt worden
        catFont = new Font(Font.FontFamily.HELVETICA, 18,
                Font.BOLD);
        subtitel = new Font(Font.FontFamily.HELVETICA, 14,
                Font.BOLD);
        subFont = new Font(Font.FontFamily.HELVETICA, 12,
                Font.NORMAL);
        smallBold = new Font(Font.FontFamily.HELVETICA, 12,
                Font.BOLD);

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            //addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMetaData(Document document) {
        document.addTitle("Order1"); //Moet order nummer uit database worden
        document.addAuthor("fotograaf1"); // naam fotograaf
        document.addCreator("Photowinkel"); // aangeboden door
    }

    private void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);

        Image Logo = null;
        try {
            Logo = Image.getInstance(env.getProperty("logo") + "Photoshop_black.png");
            Logo.scaleAbsolute(200, 100);
        } catch (BadElementException | IOException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }

        preface.add(Logo);
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Bedrijf:", subtitel));
        preface.add(new Paragraph("Rachelsmolen 1", subFont));
        preface.add(new Paragraph("5612MA Eindhoven", subFont)); //order nummer ingelezen worde
        preface.add(new Paragraph("Rekening: 165947888", subFont));
        preface.add(new Paragraph("Bank: Paypal", subFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Aan:", subtitel));
        preface.add(new Paragraph(order.getInvoiceaddress().getKlantnaam(), subFont)); //order nummer ingelezen worde
        preface.add(new Paragraph(order.getInvoiceaddress().getAdres(), subFont));
        preface.add(new Paragraph(order.getInvoiceaddress().getPostcode() + " " + order.getInvoiceaddress().getWoonplaats(), subFont));
        preface.add(new Paragraph(order.getInvoiceaddress().getTelefoonnummer(), subFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Geachte heer" + " " + order.getInvoiceaddress().getKlantnaam(), subtitel));
        addEmptyLine(preface, 1);
        createTable(preface);
        document.add(preface);

        // Start a new page
        //document.newPage();
    }
    
    private void createTable(Paragraph preface)
            throws BadElementException, DocumentException {
                // font
        Font font = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        // create header cell
        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(600);
        PdfPCell c1 = new PdfPCell(new Phrase("Hoeveelheid",font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Beschrijving",font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Prijs per eenheid",font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Totaal",font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        // set the width of the table to 100% of page
        table.setWidthPercentage(100);
 
        // set relative columns width
        table.setWidths(new float[]{0.6f, 1.4f, 0.8f,0.8f});
        table.setHeaderRows(1);
        double totaalprijs = 0;
        
        for(OrderRow row : order.getOrders())
        {
            table.addCell(row.getAantal() + "");
            table.addCell(row.getProduct().getName());
            table.addCell(row.getProduct().getPrice() + "");
            double totaalprijsproduct = row.getProduct().getPrice() * row.getAantal();
            table.addCell(totaalprijsproduct + "");
            totaalprijs = totaalprijs + totaalprijsproduct;    
        }
     
        
        preface.add(table);
        //subCatPart.add(table);

    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    
}
