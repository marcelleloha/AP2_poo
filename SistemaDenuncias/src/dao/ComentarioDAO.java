package dao;

import modelo.Comentario;
import modelo.Denuncia;
import modelo.EnderecoFixo;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class ComentarioDAO {
    private Connection connection;

    public ComentarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Object objeto){

        if (!(objeto instanceof Comentario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Comentario.");
        }

        Comentario comentario = (Comentario) objeto;

        try{
            String sql = "INSERT INTO endereco_fixo (idAutor, idDenuncia, conteudo, data) VALUES(?,?,?,?)";

            try(PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

                pstm.setInt(1, comentario.getAutor().getIdUsuario()); //Define o primeiro ?
                pstm.setInt(2, comentario.getDenuncia().getIdDenuncia()); //Define o segundo ?
                pstm.setString(3, comentario.getConteudo()); //Define o terceiro ?
                pstm.setDate(4, comentario.getData());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        comentario.setIdComentario(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Comentario buscarPorId(int id){
        UsuarioDAO udao = new UsuarioDAO(connection);
        DenunciaDAO ddao = new DenunciaDAO(connection);

        Comentario comentario = null;

        try {

            String sql ="""
                        SELECT idAutor, idDenuncia, conteudo, data
                        FROM comentario
                        WHERE idComentario = ?
                        """;
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);
                pstm.execute();
                try(ResultSet rst = pstm.getResultSet()){
                    while (rst.next()){
                        if (comentario == null){
                            String conteudo = rst.getString("conteudo");
                            Date data = rst.getDate("data");
                            int idAutor = rst.getInt("idAutor");
                            int idDenuncia = rst.getInt("idDenuncia");
                            Usuario autor = (Usuario) udao.buscarPorId(idAutor);
                            Denuncia denuncia = ddao.buscarPorId(idDenuncia);

                            comentario = new Comentario(id, autor, denuncia, conteudo, data);
                        }
                    }
                }
            }
            return comentario;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Object> listarTodosLazyLoading() {
        UsuarioDAO udao = new UsuarioDAO(connection);
        DenunciaDAO ddao = new DenunciaDAO(connection);
        ArrayList<Object> comentarios = new ArrayList<>();

        try {
            String sql = "SELECT idAutor, idDenuncia, conteudo, data FROM comentario";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idAutor = rst.getInt("idAutor");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Usuario autor = (Usuario) udao.buscarPorId(idAutor);
                    Denuncia denuncia = ddao.buscarPorId(idDenuncia);
                    String conteudo = rst.getString("conteudo");
                    Date data = rst.getDate("data");

                    Comentario comentario = new Comentario(autor, denuncia, conteudo, data);
                    comentarios.add(comentario);
                }
            }
            return comentarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Object objeto) {
        if (!(objeto instanceof Comentario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Comentario.");
        }

        Comentario comentario = (Comentario) objeto;

        // Atualizar os dados principais da denúncia
        String sql = "UPDATE comentario SET idAutor = ? and idDenuncia = ? and conteudo = ? and data = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, comentario.getAutor().getIdUsuario());
            pstm.setInt(2, comentario.getDenuncia().getIdDenuncia());
            pstm.setString(3, comentario.getConteudo());
            pstm.setDate(4, comentario.getData());

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
            String sql = "DELETE FROM comentario WHERE idComentario = ?";

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
