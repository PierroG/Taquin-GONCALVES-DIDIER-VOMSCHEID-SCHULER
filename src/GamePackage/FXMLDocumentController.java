
package GamePackage;

import Classement.Classement;
import GamePackage.Case;

import GamePackage.factoryThread;
import IA.ActionIA;
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
    private Label lScore,lTimer,linfo,lUsername;
    @FXML
    private Pane menuPane,taquinPane,classementPane; // Pane représentant le menu , et l'affichage du taquin
    @FXML
    private Pane isLogPane,notLogPane;
    @FXML
    private TextField UsernameField;
    @FXML
    private PasswordField PassWordField;
    @FXML
    private ChoiceBox tailleTaquin,tailleTaquin2;
    ObservableList list = FXCollections.observableArrayList("2x2","3x3","4x4","5x5","6x6","7x7","8x8");
    @FXML
    private TableView tableClassement;
    //tableau qui contiendra les panes constituant le jeu pour l'affichage
    private Pane paneTab[][];
    private int enCour, nbThread;
    private ArrayList<Pane> tab;
    private float objectifX,objectifY;
    //boolean pour savoir si le joueur peux effectuer des mouvement ou non , pour eviter tous problemes
    private boolean canPlay=false;
    //permet d'activer ou desaciver l'action des bouton pour eviter des bug/spam
    private boolean buttonActive=true;
    private double xOffset = 0;
    private double yOffset = 0;
    private Pane caseSelectionne=null;
    private Classement rank = null;
    private boolean isConnect;
    private String Username;
    
    
    public FXMLDocumentController(){
        System.out.println("Construct");
        this.plateau = new Plateau(); 
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialise");
        //gere le menu
        classementPane.setLayoutY(classementPane.getLayoutY()+415);
        tailleTaquin.getItems().addAll(list);
        tailleTaquin.setValue("4x4");
        //tailleTaquin.getValue() pour recuperer la valeur;
        
    } 
    //Event pour bouger la fenetre
    @FXML
    private void OnMousePressed(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
    }
    //Event pour bouger la fenetre
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
            String taille = tailleTaquin.getValue().toString().substring(0, 1); //recupere le premier nombre ecrit dans le choiceBox (4 si 4x4 est selectionne etc)
            this.destroyPlateayView(); //detruit l'ancienne partie si il y en avait une
            plateau.initialize(Integer.parseInt(taille)); //intiailise le plateau et la vue
            this.initializePlateauView();
            this.AnimMenuOut();
        }else{
            System.out.println("test");
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
                String taille = tailleTaquin.getValue().toString().substring(0, 1); //recupere le premier nombre ecrit dans le choiceBox (4 si 4x4 est selectionné etc)
                this.destroyPlateayView(); //detruit l'ancienne partie si il y en avait une
                plateau.initialize(Integer.parseInt(taille)); //initialise la plateau et la vue
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
        if(!this.plateau.isOver()){
            SerialiserPlateau.enregistrerPlateau(plateau);
        }
        
        Platform.exit();
        Jdbc.closeInstance();
    }
    //Sevent pour le bouton Home
    @FXML
    private void handleButtonHome(ActionEvent event){
        if(!this.plateau.isOver()){
            SerialiserPlateau.enregistrerPlateau(plateau);
        }
        System.out.println("Accueil");
        
        this.AnimMenuIn();
    }
    //Action du bouton inscription sur le menu
    @FXML
    private void handleButtonConnexion(ActionEvent event){
        if(buttonActive){
        System.out.println("Connexion pressed");
        String regex = "[a-zA-Z0-9]+";
        if((UsernameField.getText().matches(regex) && PassWordField.getText().matches(regex))==false){
            linfo.setText("Erreur de saisie");
        }else{
            
            String s = Jdbc.connexion(UsernameField.getText(),PassWordField.getText());
            if(s!=null){
                linfo.setText("Bienvenue "+s);
                lUsername.setText(s);
                notLogPane.setVisible(false);
                isLogPane.setVisible(true);
                isConnect=true;
                Username = s;
            }else{
                linfo.setText("Le compte n'existe pas");
            }
        }
        }

    }
    //action du bouton s'inscrire , lance le formulaire d'inscription
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
    private void handleButtonDisconnect(ActionEvent event){
        if(buttonActive){
            System.out.println("Disconect");
            notLogPane.setVisible(true);
            isLogPane.setVisible(false);
            isConnect=false;
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
        UsernameField.setDisable(false);
        PassWordField.setDisable(false);
        tailleTaquin.setDisable(false);
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
        UsernameField.setDisable(true);
        PassWordField.setDisable(true);
        tailleTaquin.setDisable(true);
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
        //objectifX et objectifY correspondent a l'emplacement de la case vide
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
                    
                    //cree une tuile au bonne dimension, set son style,  et la place dans la fenetre
                    Pane p = new Pane();
                    p.setPrefSize((double)350/taille,(double)350/taille);
                    p.getStyleClass().add("case");
                    Label l = new Label();
                    l.getStyleClass().add("num");
                    p.getChildren().add(l);
                    //Set l'event du clique sur une tuile , pour gérer le deplacement de plusieurs cases avec les fleches
                    p.setOnMouseClicked((MouseEvent e) -> {
                        if(caseSelectionne==null){
                            System.out.println("Selec First Time");
                            caseSelectionne = (Pane)e.getSource();
                            caseSelectionne.getStyleClass().add("caseSelection");
                        }
                        else if(caseSelectionne == ((Pane)e.getSource())){
                            System.out.println("Deselec ");
                            caseSelectionne.getStyleClass().remove(1);
                            caseSelectionne = null;
                        }
                        else if(caseSelectionne!=null){
                            System.out.println("Selec ");
                            caseSelectionne.getStyleClass().remove(1);
                            caseSelectionne = (Pane)e.getSource();
                            caseSelectionne.getStyleClass().add("caseSelection");
                        
              
                        }});
                    taquinPane.getChildren().add(p);
                    int num = this.plateau.getPlateau()[i][j].getNum();
                    l.setText(Integer.toString(num));
                    //centre le label en fonction de la taille du pane
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
        
        ActionIA ia = new ActionIA();
        if (touche.compareTo("n")==0) {
            ia.IAPerformed(plateau);
        }
        
        if (ke.getCode().toString() == "DOWN" ) {
            System.out.println("DOWN Pressed");
            //recupere la case selectionne
            Case c = chercheCase();
            //recupere le nombre de case a bouger si on peut
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
            System.out.println("Bien joué tu as fini");
            this.canPlay=false;
            endEvent();
        }
        }
    }
    
    public void moveLeft(){
        //récupère les coordonnées de la case vide
        int x = this.plateau.getXCaseVide();
        int y = this.plateau.getYCaseVide();
        //crée le Thread pour l'affichage du déplacement, avec en entrée , le pane a déplacé qui se situe au coordonnée de la case vide dans le plateau, l'ojectif , les coordonnée a atteindre pour le pane
        this.createThread(paneTab[x][y],this.objectifX,this.objectifY,++nbThread);
        //met a jour l'objectif pour le prochain deplacement = l'ancien objectif - la taille d'un pane
        objectifX = (float)objectifX+((float)350/this.plateau.getTaille());
        //met a jour le tableau des panes
        paneTab[x][y-1]=paneTab[x][y];
        paneTab[x][y]=null;
    }
    public void moveRight(){
        int x = this.plateau.getXCaseVide();
        int y = this.plateau.getYCaseVide();
        this.createThread(paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()],this.objectifX,this.objectifY,++nbThread);
        objectifX = (float)objectifX-((float)350/this.plateau.getTaille());
        paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()+1]=paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()];
        paneTab[this.plateau.getXCaseVide()][this.plateau.getYCaseVide()]=null;
    }
    public void moveDown(){
        int x = this.plateau.getXCaseVide();
        int y = this.plateau.getYCaseVide();
        this.createThread(paneTab[x][y],this.objectifX,this.objectifY,++nbThread);
        objectifY =(float) objectifY-((float)350/this.plateau.getTaille());
        paneTab[x+1][y]=paneTab[x][y];
        paneTab[x][y]=null;
    }
    public void moveUp(){
        int x = this.plateau.getXCaseVide();
        int y = this.plateau.getYCaseVide();
        this.createThread(paneTab[x][y],this.objectifX,this.objectifY,++nbThread);
        objectifY =(float) objectifY+((float)350/this.plateau.getTaille());
        paneTab[x-1][y]=paneTab[x][y];
        paneTab[x][y]=null;
            
}
    public void createThread(Pane p,float toX,float toY, int num){
        System.out.println(toX+"/"+toY);
        Task task = new Task<Void>() { // on definit une tache parallele pour mettre a jour la vue 
            //recupere la position du pane a deplacer
            float x=(float)p.getLayoutX();
            float y=(float)p.getLayoutY();
            //recupere les valeurs de objetifX et objectifY car elles peuvent etre modifiees en cours de thread pour deplacer une autre tuile si le joueur fait un autre déplacement avant que celui ci soit fini
            float objX=toX;
            float objY=toY;
            @Override
            public Void call() throws Exception { // implementation de la méthode protected abstract V call() dans la classe Task
                int attente;
                if(tab.contains(p)){
                    //enCour++;
                    while(enCour!=num){
                    //while(tab.contains(p)){
                    //System.out.println("tetetetete");
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
                        if(objX-x <0.25){
                            x += objX-x;
                        }else{
                            x += .25; // si on va vers la droite, on modifie la position de la tuile pixel par pixel vers la droite
                        }
                        
                    } else if(x> objX){
                        if(x-objX <0.25){
                            x -= objX-x;
                        }else{
                            x -= .25; // si on va vers la gauche, idem en décrémentant la valeur de x
                        }
                        
                    } else if(y < objY){
                        if(objY-y <0.25){
                            y += objY-y;
                        }else{
                            y +=.25;
                        }
                        
                    } else if(y >objY){
                        if(y-objY <0.25){
                            y -= objY-y;
                        }else{
                            y-=.25;
                        }
                        
                    }
                    // Platform.runLater est necessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            p.relocate(x, y); // on deplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif
                            p.setVisible(true);    
                        }
                    });
                    Thread.sleep(1);
                } // end while
                while(enCour!=(num)){
                System.out.println(enCour + " : " + num);
                }
                enCour++;
               // System.out.println(enCour + " : " + num);
                tab.remove(p);
                return null; // la methode call doit obligatoirement retourner un objet. Ici on n'a rien de particulier a retourner. Du coup, on utilise le type Void (avec un V majuscule) : c'est un type special en Java auquel on ne peut assigner que la valeur null
            } // end call

        };
        factoryThread.lancer(task);
        /*
        Thread th = new Thread(task); // on cree un controleur de Thread
        th.setDaemon(true); // le Thread s'executera en arriere-plan (demon informatique)
        th.start(); // et on execute le Thread pour mettre a jour la vue (deplacement continu de la tuile horizontalement) 
    */
    }
    //Cherche la position du paneSelectionne dans paneTab et retourne la Case associee dans le plateau qui est aux memes coordonnees
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
        if(isConnect){
            this.addScore();
        }
        
        SerialiserPlateau.EffacerPlateau();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Bravo!");
        alert.show();
        //Vérif positon dans classement
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
       if(rank==null){
           tailleTaquin2.getItems().addAll("2","3","4","5","6","7","8");
           tailleTaquin2.setValue("4");
           //Crée le classement
           rank = new Classement(tableClassement,tailleTaquin2.getValue().toString());
           //Initialise la choiceBox de la taille
       }
    }   
    @FXML
    private void HandleButtonPersoRankBestTimer(ActionEvent event){
        if(isConnect){ 
            rank.initPersonalTimerRankTab(tableClassement,tailleTaquin2.getValue().toString(),this.Username);
        }else{
            rank.clear();
            tableClassement.setPlaceholder(new Label("Vous devez être connecté"));
            
        }
    }
    @FXML
    private void HandleButtonRankBestTimer(ActionEvent event){
        rank.initTimerRankTab(tableClassement,tailleTaquin2.getValue().toString());
    }
    @FXML
    private void HandleButtonRankBestScore(ActionEvent event){
        rank.initScoreRankTab(tableClassement,tailleTaquin2.getValue().toString());
    }
    @FXML
    private void HandleButtonPersoRankBestScore(ActionEvent event){
        if(isConnect){
            rank.initPersonalScoreRankTab(tableClassement,tailleTaquin2.getValue().toString(),this.Username);
        }else{
            rank.clear();
            tableClassement.setPlaceholder(new Label("Vous devez être connecté"));
            
        }
        
    }
       
    public void addScore(){
        String id =Jdbc.getIdByName(Username);
        boolean b = Jdbc.scoreReq(plateau.getSec(), plateau.getMin(), plateau.getScore(), 0 , id, plateau.getTaille());    
    }
    
}
