package br.com.banksystem.bsContrib.domain;

import static javax.persistence.GenerationType.SEQUENCE;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bookmark implements Serializable, IPojo<Long> {

	private static final long serialVersionUID = 1L;

	/*
	 * If you are using Glassfish then remove the strategy attribute
	 */
	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@Column
	private String description;

	@Column
	private String link;

	public Bookmark() {
		super();
	}

	public Bookmark(String description, String link) {
		this.description = description;
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
