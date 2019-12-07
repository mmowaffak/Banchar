package com.boubyan.hackathon.banchar.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.boubyan.hackathon.banchar.dtos.ExampleDTO;
import com.boubyan.hackathon.banchar.entities.User;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	@Query(value = "SELECT * FROM user WHERE id = :id AND fname = :fname", nativeQuery = true)
	public User getBellyAnaAyzo(Long id, String fname);
	
	
	@Query(value="SELECT fname, password FROM user where id = 1", nativeQuery = true)
	public ExampleDTO getDTO();
	
	Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
