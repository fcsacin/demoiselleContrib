package br.com.banksystem.bsContrib.domain;

import static javax.persistence.GenerationType.SEQUENCE;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Aluno implements Serializable, IPojo<Long> {

	private static final long serialVersionUID = 2336304510181432582L;

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	private String nome;

	private Integer altura;

	private Date nascimento;

	public Aluno() {
		super();
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

}
