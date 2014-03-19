var path;
var musicpath;
var index;
var curmemorynum=0;
var prevmemorynum=0;
var final=false;
var activityToVerb;
var wordToPlural;
var music = {'pleased':"music/island.m4a",'excited':"music/peach.m4a",'content':"music/swing.m4a",'aroused':"music/bbq.m4a",'sleepy':"music/lazyday.m4a",'depressed':"music/minuet.mp3",'miserable':"music/acoustic.m4a",'distressed':"music/acoustic.m4a",'neutral':"music/acoustic.m4a"};

$wait = jQuery('#wait');
$locationBar = jQuery('#locationBar');


jQuery(document).ready(function(){
	  var availableLocations = [
      "Miami Beach, FL, United States",
      "Universal Studios Orlando, Universal Boulevard, Orlando, FL, United States",
      "Fort Canning Park Singapore",
      "Arab Street Singapore",
      "Dubai - United Arab Emirates",
      "Phnom Penh, Cambodia",
      "Fujairah - United Arab Emirates",
      "Cold Spring, NY, United States",
      "Jurong West, Singapore",
      "Jurong West Community Hall, Singapore",
      "Hougang, Singapore",
      "Bugis, Singapore",
      "Nanyang Technological University, Singapore",
      "Holland Village, Singapore",
      "Toh Payoh, Singapore",
      "Jurong West Church, Singapore",
      "Macritchie Reservoir, Singapore",
      "Botanical Gardens, Singapore",
      "Raffles Place, Singapore",
      "Changi Business Park, Singapore"
     ];
	  var availableMoods = [
      "happy",
      "sad",
      "excited",
    ];
    var availablePeople = [
      "friends",
      "acquaintances",
      "classmates",
      "family",
      "neighbours",
    ];
    var availableActivities = [
      "lunch",
      "dinner",
      "travel",
      "picnic",
      "shopping",
      "nightout",
      "sports",
      "work",
      "gathering",
      "party",
      "wedding"
    ];
    activityToVerb = {
    lunch:'having lunch',
    dinner:'having dinner',
    travel:'travelling',
    picnic:'on a picnic',
    shopping:'shopping',
    nightout:'on a night-out',
    sports:'playing sports',
    work:'at work',
    gathering:'at a gathering',
    party:'at a party',
    wedding:'at a wedding'
    };
    wordToPlural = {
    lunch:'lunches',
    dinner:'dinners',
    travel:'travels',
    picnic:'picnics',
    shopping:'shopping',
    nightout:'nightouts',
    sports:'playing sports',
    work:'work',
    gathering:'gatherings',
    party:'parties',
    wedding:'weddings'
    };

    jQuery( "#emotion" ).autocomplete({
      source: availableMoods
    });
        jQuery( "#emotion" ).autocomplete({
      source: availableMoods
    });

    jQuery( "#people" ).autocomplete({
      source: availablePeople
    });
     jQuery( "#activity" ).autocomplete({
      source: availableActivities
    });
     jQuery( "#location" ).autocomplete({
      source: availableLocations
    });
	//document.body.style.background = "url('images/bg.jpg')";
	jQuery.backstretch("images/garfield-interior.jpg");
});

