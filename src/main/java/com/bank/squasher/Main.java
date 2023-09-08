package com.bank.squasher;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * 
 * @author mirasea
 *
 */
public class Main {

	public static void main(String[] args) {

		// Get the directory to where the JAR is placed
		String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String directoryPath = null;

		// There will be times a directory will contain spaces
		// We need to decode it properly before processing
		try {
			String decodedPath = URLDecoder.decode(jarPath, "UTF-8");
			directoryPath = new File(decodedPath).getParent();
			System.out.println("JAR file directory: " + directoryPath);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ArrayList<String> logDirectories = FolderUtils.findLogFiles(directoryPath);

		for (String logPath : logDirectories) {
			LogInvestigation.processLogLine(logPath, Configuration.REGEX_PATTERN, Configuration.REPLACE_WITH);
		}

		System.out.println("PII Investigator processing done!");
	}
}
