/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gwendolyn
 */
package GamePackage;
import Classement.Player;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Jdbc {
    
    private String username = "taquin";
    private String connectUrl = "jdbc:mysql://localhost/taquin";
    private String mdp = "taquin_mdp";
    private static Connection con = null;
    
    //Constructeur privé
    private Jdbc(){
      try {
          //Class.forName("com.mysql.jdbc.Driver").newInstance();
          con = DriverManager.getConnection(connectUrl, username, mdp);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    
    //methode qui va nous retourner notre instance et la créer si elle n'existe pas
    public static Connection getInstance(){
     if(con == null){
       new Jdbc();
       System.out.println("la connexion a la base est cree");
     }
     else {
       System.out.println("la connexion est deja existante");
     }
     return con;   
   }
    
    //methode qui ferme la connexion a la base si elle a ete auparavant ouverte
    public static void closeInstance(){
        if(con != null){
            try {
                con.close();
                System.out.println("deconnexion de la base reussie");
            } catch(Exception e){ 
                e.printStackTrace();
            }
        }
    }
    
    public void inscription(String nom, String mdp){
        try {
            PreparedStatement prepare = this.con.prepareStatement("INSERT INTO joueur(nomJoueur, mdpJoueur) VALUES (?, ?)");
            prepare.setString(1, nom);
            prepare.setString(2, mdp);
            prepare.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("Erreur dans inscription");
        }
    }
    
    public String connexion(String nom, String mdp){
        String connexion= "";
        try {
            PreparedStatement prepare = con.prepareStatement("SELECT * from Joueur WHERE nomJoueur = ? AND mdpJoueur = ?");
            prepare.setString(1, nom);
            prepare.setString(2, mdp);
            ResultSet result = prepare.executeQuery();
            while(result.next()){
                System.out.println(result.getString(1));
                System.out.println(result.getString(2));
                connexion = connexion + result.getString(1) + " " + result.getString(2) + "\n";
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Erreur dans connexion");
        }
        return connexion;
    }
    
    public static ObservableList<Player> getReqClassement(){
        ObservableList<Player> list = null;//
        try{
            PreparedStatement prepare = con.prepareStatement("SELECT * FROM Classement WHERE rank < 50");
            ResultSet result = prepare.executeQuery();
            while(result.next()){
                Player p = new Player(result.getString(1),result.getString(2),result.getString(3));
                list.add(p);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Erreur dans la requéte du Classement");
        }
        return list;
    }
}
