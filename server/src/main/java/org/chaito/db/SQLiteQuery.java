package org.chaito.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.chaito.exception.SQLiteQueryException;

/**
 * Clase con lógica para simplificar la consulta y modificación de una base de datos SQLite.
 *
 * @author Jorge Re - Jkutkut
 */
public class SQLiteQuery {
    /**
     * Obtiene los valores de la base de datos requeridos.
     * @param db Base de datos a usar.
     * @param outputLength Tamaño que tiene cada resultado (número de columnas).
     * @param query String SQL con la sentencia SELECT a realizar.
     * @param input Conjunto de valores a sustituir en la query (las ?).
     * @return Arraylist con filas en forma de Array[Object] con el resultado.
     * @throws SQLiteQueryException
     */
    public static ArrayList<Object[]> getFromDB(AccessDB db, int outputLength, String query, Object[] input) throws SQLiteQueryException {
        ArrayList<Object[]> output = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rslt = null;

        try {
            con = db.getConnection();

            pstmt = con.prepareStatement(query);
            for (int i = 0; i < input.length; i++) { // Fill the query
                pstmt.setObject(i + 1, input[i]);
            }

            rslt = pstmt.executeQuery();

            while (rslt.next()) { // Get the results
                Object[] row = new Object[outputLength]; // New object with the colunms of a row.
                for (int i = 0; i < outputLength; i++) { // Fill the row
                    row[i] = rslt.getObject(i + 1);
                }
                output.add(row); // Add the row to the rows
            }
        }
        catch (ClassNotFoundException e) {
            throw new SQLiteQueryException("El driver indicado no es correcto:\n" + e.getMessage());
        }
        catch (SQLException e) {
            throw new SQLiteQueryException("Error en la base de datos:\n" + e.getMessage());
        }
        finally {
            try {
                if (rslt != null) rslt.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new SQLiteQueryException("Error al cerrar las conexiones:\n" + e.getMessage());
            }
        }
        return output;
    }

    /**
     * Función protectora de getFromDB para poder habilitar la entrada de argumentos de manera variable.
     * @param db Base de datos a usar.
     * @param outputLength Tamaño que tiene cada resultado (número de columnas).
     * @param query String SQL con la sentencia SELECT a realizar.
     * @param input Conjunto de valores a sustituir en la query (las ?).
     * @return Arraylist con filas en forma de Array[Object] con el resultado.
     * @throws SQLiteQueryException
     */
    public static ArrayList<Object[]> get(AccessDB db, int outputLength, String query, Object... input) throws SQLiteQueryException {
        return getFromDB(db, outputLength, query, input);
    }

    /**
     * Ejecuta un cambio en la base de datos.
     * @param db Base de datos a usar.
     * @param query Query deseada
     * @param input Conjunto de valores a sustituir en la query (las ?)
     * @return Código de resultado dado por la base de datos.
     * @throws SQLiteQueryException
     */
    public static int execute(AccessDB db, String query, Object... input) throws SQLiteQueryException {
        int result;

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = db.getConnection();

            pstmt = con.prepareStatement(query);

            for (int i = 0; i < input.length; i++) {
                pstmt.setObject(i + 1, input[i]);
            }

            result = pstmt.executeUpdate();
        }
        catch (ClassNotFoundException e) {
            throw new SQLiteQueryException("El driver indicado no es correcto:\n" + e.getMessage());
        }
        catch (SQLException e) {
            throw new SQLiteQueryException("Error en la base de datos:\n" + e.getMessage());
        }
        finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            }
            catch (SQLException e) {
                throw new SQLiteQueryException("Error al cerrar las conexiones:\n" + e.getMessage());
            }
        }

        return result;
    }
}