function showBar(playbackArray, k, oneormany) {
	curmemorynum=0;
	moodList=playbackArray[k].mood.split(',');
	jQuery("#form-wrapper").hide();
	locMin = playbackArray[k].location.split(',');
	var pics;
	audio = new Audio(music[playbackArray[k].mood.toLowerCase()]);
	pics="<div onclick=\"javascript:playSlideshow(audio);\" id=\"playOverlay\" style=\"height: 480px; position: absolute; top: 0px; left: 0px; background-image: url('images/overlay.png'); z-index: 102; cursor: pointer;\"></div>";
	pics += "<div id=\"slider\" class='no-flick' onclick=\"javascript:playSlideshow(audio);\">";
	var activity=document.getElementById("activity").value;
	if (activity!="")
	{
	pics+="<img name=-1 src=\"http://placehold.it/980x500&text=memories+of+"+wordToPlural[activity]+"\">";
	}
	jQuery('#slider').css({
	'position':'relative',
	'background':'url(images/loading.gif) no-repeat 50% 50%',
	'z-index':'1',
	'-webkit-transform': 'translateZ(0)',
	'-webkit-backface-visibility':'hidden'
	});
	if (oneormany==1)
		loopMax=1;
	else
		loopMax=playbackArray.length;
	for (j=0;j<loopMax;j++)
	{
	
		var relatedBar = "<div class=\"related-divs\" id=\"related-"+playbackArray[j].location.replace(/\s+/g,'').replace(/\,/g,'')+"\"><span class=\"relatedimage\">"+"<img src=\""+playbackArray[j].path+"0.jpg\"></span><span class=\"relatedcaption\">"+playbackArray[j].location+"</span></div>"	
		jQuery("#related").append(relatedBar);
		var actlist = playbackArray[j].activity.split(',');
		var moodlist = playbackArray[j].mood.split(',');
		for (i=0;i<=playbackArray[j].num;i++)
			{
				if (activity==actlist[i] || activity=="")
					{
					playbackArray[j].max=i;
					if (actlist[i]!='-')
						{
						if (moodlist[i]!='-')
						pics+="<img title=\""+activityToVerb[actlist[i]]+". Feeling "+moodlist[i]+"\" width=980px height=400px name="+i+" src=\""+playbackArray[j].path+i+".jpg\" alt=\"\" />";
						else
						pics+="<img title=\""+activityToVerb[actlist[i]]+"\" width=980px height=400px name="+i+" src=\""+playbackArray[j].path+i+".jpg\" alt=\"\" />";
						}	
	
					else
						{
						if (moodlist[i]!='-')
						pics+="<img title=\"Feeling "+moodlist[i]+"\" width=980px height=400px name="+i+" src=\""+playbackArray[j].path+i+".jpg\" alt=\"\" />";
						else
						pics+="<img width=980px height=400px name="+i+" src=\""+playbackArray[j].path+i+".jpg\" alt=\"\" />";
						}
					}
			}
	}
	pics+="</div>";
	jQuery('div.related-divs').foggy();
	jQuery("#related-"+playbackArray[curmemorynum].location.replace(/\s+/g,'').replace(/\,/g,'')).foggy(false);
	//jQuery("#map").html("<img src=\""+playbackArray[k].path+"/0.jpg\">");
	jQuery("#map").html(pics);
	window.myFlux = new flux.slider('#slider',{
        autoplay: false,
        transitions: ['dissolve'],
        captions: true,
        delay: 4000,
        onTransitionEnd: function(data) {
        var img = data.currentImage;
        var cur = img.name;
        final=false;
        if (img.name==playbackArray[curmemorynum].max && img.name!=-1)
 		{	prevmemorynum=curmemorynum;	 
 			curmemorynum++;
 			final=true;
 			if (img.name==-1)
 				{curmemorynum=0;
 				}
 			if (curmemorynum==playbackArray.length)
 				{curmemorynum=0;
 				}
 			moodListtemp=playbackArray[curmemorynum].mood.split(',');
 			if (playbackArray[prevmemorynum].mood != playbackArray[curmemorynum].mood)
			{audio.pause();
			audio = new Audio(music[playbackArray[curmemorynum].mood.toLowerCase()]);
			audio.play();}
			jQuery("#destination").fadeOut('slow');
		}
		if(img.name!=playbackArray[curmemorynum].max && !final)
		{
			jQuery('div.related-divs').foggy();
			jQuery("#related-"+playbackArray[curmemorynum].location.replace(/\s+/g,'').replace(/\,/g,'')).foggy(false);
			locMin = playbackArray[curmemorynum].location.split(',');
			jQuery("#destination").html("<div target='#' title='VISIT THE WEBSITE'>You were at " + locMin[0] + " with "+ playbackArray[curmemorynum].people+" on "+playbackArray[curmemorynum].date);
 			jQuery("#destination").fadeIn('slow');
		}

	}
	});

	$wait.fadeOut("slow",function(){
	document.getElementById("map").style.visibility="visible";
	jQuery("#destination").html("<div target='#' title='VISIT THE WEBSITE'>You were at " + locMin[0] + " with "+ playbackArray[k].people+" on "+playbackArray[k].date);
	jQuery("#related").css("visibility","visible");
	});
}


