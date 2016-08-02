import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class ETLHome {
	
	// Parse device file and populates map
	public static void loadDevices() throws IOException, org.json.simple.parser.ParseException {
		FileReader reader = new FileReader(Constants.FILE_DEVICE_TYPE);
		
		JSONParser jsonParser = new JSONParser();
		Object obj = jsonParser.parse(reader);
		JSONArray jArr = (JSONArray) obj;
		for (int i = 0; i < jArr.size(); i++) {
			JSONArray jItem = (JSONArray) jArr.get(i);
			if (Constants.s_mapDevice == null) {
				Constants.s_mapDevice = new HashMap<Integer, String>();
			}
			Constants.s_mapDevice.put(Integer.valueOf(jItem.get(0).toString()), jItem.get(1).toString());
		}
	}
	
	// Parse connection file and populates map
	public static void loadConnections() throws IOException, org.json.simple.parser.ParseException {
		FileReader reader = new FileReader(Constants.FILE_CONNECTION_TYPE);
		
		JSONParser jsonParser = new JSONParser();
		Object obj = jsonParser.parse(reader);
		JSONArray jArr = (JSONArray) obj;
		for (int i = 0; i < jArr.size(); i++) {
			JSONArray jItem = (JSONArray) jArr.get(i);
			if (Constants.s_mapConnection == null) {
				Constants.s_mapConnection = new HashMap<Integer, String>();
			}
			Constants.s_mapConnection.put(Integer.valueOf(jItem.get(0).toString()), jItem.get(1).toString());
		}
	}

	public static void main(String[] args) {
		
		// Get command line argument for number of threads.
		Options options = new Options();
		
		Option threadCount = new Option("n", "threads", true, "Number of threads");
        threadCount.setRequired(false);
        options.addOption(threadCount);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("ETL processor", options);

            System.exit(1);
            return;
        }

        int nThreads = Constants.THREAD_COUNT;
        if (cmd.hasOption("n")) {
        	nThreads = Integer.parseInt(cmd.getOptionValue("n"));
    	}
        
        // Start the log thread and create log file/directory.
        try {
	        File dirLog = new File (Constants.DIR_LOG);
			if (!dirLog.exists() || !dirLog.isDirectory()) {
				dirLog.mkdirs();
			}
			Thread tLog = new Log(Constants.FILE_LOG);
			tLog.start();
        } catch (Exception e) {
        	System.out.println("Unable to start Log thread.");
        	System.exit(1);
        }
        
        // Parse devices/connections file and populate map.
        try {
			loadDevices();
			loadConnections();
		} catch (org.json.simple.parser.ParseException e) {
			Log.log(Log.ERROR, "JSON parse error for devices/connections file." 
					+ Constants.NEW_LINE + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			Log.log(Log.ERROR, "Unable to read devices/connections file."
					+ Constants.NEW_LINE + e.getMessage());
			System.exit(1);
		}
        
        // Loop through Imp directory and start Processor threads.
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        File dirImp = new File(Constants.DIR_IMPS);
        File[] aFilesImp = dirImp.listFiles();
        if (aFilesImp != null) {
        	for (File fileImp : aFilesImp) {
        		try {
        			String stFile = fileImp.getName();
        			if (Utilities.isCSV(stFile)) {  
        				Runnable processor = new Processor(fileImp);
        				executor.execute(processor);
        			}
        		} catch (IllegalArgumentException e) {
        			Log.log(Log.ERROR, "Invalid imp file: "	+ fileImp.getName());
        			continue;
        		} catch (RejectedExecutionException e) {
        			Log.log(Log.ERROR, "Unable to start Processor thread for imp file: "
        					+ fileImp.getName());
        			continue;
        		}
        	}
        	executor.shutdown();
        	while (!executor.isTerminated()) {
        	}
        } 
	}
}
