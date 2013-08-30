package br.com.banksystem.bsContrib.business.dto;

public class RelatorioAlunoDTO {

	private String tipo;

	private Long total;

	public RelatorioAlunoDTO(String tipo, Long total) {
		this.tipo = tipo;
		this.total = total;
	}

	public String getTipo() {
		return tipo;
	}

	public Long getTotal() {
		return total;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

}
