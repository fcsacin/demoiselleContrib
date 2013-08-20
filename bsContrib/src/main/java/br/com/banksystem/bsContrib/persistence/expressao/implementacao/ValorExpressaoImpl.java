package br.com.banksystem.bsContrib.persistence.expressao.implementacao;

import java.util.Arrays;
import java.util.List;
import br.com.banksystem.bsContrib.persistence.expressao.FieldPath;
import br.com.banksystem.bsContrib.persistence.expressao.TipoExpressao;
import br.com.banksystem.bsContrib.persistence.expressao.ValorExpressao;

/**
 * Classe que representa o valor de uma expressao da consulta
 * 
 * @author Filipe Andrade
 */
public class ValorExpressaoImpl extends ValorExpressao implements FieldPath {

	private static final long serialVersionUID = 1L;

	private String campo;

	private List<Object> valores;

	private TipoExpressao tipoParametro;

	/**
	 * Construtor do valor da expressao que recebe o campo e valor da mesma. Sera adotado por padrao o tipo EQUALS da expressao.
	 * 
	 * @param campo Campo ao qual deseja-se referenciar na consulta
	 * @param valor Valor que sera utilizado na consulta para compararacao
	 */
	public ValorExpressaoImpl(final String campo, final Object valor) {
		this(TipoExpressao.EQUALS, campo, valor);
	}

	public ValorExpressaoImpl(final TipoExpressao tipoParametro, final String campo, final Object... valores) {
		super();
		setTipoParametro(tipoParametro);
		setCampo(campo);
		if (valores != null) {
			setValores(Arrays.asList(valores));
		}
	}

	/**
	 * Retorna o campo/atributo ao qual o valor e referente
	 */
	@Override
	public String getCampo() {
		return campo;
	}

	/**
	 * Retorna o tipo de parametro da expressao
	 */
	@Override
	public TipoExpressao getTipoParametro() {
		return tipoParametro;
	}

	/**
	 * Retorna o primeiro valor, caso exista, da lista de valores
	 * 
	 * @return Primeiro valor da expressao
	 */
	@Override
	public Object getValor() {
		Object valor = null;
		if ((getValores() != null) && !getValores().isEmpty()) {
			valor = getValores().iterator().next();
		}
		return valor;
	}

	/**
	 * Retorna a lista de valores da expressao
	 */
	@Override
	public List<Object> getValores() {
		return valores;
	}

	public void setCampo(final String campo) {
		this.campo = campo;
	}

	public void setTipoParametro(final TipoExpressao tipoParametro) {
		this.tipoParametro = tipoParametro;
	}

	public void setValores(List<Object> valores) {
		this.valores = valores;
	}

}
