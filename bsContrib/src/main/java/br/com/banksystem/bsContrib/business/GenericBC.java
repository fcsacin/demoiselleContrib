package br.com.banksystem.bsContrib.business;

import java.io.Serializable;
import java.util.List;
import br.com.banksystem.bsContrib.persistence.GenericDAO;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

public abstract class GenericBC<T, I extends Serializable, C extends GenericDAO<T, I>> extends DelegateCrud<T, I, C> {

	private static final long serialVersionUID = 5014035723392996384L;

	protected abstract void adicionarParametrosConsulta(FiltroGenerico<T> filtro);

	public List<T> consultar(FiltroGenerico<T> filtro) {
		adicionarParametrosConsulta(filtro);
		return getDelegate().findByFilter(filtro);
	}

}
