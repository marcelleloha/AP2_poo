package dao;

import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class UsuarioDAO implements BaseDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }


    public void salvar(Object objeto) {

        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Usuario.");
        }

        Usuario usuario = (Usuario) objeto;

        try {
            String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.setString(1, usuario.getNome()); //Define o primeiro ?
                pstm.setString(2, usuario.getEmail()); //Define o segundo ?
                pstm.setString(3, usuario.getSenha()); //Define o terceiro ?

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        usuario.setIdUsuario(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Metodo ainda não está retornando os telefones da pessoa
    @Override
    public Object buscarPorId(int id) {

        try {
            String sql = "SELECT idUsuario, nome, email, senha FROM usuario WHERE idUsuario = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int identificador = rst.getInt("idUsuario");
                    String nome = rst.getString("nome");
                    String email = rst.getString("email");
                    String senha = rst.getString("senha");
                    return new Usuario(identificador, nome, email, senha);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Usuario buscarPorNomeEmail(String nomeUsuario, String emailUsuario) {

        try {
            String sql = "SELECT idUsuario, nome, email, senha FROM usuario WHERE nome = ? and email = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, nomeUsuario);
                pstm.setString(2, emailUsuario);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int identificador = rst.getInt("idUsuario");
                    String nome = rst.getString("nome");
                    String email = rst.getString("email");
                    String senha = rst.getString("senha");
                    return new Usuario(identificador, nome, email, senha);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeUsuarioIgual(Usuario usuario) {
        String sql = "SELECT 1 FROM usuario WHERE nome = ? AND email = ? LIMIT 1";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, usuario.getNome());
            pstm.setString(2, usuario.getEmail());
            try (ResultSet rst = pstm.executeQuery()) {
                return rst.next(); // true se encontrou algum usuário igual
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Object> listarTodosLazyLoading() {

        ArrayList<Object> usuarios = new ArrayList<>();

        try {
            String sql = "SELECT idUsuario, nome, email, senha FROM usuario";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int identificador = rst.getInt("idUsuario");
                    String nome = rst.getString("nome");
                    String email = rst.getString("email");
                    String senha = rst.getString("senha");
                    Usuario u = new Usuario(identificador, nome, email, senha);
                    usuarios.add(u);
                }
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Object> listarTodosEagerLoading() {
        return listarTodosLazyLoading();
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Usuario.");
        }

        Usuario usuario = (Usuario) objeto;

        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE idUsuario = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, usuario.getNome());
            pstm.setString(2, usuario.getEmail());
            pstm.setString(3, usuario.getSenha());
            pstm.setInt(4, usuario.getIdUsuario());

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
            String sql = "DELETE FROM usuario WHERE idUsuario = ?";

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
