package modelo;

import dao.DenunciaDAO;
import dao.EnderecoFixoDAO;

import java.sql.Connection;

public class EnderecoFixo extends Localizacao {
    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private int idEndereco;

    public EnderecoFixo(String cidade, String estado, String cep, String rua, String numero, String bairro) {
        super(cidade, estado);
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setIdEndereco(int id){ idEndereco = id; }

    public boolean persistirEndereco(Connection connection) {
        // verificar se já existe outra denúncia igual no sistema (mesma categoria e local) antes de criar. se já houver uma igual, retorna false
        EnderecoFixoDAO ddao = new EnderecoFixoDAO(connection);
        if (!ddao.existeEnderecoIgual(this)) {
            ddao.salvar(this);
            System.out.println("Denúncia salva no banco de dados com sucesso!");
            return true;
        } else {
            System.out.println("Já existe uma denúncia com mesma categoria no mesmo local!");
            return false;
        }
    }

    @Override
    public String formatar() {
        return "Cep: " + this.cep + ", Rua: " + this.rua + ", Numero: " + this.numero + ", Bairro: " + this.bairro;
    }
}
