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
    
    private String username = "root";
    private String connectUrl = "jdbc:mysql://localhost:3306/taquin";
    private String mdp = "";
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
    
    public static boolean inscription(String nom, String mdp){
        boolean reussi=false;
        try {
            PreparedStatement prepare = Jdbc.con.prepareStatement("INSERT INTO joueur(nomJoueur, mdpJoueur) VALUES (?, ?)");
            prepare.setString(1, nom);
            prepare.setString(2, mdp);
            prepare.executeUpdate();
            reussi=true;
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("Erreur dans inscription");
            reussi=false;
        }
        return reussi;
    }
    
    public static String connexion(String nom, String mdp){
        String retourUsername= null;
        try {
            PreparedStatement prepare = con.prepareStatement("SELECT * from Joueur WHERE nomJoueur = ? AND mdpJoueur = ?");
            prepare.setString(1, nom);
            prepare.setString(2, mdp);
            ResultSet result = prepare.executeQuery();
            //si il n'y a pas de retour , le compte n'existe pas
            if(!result.next()){
                
            }//sinon le compte existe est la connexion est possible
            else{
                /*System.out.println(result.getString(2));
                System.out.println(result.getString(3));
                connexion = connexion + result.getString(2) + " " + result.getString(3) + "\n";*/
                retourUsername=result.getString(2);
            }
                
            //}
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Erreur dans connexion");
        }
        return retourUsername;
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
