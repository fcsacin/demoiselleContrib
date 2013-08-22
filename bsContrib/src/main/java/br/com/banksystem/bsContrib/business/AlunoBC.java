package br.com.banksystem.bsContrib.business;

import org.apache.commons.lang.StringUtils;
import br.com.banksystem.bsContrib.business.filtro.FiltroAluno;
import br.com.banksystem.bsContrib.domain.Aluno;
import br.com.banksystem.bsContrib.persistence.AlunoDAO;
import br.com.banksystem.bsContrib.persistence.expressao.TipoExpressao;
import br.com.banksystem.bsContrib.persistence.expressao.implementacao.ExpressaoUnariaImpl;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
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

	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {
			insert(new Aluno("Insert1", 200));
			insert(new Aluno("Insert2", 150));
			insert(new Aluno("Insert3", 173));
			insert(new Aluno("Insert4", 180));
			insert(new Aluno("Insert5", 190));
			insert(new Aluno("Insert6", 165));
		}
	}

}
