package br.com.banksystem.bsContrib.persistence.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import br.com.banksystem.bsContrib.persistence.expressao.ExpressaoBinaria;
import br.com.banksystem.bsContrib.persistence.expressao.ExpressaoUnaria;
import br.com.banksystem.bsContrib.persistence.expressao.FieldPath;
import br.com.banksystem.bsContrib.persistence.expressao.IExpressao;
import br.com.banksystem.bsContrib.persistence.expressao.TipoExpressao;
import br.com.banksystem.bsContrib.persistence.expressao.ValorExpressao;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroCriteriaGenerico;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.com.banksystem.bsContrib.persistence.filtro.Operator;
import br.com.banksystem.bsContrib.persistence.filtro.Order.OrderType;

@ApplicationScoped
public class DemoiselleCriteriaBuilder {

	public DemoiselleCriteriaBuilder() {
		super();
	}

	private <T> CriteriaQuery<T> adicionarCamposSelect(final CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery,
			final FiltroCriteriaGenerico<T> filtro, Root<T> root, final Class<T> beanClass) {
		/* Verifica se ira selecionar alguns campos especificos */
		if (filtro.getCamposRetornados() != null || filtro.getRetornarApenasCamposPadrao()) {
			List<Selection<?>> listaSelection = new ArrayList<Selection<?>>();

			if (filtro.getCamposRetornados() != null) {
				listaSelection.addAll(getSelectionCampos(root, filtro.getCamposRetornados()));
			}

			Selection<?>[] array = new Selection<?>[listaSelection.size()];
			array = listaSelection.toArray(array);
			criteriaQuery = criteriaQuery.select(criteriaBuilder.construct(beanClass, array));
		} else {
			criteriaQuery = criteriaQuery.select(root);
		}
		return criteriaQuery;
	}

	private Predicate adicionarExpressao(final CriteriaBuilder criteriaBuilder, final Root<?> root, Predicate predicate, IExpressao expressao) {
		Predicate retorno = null;
		Operator operadorPredicate = null;
		if (expressao instanceof ExpressaoBinaria) {
			ExpressaoBinaria expressaoBinaria = (ExpressaoBinaria) expressao;
			IExpressao primeiraExpressao = expressaoBinaria.getPrimeiraExpressao();
			IExpressao segundaExpressao = expressaoBinaria.getSegundaExpressao();
			Predicate predicatePrimeira = adicionarExpressao(criteriaBuilder, root, null, primeiraExpressao);
			Predicate predicateSegunda = adicionarExpressao(criteriaBuilder, root, null, segundaExpressao);

			operadorPredicate = expressaoBinaria.getOperadorExpressao();

			if (expressaoBinaria.getOperadorInterno().equals(Operator.AND)) {
				retorno = criteriaBuilder.and(predicatePrimeira, predicateSegunda);
			} else {
				retorno = criteriaBuilder.or(predicatePrimeira, predicateSegunda);
			}

		} else if ((expressao instanceof ExpressaoUnaria) || (expressao instanceof ValorExpressao)) {
			ValorExpressao valorExpressao = null;
			if (expressao instanceof ExpressaoUnaria) {
				ExpressaoUnaria expressaoUnaria = (ExpressaoUnaria) expressao;
				operadorPredicate = expressaoUnaria.getOperador();
				valorExpressao = expressaoUnaria.getValorExpressao();
			} else {
				valorExpressao = (ValorExpressao) expressao;
			}

			if (isValorExpressaoPossuiValor(valorExpressao)) {
				retorno = getPredicate(criteriaBuilder, getPathCampo(valorExpressao, root), valorExpressao);
			}
		}

		if ((retorno != null) && (predicate != null)) {
			if ((operadorPredicate == null) || operadorPredicate.equals(Operator.AND)) {
				retorno = criteriaBuilder.and(predicate, retorno);
			} else {
				retorno = criteriaBuilder.or(predicate, retorno);
			}
		}

		return retorno;
	}

