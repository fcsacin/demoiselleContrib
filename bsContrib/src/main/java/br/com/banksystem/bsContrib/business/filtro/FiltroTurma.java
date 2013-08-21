package br.com.banksystem.bsContrib.business.filtro;

import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import br.com.banksystem.bsContrib.domain.Turma;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroHQLSQL;

public class FiltroTurma extends FiltroHQLSQL<Turma> {

	private static final long serialVersionUID = 8904377470866017148L;

	public FiltroTurma() {
		super("t");
	}

	@Override
	public String getHQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(getCamposSelecionados());
		sb.append("FROM Turma t ");
		boolean where = false;
		if (!StringUtils.isEmpty(getEntidadeConsulta().getNome())) {
			where = append(sb, where, "upper(t.nome) LIKE :nome");
		}
		if (getEntidadeConsulta().getTipoTurma() != null) {
			append(sb, where, "t.tipoTurma = :tipoTurma");
		}
		return sb.toString();
	}

	@Override
	public void setParametros(Query query) {
		if (!StringUtils.isEmpty(getEntidadeConsulta().getNome())) {
			query.setParameter("nome", "%" + getEntidadeConsulta().getNome().toUpperCase() + "%");
		}
		if (getEntidadeConsulta().getTipoTurma() != null) {
			query.setParameter("tipoTurma", getEntidadeConsulta().getTipoTurma());
		}
	}

}
