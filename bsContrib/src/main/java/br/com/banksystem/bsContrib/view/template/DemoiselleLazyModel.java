package br.com.banksystem.bsContrib.view.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import br.com.banksystem.bsContrib.domain.IPojo;
import br.com.banksystem.bsContrib.persistence.filtro.Order;
import br.com.banksystem.bsContrib.persistence.filtro.Order.OrderType;

public class DemoiselleLazyModel<T extends IPojo<?>> extends LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	private boolean loaded = false;

	private boolean reload = false;

	private final IGenericListPage<T> pagina;

	private List<T> listaSelecionados;

	public DemoiselleLazyModel(IGenericListPage<T> pagina) {
		this.pagina = pagina;
		setRowCount(0);
	}

	protected void adicionarOrdenacaoCampo(String sortField, SortOrder sortOrder) {
		if (sortField != null && sortOrder != null) {
			OrderType tipo = SortOrder.ASCENDING.equals(sortOrder) ? OrderType.ASC : OrderType.DESC;
			pagina.getFiltro().setCampoOrdenadoModel(new Order(sortField, tipo));
		}
	}

	public void clear() {
		this.loaded = false;
		this.reload = false;
		super.setRowCount(0);
		if (pagina.isSelecaoHabilitada()) {
			this.setListaSelecionados(null);
		}
	}

	public List<T> getListaSelecionados() {
		return listaSelecionados;
	}

	public IGenericListPage<T> getPagina() {
		return pagina;
	}

	@Override
	public int getRowCount() {
		if (!this.loaded) {
			pagina.getFiltro().limpar();
			List<T> retorno = pagina.consultar();
			setWrappedData(retorno);
			this.loaded = true;
			setRowCount(pagina.getFiltro().getTotalResultados());
		}
		return super.getRowCount();
	}

	@Override
	public T getRowData(String rowKey) {
		if (pagina.isSelecaoHabilitada()) {
			T rowData = null;

			rowData = isObjetoJaSelecionado(rowKey);
			if (rowData == null) {
				if (rowKey != null) { return instanciar(rowKey); }

			}
			return rowData;
		} else {
			return super.getRowData(rowKey);
		}
	}

	@Override
	public Object getRowKey(T object) {
		if (pagina.isSelecaoHabilitada()) {
			return object.getId();
		} else {
			return super.getRowKey(object);
		}
	}

	protected T instanciar(Serializable id) {
		return pagina.buscarEntidade(id);
	}

	public boolean isLoaded() {
		return this.loaded;
	}

	private T isObjetoJaSelecionado(String rowKey) {
		if (listaSelecionados != null) {
			for (T objeto: listaSelecionados) {
				if (String.valueOf(objeto.getId()).equals(rowKey)) { return objeto; }
			}
		}
		return null;
	}

	public boolean isReload() {
		return reload;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		List<T> dados = new ArrayList<T>();
		if (reload) {
			try {
				pagina.getFiltro().limpar();
				pagina.getFiltro().setTamanhoPagina(pageSize != 0 ? pageSize : 1);
				pagina.getFiltro().setPrimeiroResultado(first);
				pagina.getFiltro().setConsultarTotalResultado(!loaded);
				pagina.getFiltro().setFiltrosModel(filters);
				adicionarOrdenacaoCampo(sortField, sortOrder);
				dados = pagina.consultar();
				if (!loaded) {
					setRowCount(pagina.getFiltro().getTotalResultados());
				}
				loaded = true;
			} catch (RuntimeException e) {
				dados = (List<T>) getWrappedData();
				throw e;
			}
		} else {
			dados = (List<T>) getWrappedData();
		}
		reload = true;
		return dados;
	}

	public void setListaSelecionados(List<T> listaSelecionados) {
		this.listaSelecionados = listaSelecionados;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}

	@Override
	public void setRowIndex(int rowIndex) {
		// FIXME Essa checagem existe devido a um bug do Primefaces
		if (getPageSize() == 0) {
			setPageSize(1);
		}
		super.setRowIndex(rowIndex);
	}
}
