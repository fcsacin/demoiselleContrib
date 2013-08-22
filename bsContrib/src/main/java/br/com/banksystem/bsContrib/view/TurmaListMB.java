package br.com.banksystem.bsContrib.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import br.com.banksystem.bsContrib.business.TurmaBC;
import br.com.banksystem.bsContrib.business.filtro.FiltroTurma;
import br.com.banksystem.bsContrib.domain.Turma;
import br.com.banksystem.bsContrib.util.TipoTurma;
import br.com.banksystem.bsContrib.view.template.GenericListPageBean;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;

@ViewController
@NextView("/turma_edit.xhtml")
@PreviousView("/turma_list.xhtml")
public class TurmaListMB extends GenericListPageBean<Turma, Long, FiltroTurma> {

	private static final long serialVersionUID = 7723643604656818476L;

	@Inject
	private TurmaBC turmaBC;

	private List<TipoTurma> listaTipo;

	@Override
	protected TurmaBC getBC() {
		return turmaBC;
	}

	public List<TipoTurma> getListaTipo() {
		if (listaTipo == null) {
			listaTipo = new ArrayList<TipoTurma>();
			listaTipo.addAll(Arrays.asList(TipoTurma.values()));
		}
		return listaTipo;
	}

}
