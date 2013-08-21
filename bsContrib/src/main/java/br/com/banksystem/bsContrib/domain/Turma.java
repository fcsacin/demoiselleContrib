package br.com.banksystem.bsContrib.domain;

import static javax.persistence.GenerationType.SEQUENCE;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import br.com.banksystem.bsContrib.util.TipoTurma;

@Entity
public class Turma implements Serializable, IPojo<Long> {

	private static final long serialVersionUID = 6742198810295704207L;

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	private String nome;

	private String resumo;

	@Enumerated(EnumType.ORDINAL)
	private TipoTurma tipoTurma;

	public Turma() {
		super();
	}

	public Turma(String nome, TipoTurma tipo) {
		super();
		this.nome = nome;
		this.tipoTurma = tipo;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getResumo() {
		return resumo;
	}

	public TipoTurma getTipoTurma() {
		return tipoTurma;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public void setTipoTurma(TipoTurma tipoTurma) {
		this.tipoTurma = tipoTurma;
	}

}
