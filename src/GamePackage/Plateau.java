package GamePackage;
import GamePackage.Case;
import IA.HeuristiqueA;
import java.io.Serializable;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;


/**
 * 
 * La class Plateau représente le corps du jeu et va contenir toute les méthodes
 * permetants d'initialiser le plateau de jeu, effectuer les déplacements , gerer la condition de fin,
 * et calculer le coéficient de désordre du taquin
 * 
 * @author Pierre
 */
public class Plateau extends Observable implements Serializable {
    private ArrayList<Integer> mouv = new ArrayList<Integer>();
    private int nb=-1;
    /**
     * La taille du taquin
     */
    private int taille;
    /**
     * La tableau de Case modélisant notre plateau de jeu
     */
    private Case plateau[][];
    /**
     * Les coordonnées de la case vide
     */
    private int xCaseVide,yCaseVide;
    /**
     * Le score du joueur
     */
    private int score=0;
    /**
     * Les secondes du Timer
     */
    private int sec = 0;
    /**
     * Les minutes du Timer
     */
    private int min = 0;
    /**
     * Référence a l'interface Heuristique
     */
    private HeuristiqueA heuristique;

    public Plateau() {
    }
    /**
     * Constructeur du Plteau
     * Initialise la taille, ainsi que le tableau de case de taille "Taille x Taille", pour l'instant vide
     * @see Plateau#taille
     * @see Plateau#plateau
     * @param t 
     *          Taille du plateau a initialiser
     */
    public Plateau(int t){
        this.taille = t;
        this.plateau= new Case[taille][taille];
    }
    
    public int getSec(){
        return this.sec;
    }
    
    public void setSec(int i){
        this.sec = i;
    }
    
    public int getMin(){
        return this.min;
    }
    
