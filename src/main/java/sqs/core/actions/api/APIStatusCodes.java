package sqs.core.actions.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import sqs.core.actions.api.APITestRequests.StatusCode;

public class APIStatusCodes {
	private static String resultValue ="";
	private static String searchValue ="";
	
	private enum INFORMATIONAL{
		CONTINUE, SWITCHING_PROTOCOLS, PROCESSING;
		
		private static int getCodeNum(INFORMATIONAL code) {
			int codeValue=0;
			
			switch(code) {
				case CONTINUE:
					codeValue = 100;
					break;
				case SWITCHING_PROTOCOLS:
					codeValue = 101;
					break;
				case PROCESSING:
					codeValue = 102;
					break;
				default:
					codeValue = 100;
					break;
		}	
			return codeValue;
		}
		private static int getCodeNum(String code) {
			int codeValue= 0;
			INFORMATIONAL codeFromString = INFORMATIONAL.valueOf(code);
			codeValue=getCodeNum(codeFromString);
			
			return codeValue;
		}
	}
	
	private enum SUCCESS{
		OK, CREATED, ACCEPTED, NON_AUTHORITATIVE_INFORMATION, NO_CONTENT, RESET_CONTENT,
		PARTIAL_CONTENT, MULTI_STATUS, ALREADY_REPORTED, IM_USED;
		
		private static int getCodeNum(SUCCESS code) {
			int codeValue=0;
			
			switch(code) {
				case OK:
					codeValue = 200;
					break;
				case CREATED:
					codeValue = 201;
					break;
				case ACCEPTED:
					codeValue = 202;
					break;
				case NON_AUTHORITATIVE_INFORMATION:
					codeValue = 203;
					break;
				case NO_CONTENT:
					codeValue = 204;
					break;
				case RESET_CONTENT:
					codeValue = 205;
					break;
				case PARTIAL_CONTENT:
					codeValue = 206;
					break;
				case MULTI_STATUS:
					codeValue = 207;
					break;
				case ALREADY_REPORTED:
					codeValue = 208;
					break;
				case IM_USED:
					codeValue = 226;
					break;
				default:
					codeValue = 200;
					break;
		}	
			return codeValue;
		}
		
		private static int getCodeNum(String code) {
			int codeValue= 0;
			SUCCESS codeFromString = SUCCESS.valueOf(code);
			codeValue=getCodeNum(codeFromString);
			
			return codeValue;
		}
	}
		
	private enum REDIRECTION{
		MULTIPLE_CHOICES, MOVED_PERMANENTLY, FOUND, SEE_OHTER, NOT_MODIFIED, USE_PROXY, UNUSED, TEMPORARY_REDIRECT, PERMANENT_REDIRECT;
		private static int getCodeNum(REDIRECTION code) {
			int codeValue=0;
			
			switch(code) {
				case MULTIPLE_CHOICES:
					codeValue = 300;
					break;
				case MOVED_PERMANENTLY:
					codeValue = 301;
					break;
				case FOUND:
					codeValue = 302;
					break;
				case SEE_OHTER:
					codeValue = 303;
					break;
				case NOT_MODIFIED:
					codeValue = 304;
					break;
				case USE_PROXY:
					codeValue = 305;
					break;
				case UNUSED:
					codeValue = 306;
					break;
				case TEMPORARY_REDIRECT:
					codeValue = 307;
					break;
				case PERMANENT_REDIRECT:
					codeValue = 308;
					break;
				default:
					codeValue = 302;
					break;
		}	
			return codeValue;
		}
		private static int getCodeNum(String code) {
			int codeValue= 0;
			REDIRECTION codeFromString = REDIRECTION.valueOf(code);
			codeValue=getCodeNum(codeFromString);
			
			return codeValue;
		}
	}
	
