$(document).ready(function() {
	var coordinates=[];
	/* Initializing google maps api here */
	function initMap() {
		var map = new google.maps.Map(document.getElementById('map'), {
			center : new google.maps.LatLng(12.8447728,80.22546299999999)
		,mapTypeId : google.maps.MapTypeId.TERRAIN, zoom : 13});
		var input = /** @type {!HTMLInputElement} */
			(document.getElementById('location'));
		var autocomplete = new google.maps.places.Autocomplete(input);
		autocomplete.bindTo('bounds', map);
		var infowindow = new google.maps.InfoWindow();
		var marker = new google.maps.Marker({
			map : map,
			anchorPoint : new google.maps.Point(0, -29)
		});

		// Define the LatLng coordinates for the polygon's path.
		/* The polygon covers Navallur, sholinganallur and ECR road areas */
		var serviceAreaCoordinates = [
		                              {lat: 12.782423073671882, lng: 80.21856307983398},
		                              {lat: 12.78903556912079, lng: 80.24311065673828},
		                              {lat: 12.86443922378586, lng: 80.24250984191895},
		                              {lat: 12.92141630447605, lng: 80.23092269897461},
		                              {lat: 12.917317065283632, lng: 80.19195556640625},
		                              {lat: 12.786022301533505, lng: 80.20774841308594},
		                              {lat: 12.782423073671882, lng: 80.21856307983398}
		                              ];

		// Construct the polygon.
		var serviceArea = new google.maps.Polygon({
			paths: serviceAreaCoordinates,
			strokeColor: '#FF0000',
			strokeOpacity: 0.8,
			strokeWeight: 2,
			fillColor: '#A3FFA3',
			fillOpacity: 0.35
		});
		serviceArea.setMap(map);

		autocomplete.addListener('place_changed', function() {
			infowindow.close();
			marker.setVisible(false);
			var place = autocomplete.getPlace();
			var result = google.maps.geometry.poly.containsLocation(place.geometry.location, serviceArea) ? true : false;
			if(result){
				if (!place.geometry) {
					window.alert("Autocomplete's returned place contains no geometry");
					return;
				}
				if (place.geometry.viewport) {
					map.fitBounds(place.geometry.viewport);
				} else {
					map.setCenter(place.geometry.location);
					map.setZoom(14); // Why 17? Because it looks good.
				}
				marker.setIcon(/** @type {google.maps.Icon} */
						({
							url : place.icon,
							size : new google.maps.Size(71, 71),
							origin : new google.maps.Point(0, 0),
							anchor : new google.maps.Point(17, 34),
							scaledSize : new google.maps.Size(35, 35)
						}));
				marker.setPosition(place.geometry.location);
				marker.setVisible(true);
				var address = '';
				if (place.address_components) {
					address = [(place.address_components[0] && place.address_components[0].short_name || ''),
					           (place.address_components[1] && place.address_components[1].short_name || ''),
					           (place.address_components[2] && place.address_components[2].short_name || '') ]
					.join(' ');}
				infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
				//infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + place.geometry.location.lat() + ',' +place.geometry.location.lng());
				coordinates = [];
				coordinates.push(place.geometry.location.lat());
				coordinates.push(place.geometry.location.lng());
				infowindow.open(map, marker);			
			}else{
				alert("Currently we are operative only in Navalur, Sholinganallur and ECR");
				$('#location').val("");
			}
		});
	} /* End of initMap() */
	
	/* Initializing selectize controls for the forms */
	var $bikeModelSelectize = $('#bikeModel')	.selectize({
		sortField : 'text'
	});
	bikeModelSelectize = $bikeModelSelectize[0].selectize;
	var $serviceTypeSelectize = $('#serviceType').selectize({
		sortField : 'text'
	});
	serviceTypeSelectize=$serviceTypeSelectize[0].selectize;
	
	/* Initializing datepicker */

	function twodigits(digits) {
	    return (digits > 9) ? digits : '0' + digits;
	}
	var fullDate = new Date();
	for(i=0; i<8;i++){
		fullDate.setDate(fullDate.getDate()+1);
		var twoDigitDays = twodigits(fullDate.getDate());
		var twoDigitMonth = twodigits(fullDate.getMonth() + 1);
		var addDate = twoDigitDays + "/" + twoDigitMonth + "/" + fullDate.getFullYear();
		$('#date').append($('<option>', { 
	        value: addDate,
	        text : addDate 
	    }));
	}
	
	/* BEGIN: Various event handlers will follow from now */

	/* Handling events for click on next step of form filling */
	$('.next').on('click', function(e){
		if($('#selectBikeTab').hasClass('active')){
			if($('#bikeModel').val() == ""){
				$('#bikeModel').closest('.form-group').addClass('has-error'); 
			}else{
				$('#bikeModel').closest('.form-group').removeClass('has-error');
				$('#type').html("Step 2: Service Type");
				$('#progressBar').css('width', '25%');
				$('#selectBikeTab').removeClass('active');
				$('#serviceTypeTab').addClass('active');
			}
		} else if($('#serviceTypeTab').hasClass('active')){
			if($('#serviceType').val() == ""){
				$('#serviceType').closest('.form-group').addClass('has-error'); 
			}else{
				$('#serviceType').closest('.form-group').removeClass('has-error');
				$('#type').html("Step 3: Share Location");
				$('#progressBar').css('width', '50%');
				$('#serviceTypeTab').removeClass('active');
				$('#locationTab').addClass('active');
				$('#map').show();
				initMap();		 
			}
		} else if($('#locationTab').hasClass('active')){
			$('#locationForm').submit();
		}
	});
	
	/* Handling events for previous button click on form */
	$('.previous').on('click', function(e){
		if($('#serviceTypeTab').hasClass('active')){
			$('#type').html("Step 1: Select Bike");
			$('#progressBar').css('width', '0%');
			$('#serviceTypeTab').removeClass('active');
			$('#selectBikeTab').addClass('active');
		} else if($('#locationTab').hasClass('active')){
			$('#type').html("Step 2: Service Type");
			$('#progressBar').css('width', '25%');
			$('#locationTab').removeClass('active');
			$('#serviceTypeTab').addClass('active');
			$('#map').hide();
		} else if($('#contactDetailsTab').hasClass('active')){
			$('#type').html("Step 3: Share Location");
			$('#progressBar').css('width', '50%');
			$('#contactDetailsTab').removeClass('active');
			$('#locationTab').addClass('active');
			$('#map').show();
			initMap();
		}
	});

	/* Handling final request submit event */
	$("#submitRequest").on('click', function(e){
		$('#contactDetailsForm').submit();
	});
	
	/* Handling event for addSampleMechanics */
	$("#addSampleMechanics").on('click', function(e){
		$.ajax({
			contentType : 'application/json; charset=utf-8',
			url : 'addSampleMechanics',
			type : "POST",
			success : function(response) {
				alert(response);
			},error : function(xhr, status,error) {
				alert(xhr.responseText);}
		}); 
	});
	/* END: Various event handlers */
	
	/* BEGIN: Validation stuff */
	
	/* Handling validation for selecting location event */
	$('#locationForm').validator().on('submit', function (e) {
		if (!e.isDefaultPrevented()) {
			e.preventDefault();
			$('#type').html("Step 4: Contact Details");
			$('#progressBar').css('width', '75%');
			$('#locationTab').removeClass('active');
			$('#contactDetailsTab').addClass('active');
			$('#map').hide();
		}
	});
	
	/* Handling validation for final request submission */
	$('#contactDetailsForm').validator().on('submit', function (e) {
		if (!e.isDefaultPrevented()) {
			$('#type').html("Submitting Request");
			$('#progressBar').css('width', '100%');
			$('#progressBar').addClass('progress-bar-success');
			$('#submitRequest').button('loading');
			e.preventDefault();
			var request = {};
			request['name'] = $("#name").val();
			request['bikeModel'] = $("#bikeModel").val();
			request['location'] = $("#location").val();
			request['serviceType'] = $("#serviceType").val();
			request['comments'] = $("#comments").val();
			request['addressDetails'] = $("#addressDetails").val();
			request['email'] = $("#email").val();
			request['mobile'] = $("#mobile").val();
			request['slot'] = $("#slot").val();
			request['date'] = $("#date").val();
			request['customerCoordinates'] = coordinates;
			$.ajax({
				contentType : 'application/json; charset=utf-8',
				url : 'submitRequest',
				data : JSON.stringify(request),
				type : "POST",
				success : function(response) {
					if(response == "SLOT_UNAVAILABLE"){
						alert("Oops, we are full at your selected time and slot. Try choosing another slot!");
						$('#submitRequest').button('reset');
						$('#progressBar').css('width', '75%');
						$('#progressBar').removeClass('progress-bar-success');
					}else if (response == "NO_MECH_AVAILABLE"){
						$('#contactDetailsTab').removeClass('active');
						alert("Oops, no mechanic available as per your requirements!");
						//$('#contactDetailsNav').removeClass('active');
						$('#selectBikeTab').addClass('active');
						//$('#selectBikeNav').addClass('active');
						//$('#myModal').modal('toggle');
						$('#submitRequest').button('reset');
						$('#type').html("Step 1: Select Bike");
						$('#progressBar').css('width', '0%');
						$('#progressBar').removeClass('progress-bar-success');
					}else{
						alert("Mechanic Alloted is: "+response);
						$("#name").val("");
						bikeModelSelectize.clear();
						serviceTypeSelectize.clear();
						$("#location").val("");
						$("#comments").val("");
						$("#addressDetails").val("");
						$("#email").val("");
						$("#mobile").val("");
						$("#slot").val("");
						$("#date").val("");
						$('#contactDetailsTab').removeClass('active');
						//$('#contactDetailsNav').removeClass('active');
						$('#selectBikeTab').addClass('active');
						//$('#selectBikeNav').addClass('active');
						//$('#myModal').modal('toggle');
						$('#submitRequest').button('reset');
						$('#type').html("Step 1: Select Bike");
						$('#progressBar').css('width', '0%');
						$('#progressBar').removeClass('progress-bar-success');
					}
				},error : function(xhr, status,error) {
					$('#type').html("Step 4: Contact Details");
					$('#progressBar').css('width', '75%');
					$('#progressBar').removeClass('progress-bar-success');
					$('#submitRequest').button('reset');
					alert(xhr.responseText);}
			});
		}
	});
	/* END: Validation stuff */
});