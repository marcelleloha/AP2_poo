package dao;

import modelo.Coordenadas;
import modelo.Denuncia;
import modelo.Midia;

import java.sql.*;
import java.util.ArrayList;

public class CoordenadasDAO implements BaseDAO {
    private Connection connection;

    public CoordenadasDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Coordenadas)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Coordenada.");
        }

        Coordenadas coordenadas = (Coordenadas) objeto;

        try {
            String sql = "INSERT INTO coordenadas (idDenuncia, latitude, longitude) VALUES (?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setObject(1, coordenadas.getDenuncia().getIdDenuncia());
                pstm.setDouble(2, coordenadas.getLatitude());
                pstm.setDouble(3, coordenadas.getLongitude());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        coordenadas.setIdCoordenada(rst.getInt(1));
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
            String sql = "SELECT idCoordenada, idDenuncia, cidade, estado, latitude, longitude FROM coordenada WHERE idCoordenada = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idCoordenada = rst.getInt("idCoordenada");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Denuncia denuncia = (Denuncia) ddao.buscarPorId(idDenuncia);
                    String cidade = rst.getString("cidade");
                    String estado = rst.getString("estado");
                    Double latitude = rst.getDouble("latitude");
                    Double longitude = rst.getDouble("longitude");
                    return new Coordenadas(idCoordenada, denuncia, cidade, estado, latitude, longitude);
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

        ArrayList<Object> coordenadas = new ArrayList<>();

        try {
            String sql = "SELECT idCoordenada, idDenuncia, cidade, estado, latitude, logitude FROM coordenadas";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idMidia = rst.getInt("idCoordenada");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Denuncia denuncia = (Denuncia) ddao.buscarPorId(idDenuncia);
                    String cidade = rst.getString("cidade");
                    String estado = rst.getString("estado");
                    Double latitude = rst.getDouble("cidade");
                    Double longitude = rst.getDouble("estado");

                    Coordenadas c = new Coordenadas(idMidia, denuncia, cidade, estado, latitude, longitude);
                    coordenadas.add(c);
                }
            }
            return coordenadas;
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
        if (!(objeto instanceof Coordenadas)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Coordenadas.");
        }

        Coordenadas coordenadas = (Coordenadas) objeto;

        String sql = "UPDATE coordenadas SET idDenuncia = ?, cidade = ?, estado = ?, latitude = ?, longitude = ? WHERE idCoordenada = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, coordenadas.getDenuncia().getIdDenuncia());
            pstm.setString(2, coordenadas.getCidade());
            pstm.setString(3, coordenadas.getEstado());
            pstm.setDouble(4, coordenadas.getLatitude());
            pstm.setDouble(5, coordenadas.getLongitude());

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar Coordenada: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM coordenadas WHERE idCoordenada = ?";

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
