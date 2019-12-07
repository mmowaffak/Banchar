package com.boubyan.hackathon.banchar.restcontrollers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boubyan.hackathon.banchar.dtos.ExampleDTO;
import com.boubyan.hackathon.banchar.entities.User;
import com.boubyan.hackathon.banchar.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping(value = "/rest", path = "/rest") 
public class UsersRESTController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	 @GetMapping("/getUser")
	    public ResponseEntity<User> getUser(@RequestParam(value="id", defaultValue="1") Long id) {
	        return new ResponseEntity<User>(userService.getUserById(id),HttpStatus.OK);
	    }
	 
	 @GetMapping("/getAllUsers")
	    public ResponseEntity<List<User>> getAllUsers() {
	        return new ResponseEntity<List<User>>(userService.getAllUsers(),HttpStatus.OK);
	    }
	 
	 @GetMapping("/getCustom")
	    public ResponseEntity<User> getCustom(@RequestParam(value="id")Long id,
	    		@RequestParam(value="fname")String fname) {
	        return new ResponseEntity<User>(userService.customSelect(id, fname),HttpStatus.OK);

	 }
	 
	 @GetMapping("/getCustomBodyParams")
	    public ResponseEntity<String> getCustomBodyParams(@RequestBody String body) {
		 	try {
		 		Map<String, String> result = objectMapper.readValue(body,Map.class);
		 		System.err.println(result.get("body"));
		 		
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return new ResponseEntity<String>("Hello", HttpStatus.OK);
	 }
	 
	 @PostMapping("/saveNewUser")
	    public ResponseEntity<Boolean> saveUser(@RequestBody User user) {
		 	userService.saveNewUser(user);
		 	System.err.println("ssdsdsd");
	        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	 }
	 
	 @GetMapping("/getExampleDTO")
	 	public ResponseEntity<ExampleDTO> getExampleDTO(){
		 return new ResponseEntity<ExampleDTO>(userService.getExampleDTO(), HttpStatus.OK);
	 }
	 
	 
}
