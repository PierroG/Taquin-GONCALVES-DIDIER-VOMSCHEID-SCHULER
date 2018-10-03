/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Button;
import java.awt.Color;
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
    
    
    //@FXML
    //private Button[][] GrilleBoutons = new Button[7][8];
    private Plateau plateau;
    @FXML
    private Label label;
    @FXML
    private GridPane grille;
    @FXML
    private Label lScore;
    @FXML
    private Pane fond; // panneau recouvrant toute la fenêtre
    
    private final Label lab = new Label("2");
    private final Pane pane = new Pane();
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    public FXMLDocumentController(){
        System.out.println("Construct");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inistialize");
       
        /*pane.getStyleClass().add("case");
        lab.getStyleClass().add("num");
        pane.getChildren().add(lab);
        fond.getChildren().add(pane);
        pane.setLayoutX(25);
        pane.setLayoutY(121);*/
        
        // TODO
        //this.initializePlateauView();
        /*Pane p = new Pane();
        Label l = new Label();
        //Titre
        label.getStyleClass().add("titre");
        
        //Case
        p.getStyleClass().add("case"); 
        p.getChildren().add(l);
        l.getStyleClass().add("num");
        l.setText("10");
        grille.add(p, 0, 0);
        */
    } 
    public void initializeButton(){
         for(int i = 0;i<plateau.getTaille();i++){
            for(int j = 0;j<plateau.getTaille();j++){
                
            }
         }
    }
    public void initializePlateauView(){
        int taille = this.plateau.getTaille();
        for(int i = 0;i<plateau.getTaille();i++){
            for(int j = 0;j<plateau.getTaille();j++){
                if(i==3 && i==j){
                    //Case Vide
                }else{
                    //Crée le visuelle d'une case 
                    
                    Pane p = new Pane();
                    p.getStyleClass().add("case");
                    Label l = new Label();
                    l.getStyleClass().add("num");
                    p.getChildren().add(l);  
                    
                    int num = this.plateau.getPlateau()[i][j].getNum();
                    l.setText(Integer.toString(num));
                    grille.add(p, j,i);
                    //or
                    //p.setLayoutX();
                    //p.setLayoutY();
                }
                
            }     
        }
    }
    public void keyPressed(KeyEvent ke) {
        
        System.out.println("touche appuyée");
        String touche = ke.getText();
        if (touche.compareTo("q")==0) {
            System.out.println("q appuyée");
            plateau.moveLeft();
        }
        if (touche.compareTo("d")==0) {
            System.out.println("d appuyée");
            plateau.moveRight();
        }
        if (touche.compareTo("z")==0) {
            System.out.println("z appuyée");
            plateau.moveDown();
        }
        if (touche.compareTo("s")==0) {
            System.out.println("s appuyée");
            plateau.moveUp();
        }
        lScore.setText(Integer.toString(plateau.getScore()));
        plateau.affichePlateauConsole();
    }
    
    public void setPlateau(Plateau p){
        this.plateau=p;
    }
    
    
    public void refreshView(){
        int taille = this.plateau.getTaille();
        for(int i = 0;i<plateau.getTaille();i++){
            for(int j = 0;j<plateau.getTaille();j++){
                /*if(grille.getChildren().get(1) instanceof Label){
                    grille.getChildren().get(1);
                }*/
                System.out.println(grille.getChildren().get(1));
                int num = this.plateau.getPlateau()[i][j].getNum();
            }
                
        }
    }
    
}
