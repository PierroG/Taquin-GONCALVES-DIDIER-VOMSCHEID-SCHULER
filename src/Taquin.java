/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import GamePackage.Jdbc;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Pierre
 */
public class Taquin extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Jdbc.getInstance();
        Parent root = FXMLLoader.load(getClass().getResource("/GamePackage/FXMLDocument.fxml"));
        
 
        Scene scene = new Scene(root);
        boolean add = scene.getStylesheets().add("css/styles.css");
        
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
