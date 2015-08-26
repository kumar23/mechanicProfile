package com.drivojoy.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drivojoy.documents.Request;
import com.drivojoy.service.IMechanicService;

/**
 * @author Kumar
 * This class acts as the controller for the app.
 * It accepts URL requests from the view
 *
 */
@Controller
public class AppController {
	
	@Autowired
	private IMechanicService mechanicService;

	private final static Logger logger = Logger.getLogger(AppController.class);
	
	
	@RequestMapping("/")
	public String redirectToHome(Model model){
		logger.debug("Loading home...");
		model.addAttribute("mechanicCount", mechanicService.getMechanicCount());
		return "home";
	}
	
	//AJAX Callback functions from down here.
	@RequestMapping(value="/submitRequest", method=RequestMethod.POST)
	public @ResponseBody String submitRequest(@RequestBody(required=true) Request request) throws IOException{
		logger.debug(request.toString());
		String result = "SLOT_UNAVAILABLE";
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dateInString = request.getDate();
		Date requestDate=null;
		try {
			requestDate = formatter.parse(dateInString);
		} catch (ParseException e) {
			logger.debug("Date format error...");
		}
		if(!mechanicService.isBookingPossible(requestDate, request.getSlot())){
			return result;
		}else{
			result = mechanicService.generateMechanicProfile(request.getLocation(), request.getCustomerCoordinates(), requestDate
					, request.getSlot(), request.getServiceType(), request.getBikeModel());
			logger.debug("Nearest Available mechanic : "+result);
		}
		//String result = mechanicService.getNearestMechanic(request.getLocation(), request.getCustomerCoordinates(), request.getServiceType(), request.getBikeModel());
		//logger.debug("Nearest Available mechanic : "+result);
		return result;
	}
	
	@RequestMapping(value="/addSampleMechanics", method=RequestMethod.POST)
	public @ResponseBody String addSampleMechanics() throws IOException{
		if(mechanicService.addSampleDataForMechanics()){
			return "Sample data added";
		}else{
			return "Sample data was not added";
		}
	}
}
