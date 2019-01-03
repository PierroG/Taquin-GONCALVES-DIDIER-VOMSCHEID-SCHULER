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
import java.sql.Statement;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Jdbc {
    
    private String username = "root";
    private String connectUrl = "jdbc:mysql://localhost:3306/taquin";//"jdbc:mysql://localhost:3306/taquin";
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
            PreparedStatement prepare = Jdbc.con.prepareStatement("INSERT INTO joueur(nomJoueur, password) VALUES (?, ?)");
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
            PreparedStatement prepare = con.prepareStatement("SELECT * from Joueur WHERE nomJoueur = ? AND password = ?");
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
    //Requéte pour récupérer le classement by Score
    public static ObservableList<Player> rankBScoreReq(String taille){
        ObservableList<Player> list = FXCollections.observableArrayList();//AND score.idScore = grille.idScore grille.nomGrille = 2
        try{
            System.out.println(taille);
            PreparedStatement prepare = con.prepareStatement("SELECT nbMouv,nomJoueur FROM score,joueur,grille WHERE joueur.idJoueur=score.idJoueur AND (score.idScore = grille.idScore AND grille.nomGrille = ?) ORDER BY nbMouv ASC");
            prepare.setString(1, taille);
            ResultSet result = prepare.executeQuery();
            int rank=1;
            while(result.next()){
                Player p = new Player(Integer.toString(rank),result.getString(2),result.getString(1));
                p.affiche();
                list.add(p);
                rank++;
            }
            
        }catch(SQLException e){
            System.out.println("Erreur dans la requéte du Classement");
            e.printStackTrace();
            
        }
        return list;
    }//Requéte pour récupérer le classement by Time
    public static ObservableList<Player> rankBTimeReq(String taille){
        ObservableList<Player> list = FXCollections.observableArrayList();
        try{
            PreparedStatement prepare = con.prepareStatement("SELECT chronoSec,chronoMin,nomJoueur FROM score,joueur,grille WHERE joueur.idJoueur=score.idJoueur AND (score.idScore = grille.idScore AND grille.nomGrille = ?) ORDER BY chronoMin ASC , chronoSec ASC");
            prepare.setString(1, taille);
            ResultSet result = prepare.executeQuery();
            int rank=1;
            while(result.next()){
                Player p = new Player(Integer.toString(rank),result.getString(3),result.getString(2)+":"+result.getString(1));
                p.affiche();
                list.add(p);
                rank++;
            }
            
        }catch(SQLException e){
            System.out.println("Erreur dans la requéte du Classement");
            e.printStackTrace();
            
        }
        return list;
    }
    
    //Requéte pour inséré un nouveau Score
    public static boolean scoreReq(int chronoS, int chronoM, int nbMouv, int nbPoi,String idJoueur, int nGrille){
        boolean reussi=false;
        try {
            PreparedStatement prepare = Jdbc.con.prepareStatement("INSERT INTO score(chronoSec, chronoMin, nbMouv, nbPoint, idJoueur) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            prepare.setString(1,Integer.toString(chronoS));
            prepare.setString(2,Integer.toString(chronoM));
            prepare.setString(3,Integer.toString(nbMouv));
            prepare.setString(4,Integer.toString(nbPoi));
            prepare.setString(5,idJoueur);
            prepare.executeUpdate();
            
            //reussi=true;
            ResultSet rs = prepare.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);    
            PreparedStatement prepare2 = Jdbc.con.prepareStatement("INSERT INTO grille(nomGrille, idScore) VALUES (?, ?)");
            prepare2.setString(1, Integer.toString(nGrille));
            prepare2.setString(2, Integer.toString(id));
            prepare2.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("Erreur");
            reussi=false;
        }
        return reussi;
    }
    public static ObservableList<Player> PersonalrankBScoreReq(String taille,String name){
        ObservableList<Player> list = FXCollections.observableArrayList();//AND score.idScore = grille.idScore grille.nomGrille = 2
        try{
            System.out.println(taille);
            PreparedStatement prepare = con.prepareStatement("SELECT nbMouv,nomJoueur FROM score,joueur,grille WHERE joueur.idJoueur=score.idJoueur AND (score.idScore = grille.idScore AND grille.nomGrille = ? AND joueur.nomJoueur = ?) ORDER BY nbMouv ASC");
            prepare.setString(1, taille);
            prepare.setString(2, name);
            ResultSet result = prepare.executeQuery();
            int rank=1;
            while(result.next()){
                Player p = new Player(Integer.toString(rank),result.getString(2),result.getString(1));
                p.affiche();
                list.add(p);
                rank++;
            }
            
        }catch(SQLException e){
            System.out.println("Erreur dans la requéte du Classement");
            e.printStackTrace();
            
        }
        return list;
    }
    //Lance la requéte qui selectionne les score de temps d'un joueur précis
    public static ObservableList<Player> personalRankBTimeReq(String taille,String Username){
        ObservableList<Player> list = FXCollections.observableArrayList();
        try{
            PreparedStatement prepare = con.prepareStatement("SELECT chronoSec,chronoMin,nomJoueur FROM score,joueur,grille WHERE joueur.idJoueur=score.idJoueur AND (score.idScore = grille.idScore AND grille.nomGrille = ? AND joueur.nomJoueur = ?) ORDER BY chronoMin ASC , chronoSec ASC");
            prepare.setString(1, taille);
            prepare.setString(2, Username);
            ResultSet result = prepare.executeQuery();
            int rank=1;
            while(result.next()){
                Player p = new Player(Integer.toString(rank),result.getString(3),result.getString(2)+":"+result.getString(1));
                p.affiche();
                list.add(p);
                rank++;
            }
            
        }catch(SQLException e){
            System.out.println("Erreur dans la requéte du Classement");
            e.printStackTrace();
            
        }
        return list;
    }
    
    
    public static String getNameById(String id){
        String retour="0";
        try {
            PreparedStatement prepare = con.prepareStatement("SELECT nomJoueur FROM joueur WHERE idJoueur = ?");
            prepare.setString(1, id);
            ResultSet result = prepare.executeQuery();
            retour=result.getString(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return retour;
    }
    public static String getIdByName(String nom){
        String retour="0";
        try {
            PreparedStatement prepare = con.prepareStatement("SELECT idJoueur FROM joueur WHERE nomJoueur = ?");
            prepare.setString(1, nom);
            ResultSet result = prepare.executeQuery();
            while(result.next()){
                retour=result.getString(1);
            }        
        }catch(SQLException e){
            e.printStackTrace();
        }
        return retour;
    }
}
