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
        
        int minvaleur = 1000;
        int tempo = 0;
        int profondeur = 20;
        int deplacement = 0;
        
        if(plat.getYCaseVide()>0){
            plat.moveRight();
            tempo = max(plat,profondeur-1);
            if(tempo < minvaleur){
                minvaleur = tempo;
                deplacement = 1;
            }
            plat.moveLeft();
        }
        else if(plat.getYCaseVide() < plat.getTaille()){
            plat.moveLeft();
            tempo = max(plat,profondeur-1);
            if(tempo < minvaleur){
                minvaleur = tempo;
                deplacement = 1;
            }
            plat.moveRight();
        }
        else if(plat.getXCaseVide() > 0){
            plat.moveUp();
            tempo = max(plat,profondeur-1);
            if(tempo < minvaleur){
                minvaleur = tempo;
                deplacement = 1;
            }
            plat.moveDown();
        }
        else if(plat.getXCaseVide() < plat.getTaille()){
            plat.moveDown();
            tempo = max(plat,profondeur-1);
            if(tempo < minvaleur){
                minvaleur = tempo;
                deplacement = 1;
            }
            plat.moveUp();
        }
        
        
    }
    
    public int max(Plateau plat, int profondeur){
        int bla=0;
        
        return bla;
    }
    
    
    
    
    
    
    
}
