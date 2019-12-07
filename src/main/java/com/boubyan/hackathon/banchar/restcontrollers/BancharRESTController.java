package com.boubyan.hackathon.banchar.restcontrollers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boubyan.hackathon.banchar.dtos.HistoricalPickUpRequest;
import com.boubyan.hackathon.banchar.dtos.InitiatedRequestDTO;
import com.boubyan.hackathon.banchar.dtos.LocationDTO;
import com.boubyan.hackathon.banchar.dtos.WinchDetailsDTO;
import com.boubyan.hackathon.banchar.dtos.WinchReqDTO;
import com.boubyan.hackathon.banchar.entities.User;
import com.boubyan.hackathon.banchar.repositories.UserRepository;
import com.boubyan.hackathon.banchar.services.RequestService;
import com.boubyan.hackathon.banchar.services.WinchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/rest", path = "/rest") 
public class BancharRESTController {

	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	WinchService winchService;
	
	@Autowired
	RequestService requestService;
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/initiateRequest")
    public ResponseEntity<Long> initiateRequest(@RequestBody WinchReqDTO winchReq) {
	 	Long reqId = requestService.initiateServiceRequest(winchReq);
	 	return new ResponseEntity<Long>(reqId, HttpStatus.OK);
	}
	
	
	@PostMapping("/isRequestAccepted")
	public ResponseEntity<Boolean> isReqAccepted(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long reqId = Long.parseLong(result.get("reqId"));
	 	Boolean accepted = requestService.isMyRequestAccepted(reqId) == 1;
	 	return new ResponseEntity<Boolean>(accepted, HttpStatus.OK);
	}
	
	@PostMapping("/whoIsMyWinch")
	public ResponseEntity<WinchDetailsDTO> whoIsMyWinch(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long reqId = Long.parseLong(result.get("reqId"));
	 	return new ResponseEntity<WinchDetailsDTO>(winchService.whoIsMyWinch(reqId), HttpStatus.OK);
	}
	
	
	@PostMapping("/cancelRequest")
	public ResponseEntity<Integer> cancelRequest(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long reqId = Long.parseLong(result.get("reqId"));
	 	return new ResponseEntity<Integer>(requestService.cancelRequest(reqId),HttpStatus.OK);
	}
	
	@PostMapping("/declineRequest")
	public ResponseEntity<Integer> declineRequest(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long reqId = Long.parseLong(result.get("reqId"));
	 	return new ResponseEntity<Integer>(requestService.declineRequest(reqId),HttpStatus.OK);
	}
	
	@PostMapping("/anyRequestsAvailableToService")
	public ResponseEntity<InitiatedRequestDTO> anyRequestsAvailableToService(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long winchId = Long.parseLong(result.get("winchId"));
	 	return new ResponseEntity<InitiatedRequestDTO>(requestService.areThereAnyRequestsForMeToService(winchId),HttpStatus.OK);
	}
	
	@PostMapping("/acceptRequest")
	public ResponseEntity<Integer> acceptRequest(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long reqId = Long.parseLong(result.get("reqId"));
	 	return new ResponseEntity<Integer>(requestService.acceptRequest(reqId),HttpStatus.OK);
	}
	
	@PostMapping("/getHistoricalRequest")
	public ResponseEntity<List<HistoricalPickUpRequest>> getHistoricalRequest(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long userId = Long.parseLong(result.get("userId"));
	 	return new ResponseEntity<List<HistoricalPickUpRequest>>(requestService.getHistoryRequests(userId),HttpStatus.OK);
	}
	
	@PostMapping("/completeTrip")
	public ResponseEntity<Boolean> complete(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long reqId = Long.parseLong(result.get("reqId"));
	 	return new ResponseEntity<Boolean>(requestService.completeTrip(reqId),HttpStatus.OK);
	}
	
	
	@PostMapping("/whereIsMyWinch")
	public ResponseEntity<LocationDTO> whereIsMyWinch(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long winchId = Long.parseLong(result.get("winchId"));
	 	return new ResponseEntity<LocationDTO>(winchService.whereIsWinch(winchId),HttpStatus.OK);
	}
	
	@PostMapping("/ratePayWinch")
	public ResponseEntity<Boolean> ratePayWinch(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long reqId = Long.parseLong(result.get("reqId"));
	 	Double rating = Double.parseDouble(result.get("rating"));
	 	String ratingText = result.get("ratingText");
	 	int creditOrCash = Integer.parseInt(result.get("payment"));
	 	return new ResponseEntity<Boolean>(requestService.ratePayWinch(reqId, rating, ratingText, creditOrCash),HttpStatus.OK);
	}
	
	@PostMapping("/isMyTripComplete")
	public ResponseEntity<Boolean> isTripComplete(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long reqId = Long.parseLong(result.get("reqId"));
	 	Boolean completed = requestService.isMyTripComplete(reqId) == 1;
	 	return new ResponseEntity<Boolean>(completed, HttpStatus.OK);
	}
	
	@PostMapping("/getUserInfo")
	public ResponseEntity<User> getUserInfo(@RequestBody String body) {
		Map<String, String> result = new HashMap<String,String>();
		try {
			result = objectMapper.readValue(body,Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Long userId = Long.parseLong(result.get("userId"));
	 	User user = userRepository.findById(userId).get();
	 	return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
}
