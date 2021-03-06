
package  evaluationfonciere;
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

public class CommercialTest {
    
    String json = null;
    JSONObject objetJson = null;
    Commercial terrain = null;
    
    @Before
    public void setUp() throws IOException {
        json = ReadWritetoFile.loadFileIntoString("entrefile.json", "UTF-8");
        objetJson = JSONObject.fromObject(json);
        terrain = new Commercial(objetJson);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void calculPrixSimpleTest() throws ValeurInvalideException {
        assertEquals(BigDecimal.valueOf(10), terrain.calculPrix(BigDecimal.valueOf(2), BigDecimal.valueOf(10)));
    }
    
    @Test (expected = NullPointerException.class)
    public void calculPrixAvecValeurNullTest() throws ValeurInvalideException {
        assertEquals(10, terrain.calculPrix(BigDecimal.valueOf(2), null));
    }

    @Test(expected = NumberFormatException.class)
    public void calculPrixAvecFormatInvalideTest() throws ValeurInvalideException {
        assertEquals(0, terrain.calculPrix(BigDecimal.valueOf(2), 
                BigDecimal.valueOf(Double.parseDouble("null"))));
    }

    @Test(expected = ValeurInvalideException.class)
    public void calculPrixAvecValeurInvalideTest() throws ValeurInvalideException {
        assertEquals(0, terrain.calculPrix(BigDecimal.valueOf(2), BigDecimal.valueOf(-6.50)));
    }

    @Test
    public void calculDuDroitDePassageSimpleTest() throws ValeurInvalideException {
        assertEquals(BigDecimal.valueOf(392.95), terrain.calculDuDroitDePassage(2, BigDecimal.valueOf(357)));
    }

    @Test(expected = NullPointerException.class)
    public void calculDuDroitDePassageAvecFormatInvalideTest() throws ValeurInvalideException {
        assertEquals(0, terrain.calculDuDroitDePassage(3, null));
    }

    @Test(expected = ValeurInvalideException.class)
    public void calculDuDroitDePassageAvecValeurInvalideTest() throws ValeurInvalideException {
        assertEquals(0, terrain.calculDuDroitDePassage(9, BigDecimal.valueOf(-8000) ) );
    }

    @Test
    public void calculServicesSimpleTest() throws ValeurInvalideException {
        assertEquals(Commercial.arrondirMnt05(new BigDecimal(5000) ), terrain.calculDuMontantDeServices(788, 4));

        assertEquals(Commercial.arrondirMnt05(new BigDecimal(2000) ), terrain.calculDuMontantDeServices(345, 2));
    }

    @Test(expected = ValeurInvalideException.class)
    public void calculServicesAvecValeurNegativeTest() throws ValeurInvalideException {
        assertEquals(0, terrain.calculDuMontantDeServices(-500, 3));
    }
}
