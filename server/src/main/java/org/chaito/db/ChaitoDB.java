package org.chaito.db;

import org.chaito.model.Msg;

import java.util.ArrayList;

public class ChaitoDB extends AccessDB {
    private static final String FILE_PATH = "db.properties";

    public ChaitoDB() {
        super(FILE_PATH);
    }

    private ArrayList<Msg> sqlite2msg(ArrayList<Object[]> data) {
        ArrayList<Msg> msgs = new ArrayList<>();
        for (Object[] row : data) {
            msgs.add(new Msg((String) row[0], (String) row[1], (String) row[2]));
        }
        return msgs;
    }

    public static void main(String[] args) {
        ChaitoDB db = new ChaitoDB();
    }
}
