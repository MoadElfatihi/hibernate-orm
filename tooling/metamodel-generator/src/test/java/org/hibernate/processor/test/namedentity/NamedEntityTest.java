/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.processor.test.namedentity;

import jakarta.persistence.TypedQueryReference;
import org.hibernate.processor.test.util.CompilationTest;
import org.hibernate.processor.test.util.WithClasses;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.hibernate.processor.test.util.TestUtil.assertMetamodelClassGeneratedFor;
import static org.hibernate.processor.test.util.TestUtil.assertPresenceOfFieldInMetamodelFor;
import static org.hibernate.processor.test.util.TestUtil.getFieldFromMetamodelFor;
import static org.hibernate.processor.test.util.TestUtil.getMetaModelSourceAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@CompilationTest
class NamedEntityTest {

	@Test
	@WithClasses(Book.class)
	void test() {
		System.out.println( getMetaModelSourceAsString( Book.class ) );

		assertMetamodelClassGeneratedFor( Book.class );

		assertPresenceOfFieldInMetamodelFor( Book.class, "QUERY_FIND_ALL_BOOKS" );
		final Field field = getFieldFromMetamodelFor( Book.class, "_findAllBooks_" );
		assertEquals( TypedQueryReference.class, field.getType() );
		final Type genericType = field.getGenericType();
		assertInstanceOf( ParameterizedType.class, genericType );
		final ParameterizedType parameterizedType = (ParameterizedType) genericType;
		assertEquals( Book.class, parameterizedType.getActualTypeArguments()[0] );
	}
}
