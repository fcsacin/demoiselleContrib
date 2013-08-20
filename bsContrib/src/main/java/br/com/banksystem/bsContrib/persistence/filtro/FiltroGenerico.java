package br.com.banksystem.bsContrib.persistence.filtro;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import br.gov.frameworkdemoiselle.internal.configuration.PaginationConfig;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;

public abstract class FiltroGenerico<T> implements Serializable {

	private static final long serialVersionUID = 6025294672119903911L;

	private List<Order> camposOrdenados;

	private List<String> camposAgrupados;

	private List<String> camposRetornados;

	private Map<String, String> filtrosModel;

	private Order campoOrdenadoModel;

	private Integer totalResultados;

	private Integer limiteResultados;

	private Integer tamanhoPagina;

	private Integer primeiroResultado;

	private Boolean limitarItensAtivos = true;

	private Boolean resultadosUnicos;

	private Boolean consultarTotalResultado = true;

	private Boolean retornarApenasCamposPadrao = false;

	private Class<T> beanClass;

	private T entidadeConsulta;

	public FiltroGenerico() {
		super();
	}

	/**
	 * Adiciona um campo que fará parte do agrupamento
	 * 
	 * @param campo
	 */
	public void addCampoAgrupado(final String campo) {
		if (getCamposAgrupados() == null) {
			setCamposAgrupados(new ArrayList<String>());
		}
		getCamposAgrupados().add(campo);
	}

	/**
	 * Adiciona um campo que fará parte da ordenação
	 * 
	 * @param campo
	 */
	public void addCampoOrdenado(final Order campo) {
		addCampoOrdenado(campo, false);
	}

	/**
	 * Adiciona um campo que fara parte da ordenacao e se deve ser no inicio da lista. Caso o campo ja exista na lista, ele e removido e
	 * adicionado no incio da lista.
	 * 
	 * @param campo Campo que deve ser adicionado
	 * @param inicio Informa que deve ser colocado no inicio da lista.
	 */
	public void addCampoOrdenado(final Order campo, final boolean inicio) {
		if (getCamposOrdenados() == null) {
			setCamposOrdenados(new ArrayList<Order>());
		}
		boolean campoPresente = false;
		// Se for para colocar no inicio, deve ser removido
		if (inicio) {
			getCamposOrdenados().remove(campo);
		} else {
			campoPresente = getCampoOrdenadoModel() != null ? campo.equals(getCampoOrdenadoModel()) : false;
		}
		if (!campoPresente) {
			if (inicio) {
				getCamposOrdenados().add(0, campo);
			} else {
				getCamposOrdenados().add(campo);
			}
		}
	}

	public Class<T> getBeanClass() {
		if (beanClass == null) {
			this.beanClass = Reflections.getGenericTypeArgument(this.getClass(), 0);
		}
		return beanClass;
	}

	public Order getCampoOrdenadoModel() {
		return campoOrdenadoModel;
	}

	/**
	 * Retorna a lista de campos que agrupam a consulta
	 * 
	 * @return Lista de campos
	 */
	public List<String> getCamposAgrupados() {
		return camposAgrupados;
	}

	/**
	 * Retorna a lista de campos que ordenam a consulta
	 * 
	 * @return Lista de campos
	 */
	public List<Order> getCamposOrdenados() {
		return camposOrdenados;
	}

	/**
	 * Retorna a lista de campos definidos para retorno na consulta
	 * 
	 * @return Lista de campos
	 */
	public List<String> getCamposRetornados() {
		return camposRetornados;
	}

	public Boolean getConsultarTotalResultado() {
		return consultarTotalResultado;
	}

