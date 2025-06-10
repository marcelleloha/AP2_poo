package modelo;

public class Coordenadas extends Localizacao {
    private double longitude;
    private double latitude;

    public Coordenadas(Denuncia denuncia, String cidade, String estado, double longitude, double latitude) {
        super(denuncia, cidade, estado);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // construtor do banco de dados
    public Coordenadas(int idCoordenada, Denuncia denuncia, String cidade, String estado, double longitude, double latitude) {
        super(denuncia, cidade, estado);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String formatar() {
        return "Longitude: " + this.longitude + ", Latitude: " + this.latitude;
    }

    public void setIdCoordenada(int idCoordenada) {
    }
}
