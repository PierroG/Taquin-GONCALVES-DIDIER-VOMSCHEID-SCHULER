/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 *
 * @author Pierre
 */
public class Taquin extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        FXMLDocumentController controller = loader.getController();
        
        loader.setController(controller);
 
        Scene scene = new Scene(root);
        boolean add = scene.getStylesheets().add("css/styles.css");
        Plateau p = new Plateau(4);
        p.melange();
        controller.setPlateau(p);
        controller.initializePlateauView();
        
        stage.setScene(scene);
        stage.show();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Plateau p = new Plateau(4);
        //p.melange();
        launch(args);
        //p.affichePlateauConsole();
        /*boolean test = false;
        while(test == false){//vraie condition : p.isOver()==false
            p.choix();
            p.affichePlateauConsole();
            System.out.println(p.coefDesordre());
         
        }*/
    }
    
}
