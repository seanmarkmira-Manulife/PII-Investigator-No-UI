package com.bank.squasher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reports class to create the reports for a given log operation
 * 
 * @author mirasea
 *
 */
public class Reports {
	private int numberOfHits;
	private int numberOfChanges;
	private int totalLogLinesRead;
	private String logFile;
	private String reportsPath;
	private String regex;
	private ArrayList<String> logLineHits = new ArrayList<>();
	private ArrayList<Integer> logHitsLineNumber = new ArrayList<>();
	private ArrayList<String> regexHit = new ArrayList<>();
	private ArrayList<String> updatedLogLines = new ArrayList<>();

	public void addUpdatedLogLines(String updatedLogLine) {
		updatedLogLines.add(updatedLogLine);
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public void setPath(String reportsPath) {
		this.reportsPath = reportsPath;
	}

	public void addLogLinesHits(String logLine) {
		logLineHits.add(logLine);
	}

	public void addRegexHit(String logLine) {
		regexHit.add(logLine);
	}

	public void addLogHitsLineNumber(Integer logLineNumber) {
		logHitsLineNumber.add(logLineNumber);
	}

	public ArrayList<String> getLogLineHits() {
		return logLineHits;
	}

	public void setLogName(String logFile) {
		File file = new File(logFile);
		this.logFile = file.getName();
	}

	public void incNumberOfHits() {
		numberOfHits++;
	}

	public void incNumberOfChanges() {
		numberOfChanges++;
	}

	public int getTotalLogLinesRead() {
		return totalLogLinesRead;
	}

	public void incTotalLogLinesRead() {
		totalLogLinesRead++;
	}

	public void reportForInvestigateAndUpdate(boolean investigateAndUpdate) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportsPath + "\\Reports.txt", true))) {
			int counter = 1;
			writer.write("Log File: " + logFile + System.lineSeparator());
			writer.write("Total Log Lines Read: " + totalLogLinesRead + System.lineSeparator());
			writer.write("Regex Used: " + regex + System.lineSeparator());
			writer.write("Number of Hits: " + numberOfHits + System.lineSeparator());

			if (investigateAndUpdate) {
				writer.write("Number of Changes: " + numberOfChanges + System.lineSeparator());
			}

			writer.write("Line Numbers that Matches Regex: ");
			for (Integer lineNumber : logHitsLineNumber) {
				writer.write(lineNumber + ", ");
			}
			writer.write(System.lineSeparator());

			writer.write("Matched String in Log Against Regex -- Possible PII: ");
			for (String LogLine : regexHit) {
				writer.write(System.lineSeparator());
				writer.write(counter + ". " + LogLine);
				counter++;
			}
			writer.write(System.lineSeparator());
			writer.write(System.lineSeparator());

			writer.write("Log Lines That Matches Regex: ");
			for (String logLine : logLineHits) {
				writer.write(System.lineSeparator());
				writer.write(System.lineSeparator());
				writer.write(logLine);
			}
			writer.write(System.lineSeparator());
			writer.write(System.lineSeparator());

			if (investigateAndUpdate) {
				writer.write("Updated Log Lines: ");
				for (String logLine : updatedLogLines) {
					writer.write(System.lineSeparator());
					writer.write(System.lineSeparator());
					writer.write(logLine);
				}
				writer.write(System.lineSeparator());
			}
			writer.write("---------------------------------------------" + System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
