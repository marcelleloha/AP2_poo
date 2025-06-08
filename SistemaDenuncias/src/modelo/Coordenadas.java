package modelo;

public class Coordenadas extends Localizacao {
    private String longitude;
    private String latitude;

    public Coordenadas(String cidade, String localizacao, String longitude, String latitude) {
        super(cidade, localizacao);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String formatar() {
        return "Longitude: " + this.longitude + "Latitude: " + this.latitude;
    }
}
