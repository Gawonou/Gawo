/* 
 * @author N'TSOUAGLO Kokou Gawonou
 * @author GAVIDIA, Cristian
 * @author DE MEDEIROS, Ricardo Julio 
 * Cours INF2015
 * 
 * @version 2018-12-02
 * 
 */
package  evaluationfonciere;

import java.math.BigDecimal;
import net.sf.json.JSONObject;

public class Agricole extends Terrain {

    /**
     * taux de valeur du lot pour le calcul des droits de passage Constante pour
     * le calcul des droits de passage ( taux = 5%)
     */
    private static final double TAUX_VALEUR_PASS = 0.05;

    public Agricole(JSONObject entrefile) {
        super(entrefile);
    }

    /**
     * @param min prix minimum du terrain
     * @param max prix maximum du terrain
     * @return prix à appliquer (ici, le prix minimum du terrain sert a calculer
     * la valeur du lot )
     * @throws evaluationfonciere.ValeurInvalideException
     */
    @Override
    public BigDecimal calculPrix(BigDecimal min, BigDecimal max) throws ValeurInvalideException {
        if (min.compareTo(new BigDecimal(0)) == -1 || max.compareTo(new BigDecimal(0)) == -1) {
            throw new ValeurInvalideException("Une des valeurs du prix du lot est negative");
        }
        try {
            return min;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Prix minimum ou maximum de format different\n"
                    + e.getMessage());
        } catch (NullPointerException e) {
            throw new NullPointerException("Valeur nulle trouvee pour Prix minimum ou maximum.\n"
                    + e.getMessage());
        }

    }

    /**
     * Méthode qui calcule le montant pour les droits de passage Utilise les
     * constantes 1_ MNT_BASE_DROITS definie dans la classe Terrain 2_
     * TAUX_VALEUR_PASS, constante definie dans cette classe ( 5 %) (Ligne 15)
     *
     * @param nbDroits nombre de droits de passage
     * @param valeurDuLot de la superficie du terrain en m2
     * @return valeurDuLot
     * @throws evaluationfonciere.ValeurInvalideException
     */
    @Override
    public BigDecimal calculDuDroitDePassage(int nbDroits, BigDecimal valeurDuLot) throws ValeurInvalideException {

        if (nbDroits < 0 || valeurDuLot.compareTo(new BigDecimal(0)) == -1) {
            throw new ValeurInvalideException("Nombre de droits ou valeur du lot ne peuvent pas etre negatifs");
        }
        try {
            valeurDuLot = valeurDuLot.multiply(new BigDecimal(TAUX_VALEUR_PASS));
            BigDecimal x = valeurDuLot.multiply(new BigDecimal(nbDroits));
            return arrondirMnt05(new BigDecimal(MNT_BASE_DROITS).subtract(x));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Nombre de Droits ou valeur du lot de format different\n"
                    + e.getMessage());
        } catch (NullPointerException e) {
            throw new NullPointerException("Valeur nulle trouvee pour nombre de droit et valeur du lot." + e.getMessage());
        }

    }

    /**
     * Méthode qui calcule le montant pour les services
     *
     * @param superficie du terrain en m2
     * @param nbServ nombre de services
     * @return calcul selon le type de terrain et superficie
     * @throws evaluationfonciere.ValeurInvalideException
     */
    @Override
    public BigDecimal calculDuMontantDeServices(double superficie, int nbServ) throws ValeurInvalideException {
        if (superficie < 0 || nbServ < 0) {
            throw new ValeurInvalideException("superficie ou nbre de service negatif.");
        }
        try {
            return new BigDecimal(0);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Superficie ou nombre de services de format different\n"
                    + e.getMessage());
        } catch (NullPointerException e) {
            throw new NullPointerException("Valeur nulle trouvee pour Superficie ou nombre de services.\n"
                    + e.getMessage());
        }

    }
    
    @Override
    public void IncrementerNbLotType(){
        EvaluationFonciere.nbLotsType0++;
    }

}