$(document).ready(function() {
	if($('#basicDetails').parent().hasClass("active")){
		$('#basicDetailsContents').show();
		$('#personalDetailsContents').hide();
	}
	$('#basicDetails').click(function(){
		$('#basicDetails').parent().addClass("active");
		$('#personalDetails').parent().removeClass("active")
		$('#basicDetailsContents').show();
		$('#personalDetailsContents').hide();
	});
	$('#personalDetails').click(function(){
		if($('#firstname').val()=="" || $('#lastname').val()=="" || $('#email').val()==""|| $('#password').val()==""|| $('#confirmPassword').val()==""){
			alert("Please fill out all the fields before clicking on another tab.");
		}else{
			$('#basicDetails').parent().removeClass("active");
			$('#personalDetails').parent().addClass("active")
			$('#basicDetailsContents').hide();
			$('#personalDetailsContents').show();
		}
		
	});
	
	$('#signupNext').click(function(){
		if($('#firstname').val()=="" || $('#lastname').val()=="" || $('#email').val()==""|| $('#password').val()==""|| $('#confirmPassword').val()==""){
			alert("Please fill out all the fields before clicking next.");
		}else{
			$('#basicDetails').parent().removeClass("active");
			$('#personalDetails').parent().addClass("active")
			$('#basicDetailsContents').hide();
			$('#personalDetailsContents').show();
		}
		
	});
	
	$('#signupPrevious').click(function(){
		$('#basicDetails').parent().addClass("active");
		$('#personalDetails').parent().removeClass("active")
		$('#basicDetailsContents').show();
		$('#personalDetailsContents').hide();
	});
	
	$('#register').submit(function(){
		var pass = $('#password').val();
		var cpass = $('#confirmPassword').val();
		if(pass!=cpass){
			event.preventDefault();
			$('#basicDetails').parent().addClass("active");
			$('#personalDetails').parent().removeClass("active")
			$('#basicDetailsContents').show();
			$('#personalDetailsContents').hide();
			$("#confirmPasswordMsg").parent().parent().show();
			$("#confirmPasswordMsg").addClass( "text-danger" );
			$("#confirmPasswordMsg").text( "Password's do not match. Please try again" ).show();
		}
		
		var dob = new Date($('#dob').val());
		var age = (new Date()-dob)/(1000*60*60*24*365.25);
		if(age<18){
			event.preventDefault();
			$("#ageMsg").parent().parent().show();
			$("#ageMsg").addClass( "text-danger" );
			$("#ageMsg").text( "Minimum age criteria failed." ).show();
		}
		
		
		
	});
	
});