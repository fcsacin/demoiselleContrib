package br.com.banksystem.bsContrib.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import br.com.banksystem.bsContrib.persistence.builder.DemoiselleCriteriaBuilder;
import br.com.banksystem.bsContrib.persistence.builder.SQLQueryBuilder;
import br.com.banksystem.bsContrib.persistence.expressao.TipoExpressao;
import br.com.banksystem.bsContrib.persistence.expressao.implementacao.ExpressaoUnariaImpl;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroCriteriaGenerico;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroHQLSQL;
import br.gov.frameworkdemoiselle.template.JPACrud;

public abstract class DAOGenerico<T, I extends Serializable> extends JPACrud<T, I> {

	private static final long serialVersionUID = -1903869057514956978L;

	@Inject
	private DemoiselleCriteriaBuilder demoiselleCriteriaBuilder;

	@Inject
	private SQLQueryBuilder sqlQueryBuilder;

	/**
	 * Metodo que adicionar os parametros do model se existirem.
	 * 
	 * @param filtro Filtro de consulta
	 */
	protected void adicionarParametrosFiltrosModel(FiltroCriteriaGenerico<T> filtroCriteria) {
		if (filtroCriteria.getFiltrosModel() != null) {
			Iterator<Entry<String, String>> iterator = filtroCriteria.getFiltrosModel().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> item = iterator.next();
				filtroCriteria.addExpressao(new ExpressaoUnariaImpl(TipoExpressao.LIKE, item.getKey(), item.getValue()));
			}
		}
		// Adiciona o campo ordenado
		if (filtroCriteria.getCampoOrdenadoModel() != null) {
			filtroCriteria.addCampoOrdenado(filtroCriteria.getCampoOrdenadoModel(), true);
		}
	}

	/**
	 * Método que cria uma Query a partir do filtro passado como parâmetro.
	 * 
	 * @param filtro Filtro de consulta
	 * @return Retorna a query criada com todos os parâmetros passados e pronto para ser executada
	 */
	protected Query criarQueryFiltro(final FiltroGenerico<T> filtro) {
		Query query = null;
		// Será utilizada a consulta HQL ou SQL escrita no filtro
		if (filtro instanceof FiltroHQLSQL) {
			StringBuilder sb = new StringBuilder();
			FiltroHQLSQL<T> filtroHQL = (FiltroHQLSQL<T>) filtro;
			sb.append(filtroHQL.getConsultaCompleta());
			if (!filtroHQL.isConsultaSQL()) {
				query = getEntityManager().createQuery(sb.toString());
			} else {
				query = getEntityManager().createNativeQuery(sb.toString());
			}
			filtroHQL.setParametros(query);
		} else {
			adicionarParametrosFiltrosModel((FiltroCriteriaGenerico<T>) filtro);

			CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = createCriteriaQuery();

			criteriaQuery = demoiselleCriteriaBuilder.createQuery(criteriaBuilder, criteriaQuery, (FiltroCriteriaGenerico<T>) filtro, getBeanClass());

			query = getEntityManager().createQuery(criteriaQuery);
		}
		return query;
	}

	/**
	 * Método que retorna a lista de objetos que se enquadram no filtro de consulta. O retorno levará sempre em consideração as informações
	 * de paginação dos dados passadas no filtro.
	 */
	public List<T> findByFilter(final FiltroGenerico<T> filtro) {
		List<T> resultado = null;
		Query query = criarQueryFiltro(filtro);

		// Coloca informacoes da paginacao
		if (filtro != null) {
			if (filtro.getConsultarTotalResultado()) {
				filtro.setTotalResultados(totalResultado(filtro).intValue());
			}
			preencherInformacaoPaginacao(filtro, query);
		}

		resultado = retornarResultadoQuery(filtro, query);

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
	protected Long queryTotalHQL(final FiltroHQLSQL<?> filtro) {
		return sqlQueryBuilder.queryTotalHQL(filtro, getEntityManager());
	}

	/**
	 * Metodo para executar a query e tratar o resultado da mesma de acordo com tipo de filtro
	 * 
	 * @param filtro Filtro de consulta
	 * @param query Query a ser executada
	 * @return Lista com os objetos retornados pela query
	 */
	@SuppressWarnings("unchecked")
	private List<T> retornarResultadoQuery(FiltroGenerico<T> filtro, Query query) {
		List<T> resultado;
		if (filtro instanceof FiltroHQLSQL && (((FiltroHQLSQL<T>) filtro).isConsultaSQL())) {
			resultado = new ArrayList<T>();
			List<Object[]> objetos = query.getResultList();
			for (Object[] objeto: objetos) {
				resultado.add(((FiltroHQLSQL<T>) filtro).montarObjeto(objeto));
			}
		} else {
			resultado = query.getResultList();
		}
		return resultado;
	}

	/**
	 * Método que retorna o total de registros retornados na consulta
	 * 
	 * @param filtro Filtro com os parâmetros de consulta
	 */
	public Long totalResultado(final FiltroGenerico<T> filtro) {
		Long retorno = 0l;
		if (filtro instanceof FiltroHQLSQL) {
			retorno = queryTotalHQL((FiltroHQLSQL<T>) filtro);
		} else {
			adicionarParametrosFiltrosModel((FiltroCriteriaGenerico<T>) filtro);
			CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
			CriteriaQuery<Long> criteriaCountQuery = criteriaBuilder.createQuery(Long.class);

			criteriaCountQuery = demoiselleCriteriaBuilder.createCountQuery(criteriaBuilder, criteriaCountQuery, (FiltroCriteriaGenerico<T>) filtro,
					getBeanClass());
			retorno = getEntityManager().createQuery(criteriaCountQuery).getSingleResult().longValue();

		}

		return retorno;
	}
}
