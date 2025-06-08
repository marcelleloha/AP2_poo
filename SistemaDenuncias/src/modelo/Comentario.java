package modelo;

import java.time.LocalDateTime;
import java.util.Set;

public class Comentario implements Votavel {
    private int idComentario;
    private Usuario autor;
    private Denuncia denuncia;
    private String conteudo;
    private LocalDateTime data;
    private Set<Usuario> votos;

    // Comentário que veio do banco
    public Comentario(int idComentario, Usuario autor, Denuncia denuncia, String conteudo, LocalDateTime data, Set<Usuario> votos) {
        this.idComentario = idComentario;
        this.autor = autor;
        this.denuncia = denuncia;
        this.conteudo = conteudo;
        this.data = data;
        this.votos = votos;
    }

    // Comentário novo, sem ID e nem votos
    public Comentario(Usuario autor, Denuncia denuncia, String conteudo, LocalDateTime data) {
        this.autor = autor;
        this.denuncia = denuncia;
        this.conteudo = conteudo;
        this.data = data;
    }

    public int getIdComentario() {
        return idComentario;
    }

    public Usuario getAutor() {
        return autor;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public String getConteudo() {
        return conteudo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Set<Usuario> getVotos() {
        return votos;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public void receberVoto(Usuario u, Integer voto) {
        votos.add(u);
    }
    public void removerVoto(Usuario u) {
        votos.remove(u);
    }

    public void editarComentario(String novoConteudo) {
        this.conteudo = novoConteudo;
    }

    public int numeroVotos () {
        return votos.size();
    }
}
