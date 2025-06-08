package modelo;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String email;
    private String senha;

    // Novo usuário que não existia ainda
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Usuário que veio do banco
    public Usuario(int idUsuario, String nome, String email, String senha) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void alterarSenha(String novaSenha) {
        if (!novaSenha.equals(this.senha)) {
            this.senha = novaSenha;
            System.out.println("Senha alterada com sucesso.");
        } else System.out.println("A nova senha não pode ser igual à antiga!");
    }
}
