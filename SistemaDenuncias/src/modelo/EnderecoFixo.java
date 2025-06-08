package modelo;

public class EnderecoFixo extends Localizacao {
    private String cep;
    private String rua;
    private String numero;
    private String bairro;

    public EnderecoFixo(String cidade, String localizacao, String cep, String rua, String numero, String bairro) {
        super(cidade, localizacao);
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
    }

    @Override
    public String formatar() {
        return "Cep: " + this.cep + "Rua: " + this.rua + "Numero: " + this.numero + "Bairro: " + this.bairro;
    }
}
