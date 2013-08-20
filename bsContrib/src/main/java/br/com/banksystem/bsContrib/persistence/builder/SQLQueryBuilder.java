package br.com.banksystem.bsContrib.persistence.builder;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroHQLSQL;

public class SQLQueryBuilder {

	public <O> List<O> consultarDTO(final FiltroHQLSQL<O> filtro, EntityManager entityManager) {
		List<O> resultado = null;
		Query query = criarQueryDTO(filtro, entityManager);

		if (filtro.getConsultarTotalResultado()) {
			filtro.setTotalResultados(queryTotalHQL(filtro, entityManager).intValue());
		}
		preencherInformacaoPaginacao(filtro, query);

		resultado = retonarResultadoQueryDTO(filtro, query);

		return resultado;
	}

	/**
	 * Metodo para criar a query de consulta DTO
	 * 
	 * @param filtro Filtro da query DTO
	 * @return Query com as informacoes da consulta preenchida
	 */
	private <O> Query criarQueryDTO(FiltroHQLSQL<O> filtro, EntityManager entityManager) {
		Query query;
		StringBuilder sb = new StringBuilder();
		sb.append(filtro.getConsultaCompleta());
		if (!filtro.isConsultaSQL()) {
			query = entityManager.createQuery(sb.toString());
		} else {
			query = entityManager.createNativeQuery(sb.toString());
		}
		filtro.setParametros(query);
		return query;
	}

	/**
	 * Gera a consulta HQL de COUNT de resultado
	 * 
	 * @param filtro Filtro da consulta
	 * @return Consulta HQL
	 */
	protected String gerarCountHQLQuery(final FiltroHQLSQL<?> filtro) {
		StringBuilder sb = new StringBuilder();

		String from = "";
		String queryString = null;
		if (filtro.isConsultaSQL()) {
			queryString = filtro.getSQLComFiltros();
		} else {
			queryString = filtro.getHQLComFiltros();
		}
		if (queryString != null) {
			int fromPosition = queryString.toLowerCase().indexOf("from");
			if (fromPosition >= 0) {
				from = queryString.substring(fromPosition);
			}
		}

		sb.append("SELECT count(");
		if ((filtro.getResultadosUnicos() != null) && filtro.getResultadosUnicos()) {
			sb.append(" distinct ");
		}
		if (filtro.getAlias() != null) {
			sb.append(filtro.getAlias()).append(".");
		}
		sb.append("id)");
		sb.append(" ").append(from);

		return sb.toString();
	}

	protected String gerarCountSQLQuery(final FiltroHQLSQL<?> filtro) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(*) FROM ( ");
		sb.append(filtro.getSQLComFiltros());
		sb.append(" ) t ");
		return sb.toString();
	}

	public <O> List<O> listarDTO(FiltroHQLSQL<O> filtro, EntityManager entityManager) {
		List<O> resultado = null;
		Query query = null;
		query = criarQueryDTO(filtro, entityManager);

		if (filtro.getConsultarTotalResultado()) {
			filtro.setTotalResultados(queryTotalHQL(filtro, entityManager).intValue());
		}

		resultado = retonarResultadoQueryDTO(filtro, query);

		return resultado;
	}

	/**
	 * Metodo para colocar na query as informacoes referentes a paginacao
	 * 
	 * @param filtro Filtro com as informacoes
	 * @param query Query que tera as informacoes preenchidas
	 */
	protected void preencherInformacaoPaginacao(final FiltroGenerico<?> filtro, Query query) {
		// Coloca informacoes da paginacao
		query.setFirstResult(filtro.getPrimeiroResultado() != null ? filtro.getPrimeiroResultado() : 0);
		if ((filtro.getLimiteResultados() != null) && (filtro.getLimiteResultados() > 0)) {
			query.setMaxResults(filtro.getLimiteResultados());
		} else {
			query.setMaxResults(filtro.getTamanhoPagina());
		}
	}

	/**
	 * Metodo para criar a query de total para filtro HQLDTO
	 * 
	 * @param filtro Filtro com os dados da query
	 * @return Total de resultados de acordo com a query
	 */
	public Long queryTotalHQL(final FiltroHQLSQL<?> filtro, EntityManager entityManager) {
		Long retorno;
		Query query = null;
		if (filtro.isConsultaSQL()) {
			query = entityManager.createNativeQuery(gerarCountSQLQuery(filtro));
		} else {
			query = entityManager.createQuery(gerarCountHQLQuery(filtro));
		}
		filtro.setParametros(query);
		Object singleResult = query.getSingleResult();
		if (singleResult != null) {
			if (singleResult instanceof Number) {
				retorno = ((Number) singleResult).longValue();
			} else {
				retorno = Long.valueOf(singleResult.toString());
			}
		} else {
			retorno = 0l;
		}
		return retorno;
	}

	/**
	 * Metodo para executar a query de DTO e tratar o resultado da mesma de acordo com tipo de filtro
	 * 
	 * @param filtro Filtro de consulta
	 * @param query Query a ser executada
	 * @return Lista com os objetos retornados pela query
	 */
	@SuppressWarnings("unchecked")
	private <O> List<O> retonarResultadoQueryDTO(final FiltroHQLSQL<O> filtro, Query query) {
		List<O> resultado;
		if (filtro.isConsultaSQL()) {
			resultado = new ArrayList<O>();
			List<Object[]> objetos = query.getResultList();
			for (Object[] objeto: objetos) {
				resultado.add(filtro.montarObjeto(objeto));
			}
		} else {
			resultado = query.getResultList();
		}
		return resultado;
	}

}
