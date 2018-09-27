
import static java.lang.Math.random;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pierre
 */
public class Plateau {
    
    private int taille;
    private Case plateau[][];
    private int xCaseVide,yCaseVide;
    
    public Plateau(){
        
    }
    
    public Plateau(int t){
        this.taille = t;
        this.plateau= new Case[taille][taille];
        
    }
    
    public void intialisePlateauOrdre(){
        int k =1;
        for(int i=0;i<taille;i++){
            for (int j=0;j<taille;j++){
                plateau[i][j]=new Case(k);
                k++;
            }
        }
        //case en bas a droite est la case vide
        plateau[taille-1][taille-1].setNum(0);
        this.setXYcaseVide(taille-1,taille-1);
    }
    public void affichePlateauConsole(){
        for(int i=0;i<taille;i++){
            System.out.println();
            for (int j=0;j<taille;j++){
                if(plateau[i][j].getNum()<10){
                    System.out.print("[0"+plateau[i][j].getNum()+"]");
                }else{
                    System.out.print("["+plateau[i][j].getNum()+"]");
                }
            }   
        }
        System.out.println();
    }
    public void setXYcaseVide(int X, int Y){
        this.xCaseVide=X;
        this.yCaseVide=Y;
    }
    
    public void moveRight(){
        if(yCaseVide>0){
            Case tmp = plateau[xCaseVide][yCaseVide];
        plateau[xCaseVide][yCaseVide]=plateau[xCaseVide][yCaseVide-1];
        plateau[xCaseVide][yCaseVide-1]=tmp;
        this.setXYcaseVide(xCaseVide, yCaseVide-1);  
    }}
    public void moveLeft(){
        if(yCaseVide<taille-1){
            Case tmp = plateau[xCaseVide][yCaseVide];
        plateau[xCaseVide][yCaseVide]=plateau[xCaseVide][yCaseVide+1];
        plateau[xCaseVide][yCaseVide+1]=tmp;
        this.setXYcaseVide(xCaseVide, yCaseVide+1);  
    }}
    public void moveUp(){
        if(xCaseVide>0){
            Case tmp = plateau[xCaseVide][yCaseVide];
        plateau[xCaseVide][yCaseVide]=plateau[xCaseVide-1][yCaseVide];
        plateau[xCaseVide-1][yCaseVide]=tmp;
        this.setXYcaseVide(xCaseVide-1, yCaseVide);  
    }}
    public void moveDown(){
        if(xCaseVide<taille-1){
            Case tmp = plateau[xCaseVide][yCaseVide];
        plateau[xCaseVide][yCaseVide]=plateau[xCaseVide+1][yCaseVide];
        plateau[xCaseVide+1][yCaseVide]=tmp;
        this.setXYcaseVide(xCaseVide+1, yCaseVide);  
    }}
    
    public int coefDesordre(){
        int retour=0;
        //crée une liste de toute les Cases
        LinkedList<Case> list = new LinkedList<Case>();
        for(int i=0;i<taille;i++){
            for (int j=0;j<taille;j++){
                list.addLast(plateau[i][j]);
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
    
    public void melange(){
        //Crée une liste de int de 1 a taille*taille pour remplir le Plateau
        LinkedList<Integer> list = new LinkedList<Integer>();
        for(int i=1;i<taille*taille;i++){
            list.add(i);
        }
        Random r = new Random();
        //a chaque case du tableau on attribut un numéro aléatoire choisie dans la liste
        //et on le retire pour pas avoir de doublon dans le plateau
        for(int i=0;i<taille;i++){
            for (int j=0;j<taille;j++){
                //si la liste est vide il reste que la case vide a placer en bas a droite
                if(list.size()!=0){
                    int k = r.nextInt(list.size());;     
                    plateau[i][j]=new Case(list.get(k));
                    list.remove(k);
                }else{
                    plateau[i][j]=new Case(0);
                    this.setXYcaseVide(taille-1,taille-1);
                }
                
            }   
        }
        //Si le coefficient de désordre n'est pas paire le taquin est impossible donc on le remélange
        System.out.println(this.coefDesordre());
        if(this.coefDesordre()%2!=0){
            this.melange();
        }
    }
    public boolean isOver(){
        boolean retour = true;
        int k=1;
        for(int i=0;i<taille;i++){
            for (int j=0;j<taille;j++){
                //Vérifie si chaque case est a ça place
                if(plateau[i][j].getNum()!=k && k!=taille*taille){
                    retour=false;
                }
            k++;
            }      
        }
        return retour;
    }
}
