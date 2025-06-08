import bd.ConnectionFactory;
import dao.DenunciaDAO;
import dao.UsuarioDAO;
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

        // Iniciando testes
        Localizacao local = new PontoDeReferencia("Rio de janeiro", "RJ", "Pedra grande no fim da esquina", "lá");

        Usuario u1 = new Usuario("Eduardo Peruzzo", "eduardo@email.com", "jorge");
        Denuncia d1 = new Denuncia(u1, "Buraco enorme na frente da minha casa", Categoria.BURACO_NA_RUA, "Não consigo sair com o carro por causa desse buracão", local, LocalDateTime.now());

        udao.salvar(u1);
        d1.persistirDenuncia(connection);

        d1.receberVoto(u1, 9);
        d1.receberVoto(u1, 8);

        Usuario u2 = new Usuario("Estevão", "estevao@email.com", "jorge2");

        d1.receberVoto(u2, 10);
        d1.receberConfirmacao(u2);

        udao.salvar(u2);

        ddao.atualizar(d1);

        Denuncia d2 = new Denuncia(u1, "Buraco enorme na frente da casa do vizinho", Categoria.BURACO_NA_RUA, "Não consigo sair com o carro por causa desse buracão", local, LocalDateTime.now());
        Denuncia d3 = new Denuncia(u2, "Buraco enorme na frente da casa do vizinho", Categoria.BURACO_NA_RUA, "Não consigo sair com o carro por causa desse buracão", local, LocalDateTime.now());

        d2.persistirDenuncia(connection);
        d3.persistirDenuncia(connection);

        ArrayList<Object> denuncias = ddao.listarTodosEagerLoading();

        for (Object denuncia : denuncias) {
            System.out.println(denuncia);
        }

        Denuncia denunciaId1 = ddao.buscarPorId(1);
        System.out.println(denunciaId1);
    }
}