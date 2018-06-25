package com.guoanshequ.eprj.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.guoanshequ.eprj.config.EprjConfig;

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

@RestController
public class MsgSenderController {

	@Autowired
	private  HttpServletRequest request;
	
	@Autowired
	private EprjConfig eprjConfig;

	static Logger logger = LoggerFactory.getLogger(MsgSenderController.class);
	
	//static String url = System.getProperty("user.dir");
	//static File file = new File(url+File.separator+".."+File.separator+"sms_counter.log");
	static File file = null;
	
	@RequestMapping("/dispatcher.action")
	public String sendRequest(@RequestParam("phone") String phoneNumber,
			@RequestParam("sendcode") String messageContent) {
		
		if (file == null) {
		    file = new File(request.getServletContext().getRealPath("/") + File.separator+".." + File.separator+"sms-counter.log");
		}
		
		System.out.println("-------------"+request.getServletContext().getRealPath("/") + File.separator+".." + File.separator+"sms-counter.log");
		
		logger.info(String.format("\nmessage detail:\nphone:\t%s\nmessage:\t%s\n", phoneNumber, messageContent));

		String resultString = null;
		
		//if (Arrays.asList(eprjConfig.getPermitClients()).contains(request.getRemoteAddr())) {
			try {
				final String requestUrlTpl = "http://q.hl95.com:8061/?username=gasjyz&password=Gasj0121&message=%s&phone=%s&epid=123743&linkid=&subcode=";
				String messageContent_gb2312 = URLEncoder.encode(messageContent, "gb2312");
				CloseableHttpClient httpclient = HttpClients.createDefault();
				HttpGet httpGet = new HttpGet(String.format(requestUrlTpl, messageContent_gb2312, phoneNumber));
				CloseableHttpResponse response = httpclient.execute(httpGet);
				resultString = EntityUtils.toString(response.getEntity(), "utf-8");
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"\t"+phoneNumber+"\t"+resultString+"\t"+messageContent+System.getProperty("line.separator");
				logger.info(resultString);
				response.close();
				FileUtils.writeStringToFile(file, date, Charset.defaultCharset(), true);
			} catch (Exception e) {
				e.printStackTrace();
				resultString = "Error:" + e.getMessage();
			}
		    //} else {
			//	resultString = "access forbidden";
			//}

		return resultString;
	}

}
