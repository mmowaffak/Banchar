package com.boubyan.hackathon.banchar.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boubyan.hackathon.banchar.dtos.LocationDTO;
import com.boubyan.hackathon.banchar.dtos.WinchDetailsDTO;


@Service
public class WinchService {

	@Autowired	
	EntityManager entityManager;
	
	public List<WinchDetailsDTO> getAllWinches(){
		List<WinchDetailsDTO> allWinches = new ArrayList<WinchDetailsDTO>();
		Query q = entityManager.createNativeQuery("Select lat,lon,name,driver_details.license_plate , driver_id from users join driver_details on users.id = driver_details.driver_id where type =1");
		List<Object[]> res = q.getResultList();
		for(Object[] row : res) {
			WinchDetailsDTO newDetail = new WinchDetailsDTO();
			newDetail.setLat(row[0].toString());
			newDetail.setLon(row[1].toString());
			newDetail.setName(row[2].toString());
			newDetail.setlPlate(row[3].toString());
			newDetail.setId(Long.parseLong(row[4].toString()));
			allWinches.add(newDetail);
		}
		return allWinches;	
	}
	
	public LocationDTO whereIsWinch(Long winchId) {
		LocationDTO loc = new LocationDTO();
		Query q =  entityManager.createNativeQuery("SELECT lat, lon from driver_details WHERE driver_id = " + winchId.toString());
		List<Object[]> res = q.getResultList();
		for(Object[] row : res) {
			loc.setLat(row[0].toString());
			loc.setLon(row[1].toString());
		}
		
		return loc;
		
	}
	
	
	public WinchDetailsDTO whoIsMyWinch(Long reqId) {
		//need to get winch id 
		String getWinchId = "SELECT winch_id from service_request WHERE id = "+ reqId.toString();
		Query getWId = entityManager.createNativeQuery(getWinchId);
		List<BigInteger> winchIds = getWId.getResultList();
		Long winchId = winchIds.get(0).longValue();
		WinchDetailsDTO winchDetails = new WinchDetailsDTO();
		String getWinchDetails = "SELECT  name, driver_details.license_plate, lat , lon FROM driver_details JOIN users ON driver_details.driver_id = users.id WHERE driver_id = "+winchId.toString();
		Query getWinchDetailsQuery = entityManager.createNativeQuery(getWinchDetails);
		List<Object[]> result = getWinchDetailsQuery.getResultList();
		for(Object[] row : result) {
			winchDetails.setId(winchId);
			winchDetails.setLat(row[2].toString());
			winchDetails.setLon(row[3].toString());
			winchDetails.setName(row[0].toString());
			winchDetails.setlPlate(row[1].toString());
		}
		
		return winchDetails;
	}
	
	public WinchDetailsDTO getNearestWinch(String lat, String lon) {
		List<WinchDetailsDTO> allWinches = getAllWinches();
		List<Double> distances = new ArrayList<Double>();
		for(WinchDetailsDTO detail : allWinches) {
			distances.add(distance(Double.parseDouble(lat), Double.parseDouble(lon), 
					Double.parseDouble(detail.getLat()), Double.parseDouble(detail.getLon())));
		}
		
		int minIndex = getIndexForMinDistance(distances);
		return allWinches.get(minIndex);
	}
	
	public WinchDetailsDTO getSecondNearestWinch() {
		return getAllWinches().get(1);
	}
	
	private int getIndexForMinDistance(List<Double> distances) {
		double minValue = 1000000000 ; 
		int minIndex = -1;
		
		for(int i =0; i<distances.size() ; i++) {
			if(distances.get(i)< minValue) {
				minIndex = i;
				minValue = distances.get(i);
			}
		}
		
		return minIndex;
	}
	
	private double distance(double lat1, double lng1, double lat2, double lng2) {

	    double earthRadius = 6371; // in miles, change to 6371 for kilometer output

	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);

	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);

	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	        * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

	    double dist = earthRadius * c;

	    return dist; // output distance, in MILES
	}
	
	
	
}
