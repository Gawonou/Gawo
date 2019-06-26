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

public class Commercial extends Terrain {

    /**
     * taux de valeur du lot pour calcul des droits de passage
     */
    private static final double TAUX_VALEUR_PASS = 0.15;

    /**
     * Montant pour le deuxième palier > 500 servant au calcul des services
     */
    public static final int MNT_SUP_SERVICES = 1500;

    /**
     * Montant maximale servant au calcul des services
     */
    public static final int MNT_MAX_SERVICES = 5000;

    public Commercial(JSONObject entrefile) {
        super(entrefile);
    }

    /**
     * @param min prix minimum du terrain
     * @param max prix maximum du terrain
     * @return prix servant de calcul de valeur de superficie (valeur du lot)
     * Ici le prix maximum du lot est utilisé
     * @throws evaluationfonciere.ValeurInvalideException
     */
    @Override
    public BigDecimal calculPrix(BigDecimal min, BigDecimal max) throws ValeurInvalideException {
        if (min.compareTo(new BigDecimal(0)) == -1 || max.compareTo(new BigDecimal(0)) == -1) {
            throw new ValeurInvalideException("Une des valeurs du prix du lot est negative");
        }
        try {
            return max;
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
     * TAUX_VALEUR_PASS definie ( 15 % dans ce cas)
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

    @Override
    public BigDecimal calculDuMontantDeServices(double superficie, int nbServ) throws ValeurInvalideException {
        BigDecimal calcul;

        if (superficie < 0 || nbServ < 0) {
            throw new ValeurInvalideException("superficie ou nbre de service negatif.");
        }
        try {
            if (superficie <= QTE_INF_SUPERFICIE) {
                calcul = new BigDecimal(NB_SRV_BASE).add(new BigDecimal(nbServ));
                calcul = calcul.multiply(new BigDecimal(MNT_INF_SERVICES));
            } else {
                calcul = new BigDecimal(NB_SRV_BASE).add(new BigDecimal(nbServ));
                calcul = calcul.multiply(new BigDecimal(MNT_SUP_SERVICES));
            }

            if (calcul.compareTo(new BigDecimal(MNT_MAX_SERVICES)) == 1) {
                calcul = new BigDecimal(MNT_MAX_SERVICES);
            }
            return arrondirMnt05(calcul);
            
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
        EvaluationFonciere.nbLotsType2++;
    }
}
