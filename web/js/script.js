var resultref;
var marker;
var thismemory;
var markersArray = [];
var shitCounter = 0;
var iteration = 0;
var drinkIcon = 'images/food.png';
var homeIcon = 'images/home.png';
var resultsStore;
var totalResults =[];
var pubResultsStore;
var barResultsStore;
var userLoc;
var currentlatlng;
var path;
var musicpath;
var index;
/*
var dates = new Array("Monday - 25 January 2010", "Wednesday - 10 February 2010", "Thursday - 10 June 2010", "Thursday - 9 September 2010", "Tuesday - 5 October 2010", "Tuesday - 2 November 2010", "Tuesday - 5 April 2011", "Friday - 8 April 2011", "Tuesday - 26 July 2011", "Tuesday - 6 September 2011", "Tuesday - 13 September 2011", "Monday - 10 October 2011", "Friday - 8 June 2012", "Friday - 14 December 2012", "Thursday - 27 December 2012", "Monday - 7 January 2013", "Monday - 8 April 2013", "Monday, 1 July 2013", "Tuesday - 6 August 2013", "Friday - 11 October 2013","Tuesday - 25th December 2012");
var people = new Array("Friends","Family","Acquaintances","Colleagues","Classmates","Relatives","Neighbors");
var locations = new Array("Miami Beach, FL, United States","Universal Studios Orlando, Universal Boulevard, Orlando, FL, United States","Fort Canning Park Singapore","Arab Street Singapore","Dubai - United Arab Emirates","Phnom Penh, Cambodia","Fujairah - United Arab Emirates","Cold Spring, NY, United States");
var videos = new Array("http://www.youtube.com/embed/watch?v=-zmx0kScUss","http://www.youtube.com/embed/watch?v=Ik-sFkQqKD8;autoplay=1","http://www.youtube.com/embed/watch?v=zw2a64fp3No&iframe;autoplay=1","http://youtube.com/embed/watch?v=0xDNuWX5nUA?t=5s;autoplay=1","http://www.youtube.com/embed/watch?v=wOSci-rWf2Q;autoplay=1");
var memories = new Array("images/photos/Memory1/","images/photos/Memory2/","images/photos/Memory3/","images/photos/Memory4/","images/photos/Memory5/","images/photos/Memory6/resized/","images/photos/Memory7/","images/photos/Memory8/");
*/
var music = {'pleased':"music/island.m4a",'excited':"music/peach.m4a",'content':"music/swing.m4a",'aroused':"music/bbq.m4a",'sleepy':"music/lazyday.m4a",'depressed':"music/minuet.mp3",'miserable':"music/acoustic.m4a",'distressed':"music/acoustic.m4a",'neutral':"music/acoustic.m4a"};

$wait = jQuery('#wait');
$locationBar = jQuery('#locationBar')
//map stuff
//
//declare b&w google maps

//set map options
function updateDate()

{

}
//create map
//other map stuff


//kick shit off 
jQuery(document).ready(function(){
	//detect string  to trigger manual location entry
	var str = window.location.href;
	var substr = str.split('?');
	if(substr[1] == "where"){			
		$locationBar.css("opacity",1);
		console.log ("manual location entry")
		
	}
	//otherwise trigger browser geolocation
	else{getLocation();}
});

//if browser has geolocation then get current location
function getLocation() {
	if (Modernizr.geolocation) {
		navigator.geolocation.getCurrentPosition(currentLocation, handle_error,   {timeout:10000});
		console.log ("browser has geolocation")
		}
	else {
		//trigger manual entry if no geolocation
		console.log("browser has geolocation");
		$locationBar.fadeIn();
	}
}

//if geolocation error trigger manual entry
function handle_error(err) {
	$locationBar.css("opacity",1);
	showError("Hi Atif, what do you want to see today?");
	console.log ("can't find location")
	if (err.code == 0) {
		console.log("unknown")
	}
	if (err.code == 1) {
		console.log("denied")
	}
	if (err.code == 2) {
		console.log("unreliable")
	}
	if (err.code == 3) {
		console.log("taking ages")
	}
}

//get current location
function currentLocation(position){
	$wait.fadeIn(function(){
		jQuery("#locationBar").fadeOut();
		var latitude = position.coords.latitude;
		var longitude = position.coords.longitude;
		currentlatlng = new google.maps.LatLng(latitude, longitude);
		console.log ("current position is: " + currentlatlng);
		//reverse geocode
		geocoder = new google.maps.Geocoder();
		geocoder.geocode({'latLng': currentlatlng}, function(results, status) {
      		if (status == google.maps.GeocoderStatus.OK) {
        		if (results[1]) {
					formattedAddress = results[1].formatted_address;
					console.log(formattedAddress);
					if (formattedAddress.indexOf("USA") !=-1) {
						console.log("in the USA");
						jQuery("#omnom").show();
					}
          		}
      		}
			else {

        	console.log("Geocoder failed due to: " + status);
      		}
    	});

		getPlaces(currentlatlng);
	});
	
}

