/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.processor.test.usertype;

import org.hibernate.processor.test.util.CompilationTest;
import org.hibernate.processor.test.util.TestForIssue;
import org.hibernate.processor.test.util.WithClasses;
import org.junit.jupiter.api.Test;

import static org.hibernate.processor.test.util.TestUtil.assertMetamodelClassGeneratedFor;
import static org.hibernate.processor.test.util.TestUtil.assertPresenceOfFieldInMetamodelFor;

/**
 * @author Hardy Ferentschik
 */
@CompilationTest
@TestForIssue(jiraKey = "METAGEN-28")
class UserTypeTest {
	@Test
	@WithClasses({ ContactDetails.class, PhoneNumber.class })
	void testCustomUserTypeInMetaModel() {
		assertMetamodelClassGeneratedFor( ContactDetails.class );
		assertPresenceOfFieldInMetamodelFor(
				ContactDetails.class, "phoneNumber", "@Type annotated field should be in metamodel"
		);
	}
}
