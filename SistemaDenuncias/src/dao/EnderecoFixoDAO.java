package dao;

import java.sql.Connection;

public class EnderecoFixoDAO implements BaseDAO {
    private Connection connection;

    public EnderecoFixoDAO(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof EnderecoFixo)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Endereço Fixo.");
        }

        EnderecoFixo enderecoFixo = (EnderecoFixo) objeto;

        try {
            String sql = "INSERT INTO endereco_fixo (idDenuncia, idEndereco, cep, rua, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setObject(1, enderecoFixo.getDenuncia().getIdDenuncia());
                pstm.setObject(2, enderecoFixo.get());
                pstm.setString(3, enderecoFixo.getLegenda());
                pstm.setString(4, enderecoFixo.getRua())

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        midia.setIdMidia(rst.getInt(1));
                    }

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
