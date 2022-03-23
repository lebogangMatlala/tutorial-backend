package za.co.crud.tutorial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import za.co.crud.tutorial.model.Tutorial;

public interface TutorialRespository extends MongoRepository<Tutorial, String>{

	List<Tutorial> findByTitleContaining(String title);
	List<Tutorial> findByPublished(boolean published);
	Optional<Tutorial> findById(String id);
}