    public void setMin(int m){
        this.min=m;
    }
    /**
     * Initialise la taille , ainsi que le tableau de case de taille "Taille x Taille", puis lance
     * la méthode melange qui positionne les Cases dans ce tableau de façon aléatoire
     * @see Plateau#melange() 
     * 
     * @param t 
     *          taille du plateau
     */
    public void initialize(int t){
        this.taille = t;
        this.plateau= new Case[taille][taille];
        this.melange();
    }
    /**
     * Initialise le plateau dans l'ordre
     */
    public void intialisePlateauOrdre(){
        int k =1;
        for(int i=0;i<getTaille();i++){
            for (int j=0;j<getTaille();j++){
                getPlateau()[i][j]=new Case(k);
                k++;
            }
        }
        //case en bas a droite est la case vide
        getPlateau()[getTaille()-1][getTaille()-1].setNum(0);
        this.setXYcaseVide(getTaille()-1,getTaille()-1);
    }
    /**
     * Affiche le plateau de jeu dans la console
     */
    public void affichePlateauConsole(){
        for(int i=0;i<getTaille();i++){
            System.out.println();
            for (int j=0;j<getTaille();j++){
                if(getPlateau()[i][j].getNum()<10){
                    System.out.print("[0"+getPlateau()[i][j].getNum()+"]");
                }else{
                    System.out.print("["+getPlateau()[i][j].getNum()+"]");
                }
            }   
        }
        System.out.println();
    }
    /**
     * Attribut les valeur x et de y de la case vide
     * @param X
     *          Valeur X, ligne
     * @param Y 
     *          Valeur Y, Colonne
     */
    public void setXYcaseVide(int X, int Y){
        this.xCaseVide=X;
        this.yCaseVide=Y;
    }
    /**
     * Echange la case vide avec la case a ça droite via le biais d'une case tempon ( Simule un déplacement ),
     * Attribut les nouveau coordonné x et y de la case vide , poour toujours savoir ou elle est, et incrémente le score.
     * A la fin , on notifie a l'observateur (nottre calsse DocumentController) qu'une action a été fait et qu'il doit actualiser la vue
     * @see FXMLDocumentController#update(java.util.Observable, java.lang.Object) 
     * @see Observable#setChanged() 
     * @see Observable#notify() 
     * @return Retourne True si le mouvement a été fait, False s'il il na pas été fait
     */
    public boolean moveRight(){
        if(yCaseVide>0){
            Case tmp = getPlateau()[xCaseVide][yCaseVide];
            getPlateau()[xCaseVide][yCaseVide]=getPlateau()[xCaseVide][yCaseVide-1];
            getPlateau()[xCaseVide][yCaseVide-1]=tmp;
            this.setXYcaseVide(xCaseVide, yCaseVide-1);  
            score++;
            mouv.add(3);
            setChanged();
            notifyObservers();
            return true;
        }else{return false;}
    }
    /**
     * Echange la case vide avec la case a ça gauche via le biais d'une case tempon ( Simule un déplacement ),
     * Attribut les nouveau coordonné x et y de la case vide , poour toujours savoir ou elle est, et incrémente le score.
     * A la fin , on notifie a l'observateur (nottre calsse DocumentController) qu'une action a été fait et qu'il doit actualiser la vue
     * @see FXMLDocumentController#update(java.util.Observable, java.lang.Object) 
     * @see Observable#setChanged() 
     * @see Observable#notify() 
     * @return Retourne True si le mouvement a été fait, False s'il il na pas été fait
     */
    public boolean moveLeft(){
        if(yCaseVide<getTaille()-1){
            Case tmp = getPlateau()[xCaseVide][yCaseVide];
            getPlateau()[xCaseVide][yCaseVide]=getPlateau()[xCaseVide][yCaseVide+1];
            getPlateau()[xCaseVide][yCaseVide+1]=tmp;
            this.setXYcaseVide(xCaseVide, yCaseVide+1);
            score++;
            mouv.add(4);
        setChanged();
        notifyObservers();
            return true;
        }else{return false;}
    }
    /**
     * Echange la case vide avec la case au dessous via le biais d'une case tempon ( Simule un déplacement ),
     * Attribut les nouveau coordonné x et y de la case vide , poour toujours savoir ou elle est, et incrémente le score.
     * A la fin , on notifie a l'observateur (nottre calsse DocumentController) qu'une action a été fait et qu'il doit actualiser la vue
     * @see FXMLDocumentController#update(java.util.Observable, java.lang.Object) 
     * @see Observable#setChanged() 
     * @see Observable#notify() 
     * @return Retourne True si le mouvement a été fait, False s'il il na pas été fait
     */
    public boolean moveDown(){
        if(xCaseVide>0){
            Case tmp = getPlateau()[xCaseVide][yCaseVide];
            getPlateau()[xCaseVide][yCaseVide]=getPlateau()[xCaseVide-1][yCaseVide];
            getPlateau()[xCaseVide-1][yCaseVide]=tmp;
            this.setXYcaseVide(xCaseVide-1, yCaseVide);
            score++;
            mouv.add(2);
        setChanged();
        notifyObservers();
            return true;
        }else{return false;}
    }
    /**
     * Echange la case vide avec la case au dessus via le biais d'une case tempon ( Simule un déplacement ),
     * Attribut les nouveau coordonné x et y de la case vide , poour toujours savoir ou elle est, et incrémente le score.
     * A la fin , on notifie a l'observateur (nottre calsse DocumentController) qu'une action a été fait et qu'il doit actualiser la vue
     * @see FXMLDocumentController#update(java.util.Observable, java.lang.Object) 
     * @see Observable#setChanged() 
     * @see Observable#notify() 
     * @return Retourne True si le mouvement a été fait, False s'il il na pas été fait
     */
    public boolean moveUp(){
        if(xCaseVide<getTaille()-1){
            Case tmp = getPlateau()[xCaseVide][yCaseVide];
            getPlateau()[xCaseVide][yCaseVide]=getPlateau()[xCaseVide+1][yCaseVide];
            getPlateau()[xCaseVide+1][yCaseVide]=tmp;
            this.setXYcaseVide(xCaseVide+1, yCaseVide);  
            score++;
            mouv.add(1);
        setChanged();
        notifyObservers();
            return true;
        }else{return false;}
    }
    
