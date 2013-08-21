package br.com.banksystem.bsContrib.domain;

import java.io.Serializable;

public interface IPojo<T extends Serializable> {

	public abstract T getId();

}
