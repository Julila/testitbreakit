package com.company;
import java.io.IOException;
import java.lang.Runtime;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        String workingdir = "C:\\Users\\yuliia\\Documents\\Visual Studio 2013\\builditbreakit2016\\out\\production\\builditbreakit2016\\";

        for(int i = 1 ; i<41; i++){


            String containers = String.format("tc%04d.containers.txt", i);
            String ships = String.format("tc%04d.ships.txt", i);
            String output = String.format("tc%04d.output.txt", i);
            String expected = String.format("tc%04d.expected.txt", i);
            String readme = String.format("tc%04d.readme.txt", i);

            File f = new File(workingdir + containers);
            if(f.exists()) {
                executeTEST(workingdir, containers, ships, output, expected, readme);
            }
        }
    }
private static void  executeTEST(String workingdir, String container, String ship, String output, String expected , String readme) throws IOException , FileNotFoundException{


    try {
        //String workingdir = "C:\\Users\\yuliia\\Documents\\Visual Studio 2013\\builditbreakit2016\\out\\production\\builditbreakit2016\\";
        Process p = Runtime.getRuntime().exec(new String[]{"java ", "builditbreakit2016.Main",
                "-c", container,
                "-s", ship,
                "-o", output,}, null, new File(workingdir) );
        //  "java' builditbreakit2016.Main  ' -s 'C:\\Users\\yuliia\\Documents\\Visual Studio 2013\\builditbreakit2016\\out\\production\\builditbreakit2016\\schiff.txt' -o 'C:\\Users\\yuliia\\Documents\\Visual Studio 2013\\builditbreakit2016\\out\\production\\builditbreakit2016\\output.txt'");
        p.waitFor();

        // Test if output equals with expected
        Scanner outputSc = new Scanner(new File(workingdir + output));
        Scanner expectedSc = new Scanner(new File(workingdir + expected));
        boolean errorTest = false;
        while(expectedSc.hasNextLine() && outputSc.hasNextLine()) {
            String lineOut = outputSc.nextLine();
            String lineExp = expectedSc.nextLine();
            if ( !lineOut.equals(lineExp)){
                System.out.println("!!! " + output + " != " + expected);
                errorTest = true;
                Scanner readmeSc = new Scanner(new File(workingdir + readme));
                while(readmeSc.hasNextLine()){
                    System.out.println(readmeSc.nextLine());
                }
                break;
            }

        }
        if( errorTest == false ){
            System.out.println(output + " == " + expected);

        }
        outputSc.close();
        expectedSc.close();

        String line;

        BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while((line = error.readLine()) != null){
            System.out.println(line);
        }
        error.close();

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while((line=input.readLine()) != null){
            System.out.println(line);
        }

        input.close();

        OutputStream outputStream = p.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printStream.println();
        printStream.flush();
        printStream.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}}