	public T getEntidadeConsulta() {
		if (entidadeConsulta == null) {
			try {
				entidadeConsulta = getBeanClass().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entidadeConsulta;
	}

	public Map<String, String> getFiltrosModel() {
		return filtrosModel;
	}

	public Boolean getLimitarItensAtivos() {
		return limitarItensAtivos;
	}

	public Integer getLimiteResultados() {
		return limiteResultados;
	}

	public Integer getPrimeiroResultado() {
		return primeiroResultado;
	}

	public Boolean getResultadosUnicos() {
		return resultadosUnicos;
	}

	public Boolean getRetornarApenasCamposPadrao() {
		return retornarApenasCamposPadrao;
	}

	public Integer getTamanhoPagina() {
		if (tamanhoPagina == null) {
			tamanhoPagina = Beans.getReference(PaginationConfig.class).getPageSize();
		}
		return tamanhoPagina;
	}

	public Integer getTotalResultados() {
		return totalResultados;
	}

	/**
	 * Usado para limpar o parâmetros do filtro
	 */
	public void limpar() {
		setTotalResultados(0);
		setPrimeiroResultado(0);
		setConsultarTotalResultado(true);
		if (camposAgrupados != null) {
			camposAgrupados.clear();
		}
		if (camposOrdenados != null) {
			camposOrdenados.clear();
		}
		this.campoOrdenadoModel = null;
	}

	/**
	 * Método para limpar os atributos da classe.
	 */
	public void limparAtributos() {
		Field[] declaredFields = getClass().getDeclaredFields();
		for (Field campo: declaredFields) {
			if (!Modifier.isFinal(campo.getModifiers()) && !Modifier.isStatic(campo.getModifiers())) {
				try {
					campo.setAccessible(true);
					if (Collection.class.isAssignableFrom(campo.getType())) {
						Collection<?> valorCampo = (Collection<?>) campo.get(this);
						if (valorCampo != null) {
							valorCampo.clear();
						}
					} else {
						campo.set(this, null);
					}
					campo.setAccessible(false);
				} catch (Exception e) {}
			}
		}
		setEntidadeConsulta(null);
	}

	public void setCampoOrdenadoModel(Order campoOrdenadoModel) {
		this.campoOrdenadoModel = campoOrdenadoModel;
	}

	public void setCamposAgrupados(final List<String> camposAgrupados) {
		this.camposAgrupados = camposAgrupados;
	}

	public void setCamposOrdenados(final List<Order> camposOrdenados) {
		this.camposOrdenados = camposOrdenados;
	}

	public void setCamposRetornados(final List<String> camposRetornados) {
		this.camposRetornados = camposRetornados;
	}

	public void setCamposRetornados(final String... camposRetornados) {
		this.camposRetornados = Arrays.asList(camposRetornados);
	}

	public void setConsultarTotalResultado(Boolean consultarTotalResultado) {
		this.consultarTotalResultado = consultarTotalResultado;
	}

	public void setEntidadeConsulta(T entidadeConsulta) {
		this.entidadeConsulta = entidadeConsulta;
	}

	public void setFiltrosModel(Map<String, String> filtrosModel) {
		this.filtrosModel = filtrosModel;
	}

	public void setLimitarItensAtivos(Boolean limitarItensAtivos) {
		this.limitarItensAtivos = limitarItensAtivos;
	}

	public void setLimiteResultados(Integer limiteResultados) {
		this.limiteResultados = limiteResultados;
	}

	public void setPrimeiroResultado(Integer primeiroResultado) {
		this.primeiroResultado = primeiroResultado;
	}

	public void setResultadosUnicos(Boolean resultadosUnicos) {
		this.resultadosUnicos = resultadosUnicos;
	}

	public void setRetornarApenasCamposPadrao(Boolean retornarApenasCamposPadrao) {
		if (retornarApenasCamposPadrao != null) {
			this.retornarApenasCamposPadrao = retornarApenasCamposPadrao;
		} else {
			this.retornarApenasCamposPadrao = false;
		}
	}

	public void setTamanhoPagina(Integer tamanhoPagina) {
		this.tamanhoPagina = tamanhoPagina;
	}

	public void setTotalResultados(Integer totalResultados) {
		this.totalResultados = totalResultados;
	}

}
