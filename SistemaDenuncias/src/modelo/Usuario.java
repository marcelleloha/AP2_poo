package modelo;

import dao.DenunciaDAO;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String email;
    private String senha;

    // Novo usuário que não existia ainda
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Usuário que veio do banco
    public Usuario(int idUsuario, String nome, String email, String senha) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void alterarSenha(String novaSenha) {
        if (!novaSenha.equals(this.senha)) {
            this.senha = novaSenha;
            System.out.println("Senha alterada com sucesso.");
        } else System.out.println("A nova senha não pode ser igual à antiga!");
    }

    public boolean criarDenuncia(Connection connection, String titulo, Categoria categoria, String descricao, Localizacao localizacao, ArrayList<Midia> midias) {
        // verificar se já existe outra denúncia igual no sistema (mesma categoria e local) antes de criar. se já houver uma igual, retorna false
        DenunciaDAO ddao = new DenunciaDAO(connection);
        ArrayList<Object> denuncias = ddao.listarTodosEagerLoading();
        Denuncia novaDenuncia = new Denuncia(this, titulo, categoria, descricao, localizacao, LocalDateTime.now(), midias);
        for (Object o : denuncias) {
            Denuncia d = (Denuncia) o;
            if (novaDenuncia.equals(d)) {
                System.out.println("Já existe uma denúncia com a mesma categoria e localização!");
                return false;
            }
        }
        ddao.salvar(novaDenuncia);
        return true;
    }

    public boolean editarDenuncia(Denuncia denuncia, String dado, Object novoDado) {
        // edita uma denúncia deste usuário, mudando um de seus atributos (dado) para o valor de novoDado

        // fazer uma verificação com 'dado' para descobrir a qual atributo da denúncia ele se refere
    }

    public boolean comentar(String conteudo, Denuncia denuncia) {
        // cria um objeto Comentario e o associa a uma Denuncia
    }

    public boolean editarComentario(Comentario c, String novoConteudo) {
        // substitui o conteúdo de um comentário que pertença a esse usuário
    }

    public boolean removerComentario(Comentario c) {
        // apaga um comentário que pertença a esse usuário
    }

    public boolean removerDenuncia(Denuncia d) {
        // caso o usuário seja um administrador, pode remover qualquer comentário, caso não, apenas os comentários deste usuário podem ser deletados
    }
}
