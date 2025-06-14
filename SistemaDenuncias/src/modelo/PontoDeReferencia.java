package modelo;

public class PontoDeReferencia extends Localizacao {
    private int idPonto;
    private String nomeponto;
    private String descricaoponto;

    public PontoDeReferencia(String cidade, String estado, String nomeponto, String descricaoponto) {
        super(cidade, estado);
        this.nomeponto = nomeponto;
        this.descricaoponto = descricaoponto;
    }

    //construtor do banco
    public PontoDeReferencia(int idPonto, Denuncia denuncia, String cidade, String estado, String nomeponto, String descricaoponto) {
        super(denuncia, cidade, estado);
        this.idPonto = idPonto;
        this.nomeponto = nomeponto;
        this.descricaoponto = descricaoponto;
        this.denuncia = denuncia;
    }

    public String getNome() {
        return nomeponto;
    }

    public String getDescricao() {
        return descricaoponto;
    }

    public int getIdPonto() {
        return idPonto;
    }

    @Override
    public String formatar() {
        return "Cidade: " + this.cidade + ", Estado: " + this.estado + "Nome: " + this.nomeponto + ", Descrição: " + this.descricaoponto;
    }

    public void setIdPonto(int idPonto) {
        this.idPonto = idPonto;
    }
}
