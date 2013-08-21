package br.com.banksystem.bsContrib.persistence.filtro;

import java.util.Iterator;
import java.util.Map.Entry;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 * Classe de Consulta criada para ser utilizada principalmente em consultas que montam DTOs ao inves de entidades mapeadas pelo JPA. O
 * consultaSQL retornara por padrao TRUE.
 * 
 * @author Filipe Andrade
 * @param <T> Tipo da entidade a ser criada
 */
public abstract class FiltroHQLSQL<T> extends FiltroGenerico<T> {

	private static final long serialVersionUID = -5584499662851520024L;

	private boolean consultaSQL = false;

	private String alias;

	public FiltroHQLSQL() {
		this(null);
	}

	public FiltroHQLSQL(final String alias) {
		super();
		this.alias = alias;
	}

	/**
	 * Adiciona o WHERE ou AND na consulta
	 * 
	 * @param sb Builder com a consulta em construção
	 * @param where Boolean para controle de adição de AND e WHERE
	 * @param and Boolean que informa se deve ser concatenado AND ou OR
	 * @param str String a ser adicionada na consulta
	 * @return Retorna a atualiação do valor do booleano
	 */
	protected boolean append(final StringBuilder sb, final boolean where, final boolean and, final String str) {
		boolean w = where;
		if (where) {
			if (and) {
				sb.append(" AND ");
			} else {
				sb.append(" OR ");
			}
		} else {
			sb.append(" WHERE ");
			w = true;
		}
		sb.append(str);
		return w;
	}

	/**
	 * Adiciona o WHERE ou AND na consulta
	 * 
	 * @param sb Builder com a consulta em construção
	 * @param where Boolean para controle de adição de AND e WHERE
	 * @param str String a ser adicionada na consulta
	 * @return Retorna a atualiação do valor do booleano
	 */
	protected boolean append(final StringBuilder sb, final boolean where, final String str) {
		return append(sb, where, true, str);
	}

	/**
	 * Metodo para adicionar a string de campo no Stringbuilder da consulta. Faz as checagens necessarias e adiciona o campo
	 * 
	 * @param builder StringBuilder com a consulta completa
	 * @param campoAdd String referente ao campo que deve ser adicionada
	 * @param acrescentaSeparador Informa se deve ser adicionado separador
	 */
	private void appendCampo(StringBuilder builder, String campoAdd, boolean acrescentaSeparador) {
		if (acrescentaSeparador) {
			builder.append(" , ");
		}
		if ((getAlias() != null) && !getAlias().isEmpty()) {
			builder.append(getAlias()).append(".");
		}
		builder.append(campoAdd);
	}

	public String getAlias() {
		return alias;
	}

	/**
	 * Monta o select com os campos especificados para serem retornados. Se camposRetornados = null, retorna apenas alias recebido como
	 * parametro
	 */

	public String getCamposSelecionados() {
		StringBuilder camposSelecionados = new StringBuilder();
		if ((getCamposRetornados() != null) || getRetornarApenasCamposPadrao()) {
			camposSelecionados.append(getAlias() + ".id");
			if (getCamposRetornados() != null) {
				for (String campo: getCamposRetornados()) {
					camposSelecionados.append(",").append(getAlias()).append("." + campo);
				}
			}
			return "new " + getBeanClass().getSimpleName() + "(" + camposSelecionados + ")";
		} else {
			return getAlias();
		}
	}

