package com.guoanshequ.eprj.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guoanshequ.eprj.config.EprjConfig;
import com.guoanshequ.eprj.util.HttpClientUtil;

@RestController
public class MsgReceiveController {
	@Autowired
	private  HttpServletRequest request;
	
	@Autowired
	private EprjConfig eprjConfig;

	static Logger logger = LoggerFactory.getLogger(MsgSenderController.class);
	
	//static String url = System.getProperty("user.dir");
	//static File file = new File(url+File.separator+".."+File.separator+"sms_counter.log");
	static File file = null;
	
	@RequestMapping("/replyMessage.action")
	public String sendRequest(@RequestParam("phone") String phoneNumber,
			@RequestParam("msgContent") String messageContent,@RequestParam("spNumber") String spNumber) {
		
		if (file == null) {
		    file = new File(request.getServletContext().getRealPath("/") + File.separator+".." + File.separator+"sms-receive.log");
		}
		
		System.out.println("-------------"+request.getServletContext().getRealPath("/") + File.separator+".." + File.separator+"sms-receive.log");
		
		logger.info(String.format("\nmessage detail:\nphone:\t%s\nmessage:\t%s\n", phoneNumber, messageContent));

		String resultString = null;
		String date="";
//		if (Arrays.asList(eprjConfig.getPermitClients()).contains(request.getRemoteAddr())) {
			try {
				String url  = eprjConfig.getDaqWebAddress()+"/replyMessage.action?phone="+phoneNumber+"&msgContent="+messageContent+"&spNumber="+spNumber;
				resultString = HttpClientUtil.doGet(url);
				date = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"\t"+phoneNumber+"\t"+resultString+"\t"+messageContent+System.getProperty("line.separator")+"success";
				FileUtils.writeStringToFile(file, date, Charset.defaultCharset(), true);
			} catch (Exception e) {
				e.printStackTrace();
				resultString = "<!DOCTYPE html><html><head><meta charset='utf-8'><title>result</title></head><body><h1>"+e.getMessage()+"</h1></body></html>";
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"\t"+phoneNumber+"\t"+resultString+"\t"+messageContent+System.getProperty("line.separator")+"\t"+"fail";
					FileUtils.writeStringToFile(file, date, Charset.defaultCharset(), true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
//		} else {
//			resultString = "access forbidden";
//		}

		return resultString;
	}

}