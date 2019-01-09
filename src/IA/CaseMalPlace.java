/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IA;

import GamePackage.Case;

/**
 *
 * @author Axel Didier
 */
public class CaseMalPlace implements HeuristiqueA {
    
    // Une heuristique possible
    public int heuristique(Case[][] plateau, int taille) {
        int cpt = 1, c = 0;
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (plateau[i][j].getNum()!= cpt) {
                    c++;
                }
                cpt++;
            }
        }
        return c;
    }

    
    public String toString() {
	return "Le nombre de cases mal placées est de : ";
    }
    
}