	public <T> void adicionarOrdenacoes(final CriteriaBuilder criteriaBuilder, final CriteriaQuery<T> criteriaQuery, final Root<T> root,
			final FiltroGenerico<T> filtro) {
		if ((filtro.getCamposOrdenados() != null) && !filtro.getCamposOrdenados().isEmpty()) {
			List<Order> orders = new ArrayList<Order>();
			for (br.com.banksystem.bsContrib.persistence.filtro.Order order: filtro.getCamposOrdenados()) {
				if (order.getTipo().equals(OrderType.ASC)) {
					orders.add(criteriaBuilder.asc(getPathCampo(order, root)));
				} else {
					orders.add(criteriaBuilder.desc(getPathCampo(order, root)));
				}
			}
			criteriaQuery.orderBy(orders);
		}
	}

	private <T> void adicionarParametros(final CriteriaBuilder criteriaBuilder, final CriteriaQuery<?> criteriaQuery, final Root<T> root,
			final FiltroCriteriaGenerico<T> filtro) {
		/** Adiciona o parâmetro para retornar sempre os elementos ativos */
		Predicate predicate = null;
		if ((filtro.getExpressoes() != null) && !filtro.getExpressoes().isEmpty()) {
			for (IExpressao expressao: filtro.getExpressoes()) {
				predicate = adicionarExpressao(criteriaBuilder, root, predicate, expressao);
			}
		}
		if (predicate != null) {
			criteriaQuery.where(predicate);
		}
	}

	public <T> CriteriaQuery<Long> createCountQuery(final CriteriaBuilder criteriaBuilder, CriteriaQuery<Long> criteriaQuery,
			final FiltroCriteriaGenerico<T> filtro, final Class<T> beanClass) {

		Root<T> root = criteriaQuery.from(beanClass);

		criteriaQuery = criteriaQuery.select(criteriaBuilder.countDistinct(root));

		adicionarParametros(criteriaBuilder, criteriaQuery, root, filtro);

		// adicionarAgrupamentos(criteriaBuilder, criteriaQuery, root, filtro);

		return criteriaQuery;
	}

	public <T> CriteriaQuery<T> createFindAllQuery(final CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, final Class<T> beanClass,
			final Boolean limitarResultadosAtivos) {
		Root<T> root = criteriaQuery.from(beanClass);

		criteriaQuery = criteriaQuery.select(root);

		return criteriaQuery;
	}

	public <T> CriteriaQuery<T> createQuery(final CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, final FiltroCriteriaGenerico<T> filtro,
			final Class<T> beanClass) {

		Root<T> root = criteriaQuery.from(beanClass);

		criteriaQuery = adicionarCamposSelect(criteriaBuilder, criteriaQuery, filtro, root, beanClass);

		adicionarParametros(criteriaBuilder, criteriaQuery, root, filtro);

		// adicionarAgrupamentos(criteriaBuilder, criteriaQuery, root, filtro);

		adicionarOrdenacoes(criteriaBuilder, criteriaQuery, root, filtro);

		if ((filtro.getResultadosUnicos() != null) && filtro.getResultadosUnicos()) {
			criteriaQuery = criteriaQuery.distinct(true);
		}

		return criteriaQuery;
	}

	private String escapeCaracteres(final String valor) {
		String retorno = valor;
		retorno = retorno.replaceAll("\\%", "\\\\%");
		// TODO colocar mais caracteres de escape
		return retorno;
	}

	/**
	 * Retorno as strings referentes ao "caminho" para encontrar o campo na entidade
	 * 
	 * @param campo
	 * @return
	 */
	private String[] getCamposString(String campo) {
		return campo.split("\\.");
	}

	private Path<?> getPath(final Root<?> root, String... camposValor) {
		return getPathHash(root, new HashMap<String, Join<?, ?>>(), camposValor);
	}

	private Path<?> getPathCampo(final FieldPath parametro, final Root<?> root) {
		String[] camposValor = getCamposString(parametro.getCampo());
		return getPath(root, camposValor);
	}

	private Path<?> getPathHash(final Root<?> root, Map<String, Join<?, ?>> joinMap, String... camposValor) {
		Path<?> path = null;
		if (camposValor.length > 1) {
			Join<?, ?> join = null;
			StringBuilder builder = new StringBuilder();
			for (int param = 0; param < camposValor.length - 1; param++) {
				String campo = camposValor[param];
				builder.append(campo);
				Join<?, ?> join2 = joinMap.get(builder.toString());
				if (join2 == null) {
					if (join == null) {
						join = root.join(campo, JoinType.LEFT);
					} else {
						join = join.join(campo, JoinType.LEFT);
					}
					joinMap.put(builder.toString(), join);
				} else {
					join = join2;
				}
				builder.append(".");
			}
			path = join.get(camposValor[camposValor.length - 1]);
		} else {
			path = root.get(camposValor[0]);
		}
		return path;
	}