//set home position and request for bars
function getPlaces(currentlatlng) {
	userLoc = currentlatlng;
	homeMarker = new google.maps.Marker({
		map: map,
		animation: google.maps.Animation.DROP,
		position:currentlatlng,
		icon:homeIcon
		});
	var requestBar = {
		location: currentlatlng,
		radius: 1000,
		keyword: "restaurant"
		};
	service.search(requestBar, storeRequestBar);
}

//store the results for bars then call for pubs
function storeRequestBar(request) {
	//if (status == google.maps.places.PlacesServiceStatus.OK)
	barResultsStore = request;
	var requestPub = {
	location: currentlatlng,
	radius: 1000,
	types: "restaurant"
	};
	service.search(requestPub, storeRequestPub);
}

//store the results for pubs & combine
function storeRequestPub(request) {
	//if (status == google.maps.places.PlacesServiceStatus.OK)
	pubResultsStore = request;
	//combine pubs & bars into one array
	totalResults = barResultsStore.concat(pubResultsStore)
	console.log("there are " + totalResults.length + "pubs & bars")
	//remove duplicate results
	resultsStore = removeDupes(totalResults, 'id');
	//randomise results
	resultsStore = resultsStore.sort(function(a, b){
		return Math.random() - 0.5
		});	
	for (i=0;i<resultsStore.length;i++) {
		console.log(resultsStore[i].name)
		}
	if (resultsStore == 0) {
		showError("can't find shit there. try somewhere else");
		} 
	chooseBar(resultsStore);	
}

//choose a bar 
function chooseBar(results) {
	barRef = {reference: results[shitCounter].reference}
	service.getDetails(barRef, showBar);
}

//show bar details
function showBar(playbackArray, status) {
	 //var x=document.getElementById("viewport");
	 document.getElementById('myCanvasContainer').style.display = 'none';
					console.log("reached showbar");										  		
	

	jQuery("#map").html("<img src=\""+playbackArray[0].path+"/0.jpg\">");
	document.getElementById('wtf').innerHTML="On "+playbackArray[0].date;
	document.getElementById('wrong').innerHTML = "You were with "+playbackArray[0].people;
	jQuery("#shit").click(function() {
		/*
		jQuery.fancybox([
			path+'1.jpg',
			path+'2.jpg',
			path+'3.jpg'
		], {
			'padding'			: 0,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'              : 'image',
			'changeFade'        : 0
		});*/
//jQuery.modal(,{); 
var audio = new Audio(music[playbackArray[0].mood.toLowerCase()]);
audio.play();
var pics;
pics = "<div id=\"slider\"><img src=\""+playbackArray[0].path+"1.jpg\" alt=\"\" /><img src=\""+playbackArray[0].path+"2.jpg\" alt=\"\" title=\"Ironman Screenshot\" /><a href=\"\"><img src=\""+playbackArray[0].path+"3.jpg\" alt=\"\" /></a><img src=\""+playbackArray[0].path+"1.jpg\" alt=\"\"/></div>" 
jQuery.modal(pics,{opacity: 50, overlayCss: {backgroundColor:"#000"}, onOpen: function (dialog) {
	dialog.overlay.fadeIn('slow', function () {
		dialog.data.hide();
		dialog.container.fadeIn('slow', function () {
			dialog.data.slideDown('slow');	 
		});
	});
},onClose: function (dialog) {
	audio.pause();
	dialog.data.fadeOut('slow', function () {
		dialog.container.hide('slow', function () {
			dialog.overlay.slideUp('slow', function () {
				$.modal.close();
			});
		});
	});
}});

window.myFlux = new flux.slider('#slider');
});

		console.log("reached showbar2");
		
		
		jQuery("#address").html(playbackArray[0].location);
				$wait.delay(500).fadeOut("slow",function(){
				jQuery("#destination").html("YOU WERE AT <br/><a href='" + "" + "' target='_blank' title='VISIT THE WEBSITE'>" + playbackArray[0].location + "</a>")
				document.getElementById("map").style.visibility="visible";
				document.getElementById("address").style.visibility="visible";
				jQuery("#actions, #about, #recommendation, .ad").fadeIn(updateDate());
				});
		//updateDate();
	}



