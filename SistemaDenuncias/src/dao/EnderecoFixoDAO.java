package dao;

import modelo.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EnderecoFixoDAO {
    private Connection connection;

    public EnderecoFixoDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Object objeto){

        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Usuario.");
        }

        EnderecoFixo endereco = (EnderecoFixo) objeto;

        try{
            String sql = "INSERT INTO endereco_fixo (cidade, estado, cep, rua, numero, bairro) VALUES(?,?,?,?,?,?)";

            try(PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

                pstm.setString(1, endereco.getCidade()); //Define o primeiro ?
                pstm.setString(2, endereco.getEstado()); //Define o segundo ?
                pstm.setString(3, endereco.getCep()); //Define o terceiro ?
                pstm.setString(4, endereco.getRua());
                pstm.setString(5, endereco.getNumero());
                pstm.setString(6, endereco.getBairro());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        endereco.setIdEndereco(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeEnderecoIgual(EnderecoFixo endereco){
        try{
            String sql ="""
                        SELECT *
                        FROM endereco_fixo as ef
                        WHERE ef.cep = ? and ef.numero = ? and ef.bairro = ? and ef.cidade = ? and ef.estado = ? and rua = ?
                        """;
            try(PreparedStatement pstm = connection.prepareStatement(sql)){
                pstm.setString(1, endereco.getCep());
                pstm.setString(2, endereco.getNumero());
                pstm.setString(3, endereco.getBairro());
                pstm.setString(4, endereco.getCidade());
                pstm.setString(5, endereco.getEstado());
                pstm.setString(6, endereco.getRua());

                try (ResultSet rst = pstm.executeQuery()) {
                    return rst.next(); // Retorna true se encontrou ao menos uma linha
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public EnderecoFixo buscarPorId(int id){
        EnderecoFixo endereco = null;

        try {

            String sql ="""
                        SELECT cidade, estado, cep, rua, numero, bairro
                        FROM endereco_fixo
                        WHERE idEndereco = ?
                        """;
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);
                pstm.execute();
                try(ResultSet rst = pstm.getResultSet()){
                    while (rst.next()){
                        if (endereco == null){
                            String cidade = rst.getString("cidade");
                            String estado = rst.getString("estado");
                            String cep = rst.getString("cep");
                            String rua = rst.getString("rua");
                            String numero = rst.getString("numero");
                            String bairro = rst.getString("bairro");

                            endereco = new EnderecoFixo(cidade, estado, cep, rua, numero, bairro);
                        }
                    }
                }
            }
            return endereco;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> enderecos = new ArrayList<>();

        try {
            String sql = "SELECT cidade, estado, cep, rua, numero, bairro FROM endereco_fixo";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    String cidade = rst.getString("cidade");
                    String estado = rst.getString("estado");
                    String cep = rst.getString("cep");
                    String rua = rst.getString("rua");
                    String numero = rst.getString("numero");
                    String bairro = rst.getString("bairro");


                    EnderecoFixo endereco = new EnderecoFixo(cidade, estado, cep, rua, numero, bairro);
                    enderecos.add(endereco);
                }
            }
            return enderecos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Object objeto) {
        if (!(objeto instanceof EnderecoFixo)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo EnderecoFixo.");
        }

        EnderecoFixo endereco = (EnderecoFixo) objeto;

        // Atualizar os dados principais da denúncia
        String sql = "UPDATE endereco_fixo SET cep = ? and numero = ? and bairro = ? and cidade = ? and estado = ? and rua = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, endereco.getCep());
            pstm.setString(2, endereco.getNumero());
            pstm.setString(3, endereco.getBairro());
            pstm.setString(4, endereco.getCidade());
            pstm.setString(5, endereco.getEstado());
            pstm.setString(6, endereco.getRua());

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar denuncia: " + e.getMessage());
        }
    }

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
