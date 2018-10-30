/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gwendolyn
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SerialiserPlateau {
    public static void enregistrerPlateau(Plateau plateau) {
        ObjectOutputStream oos = null;
        try {
            File fichier = new File("src/plateau/plateau.ser");
            oos = new ObjectOutputStream(new FileOutputStream(fichier));
            oos.writeObject(plateau);
            oos.flush();
        } 
        catch(final java.io.IOException e) {
            e.printStackTrace();
        } 
        
        
        finally {
            try {
                if(oos!= null) {
                    oos.flush();
                    oos.close();
                }
            } 
        catch (final IOException ex) {
            ex.printStackTrace();}
        }
    }

    public static void EffacerPlateau() {
        try {
            File fichier = new File("src/plateau/plateau.ser");
            fichier.delete();
        } 
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
    
    public static Plateau RecupPlateau(){
        File fichier = new File("src/plateau/plateau.ser");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier));
            return (Plateau)ois.readObject();
        } catch (Exception ex) {
            Logger.getLogger(SerialiserPlateau.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static boolean existePlateau(){
        File fichier = new File("src/plateau/plateau.ser");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier));
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
    }
}

