/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classement;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author pg540
 */
public class Player {
    
    private String rank;
    private String name;
    private String score;
    
    public Player(String R,String N,String S){
        this.rank= R;
        this.name=N;
        this.score=S;
    }
    
    
    public void affiche(){
        System.out.println(this.rank+"/"+this.name+"/"+this.score);
    }
    public String getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(String score) {
        this.score = score;
    }
    
    
}
