package dao;

import modelo.Denuncia;
import modelo.Midia;
import modelo.Usuario;

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
            String sql = "INSERT INTO midia (idDenuncia, url, legenda) VALUES (?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setObject(1, midia.getDenuncia().getIdDenuncia());
                pstm.setObject(2, midia.getUrl());
                pstm.setString(3, midia.getLegenda());

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
        DenunciaDAO ddao = new DenunciaDAO(connection);
        try {
            String sql = "SELECT idMidia, idDenuncia, url, legenda FROM midia WHERE idMidia = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idMidia = rst.getInt("idMidia");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Denuncia denuncia = (Denuncia) ddao.buscarPorId(idDenuncia);
                    String url = rst.getString("url");
                    String legenda = rst.getString("legenda");
                    return new Midia(idMidia, denuncia, url, legenda);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object buscarPorId(int id, Denuncia denuncia) {
        DenunciaDAO ddao = new DenunciaDAO(connection);
        try {
            String sql = "SELECT idMidia, idDenuncia, url, legenda FROM midia WHERE idMidia = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idMidia = rst.getInt("idMidia");
                    String url = rst.getString("url");
                    String legenda = rst.getString("legenda");
                    return new Midia(idMidia, denuncia, url, legenda);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        DenunciaDAO ddao = new DenunciaDAO(connection);

        ArrayList<Object> midias = new ArrayList<>();

        try {
            String sql = "SELECT idMidia, idDenuncia, url, legenda FROM midia";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idMidia = rst.getInt("idMidia");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Denuncia denuncia = (Denuncia) ddao.buscarPorId(idDenuncia);
                    String url = rst.getString("url");
                    String legenda = rst.getString("legenda");
                    Midia m = new Midia(idMidia, denuncia, url, legenda);
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
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Midia)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Midia.");
        }

        Midia midia = (Midia) objeto;

        String sql = "UPDATE midia SET idDenuncia = ?, url = ?, legenda = ? WHERE idMidia = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, midia.getDenuncia().getIdDenuncia());
            pstm.setString(2, midia.getUrl());
            pstm.setString(3, midia.getLegenda());
            pstm.setInt(4, midia.getIdMidia());

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM midia WHERE idMidia = ?";

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

