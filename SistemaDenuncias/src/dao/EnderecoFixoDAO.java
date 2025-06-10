package dao;

import modelo.Denuncia;
import modelo.EnderecoFixo;
import modelo.Midia;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class EnderecoFixoDAO implements BaseDAO {
    private Connection connection;

    public EnderecoFixoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof EnderecoFixo)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Endereço Fixo.");
        }

        EnderecoFixo enderecoFixo = (EnderecoFixo) objeto;

        try {
            String sql = "INSERT INTO endereco_fixo (idDenuncia, cep, rua, numero, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setObject(1, enderecoFixo.getDenuncia().getIdDenuncia());
                pstm.setString(2, enderecoFixo.getCep());
                pstm.setString(3, enderecoFixo.getRua());
                pstm.setString(4, enderecoFixo.getNumero());
                pstm.setString(5, enderecoFixo.getBairro());
                pstm.setString(6, enderecoFixo.getCidade());
                pstm.setString(7, enderecoFixo.getEstado());

                pstm.execute();
                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        enderecoFixo.setIdEndereco(rst.getInt(1));
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
            String sql = "SELECT idEndereco, idDenuncia, cidade, estado, cep, rua, numero, bairro FROM endereco_fixo WHERE idEndereco = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idEndereco = rst.getInt("idEndereco");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Denuncia denuncia = (Denuncia) ddao.buscarPorId(idDenuncia);
                    String cidade = rst.getString("cidade");
                    String estado = rst.getString("estado");
                    String cep = rst.getString("cep");
                    String rua = rst.getString("rua");
                    String numero = rst.getString("numero");
                    String bairro = rst.getString("bairro");


                    return new EnderecoFixo(idEndereco, denuncia, cidade, estado, cep, rua, numero, bairro);
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

        ArrayList<Object> enderecos = new ArrayList<>();

        try {
            String sql = "SELECT idEndereco, idDenuncia, cidade, estado, cep, rua, numero, bairro FROM endereco_fixo";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idEndereco = rst.getInt("idEndereco");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Denuncia denuncia = (Denuncia) ddao.buscarPorId(idDenuncia);
                    String cidade = rst.getString("cidade");
                    String estado = rst.getString("estado");
                    String cep = rst.getString("cep");
                    String rua = rst.getString("rua");
                    String numero = rst.getString("numero");
                    String bairro = rst.getString("bairro");
                    EnderecoFixo e = new EnderecoFixo(idEndereco, denuncia, cidade, estado, cep, rua, numero, bairro);
                    enderecos.add(e);
                }
            }
            return enderecos;
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
        if (!(objeto instanceof EnderecoFixo)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Midia.");
        }

        EnderecoFixo enderecoFixo = (EnderecoFixo) objeto;

        String sql = "UPDATE endereco_fixo SET idDenuncia = ?, cidade = ?, estado = ?, cep = ?, rua = ?, numero = ?, bairro = ? WHERE idMidia = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, enderecoFixo.getDenuncia().getIdDenuncia());
            pstm.setString(2, enderecoFixo.getCidade());
            pstm.setString(3, enderecoFixo.getEstado());
            pstm.setString(5, enderecoFixo.getCep());
            pstm.setString(6, enderecoFixo.getRua());
            pstm.setString(7, enderecoFixo.getNumero());
            pstm.setString(3, enderecoFixo.getBairro());

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar endereco: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM endereco_fixo WHERE idEndereco = ?";

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


