package Classement;


import static GamePackage.Jdbc.getInstance;
import static GamePackage.Jdbc.getReqClassement;
import java.sql.Connection;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pg540
 */
public class Classement {
    
    private ObservableList<Player> data = FXCollections.observableArrayList();
    
    //Constructeur de Test
    public Classement(String r,String n,String s,TableView table){
        Player p=new Player(r,n,s);
        data.add(p);
        this.initRankTab(table);
    }
    //Constructeur
    public Classement(TableView table){
        this.setData();
        this.initRankTab(table);
    }
    public void setData(){
        if(data!=null){
            data = getReqClassement();
        }
        else{System.out.println("Erreur , classement Vide");}
    }
    //Crée les colonne et set les Data dans la table
    public void initRankTab(TableView table){
        table.setEditable(false);
        table.getStyleClass().add("rankingTable");
 
        TableColumn RankCol = new TableColumn("RANK");
        RankCol.setMinWidth(100);
        RankCol.getStyleClass().add("rankingTableColumn");
        RankCol.setCellValueFactory(
                new PropertyValueFactory<Player,String>("rank"));
 
        TableColumn NameCol = new TableColumn("NAME");
        NameCol.setMinWidth(100);
        NameCol.getStyleClass().add("rankingTableColumn");
        NameCol.setCellValueFactory(
                new PropertyValueFactory<Player,String>("name"));
        NameCol.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
 
        TableColumn ScoreCol = new TableColumn("SCORE");
        ScoreCol.setMinWidth(170);
        ScoreCol.getStyleClass().add("rankingTableColumn");
        ScoreCol.setCellValueFactory(
                new PropertyValueFactory<Player,String>("score"));
 
        table.setItems(data);
        table.getColumns().addAll(RankCol, NameCol, ScoreCol);
            
    } 
    
    public void refreshRank(){
        
    }
    
}
