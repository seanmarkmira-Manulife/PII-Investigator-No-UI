package com.bank.squasher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author mirasea
 *
 */
public class LogInvestigation {

	public static void processLogLine(String originalLogPath, String regex, String replaceWith) {
		System.out.println("************************");
		System.out.println("Processing " + originalLogPath);
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

		// Initialization of Reports for the current log file
		Reports report = new Reports();
		report.setLogName(originalLogPath);
		report.setRegex(regex);

		// Checks if reports folder and text exists, if not create one
		FolderUtils.checkReportsFolder(originalLogPath, report);

		LogInvestigation.investigateAndReplaceOperation(originalLogPath, report, pattern, replaceWith);

		System.out.println("Finished Processing: " + originalLogPath);
		System.out.println("************************");
		System.out.println();
	}

	public static void investigateAndReplaceOperation(String originalLogPath, Reports report, Pattern pattern,
			String replaceWith) {
		System.out.println("Investigate, Update, and Backup Operation Started for Log");

		boolean containsPII = false;

		// To make sure we retain the actual String of log lines
		List<String> logLines = new ArrayList<>();

		// Reads the original log file and adds to Array of String the result
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(originalLogPath), StandardCharsets.ISO_8859_1))) {

			String line;

			while ((line = reader.readLine()) != null) {
				report.incTotalLogLinesRead();
				Matcher matcher = pattern.matcher(line);
				boolean lineProcessed = false;

				// Note: Even though we use replaceAll for all of the occurence of pattern
				// matched, it is still the safest route to ensure we processed each and all
				// occurences
				while (matcher.find()) {
					String matchedText = matcher.group();
					// Five '*', means that it is already obfuscated and must not be updated or
					// considered as a hit of unobfuscated log
					if (!matchedText.contains("*****")) {
						String updatedLine = matcher.replaceAll(replaceWith);
						logLines.add(updatedLine);
						report.addLogHitsLineNumber(report.getTotalLogLinesRead());
						report.incNumberOfHits();
						report.incNumberOfChanges();
						report.addRegexHit(updatedLine);
						lineProcessed = true;
						containsPII = true;
					}
				}

				if (!lineProcessed) {
					logLines.add(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// if contains PII, backup the file log and then update the log with PII
		if (containsPII) {
			System.out.println("Current log contains PII!");

			System.out.println("Creating backup of original file before udpating log.");
			FolderUtils.backupLogToBackup(originalLogPath);

			System.out.println("Masking the PII based on ReplaceWith Pattern.");
			try (BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(originalLogPath), StandardCharsets.ISO_8859_1))) {
				for (String line : logLines) {
					writer.write(line);
					writer.newLine();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		report.reportForInvestigateAndUpdate();
		System.out.println("Reports Created");
	}
}