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
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

public class EvaluationFonciere {

    public final static String NOMBRE_PARAMETRE_FILE = "\nVous devez avoir deux fichiers en l'entré: fichier de lecture (qui comprend "
                                                    + "les donnés à traiter) et un fichier écriture.";
    public final static String MSG_ERR_TYPE_TERRAIN = "Le choix de type de terrain est invalide. SVP, le type doit être entre 0, 1 ou 2.";
    public final static String CHOIX_MAL_ENTRE = "\nLe type du choix du terrain n'est pas correct";
    public final static String PAS_DE_REPO =  "Erreur, Le répertoire de fichier n'existe pas \n";
    public final static String MESSAGE_DROIT_PASS =  "Le nombre de droits de passage doit etre entre 0 a 10.";
    public final static String MESSAGE_NR_SERV =  "Le nombre de services doit etre entre 0 a 5.";
    public final static String MESSAGE_SURPERFICIE=  "Le superficie doit etre possitive et ne doit pas etre superieure a 50000.";
    public final static String MESSAGE_LOT_MAX =  "Le nombres de lot ne doit pas depasser 10 ou On ne doit pas etre 0.";
    public final static String MESSAGE_PRIX =  "Le montant ne dois pas etre negatif (Prix min ou Prix max).";
    public final static String OBSERVATION_DS_FICHIER = "";
    public final static String MSG_CONF_SR = "Les statistiques ont été correctement réinitialisées.";

    
    public final static int NBRE_DROIT_PASS = 10;
    public final static int NBRE_DE_SERVICE = 5;
    public final static int MAX_SUPERFICE = 50000;
    
    /** 
     * seuil de la taxe scolaire en un seul versement
     */
    public final static BigDecimal VAL_TAX_SCO_MAX = new BigDecimal(500.00);
    public final static String OBSERVATION_TAX_SCO = "La taxe scolaire payable par le propriétaire nécessite deux versements.";
    
    /** 
     * seuil de la taxe municipale en un seul versement
     */
    public final static BigDecimal VAL_TAX_MUN_MAX = new BigDecimal(1000.00);
    public final static String OBSERVATION_TAX_MUN = "La taxe municipale payable par le propriétaire nécessite deux versements.";
    
    /** 
     * seuil de l'écart maximal entre les dates de mesure
     * 6mois = 365/2 = 182.5 jours
     */
    public final static double VAL_ECART_MAX = 182.5;
    public final static String OBSERVATION_ECART_DATES = "L'écart maximal entre les dates de mesure des lots "
                                                        + "d'un même terrain devrait être de moins de 6 mois.";

    /** 
     * seuil du valeur par lot consideré trop dispandieux
     */
     public final static BigDecimal VAL_LOT_MAX = new BigDecimal(45000.00);
     public final static String OBSERVATION_VAL_LOT1 = "La valeur par lot du ";
     public final static String OBSERVATION_VAL_LOT2 = " est trop dispendieuse.";
     
     /** 
     * seuil de superficie par lot consideré trop grand (en mètres carrés)
     */
     public final static int SUPERFICIE_LOT_MAX = 45000;
     public final static String OBSERVATION_SUPERF_LOT1 = "La superficie par lot du ";
     public final static String OBSERVATION_SUPERF_LOT2 = " est trop grande.";
     
    /** 
     * seuil de la valeur foncière totale 
     */
    public final static BigDecimal VAL_FONC_TOT_MAX = new BigDecimal(300000.00);
    public final static String OBSERVATION_VAL_FONC = "La valeur foncière totale dépasse 300000.00$";
    
    /** 
     * seuil du rapport des prix maximum/minimum 
     */
    public final static BigDecimal RAPPORT_PRIX_MAX = new BigDecimal(2.00);
    public final static String OBSERVATION_PRIX_MAX = "Le prix maximum au mètre carré est plus que deux fois le prix minimum";
    
    /** 
     * montant fixe ajouté à la valeur foncière pour couvrir la valeur de base
     */
    protected static final double MONTANT_VALEUR_BASE = 733.77;
    
    /** 
     * taux de la taxe municipale : % de la valeur foncière totale 
     */
    protected static final double TAUX_TAXE_MUNICIPALE = 0.025;
    
    /** 
     * taux de la taxe scolaire : % de la valeur foncière totale 
     */
    protected static final double TAUX_TAXE_SCOLAIRE = 0.012;
    
    /** 
     * fichier ou seront gardée les statistiques 
     */
    protected static final String NOM_FICHIER_STATS = "statistiques.json";
    protected static int nbLots;
    protected static int nbLotsMoins;
    protected static int nbLotsEntre;
    protected static int nbLotsPlus;
    protected static int nbLotsType0;
    protected static int nbLotsType1;
    protected static int nbLotsType2;
    protected static int superficieMax;
    protected static BigDecimal valeurLotMax;
    
