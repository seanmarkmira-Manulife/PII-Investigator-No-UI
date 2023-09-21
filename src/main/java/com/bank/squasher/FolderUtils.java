package com.bank.squasher;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * 
 * @author mirasea
 *
 */
public class FolderUtils {
	public static void checkReportsFolder(String logPath, Reports report) {

		File[] fileArray = createFolder(logPath, "Reports");

		File reportsFile = new File(fileArray[0], "Reports.txt");

		try {
			if (!reportsFile.exists()) {
				reportsFile.createNewFile();
				System.out.println("Created 'Reports.txt' folder at " + reportsFile.getAbsolutePath());
			}
			String reportsDirectoryPath = reportsFile.getParentFile().getAbsolutePath();
			report.setPath(reportsDirectoryPath);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void backupLogToBackup(String logPath) {
		File[] fileArray = createFolder(logPath, "Backup");

		File backupLogFile = new File(fileArray[0], fileArray[1].getName());

		try {
			Files.copy(fileArray[1].toPath(), backupLogFile.toPath());
			System.out.println("Copied '" + fileArray[1].getName() + "' to '" + backupLogFile.getAbsolutePath() + "'");
		} catch (FileAlreadyExistsException e) {
			System.err.println("Warning: A log file '" + fileArray[1].getName()
					+ "' already exists in the original backup folder.");
			System.err.println("Skipping backup to ensure the safety of the last backup.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File[] createFolder(String logPath, String folder) {
		File logFile = new File(logPath);
		File[] fileArray = new File[2];

		String logDirectory = logFile.getParent();

		File folderCreated = new File(logDirectory, folder);

		if (!folderCreated.exists()) {
			folderCreated.mkdirs();
			System.out.println("Created " + folder + " folder at " + folderCreated.getAbsolutePath());
		}

		fileArray[0] = folderCreated;
		fileArray[1] = logFile;

		return fileArray;
	}

	public static ArrayList<String> findLogFiles(String directoryPath) {
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		ArrayList<String> logsToOperate = new ArrayList<String>();

		// If you have multiple files to investigate you may add another || statement
		// with file.getName().contains(Configuration.SECOND_LOG_TO_OPERATE) in inner if
		// statement

		// WHERE: Configuration.SECOND_LOG_TO_OPERATE, can be defined in
		// Configuration.java
		if (files != null) {
			for (File file : files) {
				if (file.isFile() 
						&& (file.getName().contains(Configuration.LOG_CLIENTWS) || file.getName().contains(Configuration.LOG_ACCOUNTSERVICES))
						&& !file.getName().endsWith(".Z")
						&& !file.getName().equals(Configuration.LOG_CLIENTWS + ".log")
						&& !file.getName().equals(Configuration.LOG_ERROR_CLIENTWS + ".log")
						&& !file.getName().equals(Configuration.LOG_ACCOUNTSERVICES + ".log")
						&& !file.getName().equals(Configuration.LOG_ERROR_ACCOUNTSERVICES + ".log")) {
					System.out.println("Adding to ArrayList file:" + file.getName());
					logsToOperate.add(file.getAbsolutePath());
				}
			}
		}

		return logsToOperate;
	}
}
