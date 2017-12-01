<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>INFOBUS</title>

<!-- Bootstrap -->
<link href="css/bootstrap.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/custom.css">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body onload="startTime();">
	<div class="container-fluid">
		<div class="">
			<img src="images/INFOBUSHIGH.PNG" alt="infobuslogo"
				class="infobus-logo">
			<video class="videoplayer" id="videoplayer"></video>
			<div class="text-box" id="txtbox">
				<iframe class="addblock" id="objecttag" src=""> </iframe>
			</div>
		</div>
	</div>
	<nav class="navbar navbar-color navbar-fixed-bottom">
		<marquee class="marquee"></marquee>
		<div class="time" id="time"></div>
	</nav>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.js"></script>
	<script type="text/javascript">
		var syncTime;
		var vidstTime;
        function startTime() {   
            var today = new Date();
            var h = today.getHours();
            var m = today.getMinutes();
            var s = today.getSeconds();
            m = checkTime(m);
            s = checkTime(s);
            var currenttime=h + ":" + m + ":" + s;
			if(currenttime == syncTime){
				console.log("syncTime:"+syncTime);
                window.location.reload();
            }
            document.getElementById('time').innerHTML =h + ":" + m + ":" + s;
            var t = setTimeout(startTime, 500);
        }

        function checkTime(i) {
            if (i < 10) {
                i = "0" + i;
            }; // add zero in front of numbers < 10
            return i;
        }
        function getCurrentTime(){
        	var today = new Date();
            return (today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
        }
    </script>
	<script type="text/javascript">
     $(document).ready(function($){
        //ajax code for getting service data
       $.ajax({
		type : "GET",
		contentType : "application/json",
		url : "<%=request.getContextPath()+"/load/vertical" %>",
		dataType : 'json',
		timeout : 10000,
		success : function( result ){
			//console.log("SUCCESS: ", result);
			//console.log("url::",result.videodata.url);
			//Set Scroll text from result
            $(".marquee").html(result.videodata.scrolltext);
            //refresh
            syncTime = result.videodata.syncTime;
            function refreshMe(){
	            if(currenttime == result.videodata.syncTime){
	                window.location.reload();
	            }
			}
			//load video data from result into video list
            var video_list=[];
            var videolist=result.videodata.videos;
            $.each(videolist,function(key,val){
                $.each(val,function(key,val){                
                video_list.push(val);
                });
            }); //done
            console.log("video_list",video_list,result.videodata.scrolltext);            
          //load video data from result into video list
            var sponser_list=[];	
            var sponserJson=result.videodata.sponsers;
            $.each(sponserJson,function(key,val){
                $.each(val,function(key,val){                
                	sponser_list.push(val);
                });
            }); //done
            console.log("sponser_list",sponser_list);           
              
            var video_index = 0;
            var video_player = null;
            //when load page call for playing video
            function onload() {          
              //  console.log("body loaded");
                video_player = document.getElementById("videoplayer");
                //set video 
                video_player.setAttribute("src", video_list[video_index]);
                //set timeout
                setTimeout(function(){video_player.play();},1000);
                console.log("url:"+result.videodata.url);
                //set dreampstep url
                $("#objecttag").attr('src',result.videodata.url);
            }
            //when video has error skip to next one
            document.getElementById('videoplayer').addEventListener(
               'error',
               function(e) {
                   switch (e.target.error.code) {
                   case e.target.error.MEDIA_ERR_ABORTED:
                       console.log('You aborted the video playback.');
                       break;
                   case e.target.error.MEDIA_ERR_NETWORK:
                       console.log('A network error caused the video download to fail part-way.');
                       break;
                   case e.target.error.MEDIA_ERR_DECODE:
                       console.log('The video playback was aborted due to a corruption problem or because the video used features your browser did not support.');
                       break;
                   case e.target.error.MEDIA_ERR_SRC_NOT_SUPPORTED:
                       console.log('The video could not be loaded, either because the server or network failed or because the format is not supported.');
                       break;
                   default:
                       console.log('An unknown error occurred.');
                       break;
                   }
                   playnext('error');
               }, false); 
			
            // log time when start play
            document.getElementById('videoplayer').addEventListener('play',
                    function() {
                		vidstTime = getCurrentTime();                
                    }, false);
			
            //when video ended play next one
            document.getElementById('videoplayer').addEventListener('ended',
                    function() {
                        playnext('success');
                    }, false);
			
			//playnext function
			function playnext(param){
                //console.log("video ended");
                var today = new Date();
                var data = sponser_list[video_index]+","+video_list[video_index]+","+vidstTime+" to "+getCurrentTime()+",Play - "+param;
                console.log("audit::"+data);
                $.ajax({
        			type : "POST",
        			contentType : "application/json",
        			url : "<%=request.getContextPath()+"/logPlayAudit" %>",
        			data : data,
        			dataType : 'json',
        			timeout : 100000,
        			success : function(data) {
        				//console.log("SUCCESS: ", data);
        			},
        			error : function(e) {
        				console.log("ERROR: ", e);
        			},
        			done : function(e) {
        				//console.log("DONE");
        			}
        		});
                if (video_index < video_list.length - 1) {
                    video_index++;
                    $("#txtbox").css("z-index", "2");
                    $("#objecttag").css("height", $(window).height() - 5);
                    $("#videoplayer").css("z-index", "1");
                    video_player.setAttribute("src", video_list[video_index]);
                    if (param == 'success') {
                        setTimeout(function() {
                            $("#txtbox").css("z-index", "1");
                            $("#videoplayer").css("z-index", "2");                  
                            video_player.play();
                        }, 60000);
                    } else {
                        setTimeout(function() {
                            $("#txtbox").css("z-index", "1");
                            $("#videoplayer").css("z-index", "2");
                            video_player.play();
                        }, 60000);
                    }
                } else {
                    video_index = 0;
                    onload();
                }
            }//end of playnext
			
          	//when start video call for bus data
            document.getElementById('videoplayer').addEventListener(
                    'loadedmetadata',
                    function() {
                        $("#objecttag").attr('src',result.videodata.url);
                    }, false);

			//when start call onload
            onload();
        
    	}, //end of sucess
		error : function(e) {
			console.log("ERROR: ", e);
		},//end of error
		done : function(e) {
			//console.log("DONE");
		}
		});//end of ajax call       
     });//onload reload
 </script>
</body>
</html>