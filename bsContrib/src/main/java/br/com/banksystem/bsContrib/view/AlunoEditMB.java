package br.com.banksystem.bsContrib.view;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.primefaces.model.DualListModel;
import br.com.banksystem.bsContrib.business.AlunoBC;
import br.com.banksystem.bsContrib.business.TurmaBC;
import br.com.banksystem.bsContrib.domain.Aluno;
import br.com.banksystem.bsContrib.domain.Turma;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;

@ViewController
@PreviousView("/aluno_list.xhtml")
public class AlunoEditMB extends AbstractEditPageBean<Aluno, Long> {

	private static final long serialVersionUID = -115538494009504552L;

	@Inject
	private AlunoBC alunoBC;

	@Inject
	private TurmaBC turmaBC;

	private DualListModel<Turma> modelTurmas;

	@Override
	public String delete() {
		alunoBC.delete(getId());
		return getPreviousView();
	}

	public DualListModel<Turma> getModelTurmas() {
		if (modelTurmas == null) {
			List<Turma> target = new ArrayList<Turma>();
			List<Turma> source = new ArrayList<Turma>();
			source.addAll(turmaBC.findAll());
			if (getBean().getId() != null) {
				target.addAll(getBean().getTurmas());
			}
			source.removeAll(target);
			modelTurmas = new DualListModel<Turma>(source, target);
		}
		return modelTurmas;
	}

	@Override
	protected void handleLoad() {
		setBean(alunoBC.load(getId()));
	}

	@Override
	public String insert() {
		alunoBC.insert(getBean());
		return getPreviousView();
	}

	public void setModelTurmas(DualListModel<Turma> modelTurmas) {
		this.modelTurmas = modelTurmas;
	}

	@Override
	public String update() {
		alunoBC.update(getBean());
		return getPreviousView();
	}

}