    /**
     * 
     * methode Main
     *
     * @param args nom du fichier d'entrée, nom du fichier de sortie
     *
     * @throws java.io.IOException
     * @throws evaluationfonciere.ValeurInvalideException
     */
    @SuppressWarnings({"null", "UnusedAssignment"})
    public static void main(String[] args) throws IOException, ValeurInvalideException {

        Terrain terrain = null;
        valeurLotMax = new BigDecimal(0);

        if (args.length != 2 && args.length != 3) {
            System.err.println("NOMBRE_PARAMETRE_FILE");
            System.exit(-1);
        }
        
        JSONObject entrefile = null;
        JSONObject sortiefile = new JSONObject();
        JSONObject statistiques = new JSONObject();
        try {
            String myJSON = ReadWritetoFile.loadFileIntoString(args[0], "UTF-8");
            entrefile = JSONObject.fromObject(myJSON); 
            statistiques = LireStatistiques(statistiques);
            
        } catch (IOException e) {
            System.err.println("fichier en entrée invalide");
            System.exit(-1);
        }
        
        InitialiserStatistiques(args, statistiques);
        
        int typeTerrain = entrefile.getInt("type_terrain");
        terrain = Terrain.creerTerrain(typeTerrain,entrefile);
      if (terrain == null)
                EcrireJsonsortie(sortiefile,MSG_ERR_TYPE_TERRAIN,args[1]);
        
        if((terrain.lots.isEmpty()) || (terrain.lots.size() > 9)) {     
           EcrireJsonsortie(sortiefile,MESSAGE_LOT_MAX,args[1]);
               
        }
        
       if (terrain.prixMax.compareTo(new BigDecimal(0)) < 0 && terrain.prixMax.compareTo(new BigDecimal(0)) < 0) {
                EcrireJsonsortie(sortiefile,MESSAGE_PRIX,args[1]);
       }  
         
        try {
             
           VerifieErreur(terrain.lots, "nombre_droits_passage",NBRE_DROIT_PASS, MESSAGE_DROIT_PASS);
           VerifieErreur(terrain.lots, "superficie",MAX_SUPERFICE,MESSAGE_SURPERFICIE);
           VerifieErreur(terrain.lots, "superficie",MAX_SUPERFICE,MESSAGE_SURPERFICIE);
        } catch (ValeurInvalideException ex) {
            EcrireJsonsortie(sortiefile,ex.getMessage(),args[1]);
          
        }
        
        terrain.CalculerValeurFonciereTotale();
        terrain.CalculerTaxeScolaire();
        terrain.CalculerTaxeMunicipale();
 
        terrain.ObservationValeurLot();
        terrain.ObservationSuperficieLot();
        terrain.ObservationTaxMunicipale();        
        terrain.ObservationTaxScolaire(); 
        terrain.ObservationPrixMaximum();
        try {
            terrain.ObservationDatesLot();
        } catch (ParseException ex) {
            Logger.getLogger(EvaluationFonciere.class.getName()).log(Level.SEVERE, null, ex);
        }
        terrain.ObservationValeurFonciereTotale();
        
        sortiefile.put("valeur_foncière_totale", Terrain.convertirMnt05(terrain.valeurFoncTot));
        sortiefile.put("taxe_scolaire", Terrain.convertirMnt05(terrain.taxeScolaire));
        sortiefile.put("taxe_Municipale", Terrain.convertirMnt05(terrain.taxeMunicipale));
        sortiefile.put("lotissements", terrain.CreerLotissements());
        if (!terrain.observations.isEmpty())
                sortiefile.put("observations", terrain.observations);
        statistiques = EnregistrerFichierStats(statistiques);
        
        try {
            ReadWritetoFile.saveStringIntoFile(args[1], sortiefile.toString());
        } catch (IOException e) {
            System.err.println(PAS_DE_REPO  + e.getMessage());
            System.exit(0);
        }
       
    }
         
    
    /**
     * @param lotis taille du lot dans le fichier d'entre
     * @param donneAverifie la donnee qu'on veut verifier dans le fichier d entre
     * @param NbreMax la valeur max que  donneAverifie peut prendre.
     * @param message d erreur a affichier 
     * @throws evaluationfonciere.ValeurInvalideException 
     */
      public static void  VerifieErreur(JSONArray lotis, String donneAverifie, int NbreMax, String message)
              throws ValeurInvalideException{  
         
          JSONObject leslots;
          for(int i = 0; i < lotis.size(); ++i) {
                leslots = lotis.getJSONObject(i);
                if ((leslots.getInt(donneAverifie) > NbreMax) ||  (leslots.getInt(donneAverifie) < 0))  
                               throw new ValeurInvalideException(message);
                      } 
          
             }
      
      
      
      public static void EcrireJsonsortie(JSONObject file, String messageText, String sortie) throws IOException {
      
       file.put("message",messageText );
            ReadWritetoFile.saveStringIntoFile(sortie, file.toString());
            System.exit(0);
      
      }
   
   /**
     * 
     * @param statistiques
     * @return statistiques
     */
      public static JSONObject LireStatistiques(JSONObject statistiques)  {
       
        try {
            String myJSON = ReadWritetoFile.loadFileIntoString(NOM_FICHIER_STATS, "UTF-8");
            statistiques = JSONObject.fromObject(myJSON);
        } catch (IOException ex) {
            System.err.println("Erreur fichier de statistiques.");
        }
          
          return statistiques;
      } 
      
