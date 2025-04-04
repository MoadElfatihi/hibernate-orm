/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.query.results.internal.complete;

import java.util.List;

import org.hibernate.engine.FetchTiming;
import org.hibernate.metamodel.mapping.SelectableMapping;
import org.hibernate.metamodel.mapping.ValuedModelPart;
import org.hibernate.query.results.internal.ResultsHelper;
import org.hibernate.spi.NavigablePath;
import org.hibernate.query.results.internal.DomainResultCreationStateImpl;
import org.hibernate.query.results.FetchBuilder;
import org.hibernate.sql.ast.tree.from.TableGroup;
import org.hibernate.sql.results.graph.DomainResultCreationState;
import org.hibernate.sql.results.graph.Fetch;
import org.hibernate.sql.results.graph.FetchParent;
import org.hibernate.sql.results.graph.entity.EntityValuedFetchable;
import org.hibernate.sql.results.jdbc.spi.JdbcValuesMetadata;

import static org.hibernate.query.results.internal.ResultsHelper.impl;

/**
 * CompleteFetchBuilder for entity-valued ModelParts
 *
 * @author Christian Beikov
 */
public class CompleteFetchBuilderEntityValuedModelPart
		implements CompleteFetchBuilder, ModelPartReferenceEntity {
	private final NavigablePath navigablePath;
	private final EntityValuedFetchable modelPart;
	private final List<String> columnAliases;

	public CompleteFetchBuilderEntityValuedModelPart(
			NavigablePath navigablePath,
			EntityValuedFetchable modelPart,
			List<String> columnAliases) {
		this.navigablePath = navigablePath;
		this.modelPart = modelPart;
		this.columnAliases = columnAliases;
	}

	@Override
	public FetchBuilder cacheKeyInstance() {
		return new CompleteFetchBuilderEntityValuedModelPart(
				navigablePath,
				modelPart,
				List.copyOf( columnAliases )
		);
	}

	@Override
	public NavigablePath getNavigablePath() {
		return navigablePath;
	}

	@Override
	public EntityValuedFetchable getReferencedPart() {
		return modelPart;
	}

	@Override
	public List<String> getColumnAliases() {
		return columnAliases;
	}

	@Override
	public Fetch buildFetch(
			FetchParent parent,
			NavigablePath fetchPath,
			JdbcValuesMetadata jdbcResultsMetadata,
			DomainResultCreationState domainResultCreationState) {
		assert fetchPath.equals( navigablePath );
		final DomainResultCreationStateImpl creationStateImpl = impl( domainResultCreationState );
		final TableGroup tableGroup = creationStateImpl.getFromClauseAccess().getTableGroup( navigablePath.getParent() );
		modelPart.forEachSelectable(
				(selectionIndex, selectableMapping) ->
						sqlSelection( jdbcResultsMetadata, selectionIndex, selectableMapping, creationStateImpl, tableGroup )
		);
		return parent.generateFetchableFetch(
				modelPart,
				fetchPath,
				FetchTiming.DELAYED,
				true,
				null,
				domainResultCreationState
		);
	}

	private void sqlSelection(
			JdbcValuesMetadata jdbcResultsMetadata,
			int selectionIndex,
			SelectableMapping selectableMapping,
			DomainResultCreationStateImpl creationStateImpl,
			TableGroup tableGroup) {
		creationStateImpl.resolveSqlSelection(
				ResultsHelper.resolveSqlExpression(
						creationStateImpl,
						jdbcResultsMetadata,
						tableGroup.resolveTableReference( navigablePath, (ValuedModelPart) modelPart,
								selectableMapping.getContainingTableExpression() ),
						selectableMapping,
						columnAliases.get( selectionIndex )
				),
				selectableMapping.getJdbcMapping().getJdbcJavaType(),
				null,
				creationStateImpl.getSessionFactory().getTypeConfiguration()
		);
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		final CompleteFetchBuilderEntityValuedModelPart that = (CompleteFetchBuilderEntityValuedModelPart) o;
		return navigablePath.equals( that.navigablePath )
			&& modelPart.equals( that.modelPart )
			&& columnAliases.equals( that.columnAliases );
	}

	@Override
	public int hashCode() {
		int result = navigablePath.hashCode();
		result = 31 * result + modelPart.hashCode();
		result = 31 * result + columnAliases.hashCode();
		return result;
	}
}
