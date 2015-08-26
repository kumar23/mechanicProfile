<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<title>Mechanic Profile</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<spring:url value="/resources/css/bootstrap.css" var="bootstrapCss" />
<spring:url value="/resources/css/selectize.css" var="selectizeCss" />
<spring:url value="/resources/css/bootstrap.datepicker.css"
	var="datepickerCss" />
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"> -->
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${selectizeCss}" rel="stylesheet" />
<link href="${datepickerCss}" rel="stylesheet" />
<style>
html, body {
	height: 100%;
	margin: 0;
	padding: 0;
}

.starter-template {
	/* padding: 0px 15px; */
	/* padding-bottom: 80px; */
	
}

#map {
	height: 100%;
	top: -380px;
	z-index: -999999999;
}

#locationTab {
	position: relative;
	z-index: 1; /* The z-index should be higher than Google Maps */
	/* margin: 40px auto 0; */
	padding: 10px;
	height: 265px;
	background: black;
	opacity: .85;
	/* Set the opacity for a slightly transparent Google Form */
	/* color: white; */
}
</style>
</head>
<body>

	<nav class="navbar navbar-default navbar-static-top">
		<div class="container">
			<div class="navbar-header">
				<button aria-expanded="false" type="button"
					class="navbar-toggle collapsed" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">DrivoJoy</a>
			</div>
			<div style="height: 1px;" aria-expanded="false"
				class="navbar-collapse collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">Book Now! <span
							class="sr-only">(current)</span></a></li>
					<li><a href="#" id="addSampleMechanics">Add Sample
							Mechanics</a></li>
					<li><a href="#">About Us</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#">Login</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container">
		<div class="row starter-template">
			<h4 id="type">Step 1: Select Bike</h4>
			<div class="progress">
				<div id="progressBar" class="progress-bar" style="width: 0%;"></div>
			</div>
			<div
				class="col-sm-12 col-xs-12 col-lg-6 col-lg-offset-3 col-md-6 col-md-offset-3">
				<div class="tab-content" style="padding-top: 25px">
					<div role="tabpanel" class="tab-pane active" id="selectBikeTab">
						<div class="form-group has-feedback">
							<label for="bikeModel">Select Bike</label> <select id="bikeModel"
								placeholder="Type to select" name="bikeModel" required>
								<option value="">Type to select</option>
								<option value="Pulsar">Pulsar</option>
								<option value="Karizma">Karizma</option>
								<option value="Discover">Discover</option>
								<option value="Activa">Activa</option>
								<option value="Splendor">Splendor</option>
							</select>
						</div>
						<ul class="pager">
							<li class="next"><a href="#">Next →</a></li>
						</ul>
					</div>
					<div role="tabpanel" class="tab-pane" id="serviceTypeTab">
						<div class="form-group">
							<label for="serviceType">Service Type</label> <select
								id="serviceType" placeholder="Type to select" name="serviceType">
								<option value="">Type to select</option>
								<option value="Free Service">Free Service</option>
								<option value="Regular Service">Regular Service</option>
								<option value="Oil Change">Oil Change</option>
								<option value="Breakdown">Breakdown</option>
								<option value="Breaks or Tyres">Breaks or Tyres</option>
								<option value="Clutch">Clutch</option>
								<option value="Others">Others</option>
							</select>
						</div>
						<div class="form-group">
							<label for="problem">Comments (optional)</label>
							<textarea class="form-control" rows="4" id="comments"
								placeholder="Describe your problem here"></textarea>
						</div>
						<ul class="pager">
							<li class="previous"><a href="#">← Back</a></li>
							<li class="next"><a href="#">Next →</a></li>
						</ul>
					</div>
					<div role="tabpanel" class="tab-pane" id="locationTab">
						<form id="locationForm" data-toggle="validator" role="form">
							<div class="form-group has-feedback">
								<div class="input-group has-feedback">
									<div class="input-group-addon">
										<span class="glyphicon glyphicon-map-marker"
											aria-hidden="true"></span>
									</div>
									<input type="text" class="form-control " id="location"
										placeholder="Enter your location"
										data-error="Oops, we can't find you without a location!"
										required>
									<div class="help-block with-errors"></div>
								</div>
							</div>
							<div class="form-group has-feedback">
								<label for="addressDetails">Address Details</label><input
									type="text" class="form-control" id="addressDetails"
									placeholder="Flat #, Building, Street Name"
									data-error="Adding details helps us find you better" required>
								<div class="help-block with-errors"></div>
							</div>
							<ul class="pager">
								<li class="previous"><a href="#">← Back</a></li>
								<li class="next"><a href="#">Next →</a></li>
							</ul>
						</form>
					</div>
					<div role="tabpanel" class="tab-pane" id="contactDetailsTab">
						<form id="contactDetailsForm" data-toggle="validator" role="form">
							<div class="row">
								<div class="form-group col-lg-6 has-feedback">
									<label for=name>Name</label> <input type="text"
										class="form-control" id="name"
										placeholder="eg. Kumar, minimum 3 characters"
										data-error="Let us introduce ourselves!" minlength=3 required>
									<div class="help-block with-errors"></div>
								</div>
								<div class="form-group col-lg-6 has-feedback">
									<label for=mobile>Mobile</label> <input type="number"
										class="form-control" id="mobile"
										placeholder="eg. 1234567890, 10 digits"
										data-error="We will call you once we reach the location!"
										required>
									<div class="help-block with-errors"></div>
								</div>

							</div>
							<div class="form-group has-feedback">
								<label for=email>Email</label> <input type="email"
									class="form-control" id="email" placeholder="abc@example.com"
									data-error="Email helps us to keep in touch with you!" required>
								<div class="help-block with-errors"></div>
							</div>
							<div class="row">
								<div class="form-group col-lg-6 has-feedback">
									<label for=date>Date</label>
										<select id="date" class="form-control">
											<option value=""></option>
										</select>
									<div class="help-block with-errors"></div>
								</div>
								<div class="form-group col-lg-6 has-feedback">
									<label for=slot>Slot</label> <select id="slot"
										placeholder="Select a slot" class="form-control" name="slot"
										data-error="We will reach you as per your convenience!"
										required>
										<option value=""></option>
										<option value="1">9am - 11am</option>
										<option value="2">11am - 1pm</option>
										<option value="3">2pm - 4pm</option>
										<option value="3">4pm - 6pm</option>
									</select>
									<!-- <input type="text"
										class="form-control" id="time" placeholder="eg. 9am, 10.30pm"
										required> -->
									<div class="help-block with-errors"></div>
								</div>
							</div>
							<ul class="pager">
								<li class="previous"><a href="#">← Back</a></li>
								<li class="next"><a id="submitRequest"
									data-loading-text="Please Wait..." href="#">Submit</a></li>
							</ul>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="map" style="display: none;"></div>
	<!-- Jquery and bootstrap js files -->
	<spring:url value="/resources/js/jquery.min.js" var="jqueryJs" />
	<spring:url value="/resources/js/jquery.validate.min.js" var="jqueryValidateJs" />
	<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs" />
	<spring:url value="/resources/js/validator.min.js" var="validatorJs" />
	<spring:url value="/resources/js/selectize.min.js" var="selectizeJs" />
	<spring:url value="/resources/js/app.js" var="appJs" />

	<script
		src="https://maps.googleapis.com/maps/api/js?signed_in=false&libraries=places&callback=initMap"
		async defer></script>
	<script src="${jqueryJs}"></script>
	<script src="${jqueryValidateJs}"></script>
	<script src="${bootstrapJs}"></script>
	<script src="${validatorJs}"></script>
	<script src="${selectizeJs}"></script>
	<script src="${appJs}"></script>

</body>
</html>