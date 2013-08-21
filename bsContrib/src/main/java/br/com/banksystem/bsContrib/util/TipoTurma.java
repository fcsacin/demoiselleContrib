package br.com.banksystem.bsContrib.util;

import br.com.banksystem.bsContrib.domain.IPojo;

public enum TipoTurma implements IPojo<Integer> {

	NORMAL,
	SUPLETIVO;

	@Override
	public Integer getId() {
		return this.ordinal();
	}

}
