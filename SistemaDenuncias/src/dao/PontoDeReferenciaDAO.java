package dao;

import modelo.Denuncia;
import modelo.PontoDeReferencia;

import java.sql.*;
import java.util.ArrayList;

public class PontoDeReferenciaDAO implements BaseDAO {
    private Connection connection;

    public PontoDeReferenciaDAO(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof PontoDeReferencia)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Ponto de Referencia.");
        }

        PontoDeReferencia pontoDeReferencia = (PontoDeReferencia) objeto;

        try {
            String sql = "INSERT INTO ponto_referencia (idDenuncia, nomePonto, descricaoPonto, cidade, estado) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setInt(1, pontoDeReferencia.getDenuncia().getIdDenuncia());
                pstm.setString(2, pontoDeReferencia.getNome());
                pstm.setString(3, pontoDeReferencia.getDescricao());
                pstm.setString(4, pontoDeReferencia.getCidade());
                pstm.setString(5, pontoDeReferencia.getEstado());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        pontoDeReferencia.setIdPonto(rst.getInt(1));
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
            String sql = "SELECT idPonto, idDenuncia, cidade, estado, nomePonto, descricaoPonto FROM ponto_referencia WHERE idPonto = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idPonto = rst.getInt("idPonto");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Denuncia denuncia = (Denuncia) ddao.buscarPorId(idDenuncia);
                    String cidade = rst.getString("cidade");
                    String estado = rst.getString("estado");
                    String nomePonto = rst.getString("nomePonto");
                    String descricaoPonto = rst.getString("descricaoPonto");
                    return new PontoDeReferencia(idPonto, denuncia, cidade, estado, nomePonto, descricaoPonto);
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
            String sql = "idPonto, idDenuncia, cidade, estado, nomePonto, descricaoPonto FROM ponto_referencia";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idPonto = rst.getInt("idPonto");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Denuncia denuncia = (Denuncia) ddao.buscarPorId(idDenuncia);
                    String cidade = rst.getString("cidade");
                    String estado = rst.getString("estado");
                    String nomePonto = rst.getString("nomePonto");
                    String descricaoPonto = rst.getString("descricaoPonto");

                    PontoDeReferencia p = new PontoDeReferencia(idPonto, denuncia, cidade, estado, nomePonto, descricaoPonto);
                    coordenadas.add(p);
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
        if (!(objeto instanceof PontoDeReferencia)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Ponto de Referencia.");
        }

        PontoDeReferencia pontoDeReferencia = (PontoDeReferencia) objeto;

        String sql = "UPDATE ponto_referencia SET idDenuncia = ?, cidade = ?, estado = ?, nomePonto = ?, descricaoPonto = ? WHERE idPonto = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, pontoDeReferencia.getDenuncia().getIdDenuncia());
            pstm.setString(2, pontoDeReferencia.getCidade());
            pstm.setString(3, pontoDeReferencia.getEstado());
            pstm.setString(4, pontoDeReferencia.getNome());
            pstm.setString(5, pontoDeReferencia.getDescricao());

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar Ponto de Referencia: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM ponto_referencia WHERE idPonto = ?";

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

