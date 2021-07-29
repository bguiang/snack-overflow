package com.bernardguiang.SnackOverflow.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.model.Product;

@Service
public class ProductIndexingService {
	
	private final EntityManager entityManager;
	
	@Autowired
	public ProductIndexingService(EntityManagerFactory entityManagerFactory) {
		//this.entityManager = entityManager;
		this.entityManager = entityManagerFactory.createEntityManager();
	}

	// call this at startup
	@PostConstruct
	public void startIndexing() throws InterruptedException {
		System.out.println("CONTEXT STARTED");
		SearchSession searchSession = Search.session( entityManager );
		MassIndexer indexer = searchSession.massIndexer( Product.class ) 
		        .threadsToLoadObjects( 7 );
		indexer.startAndWait();
	}
	
	public List<ProductDTO> searchProducts(String search){
		// Not shown: get the entity manager and open a transaction
		SearchSession searchSession = Search.session( entityManager ); 

		SearchResult<Product> result = searchSession.search( Product.class ) 
		        .where( f -> f.match() 
		                .fields( "name", "description" )
		                .matching( search ) )
		        .fetch( 20 ); 

		long totalHitCount = result.total().hitCount(); 
		List<Product> hits = result.hits();  
		// Not shown: commit the transaction and close the entity manager
		
		List<ProductDTO> results = new ArrayList<>();
		for(Product p : hits) {
			results.add(new ProductDTO(p));
		}
		
		return results;
	  }
}
