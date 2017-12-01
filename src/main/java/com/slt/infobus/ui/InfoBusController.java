package com.slt.infobus.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.slt.infobus.common.IBConstants;
import com.slt.infobus.common.IBPlayAudit;
import com.slt.infobus.entity.IBMsgContentDtl;
import com.slt.infobus.entity.IBVideoSlotDtl;
import com.slt.infobus.service.InfoBusService;


@Controller
public class InfoBusController {
	 private final Logger logger = LoggerFactory.getLogger(InfoBusController.class);
	 @Autowired
	 InfoBusService service;
	 	 
	 @Autowired
	 IBPlayAudit playAudit;
	 
	 @RequestMapping(value="/play", method=RequestMethod.GET)
	 public String play(){
		 logger.info("play.jsp returning..");
		 return "play";
	 }
	 
	 @RequestMapping(value="/infoplay", method=RequestMethod.GET)
	 public String infoPlay(){
		 logger.info("infoplay.jsp returning..");
		 return "infoPlay";
	 }
	 
	 @RequestMapping(value="/display", method=RequestMethod.GET)
	 public String display(){
		 logger.info("display.jsp returning..");
		 return "display";
	 }
	 
	 @RequestMapping(value="/infodisplay", method=RequestMethod.GET)
	 public String infoDisplay(){
		 logger.info("infoDisplay.jsp returning..");
		 return "infoDisplay";
	 }
	 
	 @SuppressWarnings("unchecked")
	 @RequestMapping(value="/load/horizontal", method=RequestMethod.GET)
	 public @ResponseBody String loadDataToPlay(){
		 JSONObject respJson = new JSONObject();
		 respJson.put("videodata", processRequest(IBConstants.IB_HORIZONTAL));
		 logger.info("VideoData::"+respJson.toJSONString());
		 return respJson.toJSONString();
	 }
	
	 @SuppressWarnings("unchecked")
	 @RequestMapping(value="/load/vertical", method=RequestMethod.GET)
	 public @ResponseBody String loadVerticalDataToPlay(){  
		 JSONObject respJson = new JSONObject();
		 respJson.put("videodata", processRequest(IBConstants.IB_VERTICAL));
		 logger.info("VideoData::"+respJson.toJSONString());		 
		 return respJson.toJSONString();
	 }
	 
	 @RequestMapping(value="/logPlayAudit", method=RequestMethod.POST)
	 public @ResponseBody String logPlayAudit(@RequestBody String auditLog){
		 JSONParser parser = new JSONParser();
		 JSONObject respJson = new JSONObject();		 
		 if(auditLog!=null){
			 //logger.info("auditLog::"+auditLog);
			 playAudit.playAuditLog(auditLog); 
			 respJson.put("Status", "OK");
		 }
		 return respJson.toJSONString();
	 }
	 
	 @SuppressWarnings("unchecked")	 
	 private JSONObject processRequest(String or){
		 JSONObject respJson = new JSONObject();
		 try{
			 List<IBVideoSlotDtl> videos = service.getSlotList();		 
			 List<IBMsgContentDtl> msgData = service.getMsgList();
			 String videosPath= service.getVideosPath();
			 String url = service.getDreamStepUrl();
			 
			 //get video info
			 ArrayList<JSONArray> jsonArrays = getVideoDetails(or);
			 JSONArray vidContent = jsonArrays.get(0);
			 JSONArray sponserContent = jsonArrays.get(1);
			 
			 //System.out.println("vidcontent::"+vidContent.toJSONString());
			 //System.out.println("Sponsercontent::"+sponserContent.toJSONString());
			 StringBuffer msgBuffer = new StringBuffer();
			 for (IBMsgContentDtl msgContent : msgData) {
				 msgBuffer.append(msgContent.getMsgContent());
				 msgBuffer.append("&nbsp &nbsp &nbsp");
			 }
			 //System.out.println("You are successfully connect and data ::"+videos.size()+", msg:"+msgData.size()+", bus:"+busData.size()+", vidPath:"+videosPath+", url:"+url);
			 logger.info("You are successfully connect and data ::"+videos.size()+", msg:"+msgBuffer.toString()+", vidPath:"+videosPath+", url:"+url);
			 
			 String time = service.getStartedDate().getHours()+":"+(service.getStartedDate().getMinutes())+":"+service.getStartedDate().getSeconds();
			 
 	         respJson.put("scrolltext",msgBuffer.toString());
		     respJson.put("videos", vidContent);
		     respJson.put("sponsers", sponserContent);		     
		     respJson.put("url", url);
		     respJson.put("syncTime",time);
		     
			 logger.info("Server Started Time::"+time);
			 logger.info("Screen Sync Time::"+time);
			 
		 }catch(Exception ex){
			 logger.error("Error while loading data to play::",ex);
		 }
		 return respJson;
	 }
 
	 @SuppressWarnings("unchecked")
	 private ArrayList<JSONArray> getVideoDetails(String or) throws Exception{
		 List<IBVideoSlotDtl> videos = service.getSlotList();
		 String videosPath= service.getVideosPath();
		 //System.out.println("You are successfully connect and data ::"+videos.size()+", vidPath:"+videosPath);
		 logger.info("You are successfully connect and data ::"+videos.size()+", vidPath:"+videosPath);
		 JSONArray videoArray = new JSONArray();
		 JSONArray sponserArray = new JSONArray();
		 JSONObject sponserObj = null;
		 JSONObject vidObj = null;
		 
		 int i=1;
		 for (IBVideoSlotDtl slot : videos) {			 
			 if(IBConstants.IB_VERTICAL.equalsIgnoreCase(or) && 
					 (slot.getVideoDtl().getOrientation().equalsIgnoreCase(IBConstants.IB_VERTICAL))){
				 vidObj = new JSONObject();
				 vidObj.put("v"+i,videosPath+slot.getVideoDtl().getVideoName());
				 sponserObj = new JSONObject();				 
				 sponserObj.put("s"+i,slot.getVideoDtl().getSponser().getName());				 
				 ++i;
				 videoArray.add(vidObj);
				 sponserArray.add(sponserObj);
			 }else if(IBConstants.IB_HORIZONTAL.equalsIgnoreCase(or) && 
					 (slot.getVideoDtl().getOrientation().equalsIgnoreCase(IBConstants.IB_HORIZONTAL))){
				 vidObj = new JSONObject();	
				 vidObj.put("v"+i,videosPath+slot.getVideoDtl().getVideoName());
				 sponserObj = new JSONObject();				 
				 sponserObj.put("s"+i,slot.getVideoDtl().getSponser().getName());
				 ++i;
				 videoArray.add(vidObj);
				 sponserArray.add(sponserObj);
			 }
		 }//for
		 ArrayList<JSONArray> jsonList = new ArrayList<JSONArray>();
		 jsonList.add(videoArray);
		 jsonList.add(sponserArray);
		 
		 return jsonList;
	 } 	 
}
