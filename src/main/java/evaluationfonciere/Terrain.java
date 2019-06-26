/*
 *
 * @author N'TSOUAGLO Kokou Gawonou
 * @author GAVIDIA, Cristian 
 * @author de MEDEIROS Ricardo Julio
 *
 * 
 */
package  evaluationfonciere;
import static evaluationfonciere.EvaluationFonciere.MONTANT_VALEUR_BASE;
import static evaluationfonciere.EvaluationFonciere.OBSERVATION_ECART_DATES;
import static evaluationfonciere.EvaluationFonciere.OBSERVATION_PRIX_MAX;
import static evaluationfonciere.EvaluationFonciere.OBSERVATION_SUPERF_LOT1;
import static evaluationfonciere.EvaluationFonciere.OBSERVATION_SUPERF_LOT2;
import static evaluationfonciere.EvaluationFonciere.VAL_TAX_MUN_MAX;
import static evaluationfonciere.EvaluationFonciere.OBSERVATION_TAX_MUN;
import static evaluationfonciere.EvaluationFonciere.OBSERVATION_TAX_SCO;
import static evaluationfonciere.EvaluationFonciere.OBSERVATION_VAL_FONC;
import static evaluationfonciere.EvaluationFonciere.OBSERVATION_VAL_LOT1;
import static evaluationfonciere.EvaluationFonciere.OBSERVATION_VAL_LOT2;
import static evaluationfonciere.EvaluationFonciere.RAPPORT_PRIX_MAX;
import static evaluationfonciere.EvaluationFonciere.SUPERFICIE_LOT_MAX;
import static evaluationfonciere.EvaluationFonciere.TAUX_TAXE_MUNICIPALE;
import static evaluationfonciere.EvaluationFonciere.TAUX_TAXE_SCOLAIRE;
import static evaluationfonciere.EvaluationFonciere.VAL_ECART_MAX;
import static evaluationfonciere.EvaluationFonciere.VAL_FONC_TOT_MAX;
import static evaluationfonciere.EvaluationFonciere.VAL_LOT_MAX;
import static evaluationfonciere.EvaluationFonciere.VAL_TAX_SCO_MAX;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public abstract class Terrain {

    /**
     * montant de base pour calcul des droits de passage
     */
    protected static final int MNT_BASE_DROITS = 500;
    /**
     * nombre de services de base à ajouter au nombre reçu
     */
    public static final int NB_SRV_BASE = 2;
    /**
     * superficie limite (m2) du premier palier servant au calcul du montant des
     * services
     */
    public static final int QTE_INF_SUPERFICIE = 500;
    /**
     * Montant pour le premier palier servant au calcul des services
     */
    public static final int MNT_INF_SERVICES = 500;

    protected int typeTerrain;
    protected BigDecimal prixMin;
    protected BigDecimal prixMax;
    protected JSONObject entrefile;
    protected JSONArray lots;
    protected JSONArray observations;
    protected BigDecimal valeurFoncTot;
    protected BigDecimal taxeScolaire;
    protected BigDecimal taxeMunicipale;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * @param entrefile Constructeur de la classe Terrain Prend en valeur un
     * JSONObject et Initialise les differentes valeurs pour le calcul des lots
     */
    public Terrain(JSONObject entrefile) {

        typeTerrain = entrefile.getInt("type_terrain");
        prixMin = lirePrix(entrefile.getString("prix_m2_min"));
        prixMax = lirePrix(entrefile.getString("prix_m2_max"));
        lots = entrefile.getJSONArray("lotissements");
        observations = new JSONArray();
        valeurFoncTot = new BigDecimal(0);
        taxeScolaire = new BigDecimal(0);
        taxeMunicipale = new BigDecimal(0);

    }

    /**
     * Méthode qui cree le terrain
     *
     * @param type du terrain à créer
     * @param entrefile
     * @return Terrain crée
     */
    public static Terrain creerTerrain(int type, JSONObject entrefile) {
        Terrain terrain = null;
        switch (type) {
            case 0:
                terrain = new Agricole(entrefile);
                break;
            case 1:
                terrain = new Residentiel(entrefile);
                break;
            case 2:
                terrain = new Commercial(entrefile);
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
        return terrain;
    }

    /**
     * Méthode qui calcule le prix à appliquer
     *
     * @param min prix minimum du terrain
     * @param max prix maximum du terrain
     * @return prix à appliquer selon le type
     * @throws evaluationfonciere.ValeurInvalideException
     */
    public abstract BigDecimal calculPrix(BigDecimal min, BigDecimal max) throws ValeurInvalideException;

    /**
     * Méthode qui calcule le montant pour les droits de passage utilise
     * constantes MNT_BASE_DROITS definie dans la classe Terrain
     * TAUX_VALEUR_PASS definie dans la classe du type de Terrain
     *
     * @param nbDroits nombre de droits de passage
     * @param mntSuperficie de la superficie du terrain en m2
     * @throws NullPointerException
     * @throws NumberFormatException
     * @throws ValeurInvalideException
     *
     * @return montant calculé
     */
    public abstract BigDecimal calculDuDroitDePassage(int nbDroits, BigDecimal mntSuperficie)
            throws ValeurInvalideException;

    /**
     * Méthode qui calcule le montant pour les services
     *
     * @param superficie du terrain en m2
     * @param nbServ nombre de services
     *
     * @return calcul selon le type de terrain et superficie
     * @throws evaluationfonciere.ValeurInvalideException
     */
    public abstract BigDecimal calculDuMontantDeServices(double superficie, int nbServ)
            throws ValeurInvalideException;

    /**
     * Méthode qui ajoute le montant de valeur de base à la valeur foncière
     * totale
     *
     * @throws ValeurInvalideException due a certaines valeurs peuvent etre negative dans le fichier lu
     */
    public void CalculerValeurFonciereTotale() throws ValeurInvalideException {
        valeurFoncTot = new BigDecimal(0);
        AjouterValeurDesLots();
        valeurFoncTot = arrondirMnt05(valeurFoncTot.add(new BigDecimal(MONTANT_VALEUR_BASE)));
    }

    public void AjouterValeurDesLots() throws ValeurInvalideException {
        for (int i = 0; i < lots.size(); i++) {
            BigDecimal valeurDuLot = calculValeurFonciereDuLot(lots.getJSONObject(i));
            valeurFoncTot = valeurFoncTot.add(valeurDuLot);
            AjouterStatistiques(valeurDuLot, lots.getJSONObject(i).getInt("superficie"));
            lots.getJSONObject(i).put("valeur_par_lot", convertirMnt05(valeurDuLot));
        }
    }

    private void AjouterStatistiques(BigDecimal valeurDuLot, int superficie) {
        EvaluationFonciere.nbLots++;
        IncrementerNbLotValeur(valeurDuLot.compareTo(new BigDecimal(1000)), valeurDuLot);
        IncrementerNbLotType();
        statistiquesSuperficie(superficie);
        statistiquesValeur(valeurDuLot);
    }

    /**
     * Méthode qui calcule la valeur
     *
     * @param lot objet JSOn à processer
     * @return montant calculé pour le lot
     * @throws evaluationfonciere.ValeurInvalideException
     */
    public BigDecimal calculValeurFonciereDuLot(JSONObject lot) throws ValeurInvalideException {

        if (lot.getDouble("superficie") < 0 || lot.getInt("nombre_droits_passage") < 0 || lot.getInt("nombre_services") < 0) {
            throw new ValeurInvalideException("superficie, nbre de droits de passage ou nbre de service négatif dans le fichier");
        }

        try {
            BigDecimal valeurDuLot = calculPrix(prixMin, prixMax).multiply(new BigDecimal(lot.getDouble("superficie")));
            BigDecimal montantdePassage = calculDuDroitDePassage(lot.getInt("nombre_droits_passage"), valeurDuLot);
            BigDecimal montantdeService = calculDuMontantDeServices(lot.getDouble("superficie"), lot.getInt("nombre_services"));
            BigDecimal calcul = valeurDuLot.add(montantdePassage);

            return calcul.add(montantdeService);
        } catch (JSONException e) {
            throw new JSONException(e.getMessage());
        }

    }

    /**
     * Méthode qui ajoute le montant du calcul de la taxe scolaire
     *
     */
    public void CalculerTaxeScolaire() {
        taxeScolaire = arrondirMnt01(valeurFoncTot.multiply(new BigDecimal(TAUX_TAXE_SCOLAIRE)));

    }

    /**
     * Méthode qui ajoute le montant du calcul de la taxe municipale
     *
     */
    public void CalculerTaxeMunicipale() {
        taxeMunicipale = arrondirMnt05(valeurFoncTot.multiply(new BigDecimal(TAUX_TAXE_MUNICIPALE)));

    }

    private void statistiquesSuperficie(int superficie) {
        if (superficie > EvaluationFonciere.superficieMax) {
            EvaluationFonciere.superficieMax = superficie;
        }
    }

    private void statistiquesValeur(BigDecimal valeurDuLot) {
        EvaluationFonciere.valeurLotMax = new BigDecimal(0);
        if (EvaluationFonciere.valeurLotMax.compareTo(valeurDuLot) == -1) {
            EvaluationFonciere.valeurLotMax = valeurDuLot;
        }
    }

    /**
     * Méthode qui converti le prix+$ en BigDecimal
     *
     * @param prix String contenant le signe $
     * @return montant valeur en float
     */
    public static BigDecimal lirePrix(String prix) {
        if (prix.contains("$")) {
            prix = prix.substring(0, prix.indexOf("$"));
        }
        prix = prix.trim();
        if (prix.contains(",")) {
            prix = prix.replace(",", ".");
        }

        return new BigDecimal(prix);
    }

    /**
     * Méthode qui formate le montant obtenu en valeur String
     *
     * @param montant valeur en BigDecimal
     * @return prix String contenant le signe $
     */
    public static String convertirMnt05(BigDecimal montant) {
        return arrondirMnt05(montant) + " $";
    }

    /**
     * Méthode qui formate le montant obtenu en valeur String
     *
     * @param montant valeur en BigDecimal
     * @return prix String contenant le signe $
     */
    public static String convertirMnt01(BigDecimal montant) {
        montant = montant.setScale(2, BigDecimal.ROUND_HALF_UP);
        return montant + " $";
    }

    /**
     * Méthode qui arrondi au "0.05" supérieur
     *
     * @param montant a arrondir
     * @return montant arrondi
     */
    public static BigDecimal arrondirMnt05(BigDecimal montant) {
        BigDecimal increment = new BigDecimal("0.05");
        BigDecimal divided = montant.divide(increment, 0, RoundingMode.UP);
        BigDecimal result = divided.multiply(increment);
        result.setScale(2);
        return result;
    }

    /**
     * Méthode qui arrondi au "0.01" supérieur
     *
     * @param montant a arrondir
     * @return montant arrondi
     */
    public static BigDecimal arrondirMnt01(BigDecimal montant) {
        BigDecimal increment = new BigDecimal("0.01");
        BigDecimal divided = montant.divide(increment, 0, RoundingMode.HALF_UP);
        BigDecimal result = divided.multiply(increment);
        result.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result;
    }

    /**
     * Méthode qui ajoute observation si le prixmaximum est plus que 2 foix le
     * prix minimum
     *
     *
     */
    public void ObservationValeurFonciereTotale() {

        if (valeurFoncTot.compareTo(VAL_FONC_TOT_MAX) == 1) {
            observations.add(OBSERVATION_VAL_FONC);
        }

    }

    /**
     * Méthode qui verifie si un lot est trop dispendieux
     *
     *
     */
    public void ObservationValeurLot() {

        for (int i = 0; !lots.isEmpty() && i < lots.size(); i++) {
            if (lirePrix(lots.getJSONObject(i).getString("valeur_par_lot")).compareTo(VAL_LOT_MAX) == 1) {
                observations.add(OBSERVATION_VAL_LOT1
                        + lots.getJSONObject(i).getString("description").trim() + OBSERVATION_VAL_LOT2);
            }
        }

    }

    /**
     * Méthode qui verifie si la taxe scolaire est payable en deux versements
     *
     *
     */
    public void ObservationTaxScolaire() {

        if (this.taxeScolaire.compareTo(VAL_TAX_SCO_MAX) == 1) {
            observations.add(0, OBSERVATION_TAX_SCO);
        }

    }

    /**
     * Méthode qui verifie si la taxe municipale est payable en deux versements
     *
     *
     */
    public void ObservationTaxMunicipale() {

        if (taxeMunicipale.compareTo(VAL_TAX_MUN_MAX) == 1) {
            observations.add(0, OBSERVATION_TAX_MUN);
        }

    }

    /**
     * Méthode qui ajoute observation si le prixmaximum est plus que 2 foix le
     * prix minimum
     *
     *
     */
    public void ObservationPrixMaximum() {

        if (prixMax.divide(prixMin, 2, RoundingMode.HALF_UP).compareTo(RAPPORT_PRIX_MAX) == 1) {
            observations.add(OBSERVATION_PRIX_MAX);
        }

    }

    /**
     * Méthode qui verifie si un lot est trop grand
     *
     *
     */
    public void ObservationSuperficieLot() {

        for (int i = 0; !lots.isEmpty() && i < lots.size(); i++) {
            if (lots.getJSONObject(i).getInt("superficie") > SUPERFICIE_LOT_MAX) {
                observations.add(OBSERVATION_SUPERF_LOT1
                        + lots.getJSONObject(i).getString("description").trim() + OBSERVATION_SUPERF_LOT2);
            }
        }

    }

    /**
     * Méthode qui ajoute observation pour les dates de mesure des lots
     *
     * @throws java.text.ParseException
     *
     */
    public void ObservationDatesLot() throws ParseException {

        if (VerifierDates()) {
            observations.add(0, OBSERVATION_ECART_DATES);
        }

    }

    /**
     * Méthode qui verifie s'il y a un écart qui dépase la limite dans les lots
     *
     * @return vrai si trouvé un écart qui dépasse la limite
     * @throws java.text.ParseException
     *
     */
    protected boolean VerifierDates() throws ParseException {
        boolean trouve = false;
        for (int i = 0; !lots.isEmpty() && !trouve && i < lots.size(); i++) {
            for (int j = i + 1; !trouve && j < lots.size(); j++) {
                trouve = plusde6mois(format.parse(lots.getJSONObject(i).getString("date_mesure")),
                        format.parse(lots.getJSONObject(j).getString("date_mesure")));
            }
        }

        return trouve;
    }

    /**
     * Méthode qui verifie s'il y a un écart qui dépase la limite entre deux
     * dates
     *
     * @param date1
     * @param date2
     *
     * @return vrai si trouvé un écart qui dépasse la limite
     *
     */
    protected static boolean plusde6mois(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        return (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > VAL_ECART_MAX);

    }

    /**
     * Méthode qui cree le array des lots en sortie
     *
     *
     * @return lotissements
     */
    public JSONArray CreerLotissements() {
        JSONArray lotissements = new JSONArray();
        for (int i = 0; i < lots.size(); i++) {
            JSONObject lot = new JSONObject();
            lot.put("description", lots.getJSONObject(i).getString("description").trim());
            lot.put("valeur_par_lot", lots.getJSONObject(i).getString("valeur_par_lot").trim());
            lotissements.add(lot);
        }
        return lotissements;
    }

    private void IncrementerNbLotValeur(int val, BigDecimal valeurDuLot) {
        if (val == -1) {
            EvaluationFonciere.nbLotsMoins++;
        } else if (valeurDuLot.compareTo(new BigDecimal(10000)) == 1) {
            EvaluationFonciere.nbLotsPlus++;
        } else {
            EvaluationFonciere.nbLotsEntre++;
        }

    }

    public abstract void IncrementerNbLotType();

}
