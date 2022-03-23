package za.co.crud.tutorial.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;
import za.co.crud.tutorial.model.Tutorial;
import za.co.crud.tutorial.repository.TutorialRespository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@Log
public class TutorialController {
	@Autowired
	TutorialRespository tutorialRespository;

	
	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {

		log.info("This is my data:"+tutorial.getTitle()+" "+tutorial.getDescription());
		try {
			Tutorial _tutorial = tutorialRespository
					.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);

		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
		try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();
			if (title == null)
				tutorialRespository.findAll().forEach(tutorials::add);
			else
				tutorialRespository.findByTitleContaining(title).forEach(tutorials::add);
			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);

		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") String id) {
		Optional<Tutorial> tutorialData = tutorialRespository.findById(id);
		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> findByPublished() {
		try {
			List<Tutorial> tutorials = tutorialRespository.findByPublished(true);
			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") String id, @RequestBody Tutorial tutorial) {
		Optional<Tutorial> tutorialData = tutorialRespository.findById(id);
		if (tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDescription(tutorial.getDescription());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRespository.save(_tutorial), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") String id) {
		try {
			tutorialRespository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		try {
			tutorialRespository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
