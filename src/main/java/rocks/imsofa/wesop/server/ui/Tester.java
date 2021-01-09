/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import rocks.imsofa.wesop.server.ui.server.DownloadingSessionManager;
import rocks.imsofa.wesop.server.ui.server.FileDownloadServlet;
import rocks.imsofa.wesop.server.ui.server.Global;
import rocks.imsofa.wesop.server.ui.server.PlayStartReportServlet;
import rocks.imsofa.wesop.server.ui.server.TaskDetailInstanceQueue;
import rocks.imsofa.wesop.server.ui.server.TerminatedReportServlet;

/**
 *
 * @author lendle
 */
public class Tester extends Application {

    private Server server;

    @Override
    public void start(Stage primaryStage) {
        try {
            server = new Server();
            Global.downloadingSessionManager=new DownloadingSessionManager();
            Global.taskDetailInstanceQueue=new TaskDetailInstanceQueue();
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(8080);
            server.setConnectors(new Connector[]{connector});
            ServletContextHandler ctx = new ServletContextHandler();
            Global.servletContext=ctx.getServletContext();
            ctx.setContextPath("/");

            DefaultServlet defaultServlet = new DefaultServlet();
            ServletHolder holderPwd = new ServletHolder("default", defaultServlet);
            holderPwd.setInitParameter("resourceBase", "./");
            ctx.addServlet(holderPwd, "/*");
            ctx.addServlet(FileDownloadServlet.class, "/download");
            ctx.addServlet(TerminatedReportServlet.class, "/terminatedReport");
            ctx.addServlet(PlayStartReportServlet.class, "/playStartReport");
            server.setHandler(ctx);
            server.start();

            FXMLLoader fxmlLoader = new FXMLLoader(Tester.class.getResource("Tester.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            
            primaryStage.setTitle("WeSOP Tester");
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        server.stop();
                        File [] folders=new File(".").listFiles();
                        for(File folder : folders){
                            if(folder.isDirectory() && Character.isDigit(folder.getName().charAt(0)) && Character.isDigit(folder.getName().charAt(1))){
                                FileUtils.deleteDirectory(folder);
                            }
                        }
                        System.exit(0);
                    } catch (Exception ex) {
                        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
