package modelo;

public class EnderecoFixo extends Localizacao {
    private String cep;
    private String rua;
    private String numero;
    private String bairro;

    public EnderecoFixo(Denuncia denuncia, String cidade, String estado, String cep, String rua, String numero, String bairro) {
        super(denuncia, cidade, estado);
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
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

    @Override
    public String formatar() {
        return "Cep: " + this.cep + ", Rua: " + this.rua + ", Numero: " + this.numero + ", Bairro: " + this.bairro;
    }
}
