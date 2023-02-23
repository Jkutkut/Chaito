package org.chaito.exception;

import java.io.Serial;

/**
 * Exception thrown when the data is invalid.
 */
public class InvalidDataException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDataException(String s) {
        super(s);
    }

    public InvalidDataException(String s, int min, int max) {
        super(String.format("%s [%d, %d]", s, min, max));
    }

    public InvalidDataException(String s, Object[] arr) {
        super(addArrayToStr(s, arr));
    }

    private static String addArrayToStr(String s, Object[] arr) {
        StringBuilder arrStr = new StringBuilder("{");
        if (arr != null && arr.length > 0) {
            arrStr.append(" ").append(arr[0]);
            for (int i = 1; i < arr.length; i++) {
                arrStr.append(", ").append(arr[i]);
            }
        }
        arrStr.append("}");

        return String.format(s, arrStr);
    }
}
