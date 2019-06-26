package evaluationfonciere;





import java.io.IOException;
import java.math.BigDecimal;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class Observations {
    String _json = null;
    JSONObject _objetJson = null;
    int _typeTerrain;
    Terrain _terrain;
    
    public Observations() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IOException {
        _json = ReadWritetoFile.loadFileIntoString("entrefile.json", "UTF-8");
        _objetJson = JSONObject.fromObject(_json);
        _typeTerrain = _objetJson.getInt("type_terrain");
        _terrain = Terrain.creerTerrain(_typeTerrain,_objetJson);
        
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void valeurParLot() {
    }
}
