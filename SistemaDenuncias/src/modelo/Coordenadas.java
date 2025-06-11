package modelo;

public class Coordenadas extends Localizacao {
    private int idCoordenada;
    private double longitude;
    private double latitude;

    public Coordenadas(Denuncia denuncia, String cidade, String estado, double longitude, double latitude) {
        super(cidade, estado);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // construtor do banco de dados
    public Coordenadas(int idCoordenada, Denuncia denuncia, String cidade, String estado, double longitude, double latitude) {
        super(denuncia, cidade, estado);
        this.idCoordenada = idCoordenada;
        this.longitude = longitude;
        this.latitude = latitude;
        this.denuncia = denuncia;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getIdCoordenada() {
        return idCoordenada;
    }


    @Override
    public String formatar() {
        return "Cidade: " + this.cidade + "Estado: " + this.estado + "Longitude: " + this.longitude + ", Latitude: " + this.latitude;
    }

    public void setIdCoordenada(int idCoordenada) {
        this.idCoordenada = idCoordenada;
    }
}
