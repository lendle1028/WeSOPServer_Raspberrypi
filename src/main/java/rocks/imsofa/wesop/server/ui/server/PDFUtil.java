/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 *
 * @author lendle
 */
public class PDFUtil {
    public static void split(File oldFile, File newFile, int [] pages) throws Exception{
        //Logger.getLogger(PDFUtil.class.getName()).info(oldFile.getAbsolutePath()+":"+oldFile.exists()+":"+oldFile.length());
        try(InputStream input=new FileInputStream(oldFile); OutputStream output=new FileOutputStream(newFile)){
            PdfReader inputPDF = new PdfReader(input);
            Document document = new Document(inputPDF.getPageSize(1));
            PdfWriter writer = PdfWriter.getInstance(document, output);
            //writer.addPageDictEntry(PdfName.ROTATE, inputPDF.getPageN(1).getAsNumber(PdfName.ROTATE));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            for(int page : pages){
                document.newPage();
                System.out.println(writer+":"+inputPDF);
                writer.addPageDictEntry(PdfName.ROTATE, inputPDF.getPageN(page).getAsNumber(PdfName.ROTATE));
                PdfImportedPage importedPage=writer.getImportedPage(inputPDF, page);
                cb.addTemplate(importedPage, 0, 0);
            }
            output.flush();
            document.close();
        }
    }
    
    public static int getPages(File file) throws Exception{
        try(InputStream input=new FileInputStream(file)){
            PdfReader inputPDF = new PdfReader(input);
            return inputPDF.getNumberOfPages();
        }
    }
    
    public static void main(String [] args) throws Exception{
        /*PDFUtil.split(new File("/home/lendle/dev/android_studio_projects/EsopServer/WeSOPProducer/src/main/webapp/WEB-INF/files/test.pdf"), 
                new File("/home/lendle/dev/android_studio_projects/EsopServer/WeSOPProducer/src/main/webapp/WEB-INF/files/test1.pdf"), 
                new int[]{2, 3, 5});*/
        PDFUtil.split(new File("/home/lendle/Desktop/FamesESOP.pdf"), new File("/home/lendle/Desktop/test1.pdf"), new int[]{1, 2, 3});
    }
}
