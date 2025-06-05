package modelo;

import java.util.ArrayList;

public class Usuario {
    private final int idUsuario;
    private String nome;
    private String email;
    private String senha;
    private boolean ehAdm;
    private ArrayList<Denuncia> denuncias = new ArrayList<Denuncia>();

    private static int id = 0;

    public Usuario(String nome, String email, String senha, boolean ehAdm) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ehAdm = ehAdm;
        this.idUsuario = ++id;
    }

    public void alterarSenha(String novaSenha) {
        if (!novaSenha.equals(this.senha)) {
            this.senha = novaSenha;
            System.out.println("Senha alterada com sucesso.");
        } else System.out.println("A nova senha não pode ser igual à antiga!");
    }

    public boolean criarDenuncia(Categorias categoria, String desc, Localizacao local, Midia midia) {
        // verificar se já existe outra denúncia igual no sistema (mesma categoria e local) antes de criar. se já houver uma igual, retorna false

        // Denuncia novaDenuncia = new Denuncia(categoria, desc, local, midia);
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

    public boolean votar(int voto, Denuncia d) {
        // adiciona um voto (valor inteiro entre 1 e 10) a uma denuncia, cada usuário só pode votar uma vez em cada denúncia
    }

    public boolean votar(int voto, Comentario c) {
        // adiciona um voto (+1) a um comentário, cada usuário só pode votar uma vez em cada comentário
    }

    public boolean removerVoto(Denuncia d) {
        // remove o voto deste usuário de uma denúncia
    }

    public boolean removerVoto(Comentario c) {
        // remove o voto deste usuário de um comentário
    }
}
