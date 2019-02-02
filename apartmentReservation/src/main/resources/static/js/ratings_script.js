/**
 * 
 * This script contains all javascript/jQuery functionality with respect to ratings and reviews functionality.
 */
$(document).ready(function() {
	var msg = $('#changePasswordMsg').text();
	if(msg){
		$('#changePasswordMsg').show();
	}
	var userRole = $('.userRole').text().toLowerCase();
	var userName = $('.userName').text().toLowerCase();
	var allowRatings = $('.allowRatings').text().toLowerCase();
	var hasAlreadyReviewedController = $('.hasAlreadyReviewed').text().toLowerCase();
	$('.property').each(function(){
		var avgRating = $(this).find('.ratingPoints').text();
		avgRating= Number(avgRating);
		var fullStarsCount = parseInt(avgRating/1);
		var stars ="";
		var remainingStarsCount = parseInt(5-avgRating);
		for(var i=0;i<fullStarsCount;i++){
			stars += "<i class='fa fa-star fa-2x'></i>";
		}
		if(avgRating%1!=0){
			stars += "<i class='fa fa-star-half-o fa-2x'></i>";
		}
		if(remainingStarsCount>0){
			for(var i=0;i<remainingStarsCount;i++){
				stars += "<i class='fa fa-star-o fa-2x'></i>"
			}	
		}
		$(this).find('.ratingPoints').after(stars);	
	});
	
	$('.userRatingAndReviews').each(function(){
		var personalRating = $(this).find('.ratingPointsByUser').text();
		personalRating= Number(personalRating);
		var fullStarsCount =parseInt(personalRating/1);
		var stars ="<div>";
		var remainingStarsCount = parseInt(5-personalRating);
		for(var i=0;i<fullStarsCount;i++){
			stars += "<i class='fa fa-star'></i>";
		}
		if(personalRating%1!=0){	
			stars += "<i class='fa fa-star-half-o'></i>";
		}
		if(remainingStarsCount>0){
			for(var i=0;i<remainingStarsCount;i++){
				stars += "<i class='fa fa-star-o'></i>"
			}	
		}
		stars += "</div>";
		$(this).find('.ratingPointsByUser').after(stars);
		
	});

	if(userRole == "customer" && allowRatings=="allowed"){
		if(hasAlreadyReviewedController=="yes"){
			$('#newRatingAndReview').hide();
		}else{
			$('#newRatingAndReview').show();
		}
		
	}else{
		$('#newRatingAndReview').hide();
	}
	
	
	/**
	 * Javascript functionality to preselect values on the Roommate Preference Form
	 */
	if($('.pref').text()){
		var genderPreference = $('.gender').val().toLowerCase();
		var veganPreference = $('.vegan').val().toLowerCase();
		var drinkingPreference = $('.drinking').val().toLowerCase();
		var smokingPreference = $('.smoking').val().toLowerCase();
		var petsPreference = $('.pets').val().toLowerCase();
		if(genderPreference){
			if(genderPreference == "male"){
				$('.gendermale').addClass('active ');
			}else{
				$('.genderfemale').addClass('active ');
			}
		}
		if(veganPreference){
			
			if(veganPreference=="true"){
				$('.vegantrue').addClass('active ');
			}else{
				$('.veganfalse').addClass('active ');
			}
		}
		if(drinkingPreference){
			
			if(drinkingPreference=="true"){
				$('.drinkingtrue').addClass('active ');
			}else{
				$('.drinkingfalse').addClass('active ');
			}
		}
		if(smokingPreference){
			if(smokingPreference=="true"){
				$('.smokingtrue').addClass('active ');
			}else{
				$('.smokingfalse').addClass('active ');
			}
		}
		if(petsPreference){
			if(petsPreference=="true"){
				$('.petstrue').addClass('active ');
			}else{
				$('.petsfalse').addClass('active ');
			}
		}
	}
	
	
	$('#changePasswordForm').submit(function(event){
		
		var pass = $('#password').val();
		var cpass = $('#changePassword').val();
		if(pass!=cpass){
			$("#changePasswordMsg").removeClass( "alert alert-success" ).addClass( "alert alert-danger" );

			$("#changePasswordMsg").text( "Password's do not match. Please try again" ).show();
			event.preventDefault();
		}
	});
	
	
	/*
	 * Preference score rating and Progress bar functionality
	 */
	$('.roommatePreferenceResult').each(function(){
		var preferenceScore = $(this).find('#preferenceScore').text();
		preferenceScore= Number(preferenceScore);
		var preferenceScorePercentage= ((preferenceScore/7)*100);
		var preferenceScorePercentageString = preferenceScorePercentage.toFixed()+"%";
		var html = "<div class='progressCss'>" +
						"<div class='progressBar textCenter'>";
						html += preferenceScorePercentageString+"</div>" +
					"</div>";
		$(this).find('#preferenceScore').after(html);
		var color = "#f63a0f";
		
		$(this).find('.progressCss >.progressBar').css({
			"height": "16px",
			"border-radius": "4px",
			"background-image": "linear-gradient(to bottom, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0.05))",
			"transition": "2.0s linear",
			"transition-property": "width, background-color",
			"box-shadow": "0 0 1px 1px rgba(0, 0, 0, 0.25), inset 0 1px rgba(255, 255, 255, 0.1)",
			"width": "5%",
			"background-color": "#f63a0f"
		});
		
		
		if(preferenceScorePercentage>84.99){
			color = "#86e01e";
		}else if(preferenceScorePercentage>56.99){
			color = "#f2d31b";
		}else if(preferenceScorePercentage>27.99){
			color = "#f2b01e";
		}else if(preferenceScorePercentage>9.99){
			color = "#f27011";
		}else{
			color = "#f63a0f";
		}

		$(this).find('.progressCss >.progressBar').css({
			"height": "16px",
			"border-radius": "4px",
			"background-image": "linear-gradient(to bottom, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0.05))",
			"transition": "2.0s linear",
			"transition-property": "width, background-color",
			"box-shadow": "0 0 1px 1px rgba(0, 0, 0, 0.25), inset 0 1px rgba(255, 255, 255, 0.1)",
			"width": function(initWidth){
				return preferenceScorePercentageString;
			},
			"background-color": function(initbgc){
				return color;
			} 
		});
		
		$(this).find('.textCenter').css({
			"text-align":"center",
			"font-weight": "bold"
		});
		
	});
	
	/*
	 * wait cursor and show modal on hover.
	 */
	$('.user-icon').hover(function(){
		$('.user-icon').css({
			cursor: "pointer"
		});
	});

	$('.user-icon').click(function(){
		var userId =$(this).next('.userId').text(); 
		var userEmail = $(this).next('.userId').next('.userEmail').text();
		var loc = $(location).attr("pathname");
		var searchType = "newRoommateSearch";
		if(loc == "/sharedProperties/search"){
			searchType = "sharedPropertiesSearch";
		}
		
		if(userEmail){
			$('.user-icon').css({
				cursor: "wait"
			});
			$.ajax({
				url: "/roommatePreferencesSearch/"+userId+"/"+searchType,
				success: function(data){
					$('.modalHolder').html(data);
					$('.roommate-preference-modal-md').modal('show'); 
					
				}
			});
		}

	});
	
	$('#viewMatchedRequests').click(function(){
		var userId =$('#viewRequestDetailsApplicationId').text(); 
		//var userEmail = $(this).next('.userId').next('.userEmail').text();
		var loc = $(location).attr("pathname");
		var searchType = "newRoommateSearch";
		if(loc == "/sharedProperties/search"){
			searchType = "sharedPropertiesSearch";
		}
		
		//if(userEmail){
			$('.user-icon').css({
				cursor: "wait"
			});
			$.ajax({
				url: "/roommatePreferencesSearch/"+userId+"/"+"fetchAll",
				success: function(data){
					console.log(data);
					$('.modalHolder').html(data);
					$('.roommate-preference-modal-md').modal('show'); 
					
				}
			});
		//}
	});
	
	$('.testinglease').each(function(){
		var preferenceScore = $(this).find('#leasetest').text();
		var html = "<h4> Preference Match: ";
		html += preferenceScore+"%</h4>";
		/*var fullStarsCount = parseInt(avgRating/1);
		var stars ="";
		var remainingStarsCount = parseInt(5-avgRating);
		for(var i=0;i<fullStarsCount;i++){
			stars += "<i class='fa fa-star fa-2x'></i>";
		}
		if(avgRating%1!=0){
			stars += "<i class='fa fa-star-half-o fa-2x'></i>";
		}
		if(remainingStarsCount>0){
			for(var i=0;i<remainingStarsCount;i++){
				stars += "<i class='fa fa-star-o fa-2x'></i>"
			}	
		}	
		$(this).find('.ratingPoints').after(stars);*/	
			
		$(this).find('#leasetest').after(html);
	});
});

