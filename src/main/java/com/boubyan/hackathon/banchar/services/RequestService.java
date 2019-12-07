package com.boubyan.hackathon.banchar.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boubyan.hackathon.banchar.dtos.HistoricalPickUpRequest;
import com.boubyan.hackathon.banchar.dtos.InitiatedRequestDTO;
import com.boubyan.hackathon.banchar.dtos.WinchDetailsDTO;
import com.boubyan.hackathon.banchar.dtos.WinchReqDTO;


@Service
public class RequestService {

	@Autowired	
	EntityManager entityManager;
	
	@Autowired
	WinchService winchService;
	
	@Transactional
	public Long initiateServiceRequest(WinchReqDTO winchReq) { //for customer
		
		String reqInsertQuery = "INSERT INTO service_request (requester_id, customer_lon, customer_lat, details) VALUES"
				+ "(" + winchReq.getId() + ", '" + winchReq.getLon() + "' , '"   + winchReq.getLat() +"' , '" + winchReq.getDetails() + "')";
		Query q = entityManager.createNativeQuery(reqInsertQuery);
		
		System.err.println("INSERT QUERY : " + reqInsertQuery);
		entityManager.joinTransaction();
		int res = q.executeUpdate();
		
		String getReqIdQuery = "SELECT id, details FROM service_request WHERE requester_id = " + winchReq.getId() + " AND accepted  = 0 AND user_canceled = 0 AND winch_canceled =0";
		Query getId = entityManager.createNativeQuery(getReqIdQuery);
		
		
		WinchDetailsDTO winch = winchService.getNearestWinch(winchReq.getLat(), winchReq.getLon());
		
		
		
		System.err.println(getReqIdQuery);
		List<Object[]> result = getId.getResultList();
		Long reqId = null;
		for(Object[] row : result) {
			reqId =  Long.parseLong(row[0].toString());
		}
		
		String assignToNearestWinchString = "UPDATE service_request SET winch_id = " + winch.getId() + " WHERE id  = " + reqId;
		Query assignQuery = entityManager.createNativeQuery(assignToNearestWinchString);
		assignQuery.executeUpdate();
		
		return reqId;
	}
	
	
	@Transactional
	public int acceptRequest(Long reqId) {
		Query q = entityManager.createNativeQuery("UPDATE service_request SET accepted = 1 WHERE id = " + reqId.toString());
		
		
		entityManager.joinTransaction();
		int res = q.executeUpdate();
		return res;
	}

	
	
	@Transactional
	public int cancelRequest(Long reqId) {
		String queryString = "UPDATE service_request SET user_canceled = 1 WHERE id = " + reqId.toString() + " AND accepted =0";
		Query q = entityManager.createNativeQuery(queryString);
		System.err.println(queryString);
		entityManager.joinTransaction();
		int res = q.executeUpdate();
		return res;
	}

	
	@Transactional
	public int declineRequest(Long reqId) {
		Query q = entityManager.createNativeQuery("UPDATE service_request SET winch_canceled = 1 WHERE id = " + reqId.toString());
		
		entityManager.joinTransaction();
		int res = q.executeUpdate();
		
		//allocate to second nearest 
		WinchDetailsDTO winch = winchService.getSecondNearestWinch();
		String assignToNearestWinchString = "UPDATE service_request SET winch_id = " + winch.getId() + " WHERE id  = " + reqId;
		Query assignQuery = entityManager.createNativeQuery(assignToNearestWinchString);
		assignQuery.executeUpdate();
		return res;
	}
	
	
	@Transactional
	public int isMyRequestAccepted(Long reqId) {
		Query q = entityManager.createNativeQuery("SELECT accepted FROM service_request WHERE id =  " + reqId.toString() + " AND accepted = 1");
		List<Integer> result = q.getResultList();
		for(Integer accepted : result) {
			return accepted;
		}
		return 0;
	}
	
	@Transactional
	public int isMyTripComplete(Long reqId) {
		Query q = entityManager.createNativeQuery("SELECT completed FROM service_request WHERE id =  " + reqId.toString() + " AND completed = 1");
		List<Integer> result = q.getResultList();
		for(Integer accepted : result) {
			return accepted;
		}
		return 0;
	}
	
