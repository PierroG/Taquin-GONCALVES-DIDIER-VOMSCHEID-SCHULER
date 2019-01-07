
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
public class DistanceManhattan implements HeuristiqueA {
    
    // L'heuristique la plus utile et appréciée pour le taquin
    /**
     * Calcule la distance de chaque case entre ça place actuelle et ça position finale
     * @return 
     */
    @Override
    public int heuristique(Case[][] plateau, int dimension) {
	int sommes = 0, c = 0;
	for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int y = (plateau[i][j].getNum() - 1) % dimension;
                int x = (plateau[i][j].getNum() - 1) / dimension;
                c = Math.abs((i - x)) + Math.abs((j - y));
                sommes = sommes + c;
            }
	}
	return sommes;
    }

    @Override
    public String toString() {
        return "Sommes des Distances Manhattan est de : ";
    }
    
}