//get directions
var directionsDisplay =  new google.maps.DirectionsRenderer();
var directionsService = new google.maps.DirectionsService();
function calcRoute(start, end) {
  	var request = {
    	origin:start,
    	destination:end,
    	travelMode: google.maps.TravelMode.WALKING
  	};
	directionsService.route(request, function(result, status) {
    	if (status == google.maps.DirectionsStatus.OK) {
      		directionsDisplay.setDirections(result);
			//strokeColor
    	}
  	});
}

 
//manual geolocation
function codeAddress() {

    //document.body.style.background = "";
    //document.body.style.backgroundColor = "#FFF";
              document.getElementById('myCanvasContainer').style.display = 'none';

	$wait.fadeIn(function(){
	$locationBar.fadeOut();
	var filledFields = new Array();
	var location = document.getElementById("location").value;
	var locationWeight = document.getElementById("locationWeight").value;
	var date=document.getElementById("datepicker").value;
	var dateWeight = document.getElementById("dateWeight").value;
	var people = document.getElementById("people").value;
	var peopleWeight = document.getElementById("peopleWeight").value;
	var mood = document.getElementById("emotion").value;
	var moodWeight = document.getElementById("moodWeight").value;
	var playbackArray = new Array();
	var index=0;
	var score=0;
	var greatestScore=0;
	for (i = 0; i < memories.length; ++i) {	
    if(location.toLowerCase()==memories[i].location.toLowerCase())
	{		console.log(location.toLowerCase()+"-"+memories[i].location.toLowerCase());

	if (locationWeight>=50)
	score+=2;
	else score++;
	}
	if(date==memories[i].date)
	{
			console.log(date.toLowerCase()+"-"+memories[i].date.toLowerCase());

	if (dateWeight>=50)
	score+=2;
	else score++;
	}

	if(memories[i].people.toLowerCase().indexOf(people) != -1 && people != "")
	{	
		console.log(people.toLowerCase()+"-"+memories[i].people.toLowerCase());

	if (peopleWeight>=50)
	score+=2;
	else score++;
	}
	if(mood.toLowerCase()==memories[i].mood.toLowerCase() && mood!="")
	{
	console.log(mood.toLowerCase()+"-"+memories[i].mood.toLowerCase());
	if (moodWeight>=50)
	score+=2;
	else score++;
	}
	if (score>greatestScore)
	{ greatestScore = score;
	if (index!=0)
	{
	var temp=playbackArray[0];
	playbackArray[0]=memories[i];
	playbackArray[index]=temp;
	score=0;
	}
	else {playbackArray[0]=memories[i]}
	index++;
	continue;
	}
	if (score>=1)
	{
	playbackArray[index]=memories[i];
	index++;
	}
	score=0;
	}
	console.log("reached end of codeAddress");
	showBar(playbackArray,0);
	/*
	var address = document.getElementById("location").value;
	index = memories.location.indexOf(address);
	path = memories.path[index];
	jQuery("#map").html("<img src=\""+path+"/0.jpg\">");
	musicpath = music[index];
	thismemory=address;
	
	geocoder.geocode( { 'address': address}, function(results, status) {
		console.log("manual location");										  		
		if (status == google.maps.GeocoderStatus.OK) {
			//console.log(results[0].geometry.location);
			if (results[0].formatted_address.indexOf("USA") !=-1) {
				console.log("in the USA");
				jQuery("#omnom").show();
			}
			currentlatlng = results[0].geometry.location;
			getPlaces(currentlatlng); 
			
		}
		else {
				console.log("reached");										  		

			//console.log("Geocode was not successful for the following reason: " + status);
			//showError("Can't find your location, try again");
			jQuery("#omnom").show();
			var place=new Object;
			place.name="Dubai - United Arab Emirates";
			place.formatted_address="Al Mamzar, Deira, Dubai"
			showBar(place,google.maps.places.PlacesServiceStatus.ZERO_RESULTS);
		}
		});
		*/
		});
		
}


//choose another bar
/*
jQuery("#shit").click(function(){
	//if there's still more bars in the array, choose the next one					  
	if (shitCounter < (resultsStore.length - 1)) {
		shitCounter++;
	}
	//otherwise go back to the beginning
	else {
		shitCounter = 0;
		console.log("repeat");
	}
	console.log(shitCounter);
	chooseBar(resultsStore)
	window.scroll(0,0);
	return false
});
*/

//hide url bar on ios
addEventListener("load", function() {setTimeout(hideURLbar, 0); }, false);
function hideURLbar(){
        window.scrollTo(0,1)
    }
	
//show error messages	
function showError(msg){
	$wait.fadeOut();
	$locationBar.fadeIn();
	jQuery("#error span").css("background","white").css("opacity","0.8");
	jQuery('#error').html("<span>"+msg+"</span><a id=\"showMoreButton\"><img src=\"images/showmore.png\" id=\"showMore\"></a>").fadeIn();
	jQuery("#showMoreButton").click(function(){
	jQuery("html, body").animate({ scrollTop: "750px" }, "slow");
	jQuery(".weights").toggle("slow");
	});
}


//google geocode autocomplete
var autoOptions = {types: ['geocode']};
var autoInput = document.getElementById('location');
autocomplete = new google.maps.places.Autocomplete(autoInput, autoOptions);

//remove duplicate objects in array
function removeDupes(arr, prop) {
    var new_arr = [];
    var lookup  = {};
 
    for (var i in arr) {
        lookup[arr[i][prop]] = arr[i];
    }
 
    for (i in lookup) {
        new_arr.push(lookup[i]);
    }
 
    return new_arr;
}