import bd.ConnectionFactory;
import dao.*;
import modelo.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Conectando com o banco
        ConnectionFactory fabricaDeConexao = new ConnectionFactory();
        Connection connection = fabricaDeConexao.recuperaConexao();

        // Criando DAOs
        UsuarioDAO udao = new UsuarioDAO(connection);
        DenunciaDAO ddao = new DenunciaDAO(connection);
        PontoDeReferenciaDAO pdao = new PontoDeReferenciaDAO(connection);
        EnderecoFixoDAO edao = new EnderecoFixoDAO(connection);
        CoordenadasDAO coodao = new CoordenadasDAO(connection);
        MidiaDAO mdao = new MidiaDAO(connection);
        ComentarioDAO cdao = new ComentarioDAO(connection);

        // Iniciando testes
        Localizacao l1 = new PontoDeReferencia("Rio de janeiro", "RJ", "Pedra grande no fim da esquina", "lá");
        Localizacao l2 = new EnderecoFixo("Rio de Janeiro", "RJ", "20000-000", "Rua das Flores", "123", "Centro");
        Localizacao l3 = new Coordenadas("Rio de Janeiro", "RJ", -22.9068, -43.1729);
        Usuario u1 = new Usuario("Eduardo Peruzzo", "eduardo@email.com", "jorge");
        Usuario u2 = new Usuario("Jorge", "jorge@gmail.com", "1234");
        Usuario u3 = new Usuario("Estevão", "estevao@email.com", "jorge2");
        Denuncia d1 = new Denuncia(u1, "Buraco enorme na frente da minha casa", Categoria.BURACO_NA_RUA, "Não consigo sair com o carro por causa desse buracão", l1, LocalDateTime.now());
        Denuncia d2 = new Denuncia(u1, "Buraco enorme na frente da casa do vizinho", Categoria.BURACO_NA_RUA, "Não consigo sair com o carro por causa desse buracão", l2, LocalDateTime.now());
        Denuncia d3 = new Denuncia(u3, "Buraco enorme na frente da casa do vizinho", Categoria.BURACO_NA_RUA, "Não consigo sair com o carro por causa desse buracão", l3, LocalDateTime.now());
        Midia m1 = new Midia(d1, "https://www.youtube.com", "Youtube");
        Midia m2 = new Midia(d2, "https://www.instagram.com", "Instagram");
        Midia m3 = new Midia(d3, "https://www.facebook.com", "Facebook");
        Comentario c1 = new Comentario(u1, d1, "Esse buraco é muito perigoso!", LocalDateTime.now());
        Comentario c2 = new Comentario(u2, d1, "Concordo, é muito perigoso!", LocalDateTime.now());

        u1.persistirUsuario(connection);
        u2.persistirUsuario(connection);
        u3.persistirUsuario(connection);
        d1.addMidia(m1);
        d2.addMidia(m2);
        d3.addMidia(m3);
        d1.persistirDenuncia(connection);
        d2.persistirDenuncia(connection);
        d3.persistirDenuncia(connection);
        c1.persistirComentario(connection);
        c2.persistirComentario(connection);
        pdao.salvar(l1);
        edao.salvar(l2);
        coodao.salvar(l3);
        mdao.salvar(m1);
        mdao.salvar(m2);
        mdao.salvar(m3);

        d1.receberVoto(u1, 9);
        d1.receberVoto(u2, 8);
        d1.receberVoto(u3, 10);
        d1.receberConfirmacao(u3);
        ddao.atualizar(d1);

        ArrayList<Object> denuncias = ddao.listarTodosEagerLoading();

        System.out.println();
        System.out.println("Denúncias cadastradas no banco:");
        System.out.println("========================================");
        for (Object o : denuncias) {
            Denuncia denuncia = (Denuncia) o;
            System.out.println(denuncia);
            System.out.println(denuncia.getLocalizacao().formatar());
            System.out.println("Midias dessa denúncia:");
            for (Midia m : denuncia.getMidias()) {
                System.out.println(m.getUrl());
                System.out.println(m.getLegenda());
            }
            System.out.println();
        }

        ArrayList<Object> usuarios = udao.listarTodosEagerLoading();

        System.out.println("Usuários cadastrados no banco:");
        System.out.println("========================================");
        for (Object o : usuarios) {
            Usuario usuario = (Usuario) o;
            System.out.println(usuario);
            System.out.println();
        }

        ArrayList<Object> comentarios = cdao.listarTodosEagerLoading();

        System.out.println("Comentários cadastrados no banco:");
        System.out.println("========================================");
        for (Object o : comentarios) {
            Comentario comentario = (Comentario) o;
            System.out.println("Conteúdo: " + comentario.getConteudo());
            System.out.println("Denúncia: " + comentario.getDenuncia().getTitulo());
            System.out.println("Usuário: " + comentario.getAutor().getNome());
            System.out.println("Data: " + comentario.getData());
            System.out.println();
        }
    }
}