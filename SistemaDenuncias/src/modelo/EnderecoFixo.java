package modelo;

public class EnderecoFixo extends Localizacao {
    private int idEndereco;
    private String cep;
    private String rua;
    private String numero;
    private String bairro;

    public EnderecoFixo(String cidade, String estado, String cep, String rua, String numero, String bairro) {
        super(cidade, estado);
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
    }

    // construtor do banco
    public EnderecoFixo(int idEndereco, Denuncia denuncia, String cidade, String estado, String cep, String rua, String numero, String bairro) {
        super(denuncia, cidade, estado);
        this.idEndereco = idEndereco;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.denuncia = denuncia;
    }


    public String getCep() {
        return cep;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public int getIdEndereco() {
        return idEndereco;
    }


    @Override
    public String formatar() {
        return "Cidade: " + this.cidade + ", Estado: " + this.estado + this.cep + ", Rua: " + this.rua + ", Numero: " + this.numero + ", Bairro: " + this.bairro;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }
}
