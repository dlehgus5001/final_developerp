package com.dev.erp.messenger.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.dev.erp.common.util.Utils;
import com.dev.erp.member.model.vo.Member;
import com.dev.erp.messenger.model.service.MessengerService;
import com.dev.erp.messenger.model.vo.ChatRoom;
import com.dev.erp.messenger.model.vo.Msg;

@Controller
public class MessengerController {

static final Logger logger = LoggerFactory.getLogger(MessengerController.class);
	
	@Autowired
	MessengerService messengerService;
	
	/**
	 * 인자로 전달될 길이의 임의의 문자열을 생성하는 메소드
	 * 영대소문자/숫자의 혼합.
	 * 
	 * @param len
	 * @return
	 */
	private String getRandomChatId(int len){
		Random rnd = new Random();
		StringBuffer buf =new StringBuffer();
		buf.append("chat_");
		for(int i=0;i<len;i++){
			//임의의 참거짓에 따라 참=>영대소문자, 거짓=> 숫자
		    if(rnd.nextBoolean()){
		    	boolean isCap = rnd.nextBoolean();
		        buf.append((char)((int)(rnd.nextInt(26))+(isCap?65:97)));
		    }
		    else{
		        buf.append((rnd.nextInt(10))); 
		    }
		}
		return buf.toString();
	}
	

	/**
	 * - @MessageMapping 을 통해 메세지를 받고,
	 * - @SendTo 를 통해 메세지 전달. 작성된 주소를 구독하고 있는 client에게 메세지 전송
	 * 
	 * @param fromMessage
	 * @return
	 */
	@MessageMapping("/hello")
	@SendTo("/hello")
	public Msg stomp(Msg fromMessage,
					 @Header("simpSessionId") String sessionId,//WesocketSessionId값을 가져옴.
					 SimpMessageHeaderAccessor headerAccessor//HttpSessionHandshakeInterceptor빈을 통해 httpSession의 속성에 접근 가능함.
					 ){
		logger.info("fromMessage={}",fromMessage);
		logger.info("@Header sessionId={}",sessionId);
		
		//httpSession속성 가져오기 테스트: 필요에 따라 httpSession속성을 사용할 수 있다. 
		String sessionIdFromHeaderAccessor = headerAccessor.getSessionId();//@Header sessionId와 동일
		Map<String,Object> httpSessionAttr = headerAccessor.getSessionAttributes();
		Member member = (Member)httpSessionAttr.get("memberLoggedIn");
		String httpSessionId = (String)httpSessionAttr.get("HTTP.SESSION.ID");//비회원인 경우 memberId로 사용함.
		logger.info("sessionIdFromHeaderAccessor={}",sessionIdFromHeaderAccessor);
		logger.info("httpSessionAttr={}",httpSessionAttr);
		logger.info("httpSessionId={}",httpSessionId);
		logger.info("memberLoggedIn={}",member);
		
		return fromMessage; 
	}
	
	@MessageMapping("/chat/{chatId}")
	@SendTo(value={"/chat/{chatId}"})
	public Msg sendEcho(Msg fromMessage, 
						@DestinationVariable String chatId, 
						@Header("simpSessionId") String sessionId){
		logger.info("fromMessage={}",fromMessage);
		logger.info("chatId={}",chatId);
		logger.info("sessionId={}",sessionId);
		messengerService.insertChatLog(fromMessage);

		return fromMessage; 
	}
	

	/**
	 * 읽음 여부 확인을 위해 최종 focus된 시각정보를 수집한다.
	 * 
	 * @param fromMessage
	 * @return
	 */
	@MessageMapping("/lastCheck")
	@SendTo(value={"/chat/{chatId}"})
	public Msg lastCheck(@RequestBody Msg fromMessage){
		logger.info("fromMessage={}",fromMessage);
		
		messengerService.updateLastCheck(fromMessage);
		
		return fromMessage; 
	}
	@RequestMapping("messenger/messengerSelectList.do")
	public ModelAndView messengerSelectList(ModelAndView mav) {
		mav.setViewName("messenger/messengerSelectList");
		return mav;
	}
	
	
	@GetMapping("/messenger/messengerList.do")
	public void messengerList(Model model, 
					  HttpSession session, 
					  @SessionAttribute(value="memberLoggedIn", required=false) Member memberLoggedIn){
		String email = Optional.ofNullable(memberLoggedIn).map(Member::getEmail)
															 .orElseThrow(IllegalStateException::new);
		List<Map<String,String>> chatIdList = messengerService.findChatIdList(email);
		List<Map<String, String>> recentList =new ArrayList<>();
		List<Map<String, String>> sumList =new ArrayList<>();
		for(int i=0; i<chatIdList.size(); i++) {
			String chatId = ((Map<String,String>)chatIdList.get(i)).get("CHATID");
			Map<String,String> param = new HashMap<>();
			param.put("email", email);
			param.put("chatId", chatId);
			recentList = messengerService.findRecentList(param);
			sumList.add(recentList.get(0));
		}
		logger.info("recentList={}",recentList);
		model.addAttribute("recentList", sumList);
		
	}
	
	@GetMapping("/messenger/{chatId}/messengerChat.do")
	public String messengerChat(@PathVariable("chatId") String chatId, Model model){
		List<Msg> chatList = messengerService.findChatListByChatId(chatId);
		model.addAttribute("chatList", chatList);
		
		logger.info("chatList={}",chatList);
		return "messenger/messengerChat";
	}
	
	@RequestMapping("/messenger/messengerListPage.do")
	@ResponseBody
	public Map<String,Object> messengerListPage(@RequestParam(defaultValue="1") int cPage,  HttpServletResponse rexsponse) {
		
		
		List<Map<String,String>> list = new ArrayList<>();
		final int numPerPage = 7;
		int totalContents = 0;
		
		list = messengerService.selectMemberList(cPage,numPerPage);  
		logger.debug("list={}",list);
		totalContents = messengerService.selectAllCountByAccountNo(); 
		String url = "messengerListPage.do?";
		String pageBar = Utils.getPageBar(totalContents, cPage, numPerPage, url);
		Map<String,Object> map = new HashMap<>();
		map.put("numPerPage",numPerPage);
		map.put("cPage",cPage);
		map.put("totalContents",totalContents);
		map.put("list",list);
		map.put("pageBar", pageBar);
		return map;
	}
	@RequestMapping("/messenger/makeChatRoom.do")
	@ResponseBody
	public Map<String,Object> makeChatRoom(@RequestParam("email") String email,
									HttpSession session, HttpServletResponse rexsponse, 
			  @SessionAttribute(value="memberLoggedIn", required=false) Member memberLoggedIn) {
		
		
		String sessionEmail = Optional.ofNullable(memberLoggedIn).map(Member::getEmail)
				 .orElseThrow(IllegalStateException::new);
		String chatId=null;
		chatId = getRandomChatId(15);//chat_randomToken -> jdbcType=char(20byte)
		
		List<ChatRoom> list = new ArrayList<>();
		list.add(new ChatRoom(chatId, email, 0, "Y", null, null));
		list.add(new ChatRoom(chatId, sessionEmail,0, "Y", null, null));
		messengerService.insertChatRoom(list);
		Map<String,Object> map = new HashMap<>();
		map.put("chatId",chatId);
		return map;
	}
	
	
	
	
	
	
	
	
	
	
}