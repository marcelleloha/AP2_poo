package dao;

import java.util.ArrayList;

public interface BaseDAO {
    public void salvar(Object objeto);
    public Object buscarPorId(int id);
    public ArrayList<Object> listarTodosLazyLoading();
    public ArrayList<Object> listarTodosEagerLoading();
    public ArrayList<Object> atualizar(Object objeto);
    public void excluir(int id);
}
