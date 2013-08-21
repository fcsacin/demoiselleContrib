package br.com.banksystem.bsContrib.business;

import org.apache.commons.lang.StringUtils;
import br.com.banksystem.bsContrib.business.filtro.FiltroBookmark;
import br.com.banksystem.bsContrib.domain.Bookmark;
import br.com.banksystem.bsContrib.persistence.BookmarkDAO;
import br.com.banksystem.bsContrib.persistence.expressao.TipoExpressao;
import br.com.banksystem.bsContrib.persistence.expressao.implementacao.ExpressaoUnariaImpl;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@BusinessController
public class BookmarkBC extends GenericBC<Bookmark, Long, BookmarkDAO> {

	private static final long serialVersionUID = 1L;

	@Override
	protected void adicionarParametrosConsulta(FiltroGenerico<Bookmark> filtro) {
		if (!StringUtils.isEmpty(filtro.getEntidadeConsulta().getDescription())) {
			((FiltroBookmark) filtro).addExpressao(new ExpressaoUnariaImpl(TipoExpressao.LIKE, FiltroBookmark.DESCRICAO, filtro.getEntidadeConsulta()
					.getDescription()));
		}
	}

	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {
			insert(new Bookmark("Demoiselle Portal", "http://www.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Demoiselle SourceForge", "http://sf.net/projects/demoiselle"));
			insert(new Bookmark("Twitter", "http://twitter.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Blog", "http://blog.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Wiki", "http://wiki.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Bug Tracking", "http://tracker.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Forum", "http://forum.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("SVN", "http://svn.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Maven", "http://repository.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Downloads", "http://download.frameworkdemoiselle.gov.br"));
		}
	}

}
