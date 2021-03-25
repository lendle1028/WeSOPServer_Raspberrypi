/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Paint;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author lendle
 */
public class Main_FX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main_FX.class.getResource("Main.fxml"));
            MainController mainController=fxmlLoader.getController();
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            primaryStage.setTitle("WeSOP");
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        mainController.saveLog();
                        System.exit(0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            primaryStage.setScene(scene);
            if (SystemTray.isSupported()) {
                PopupMenu popup = new PopupMenu();
                Image image = Toolkit.getDefaultToolkit().getImage(Main_FX.class.getResource("icon.png"));
                TrayIcon trayIcon = new TrayIcon(image);
                SystemTray tray = SystemTray.getSystemTray();
                MenuItem showItem = new MenuItem("Show");
                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
                popup.add(showItem);
                showItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                primaryStage.setIconified(false);
                                primaryStage.show();
                            }
                        });
                    }
                });
                trayIcon.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    primaryStage.setIconified(false);
                                    primaryStage.show();
                                }
                            });
                        }
                    }

                });
                primaryStage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (newValue.booleanValue()) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("hide");
                                    primaryStage.hide();
                                }
                            });
                        }
                    }
                });
                popup.add(exitItem);
                trayIcon.setPopupMenu(popup);
                tray.add(trayIcon);
                scene.setFill(Color.TRANSPARENT);
                Platform.setImplicitExit(false);

            } else {
                primaryStage.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(Main_FX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
