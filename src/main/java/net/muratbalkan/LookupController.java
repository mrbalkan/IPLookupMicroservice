package net.muratbalkan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mrbalkan
 *
 */
@RestController
public class LookupController {
	/*
	 * @Value("${api_key}") private static String
	 * API_KEY="";
	 * 
	 * @Value("${remote_url}") private static String
	 * API_URL="https://api.ipgeolocation.io/ipgeo";
	 */
	Logger logger = LoggerFactory.getLogger(LookupController.class);

	@Value("${api_key:key1}")
	private String API_KEY;
	@Value("${remote_url:localhost}")
	private String API_URL;

	@PostMapping("/lookup")
	public LookupModel lookup(@RequestParam(value="ip", defaultValue="1.1.1.1") String ip) throws Exception {
		String remoteurl=API_URL + "?apiKey="+ API_KEY+"&ip=" + ip;
		logger.info("Looking up IP: " + ip);
		URL url = null;
		try {
			url = new URL(remoteurl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception(e);
		}
		HttpURLConnection con;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			try{in.close();}catch(IOException e) {e.printStackTrace();}
			ObjectMapper objectMapper = new ObjectMapper();
			LookupModel model = objectMapper.readValue(content.toString(),LookupModel.class);
			return model;
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw (e);
		}
	}

	@GetMapping("/")
	public String defaultMapping() throws Exception {
		return "OK";
	}
}
