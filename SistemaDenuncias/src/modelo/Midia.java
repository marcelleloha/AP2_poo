package modelo;

public class Midia {
    private int idMidia;
    private String url;
    private String legenda;
    private Denuncia denuncia;

    public Midia(Denuncia denuncia, String url, String legenda) {
        this.url = url;
        this.legenda = legenda;
        this.denuncia = denuncia;
    }

    // Midia do Banco
    public Midia(int idMidia, Denuncia denuncia, String url, String legenda) {
        this.idMidia = idMidia;
        this.url = url;
        this.legenda = legenda;
        this.denuncia = denuncia;
    }

    public String getLegenda() {
        return legenda;
    }

    public String getUrl() {
        return url;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public int getIdMidia() {
        return idMidia;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setIdMidia(int idMidia) {
        this.idMidia = idMidia;
    }
}
