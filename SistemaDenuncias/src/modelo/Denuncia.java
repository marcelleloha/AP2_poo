package modelo;

import dao.DenunciaDAO;

import java.sql.Connection;
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

    // Nova denuncia que não existia (ainda não tem votos e confirmações nem ID)
    public Denuncia(Usuario criador, String titulo, Categoria categoria, String descricao, Localizacao localizacao, LocalDateTime data) {
        this.criador = criador;
        this.titulo = titulo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.localizacao = localizacao;
        if (localizacao != null) {
            localizacao.setDenuncia(this);
        }
        this.data = data;

        this.votosPrioridade = new HashMap<Usuario, Integer>();
        this.confirmacoes = new HashSet<Usuario>();
        this.midias = new ArrayList<Midia>();
    }

    // Denuncia do banco
    public Denuncia(int idDenuncia, Usuario criador, String titulo, Categoria categoria, String descricao, Localizacao localizacao, LocalDateTime data) {
        this.idDenuncia = idDenuncia;
        this.criador = criador;
        this.titulo = titulo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.localizacao = localizacao;
        if (localizacao != null) {
            localizacao.setDenuncia(this);
        }
        this.data = data;

        this.votosPrioridade = new HashMap<Usuario, Integer>();
        this.confirmacoes = new HashSet<Usuario>();
        this.midias = new ArrayList<Midia>();
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

    @Override
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

    public void removerConfirmacao(Usuario u) {
        confirmacoes.remove(u);
    }

    public void addMidia(Midia midia) {
        midias.add(midia);
    }

    public void removerMidia(Midia midia) {
        midias.remove(midia);
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

    // calcula a media votos para definir a prioridade
    public float calcularMediaVotos() {
        int quantidade = 0;
        int soma = 0;
        for (int voto : votosPrioridade.values()) {
            soma += voto;
            quantidade++;
        }
        return (float) soma /quantidade;
    }

    public boolean persistirDenuncia(Connection connection) {
        // verificar se já existe outra denúncia igual no sistema (mesma categoria e local) antes de criar. se já houver uma igual, retorna false
        DenunciaDAO ddao = new DenunciaDAO(connection);
        if (!ddao.existeDenunciaIgual(this)) {
            ddao.salvar(this);
            System.out.println("Denúncia salva no banco de dados com sucesso!");
            return true;
        } else {
            System.out.println("Já existe uma denúncia com mesma categoria no mesmo local!");
            this.idDenuncia = ddao.buscarPorCategoriaLocalizacao(this.categoria, this.localizacao);
            System.out.println("ID da denúncia já existente: " + this.idDenuncia);
            return false;
        }
    }

    @Override
    public int numeroVotos() {
        return votosPrioridade.size();
    }

    @Override
    public String toString() {
        return "ID da denúncia: "+ idDenuncia + ", Denúncia: " + titulo + ", criador: " + criador + ", data: " + data;
    }
}
