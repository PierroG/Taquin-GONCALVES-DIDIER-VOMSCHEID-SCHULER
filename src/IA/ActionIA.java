/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IA;

import GamePackage.*;
import java.util.ArrayList;

/**
 *
 * @author Axel Didier
 */
public class ActionIA {
    
    ArrayList<Plateau> List_successeurs = new ArrayList<Plateau>();
    ArrayList<Plateau> Chemin = new ArrayList<Plateau>();
    HeuristiqueA heuristique;
    
    
    // Implémentation de l'algorithme A* (essai)
    // A* est très rapide mais trop gourmand en mémoire
    public void IAPerformed(Plateau plat) {
	Chemin.clear();
	List_successeurs.clear();
	boolean solution_trouvee = false;
        Plateau meilleure = null;
        
        // on ajoute le départ dans une liste et on cherche la meilleure résolution
	List_successeurs.add(plat);
        while (List_successeurs.size() > 0 && !solution_trouvee) {
            meilleure = List_successeurs.get(0);
            meilleure.setheuristique(heuristique);
            for (int i = 1; i < List_successeurs.size(); i++) {
		Plateau p = List_successeurs.get(i);
                p.setheuristique(heuristique);
		if (p.heuristique() < meilleure.heuristique()) {
                    meilleure = p;
		}
            } // si on a trouvé, on termine  
            if (meilleure.isOver()) {
            solution_trouvee = true;
		Chemin.add(meilleure);
            } else { // sinon on continue d'ajouter un mouvement
                if (existDansChemin(meilleure) >= 0) {
                    List_successeurs.remove(meilleure);
                } else {
                    Chemin.add(meilleure);
                    List_successeurs.clear();
                    List_successeurs = meilleure.Successeurs();
                }
            }
	}
    }

    //
    private int existDansChemin(Plateau p) {
        for (int i = 0; i < Chemin.size() - 1; i++) {
            if (p.equals(Chemin.get(i))) {
		return i;		
            }	
        }
	return -1;
    }
    
    
    // Informations supplémentaires :
    
    // évolution possible : améliorer l'estimation de distance
    // et élagage des coups redondantes (symétriques ou remet le plateau dans le même ordre)
    
    
    // Autres algorithmes de résolutions pour ce projet que A* :
    // IDA* : trop peu gourmand en mémoire, mais convient bien à la résolution du taquin.
    // SMA* : bon compromis entre mémoire et rapidité. (plus lent que A*)
    
    
    
    //Test d'un IA non adapté
    public void MimaxIA(Plateau plat){
        
        //Plateau platcopie = new Plateau();
        //platcopie = plat;
        
        
        int minvaleur = 1000;
        int tempo = 0;
        int profondeur = 10;
        String memo = "";
        
        // si la case vide est n'est pas situé sur le bord gauche
        // on effectue un mouvement à gauche puis on descend en profondeur
        if(plat.getYCaseVide() > 0){
            plat.moveRightIA();
            tempo = min1(plat,profondeur-1);
            // si la valeur remonté de la feuille est inférieur à minvaleur on garde en mémoire le mouvement à faire
            if(tempo < minvaleur){
                minvaleur = tempo;
                memo = "D";
            } // mouvement inverse
            plat.moveLeftIA();
        }// si la case vide est n'est pas situé sur le bord droit
        if(plat.getYCaseVide() < plat.getTaille()-1){
            plat.moveLeftIA();
            tempo = min1(plat,profondeur-1);
            if(tempo < minvaleur){
                minvaleur = tempo;
                memo = "Q";
            } // mouvement inverse
            plat.moveRightIA();
        } // si la case vide est n'est pas situé sur le bord haut
        if(plat.getXCaseVide() > 0){
            plat.moveDownIA();
            tempo = min1(plat,profondeur-1);
            if(tempo < minvaleur){
                minvaleur = tempo;
                memo = "S";
            } // mouvement inverse
            plat.moveUpIA();
        } // si la case vide est n'est pas situé sur le bord bas
        if(plat.getXCaseVide() < plat.getTaille()-1){
            plat.moveUpIA();
            tempo = min1(plat,profondeur-1);
            if(tempo < minvaleur){
                minvaleur = tempo;
                memo = "Z";
            } // mouvement inverse
            plat.moveDownIA();
        }
        
        // on effectue le meilleur mouvement à effectuer pour arriver au résultat
        if (memo == "Z"){
            plat.moveUp();
        } else if (memo == "Q"){
            plat.moveLeft();
        } else if (memo == "S"){
            plat.moveDown();
        } else if (memo == "D") {
            plat.moveRight();
        } else {
            // ça n'a pas marché
        }
        
    }
    
    // une fois descendu en profondeur on continue et on alterne entre deux algo
    public int min1(Plateau platcopie, int profondeur){
        if (profondeur == 0){
            return platcopie.coefDesordre();
        } else if (platcopie.coefDesordre()==0){
            return platcopie.coefDesordre();
        }
        System.out.println(platcopie.coefDesordre());
        
        int valmin1 = 1000;
        int tempo = 0;
        
        // si la case vide est n'est pas situé sur le bord gauche
        // on effectue un mouvement à gauche puis on descend en profondeur
        if(platcopie.isOver()){
            valmin1 = -1000;
            return valmin1;
        }
        if(platcopie.getYCaseVide() > 0){
            platcopie.moveRightIA();
            tempo = min1(platcopie,profondeur-1);
            // si tempo est supérieur à valmax, on enregistre tempo dans valmax
            if(tempo < valmin1){
                valmin1 = tempo;
            } // mouvement inverse
            platcopie.moveLeftIA();
        }
        if(platcopie.getYCaseVide() < platcopie.getTaille()-1){
            platcopie.moveLeftIA();
            tempo = min1(platcopie,profondeur-1);
            if(tempo < valmin1){
                valmin1 = tempo;
            }
            platcopie.moveRightIA();
        }
        if(platcopie.getXCaseVide() > 0){
            platcopie.moveDownIA();
            tempo = min1(platcopie,profondeur-1);
            if(tempo < valmin1){
                valmin1 = tempo;
            }
            platcopie.moveUpIA();
        }
        if(platcopie.getXCaseVide() < platcopie.getTaille()-1){
            platcopie.moveUpIA();
            tempo = min1(platcopie,profondeur-1);
            if(tempo < valmin1){
                valmin1 = tempo;
            }
            platcopie.moveDownIA();
        }
        
        // si le jeu n'a réussi à faire aucune action :
        if(valmin1 != 1000){
            return valmin1;
        } else {
            return platcopie.coefDesordre();
        }
    }
    
    // fonction qui va évaluer la score a donner à chaque feuille :
    // coefDesordre() présent dans plateau.java
    
    // cette IA a un gros problème, si deux pieces sont inversé sur la même ligne
    // il ne va pas pouvoir défaire une autre ligne car cela va augmenter le coefficient de désordre
    // l'IA va donc rester bloqué car il ne trouvera pas de solution pour résoudre le taquin.
    
}
