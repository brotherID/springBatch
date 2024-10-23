package org.file.grain.enrichment.repository;

import org.file.grain.enrichment.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

	boolean existsByEmail(String email);

}