    public boolean moveRightIA(){
        if(yCaseVide>0){
            Case tmp = getPlateau()[xCaseVide][yCaseVide];
            getPlateau()[xCaseVide][yCaseVide]=getPlateau()[xCaseVide][yCaseVide-1];
            getPlateau()[xCaseVide][yCaseVide-1]=tmp;
            this.setXYcaseVide(xCaseVide, yCaseVide-1);
            return true;
        }else{return false;}
    }
    public boolean moveLeftIA(){
        if(yCaseVide<getTaille()-1){
            Case tmp = getPlateau()[xCaseVide][yCaseVide];
            getPlateau()[xCaseVide][yCaseVide]=getPlateau()[xCaseVide][yCaseVide+1];
            getPlateau()[xCaseVide][yCaseVide+1]=tmp;
            this.setXYcaseVide(xCaseVide, yCaseVide+1);
            return true;
        }else{return false;}
    }
    public boolean moveDownIA(){
        if(xCaseVide>0){
            Case tmp = getPlateau()[xCaseVide][yCaseVide];
            getPlateau()[xCaseVide][yCaseVide]=getPlateau()[xCaseVide-1][yCaseVide];
            getPlateau()[xCaseVide-1][yCaseVide]=tmp;
            this.setXYcaseVide(xCaseVide-1, yCaseVide);
            return true;
        }else{return false;}
    }
    public boolean moveUpIA(){
        if(xCaseVide<getTaille()-1){
            Case tmp = getPlateau()[xCaseVide][yCaseVide];
            getPlateau()[xCaseVide][yCaseVide]=getPlateau()[xCaseVide+1][yCaseVide];
            getPlateau()[xCaseVide+1][yCaseVide]=tmp;
            this.setXYcaseVide(xCaseVide+1, yCaseVide);
            return true;
        }else{return false;}
    }
    /**
     * Calcul le coefficient de désordre du taquin
     * @return retourne le coefficient
     */
    public int coefDesordre(){
        int retour=0;
        //crée une liste de toute les Cases
        LinkedList<Case> list = new LinkedList<Case>();
        for(int i=0;i<getTaille();i++){
            for (int j=0;j<getTaille();j++){
                list.addLast(getPlateau()[i][j]);
            }}
        int k=0;
        for(int i=0;i<list.size();i++){
            k++;
            for(int j=k;j<list.size();j++){
                if(list.get(i).getNum()>list.get(j).getNum() && list.get(i).getNum()!=0 && list.get(j).getNum()!=0 ){
                    retour++;
                    //System.out.println(list.get(i).getNum()+" Avant "+list.get(j).getNum());
                }
            }
        }
        return retour;
    }
    /**
     * Demande a l'utilisateur le déplacement quil veut faire dans la console
     */
    public void choix(){
        Scanner sc = new Scanner(System.in); 
        System.out.println("A vous de jouer");
        String str = sc.nextLine();
        if(str.equals("q")){
            this.moveLeft();
        }
        if(str.equals("d")){
            this.moveRight();
        }
        if(str.equals("z")){
            this.moveDown();
        }
        if(str.equals("s")){
            this.moveUp();
        }   
    }
    /**
     * Intialise le tableu de jeu de façon aléatoire
     */
    public void melange(){
        //Crée une liste de int de 1 a taille*taille pour remplir le Plateau
        LinkedList<Integer> list = new LinkedList<Integer>();
        for(int i=1;i<getTaille()*getTaille();i++){
            list.add(i);
        }
        Random r = new Random();
        //a chaque case du tableau on attribut un numéro aléatoire choisie dans la liste
        //et on le retire pour pas avoir de doublon dans le plateau
        for(int i=0;i<getTaille();i++){
            for (int j=0;j<getTaille();j++){
                //si la liste est vide il reste que la case vide a placer en bas a droite
                if(list.size()!=0){
                    int k = r.nextInt(list.size());;     
                    getPlateau()[i][j]=new Case(list.get(k));
                    list.remove(k);
                }else{
                    getPlateau()[i][j]=new Case(0);
                    this.setXYcaseVide(getTaille()-1,getTaille()-1);
                }
                
            }   
        }
        //Si le coefficient de désordre n'est pas paire le taquin est impossible ou si c'est égale a 0 ce qui veux dire quil est dans l'ordre , on remélange.
        System.out.println(this.coefDesordre());
        if(this.coefDesordre()%2!=0 || this.coefDesordre()==0){
            this.melange();
        }
    }
    /**
     * Regarde si le jeu est fini , que les cases sont dans l'ordre
     * @return true si le jeu est fini, sinon false
     */
    public boolean isOver(){
        boolean retour = true;
        int k=1;
        for(int i=0;i<getTaille();i++){
            for (int j=0;j<getTaille();j++){
                //Vérifie si chaque case est a ça place
                if(getPlateau()[i][j].getNum()!=k && k!=getTaille()*getTaille()){
                    retour=false;
                }
            k++;
            }      
        }
        return retour;
    }
    /**
     * Pour l'IA
     * Ajoute dans une liste tout les états successeur a partir d'un état donné
     * @return Retourne cette liste
     */
    // Liste pour L'algorithme A* de l'IA.
    public ArrayList<Plateau> Successeurs() {

	ArrayList<Plateau> list = new ArrayList<Plateau>();
	if (this.moveUp()) {
            list.add(this);
            this.moveDown();
	}
	if (this.moveDown()) {
            list.add(this);
            this.moveUp();
	}
	if (this.moveLeft()) {
            list.add(this);
            this.moveRight();
	}
        if (this.moveRight()) {
            list.add(this);
            this.moveLeft();
	}
	return list;
    }
    
    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public Case[][] getPlateau() {
        return plateau;
    }

    public void setPlateau(Case[][] plateau) {
        this.plateau = plateau;
    }
    public int getScore(){
        return this.score;
    }
    public int getXCaseVide(){
        return this.xCaseVide;
    }
    public int getYCaseVide(){
        return this.yCaseVide;
    }
    public void setScore(int i){
        this.score = i;
    }
    
    public int heuristique() {
	return heuristique.heuristique(this.plateau, this.taille);
    }
    public void setheuristique(HeuristiqueA h) {
	this.heuristique = h;
    }
    /**
     * récupére le prochain mouvement a effectuer
     * @return 
     */
    int getNextMouv() {
        nb++;
        return this.mouv.get(nb);
    }
}
