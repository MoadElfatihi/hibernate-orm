/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.processor.test.separatecompilationunits;

import org.hibernate.processor.test.separatecompilationunits.superclass.MappedSuperclass;
import org.hibernate.processor.test.util.CompilationTest;
import org.hibernate.processor.test.util.IgnoreCompilationErrors;
import org.hibernate.processor.test.util.TestForIssue;
import org.hibernate.processor.test.util.WithClasses;
import org.junit.jupiter.api.Test;

import static org.hibernate.processor.test.util.TestUtil.getMetaModelSourceAsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@CompilationTest
@TestForIssue(jiraKey = "METAGEN-35")
class SeparateCompilationUnitsTest {
	@Test
	@WithClasses(value = Entity.class, preCompile = MappedSuperclass.class)
	@IgnoreCompilationErrors
	void testInheritance() throws Exception {
		// need to work with the source file. Entity_.class won't get generated, because the mapped superclass
		// will not be on the classpath
		String entityMetaModel = getMetaModelSourceAsString( Entity.class );
		assertTrue(
				entityMetaModel.contains(
						"import org.hibernate.processor.test.separatecompilationunits.superclass.MappedSuperclass_;"
				)
		);
		assertTrue(
				entityMetaModel.contains(
						"extends MappedSuperclass_"
				)
		);
	}
}
