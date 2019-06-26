package  evaluationfonciere;

/**
 * @author N'TSOUAGLO Kokou Gawonou
 *
 * @version 2018-12-02 Copyright(c) 2018 Kokou. All Rights
 * Reserved.
 */


import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class LectureEtEcritureDuFichierTest {
    
    @Test(expected = FileNotFoundException.class)
    public void lectureFichierAvecValeurInvalideTest() throws IOException{
        assertEquals("entrefile", ReadWritetoFile.loadFileIntoString("en.json", "UTF-8"));
    }
}
