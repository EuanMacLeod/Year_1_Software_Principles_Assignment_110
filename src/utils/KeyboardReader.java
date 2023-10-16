package utils;

import java.util.Scanner;

/*
This is a general utilities library for getting and verifying user inputs
 */

public class KeyboardReader {
    Scanner sc;

    public KeyboardReader() {
        sc = new Scanner(System.in);
    }

    public int getInt(String displayText, int lowerBound, int upperBound) {
        int returnValue;
        boolean valid = false;
        do {
            returnValue = getInt(displayText);
            if (returnValue >= lowerBound && returnValue <= upperBound) {
                valid = true;
            } else {
                System.out.println("\nNumber Entered Is Not Withing Acceptable Range.\n");
            }
        } while (!valid);

        return returnValue;
    }

    public int getInt(String displayText) {
        int returnValue = 0;
        boolean valid = false;
        do {
            System.out.println(displayText);
            try {
                String input = sc.nextLine();
                returnValue = Integer.parseInt(input);
                valid = true;

            } catch (NumberFormatException ex) {
                System.out.println("Integer not entered");
                System.out.println("Please Try Again");
            }
        } while (!valid);

        return returnValue;
    }

    public String getString(String displayText) {
        System.out.println(displayText);

        return sc.nextLine();
    }


    public char getChar(String displayText, String allowedCharacters) {

        char returnValue = '#';
        boolean valid = false;

        do {
            System.out.println(displayText);
            String input = sc.nextLine();
            if (input.length() != 1) {
                System.out.println("Invalid Number Of Characters Entered, Please Only Enter A Single Character. \n");
            } else {
                returnValue = input.charAt(0);
                for (int i = 0; i < allowedCharacters.length(); i++) {
                    char c = allowedCharacters.charAt(i);
                    if (returnValue == c) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    System.out.println("\nInvalid Character Entered, Please Try Again.\n");
                }
            }
        } while (!valid);

        return returnValue;
    }


    public void pause() {
        System.out.println("Press Enter To Continue...");
        System.out.flush();
        sc.nextLine();


    }

}
