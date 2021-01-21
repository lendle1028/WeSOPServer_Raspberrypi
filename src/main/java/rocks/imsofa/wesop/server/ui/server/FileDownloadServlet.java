/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author lendle
 */
@WebServlet(name = "download", urlPatterns = {"/download"})
public class FileDownloadServlet extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        OutputStream out = response.getOutputStream();
        //System.out.println("file="+request.getParameter("file"));
        //File file=new File(this.getServletContext().getRealPath("/WEB-INF/files/"+URLDecoder.decode(request.getParameter("file"), "utf-8") ));
        //File file=new File(Global.systemProperties.getFilesFolder(), URLDecoder.decode(request.getParameter("file"), "utf-8") );
        String partString = request.getParameter("part");
        String sessionId = request.getParameter("sessionId");
        if (partString == null) {
            //assume the first part is used
            partString = "0";
        }
        int part = Integer.valueOf(partString);
        //int page=Integer.valueOf(request.getParameter("page"));
        DownloadingSession session = Global.downloadingSessionManager.getDownloadSession(sessionId);
        if (session != null) {
            session.setCurrentPartIndex(part);
            session.setNumFinishedParts(part);
            Logger.getLogger(this.getClass().getName()).info("part=" + part + ":" + session.getSessionId());
            File file = Global.downloadingSessionManager.getChunkFile(sessionId, part);
//        boolean autoflip=Boolean.valueOf(request.getParameter("autoflip"));
//        if(autoflip && page!=-1 && file.getAbsolutePath().toLowerCase().endsWith(".pdf")){
//            SplitPDFFileAction action=new SplitPDFFileAction();
//            file=action.execute(file, page);
//        }
//        
            try (InputStream input = new FileInputStream(file)) {
               
                response.setHeader("sessionStatus", "ok");
                response.setHeader("Content-Disposition", "attachment;filename=" + request.getParameter("file") + ";");
                response.setHeader("Content-Length", "" + file.length());
                IOUtils.copy(input, out);
            } finally {
                out.close();
            }
            //TaskDetailInstance instance=Global.taskDetailInstanceQueue.getTaskDetailInstance(session.getTaskDetailInstanceId());
            //instance.setCurrentStatusValidThrough(instance.getCurrentStatusValidThrough()+Constants.PER_MEGA_DOWNLOAD_TIMEOUT);
        }else{
            //the session doesn't exist or is cancelled
            try{
                response.setHeader("sessionStatus", "removed");
                IOUtils.write("fail", out);
            }finally{
                out.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