	/**
	 * Retorna a expressão referente ao parâmetro passado.
	 * 
	 * @param criteriaBuilder CriteriaBuilder utilizado para criar as expressões
	 * @param root Objeto root do select
	 * @param parametro Parâmetro a ter o predicate criado
	 * @return Predicate com a expressão do parâmetro
	 */
	@SuppressWarnings("unchecked")
	private <T> Predicate getPredicate(final CriteriaBuilder criteriaBuilder, final Path<?> path, final ValorExpressao parametro) {
		Predicate predicate = null;
		if (parametro.getTipoParametro().equals(TipoExpressao.EQUALS)) {
			predicate = criteriaBuilder.equal(path, parametro.getValor());
		} else if (parametro.getTipoParametro().equals(TipoExpressao.EQUALS_IGNORECASE) && String.class.equals(parametro.getValor().getClass())) {
			predicate = criteriaBuilder.equal(criteriaBuilder.upper((Expression<String>) path), String.valueOf(parametro.getValor()).toUpperCase());
		} else if (parametro.getTipoParametro().equals(TipoExpressao.LIKE) && String.class.equals(parametro.getValor().getClass())) {
			predicate = criteriaBuilder.like(criteriaBuilder.upper((Expression<String>) path),
					String.valueOf("%" + String.valueOf(escapeCaracteres((String) parametro.getValor())) + "%").toUpperCase(), '\\');
		} else if (parametro.getTipoParametro().equals(TipoExpressao.RIGHT_LIKE) && String.class.equals(parametro.getValor().getClass())) {
			predicate = criteriaBuilder.like(criteriaBuilder.upper((Expression<String>) path),
					String.valueOf(String.valueOf(escapeCaracteres((String) parametro.getValor())) + "%").toUpperCase(), '\\');
		} else if (parametro.getTipoParametro().equals(TipoExpressao.LEFT_LIKE) && String.class.equals(parametro.getValor().getClass())) {
			predicate = criteriaBuilder.like(criteriaBuilder.upper((Expression<String>) path),
					String.valueOf("%" + String.valueOf(escapeCaracteres((String) parametro.getValor()))).toUpperCase(), '\\');
		} else if (parametro.getTipoParametro().equals(TipoExpressao.NOT_EQUALS)) {
			predicate = criteriaBuilder.notEqual(path, parametro.getValor());
		} else if (parametro.getTipoParametro().equals(TipoExpressao.IN)) {
			predicate = criteriaBuilder.in(path).value(parametro.getValor());
		} else if (parametro.getTipoParametro().equals(TipoExpressao.NOT_IN)) {
			predicate = criteriaBuilder.not(criteriaBuilder.in(path).value(parametro.getValor()));
		} else if (parametro.getTipoParametro().equals(TipoExpressao.IS_MEMBER)) {
			predicate = criteriaBuilder.isMember(parametro.getValor(), (Expression<Collection<Object>>) path);
		} else if (parametro.getTipoParametro().equals(TipoExpressao.IS_NOT_MEMBER)) {
			predicate = criteriaBuilder.isNotMember(parametro.getValor(), (Expression<Collection<Object>>) path);
		} else if (parametro.getTipoParametro().equals(TipoExpressao.GREATER_THEN)) {
			if (isNumberClass(parametro.getValor().getClass())) {
				predicate = criteriaBuilder.gt((Expression<Number>) path, (Number) parametro.getValor());
			} else if (Date.class.isAssignableFrom(parametro.getValor().getClass())) {
				predicate = criteriaBuilder.greaterThan((Expression<? extends Date>) path, (Date) parametro.getValor());
			}
		} else if (parametro.getTipoParametro().equals(TipoExpressao.GREATER_EQUALS_THEN)) {
			if (isNumberClass(parametro.getValor().getClass())) {
				predicate = criteriaBuilder.ge((Expression<Number>) path, (Number) parametro.getValor());
			} else if (Date.class.isAssignableFrom(parametro.getValor().getClass())) {
				predicate = criteriaBuilder.greaterThanOrEqualTo((Expression<? extends Date>) path, (Date) parametro.getValor());
			}
		} else if (parametro.getTipoParametro().equals(TipoExpressao.LESS_THEN)) {
			if (isNumberClass(parametro.getValor().getClass())) {
				predicate = criteriaBuilder.lt((Expression<Number>) path, (Number) parametro.getValor());
			} else if (Date.class.isAssignableFrom(parametro.getValor().getClass())) {
				predicate = criteriaBuilder.lessThan((Expression<? extends Date>) path, (Date) parametro.getValor());
			}
		} else if (parametro.getTipoParametro().equals(TipoExpressao.LESS_EQUALS_THEN)) {
			if (isNumberClass(parametro.getValor().getClass())) {
				predicate = criteriaBuilder.le((Expression<Number>) path, (Number) parametro.getValor());
			} else if (Date.class.isAssignableFrom(parametro.getValor().getClass())) {
				predicate = criteriaBuilder.lessThanOrEqualTo((Expression<? extends Date>) path, (Date) parametro.getValor());
			}
		} else if (parametro.getTipoParametro().equals(TipoExpressao.IS_NULL)) {
			predicate = criteriaBuilder.isNull(path);
		} else if (parametro.getTipoParametro().equals(TipoExpressao.IS_NOT_NULL)) {
			predicate = criteriaBuilder.isNotNull(path);
		} else if (parametro.getTipoParametro().equals(TipoExpressao.BETWEEN) && (parametro.getValores().size() == 2)) {
			Iterator<Object> iterator = parametro.getValores().iterator();
			Object primeiroParametro = iterator.next();
			Object segundoParametro = iterator.next();
			if (primeiroParametro.getClass().equals(Integer.class) && segundoParametro.getClass().equals(Integer.class)) {
				predicate = criteriaBuilder.between((Expression<? extends Integer>) path, (Integer) primeiroParametro, (Integer) segundoParametro);
			} else if (primeiroParametro.getClass().equals(Long.class) && segundoParametro.getClass().equals(Long.class)) {
				predicate = criteriaBuilder.between((Expression<? extends Long>) path, (Long) primeiroParametro, (Long) segundoParametro);
			} else if (Date.class.isAssignableFrom(parametro.getValor().getClass())) {
				predicate = criteriaBuilder.between((Expression<? extends Date>) path, (Date) primeiroParametro, (Date) segundoParametro);
			}
		} else if (parametro.getTipoParametro().equals(TipoExpressao.IS_EMPTY)) {
			criteriaBuilder.isEmpty((Expression<? extends Collection<?>>) path);
		} else if (parametro.getTipoParametro().equals(TipoExpressao.IS_NOT_EMPTY)) {
			criteriaBuilder.isNotEmpty((Expression<? extends Collection<?>>) path);
		}
		// TODO adicionar demais tipos
		return predicate;
	}

