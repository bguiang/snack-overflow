package com.bernardguiang.SnackOverflow.repository;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bernardguiang.SnackOverflow.model.Product;

@Repository //TODO: look into JPA specification
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>{//, JpaSpecificationExecutor<Product>{
	
	// JPA Supports Pagination with native queries by using countQuery but does not support execution of dynamic sorting for native queries
	// JPQL doesn't only supports SELECT inside WHEN and HAVING clauses so this isnt possible there either.
	// For now, just manually handle sorting with JpaSort.unsafe()
	// https://docs.spring.io/spring-data/jpa/docs/1.10.2.RELEASE/reference/html/#_native_queries
	@Query(
		value = 
		"SELECT "
		+ "*, "
		+ "(SELECT SUM(ORDER_ITEM.quantity) "
			+ "FROM ORDER_ITEM "
			+ "INNER JOIN ORDERS "
			+ "ON ORDER_ITEM.ORDER_ID = ORDERS.ID "
			+ "WHERE "
			+ "ORDER_ITEM.PRODUCT_ID = PRODUCT.ID "
			+ "AND (:start IS NULL OR ORDERS.CREATED_DATE > :start)"
			+ "AND ORDERS.STATUS != 'CANCELLED'"	// do not count units from cancelled, failed, or refunded orders
			+ "AND ORDERS.STATUS != 'FAILED'"
			+ "AND ORDERS.STATUS != 'REFUNDED'"
		+ ") AS UNITS_SOLD "
		+ "FROM PRODUCT "
		+ "WHERE UPPER(NAME) LIKE UPPER(CONCAT('%',:searchText,'%')) "
		+ "AND DELETED = FALSE"
		,countQuery = "SELECT count(*) FROM PRODUCT WHERE UPPER(NAME) LIKE UPPER(CONCAT('%',:searchText,'%')) AND DELETED = FALSE",
		nativeQuery = true
	)
	Page<Product> findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(@Param("searchText") String searchText, @Param("start")Instant start, Pageable pageable);
}
