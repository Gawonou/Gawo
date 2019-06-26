package evaluationfonciere;



/* 
 * @author N'TSOUAGLO Kokou Gawonou
 * 
 * @version 2018-10-01
 * 
 * Copyright(c) 2018  Kokou.  All Rights Reserved.
 */

import java.io.IOException;
import java.math.BigDecimal;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class AgricoleTest {

    String json = null;
    JSONObject objetJson = null;
    BigDecimal prixMin = null;
    BigDecimal prixMax = null;
    Agricole terrainAgricole;
    JSONArray lots = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        json = ReadWritetoFile.loadFileIntoString("entrefile.json", "UTF-8");
        objetJson = JSONObject.fromObject(json);
        terrainAgricole = new Agricole(objetJson);
        prixMin = Agricole.lirePrix(objetJson.getString("prix_m2_min"));
        prixMax = Agricole.lirePrix(objetJson.getString("prix_m2_max"));
        lots = terrainAgricole.lots;
    }

    @After
    public void tearDown() {
    }

    @Test
    public void calculPrixAvecValeurMinTest() throws ValeurInvalideException{
        terrainAgricole = new Agricole(objetJson);
        prixMin = Agricole.lirePrix(objetJson.getString("prix_m2_min"));
        prixMax = Agricole.lirePrix(objetJson.getString("prix_m2_max"));
        BigDecimal _valEsperee = prixMin;
        BigDecimal _valActuelle = terrainAgricole.calculPrix(prixMin, prixMax);
        assertEquals(_valEsperee, _valActuelle);
    }

    @Test(expected = NullPointerException.class)
    public void calculPrixAvecNullTest() throws ValeurInvalideException{
        BigDecimal _valEsperee = new BigDecimal(0);
        BigDecimal _valActuelle = terrainAgricole.calculPrix(null, new BigDecimal("7.00"));
        assertEquals(_valEsperee, _valActuelle);
    }
    
    @Test(expected = NumberFormatException.class)
    public void calculPrixAvecValInvalideTest() throws ValeurInvalideException{
        BigDecimal _valEsperee = new BigDecimal(2);
        BigDecimal _valActuelle = terrainAgricole.calculPrix(new BigDecimal("25f"), new BigDecimal("7.00"));
        assertEquals(_valEsperee, _valActuelle);
    }

    @Test
    public void calculDroitdePassageTest() throws ValeurInvalideException {
        assertEquals(Agricole.arrondirMnt05(new BigDecimal(117.5)), 
                terrainAgricole.calculDuDroitDePassage(3, BigDecimal.valueOf(2550)));
    }
    
    @Test(expected = ValeurInvalideException.class)
    public void calculDroitdePassageAvecValeurNegativeTest() throws ValeurInvalideException{
        assertEquals(Agricole.arrondirMnt05(new BigDecimal(0)), 
                terrainAgricole.calculDuDroitDePassage(-3, BigDecimal.valueOf(450)));
    }
    
    @Test(expected = NullPointerException.class)
    public void calculDroitdePassageAvecValeurNullTest() throws ValeurInvalideException{
        assertEquals(Agricole.arrondirMnt05(new BigDecimal(117.5)), 
                terrainAgricole.calculDuDroitDePassage(0, null));
    }
    
    @Test(expected = NumberFormatException.class)
    public void calculDroitdePassageAvecValeurInvalideTest() throws ValeurInvalideException{
        assertEquals(Agricole.arrondirMnt05(new BigDecimal(117.5)), 
                terrainAgricole.calculDuDroitDePassage(Integer.parseInt("2f"), BigDecimal.valueOf(350)));
    }

    @Test
    public void calculMontantPourServicesTest() throws ValeurInvalideException {
            assertEquals(BigDecimal.valueOf(0),terrainAgricole.calculDuMontantDeServices(344.90, 3));        
    }
    
    @Test (expected = ValeurInvalideException.class)
    public void calculMontantPourServicesAvecValeurNegativeTest() throws ValeurInvalideException {
            assertEquals(BigDecimal.valueOf(0),terrainAgricole.calculDuMontantDeServices(-344.90, 3));        
    }
    
    @Test (expected = NumberFormatException.class)
    public void calculMontantPourServicesAvecValeurNullTest() throws ValeurInvalideException {
            assertEquals(BigDecimal.valueOf(0),terrainAgricole.calculDuMontantDeServices(Double.parseDouble("null"), 3));        
    }
}