package br.com.banksystem.bsContrib.business;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import br.com.banksystem.bsContrib.business.dto.RelatorioAlunoDTO;
import br.com.banksystem.bsContrib.business.filtro.FiltroAluno;
import br.com.banksystem.bsContrib.domain.Aluno;
import br.com.banksystem.bsContrib.persistence.AlunoDAO;
import br.com.banksystem.bsContrib.persistence.expressao.TipoExpressao;
import br.com.banksystem.bsContrib.persistence.expressao.implementacao.ExpressaoImpl;
import br.com.banksystem.bsContrib.persistence.expressao.implementacao.ExpressaoUnariaImpl;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.com.banksystem.bsContrib.persistence.filtro.Operator;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@BusinessController
public class AlunoBC extends GenericBC<Aluno, Long, AlunoDAO> {

	private static final long serialVersionUID = -6236657683899707758L;

	@Override
	protected void adicionarParametrosConsulta(FiltroGenerico<Aluno> filtro) {
		FiltroAluno filtroConsulta = (FiltroAluno) filtro;
		if (!StringUtils.isEmpty(filtroConsulta.getEntidadeConsulta().getNome())) {
			filtroConsulta.addExpressao(new ExpressaoUnariaImpl(TipoExpressao.LIKE, FiltroAluno.NOME, filtroConsulta.getEntidadeConsulta().getNome()));
		}
		if (filtroConsulta.getAlturaInicial() != null || filtroConsulta.getAlturaFinal() != null) {

			if (filtroConsulta.getAlturaInicial() != null && filtroConsulta.getAlturaFinal() != null) {
				filtroConsulta.addExpressao(new ExpressaoUnariaImpl(TipoExpressao.BETWEEN, FiltroAluno.ALTURA, filtroConsulta.getAlturaInicial(),
						filtroConsulta.getAlturaFinal()));
			} else if (filtroConsulta.getAlturaInicial() != null) {
				filtroConsulta.addExpressao(new ExpressaoUnariaImpl(TipoExpressao.GREATER_EQUALS_THEN, FiltroAluno.ALTURA, filtroConsulta
						.getAlturaInicial()));
			} else {
				filtroConsulta.addExpressao(new ExpressaoUnariaImpl(TipoExpressao.LESS_EQUALS_THEN, FiltroAluno.ALTURA, filtroConsulta.getAlturaFinal()));
			}

		}

	}

	/**
	 * Exemplo Bobo e sem sentido da utilizacao de filtro com expressao "binaria".
	 * 
	 * @return total da consulta
	 */
	private Long consultarAlunosFiltros(Integer alturaInicial, Integer alturaFinal, Integer numeroFilhos) {
		Long total = 0l;

		FiltroAluno filtroAluno = new FiltroAluno();

		ExpressaoUnariaImpl exp1 = new ExpressaoUnariaImpl(TipoExpressao.BETWEEN, FiltroAluno.ALTURA, alturaInicial, alturaFinal);
		ExpressaoUnariaImpl exp2 = new ExpressaoUnariaImpl(TipoExpressao.GREATER_EQUALS_THEN, FiltroAluno.FILHOS, numeroFilhos);

		filtroAluno.addExpressao(new ExpressaoImpl(Operator.OR, exp1, exp2));

		total = getDelegate().totalResultado(filtroAluno);

		return total;
	}

	/**
	 * Exemplo Bobo e sem sentido da utilizacao de filtros com expressoes binarias.
	 * 
	 * @return relatorio
	 */
	public List<RelatorioAlunoDTO> gerarDadosAlunos() {
		List<RelatorioAlunoDTO> lista = new ArrayList<RelatorioAlunoDTO>();

		lista.add(new RelatorioAlunoDTO("Entre 100/120 Ou mais de 4 filhos", consultarAlunosFiltros(100, 120, 4)));
		lista.add(new RelatorioAlunoDTO("Entre 120/140 Ou mais de 4 filhos", consultarAlunosFiltros(120, 140, 4)));
		lista.add(new RelatorioAlunoDTO("Entre 140/160 Ou mais de 4 filhos", consultarAlunosFiltros(140, 160, 4)));
		lista.add(new RelatorioAlunoDTO("Entre 160/180 Ou mais de 4 filhos", consultarAlunosFiltros(160, 180, 4)));
		lista.add(new RelatorioAlunoDTO("Entre 180/200 Ou mais de 4 filhos", consultarAlunosFiltros(180, 200, 4)));
		lista.add(new RelatorioAlunoDTO("Entre 200/220 Ou mais de 4 filhos", consultarAlunosFiltros(200, 220, 4)));

		return lista;
	}

	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {
			insert(new Aluno("Insert1", 200, 2));
			insert(new Aluno("Insert2", 150, 0));
			insert(new Aluno("Insert3", 173, 0));
			insert(new Aluno("Insert4", 180, 0));
			insert(new Aluno("Insert5", 190, 1));
			insert(new Aluno("Insert6", 165, 4));
			insert(new Aluno("Insert7", 180, 5));
			insert(new Aluno("Insert8", 175, 4));
			insert(new Aluno("Insert9", 148, 1));
			insert(new Aluno("Insert10", 160, 0));
		}
	}

}
