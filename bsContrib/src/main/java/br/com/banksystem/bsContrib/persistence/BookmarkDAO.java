package br.com.banksystem.bsContrib.persistence;

import br.com.banksystem.bsContrib.domain.Bookmark;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;

@PersistenceController
public class BookmarkDAO extends GenericDAO<Bookmark, Long> {

	private static final long serialVersionUID = 1L;

}
