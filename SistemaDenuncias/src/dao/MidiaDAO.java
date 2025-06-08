package dao;

import modelo.Midia;

import java.sql.*;
import java.util.ArrayList;

public class MidiaDAO implements BaseDAO {
    private Connection connection;

    public MidiaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Midia)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Midia.");
        }

        Midia midia = (Midia) objeto;

        try {
            String sql = "INSERT INTO midia (url, legenda) VALUES (?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.setObject(1, midia.getUrl());
                pstm.setString(2, midia.getLegenda());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        midia.setIdMidia(rst.getInt(1));
                    }

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object buscarPorId(int id) {
        try {
            String sql = "SELECT idMidia, url, legenda FROM midia WHERE idMidia = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int identificador = rst.getInt("idMidia");
                    String url = rst.getString("url");
                    String legenda = rst.getString("legenda");
                    return new Midia(identificador, url, legenda);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {

        ArrayList<Object> midias = new ArrayList<>();

        try {
            String sql = "SELECT idMidia, url, legenda FROM midia";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int identificador = rst.getInt("idMidia");
                    String url = rst.getString("url");
                    String legenda = rst.getString("legenda");
                    Midia m = new Midia(identificador, url, legenda);
                    midias.add(m);
                }
            }
            return midias;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        return listarTodosLazyLoading();
    }

    @Override
    public ArrayList<Object> atualizar(Object objeto) {

        ArrayList<Object> midias = new ArrayList<>();

        try {
            String sql = "SELECT idMidia, url, legenda FROM midia";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int identificador = rst.getInt("idMidia");
                    String url = rst.getString("url");
                    String legenda = rst.getString("legenda");
                    String senha = rst.getString("senha");
                    Midia m = new Midia(identificador, url, legenda);
                    midias.add(m);
                }
            }
            return midias;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM midia WHERE id = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

                int linhasAfetadas = pstm.executeUpdate();

                if (linhasAfetadas == 0) {
                    throw new SQLException("Falha ao deletar: nenhuma linha foi afetada.");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

