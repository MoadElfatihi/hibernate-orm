/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.jpa.cascade.multicircle;

/**
 * No Documentation
 */
@jakarta.persistence.Entity
public class F extends AbstractEntity {
	private static final long serialVersionUID = 1471534025L;

	/**
	 * No documentation
	 */
	@jakarta.persistence.OneToMany(cascade =  {
		jakarta.persistence.CascadeType.MERGE, jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.REFRESH}
	, mappedBy = "f")
	private java.util.Set<E> eCollection = new java.util.HashSet<E>();

	@jakarta.persistence.ManyToOne(optional = false)
	private D d;

	@jakarta.persistence.ManyToOne(optional = false)
	private G g;

	public java.util.Set<E> getECollection() {
		return eCollection;
	}
	public void setECollection(
		java.util.Set<E> parameter) {
		this.eCollection = parameter;
	}

	public D getD() {
		return d;
	}
	public void setD(D parameter) {
		this.d = parameter;
	}

	public G getG() {
		return g;
	}
	public void setG(G parameter) {
		this.g = parameter;
	}

}
