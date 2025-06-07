package dao;

import java.sql.Connection;

public class CoordenadasDAO {
    private Connection connection;

    public CoordenadasDAO(Connection connection) {
        this.connection = connection;
    }
}
