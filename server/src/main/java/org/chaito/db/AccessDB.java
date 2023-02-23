package org.chaito.db;

import org.chaito.exception.InvalidDataException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase con la lógica necesaria para poder acceder a una base de datos SQL.
 *
 * @author Jorge Re - Jkutkut
 */
public class AccessDB {
    private String driver;
    private String url;

    public AccessDB(String dbFile) {
        String error = null;

        Properties properties = new Properties();
        InputStream is = null;

        try {
            is = new FileInputStream(dbFile);
            properties.load(is);

            if (!validPropertiesFile(properties))
                throw new InvalidDataException("El archivo properties no es válido.");
            this.driver = properties.getProperty("DRIVER");

            String[] paths = properties.getProperty("PATHS").split(":");
            this.url = null;

            for (String p : paths) {
                if (fileExist(p + properties.getProperty("DB_NAME"))) {
                    this.url = properties.getProperty("URL_TYPE") + p + properties.getProperty("DB_NAME");
                    break;
                }
            }
            if (this.url == null)
                throw new InvalidDataException("BBDD no encontrada.");
        }
        catch (FileNotFoundException e) {
            error = "El archivo " + dbFile + " no existe.";
        }
        catch (IOException e) {
            error = "No ha sido posible leer el archivo " + dbFile;
        }
        catch (InvalidDataException e) {
            error = e.getMessage();
        }
        finally {
            try {
                if (is != null) is.close();
            }
            catch (IOException e) {
                error = "Error cerrando el archivo " + dbFile;
            }
        }

        if (error != null)
            throw new InvalidDataException(error);
    }

    /**
     * Obtiene la conexión con la base de datos.
     * @return Connection object con la conexión.
     * @throws ClassNotFoundException Si no se encontró la clase con el driver especificado.
     * @throws SQLException Si la base de datos tiene algún problema obteniendo la conexión.
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url);
    }

    /**
     * Verifica que todos los campos necesarios para la clase se encuentran en el archivo.properties.
     * @param properties Archivo properties ya listo para su lectura.
     * @return True si es válido, false en caso contrario.
     */
    private static boolean validPropertiesFile(Properties properties) {
        final String[] requiredFields = {"DRIVER", "URL_TYPE", "DB_NAME", "PATHS"};
        for (String s : requiredFields)
            if (properties.getProperty(s) == null)
                return false;
        return true;
    }

    // Help functions

    /**
     * Verifica que el archivo dado como URL existe.
     * @param path Localización del archivo.
     * @return El resultado de la comprobación.
     */
    private static boolean fileExist(String path) {
        Path pathFile = Path.of(path);
        return Files.exists(pathFile);
    }
}