	public InitiatedRequestDTO areThereAnyRequestsForMeToService(Long winchId) {
		InitiatedRequestDTO reqDTO = new InitiatedRequestDTO();
		String queryString = "SELECT service_request.id, name, customer_lat, customer_lon, details, users.rating"
				+ " FROM service_request JOIN users ON service_request.requester_id = users.id WHERE winch_id =  " + winchId.toString() +""
				+ " AND user_canceled = 0 AND accepted = 0 ORDER BY service_request.id ASC";
		Query q = entityManager.createNativeQuery(queryString);
		System.err.println(queryString);
		List<Object[]> result = q.getResultList();
		for(Object[] row : result) {
			reqDTO.setCustomerLat(row[2].toString());
			reqDTO.setCustomerLon(row[3].toString());
			reqDTO.setReqDetails(row[4].toString());
			reqDTO.setCustomerName(row[1].toString());
			reqDTO.setCustomerRating(Double.parseDouble(row[5].toString()));
			reqDTO.setId(Long.parseLong(row[0].toString()));
		}
		
		return reqDTO;
	}
	
	public List<HistoricalPickUpRequest> getHistoryRequests(Long userId){
		List<HistoricalPickUpRequest> list = new ArrayList<HistoricalPickUpRequest>();
		String queryString = "SELECT users.name, accepted, user_canceled, winch_canceled, completed, license_plate, cost , payment FROM "
				+ "service_request JOIN users ON winch_id = users.id WHERE service_request.requester_id =" + userId.toString();
		Query q = entityManager.createNativeQuery(queryString);
	
		List<Object[]> result = q.getResultList();
		for(Object[] row : result) {
			HistoricalPickUpRequest req = new HistoricalPickUpRequest();
			req.setDriverName(row[0].toString());
			req.setLicensePlate(row[5].toString());
			req.setCost(row[6].toString());
			int accepted = Integer.parseInt(row[1].toString());
			int user_canceled = Integer.parseInt(row[2].toString());
			int winch_canceled = Integer.parseInt(row[3].toString());	
			
			if(accepted == 1) {
				req.setStatus("Accepted");
			} else if (user_canceled == 1) {
				req.setStatus("User Canceled");
			} else if (winch_canceled == 1) {
				req.setStatus("Winch Canceled");
			} else 
				req.setStatus("Completed");
			
			
			
			list.add(req);
		}
		System.err.println(queryString);
		return list;
	}
	
	
	@Transactional
	public boolean completeTrip(Long reqId) {
		Double cost = Math.random() *10;
		String queryString = "UPDATE service_request SET completed = 1 , cost = " + cost.toString() + " WHERE id = " + reqId.toString();
		Query q = entityManager.createNativeQuery(queryString);
		
		System.err.println(queryString);
		entityManager.joinTransaction();
		int res = q.executeUpdate();
		return res ==1;
	}
	
	@Transactional
	public boolean ratePayWinch(Long reqId, Double rating, String ratingText, Integer payment) {
		String queryString = "UPDATE service_request SET rating_for_winch = " +rating + ", rating_text = '" +ratingText + "' , "
				+ "payment = " +  payment.toString()  + " WHERE id = " + reqId.toString();
		Query q = entityManager.createNativeQuery(queryString);
		
		System.err.println(queryString);
		entityManager.joinTransaction();
		int res = q.executeUpdate();
		
		
		String getWinchId = "SELECT winch_id FROM service_request WHERE id = " + reqId.toString();
		System.err.println(queryString);
		entityManager.joinTransaction();
		Query q1 =   entityManager.createNativeQuery(getWinchId);
		List<BigInteger> id = q1.getResultList();
		Long winchId = id.get(0).longValue();
		
		
		String ratings = "SELECT rating_for_winch FROM service_request WHERE completed = 1 AND rating_for_winch IS NOT NULL   AND winch_id = " + winchId.toString();
		System.err.println(ratings);
		entityManager.joinTransaction();
		Query q2 =   entityManager.createNativeQuery(ratings);
		List<BigDecimal> allRatings = q2.getResultList();
		
		Double sum = 0d ; 
		for(int i =0 ; i< allRatings.size() ; i++) {
			sum += allRatings.get(i).doubleValue();
		}
		
		Double avg = sum/allRatings.size();
		
		String updateRating = "UPDATE users SET rating = " + avg.toString() + " , numberofreviews = " + allRatings.size() +" WHERE id = " + winchId.toString();
		Query q3 = entityManager.createNativeQuery(updateRating);
		
		System.err.println(updateRating);
		entityManager.joinTransaction();
		int res3 = q3.executeUpdate();
		
		
		
		
		return res3 ==1;
	}
	
}
