package modelo;

public class PontoDeReferencia extends Localizacao {
    private String nomeponto;
    private String descricaoponto;

    public PontoDeReferencia(Denuncia denuncia, String cidade, String estado, String nomeponto, String descricaoponto) {
        super(denuncia, cidade, estado);
        this.nomeponto = nomeponto;
        this.descricaoponto = descricaoponto;
    }

    //construtor do banco
    public PontoDeReferencia(int idPonto, Denuncia denuncia, String cidade, String estado, String nomeponto, String descricaoponto) {
        super(denuncia, cidade, estado);
        this.nomeponto = nomeponto;
        this.descricaoponto = descricaoponto;
    }

    public String getNome() {
        return nomeponto;
    }

    public String getDescricao() {
        return descricaoponto;
    }

    @Override
    public String formatar() {
        return "Nome: " + this.nomeponto + ", Descrição: " + this.descricaoponto;
    }

    public void setIdPonto(int idPonto) {
    }
}
