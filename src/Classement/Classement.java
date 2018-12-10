package Classement;


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
    
    private final ObservableList<Player> data = FXCollections.observableArrayList();
    
    //Constructeur de Test
    public Classement(String r,String n,String s,TableView table){
        Player p=new Player(r,n,s);
        data.add(p);
        this.initScoreRank(table);
    }
    //Constructeur
    public Classement(LinkedList<Player> list,TableView table){
        for(int i=0;i<list.size();i++){
            data.add(list.get(i));
        }
        this.initScoreRank(table);
    }
    //Crée les colonne et set les Data dans la table
    public void initScoreRank(TableView table){
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
    
}
