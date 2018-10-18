/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

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
    private Pane menuPane; // Pane représentant le menu
    @FXML
    private Pane taquinPane; // Pane représentant l'afficahge du jeu
    ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
    "First", "Second", "Third"));
    
    @FXML
    private ChoiceBox tailleTaquin;
    ObservableList list = FXCollections.observableArrayList("2x2","3x3","4x4","5x5","6x6","7x7","8x8");
    //tableau qui contiendras les panes constituant le jeu
    private Pane paneTab[][];
    
    private int objectifX; private int objectifY;
    private boolean canPlay=false;
    
    private final Label lab = new Label("2");
    private final Pane pane = new Pane();
    
    
    @FXML
    private Label labelTestAnimation;
    
    @FXML
    private void HandleButtonLancerJeuAction(ActionEvent event){
        System.out.println("lance le jeu");
        String test = tailleTaquin.getValue().toString().substring(0, 1);
        System.out.println(test);

        plateau.initialize(Integer.parseInt(test));
        
            this.initializePlateauView2();
            this.testAnimation();
        
    }
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        
    }
    @FXML
    private void handleButtonHome(ActionEvent event){
        System.out.println("Home");
        this.destroyPlateayView();
        taquinPane.setVisible(false);
        menuPane.setVisible(true);
    }
    public FXMLDocumentController(){
        System.out.println("Construct");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inistialize");
        //menuPane.setVisible(true);
        //Gére le menu
        tailleTaquin.getItems().addAll(list);
        tailleTaquin.setValue("4x4");
        //tailleTaquin.getValue() pour récupérer la valeur;
        
    } 
    public void testAnimation(){
        TranslateTransition t = new TranslateTransition();
        t.setDuration(Duration.seconds(1));
        t.setNode(labelTestAnimation);
        t.setToY(-70);
        
        t.play();
        t.setOnFinished((e)->{
            this.canPlay=true;
            menuPane.setVisible(false);
            taquinPane.setVisible(true);
        });
    }
    public void destroyPlateayView(){
        if (paneTab!=null){
            for(int i=0;i<this.plateau.getTaille();i++){
                for(int j=0;j<this.plateau.getTaille();j++){
                    taquinPane.getChildren().remove(this.paneTab[i][j]);
                }
            }
        }
    }
    public void initializePlateauView2(){
        int taille=this.plateau.getTaille();
        this.paneTab = new Pane[taille][taille];
        int x = 25;
        int y = 121;
        //objectifX et objectifY corresponde a l'emplacement de la case vide
        objectifX=25+((350/taille)*(taille-1));
        objectifY=121+((350/taille)*(taille-1));
        System.out.println(taille);
        for(int i=0;i<taille;i++){
            x=25;
            for(int j=0;j<taille;j++){
                if(i==taille-1 && j==taille-1){
                    paneTab[i][j]=null;
                }else{
                    //Crée une tuile et la place dans la fenétre
                    Pane p = new Pane();
                    p.setPrefSize(350/taille,350/taille);
                    p.getStyleClass().add("case");
                    Label l = new Label();
                    l.getStyleClass().add("num");
                    p.getChildren().add(l);
                    taquinPane.getChildren().add(p);
                    int num = this.plateau.getPlateau()[i][j].getNum();
                    l.setText(Integer.toString(num));
                    p.setLayoutX(x);
                    p.setLayoutY(y);
                    paneTab[i][j]=p;
                }
                //350 = taille du grid Pane
                x=x+(350/taille);
            }
            y=y+(350/taille);
        }
    }

    public void keyPressed(KeyEvent ke) {
        if(canPlay==true){
            
        System.out.println("touche appuyée");
        String touche = ke.getText();
        
        if (touche.compareTo("q")==0) {
            System.out.println("q appuyée");
            //effectu le mouvement dans le plateau
            if(plateau.moveLeft()){
                //effectu le mouvement dans la vue
                //récupére la pane a déplacer qui est a la place de la case vide dans le plateau , et la dépalce
                
                this.createThread(paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()],this.objectifX,this.objectifY);
                objectifX = objectifX+(350/this.plateau.getTaille());
                //paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()].relocate(objectifX,objectifY);
            
                paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()-1]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
                paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;

                //mets a jours l'objectifX pour le prochain déplacement
                //objectifX = objectifX+(350/this.plateau.getTaille());
            }
            
        }
        if (touche.compareTo("d")==0) {
            System.out.println("d appuyée");
            if(plateau.moveRight()){
                
                this.createThread(paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()],this.objectifX,this.objectifY);
                objectifX = objectifX-(350/this.plateau.getTaille());
                //déplacement sans thread
                //paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()].relocate(objectifX,objectifY);
            
                paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()+1]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
                paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;
            
                //objectifX = objectifX-(350/this.plateau.getTaille());
            
            }
        }
        if (touche.compareTo("z")==0) {
            System.out.println("z appuyée");
            if(plateau.moveDown()){
                
                this.createThread(paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()],this.objectifX,this.objectifY);
                objectifY = objectifY+(350/this.plateau.getTaille());
                //paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()].relocate(objectifX,objectifY);
            
                paneTab[this.plateau.getXCaseVide()-1][this.plateau.getYCaseVide()]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
                paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;
            
                //objectifY = objectifY+(350/this.plateau.getTaille());
            }
        }
        if (touche.compareTo("s")==0) {
            System.out.println("s appuyée");
            if(plateau.moveUp()){
                
                this.createThread(paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()],this.objectifX,this.objectifY);
                objectifY = objectifY-(350/this.plateau.getTaille());
                //paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()].relocate(objectifX,objectifY);
                
                paneTab[this.plateau.getXCaseVide()+1][this.plateau.getYCaseVide()]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
                paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;
                
                //objectifY = objectifY-(350/this.plateau.getTaille());
            }
        }
        lScore.setText(Integer.toString(plateau.getScore()));
        plateau.affichePlateauConsole();
        if(this.plateau.isOver()){
            System.out.println("Bien ouéj ta fini");
            this.canPlay=false;
            endEvent();
        }
        }
    }
    public void createThread(Pane p,int toX,int toY){
        
        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
            //récupére la postion du pane a dépalacer
            int x=(int)Math.round(p.getLayoutX());
            int y=(int)Math.round(p.getLayoutY());
            //récupére les valeur de objetifX et objectifY car elle peuvent étre modifier en cours de thread pour déplacer une autre tuile si le jouer fait un autre déplacement avant que celui ci soit fini
            int objX=toX;
            int objY=toY;
            @Override
            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                
                while (x != objX || y != objY) { // si la tuile n'est pas à la place qu'on souhaite attendre en abscisse
                    if (x < objX) {
                        x += 1; // si on va vers la droite, on modifie la position de la tuile pixel par pixel vers la droite
                    } else if(x> objX){
                        x -= 1; // si on va vers la gauche, idem en décrémentant la valeur de x
                    } else if(y < objY){
                        y +=1;
                    } else if(y >objY){
                        y-=1;
                    }
                    // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            p.relocate(x, y); // on déplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif
                            p.setVisible(true);    
                        }
                    }
                    );
                    Thread.sleep(5);
                } // end while
                return null; // la méthode call doit obligatoirement retourner un objet. Ici on n'a rien de particulier à retourner. Du coup, on utilise le type Void (avec un V majuscule) : c'est un type spécial en Java auquel on ne peut assigner que la valeur null
            } // end call

        };
        Thread th = new Thread(task); // on crée un contrôleur de Thread
        th.setDaemon(true); // le Thread s'exécutera en arrière-plan (démon informatique)
        th.start(); // et on exécute le Thread pour mettre à jour la vue (déplacement continu de la tuile horizontalement)
    }    
    public void endEvent(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Bien ouéj");
        alert.show();
    }
    public void setPlateau(Plateau p){
        this.plateau=p;
    }
    
    
   
    
}