	private enum CLIENT_ERROR{
		 BAD_REQUEST, UNAUTHORIZED, PAYMENT_REQUIRED, FORBIDDEN, NOT_FOUND, METHOD_NOT_ALLOWED, NOT_ACCEPTABLE, PROXY_AUTHENTICATION_REQUIRED, REQUEST_TIMEOUT, 
		 CONFLICT, GONE, LENGTH_REQUIRED, PRECONDITION_FAILED, REQUEST_ENTITY_TOO_LARGE, REQUEST_URI_TOO_LONG, UNSUPPORTED_MEDIA_TYPE, REQUESTED_RANGE_NOT_SATISFIABLE, 
		 EXPECTATION_FAILED, UNPROCESSABLE_ENTITY, LOCKED, FAILED_DEPENDENCY, 
		 RESERVED_FOR_WEBDAV, UPGRADE_REQUIRED, PRECONDITION_REQUIRED, TOO_MANY_REQUESTS, REQUEST_HEADER_FIELDS_TOO_LARGE, NO_RESPONSE,  
		 UNAVAILABLE_FOR_LEGAL_REASONS, CLIENT_CLOSED_REQUEST;
		
		private static int getCodeNum(CLIENT_ERROR code) {
			int codeValue=0;
			
			switch(code) {
				case BAD_REQUEST:
					codeValue = 400;
					break;
				case UNAUTHORIZED:
					codeValue = 401;
					break;
				case PAYMENT_REQUIRED:
					codeValue = 402;
					break;
				case FORBIDDEN:
					codeValue = 403;
					break;
				case NOT_FOUND:
					codeValue = 404;
					break;
				case METHOD_NOT_ALLOWED:
					codeValue = 405;
					break;
				case NOT_ACCEPTABLE:
					codeValue = 406;
					break;
				case PROXY_AUTHENTICATION_REQUIRED:
					codeValue = 407;
					break;
				case REQUEST_TIMEOUT:
					codeValue = 408;
					break;
				case CONFLICT:
					codeValue = 409;
					break;
				case GONE:
					codeValue = 410;
					break;
				case LENGTH_REQUIRED:
					codeValue = 411;
					break;
				case PRECONDITION_FAILED:
					codeValue = 412;
					break;
				case REQUEST_ENTITY_TOO_LARGE:
					codeValue = 413;
					break;
				case REQUEST_URI_TOO_LONG:
					codeValue = 41;
					break;
				case UNSUPPORTED_MEDIA_TYPE:
					codeValue = 415;
					break;
				case REQUESTED_RANGE_NOT_SATISFIABLE:
					codeValue = 416;
					break;
				case EXPECTATION_FAILED:
					codeValue = 417;
					break;
				case UNPROCESSABLE_ENTITY:
					codeValue = 422;
					break;
				case LOCKED:
					codeValue = 423;
					break;
				case FAILED_DEPENDENCY:
					codeValue = 424;
					break;
				case RESERVED_FOR_WEBDAV:
					codeValue = 425;
					break;
				case UPGRADE_REQUIRED:
					codeValue = 426;
					break;
				case PRECONDITION_REQUIRED:
					codeValue = 428;
					break;
				case TOO_MANY_REQUESTS:
					codeValue = 429;
					break;
				case REQUEST_HEADER_FIELDS_TOO_LARGE:
					codeValue = 431;
					break;
				case NO_RESPONSE:
					codeValue = 444;
					break;
				case UNAVAILABLE_FOR_LEGAL_REASONS:
					codeValue = 451;
					break;
				case CLIENT_CLOSED_REQUEST:
					codeValue = 499;
					break;				
				default:
					codeValue = 400;
					break;
		}	
			return codeValue;
		}
		private static int getCodeNum(String code) {
			int codeValue= 0;
			CLIENT_ERROR codeFromString = CLIENT_ERROR.valueOf(code);
			codeValue=getCodeNum(codeFromString);
			
			return codeValue;
		}
	}
	
	private enum SERVER_ERROR{
		INTERNAL_SERVER_ERROR,NOT_IMPLEMENTED, BAD_GATEWAY, SERVICE_UNAVAILABLE, GATEWAY_TIMEOUT, HTTP_VERSION_NOT_SUPPORTED, VARIANT_ALSO_NEGOTIATES, 
		INSUFFICIENT_STORAGE, LOOP_DETECTED, BANDWIDTH_LIMIT_EXCEEDED, NOT_EXTENDED, NETWORK_AUTHENTICATION_REQUIRED,NETWORK_READ_TIMEOUT_ERROR,
		NETWORK_CONNECT_TIMEOUT_ERROR;
		
