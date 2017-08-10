package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Sphere;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sphere entity.
 */
public interface SphereSearchRepository extends ElasticsearchRepository<Sphere, Long> {
}
