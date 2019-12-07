package com.boubyan.hackathon.banchar.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boubyan.hackathon.banchar.dtos.ExampleDTO;
import com.boubyan.hackathon.banchar.entities.User;
import com.boubyan.hackathon.banchar.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired	
	EntityManager entityManager;
	
	public User getUserById(Long id) {
		Optional<User> result =  userRepository.findById(id);
		if(!result.isPresent()) {
			return null;
		}
		
		return result.get();
	}
	
	public List<User> getAllUsers() {
		Iterable<User> result =  userRepository.findAll();
		List<User> users = new ArrayList<User>();
		result.forEach(users::add);
		return users;
	}
	
	public User customSelect(Long id, String fname) {
		User res = userRepository.getBellyAnaAyzo(id, fname);
		return res;
	}
	
	public Boolean saveNewUser (User newUser) {
		userRepository.save(newUser);
		return true;
	}
	
	public ExampleDTO getExampleDTO() {
		ExampleDTO exDTO = new ExampleDTO();
		Query q = entityManager.createNativeQuery("Select * from user where id =1");
		List<Object[]> res = q.getResultList();
		for(Object[] row : res) {
			exDTO.setFname((String) row[1]);
			exDTO.setPassword((String)row[4]);
		}
		return exDTO;
	}
	
	@Transactional
	public int addDetailsToDriver(String licensePlate, Long id) {
		String queryString = "INSERT INTO driver_details (driver_id, license_plate) VALUES (" +id.toString() + " , " +licensePlate+")";
		Query q = entityManager.createNativeQuery(queryString);
		
		System.err.println("INSERT QUERY : " + queryString);
		entityManager.joinTransaction();
		
		return q.executeUpdate();
	}
	
}
