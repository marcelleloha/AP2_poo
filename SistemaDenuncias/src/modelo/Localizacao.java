package modelo;

public abstract class Localizacao {
    private String cidade;
    private String localizacao;

    public Localizacao(String cidade, String localizacao) {
        this.cidade = cidade;
        this.localizacao = localizacao;
    }

    public String getTipoLocalizacao(){
        return this.getClass().getSimpleName();
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public abstract String formatar();
}
