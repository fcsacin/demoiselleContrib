package br.com.banksystem.bsContrib.view;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import br.com.banksystem.bsContrib.business.AlunoBC;
import br.com.banksystem.bsContrib.business.dto.RelatorioAlunoDTO;
import br.gov.frameworkdemoiselle.stereotype.ViewController;

@ViewController
@ViewScoped
public class AlunoReportMB implements Serializable {

	private static final long serialVersionUID = 5241775789427090920L;

	private CartesianChartModel model;

	@Inject
	private AlunoBC alunoBC;

	private void carregarChartModel() {
		List<RelatorioAlunoDTO> dadosAlunos = alunoBC.gerarDadosAlunos();
		if (!dadosAlunos.isEmpty()) {
			ChartSeries serie = new ChartSeries();
			serie.setLabel("Alunos");
			for (RelatorioAlunoDTO item: dadosAlunos) {
				serie.set(item.getTipo(), item.getTotal());
				// seriePontosContratos.set(contrato.getNumero(), contrato.getPontosRestantes());
			}
			getModel().addSeries(serie);
		}
	}

	public CartesianChartModel getModel() {
		if (model == null) {
			model = new CartesianChartModel();
			carregarChartModel();
		}
		return model;
	}

}
