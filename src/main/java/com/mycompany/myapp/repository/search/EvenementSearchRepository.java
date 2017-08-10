package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Evenement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Evenement entity.
 */
public interface EvenementSearchRepository extends ElasticsearchRepository<Evenement, Long> {
}
