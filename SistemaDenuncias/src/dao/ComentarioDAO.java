package dao;

import modelo.Comentario;
import modelo.Denuncia;
import modelo.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

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
            String sql = "INSERT INTO comentario (idUsuario, idDenuncia, texto, dtComentario) VALUES(?,?,?,?)";

            try(PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

                pstm.setInt(1, comentario.getAutor().getIdUsuario()); //Define o primeiro ?
                pstm.setInt(2, comentario.getDenuncia().getIdDenuncia()); //Define o segundo ?
                pstm.setString(3, comentario.getConteudo()); //Define o terceiro ?
                pstm.setObject(4, comentario.getData());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        comentario.setIdComentario(rst.getInt(1));
                    }
                }

                salvarVotos(comentario);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void salvarVotos(Comentario comentario){
        try{
            String sql = "INSERT INTO voto_comentario (idUsuario, idDenuncia) VALUES(?,?)";
            for (Usuario usuario : comentario.getVotos()) {
                try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstm.setInt(1, usuario.getIdUsuario());
                    pstm.setInt(2, comentario.getDenuncia().getIdDenuncia());
                    pstm.execute();
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
        String conteudo;
        LocalDateTime data;
        int idAutor;
        int idDenuncia;
        Usuario autor;
        Denuncia denuncia;
        Set<Usuario> votos;

        try {

            String sql ="""
                        SELECT c.idUsuario, c.idDenuncia, c.texto, c.dtComentario, v.idUsuario as idVotante
                        FROM comentario as c
                        LEFT JOIN voto_comentario as v ON v.idComentario = c.idComentario
                        WHERE idComentario = ?
                        """;
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);
                pstm.execute();
                try(ResultSet rst = pstm.getResultSet()){
                    while (rst.next()){
                        if (comentario == null){
                            conteudo = rst.getString("texto");
                            data = rst.getObject("dtComentario", LocalDateTime.class);
                            idAutor = rst.getInt("idUsuario");
                            idDenuncia = rst.getInt("idDenuncia");
                            autor = (Usuario) udao.buscarPorId(idAutor);
                            denuncia = ddao.buscarPorId(idDenuncia);

                            comentario = new Comentario(id, autor, denuncia, conteudo, data);
                        }

                        int idUsuario = rst.getInt("idUsuario");
                        if(idUsuario != 0){
                            Usuario usuario = (Usuario) udao.buscarPorId(idUsuario);
                            comentario.receberVoto(usuario, 1);
                        }

                    }

                    return comentario;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Object> listarTodosLazyLoading() {
        UsuarioDAO udao = new UsuarioDAO(connection);
        DenunciaDAO ddao = new DenunciaDAO(connection);
        ArrayList<Object> comentarios = new ArrayList<>();

        try {
            String sql = "SELECT idAutor, idDenuncia, texto, dtComentario FROM comentario";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idAutor = rst.getInt("idAutor");
                    int idDenuncia = rst.getInt("idDenuncia");
                    Usuario autor = (Usuario) udao.buscarPorId(idAutor);
                    Denuncia denuncia = ddao.buscarPorId(idDenuncia);
                    String conteudo = rst.getString("texto");
                    LocalDateTime data = rst.getObject("dtComentario", LocalDateTime.class);

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
        String sql = "UPDATE comentario SET idUsuario = ? and idDenuncia = ? and texto = ? and dtComentario = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, comentario.getAutor().getIdUsuario());
            pstm.setInt(2, comentario.getDenuncia().getIdDenuncia());
            pstm.setString(3, comentario.getConteudo());
            pstm.setObject(4, comentario.getData());

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }

            excluirVotos(comentario);
            salvarVotos(comentario);

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

    public void excluirVotos(Comentario comentario) {
        String sql;

        try {
            sql = "DELETE FROM voto_comentario WHERE idComentario = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, comentario.getIdComentario());

                int linhasAfetadas = pstm.executeUpdate();

                if (linhasAfetadas == 0) {
                    System.out.println("Aviso: a denúncia ainda não possui votos, ou ocorreu um erro, pois nenhuma linha foi afetada pela atualização.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}