package br.com.banksystem.bsContrib.view;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import br.com.banksystem.bsContrib.business.AlunoBC;
import br.com.banksystem.bsContrib.business.filtro.FiltroAluno;
import br.com.banksystem.bsContrib.domain.Aluno;
import br.com.banksystem.bsContrib.view.template.GenericListPageBean;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;

@ViewController
@NextView("/aluno_edit.xhtml")
@PreviousView("/aluno_list.xhtml")
public class AlunoListMB extends GenericListPageBean<Aluno, Long, FiltroAluno> {

	private static final long serialVersionUID = -7568516468143950949L;

	@Inject
	private AlunoBC alunoBC;

	@Override
	public Aluno buscarEntidade(Serializable id) {
		return alunoBC.load(Long.valueOf((String) id));
	}

	@Override
	public List<Aluno> consultar() {
		return alunoBC.consultar(getFiltro());
	}

}
