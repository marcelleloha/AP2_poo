package dao;

import modelo.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DenunciaDAO implements BaseDAO{
    private Connection connection;

    public DenunciaDAO(Connection connection) {
        this.connection = connection;
    }


    public void salvar(Object objeto) {

        if (!(objeto instanceof Denuncia)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Denuncia.");
        }

        Denuncia denuncia = (Denuncia) objeto;

        try {
            String sql = "INSERT INTO denuncia (idCriador, titulo, categoria, descricao, dtDenuncia) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.setInt(1, denuncia.getCriador().getIdUsuario());
                pstm.setString(2, denuncia.getTitulo());
                pstm.setObject(3, denuncia.getCategoria());
                pstm.setString(4, denuncia.getDescricao());
                pstm.setObject(5, denuncia.getData());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        denuncia.setIdDenuncia(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Metodo ainda não está retornando os telefones da pessoa
    @Override
    public Denuncia buscarPorId(int id) {
        UsuarioDAO udao = new UsuarioDAO(connection);

        Denuncia denuncia = null;

        Localizacao localizacao = null;

        try {
            String sql = """
            SELECT d.idDenuncia, d.idCriador, d.titulo, d.categoria, d.descricao, d.dtDenuncia, ef.idEndereco, co.idCoordenada, pr.idPonto, v.valor_voto, v.idUsuario as idVotante, c.idUsuario as idConfirmador, m.idMidia
            FROM denuncia as d
            LEFT JOIN endereco_fixo as ef on ef.idDenuncia = d.idDenuncia
            LEFT JOIN ponto_referencia as pr on pr.idDenuncia = d.idDenuncia
            LEFT JOIN coordenadas as co on co.idDenuncia = d.idDenuncia
            LEFT JOIN voto_prioridade as v on v.idDenuncia = d.idDenuncia
            LEFT JOIN confirmacoes as c on c.idDenuncia = d.idDenuncia
            LEFT JOIN midia as m on m.idDenuncia = d.idDenuncia
            WHERE d.idDenuncia = ?;
        """;

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);
                pstm.execute();

                try (ResultSet rst = pstm.getResultSet()) {
                    while (rst.next()) {
                        if (denuncia == null) {
                            int idDenuncia = rst.getInt("idDenuncia");
                            int idCriador = rst.getInt("idCriador");
                            Usuario criador = (Usuario) udao.buscarPorId(idCriador);

                            String titulo = rst.getString("titulo");
                            Categoria categoria = Categoria.valueOf(rst.getString("categoria"));
                            String descricao = rst.getString("descricao");
                            LocalDateTime dtDenuncia = rst.getObject("dtDenuncia", LocalDateTime.class);

                            if (rst.getInt("idEndereco") != 0) {
                                int idEndereco = rst.getInt("idEndereco");
                                EnderecoFixoDAO edao = new EnderecoFixoDAO(connection);
                                localizacao = (EnderecoFixo) edao.buscarPorId(idEndereco);

                            } else if (rst.getInt("idPonto") != 0) {
                                int idPonto = rst.getInt("idPonto");
                                PontoDeReferenciaDAO pdao = new PontoDeReferenciaDAO(connection);
                                localizacao = (PontoDeReferencia) pdao.buscarPorId(idPonto);

                            } else if (rst.getInt("idCoordenada") != 0) {
                                int idCoordenada = rst.getInt("idCoordenada");
                                CoordenadasDAO cdao = new CoordenadasDAO(connection);
                                localizacao = (Coordenadas) cdao.buscarPorId(idCoordenada);

                            }

                            denuncia = new Denuncia(idDenuncia, criador, titulo, categoria, descricao, localizacao, dtDenuncia);
                        }

                        // Adiciona voto
                        int idVotante = rst.getInt("idVotante");
                        if (idVotante != 0) {
                            Usuario votante = (Usuario) udao.buscarPorId(idVotante);
                            int valor = rst.getInt("valor_voto");
                            denuncia.receberVoto(votante, valor);
                        }

                        // Adiciona confirmação
                        int idConfirmador = rst.getInt("idConfirmador");
                        if (idConfirmador != 0) {
                            Usuario confirmador = (Usuario) udao.buscarPorId(idConfirmador);
                            denuncia.receberConfirmacao(confirmador);
                        }

                        // Adiciona mídia
                        int idMidia = rst.getInt("idMidia");
                        if (idMidia != 0) {
                            MidiaDAO mdao = new MidiaDAO(connection);
                            Midia midia = (Midia) mdao.buscarPorId(idMidia);
                            denuncia.addMidia(midia);
                        }
                    }
                }
            }

            return denuncia;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Object> listarTodosLazyLoading() {
        UsuarioDAO udao = new UsuarioDAO(connection);
        ArrayList<Object> denuncias = new ArrayList<>();

        try {
            String sql = "SELECT idDenuncia, idCriador, titulo, categoria, descricao, dtDenuncia FROM denuncia";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idDenuncia = rst.getInt("idDenuncia");
                    int idCriador = rst.getInt("idCriador");
                    Usuario criador = (Usuario) udao.buscarPorId(idCriador);

                    String titulo = rst.getString("titulo");
                    Categoria categoria = Categoria.valueOf(rst.getString("categoria"));
                    String descricao = rst.getString("descricao");
                    LocalDateTime dtDenuncia = rst.getObject("dtDenuncia", LocalDateTime.class);

                    Denuncia d = new Denuncia(idDenuncia, criador, titulo, categoria, descricao, null, dtDenuncia);
                    denuncias.add(d);
                }
            }
            return denuncias;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Object> listarTodosEagerLoading() {
        UsuarioDAO udao = new UsuarioDAO(connection);
        MidiaDAO mdao = new MidiaDAO(connection);
        CoordenadasDAO cdao = new CoordenadasDAO(connection);
        PontoDeReferenciaDAO pdao = new PontoDeReferenciaDAO(connection);
        EnderecoFixoDAO edao = new EnderecoFixoDAO(connection);

        ArrayList<Object> denuncias =new ArrayList<>();
        Denuncia ultima = null;

        Localizacao localizacao = null;

        try {
            String sql = """
            SELECT d.idDenuncia, d.idCriador, d.titulo, d.categoria, d.descricao, d.dtDenuncia, ef.idEndereco, co.idCoordenada, pr.idPonto, v.valor_voto, v.idUsuario as idVotante, c.idUsuario as idConfirmador, m.idMidia
            FROM denuncia as d
            LEFT JOIN endereco_fixo as ef on ef.idDenuncia = d.idDenuncia
            LEFT JOIN ponto_referencia as pr on pr.idDenuncia = d.idDenuncia
            LEFT JOIN coordenadas as co on co.idDenuncia = d.idDenuncia
            LEFT JOIN voto_prioridade as v on v.idDenuncia = d.idDenuncia
            LEFT JOIN confirmacoes as c on c.idDenuncia = d.idDenuncia
            LEFT JOIN midia as m on m.idDenuncia = d.idDenuncia;
        """;

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();

                try (ResultSet rst = pstm.getResultSet()) {
                    while (rst.next()) {
                        if (ultima == null || ultima.getIdDenuncia() != rst.getInt(1)) {
                            int idDenuncia = rst.getInt("idDenuncia");
                            int idCriador = rst.getInt("idCriador");
                            Usuario criador = (Usuario) udao.buscarPorId(idCriador);

                            String titulo = rst.getString("titulo");
                            Categoria categoria = Categoria.valueOf(rst.getString("categoria"));
                            String descricao = rst.getString("descricao");
                            LocalDateTime dtDenuncia = rst.getObject("dtDenuncia", LocalDateTime.class);

                            if (rst.getInt("idEndereco") != 0) {
                                int idEndereco = rst.getInt("idEndereco");
                                localizacao = (EnderecoFixo) edao.buscarPorId(idEndereco);

                            } else if (rst.getInt("idPonto") != 0) {
                                int idPonto = rst.getInt("idPonto");
                                localizacao = (PontoDeReferencia) pdao.buscarPorId(idPonto);

                            } else if (rst.getInt("idCoordenada") != 0) {
                                int idCoordenada = rst.getInt("idCoordenada");
                                localizacao = (Coordenadas) cdao.buscarPorId(idCoordenada);

                            }

                            Denuncia denuncia = new Denuncia(idDenuncia, criador, titulo, categoria, descricao, localizacao, dtDenuncia);
                            denuncias.add(denuncia);
                            ultima = denuncia;
                        }

                        // Adiciona voto
                        int idVotante = rst.getInt("idVotante");
                        if (idVotante != 0) {
                            Usuario votante = (Usuario) udao.buscarPorId(idVotante);
                            int valor = rst.getInt("valor_voto");
                            ultima.receberVoto(votante, valor);
                        }

                        // Adiciona confirmação
                        int idConfirmador = rst.getInt("idConfirmador");
                        if (idConfirmador != 0) {
                            Usuario confirmador = (Usuario) udao.buscarPorId(idConfirmador);
                            ultima.receberConfirmacao(confirmador);
                        }

                        // Adiciona mídia
                        int idMidia = rst.getInt("idMidia");
                        if (idMidia != 0) {
                            Midia midia = (Midia) mdao.buscarPorId(idMidia);
                            ultima.addMidia(midia);
                        }
                    }
                }
            }

            return denuncias;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            String sql = "DELETE FROM denuncia WHERE id = ?";

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
