package br.com.banksystem.bsContrib.view.template;

import java.io.Serializable;
import java.util.List;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;

public interface IGenericListPage<T> {

	public T buscarEntidade(Serializable id);

	public List<T> consultar();

	public FiltroGenerico<T> getFiltro();

	public boolean isSelecaoHabilitada();

}
