/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import smpc.MainSMPC;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author r3d_i3p3
 */
public class main {

    public static MainSMPC smpc = new MainSMPC();
    
    /*VARIABLE FOR ENCRYPTE*/
    public static String projectname = "";
    public static File file = null;
    public static byte[] secret = null;
    public static File outputFloder = null;
    public static int degree = 0;
    public static int nbrOfKeys = 0;
    public static int max = -1;
    public static int[] xValues = null;
    public static BigDecimal[] keys = null;
    public static FileOutputStream fw = null;
    public static String[] filesNames = null;
    
    public static void encryptionMode(Options opt, CommandLine cmd, CommandLineParser parser, String[] args) {
        //LoadOptions
        opt = loadEncryptionOptions(opt);
        //parse the options passed as command line arguments
        try {
            cmd = parser.parse(opt, args);
        } catch (MissingOptionException ex) {
           HelpFormatter formatter = new HelpFormatter();formatter.printHelp(projectname, opt); ex.printStackTrace(); System.exit(0);
        } catch (ParseException ex) {
            HelpFormatter formatter = new HelpFormatter();formatter.printHelp(projectname, opt); ex.printStackTrace(); System.exit(0);
        }
        //Check all encyption paramaters 
        checkEncryptionInput(cmd);
        //Loading parameters 
        loadEncryptionInput(cmd);
        //start
        System.out.println("[*] Operation start ,watting ...");
        if ((cmd.hasOption("om") && cmd.getOptionValue("om").equals("1")) || !cmd.hasOption("om")) 
        {
            System.out.println("[*] Generate files name ,watting ...");
            filesNames = new String[nbrOfKeys];
            for (int i = 0; i < filesNames.length; i++) {
                filesNames[i] = outputFloder.getAbsolutePath() + File.separator + "part" + (i) + ".p";
            }
            System.out.println("[*] calcule parts ,watting ...");
            try {
                smpc.generateKeysC(secret, xValues, filesNames, degree, true);
                if(cmd.hasOption("ch")){
                    if(Arrays.equals(secret, smpc.generateSecretC(xValues, filesNames)))
                        System.out.println("CHECK DONE WITH SUCCUED");
                    else
                        System.err.println("CHECK FAILURE");
                }
            } catch (Exception ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (cmd.hasOption("om") && cmd.getOptionValue("om").equals("2")) {
            System.out.println("[*] calcule parts ,watting ...");
            try {
                keys = smpc.generateKeysA(secret, xValues, degree);
                if(cmd.hasOption("ch")){
                    if(Arrays.equals(secret, smpc.generateSecretA(xValues, keys)))
                        System.out.println("CHECK DONE WITH SUCCUED");
                    else
                        System.err.println("CHECK FAILURE");
                }
            } catch (Exception ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("[*] Create parts  files ,watting ...");
            for (int i = 0; i < keys.length; i++) {
                try {
                    smpc.BigDecimalToDataFile(outputFloder.getAbsolutePath() + File.separator + xValues[i] + ".p", keys[i]);
                } catch (IOException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("[+] Operatione end.");
        
    }     
    public static void decryptionMode(Options opt, CommandLine cmd, CommandLineParser parser, String[] args) {
        //Load options 
        opt = loadDecryptionOptions(opt);
        //parse the options passed as command line arguments
        try {
            cmd = parser.parse(opt, args);
        } catch (MissingOptionException ex) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(projectname, opt);
            ex.printStackTrace();
            System.exit(0);
        } catch (ParseException ex) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(projectname, opt);
            ex.printStackTrace();
            System.exit(0);
        }
        //Check input & load input
        checkLoadDecryptionParamaters(cmd);
        //Process
        try {
            if(cmd.hasOption("p")){
                byte[] p = null;
                p = smpc.generateSecretC(xValues, filesNames);
                System.out.println("-- BEGING SECRET  ---------------------------");
                System.out.println("" + new String(p));
                System.out.println("-- END SECRET --------------------------------");
                fw = new FileOutputStream(cmd.getOptionValue("w"));
                fw.write(p);
            }else
                smpc.generateSecretC(xValues, filesNames, cmd.getOptionValue("w"));
            //end
        } catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            /*
            //Check GUI
            if (args.length == 0) {
                System.out.println("Graphics MODE");
                System.exit(0);
            }*/
        //Using CLI
        if(args.length == 0)
        {
            printHelp();
            System.exit(0);
        }
        CommandLine cmd = null;
        CommandLineParser parser = new DefaultParser();
        Options opt = new Options();
        if (args[0].equalsIgnoreCase("-e")) {
            encryptionMode(opt, cmd, parser, args);
        } else if (args[0].equals("-d")) {
            decryptionMode(opt, cmd, parser, args);
        } 

    }
    public static void printHelp()
    {
            Options t = new Options();
            System.out.println("Encryption mode");
            HelpFormatter formatter = new HelpFormatter();formatter.printHelp(projectname, loadEncryptionOptions(t));
            System.out.println("Deycryption mode");
            t = new Options();
            formatter = new HelpFormatter();formatter.printHelp(projectname, loadDecryptionOptions(t));
    }
    //-- Functions to use with Encrypyion Mode
    /**
     * Load Encryption mode option
     *
     * @param options
     * @return
     */
    public static Options loadEncryptionOptions(Options options) {
        //requierd
        options.addOption(createOption("e", "encryptionmode", false, "use enc", true, 0, false, ' '));
        options.addOption(createOption("st", "secretType", true, "Type of input secret \n\tF : for file\n\tT : for Text", true, 1, false, ' '));
        options.addOption(createOption("s", "secret", true, "Input secret (File path or text, use -st option to chose", true, 1, false, ' '));
        options.addOption(createOption("nk", "numberofkeys", true, "number of parts you need to generate", true, 1, false, ' '));
        options.addOption(createOption("km", "keymode", true, "X values \n\t AUTO : auto genreated \n\t USER : introduce it by user", true, 1, false, ' '));
        options.addOption(createOption("om", "outmode", true, "Output keys mode \n\t 1 [DEFAULT] : create keys in files nameed part1.p part2.p ...etc part(n).p, and creater file txt with all x values ofeach parts"
                + "\n\t 2 : named each part with x values like : 123548755534.p (not reco)", false, 1, true, ' '));
        options.addOption(createOption("w", "write", true, "Output Floder", true, 1, false, ' '));
        //optional
        options.addOption(createOption("D", "degree", true, "Degree of equation , at least parts need to be more then degree.", false, 1, true, ' '));
        options.addOption(createOption("m", "max", true, "max of x values to generate", false, 1, true, ' '));
        Option o = new Option("V", "values", true, "X values");
        o.setRequired(false);
        o.setArgs(Option.UNLIMITED_VALUES);
        o.setValueSeparator(' ');
        options.addOption(o);
        options.addOption(createOption("ch", "checkcalcule", false, "Check if the secret is the same after calcule using parts [DEFAULT=OFF]", false, 0, true, ' '));
        options.addOption(createOption("p", "precision", false, "Set MAX PRECISION [DEFAULT=30]", false, 1, true, ' '));
        return options;
    }
    /**
     * Check all Encrpyttion Mode Paramaters
     *
     * @param cmd
     */
    public static void checkEncryptionInput(CommandLine cmd) {
        //Check if the Secret input mode is correct
        System.out.println("[*] Check Input Secret Mode , watting ...");
        if (!cmd.getOptionValue("st").equalsIgnoreCase("F") && !cmd.getOptionValue("st").equalsIgnoreCase("T")) {
            System.err.println("this : " + cmd.getOptionValue("st") + " is not a correct options");
            System.exit(0);
        } else if (cmd.getOptionValue("st").equals("F")) {
            //Loading secret file into file object
            file = new File(cmd.getOptionValue("s"));
            //Check file
            if (!(file.isFile() && file.exists() && file.canRead())) {
                System.err.println("[-] The Secret File is not correct");
                System.exit(0);
            }
        }
        //Check Key Mode (parts) if it's correct (nbr of part entry by user)
        System.out.println("[*] Check Input Key Mode , watting ...");
        if (!cmd.getOptionValue("km").equalsIgnoreCase("AUTO") && !cmd.getOptionValue("km").equalsIgnoreCase("USER")) {
            System.err.println("this : " + cmd.getOptionValue("km") + " is not a correct options.");
            System.exit(0);
        }
        if (cmd.getOptionValue("km").equalsIgnoreCase("USER")) {
            if (!cmd.hasOption("V")) {
                System.err.println("When you use mode USER you need to introduce  x values.");
                System.exit(0);
            }
            if (cmd.getOptionValues("V").length != Integer.parseInt(cmd.getOptionValue("nk"))) {
                System.err.println("X values is not with the same number with number of keys");
                System.exit(0);
            }
        } else if (cmd.getOptionValue("km").equalsIgnoreCase("AUTO")) {
            if (cmd.hasOption("m")) {
                max = Integer.parseInt(cmd.getOptionValue("m"));
                System.err.println("[*] update max values to : " + max);
            }
        }
        //Check Degree value if it's existe with nbr of keys (parts)
        if (cmd.hasOption("D")) {
            System.out.println("[*] Check Degree polynome , watting ...");
            if (Integer.parseInt(cmd.getOptionValue("nk")) <= Integer.parseInt(cmd.getOptionValue("D"))) {
                System.err.println("Nombre of keys need to be at least more then Degree of equation");
                System.exit(0);
            }
        }
        //Check OutputFoloder existance 
        System.out.println("[*] Check Output Floder , watting ...");
        outputFloder = new File(cmd.getOptionValue("w"));
        if(!outputFloder.exists() )
            outputFloder.mkdirs();
    }
    /**
     * Load Enctyption paramaters from the command
     *
     * @param cmd
     */
    public static void loadEncryptionInput(CommandLine cmd) {
        //loading the secret 
        if(cmd.hasOption("p"))
        {
            try{
            System.out.println("[*] Change precision value to " +(cmd.getOptionValue("p")));
            smpc.setPrecision(Integer.parseInt(cmd.getOptionValue("p")));
            }catch(NumberFormatException e ){System.err.println("[-] " +cmd.getOptionValue("p") + " is not a correct value, precision need to be a integer value.");System.exit(0);}
        }
        System.out.println("[*] Loading the secret, watting ...");
        if (cmd.getOptionValue("st").equalsIgnoreCase("F")) {
            
            file = new File(cmd.getOptionValue("s"));
            
            if (!(file.isFile() && file.exists() && file.canRead())) {
                System.err.println("[-] The Secret File is not correct");
                System.exit(0);
            }
            
            try {
                secret = Files.readAllBytes(file.toPath());
            } catch (IOException ex) {
                System.err.println("[-] Loading secret was fail."); System.exit(0);
            }
        } else 
        {
            secret = cmd.getOptionValue("s").getBytes();
        }

        //Set Nbr of keys
        System.out.println("[*] Config polynom degree ...");
        nbrOfKeys = Integer.parseInt(cmd.getOptionValue("nk"));
        
        //set degree or auto set degree
        if (cmd.hasOption("D")) {
            degree = Integer.parseInt(cmd.getOptionValue("D"));
        } else {
            degree = nbrOfKeys-1;
        }
        
        //loading x values or generete 
        
        if (cmd.getOptionValue("km").equalsIgnoreCase("USER")) {
            System.out.println("[*] Loading the X Values ...");
            xValues = new int[nbrOfKeys];
            int i = 0;
            for (String s : cmd.getOptionValues("V")) {
                try{xValues[i] = Integer.parseInt(s);}
                catch(NumberFormatException e ){System.err.println("[-] " +s + " is not a correct value.");System.exit(0);}
                if(xValues[i] <= 0)
                {
                    System.err.println("[-] " +xValues[i] +  " is not a correct value.");
                    System.exit(0);
                }
                i++;
            }
        } else { // AUTO
            System.out.println("[*] Generate X Values  : keys : "+ nbrOfKeys+" MAX :" + max);
            xValues = smpc.generateXIntValue(nbrOfKeys, max);
        }
        //for testing
        for(int i : xValues)
            System.out.println("X value : " + i);
        System.out.println("[+] Loading the X Values DONE");
    }
    //-- Functions  to use With Decrypyion Mode
    /**
     * 
     * @param options
     * @return 
     */
    public static Options loadDecryptionOptions(Options options) {
        options.addOption(createOption("d", "decryptionmpde", false, "use decryption mode", true, 0, false, ' '));

        options.addOption(createOption("p", "printsecret", false, "print secret in console", false, 0, true, ' '));

        options.addOption(createOption("w", "write", true, "prin secret to file", true, 1, false, ' '));

        Option o = new Option("xval", "xvalues", true, "X values");
        o.setRequired(true);
        o.setArgs(Option.UNLIMITED_VALUES);
        o.setValueSeparator(' ');
        options.addOption(o);

        o = new Option("k", "keysfiles", true, "keys Files name");
        o.setRequired(true);
        o.setArgs(Option.UNLIMITED_VALUES);
        o.setValueSeparator(' ');
        options.addOption(o);
        return options;

    }
    /**
     * 
     * @param cmd 
     */
    public static void checkLoadDecryptionParamaters(CommandLine cmd) {
        System.out.println("[*] Check & Load Input Decryption  , watting ...");
        if (cmd.getOptionValues("xval").length == cmd.getOptionValues("k").length) {
            xValues = new int[cmd.getOptionValues("xval").length];
            filesNames = new String[cmd.getOptionValues("k").length];
            //check xvalues and files
            for (int i = 0; i < xValues.length; i++) {
                try {
                    xValues[i] = Integer.parseInt(cmd.getOptionValues("xval")[i]);
                } catch (Exception e) {
                    System.err.println("[-] Error converting value " + cmd.getOptionValues("xval")[i] + " to numeric value ");
                    System.exit(0);
                }
                if(xValues[i] <= 0){
                    System.err.println("[-] Error : x value = 0 ");
                    System.exit(0);
                }
                filesNames[i] = cmd.getOptionValues("k")[i];
                file = new File(filesNames[i]);
                if (!(file.isFile() && file.exists())) {
                    System.err.println("[-]" + cmd.getOptionValues("k")[i] + " is not an existing file");
                    System.exit(0);
                }
            }
        } else {
            System.err.println("[-] number of values of X not corresponding to the number of parts.");
            System.exit(0);
        }
        file = new File(cmd.getOptionValue("w"));
        if (file.exists() && file.isDirectory()) {
            System.err.println("[-]" + cmd.getOptionValue("w") + " is a Directory");
            System.exit(0);
        }
        try {
            file.createNewFile();
        } catch (IOException ex) {
            System.err.println("[-] Can't create file " + cmd.getOptionValue("w") + " .");
        }
        
    }
    // Functions
    /**
     *
     * @param opt option name short -e
     * @param longOpt option name long -exemple
     * @param hasArg option has Argement
     * @param description option desciption
     * @param requierd if option is requierd
     * @param args how many arguments this option need
     * @param optionalArg if arguments are optional
     * @param valueSeperator char to seperate arguments values
     * @return
     */
    public static Option createOption(String opt, String longOpt, boolean hasArg, String description, boolean requierd, int args, boolean optionalArg, char valueSeperator) {
        Option o = new Option(opt, longOpt, hasArg, description);
        o.setRequired(requierd);
        o.setArgs(args);
        o.setOptionalArg(optionalArg);
        if (args > 1) {
            o.setValueSeparator(valueSeperator);
        }
        return o;
    }

}
