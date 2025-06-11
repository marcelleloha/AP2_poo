package modelo;

public abstract class Localizacao {
    protected Denuncia denuncia;
    protected String cidade;
    protected String estado;

    public Localizacao(String cidade, String estado) {
        this.cidade = cidade;
        this.estado = estado;
    }

    public Localizacao(Denuncia denuncia, String cidade, String estado) {
        this.denuncia = denuncia;
        this.cidade = cidade;
        this.estado = estado;
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

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }
}
