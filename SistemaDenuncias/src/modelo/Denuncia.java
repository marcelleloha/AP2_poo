package modelo;

import java.time.LocalDateTime;
import java.util.*;

public class Denuncia implements Votavel {
    private int idDenuncia;
    private Usuario criador;
    private String titulo;
    private Categoria categoria;
    private String descricao;
    private Localizacao localizacao;
    private LocalDateTime data;
    private Map<Usuario, Integer> votosPrioridade;
    private Set<Usuario> confirmacoes;
    private ArrayList<Midia> midias;
    // private Map<Usuario, Comentario> comentarios;

    // Nova denuncia que não existia (ainda não tem votos, confirmações e comentários)
    public Denuncia(Usuario criador, String titulo, Categoria categoria, String descricao, Localizacao localizacao, LocalDateTime data, ArrayList<Midia> midias) {
        this.criador = criador;
        this.titulo = titulo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.data = data;
        this.midias = midias;
    }

    // Denuncia do banco
    public Denuncia(int idDenuncia, Usuario criador, String titulo, Categoria categoria, String descricao, Localizacao localizacao, LocalDateTime data) { //, Map<Usuario, Comentario> comentarios) {
        this.idDenuncia = idDenuncia;
        this.criador = criador;
        this.titulo = titulo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.data = data;

        this.votosPrioridade = new HashMap<Usuario, Integer>();
        this.confirmacoes = new HashSet<Usuario>();
        this.midias = new ArrayList<Midia>();
        //this.comentarios = comentarios;
    }

    public int getIdDenuncia() {
        return idDenuncia;
    }

    public Usuario getCriador() {
        return criador;
    }

    public String getTitulo() {
        return titulo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Map<Usuario, Integer> getVotosPrioridade() {
        return votosPrioridade;
    }

    public Set<Usuario> getConfirmacoes() {
        return confirmacoes;
    }

    public ArrayList<Midia> getMidias() {
        return midias;
    }

    public void setIdDenuncia(int idDenuncia) {
        this.idDenuncia = idDenuncia;
    }

    public void receberVoto(Usuario u, Integer voto) {
        votosPrioridade.put(u, voto);
    }

    @Override
    public void removerVoto(Usuario u) {
        votosPrioridade.remove(u);
    }

    public void receberConfirmacao(Usuario u) {
        confirmacoes.add(u);
    }

    public void addMidia(Midia midia) {
        if (!midias.contains(midia)) {
            midias.add(midia);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Denuncia denuncia = (Denuncia) o;
        return categoria == denuncia.categoria && Objects.equals(localizacao, denuncia.localizacao);
    }

    public void editarCategoria(Categoria novaCategoria) {
        this.categoria = novaCategoria;
    }

    public void editarLocalizacao(Localizacao novaLocalizacao) {
        this.localizacao = novaLocalizacao;
    }

    public void editarDescricao(String novaDesc) {
        this.descricao = novaDesc;
    }

    public boolean receberComentario(Comentario)
    public boolean removerComentario(Comentario)
    public boolean adicionarMidia(Midia midia) {

    }
    public boolean removerMidia(Midia)

    public float calcularMediaVotos() {
        int quantidade = 0;
        int soma = 0;
        for (int voto : votosPrioridade.values()) {
            soma += voto;
            quantidade++;
        }
        return (float) soma /quantidade;
    }
}
