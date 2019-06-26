/**
 * @author N'TSOUAGLO Kokou Gawonou
 * @author GAVIDIA, Cristian
 * @author de MEDEIROS Ricardo Julio
 *
 * Cours INF2015
 *
 * @version 2018-12-02 Copyright(c) 2018 Cristian & Kokou. All Rights Reserved.
 */

package  evaluationfonciere;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ReadWritetoFile {

    /**
     * Méthode qui lit le fichier JSON en entrée
     *
     * @param filePath
     * @param fileEncoding
     *
     * @throws FileNotFoundException retourne une erreur si le fichier n'existe
     * pas
     * @throws IOException retourne une erreur general avec un message si le
     * fichier n'existe pas
     *
     * @return IOUtils.toString();
     *
     */
    public static String loadFileIntoString(String filePath, String fileEncoding)
            throws FileNotFoundException, IOException {
        return IOUtils.toString(new FileInputStream(filePath), fileEncoding);
    }

    /**
     * Méthode qui sauvegarde le fichier JSON en sortie
     *
     * @param FilePath nom du fichier de sauvegarde
     * @param ContentToSave Contenu du fichier de sauvegarde
     * @return e.getMessage() si une exception est levee
     * @throws IOException retourne une erreur avec un message indiquant
     * l'erreur
     *
     */
    public static String saveStringIntoFile(String FilePath, String ContentToSave)
            throws IOException {
        File _fichier = new File(FilePath);
        try {
            FileUtils.writeStringToFile(_fichier, ContentToSave, "UTF-8");
        } catch (IOException e) {
            return e.getMessage();
        }
        return "Fichier enregistre avec succes.";
    }

}
