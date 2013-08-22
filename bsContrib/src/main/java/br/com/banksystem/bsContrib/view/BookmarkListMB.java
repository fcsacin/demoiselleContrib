package br.com.banksystem.bsContrib.view;

import javax.inject.Inject;
import br.com.banksystem.bsContrib.business.BookmarkBC;
import br.com.banksystem.bsContrib.business.filtro.FiltroBookmark;
import br.com.banksystem.bsContrib.domain.Bookmark;
import br.com.banksystem.bsContrib.view.template.GenericListPageBean;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;

@ViewController
@NextView("/bookmark_edit.xhtml")
@PreviousView("/bookmark_list.xhtml")
public class BookmarkListMB extends GenericListPageBean<Bookmark, Long, FiltroBookmark> {

	private static final long serialVersionUID = 1L;

	@Inject
	private BookmarkBC bc;

	@Override
	protected BookmarkBC getBC() {
		return bc;
	}

}
