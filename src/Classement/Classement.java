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
    
    //Crée les colonne pour le classement par Score et set les Data dans la table
    public void initScoreRankTab(TableView table,String taille){
        System.out.println("Initialize Score Rank");
        //Lance la requéte
        data = Jdbc.rankBScoreReq(taille);
        //si il n'y a pas de donnée retourné on clear le classement , et on écrit qu'il n'y a pas de donnée
        if(data.isEmpty()){
            this.clear();
            table.setPlaceholder(new Label("No data"));
            table.refresh();
        }
        //Sinon il y en a , et on initialise la table
        else{
            
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
        //set les data dans la table
        this.table.setItems(data);
        //set les différente colonne crée juste avant
        this.table.getColumns().setAll(RankCol, NameCol, ScoreCol);
        }
    } 
    //Crée les colonne pour le classement par Time et set les Data dans la table
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
 
        this.table.setItems(data);
        this.table.getColumns().setAll(RankCol, NameCol, ScoreCol);
        }
    } 
    public void initPersonalTimerRankTab(TableView table,String taille,String Username){
        System.out.println("Initialize Personal Timer Rank");
        data = Jdbc.personalRankBTimeReq(taille,Username);
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
 
        this.table.setItems(data);
        this.table.getColumns().setAll(RankCol, NameCol, ScoreCol);
        }
    } 
    public void initPersonalScoreRankTab(TableView table,String taille,String Username){
        System.out.println("Initialize Score Rank");
        data = Jdbc.PersonalrankBScoreReq(taille,Username);
        if(data.isEmpty()){
            this.clear();
            table.setPlaceholder(new Label("No data"));
            table.refresh();
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
 
        this.table.setItems(data);
        this.table.getColumns().setAll(RankCol, NameCol, ScoreCol);
        }
    } 
    //détruit les données contenu dans data
    public void clear(){
        this.table.getItems().clear();
        this.data.setAll();
        this.table.refresh();
    }
}
