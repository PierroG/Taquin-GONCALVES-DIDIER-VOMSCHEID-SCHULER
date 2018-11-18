/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gwendolyn
 */
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
    
    public void inscription(){
        try {
        
        } catch(Exception e){
            System.out.println("Erreur");
        }
    }
}
