
package GamePackage;

import Classement.Classement;
import GamePackage.Case;

import GamePackage.factoryThread;
import Login.FXMLFormulaireInscriptionController;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
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
public class FXMLDocumentController implements Initializable, Observer {
    
    
    //@FXML
    //private Button[][] GrilleBoutons = new Button[7][8];
    
    private Plateau plateau;
    @FXML
    private Label label;
    @FXML
    private GridPane grille;
    @FXML
    private Label lScore,lTimer,linfo;
    @FXML
    private Pane menuPane,taquinPane,classementPane; // Pane représentant le menu , et l'affichage du taquin
    @FXML
    private Pane connexionPane,notLogPane,inscriptionPane,logPane;
    @FXML
    private TextField UsernameField;
    @FXML
    private PasswordField PassWordField;
    @FXML
    private ChoiceBox tailleTaquin;
    ObservableList list = FXCollections.observableArrayList("2x2","3x3","4x4","5x5","6x6","7x7","8x8");
    @FXML
    private TableView tableClassement;
    //tableau qui contiendras les panes constituant le jeu pour l'affichage
    private Pane paneTab[][];
    private int enCour, nbThread;
    private ArrayList<Pane> tab;
    private float objectifX,objectifY;
    //boolean pour savoir si le joueur peux effectuer des mouvement ou non , pour éviter tous probléme
    private boolean canPlay=false;
    //permet d'activer ou desaciver laction des bouton pour éviter des bug/spam
    private boolean buttonActive=true;
    private double xOffset = 0;
    private double yOffset = 0;
    private Pane caseSelectionne=null;
    @FXML
    private Label labelTestAnimation;
    
    
    //Constructeur de la class
    public FXMLDocumentController(){
        System.out.println("Construct");
        this.plateau = new Plateau(); 
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialise");
        //menuPane.setVisible(true);
        //Gére le menu
        classementPane.setLayoutY(classementPane.getLayoutY()+415);
        tailleTaquin.getItems().addAll(list);
        tailleTaquin.setValue("4x4");
        //tailleTaquin.getValue() pour récupérer la valeur;
        
    } 
    //Event Pour bouger la fenétre
    @FXML
    private void OnMousePressed(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
    }
    //Event Pour bouger la fenétre
    @FXML
    private void OnMouseDrag(MouseEvent event) {
                Stage stage = (Stage) menuPane.getScene().getWindow();
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
    }
    @FXML
    private void HandleButtonLancerJeuAction(ActionEvent event){
        if(buttonActive){
        this.tab=new ArrayList<Pane>();
        this.enCour=0;
        this.nbThread=-1;
        plateau = new Plateau();
        if(!SerialiserPlateau.existePlateau()){
            String taille = tailleTaquin.getValue().toString().substring(0, 1); //récupère le premier nombre écrit dans le choiceBox (4 si 4x4 est selectionné etc)
            this.destroyPlateayView(); //Détruit l'ancienne partie si il y en avait une
            plateau.initialize(Integer.parseInt(taille)); //intiailise le plateau et la vue
            this.initializePlateauView();
            this.AnimMenuOut();
        }else{
            System.out.println("tewst");
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
                plateau.affichePlateauConsole();
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
        
        plateau.addObserver(this);
        }
        
    }
    //Event pour le bouton Close
    @FXML
    private void handleButtonClose(ActionEvent event) {
        SerialiserPlateau.enregistrerPlateau(plateau);
        Platform.exit();
        Jdbc.closeInstance();
    }
    //Sevent pour le bouton Home
    @FXML
    private void handleButtonHome(ActionEvent event){
        SerialiserPlateau.enregistrerPlateau(plateau);
        System.out.println("Home");
        
        this.AnimMenuIn();
    }
    //Action du bouton inscription sur le menu
    @FXML
    private void handleButtonConnexion(ActionEvent event){
        if(buttonActive){
        System.out.println("Connexion pressed");
        String regex = "[a-zA-Z0-9]+";
        System.out.println(UsernameField.getText().matches(regex));
        if((UsernameField.getText().matches(regex) && PassWordField.getText().matches(regex))==false){
            linfo.setText("Ereur de saisie");
        }else{
            linfo.setText("");
        }
        }
        /*notLogPane.setVisible(false);
        connexionPane.setVisible(true);*/
    }
    //Action du bouton connexion sur le menu
    @FXML
    private void handleButtonInscription(ActionEvent event){
        if(buttonActive){
        System.out.println("Inscription pressed");
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Login/FXMLFormulaireInscription.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLFormulaireInscriptionController controller = fxmlLoader.getController();
            controller.init(this);
            Stage stage = new Stage();
            Scene scene = new Scene(root1);
            stage.setScene(scene);  
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
        }
    }
    @FXML
    private void handleButtonRanking(ActionEvent event){
        if(buttonActive){
            this.initRank();
            this.AnimRankIn();
        }
    }
    @FXML
    private void handleButtonRankingBack(ActionEvent event){
        if(buttonActive){
            this.AnimRankOut();
        }
    }

    public void AnimMenuIn(){
        buttonActive=false;
        this.canPlay=false;
        TranslateTransition t = new TranslateTransition();
        t.setDuration(Duration.seconds(2));
        t.setNode(menuPane);
        t.setToY(0);
        t.play();
        t.setOnFinished((e)->{
            this.destroyPlateayView();
            buttonActive=true;
        }); 
    }
    public void AnimMenuOut(){
        buttonActive=false;
        TranslateTransition t = new TranslateTransition();
        t.setDuration(Duration.seconds(2));
        t.setNode(menuPane);
        t.setToY(-520);
        t.play();
        t.setOnFinished((e)->{
            this.canPlay=true;
            this.createTimer();
            buttonActive=true;
        }); 
    }
    public void AnimRankIn(){
        buttonActive=false;
        classementPane.setVisible(true);
        TranslateTransition t = new TranslateTransition();
        t.setDuration(Duration.seconds(1));
        t.setNode(classementPane);
        t.setToY(-415);
        t.play();
        t.setOnFinished((e)->{
            buttonActive=true;
        }); 
    }
    public void AnimRankOut(){
        buttonActive=false;
        TranslateTransition t = new TranslateTransition();
        t.setDuration(Duration.seconds(1));
        t.setNode(classementPane);
        t.setToY(0);
        t.play();
        t.setOnFinished((e)->{
            classementPane.setVisible(false);
            buttonActive=true;
        }); 
    }
    
    public void destroyPlateayView(){
        if (paneTab!=null){
            lTimer.setText("0 : 00");
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
        float x = 25;
        float y = 142;
        //objectifX et objectifY corresponde a l'emplacement de la case vide
        objectifX=x+(((float)350/taille)*(taille-1));
        objectifY=y+(((float)350/taille)*(taille-1));
        System.out.println(taille);
        for(int i=0;i<taille;i++){
            x=25;
            for(int j=0;j<taille;j++){
                if(plateau.getPlateau()[i][j].getNum()==0){
                    paneTab[i][j]=null;
                    objectifX=25+(((float)350/taille)*j);
                    objectifY=142+(((float)350/taille)*i);
                }else{
                    
                    //Crée une tuile et la place dans la fenétre
                    Pane p = new Pane();
                    p.setPrefSize((double)350/taille,(double)350/taille);
                    p.getStyleClass().add("case");
                    Label l = new Label();
                    l.getStyleClass().add("num");
                    p.getChildren().add(l);
                    //Set l'event du clique sur une tuile , pour gérer le déplacement de plusieur case avec les fléche
                    p.setOnMouseClicked((MouseEvent e) -> {
                        if(caseSelectionne==null){
                            caseSelectionne = (Pane)e.getSource();
                            caseSelectionne.getStyleClass().add("caseSelection");
                        }
                        else if(caseSelectionne!=null){
                            caseSelectionne.getStyleClass().remove(1);
                            caseSelectionne = (Pane)e.getSource();
                            caseSelectionne.getStyleClass().add("caseSelection");
                        }else if(caseSelectionne == ((Pane)e.getSource())){
                            caseSelectionne.getStyleClass().remove(1);
                            caseSelectionne = null;
                        }
                        
                        /*caseSelectionne = (Pane)e.getSource();
                        caseSelectionne.getStyleClass().add("caseSelection");*/
                    });
                    taquinPane.getChildren().add(p);
                    int num = this.plateau.getPlateau()[i][j].getNum();
                    l.setText(Integer.toString(num));
                    //Centre la label en fonction de la taille du pane
                    l.layoutXProperty().bind(p.widthProperty().divide(2).subtract(l.widthProperty().divide(2)));
                    l.layoutYProperty().bind(p.heightProperty().divide(2).subtract(l.heightProperty().divide(2)));
                    p.setLayoutX((double)x);
                    p.setLayoutY(y);
                    paneTab[i][j]=p;
                }
                //350 = taille du grid Pane
                x=x+((float)350/taille);
            }
            y=y+((float)350/taille);
        }
    }

    public void keyPressed(KeyEvent ke) {
        if(canPlay==true){
            
        System.out.println("touche appuyée");
        String touche = ke.getText();
        
        if (touche.compareTo("q")==0) {
            System.out.println("q appuyée");
            if(plateau.moveLeft()){
                //mouv.add(4);
            }
            //moveLeft();
        }
        if (touche.compareTo("d")==0) {
            System.out.println("d appuyée");
            if(plateau.moveRight()){
                //mouv.add(3);
            }
            //moveRight();
        }
        if (touche.compareTo("z")==0) {
            System.out.println("z appuyée");
            if(plateau.moveUp()){
                //mouv.add(1);
            }
            //moveUp();
        }
        if (touche.compareTo("s")==0) {
            System.out.println("s appuyée");
            if(plateau.moveDown()){
                //mouv.add(2);
            }
            //moveDown();
        }
        
        if (ke.getCode().toString() == "DOWN" ) {
            System.out.println("DOWN Pressed");
            //récupére la Case Selectionné
            Case c = chercheCase();
            //récupére le nombre de case a bouger si on peux
            int nbCaseToMove = c.isCaseVideDown(plateau);
            //fait les déplacements
            for(int i=0;i<nbCaseToMove;i++){
                if(plateau.moveDown()){
                    //mouv.add(2);
                }
                //moveDown();
            }   
        }
        if (ke.getCode().toString() == "UP" ) {
            System.out.println("UP pressed");
            Case c = chercheCase();
            int nbCaseToMove = c.isCaseVideUp(plateau);
            for(int i=0;i<nbCaseToMove;i++){
                if(plateau.moveUp()){
                    //mouv.add(1);
                }
                //moveUp();
            }   
        }
        if (ke.getCode().toString() == "LEFT" ) {
            System.out.println("LEFT pressed");
            Case c = chercheCase();
            int nbCaseToMove = c.isCaseVideAGauche(plateau);
            for(int i=0;i<nbCaseToMove;i++){
                if(plateau.moveLeft()){
                    //mouv.add(4);
                }
                //moveLeft();
            }   
        }
        if (ke.getCode().toString() == "RIGHT" ) {
            System.out.println("RIGHT pressed");
            Case c = chercheCase();
            int nbCaseToMove = c.isCaseVideADroite(plateau);
            for(int i=0;i<nbCaseToMove;i++){
                if(plateau.moveRight()){
                    //mouv.add(3);
                }
                //moveRight();
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
    
    public void moveLeft(){
        //récupére la pane a déplacer qui est a la place de la case vide dans le plateau , et la dépalce
        this.createThread(paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()],this.objectifX,this.objectifY,++nbThread);
        //met a jour l'objectif pour le prochain déplacement
        objectifX = (float)objectifX+((float)350/this.plateau.getTaille());
        //met a jour le tableau des Pane
        paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()-1]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
        paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;
    }
    public void moveRight(){
        this.createThread(paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()],this.objectifX,this.objectifY,++nbThread);
        objectifX = (float)objectifX-((float)350/this.plateau.getTaille());
        paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()+1]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
        paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;
    }
    public void moveDown(){
        this.createThread(paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()],this.objectifX,this.objectifY,++nbThread);
        objectifY =(float) objectifY-((float)350/this.plateau.getTaille());
        paneTab[this.plateau.getXCaseVide()+1][this.plateau.getYCaseVide()]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
        paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;

    }
    public void moveUp(){
        this.createThread(paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()],this.objectifX,this.objectifY,++nbThread);
        objectifY =(float) objectifY+((float)350/this.plateau.getTaille());
        paneTab[this.plateau.getXCaseVide()-1][this.plateau.getYCaseVide()]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
        paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;
            
}
    public void createThread(Pane p,float toX,float toY, int num){
        System.out.println(toX+"/"+toY);
        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue 
            //récupére la postion du pane a dépalacer
            float x=(float)p.getLayoutX();
            float y=(float)p.getLayoutY();
            //récupére les valeur de objetifX et objectifY car elle peuvent étre modifier en cours de thread pour déplacer une autre tuile si le jouer fait un autre déplacement avant que celui ci soit fini
            float objX=toX;
            float objY=toY;
            @Override
            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                if(tab.contains(p)){
                    while(enCour!=num){
                        Thread.sleep(1);
                    }
                }
                tab.add(p);
                x=(float)p.getLayoutX();
                y=(float)p.getLayoutY();
                objX=toX;
                objY=toY;
                while (x != objX || y != objY) { // si la tuile n'est pas à la place qu'on souhaite attendre en abscisse
                    if (x < objX) {
                        x += .25; // si on va vers la droite, on modifie la position de la tuile pixel par pixel vers la droite
                    } else if(x> objX){
                        x -= .25; // si on va vers la gauche, idem en décrémentant la valeur de x
                    } else if(y < objY){
                        y +=.25;
                    } else if(y >objY){
                        y-=.25;
                    }
                    // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            p.relocate(x, y); // on déplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif
                            p.setVisible(true);    
                        }
                    });
                    Thread.sleep(1);
                } // end while
                enCour++;
                tab.remove(p);
                return null; // la méthode call doit obligatoirement retourner un objet. Ici on n'a rien de particulier à retourner. Du coup, on utilise le type Void (avec un V majuscule) : c'est un type spécial en Java auquel on ne peut assigner que la valeur null
            } // end call

        };
        factoryThread.lancer(task);
        /*
        Thread th = new Thread(task); // on crée un contrôleur de Thread
        th.setDaemon(true); // le Thread s'exécutera en arrière-plan (démon informatique)
        th.start(); // et on exécute le Thread pour mettre à jour la vue (déplacement continu de la tuile horizontalement) 
    */
    }
    //Cherche la position du pane dans paneTab et retourne la Case associé dans le plateau qui est au méme coordonnés
    public Case chercheCase(){
        Case retour=null;
        int posX=-1;
        int poxY=-1;
        for(int i=0;i<plateau.getTaille();i++){
            for (int j=0;j<plateau.getTaille();j++){
                if(paneTab[i][j]==caseSelectionne){
                   retour = plateau.getPlateau()[i][j];
                }
            }
        }
        
        return retour;
    }
    public void createTimer(){
        Task task = new Task<Void>() {
            int minute = plateau.getMin();
            int seconde = plateau.getSec();
            @Override
            public Void call() throws Exception {
                while(canPlay){
                    if(seconde<60){
                        seconde+=1;
                        plateau.setSec(seconde);
                    }else{
                        minute+=1;
                        seconde=0;
                        plateau.setSec(seconde);
                        plateau.setMin(minute);
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

    @Override
    public void update(Observable o, Object arg) {
        int i = this.plateau.getNextMouv();
        switch(i){
            case 1:
                moveUp();
                break;
            case 2:
                moveDown();
                break;
            case 3:
                moveRight();
                break;
            case 4:
                moveLeft();
                break;
        }
    }
    
   //Initialise la tableau de classement 
   public void initRank(){
       System.out.println("initRank");
       //Classement de Test ( a enlever )
       Classement c = new Classement("1","billy","50",tableClassement);
       
       //il faudras créer un classement avec en entré la list des joueur et tableClassement
       //Classement c = new Classement(list,tableClassement);
       
   }
    
}