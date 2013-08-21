package br.com.banksystem.bsContrib.domain;

import static javax.persistence.GenerationType.SEQUENCE;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Aluno implements Serializable, IPojo<Long> {

	private static final long serialVersionUID = 2336304510181432582L;

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	private String nome;

	private Integer altura;

	@Temporal(TemporalType.DATE)
	private Date nascimento;

	@ManyToMany
	@JoinTable(name = "aluno_turma", joinColumns = @JoinColumn(name = "aluno_id"), inverseJoinColumns = @JoinColumn(name = "turma_id"))
	private List<Turma> turmas;

	public Aluno() {
		super();
	}

	public Aluno(String nome, Integer altura) {
		super();
		this.nome = nome;
		this.altura = altura;
	}

	public Integer getAltura() {
		return altura;
	}

	@Override
	public Long getId() {
		return id;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public String getNome() {
		return nome;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setAltura(Integer altura) {
		this.altura = altura;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

}
