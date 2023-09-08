package com.bank.squasher;

/*
 * Note: if you have multiple logs to investigate, you may add an || statement in the 
 * inner if statement of method findLogFiles.
 *  
 * Define the operation settings for the application.
 * 
 * @author mirasea
 */
public class Configuration {
	public static final String REPLACE_WITH = "&lt;JointOwner&gt;*****&lt;&#x2F;JointOwner&gt;";
	public static final String REGEX_PATTERN = "&lt;JointOwner&gt;.*?&lt;&#x2F;JointOwner&gt;";
	public static final String LOG_TO_OPERATE = "MBC_ClientWSEAR";
	public static final String LOG_ERROR_TO_EXCLUDE = "MBC_ClientWSSEAR_Errors";
}
