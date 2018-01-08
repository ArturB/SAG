package sag.requester;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Requester {
    public static void main(String[] args) {
        // Lista obsługiwanych parametrów CLI
        DefaultParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("l", "agents-list", true, "Path to file - list of all agents in system. Each line of the file should correspond to path of one system agent.");
        opts.addOption("h", "help", false, "Show this help");

        try {
            CommandLine cmd = parser.parse(opts, args);
            // PRINT HELP
            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("sag-launcher", opts, true);
                System.exit(0);
            }
            if(!cmd.hasOption("l")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "sag-launcher", opts, true );
                System.exit(-1);
            }

            try {
                List<String> agentsPaths = Files.readAllLines(Paths.get(cmd.getOptionValue("l")));




                System.out.println("Agents list loaded. \nNow, you can type here path to CV text file to classify and wait for the response.");
                while(true) {
                    System.out.println(">> ");
                    String cvpath = System.console().readLine();
                    String cv = new String(Files.readAllBytes(Paths.get(cvpath)));

                }


            }
            catch(IOException e) {
                System.out.println("Invalid agents list file path!");
            }


        }
        catch(ParseException e) {
            System.out.println("Unknown option!");
        }




    }
}
