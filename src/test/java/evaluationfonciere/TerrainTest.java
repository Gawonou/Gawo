
package  evaluationfonciere;
/* 
 * @author N'TSOUAGLO Kokou Gawonou
 
 * @version 2018-10-01
 * 
 * Copyright(c) 2018  Kokou.  All Rights Reserved.
 */



import java.io.IOException;
import java.math.BigDecimal;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TerrainTest {

    public TerrainTest() {
    }

    String json = "";
    JSONObject objetJson = null;
    Agricole valeurDeTestAgricole = null;

    @Before
    public void setUp() throws IOException {
        json = ReadWritetoFile.loadFileIntoString("entrefile.json", "UTF-8");
        objetJson = JSONObject.fromObject(json);
        valeurDeTestAgricole = new Agricole(objetJson);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void lirePrixTest() {
        
        assertEquals(new BigDecimal(3.5), Agricole.lirePrix("3,5 $"));
        assertEquals(new BigDecimal(3.5), Agricole.lirePrix("3.5 $"));
        assertEquals(new BigDecimal(-2), Agricole.lirePrix("-2"));
    }

    @Test
    public void creerTerrain() {
        assertTrue(valeurDeTestAgricole instanceof Terrain); 
    }
    
    @Test(expected = NullPointerException.class)
    public void lirePrixAvecValeurNullTest() {
        assertEquals(null, Agricole.lirePrix(null));
    }

    @Test
    public void arroundirMntTest() {

        assertEquals(BigDecimal.valueOf(5.50).setScale(2), Agricole.arrondirMnt05(new BigDecimal(5.46)));

        assertEquals(BigDecimal.valueOf(5.55), Agricole.arrondirMnt05(new BigDecimal(5.55)));

    }

    /*
    Lance un NumberFormatException pour toutes les valeurs esperees 
    passees en parametre. Tant que la methode aroundirMnt(BigDecimal valeur) 
    recoit null en parametre, une exception est toujours lancee.
     */
    @Test(expected = NumberFormatException.class)
    public void arroundirMntAvecNullTest() {
        assertEquals(null, Agricole.arrondirMnt05(new BigDecimal("null")));
    }

    //Problem ici avec la valeur 178.30
    @Test
    public void convertirMnt() {
        assertEquals("3.00 $", Agricole.convertirMnt05(new BigDecimal(3)));        
        assertEquals("178.25 $", Agricole.convertirMnt05(new BigDecimal(178.25)));
        
        assertEquals("178.25 $", Agricole.convertirMnt01(new BigDecimal(178.25)));
        assertEquals("178.30 $", Agricole.convertirMnt01(new BigDecimal(178.30)));
    }
    
    @Test
    public void calculValeurFonciereDunLotTest() throws ValeurInvalideException{
       BigDecimal actuel = valeurDeTestAgricole.calculValeurFonciereDuLot(objetJson.getJSONArray("lotissements").getJSONObject(0));
       assertEquals(Agricole.arrondirMnt05(new BigDecimal(1943.83)), actuel);
    }
    
   // @Test (expected = ValeurInvalideException.class)
   // public void calculValeurFonciereDunLotAvecValeurInvalideTest1() throws ValeurInvalideException{
   //    BigDecimal actuel = valeurDeTestAgricole.calculValeurFonciereDuLot(objetJson.getJSONArray("lotissements").getJSONObject(0));
   //    assertEquals(Agricole.arrondirMnt05(new BigDecimal(1943.83)), actuel);
  //  }
    
  //  @Test (expected = JSONException.class)
   // public void calculValeurFonciereDunLotAvecValeurInvalideTest2() throws ValeurInvalideException{
   //    BigDecimal actuel = valeurDeTestAgricole.calculValeurFonciereDuLot(objetJson.getJSONArray("lotissements").getJSONObject(0));
   //    assertEquals(Agricole.arrondirMnt05(new BigDecimal(1943.83)), actuel);
   // }
    
    @Test
    public void calculerTaxeScolaireTest() throws ValeurInvalideException{
        valeurDeTestAgricole.CalculerValeurFonciereTotale();
        valeurDeTestAgricole.CalculerTaxeScolaire();    
        
        assertEquals(Agricole.arrondirMnt01(new BigDecimal(480.62)), valeurDeTestAgricole.taxeScolaire);
    }
    
    @Test
    public void calculerTaxeMunicipaleTest() throws ValeurInvalideException{
        valeurDeTestAgricole.CalculerValeurFonciereTotale();
        valeurDeTestAgricole.CalculerTaxeMunicipale();
        
        assertEquals(Agricole.arrondirMnt01(new BigDecimal(1001.30)), valeurDeTestAgricole.taxeMunicipale);
    }
    
    @Test
    public void creerTerrainTest() {        
        assertTrue("should be Agricole", Terrain.creerTerrain(0, objetJson) instanceof Agricole);
        assertTrue("should be Residentiel", Terrain.creerTerrain(1, objetJson) instanceof Residentiel);
        assertTrue("should be Commercial", Terrain.creerTerrain(2, objetJson) instanceof Commercial);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void creerTerrainAvecValeurInvalideTest() {        
        assertThat("nouveau type de terrain creer", new Agricole(objetJson), equalTo(Terrain.creerTerrain(4, objetJson)) );
    }
}
