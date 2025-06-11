package modelo;

public interface Votavel {
    public void receberVoto(Usuario u, Integer voto);
    public void removerVoto(Usuario u);
    public int numeroVotos();
}