		private static int getCodeNum(SERVER_ERROR code) {
			int codeValue=0;
			
			switch(code) {
				case INTERNAL_SERVER_ERROR:
					codeValue = 500;
					break;
				case NOT_IMPLEMENTED:
					codeValue = 501;
					break;
				case BAD_GATEWAY:
					codeValue = 502;
					break;
				case SERVICE_UNAVAILABLE:
					codeValue = 503;
					break;
				case GATEWAY_TIMEOUT:
					codeValue = 504;
					break;
				case HTTP_VERSION_NOT_SUPPORTED:
					codeValue = 505;
					break;
				case VARIANT_ALSO_NEGOTIATES:
					codeValue = 506;
					break;
				case INSUFFICIENT_STORAGE:
					codeValue = 507;
					break;
				case LOOP_DETECTED:
					codeValue = 508;
					break;
				case BANDWIDTH_LIMIT_EXCEEDED:
					codeValue = 509;
					break;
				case NOT_EXTENDED:
					codeValue = 510;
					break;
				case NETWORK_AUTHENTICATION_REQUIRED:
					codeValue = 511;
					break;
				case NETWORK_READ_TIMEOUT_ERROR:
					codeValue = 598;
					break;
				case NETWORK_CONNECT_TIMEOUT_ERROR:
					codeValue = 599;
					break;
				default:
					codeValue = 500;
					break;
		}	
			return codeValue;
		}
		private static int getCodeNum(String code) {
			int codeValue= 0;
			SERVER_ERROR codeFromString = SERVER_ERROR.valueOf(code);
			codeValue=getCodeNum(codeFromString);
			
			return codeValue;
		}
	}
	
	public static int getCodeNum(String codeName) {
		int codeValue = 0;
		
		searchValue =codeName;
		formatSearchValueToAcceptable();
		resultValue ="";

		searchEnumOfStatusCode();
		
		switch(resultValue) {
				case "INFORMATIONAL":
					codeValue = INFORMATIONAL.getCodeNum(searchValue);
					break;
				case "SUCCESS":
					codeValue = SUCCESS.getCodeNum(searchValue);
					break;
				case "REDIRECTION":
					codeValue = REDIRECTION.getCodeNum(searchValue);
					break;
				case "CLIENT_ERROR":
					codeValue = CLIENT_ERROR.getCodeNum(searchValue);
					break;
				case "SERVER_ERROR":
					codeValue = SERVER_ERROR.getCodeNum(searchValue);
					break;
				default:
					codeValue = SUCCESS.getCodeNum(searchValue.toUpperCase());
					break;
		}	
		return codeValue;
	}
	
	public static int getCodeNum(StatusCode codeName) {
		return getCodeNum(codeName.toString());
	}
	
	private static HashMap returnStatusCodeAsMap() {
		HashMap <String,List> codeAsMap=new HashMap<>();
		
		List<INFORMATIONAL> listInformational = Arrays.asList(INFORMATIONAL.values());
		List<SUCCESS> listSuccess = Arrays.asList(SUCCESS.values());
		List<REDIRECTION> listRedirection = Arrays.asList(REDIRECTION.values());
		List<CLIENT_ERROR> listClientError = Arrays.asList(CLIENT_ERROR.values());
		List<SERVER_ERROR> listServerError = Arrays.asList(SERVER_ERROR.values());

		codeAsMap.put("INFORMATIONAL", listInformational);
		codeAsMap.put("SUCCESS", listSuccess);
		codeAsMap.put("REDIRECTION", listRedirection);
		codeAsMap.put("CLIENT_ERROR", listClientError);
		codeAsMap.put("SERVER_ERROR", listServerError);
		
		return codeAsMap;
	}
	
	private static void formatSearchValueToAcceptable() {
		if((searchValue.contains(" "))||(searchValue.contains("-"))) {
			searchValue = searchValue.replace(" ", "_");
			searchValue = searchValue.replace("-", "_");
		}
		searchValue = searchValue.toUpperCase();
	}
	
	private static void searchEnumOfStatusCode() {
		HashMap <String,List> codeAsMap=returnStatusCodeAsMap();
		for (String key : codeAsMap.keySet()) {
		    String search_in = "" + codeAsMap.get(key);
		    if (search_in.contains(searchValue))
		    	resultValue =key;
		    if(!resultValue.isEmpty())
		    	break;
		}
	}
}
