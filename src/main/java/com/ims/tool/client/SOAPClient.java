package com.ims.tool.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.io.IOUtils;

import com.ims.tool.domain.ToolConfig.ConfigParams;
import com.ims.tool.util.ResourcePaths;
import com.ims.tool.util.SetupUtil;

public final class SOAPClient {

	private static final Logger logger = Logger.getLogger(SOAPClient.class.getName());

	private static final Charset CHARSET = StandardCharsets.UTF_8;

	private static final String CONTENT_TYPE = "text/xml";

	private static final int TIMEOUT = 2000;

	private long lastRequestTime;

	public Response executeRequest(String envelop)
			throws ConnectTimeoutException {
		Response logInResponse = login();

		if (!logInResponse.getSessionId().isPresent()) {
			logger.log(Level.SEVERE, "Failed to login.");
			return logInResponse;
		}

		String sessionId = logInResponse.getSessionId().get();
		logger.log(Level.INFO, "Retrieved session id : " + sessionId);
		envelop = amendSessionId(envelop, sessionId);
		List<String> params = SetupUtil.getParameterConfig().getParamNames();
		for (String param : params) {
			envelop = envelop.replace(String.format("${%s}", param), SetupUtil.getParameterConfig().getValue(param));
		}

		long start = System.currentTimeMillis();
		Response response = call(envelop);
		long end = System.currentTimeMillis();
		Response logOutResponse = logout(sessionId);
		lastRequestTime = end - start;

		if (logOutResponse.getHttpStatus() != HttpStatus.SC_OK) {
			return logOutResponse;
		}
		return response;
	}

	public long getLastRequestTime() {
		return lastRequestTime;
	}

	private Response call(String envelope) throws ConnectTimeoutException {
		HttpClient httpClient = new HttpClient();
		HttpParams params = httpClient.getParams();
		params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, TIMEOUT);

		BufferedReader br = null;
		String url = SetupUtil.getToolConfig().get(ConfigParams.URL);

		PostMethod methodPost = new PostMethod(url);
		Response response = new Response();
		try {
			StringRequestEntity stringRequestEntity = new StringRequestEntity(envelope, CONTENT_TYPE, CHARSET.displayName());
			methodPost.setRequestEntity(stringRequestEntity);
			int httpStatusCode = httpClient.executeMethod(methodPost);
			logger.log(Level.INFO, "HTTP method return code : " + httpStatusCode);
			response.setResponseBody(methodPost.getResponseBodyAsString());
			response.setHttpStatus(httpStatusCode);

			if (httpStatusCode != HttpStatus.SC_OK) {
				String errorResponse = methodPost.getResponseBodyAsString();
				logger.log(Level.INFO, "Error in SOAP Response :\n" + errorResponse);
			}
		} catch (ConnectTimeoutException e) {
			throw e;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Problem in calling SOAP service.");
		} finally {
			methodPost.releaseConnection();
			if (br != null) {
				try {
					br.close();
				} catch (Exception fe) {
					fe.printStackTrace();
				}
			}
		}
		return response;
	}

	private Response login() throws ConnectTimeoutException {
		logger.log(Level.INFO, "Calling login service to get session id");
		String loginEnvelope = fileToString(ResourcePaths.LOGIN_ENVELOP_XML);
		loginEnvelope = loginEnvelope.replace("${USER_ID}", SetupUtil.getToolConfig().get(ConfigParams.USER));
		loginEnvelope = loginEnvelope.replace("${PASSWORD}", SetupUtil.getToolConfig().get(ConfigParams.PASS));
		Response response = call(loginEnvelope);

		if (response.getHttpStatus() == HttpStatus.SC_OK) {
			Pattern pattern = Pattern.compile("<sessionId>(.*?)</sessionId>");
			Matcher matcher = pattern.matcher(response.getResponseBody());
			if (matcher.find()) {
				String sessionId = matcher.group(1);
				response.setSessionId(sessionId);
			}
		}

		return response;
	}

	private Response logout(String sessionId) throws ConnectTimeoutException {
		logger.log(Level.INFO, "Calling Logout service for session id.." + sessionId);
		String logoutEnvelope = fileToString(ResourcePaths.LOGOUT_ENVELOPE_XML);
		return call(amendSessionId(logoutEnvelope, sessionId));
	}

	private String fileToString(String fileName) {
		InputStream stream = SOAPClient.class.getClassLoader().getResourceAsStream(fileName);
		try {
			return IOUtils.toString(stream, CHARSET);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Problem in reading file :" + fileName);
			logger.log(Level.INFO, "Failed to logout.");
		}
		return null;
	}

	private String amendSessionId(String envelop, String sessionId) {
		envelop = envelop.replaceAll("<cai3:SessionId>(.*?)</cai3:SessionId>", "<cai3:SessionId>" + sessionId + "</cai3:SessionId>");
		envelop = envelop.replaceAll("<cai3:sessionId>(.*?)</cai3:sessionId>", "<cai3:sessionId>" + sessionId + "</cai3:sessionId>");
		return envelop;
	}
}
