package br.com.banksystem.bsContrib.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import br.com.banksystem.bsContrib.business.TurmaBC;
import br.com.banksystem.bsContrib.domain.Turma;
import br.com.banksystem.bsContrib.util.TipoTurma;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;

@ViewController
@PreviousView("/turma_list.xhtml")
public class TurmaEditMB extends AbstractEditPageBean<Turma, Long> {

	private static final long serialVersionUID = -2157083040996279098L;
	@Inject
	private TurmaBC turmaBC;

	private List<TipoTurma> listaTipo;

	@Override
	public String delete() {
		turmaBC.delete(getId());
		return getPreviousView();
	}

	public List<TipoTurma> getListaTipo() {
		if (listaTipo == null) {
			listaTipo = new ArrayList<TipoTurma>();
			listaTipo.addAll(Arrays.asList(TipoTurma.values()));
		}
		return listaTipo;
	}

	@Override
	protected void handleLoad() {
		setBean(turmaBC.load(getId()));
	}

	@Override
	public String insert() {
		turmaBC.insert(getBean());
		return getPreviousView();
	}

	@Override
	public String update() {
		turmaBC.update(getBean());
		return getPreviousView();
	}

}
