/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IA;

import GamePackage.Case;

/**
 * L'heuristique est une évaluation qui a chaque état (neaud) va estimer le nombre de coup
 * qu'auras le meilleur chemin de résolution
 * 
 * @author Axel Didier
 */
public interface HeuristiqueA {
    
    // interface pour toutes les heuristiques qu'on peut implémenter
    public int heuristique(Case plateau[][] , int taille);
    
}
