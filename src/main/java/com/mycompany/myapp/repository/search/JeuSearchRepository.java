package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Jeu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Jeu entity.
 */
public interface JeuSearchRepository extends ElasticsearchRepository<Jeu, Long> {
}
