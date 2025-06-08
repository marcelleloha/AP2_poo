package modelo;

public class PontoDeReferencia extends Localizacao {
    private String nome;
    private String descricao;

    public PontoDeReferencia(String cidade, String estado, String nome, String descricao) {
        super(cidade, estado);
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String formatar() {
        return "Nome: " + this.nome + ", Descrição: " + this.descricao;
    }
}
