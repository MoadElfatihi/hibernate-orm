/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.mapping.identifier.composite;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.Jira;
import org.hibernate.testing.orm.junit.ServiceRegistry;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.Setting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This test Fails
 */
@DomainModel(
		annotatedClasses = {
				CompositeInheritanceFailTest.TupAbstractEntity.class,
				CompositeInheritanceFailTest.DummyEntity.class,
				CompositeInheritanceFailTest.TestEntity.class, // Here the class is called TestEntity
				CompositeInheritanceFailTest.Test2Entity.class,
		}
)
@ServiceRegistry(
		settings = {
				// For your own convenience to see generated queries:
				@Setting(name = AvailableSettings.SHOW_SQL, value = "true"),
				@Setting(name = AvailableSettings.FORMAT_SQL, value = "true"),
				// @Setting( name = AvailableSettings.GENERATE_STATISTICS, value = "true" ),
		}
)
@SessionFactory
@Jira("HHH-19076")
public class CompositeInheritanceFailTest {

	@Test
	void hhh19076FailingTest(SessionFactoryScope scope) {
		scope.inTransaction( em -> {
			TestEntity e1 = new TestEntity("foo", "bar");
			em.persist(e1);

			CompositeIdClass key = e1.getCompositeId();
			TestEntity e2 = em.find(TestEntity.class, key);
			assertNotNull(e2);
		} );
	}

	@Test
	void hhh19076FailingTest2(SessionFactoryScope scope) {
		scope.inTransaction( em -> {
			Test2Entity e1 = new Test2Entity("foo", "xxxxxx");
			em.persist(e1);

			CompositeId2Class key = e1.getCompositeId();
			Test2Entity e2 = em.find(Test2Entity.class, key);
			assertNotNull(e2);
		} );
	}

	@MappedSuperclass
	public static abstract class TupAbstractEntity {
		@Id
		private String oid = null;


		@SuppressWarnings("this-escape")
		protected TupAbstractEntity() {
		}

		protected TupAbstractEntity(String oid) {
			this.oid = oid;
		}

		public String getOid() {
			return oid;
		}

	}

	@Entity
	public static class DummyEntity extends TupAbstractEntity {
	}

	@Entity
	@IdClass(CompositeIdClass.class)
	public static class TestEntity extends TupAbstractEntity {

		@Id
		private String myId;

		protected TestEntity() {
			// for JPA
		}

		public TestEntity(String oid, String myId) {
			super(oid);
			this.myId = myId;
		}

		public String myId() {
			return myId;
		}

		public CompositeIdClass getCompositeId() {
			return new CompositeIdClass(getOid(), myId);
		}

	}

	@Entity
	@IdClass(CompositeId2Class.class)
	public static class Test2Entity extends TupAbstractEntity {

		@Id
		private String otherId;

		@Version
		private long tanum = 0;

		protected Test2Entity() {
			// for JPA
		}

		public Test2Entity(String oid, String otherId) {
			super(oid);
			this.otherId = otherId;
		}

		public String myId() {
			return otherId;
		}

		public long tanum() {
			return tanum;
		}

		public CompositeId2Class getCompositeId() {
			return new CompositeId2Class(getOid(), otherId);
		}

	}

	public static class CompositeIdClass {

		private String oid;
		private String myId;

		public CompositeIdClass(String oid, String myId) {
			this.oid = oid;
			this.myId = myId;
		}

		public CompositeIdClass() {
		}

		public String oid() {
			return oid;
		}

		public String myId() {
			return myId;
		}

	}

	public static class CompositeId2Class {

		private String oid;
		private String otherId;

		public CompositeId2Class(String oid, String otherId) {
			this.oid = oid;
			this.otherId = otherId;
		}

		public CompositeId2Class() {
		}

		public String oid() {
			return oid;
		}

		public String otherId() {
			return otherId;
		}

	}

}
