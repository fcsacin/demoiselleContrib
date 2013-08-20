package br.com.banksystem.bsContrib.persistence.filtro;

import br.com.banksystem.bsContrib.persistence.expressao.FieldPath;

public class Order implements FieldPath {

	public enum OrderType {
		ASC,
		DESC;
	}

	private String campo;

	private OrderType tipo;

	public Order(final String campo) {
		this(campo, OrderType.ASC);
	}

	public Order(final String campo, final OrderType tipo) {
		super();
		setCampo(campo);
		setTipo(tipo);
	}

	@Override
	public boolean equals(Object arg0) {
		Order arg = (Order) arg0;
		return this.campo.equals(arg.getCampo());
	}

	@Override
	public String getCampo() {
		return campo;
	}

	public OrderType getTipo() {
		return tipo;
	}

	@Override
	public int hashCode() {
		int hash = 13;
		if (this.campo != null) {
			hash = hash * this.campo.hashCode();
		}
		return hash;
	}

	public void setCampo(final String campo) {
		this.campo = campo;
	}

	public void setTipo(final OrderType tipo) {
		this.tipo = tipo;
	}

}
