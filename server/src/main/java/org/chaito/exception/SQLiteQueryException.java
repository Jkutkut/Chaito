package org.chaito.exception;

/**
 * Clase para excepciones relacionadas con SQLiteQuery.
 */
public class SQLiteQueryException extends InvalidDataException{
    public SQLiteQueryException(String s) {
        super(s);
    }
}
