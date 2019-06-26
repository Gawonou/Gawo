/* 
 * @author N'TSOUAGLO Kokou Gawonou
 * 
 * @version 2018-12-02
 * 
 */

package  evaluationfonciere;
import java.math.BigDecimal;
import net.sf.json.JSONObject;

public class Residentiel extends Terrain {

    /**
     * Constante. Taux servant a calculer les droits de passage
     */
    private static final double TAUX_VALEUR_PASS = 0.10;

    /**
     * superficie limite (m2) maximum servant au calcul du montant des services
     */
    public static final int SUPERFICIE_MAX = 10000;

    /**
     * Montant pour le deuxième palier servant au calcul des services
     */
    public static final int MNT_SUP_SERVICES = 1000;

    public Residentiel(JSONObject entrefile) {
        super(entrefile);
    }

    /**
     * @param min prix minimum du terrain
     * @param max prix maximum du terrain
     * @return la moyenne des prix pour le calcul de la valeur du lot
     */
    @Override
    public BigDecimal calculPrix(BigDecimal min, BigDecimal max) throws ValeurInvalideException {
        if (min.compareTo(new BigDecimal(0)) == -1 || max.compareTo(new BigDecimal(0)) == -1) {
            throw new ValeurInvalideException("Une des valeurs du prix du lot est negative");
            
        }
        try {
            return min.add(max).divide(new BigDecimal(2));
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
     * constantes MNT_BASE_DROITS definie dans la classe Terrain
     * TAUX_VALEUR_PASS definie dans cette classe (Ligne 15)
     *
     * @param nbDroits nombre de droits de passage
     * @param valeurDuLot valeur de la superficie du terrain en m2
     * @return valeur
     */
    @Override
    public BigDecimal calculDuDroitDePassage(int nbDroits, BigDecimal valeurDuLot) throws ValeurInvalideException {
        if (nbDroits < 0 || valeurDuLot.compareTo(new BigDecimal(0)) == -1) {
            throw new ValeurInvalideException("Nombre de droits ou valeur du lot ne peuvent pas etre negatifs");
        }
        try {
            BigDecimal temp = valeurDuLot.multiply(new BigDecimal(TAUX_VALEUR_PASS)).multiply(new BigDecimal(nbDroits));
            return arrondirMnt05(new BigDecimal(MNT_BASE_DROITS).subtract(temp));
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
     * @param nbredeService nombre de services
     *
     * @return calcul selon le type de terrain et superficie
     */
    @Override
    public BigDecimal calculDuMontantDeServices(double superficie, int nbredeService) throws ValeurInvalideException {
        if (superficie < 0 || nbredeService < 0) {
            throw new ValeurInvalideException("superficie ou nbre de service negatif.");
        }

        try {
            BigDecimal calcul;
            if (superficie <= QTE_INF_SUPERFICIE) {
                calcul = new BigDecimal(0);
            } else {
                calcul = new BigDecimal(NB_SRV_BASE).add(new BigDecimal(nbredeService));
                if (superficie <= SUPERFICIE_MAX) {
                    calcul = calcul.multiply(new BigDecimal(MNT_INF_SERVICES));
                } else {
                    calcul = calcul.multiply(new BigDecimal(MNT_SUP_SERVICES));
                }
            }
            return calcul;
            
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
        EvaluationFonciere.nbLotsType1++;
    }
}
