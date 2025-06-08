package modelo;

public class Midia {
    private String url;
    private String legenda;

    public Midia(String url, String legenda) {
        this.url = url;
        this.legenda = legenda;
    }

    // Midia do Banco
    public Midia(int identificador, String url, String legenda) {
        this.url = url;
        this.legenda = legenda;
    }

    public String getLegenda() {
        return legenda;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public void setIdMidia(int idMidia) {
    }
}
