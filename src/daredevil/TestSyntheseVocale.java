package daredevil;

import t2s.son.LecteurTexte;
 
public class TestSyntheseVocale {
 
    public static void main(String[] args) {
        final LecteurTexte lecteur = new LecteurTexte();
        lecteur.setTexte("Bonjour. 75.");
        lecteur.playAll();
    }
 
}
