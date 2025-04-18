/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.engine.jdbc.spi;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.hibernate.ScrollMode;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Interface to the object that prepares JDBC {@link Statement}s and {@link PreparedStatement}s
 * on behalf of a {@link JdbcCoordinator}.
 *
 * @author Steve Ebersole
 * @author Brett Meyer
 *
 * @see JdbcCoordinator#getStatementPreparer()
 */
public interface StatementPreparer {
	/**
	 * Create a statement.
	 *
	 * @return the statement
	 */
	Statement createStatement();

	/**
	 * Prepare a statement.
	 *
	 * @param sql The SQL the statement to be prepared
	 *
	 * @return the prepared statement
	 */
	PreparedStatement prepareStatement(String sql);

	/**
	 * Prepare a statement.
	 *
	 * @param sql The SQL the statement to be prepared
	 *
	 * @return the prepared statement
	 *
	 * @since 7.0
	 */
	CallableStatement prepareCallableStatement(String sql);

	/**
	 * Prepare a statement.
	 *
	 * @param sql The SQL the statement to be prepared
	 * @param isCallable Whether to prepare as a callable statement.
	 *
	 * @return the prepared statement
	 */
	PreparedStatement prepareStatement(String sql, boolean isCallable);

	/**
	 * Prepare an INSERT statement, specifying how auto-generated (by the database) keys should be handled.  Really this
	 * is a boolean, but JDBC opted to define it instead using 2 int constants:
	 * <ul>
	 *     <li>{@link PreparedStatement#RETURN_GENERATED_KEYS}</li>
	 *     <li>{@link PreparedStatement#NO_GENERATED_KEYS}</li>
	 * </ul>
	 * <p>
	 * Generated keys are accessed afterwards via {@link PreparedStatement#getGeneratedKeys}
	 *
	 * @param sql The INSERT SQL
	 * @param autoGeneratedKeys The autoGeneratedKeys flag
	 *
	 * @return the prepared statement
	 *
	 * @see java.sql.Connection#prepareStatement(String, int)
	 */
	PreparedStatement prepareStatement(String sql, int autoGeneratedKeys);

	/**
	 * Prepare an INSERT statement, specifying columns which are auto-generated values to be returned.
	 * Generated keys are accessed afterwards via {@link PreparedStatement#getGeneratedKeys}
	 *
	 * @param sql - the SQL for the statement to be prepared
	 * @param columnNames The name of the columns to be returned in the generated keys result set.
	 *
	 * @return the prepared statement
	 *
	 * @see java.sql.Connection#prepareStatement(String, String[])
	 */
	PreparedStatement prepareStatement(String sql, String[] columnNames);

	/**
	 * Get a prepared statement for use in loading / querying.
	 *
	 * @param sql The SQL the statement to be prepared
	 * @param isCallable Whether to prepare as a callable statement.
	 * @param scrollMode (optional) scroll mode to be applied to the resulting result set; may be null to indicate
	 * no scrolling should be applied.
	 *
	 * @return the prepared statement
	 */
	PreparedStatement prepareQueryStatement(String sql, boolean isCallable, @Nullable ScrollMode scrollMode);
}
