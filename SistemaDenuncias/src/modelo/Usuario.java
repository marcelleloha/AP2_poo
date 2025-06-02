package modelo;

import java.util.ArrayList;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String email;
    private String senha;
    private boolean ehAdm;
    private ArrayList<Denuncia> denuncias;

    private static int id = 0;

    public Usuario(String nome, String email, String senha, boolean ehAdm) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ehAdm = ehAdm;
        this.idUsuario = ++id;
    }

//- alterarSenha(novaSenha)
//- criarDenuncia(categoria, desc, Localizacao, Midia)
//- comentar(conteudo, Denuncia)
//- editarComentario(Comentario)
//- removerComentario(Comentario)
//- removerDenuncia(Denuncia)
//- editarDenuncia(Denuncia, dado, novoDado)
//- votar(voto, Denuncia ou Comentario)
//- removerVoto(Denuncia ou Comentario)

    
}
