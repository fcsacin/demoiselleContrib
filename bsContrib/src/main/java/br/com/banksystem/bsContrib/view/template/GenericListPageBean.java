package br.com.banksystem.bsContrib.view.template;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.faces.event.ActionEvent;
import br.com.banksystem.bsContrib.business.GenericBC;
import br.com.banksystem.bsContrib.domain.IPojo;
import br.com.banksystem.bsContrib.persistence.GenericDAO;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Reflections;

public abstract class GenericListPageBean<T extends IPojo<I>, I extends Serializable, F extends FiltroGenerico<T>> extends AbstractListPageBean<T, I>
		implements IGenericListPage<T> {

	private static final long serialVersionUID = -576374572268465843L;

	private DemoiselleLazyModel<T> model;

	private F filtro;

	/** Armazena a classe referente ao filtro de consulta */
	private Class<F> filtroClass;

	public void acaoConsultar(ActionEvent event) {
		getFiltro().limpar();
		getDataModel().clear();
	}

	@Override
	public T buscarEntidade(Serializable id) {
		return getBC().load((I) id);
	}

	@Override
	public List<T> consultar() {
		return getBC().consultar(getFiltro());
	}

	@Transactional
	public void deleteSelection(ActionEvent event) {
		boolean delete;
		for (Iterator<I> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			I id = iter.next();
			delete = getSelection().get(id);

			if (delete) {
				getBC().delete(id);
				iter.remove();
			}
		}
	}

	protected abstract <C extends GenericBC<T, I, GenericDAO<T, I>>> C getBC();

	@Override
	public DemoiselleLazyModel<T> getDataModel() {
		if (model == null) {
			model = new DemoiselleLazyModel<T>(this);
		}
		return model;
	}

	@Override
	public F getFiltro() {
		if (filtro == null) {
			initFiltro();
		}
		return filtro;
	}

	protected Class<F> getFiltroClass() {
		if (filtroClass == null) {
			filtroClass = Reflections.getGenericTypeArgument(this.getClass(), 2);
		}
		return filtroClass;
	}

	@Override
	protected List<T> handleResultList() {
		return null;
	}

	/**
	 * Metodo para criar/limpar o filtro
	 */
	protected void initFiltro() {
		if (this.filtro == null) {
			try {
				this.filtro = (getFiltroClass().newInstance());
			} catch (Exception e) {}
		}
	}

	@Override
	public boolean isSelecaoHabilitada() {
		return false;
	}

	public void limpar(ActionEvent event) {
		super.clear();
	}

	public void setFiltro(F filtro) {
		this.filtro = filtro;
	}

}
