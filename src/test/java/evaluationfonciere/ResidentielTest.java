package evaluationfonciere;


/* 
 * @author N'TSOUAGLO Kokou Gawonou
 * 
 * @version 2018-10-01
 * 
 * Copyright(c) 2018 Kokou.  All Rights Reserved.
 */


import java.io.IOException;
import java.math.BigDecimal;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResidentielTest {
    
     String json = null;
    JSONObject objetJson = null;
    Residentiel terrain = null;
    
    @Before
    public void setUp() throws IOException {
        json = ReadWritetoFile.loadFileIntoString("entrefile.json", "UTF-8");
        objetJson = JSONObject.fromObject(json);
        terrain = new Residentiel(objetJson);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void calculPrix() throws ValeurInvalideException {
        assertEquals(BigDecimal.valueOf(3.0), terrain.calculPrix(BigDecimal.valueOf(2.0), BigDecimal.valueOf(4.0)) );
        assertEquals(BigDecimal.valueOf(6.0), terrain.calculPrix(BigDecimal.valueOf(2.0), BigDecimal.valueOf(10)) );
    }

    @Test(expected = ValeurInvalideException.class)
    public void calculPrixAvecValeurNegativeTest() throws ValeurInvalideException {
        assertEquals(-3.0, terrain.calculPrix(BigDecimal.valueOf(-2.0), BigDecimal.valueOf(-4.0)));
    }
    
    @Test(expected = NullPointerException.class)
    public void calculPrixAvecValeurNullTest() throws ValeurInvalideException {
        assertEquals(10, terrain.calculPrix(null, BigDecimal.valueOf(2.0)));
    }

    @Test(expected = NumberFormatException.class)
    public void calculPrixAvecFormatInvalideTest() throws ValeurInvalideException {
        assertEquals(0, terrain.calculPrix(BigDecimal.valueOf(Double.parseDouble("null")), BigDecimal.valueOf(2.0) ) );
    }

    @Test
    public void calculDuDroitDePassageSimpleTest() throws ValeurInvalideException {
        assertEquals(Residentiel.arrondirMnt05(BigDecimal.valueOf(428.6)), terrain.calculDuDroitDePassage(2, BigDecimal.valueOf(357)) );
    }

    @Test(expected = NullPointerException.class)
    public void calculDuDroitDePassageAvecFormatInvalideTest() throws ValeurInvalideException {
        assertEquals(0, terrain.calculDuDroitDePassage(3, null));
    }

    @Test(expected = ValeurInvalideException.class)
    public void calculDuDroitDePassageAvecValeurInvalideTest() throws ValeurInvalideException {
        assertEquals(0, terrain.calculDuDroitDePassage(9, BigDecimal.valueOf(-2.0)));
    }

    @Test
    public void calculServicesSimpleTest() throws ValeurInvalideException {
        assertEquals(BigDecimal.valueOf(3000), terrain.calculDuMontantDeServices(788, 4));

        assertEquals(BigDecimal.valueOf(0), terrain.calculDuMontantDeServices(345, 2));
    }

    @Test(expected = ValeurInvalideException.class)
    public void calculServicesAvecValeurNegativeTest() throws ValeurInvalideException {
        assertEquals(0, terrain.calculDuMontantDeServices(-500, 3));
    }
}
