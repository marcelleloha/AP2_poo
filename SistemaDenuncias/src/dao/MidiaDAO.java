package dao;

import java.sql.Connection;

public class MidiaDAO {
    private Connection connection;

    public MidiaDAO(Connection connection) {
        this.connection = connection;
    }
}
