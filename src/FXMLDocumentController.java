/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
    private Label lScore,lTimer;
    @FXML
    private Pane menuPane,taquinPane; // Pane représentant le menu , et l'affichage du taquin
    @FXML
    private ChoiceBox tailleTaquin;
    ObservableList list = FXCollections.observableArrayList("2x2","3x3","4x4","5x5","6x6","7x7","8x8");
    
    //tableau qui contiendras les panes constituant le jeu pour l'affichage
    private Pane paneTab[][];
    
    private int objectifX,objectifY;
    //boolean pour savoir si le joueur peux effectuer des mouvement ou non , pour éviter tous probléme
    private boolean canPlay=false;
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private Label labelTestAnimation;
    
    
    //Constructeur de la class
    public FXMLDocumentController(){
        System.out.println("Construct");
        this.plateau = new Plateau(); 
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
    @FXML
    private void OnMousePressed(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
    }
    @FXML
    private void OnMouseDrag(MouseEvent event) {
                Stage stage = (Stage) menuPane.getScene().getWindow();
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
    }
    @FXML
    private void HandleButtonLancerJeuAction(ActionEvent event){
        if(!SerialiserPlateau.existePlateau()){
            String taille = tailleTaquin.getValue().toString().substring(0, 1); //récupére le premier nombre écrit dans le choiceBox (4 si 4x4 est selectionné etc)
            this.destroyPlateayView(); //Détruit l'ancienne partie si il y en avais une
            plateau.initialize(Integer.parseInt(taille)); //intiailise la plateau est la vue
            this.initializePlateauView();
            this.AnimMenuOut();
        }else{
            //si il existe une partie en cours, une boite de dialogue est cree 
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Souhaitez-vous continuer la partie en cours ?");
            ButtonType buttonOui = new ButtonType("Oui");
            ButtonType buttonNon = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonOui, buttonNon);
            Optional<ButtonType> result = alert.showAndWait();
            //si on clique sur le bouton oui alors on recharge la partie precedente
            if (result.get() == buttonOui){
                plateau = SerialiserPlateau.RecupPlateau();
                this.initializePlateauView();
                this.AnimMenuOut();
            }
            //sinon on cree une nouvelle partie
            else {
                String taille = tailleTaquin.getValue().toString().substring(0, 1); //récupére le premier nombre écrit dans le choiceBox (4 si 4x4 est selectionné etc)
                this.destroyPlateayView(); //Détruit l'ancienne partie si il y en avais une
                plateau.initialize(Integer.parseInt(taille)); //intiailise la plateau est la vue
                this.initializePlateauView();
                this.AnimMenuOut();
            }
        }
        
        
    }
    @FXML
    private void handleButtonClose(ActionEvent event) {
        Platform.exit();
    }
    @FXML
    private void handleButtonHome(ActionEvent event){
        System.out.println("Home");
        
        this.AnimMenuIn();
    }

    public void AnimMenuIn(){
        this.canPlay=false;
        TranslateTransition t = new TranslateTransition();
        t.setDuration(Duration.seconds(2.5));
        t.setNode(menuPane);
        t.setToY(0);
        t.play();
        t.setOnFinished((e)->{
            this.destroyPlateayView();
        }); 
    }
    public void AnimMenuOut(){
        TranslateTransition t = new TranslateTransition();
        t.setDuration(Duration.seconds(2.5));
        t.setNode(menuPane);
        t.setToY(-520);
        t.play();
        t.setOnFinished((e)->{
            this.canPlay=true;
            this.createTimer();
        }); 
    }
    public void destroyPlateayView(){
        if (paneTab!=null){
            lScore.setText("0");
            plateau.setScore(0);
            for(int i=0;i<this.plateau.getTaille();i++){
                for(int j=0;j<this.plateau.getTaille();j++){
                    taquinPane.getChildren().remove(this.paneTab[i][j]);
                }
            }
        }
    }
    public void initializePlateauView(){
        SerialiserPlateau.enregistrerPlateau(plateau);
        lScore.setText(plateau.getScore() + "");
        int taille=this.plateau.getTaille();
        this.paneTab = new Pane[taille][taille];
        int x = 25;
        int y = 142;
        //objectifX et objectifY corresponde a l'emplacement de la case vide
        objectifX=x+((350/taille)*(taille-1));
        objectifY=y+((350/taille)*(taille-1));
        System.out.println(taille);
        for(int i=0;i<taille;i++){
            x=25;
            for(int j=0;j<taille;j++){
                if((this.plateau.getPlateau()[i][j]).getNum()==0){
                    paneTab[i][j]=null;
                    objectifX=25+((350/taille)*j);       
                    objectifY=142+((350/taille)*i);
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

    public void keyPressed(KeyEvent ke) throws InterruptedException {
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
                    //met a jour l'objectif pour le prochain déplacement
                    objectifX = objectifX+(350/this.plateau.getTaille());
                    //paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()].relocate(objectifX,objectifY);

                    paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()-1]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
                    paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;


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
                System.out.println("Félicitation, vous avez gagné !");
                SerialiserPlateau.EffacerPlateau();
                this.canPlay=false;
                endEvent();
            }else{
                SerialiserPlateau.enregistrerPlateau(plateau);
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
    public void createTimer(){
        Task task = new Task<Void>() {
            int minute = plateau.getChronoMin();
            int seconde = plateau.getChronoSec();
            @Override
            public Void call() throws Exception {
                while(canPlay){
                    if(seconde<60){
                        seconde+=1;
                        plateau.setChronoSec(seconde);
                    }else{
                        minute+=1;
                        seconde=0;
                        plateau.setChronoSec(seconde);
                        plateau.setChronoMin(minute);
                    }
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            if(seconde<10){
                                lTimer.setText(minute+" : 0"+seconde);
                            }else{
                                lTimer.setText(minute+" : "+seconde);
                            }
                            
                        }
                    }
                    );
                    Thread.sleep(1000);
                }
                return null;
            }
        }; 
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
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
