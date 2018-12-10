package GamePackage;

import java.util.LinkedList;
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pierre
 */
public class Case implements Serializable{
    
    private int num;
    private int test;
    
    public Case(){

    }

    public Case(int n){
        this.num = n;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    
    public int isCaseVideDown(Plateau p){
        int retour = 0;
        for(int x = 0;x<p.getTaille();x++){
            if(p.getPlateau()[x][p.getYCaseVide()].equals(this)){
                retour = p.getXCaseVide()-x;
            }
        }
        return retour;
    }
    public int isCaseVideUp(Plateau p){
        int retour = 0;
        for(int x = 0;x<p.getTaille();x++){
            if(p.getPlateau()[x][p.getYCaseVide()].equals(this)){
                retour = x-p.getXCaseVide();
            }
        }
        return retour;
    }
    public int isCaseVideAGauche(Plateau p){
        int retour = 0;
        for(int y = 0;y<p.getTaille();y++){
            if(p.getPlateau()[p.getXCaseVide()][y].equals(this)){
                retour = y-p.getYCaseVide();
            }
        }
        return retour;
    }
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
