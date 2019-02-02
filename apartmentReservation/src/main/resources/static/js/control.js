$(document).ready(function() {

	var userRole = $('.userRole').text().toLowerCase();
	var userName = $('.userName').text().toLowerCase();
	var applicationStatus = $('.applicationSubmitted').text();
	$('#propertiesElements').append(appendListItemsToPropertiesTab(userRole));
	if(userRole=="admin"){
		$('#navbarListElements').append(appendListItemsToNavbar(userRole));
		$('#userAccountElements').append(appendListItemsToUserAccount(userRole));
	}else if(userRole=="owner"){
		$('#navbarListElements').append(appendListItemsToNavbar(userRole));
		$('#userAccountElements').append(appendListItemsToUserAccount(userRole));
	}else if(userRole == "customer"){
		$('#navbarListElements').append(appendListItemsToNavbar(userRole));
		$('#userAccountElements').append(appendListItemsToUserAccount(userRole));
	}else{
		$('#userAccountElements').append(appendListItemsToUserAccount(userRole));
		$('#navbarListElements').append(appendListItemsToNavbar(userRole));
	}
	
	
	/* Scroll to top js start */
	$('#scrollTop').css("opacity", "0");
	$(window).scroll(function () {
		// declare variable
		var topPos = $(this).scrollTop();
	    // if user scrolls down - show scroll to top button
	    if (topPos > 200) {
	      $('#scrollTop').css("opacity", "1");

	    } else {
	      $('#scrollTop').css("opacity", "0");
	    }
		
    });
	
    $('#scrollTop').click(function () {
        $("html, body").animate({
            scrollTop: 0
        }, 600);
        return false;
    });
    /* Scroll to top js end */
    
    /*Filters display functionality start */
	if($('.propertiesSize').text()){
		$('.displayFilters').hide();
		$('.displayFilterMessage').show();
	}
	if($('.filteredPropertiesSize').text() ||$('#apartmentNotFoundMessage').text() ){
		$('.displayFilterMessage').hide();
	}
	
    $('#displayFilter').click(function(){
    	$('.displayFilterMessage').hide()
    	$('.displayFilters').show()
    });
    
    $('#resetFilters').click(function(){
    	$(location).attr('href','http://localhost:8080/properties');

    });
    /*Filters display functionality end*/
   
    function appendListItemsToNavbar(role){
    	var html = "<p>Issue occured at function appendListItemsToNavbar in control.js</p>";
    	if(role=="owner"){
    		 html = "<li class=''><a href='/viewProperties'>View My Properties</a></li>" +
    		 		"<li class=''><a href='/viewAllApplications'>View Applications</a></li>";
    	}else if(role=="admin"){
    		html = "<li class=''><a href='/process_background_applications'>View Applications</a></li>";
    	}else if(role=="customer"){
    		html="";
    		if(applicationStatus=='yes'){
    			html = "<li class=''><a href='#'  data-toggle='modal' data-target='.application-status-modal-md'>Application Status</a></li>";
    		}
    	}else{
    		html = "<li class=''><a href='/visitingCustomerRoommatePreferencesSearch'>Roommate Search</a></li>";
    	}
    	return html;
    }
    
    var html1 = "";
    
    function appendListItemsToUserAccount(role){
    	var html = "<p>Issue occured at function appendListItemsToUserAccount in control.js</p>";
    	if(role=="admin"){
    		 html = "<li class='dropdown-submenu'>" +
						"<a href='#' class='dropdown-toggle' data-toggle='dropdown' >My Account</a>" +
						"<ul id='dropdownSubmenu' class='nav navbar-nav navbar-inverse dropdown-menu'>" +
							"<li>" +
								"<a href='/myAccount'>Edit Details</a>" +
							"</li>\n" +
							"<li>" +
								"<a href='/changePassword'>Change Password</a>" +
							"</li>\n" +
						"</ul>"+
					"</li>\n" +
    		 		"<li>" +
    		 		"<a href='/viewAllIssues/'>View Issue Requests</a>" +
    		 		"</li>\n  " +
    		 		"<li role='separator' class='divider'></li>" +
    		 		"<li>" +
    		 		"<a href='#' data-toggle='modal' data-target='.bs-example-modal-sm'>Log Out</a>" +
    		 		"</li>";
    	}else if(role=="owner"){
    		html = "<li class='dropdown-submenu'>" +
						"<a href='#' class='dropdown-toggle' data-toggle='dropdown' >My Account</a>" +
						"<ul id='dropdownSubmenu' class='nav navbar-nav navbar-inverse dropdown-menu'>" +
							"<li>" +
								"<a href='/myAccount'>Edit Details</a>" +
							"</li>\n" +
							"<li>" +
								"<a href='/changePassword'>Change Password</a>" +
							"</li>\n" +
						"</ul>"+
					"</li>\n" +
    				"<li>" +
    				"<a href='/viewAllMyPropertyIssues/'>View Issue Requests</a>" +
    				"</li>\n" +
    				"<li role='separator' class='divider'></li>" +
    				"<li>" +
    				"<a href='#' data-toggle='modal' data-target='.bs-example-modal-sm'>Log Out</a>" +
    				"</li>";
    	}else if(role=="customer"){

    		html = "<li class='dropdown-submenu'>" +
    					"<a href='#' class='dropdown-toggle' data-toggle='dropdown' >My Account</a>" +
    					"<ul id='dropdownSubmenu' class='nav navbar-nav navbar-inverse dropdown-menu'>" +
	    					"<li>" +
								"<a href='/myAccount'>Edit Details</a>" +
							"</li>\n" +
	    					"<li>" +
	    						"<a href='/changePassword'>Change Password</a>" +
	    					"</li>\n" +
    					"</ul>"+
    				"</li>\n" +
    				"<li>" +
    				"<a href='/serviceIssuePortalHome'>Service Issue Portal</a>" +
    				"</li>\n" +
    				"<li class='dropdown-submenu'>" +
						"<a class='dropdown-toggle' data-toggle='dropdown'  href='#' >Roommate Preferences</a>" +
						"<ul id='dropdownSubmenu' class='nav navbar-nav navbar-inverse dropdown-menu'>" +
	    					"<li>" +
								"<a href='/roommatePreferences'>Set/Edit Preferences</a>" +
							"</li>\n" +
	    					"<li>" +
	    						"<a href='/roommatePreferencesSearch'>Roommate Search</a>" +
	    					"</li>\n" +
	    					"<li role='separator' class='divider'></li>" +
	    					"<li>" +
    						"<a href='/roommateRequest'>Roommate Requests</a>" +
    					"</li>\n" +
						"</ul>"+
					"</li>\n" +
    				"<li role='separator' class='divider'></li>" +
    				"<li>" +
    				"<a href='#' data-toggle='modal' data-target='.bs-example-modal-sm'>Log Out</a>" +
    				"</li>";
    	}else{
    		html = "<li><a href='/login'>Log In</a></li>\n<li><a href='/signup'>Register</a></li>";
    	}
    	return html;	
    }
    
    function appendListItemsToPropertiesTab(role){
    	var html = "<p>Issue occured at function appendListItemsToPropertiesTab in control.js</p>";
    	if(role=="admin"){
    		var html = "<li><a href='/properties'>All Properties</a></li>\n";
    	}else if(role=="owner"){
    		var html = "<li><a href='/properties'>All Properties</a></li>\n" ;
    	}else if(role=="customer"){

    		var html = "<li><a href='/properties'>All Properties</a></li>\n" +
    				"<li role='separator' class='divider'></li>" +
    				"<li><a href='/sharedProperties'>Shared Properties</a></li>";
    	}else{
    		var html = "<li><a href='/properties'>All Properties</a></li>\n" +
    				"<li role='separator' class='divider'></li>" +
    				"<li><a href='/sharedProperties'>Shared Properties</a></li>";
    	}
    	return html;	
    	
    	
    	return html;
    }
    
    var pathname = window.location.pathname;
	$('.nav > li > a[href="'+pathname+'"]').parent().addClass('active');
	
	String.prototype.capitalize = function() {
	    return this.charAt(0).toUpperCase() + this.slice(1);
	}
	
	$('.userAccountName').html(" "+userName.split(" ")[0].capitalize());
	
	 $('#otherroommates').change(function() {
	        $('.enterdetails_rm1').slideUp("slow");
	        $('.enterdetails_rm2').slideUp("slow");
	        $('#' + $(this).val()).slideDown("slow");
	    });
	 
 	$('#issueType').change(function() {
 		if($('#issueType').val()==''){
 			 $('#textBox').hide()
 		}else{
 			 $('#textBox').show()
 		}
	   
	    //$('#' + $(this).val()).slideDown("slow");
	});
});