	/**
	 * Retorna a consulta HQL/SQL completa, ja com a adicao das ordenacoes e agrupamentos
	 * 
	 * @return HQL/SQL da consulta completa
	 */
	public String getConsultaCompleta() {
		StringBuilder sb = new StringBuilder();
		if (isConsultaSQL()) {
			sb.append(getSQLComFiltros());
		} else {
			sb.append(getHQLComFiltros());
		}

		if (getCamposAgrupados() != null) {
			sb.append(" GROUP BY ");
			for (int cont = 0; cont < getCamposAgrupados().size(); cont++) {
				String campo = getCamposAgrupados().get(cont);
				appendCampo(sb, campo, (cont < getCamposAgrupados().size() - 1));
			}
		}
		if (getCamposOrdenados() != null || getCampoOrdenadoModel() != null) {
			sb.append(" ORDER BY ");
			boolean addSeparador = false;
			if (getCampoOrdenadoModel() != null) {
				String campoOrder = null;
				if (isConsultaSQL()) {
					campoOrder = getSQLCampoOrdenadoModel();
				} else {
					campoOrder = montarStringCampoOrdenado(getCampoOrdenadoModel());
				}
				if (campoOrder != null) {
					appendCampo(sb, campoOrder, addSeparador);
					addSeparador = true;
				}
			}
			if (getCamposOrdenados() != null) {
				for (int cont = 0; cont < getCamposOrdenados().size(); cont++) {
					Order campo = getCamposOrdenados().get(cont);
					// Verificacao para nao adicionar no order by duas vezes a mesma coluna
					if (getCampoOrdenadoModel() == null || !getCampoOrdenadoModel().equals(campo)) {
						appendCampo(sb, montarStringCampoOrdenado(campo), addSeparador);
						addSeparador = true;
					}
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Metodo que retorna o HQL da consulta a ser realizada. Devera ser implementado na classe filha se a consulta for utilizar HQL OBS:
	 * ESTA CONSULTA NAO DEVERA CONTER ORDER BY OU GROUP BY finais da consulta (Os que forem de consulta internas podem ser colocados)
	 * 
	 * @return Consulta HQL
	 */
	public String getHQL() {
		return null;
	}

	/**
	 * MEtodo que retorna o HQL ja com a juncao dos filtros HQL
	 * 
	 * @return
	 */
	public String getHQLComFiltros() {
		return montarConsultaHqlSqlFiltros(getHQL(), getHQLDosFiltrosDoModel());
	}

	/**
	 * Metodo que podera ser sobrescrito no caso de filtros SQL que adiciona os filtros do getFiltrosModel no formato SQL
	 * 
	 * @return
	 */
	public String getHQLDosFiltrosDoModel() {
		StringBuilder sb = new StringBuilder();
		if (getFiltrosModel() != null && !getFiltrosModel().isEmpty()) {
			Iterator<Entry<String, String>> iterator = getFiltrosModel().entrySet().iterator();
			boolean acrescentarAnd = false;
			while (iterator.hasNext()) {
				if (acrescentarAnd) {
					sb.append(" AND ");
				}
				Entry<String, String> item = iterator.next();
				sb.append("upper( ");
				sb.append(item.getKey());
				sb.append(") ");
				sb.append(" LIKE ");
				sb.append("'%");
				sb.append(item.getValue().toUpperCase());
				sb.append("%' ");
				acrescentarAnd = true;
			}
		}
		return sb.toString();
	}

	/**
	 * Metodo que retorna a SQL da consulta a ser realizada. Devera ser sobrescrita na classe filha se a consulta for SQL nativa. OBS: ESTA
	 * CONSULTA NAO DEVERA CONTER ORDER BY OU GROUP BY finais da consulta (Os que forem de consulta internas podem ser colocados)
	 * 
	 * @return Consulta SQL
	 */
	public String getSQL() {
		return null;
	}

	/**
	 * Metodo que devera ser sobrescrito em consultas SQL para gerar a string do orderBy do campoOrdenadoModel
	 * 
	 * @return String Orderby do campoOrdenadoModel
	 */
	public String getSQLCampoOrdenadoModel() {
		return null;
	}

	/**
	 * Metodo que retorna o SQL ja com a juncao dos filtros SQL do model
	 * 
	 * @return
	 */
	public String getSQLComFiltros() {
		return montarConsultaHqlSqlFiltros(getSQL(), getSQLDosFiltrosDoModel());
	}

	/**
	 * Metodo que devera ser sobrescrito no caso de filtros SQL que adiciona os filtros do getFiltrosModel no formato SQL
	 * 
	 * @return
	 */
	public String getSQLDosFiltrosDoModel() {
		return "";
	}

	/**
	 * Informa se a consulta sera via SQL. Por padrao, o resultado e FALSE.
	 * 
	 * @return TRUE se consulta SQL
	 */
	public boolean isConsultaSQL() {
		return consultaSQL;
	}

	/**
	 * Metodo interno para juncao da consulta e os filtros do Model
	 */
	private String montarConsultaHqlSqlFiltros(String consulta, String filtrosModel) {
		StringBuilder sb = new StringBuilder();
		sb.append(consulta);
		if (StringUtils.isNotEmpty(filtrosModel)) {
			int indice = sb.indexOf("WHERE");
			if (indice != -1) { // Significa que foi encontrado where na consulta ja
				sb.append(" AND ");
			} else {
				sb.append(" WHERE ");
			}
			sb.append(filtrosModel);
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * Metodo a ser implementado em caso de consultas SQL, que deve retornar o objeto do tipo da consulta
	 * 
	 * @param object Objeto que representa o retorno da query SQL
	 */
	public T montarObjeto(Object[] object) {
		return null;
	}

	/**
	 * Monta a string de um BSOrder
	 * 
	 * @param campo
	 * @return
	 */
	private String montarStringCampoOrdenado(Order campo) {
		StringBuilder campoAdd = new StringBuilder(campo.getCampo());
		campoAdd.append(" ").append(campo.getTipo().toString());
		return campoAdd.toString();
	}

	public void setAlias(final String alias) {
		this.alias = alias;
	}

	public void setConsultaSQL(final boolean consultaSQL) {
		this.consultaSQL = consultaSQL;
	}

	/**
	 * Metodo a ser implementado que seta na consulta o valor dos parametros existentes.
	 * 
	 * @param query Consulta a qual tera os parametros incluidos
	 */
	public abstract void setParametros(Query query);

}
