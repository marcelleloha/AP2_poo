package modelo;

public abstract class Localizacao {
    private String cidade;
    private String estado;

    public Localizacao(String cidade, String localizacao) {
        this.cidade = cidade;
        this.estado = localizacao;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public abstract String formatar();
}
