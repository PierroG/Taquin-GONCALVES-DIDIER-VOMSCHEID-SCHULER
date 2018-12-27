package Classement;


import GamePackage.Jdbc;
import static GamePackage.Jdbc.getInstance;
import java.sql.Connection;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
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
    TableView table;

    //Constructeur
    public Classement(TableView table,String  taille){
        this.table = table;
        this.initScoreRankTab(table,taille);
        
    }
    
    //Crée les colonne et set les Data dans la table
    public void initScoreRankTab(TableView table,String taille){
        System.out.println("Initialize Score Rank");
        data = Jdbc.rankBScoreReq(taille);
        if(data.isEmpty()){
            this.clear();
            table.setPlaceholder(new Label("No data"));
        }else{
            
        table.setEditable(false);
        table.getStyleClass().add("rankingTable");
 
        TableColumn RankCol = new TableColumn("RANK");
        RankCol.setMinWidth(50);
        RankCol.getStyleClass().add("rankingTableColumn");
        RankCol.setCellValueFactory(
                new PropertyValueFactory<Player,String>("rank"));
 
        TableColumn NameCol = new TableColumn("NAME");
        NameCol.setMinWidth(170);
        NameCol.getStyleClass().add("rankingTableColumn");
        NameCol.setCellValueFactory(
                new PropertyValueFactory<Player,String>("name"));
        NameCol.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
 
        TableColumn ScoreCol = new TableColumn("SCORE");
        ScoreCol.setMinWidth(150);
        ScoreCol.getStyleClass().add("rankingTableColumn");
        ScoreCol.setCellValueFactory(
                new PropertyValueFactory<Player,String>("score"));
 
        table.setItems(data);
        table.getColumns().setAll(RankCol, NameCol, ScoreCol);
        }
    } 
    
    public void initTimerRankTab(TableView table,String taille){
        System.out.println("Initialize Timer Rank");
        data = Jdbc.rankBTimeReq(taille);
        if(data.isEmpty()){
            this.clear();
            table.setPlaceholder(new Label("No data"));
        }else{
            
        table.setEditable(false);
        table.getStyleClass().add("rankingTable");
 
        TableColumn RankCol = new TableColumn("RANK");
        RankCol.setMinWidth(50);
        RankCol.getStyleClass().add("rankingTableColumn");
        RankCol.setCellValueFactory(
                new PropertyValueFactory<Player,String>("rank"));
 
        TableColumn NameCol = new TableColumn("NAME");
        NameCol.setMinWidth(170);
        NameCol.getStyleClass().add("rankingTableColumn");
        NameCol.setCellValueFactory(
                new PropertyValueFactory<Player,String>("name"));
        NameCol.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
 
        TableColumn ScoreCol = new TableColumn("TIME");
        ScoreCol.setMinWidth(150);
        ScoreCol.getStyleClass().add("rankingTableColumn");
        ScoreCol.setCellValueFactory(
                new PropertyValueFactory<Player,String>("score"));
 
        table.setItems(data);
        table.getColumns().setAll(RankCol, NameCol, ScoreCol);
        }
    } 
    //Lance la requéte associé et set les data qui en retourne
    public void setDataBestScore(String taille){
        data = Jdbc.rankBScoreReq(taille);
        if(data.isEmpty()){
            table.setPlaceholder(new Label("No data"));
        }else{
            table.setItems(data);
        }
    }
    public void setDataPersonalBestScore(String taille,String Username){
        data = Jdbc.PersonalrankBScoreReq(taille,Username);
        if(data.isEmpty()){
            table.setPlaceholder(new Label("No data"));
        }else{
            table.setItems(data);
        }
    }
    public void setDataPersonalBestTimer(String taille,String Username){
        data = Jdbc.personalRankBTimeReq(taille,Username);
        if(data.isEmpty()){
            System.out.println("no data");
            table.setPlaceholder(new Label("No data"));
        }else{
            table.setItems(data);
        }
        
    }
    public void setDataBestTimer(String taille){
        data = Jdbc.rankBTimeReq(taille);
        if(data.isEmpty()){
            System.out.println("no data");
            table.setPlaceholder(new Label("No data"));
        }else{
            table.setItems(data);
        }
    }
    //détruit les données contenu dans datas
    public void clear(){
        data.setAll();
    }
}
