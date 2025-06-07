package dao;

import java.sql.Connection;

public class EnderecoFixoDAO {
    private Connection connection;

    public EnderecoFixoDAO(Connection connection) {
        this.connection = connection;
    }
}
