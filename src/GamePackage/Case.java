package GamePackage;

import java.util.LinkedList;
import java.io.Serializable;


/**
 * La class Case représente une case dans notre taquin
 * 
 * Elle est représenté par un numéro
 * 
 * @author Pierre
 */
public class Case implements Serializable{
    
    /**
     * Le Numéro de la case
     * @see Case#setNum(int) 
     * @see Case#getNum() 
     */
    private int num;
    
    public Case(){

    }
    /**
     * Le Constructeur de la classe
     * @param n 
     *          Le numéro a attribuer
     */
    public Case(int n){
        this.num = n;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    /**
     * Permet de savoir si la case vide est aligné en dessous de cette case
     * @param p
     *          Le Palteau de jeu
     * @return retourne le nombre de case qui sépare la case vide a cette case, 0 si la case vide n'est pas aligner en dessous
     */
    public int isCaseVideDown(Plateau p){
        int retour = 0;
        for(int x = 0;x<p.getTaille();x++){
            if(p.getPlateau()[x][p.getYCaseVide()].equals(this)){
                retour = p.getXCaseVide()-x;
            }
        }
        return retour;
    }
    /**
     * Permet de savoir si la case vide est aligné au dessus de cette case
     * @param p
     *          Le Palteau de jeu
     * @return retourne le nombre de case qui sépare la case vide a cette case, 0 si la case vide n'est pas aligner au dessus
     */ 
    public int isCaseVideUp(Plateau p){
        int retour = 0;
        for(int x = 0;x<p.getTaille();x++){
            if(p.getPlateau()[x][p.getYCaseVide()].equals(this)){
                retour = x-p.getXCaseVide();
            }
        }
        return retour;
    }
    /**
     * Permet de savoir si la case vide est aligné a gauche de cette case
     * @param p
     *          Le Palteau de jeu
     * @return retourne le nombre de case qui sépare la case vide a cette case, 0 si la case vide n'est pas aligner a gauche
     */ 
    public int isCaseVideAGauche(Plateau p){
        int retour = 0;
        for(int y = 0;y<p.getTaille();y++){
            if(p.getPlateau()[p.getXCaseVide()][y].equals(this)){
                retour = y-p.getYCaseVide();
            }
        }
        return retour;
    }
    /**
     * Permet de savoir si la case vide est aligné a droite de cette case
     * @param p
     *          Le Palteau de jeu
     * @return retourne le nombre de case qui sépare la case vide a cette case, 0 si la case vide n'est pas aligner a droite
     */
    public int isCaseVideADroite(Plateau p){
        int retour = 0;
        for(int y = 0;y<p.getTaille();y++){
            if(p.getPlateau()[p.getXCaseVide()][y].equals(this)){
                retour = p.getYCaseVide()-y;
            }
        }
        return retour;

    } 
}
