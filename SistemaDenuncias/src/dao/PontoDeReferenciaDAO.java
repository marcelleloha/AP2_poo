package dao;

import java.sql.Connection;

public class PontoDeReferenciaDAO {
    private Connection connection;

    public PontoDeReferenciaDAO(Connection connection) {
        this.connection = connection;
    }
}
