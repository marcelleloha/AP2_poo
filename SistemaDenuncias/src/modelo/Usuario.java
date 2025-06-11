package modelo;

import dao.UsuarioDAO;

import java.sql.Connection;

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

    public boolean persistirUsuario(Connection connection) {
        UsuarioDAO udao = new UsuarioDAO(connection);
        if(!udao.existeUsuarioIgual(this)){
            udao.salvar(this);
            return true;
        }
        else {
            System.out.println("Usuário já existe no banco de dados.");
            Usuario u = udao.buscarPorNomeEmail(this.nome, this.email);
            this.idUsuario = u.getIdUsuario();
            return false;
        }

    }

    @Override
    public String toString() {
        return "idUsuario = " + idUsuario + ", nome = '" + nome + '\'' + ", email = '" + email + '\'';
    }
}
