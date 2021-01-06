/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lendle
 */
public class SplitPDFFileAction {
     private String convertPagesToFileSuffix(int [] pages){
         String suffix="";
         for(int page : pages){
             if(suffix.length()!=0){
                 suffix=suffix+"_";
             }
             suffix=suffix+page;
         }
         return suffix;
     }
     
     public File execute(File file, int [] pages){
        //InputStream input=null;
        File outputFolder=new File("paged_files");
        String outputFile=new File(outputFolder, file.getName()+"__"+convertPagesToFileSuffix(pages)+".pdf").getAbsolutePath();
        File outputFileObject=new File(outputFile);
        if(outputFileObject.exists() && outputFileObject.isFile() && outputFileObject.lastModified()>=file.lastModified()){
            //re-split is not needed
            return outputFileObject;
        }
        try {
            PDFUtil.split(file, outputFileObject, pages);
        } catch (Exception ex) {
            Logger.getLogger(SplitPDFFileAction.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return outputFileObject;
     }
    public File execute(File file, int page){
        //InputStream input=null;
        return this.execute(file, new int[]{page});
//        try {
//            input = new FileInputStream(file);
//            PdfReader inputPDF = new PdfReader(input);
//            //System.out.println(inputPDF.getPageN(1).getAsNumber(PdfName.ROTATE)+":"+PdfPage.LANDSCAPE);
//            OutputStream output=new FileOutputStream(outputFile);
//            //Document document = new Document(inputPDF.getPageSizeWithRotation(1));
//            Document document = new Document(inputPDF.getPageSize(1));
//            //System.out.println(inputPDF.getPageRotation(1)/Math.PI+":"+Math.cos(Math.toRadians(inputPDF.getPageRotation(1))));
//            
//            PdfWriter writer = PdfWriter.getInstance(document, output);
//            writer.addPageDictEntry(PdfName.ROTATE, inputPDF.getPageN(1).getAsNumber(PdfName.ROTATE));
//            document.open();
//            PdfContentByte cb = writer.getDirectContent();
//            document.newPage();
//            PdfImportedPage importedPage=writer.getImportedPage(inputPDF, page);
//           
//            
//            cb.addTemplate(importedPage, 
//                    0, 0);
//            output.flush();
//            input.close();
//            document.close();
//            output.close();
//        } catch (Exception ex) {
//            Logger.getLogger(SplitPDFFileAction.class.getName()).log(Level.SEVERE, null, ex);
//            Logger.getLogger(SplitPDFFileAction.class.getName()).severe("the file "+file+" cannot be successfully splitted, will send the original file");
//            return file;
//        } finally {
//            try {
//                input.close();
//            } catch (IOException ex) {
//                Logger.getLogger(SplitPDFFileAction.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return new File(outputFile);
    }
}
