package modelo;

public class Coordenadas extends Localizacao {
    private double longitude;
    private double latitude;

    public Coordenadas(String cidade, String estado, double longitude, double latitude) {
        super(cidade, estado);
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
}