    /**
     * Initialisation si pas de switch
     * @param args
     * @param stats
     * @return stats
     * 
     */
      public static JSONObject InitialiserStatistiques(String[] args, JSONObject stats)  {
          if (args.length == 2) {
            InitialiserStatTaille(stats); 
            InitialiserStatType(stats);
          } else  stats = LireSwitch(args,stats);    
          return stats;
      }
      
    /**
     * Initialisation des variables avec valeurs lus
     * 
     * @param stats
     * 
     */
    private static void InitialiserStatTaille(JSONObject stats) {
        nbLots = stats.getInt("nombre_lots");
        nbLotsMoins = stats.getInt("nombre_moins_1000");
        nbLotsEntre = stats.getInt("nombre_entre");
        nbLotsPlus = stats.getInt("nombre_plus_10000");
        valeurLotMax = Terrain.lirePrix(stats.getString("valeur_par_lot_max"));
    }

    /**
     * Initialisation des variables avec valeurs lus
     * 
     * @param stats
     * 
     */
    private static void InitialiserStatType(JSONObject stats) {
        nbLotsType0 = stats.getInt("nombre_type0");
        nbLotsType1 = stats.getInt("nombre_type1");
        nbLotsType2 = stats.getInt("nombre_type2");
        superficieMax  = stats.getInt("superficie_max");
    }

    /**
     * choix d'action selon switch reçu en argument
     * 
     * @param args
     * 
     */
    private static JSONObject LireSwitch(String[] args,JSONObject statistiques) {
        if (args.length == 3 && args[2].equals("-SR")) 
            statistiques = reinitialiserStats(statistiques);
         else if (args.length == 3 && args[2].equals("-S")) 
            afficherStats(statistiques);
        return statistiques;
    }

     /**
     * réinitialisation selon switch reçu en argument
     * 
     * 
     */
    private static JSONObject reinitialiserStats(JSONObject statistiques) {
        ReinitialiserStatTaille();
        ReinitialiserStatType();
        statistiques = EnregistrerFichierStats(statistiques);
        ConfirmerReinitialisation();
        return statistiques;
    }

    private static void afficherStats(JSONObject stats) {
        InitialiserStatTaille(stats); 
        InitialiserStatType(stats);
        afficherStatTaille();
        afficherStatType();
    }

    private static void ReinitialiserStatTaille() {
        nbLots = 0;
        nbLotsMoins = 0;
        nbLotsEntre = 0;
        nbLotsPlus = 0;
        valeurLotMax = new BigDecimal(0);
    }

    private static void ReinitialiserStatType() {
        nbLotsType0 = 0;
        nbLotsType1 = 0;
        nbLotsType2 = 0;
        superficieMax  = 0;
    }

    private static JSONObject  EnregistrerFichierStats(JSONObject statistiques) {
        
            statistiques = SauverStatTaille(statistiques);
            statistiques = SauverStatType(statistiques);
            EcrireFichierStats(statistiques);
            
            return statistiques;
        
    }

    private static void ConfirmerReinitialisation() {
        System.out.println(MSG_CONF_SR);
    }

    private static JSONObject  SauverStatTaille(JSONObject stats) {
         stats.put("nombre_lots", nbLots);
         stats.put("nombre_moins_1000",nbLotsMoins);
         stats.put("nombre_entre",nbLotsEntre);
         stats.put("nombre_plus_10000",nbLotsPlus);
         stats.put("valeur_par_lot_max",Terrain.convertirMnt05(valeurLotMax));
        return stats;
    }

    private static JSONObject  SauverStatType(JSONObject stats) {
        stats.put("nombre_type0",nbLotsType0);
        stats.put("nombre_type1",nbLotsType1);
        stats.put("nombre_type2",nbLotsType2);
        stats.put("superficie_max",superficieMax);
        return stats;
    }

    private static void EcrireFichierStats(JSONObject statistiques) {
          
    File _fichier = new File(NOM_FICHIER_STATS);
        try {
            FileUtils.writeStringToFile(_fichier, statistiques.toString(), "UTF-8");
        } catch (IOException e) {
            
        }


}

    private static void afficherStatTaille() {
        System.out.println("Statistiques des demandes");
        System.out.println("\tNombre total des lots : " + nbLots);
        System.out.println("\tLots de moins de 1000$: " + nbLotsMoins);
        System.out.println("\tLots de 1000$ à 10000$: " + nbLotsEntre);
        System.out.println("\tLots de plus de 10000$: " + nbLotsPlus);
  
    }

    private static void afficherStatType() {
        System.out.println("\tLots de type Agricole   : " + nbLotsType0);
        System.out.println("\tLots de type Residentiel: " + nbLotsType1);
        System.out.println("\tLots de type Commercial : " + nbLotsType2);
        System.out.println("\tSuperficie maximale     : " + superficieMax);
        System.out.println("\tvaleur maximale         : " + Terrain.convertirMnt01(valeurLotMax));
    }
    
    
}