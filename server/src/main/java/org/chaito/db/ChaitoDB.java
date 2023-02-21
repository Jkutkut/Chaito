package org.chaito.db;

import org.chaito.ClientThread;
import org.chaito.Server;
import org.chaito.model.Msg;

import java.util.ArrayList;

public class ChaitoDB extends AccessDB {
    private static final String FILE_PATH = "db.properties";

    public ChaitoDB() {
        super(FILE_PATH);
    }

    public ArrayList<Msg> getAll() {
        String query = "SELECT * FROM MSGS;";
        return sqlite2msg(SQLiteQuery.get(this, 4, query));
    }

    public ArrayList<Msg> getMsgs(String target) {
        String query = "SELECT * FROM MSGS WHERE target_user = ? or target_user = '" + Server.ALL_TARGET + "' or sender_user = ?;";
        return sqlite2msg(SQLiteQuery.get(this, 4, query, target, target));
    }

    public int insert(Msg msg) {
        String query = "INSERT INTO MSGS (target_user, sender_user, msg) VALUES (?, ?, ?);";
        return SQLiteQuery.execute(this, query, msg.getTarget(), msg.getSender(), msg.getMsg());
    }

    private ArrayList<Msg> sqlite2msg(ArrayList<Object[]> data) {
        ArrayList<Msg> msgs = new ArrayList<>();
        for (Object[] row : data) {
            msgs.add(new Msg((String) row[1], (String) row[2], (String) row[3]));
        }
        return msgs;
    }
}