function codeAddress(elem) {

    //document.body.style.background = "";
    //document.body.style.backgroundColor = "#FFF";
	$wait.fadeIn(function(){
	var filledFields = new Array();
	var location = document.getElementById("location").value;
	var locationWeight = document.getElementById("locationWeight").value;
	var date=document.getElementById("datepicker").value;
	var dateWeight = document.getElementById("dateWeight").value;
	var people = document.getElementById("people").value;
	var peopleWeight = document.getElementById("peopleWeight").value;
	var mood = document.getElementById("emotion").value;
	var moodWeight = document.getElementById("moodWeight").value;
	var activity = document.getElementById("activity").value;
	var activityWeight = document.getElementById("activityWeight").value;
	if (date=="")
		dateWeight=0;
	if (people=="")
		peopleWeight=0;
	if (mood=="")
		moodWeight=0;
	if (activity=="")
		activityWeight=0;
	var playbackArray = new Array();
	var index=0;
	var score=0;
	var greatestScore=0;
	for (i = 0; i < memories.length; ++i) {	
    if(location.toLowerCase()==memories[i].location.toLowerCase())
	{		

	score+=parseInt(locationWeight);
	//console.log("Score of "+memories[i].location+"after loc is "+score);
	}
	if(memories[i].date.toLowerCase().indexOf(date.toLowerCase()) != -1 && date != "")
	{

	score+=parseInt(dateWeight);
	//	console.log("Score of "+memories[i].location+"after date is "+score);

	}

	if(memories[i].people.toLowerCase().indexOf(people.toLowerCase()) != -1 && people != "")
	{	

	score+=parseInt(peopleWeight);
	//	console.log("Score of "+memories[i].location+"after people is "+score);

	}
	if(memories[i].mood.toLowerCase().indexOf(mood.toLowerCase())!=-1 && mood!="")
	{
	
	score+=parseInt(moodWeight);
	console.log("Score of "+memories[i].location+"after mood is "+score);

	}
	if(memories[i].activity.toLowerCase().indexOf(activity.toLowerCase()) != -1 && activity != "")
	{	
		//console.log(activity.toLowerCase()+"-"+memories[i].activity.toLowerCase());
		score+=parseInt(activityWeight);
		console.log("Score of "+memories[i].location+"after activity is "+score);

	}

	if(memories[i].activity.toLowerCase().indexOf(activity) == -1 && activity != "")
	{
	console.log('activity is: '+activity);
	score=0;
	}
	//console.log("Score of "+memories[i].location+" is "+score);
	if (score>greatestScore)
	{ greatestScore = score;
	if (index!=0)
	{
	var temp=playbackArray[0];
	playbackArray[0]=memories[i];
	playbackArray[index]=temp;
	score=0;
	}
	else {playbackArray[0]=memories[i];score=0
	index++;
	continue;
	}
	}
	if (score>0)
	{
	playbackArray[index]=memories[i];
	index++;
	}
	score=0;

	}
	//console.log(playbackArray);
	if (index>0)
	{$locationBar.fadeOut();
	if (document.getElementById('wander').checked)
	showBar(playbackArray,0,0);
	else showBar(playbackArray,0,1);
	}
	else
	{
	$wait.hide();
	$locationBar.fadeIn();	
	jQuery('.message').hide().html("No Memories Found!").fadeIn();
}
});
}

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

var x=false;
function playSlideshow(audio)
{
	if (window.myFlux.isPlaying())
		{
		audio.pause()
		window.myFlux.stop();
		jQuery("#playOverlay").show();
		}
	else
		{audio.play();
		window.myFlux.start();	
		jQuery("#playOverlay").hide();
		}	
}