	private <T> List<Selection<?>> getSelectionCampos(Root<T> root, List<String> campos) {
		List<Selection<?>> listaSelection = new ArrayList<Selection<?>>();

		HashMap<String, Join<?, ?>> hashMap = new HashMap<String, Join<?, ?>>();
		for (String campo: campos) {
			listaSelection.add(getPathHash(root, hashMap, getCamposString(campo)));
		}

		return listaSelection;
	}

	/**
	 * Método para checar se uma classe passada é de algum tipo Number
	 * 
	 * @param clazz Classe que deseja-se checar
	 * @return TRUE se classe pertence a familia de Number
	 */
	protected boolean isNumberClass(Class<?> clazz) {
		boolean numberClass = false;
		if (clazz != null) {
			if (Number.class.isAssignableFrom(clazz)) {
				numberClass = true;
			} else {
				numberClass = isNumberClass(clazz.getSuperclass());
			}
		}

		return numberClass;
	}

	private boolean isValorExpressaoPossuiValor(ValorExpressao valorExpressao) {
		boolean possuiValor = false;
		possuiValor = ((valorExpressao.getValores() != null) && !valorExpressao.getValores().isEmpty())
				|| (valorExpressao.getTipoParametro().equals(TipoExpressao.IS_NULL) || valorExpressao.getTipoParametro().equals(TipoExpressao.IS_NOT_NULL));
		return possuiValor;
	}

}
