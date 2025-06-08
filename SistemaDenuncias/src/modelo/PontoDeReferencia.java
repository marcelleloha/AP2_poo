package modelo;

public class PontoDeReferencia extends Localizacao {
    private String nome;
    private String descricao;

    public PontoDeReferencia(String cidade, String localizacao, String nome, String descricao) {
        super(cidade, localizacao);
        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public String formatar() {
        return "Nome: " + this.nome + "Descrição: " + this.descricao;
    }
}
