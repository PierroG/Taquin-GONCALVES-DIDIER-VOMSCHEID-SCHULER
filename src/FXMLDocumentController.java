/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Pierre
 */
public class FXMLDocumentController implements Initializable {
    
    
    private Plateau plateau;
    @FXML
    private Label label;
    @FXML
    private GridPane grille;
    @FXML
    private Pane fond; // panneau recouvrant toute la fenêtre
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("taquin");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Pane p = new Pane();
        Label l = new Label();
        p.getStyleClass().add("case"); 
        p.getChildren().add(l);
        l.getStyleClass().add("num");
        l.setText("10");
        grille.add(p, 0, 0);
    } 
    
    public void keyPressed(KeyEvent ke) {
        System.out.println("touche appuyée");
        String touche = ke.getText();
        if (touche.compareTo("q")==0) {
            System.out.println("q appuyée");
        }
        if (touche.compareTo("d")==0) {
            System.out.println("d appuyée");
        }
        if (touche.compareTo("z")==0) {
            System.out.println("z appuyée");
        }
        if (touche.compareTo("s")==0) {
            System.out.println("s appuyée");
        }
    }
    
    public void setPlateau(Plateau p){
        
    }
}
