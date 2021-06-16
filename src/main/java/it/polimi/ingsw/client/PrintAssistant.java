package it.polimi.ingsw.client;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintAssistant {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static PrintAssistant instance = new PrintAssistant();

    /**
     * Print the text with the txColor and with the bkColor
     * @param text to print
     * @param txColor of text
     * @param bkColor of text
     * */
    public void printf(String text, String txColor, String bkColor){
        System.out.println(bkColor + txColor+text+ANSI_RESET+ANSI_BLACK+"|"+ANSI_RESET);
    }

    /**
     * Print the text with the txColor and with the bkColor
     * @param text to print
     * @param txColor of text
     * */
    public void printf(String text, String txColor){
        System.out.println(txColor+text+ANSI_RESET);
    }

    /**
     * Print the text with the txColor and with the bkColor
     * @param text to print
     * */
    public void printf(String text){
        System.out.println(text);
    }

    /**
     * Print the text as error, with red background and the text color: black
     * @param text i want to print
     * */
    public void errorPrint(String text){
        System.out.println(ANSI_RED_BACKGROUND + ANSI_BLACK+text+ANSI_RESET);
    }

    /**
     * Print an ArrayList of String with txColor and bkColor
     * @param texts to print
     * @param txColor of text
     * @param bkColor of text
     * */
    public void printfMultipleString(ArrayList<String>texts, String txColor, String bkColor){
        int maxSize = findMaxLength(texts);
        for(String text : texts){
            System.out.println(bkColor + txColor+padRight(text, maxSize)+ANSI_RESET);
        }
    }

    /**
     * Print an ArrayList of String with txColor and bkColor
     * @param texts to print
     * @param txColor of text
     * */
    public void printfMultipleString(ArrayList<String>texts, String txColor){
        for(String text : texts){
            System.out.println(txColor+text+ANSI_RESET);
        }
    }

    /**
     * Print an ArrayList of String with txColor and bkColor
     * @param texts to print
     * */
    public void printfMultipleString(ArrayList<String>texts){
        for(String text : texts){
            System.out.println(text+ANSI_RESET);
        }
    }

    /**
     * Return the max length in the array
     * @param texts to check
     * */
    private int findMaxLength(ArrayList<String> texts){
        int max=texts.get(0).length();
        for(String text : texts){
            if(max<text.length())
                max=text.length();
        }
        return max;
    }

    /**
     * Pad the string right of n spaces
     * @param s string to pad
     * @param n number of spaces
     * */
    public String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    /**
     * Pad the string left of n spaces
     * @param s string to pad
     * @param n number of spaces
     * @deprecated
     * */
    public String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    /**
     * Return the string s centered between a string of numberOfCharTot char
     * @param s string to center
     * @param charToFill the character u want to use to fill it up
     * @param numberOfCharTot integer
     * @param startChar first char
     * @param endChar last char
     * @return the string s centered between a string of numberOfCharTot char
     * */
    public String stringBetweenChar(String s, char charToFill, int numberOfCharTot, char startChar, char endChar){
        int numContained = howManyColorContain(s);
        int numReset = howManyResetContain(s);
        int widthRealColumn = numberOfCharTot - s.length() + (numContained*5)+(numReset*4) - 2;
        int rest = widthRealColumn % 2;
        return startChar + String.valueOf(charToFill).repeat(Math.max(0, (widthRealColumn / 2) + rest)) +
                s +
                String.valueOf(charToFill).repeat(Math.max(0, (widthRealColumn / 2))) +
                endChar;
    }

    /**
     * Return the string fit to width if possible
     * @param originalString text to fit
     * @param width number of char tot
     * @param spacing char to use to fill up
     * @param starChar first char
     * @param endChar last char
     * */
    public String fitToWidth(String originalString, int width, char spacing, char starChar, char endChar){
        String s=starChar+"";
        s+=originalString;
        int numContained = howManyColorContain(originalString);
        int numResetCont= howManyResetContain(originalString);
        int numberOfSpaces=width - s.length() + (numContained*5)+(numResetCont*4) - 1;
        s+= generateAStringOf(spacing, numberOfSpaces);
        s+=endChar;
        return s;
    }

    /**
     * Return the string fit to width if possible
     * @param originalString text to fit
     * @param width number of char tot
     * */
    public String fitToWidth(String originalString, int width){
        String s="";
        s+=originalString;
        int numContained = howManyColorContain(originalString);
        int numResetCont= howManyResetContain(originalString);
        int numberOfSpaces=width-s.length() + (numContained*5)+(numResetCont*4);
        s+= generateAStringOf(' ', numberOfSpaces);
        return s;
    }

    /**
     * Return the number of color contained in s
     * @param s the string to check
     * @return the number of color contained in s
     * */
    private int howManyColorContain(String s){
        int num=0;
        num+= howManyXContain(ANSI_WHITE_BACKGROUND, s);
        num+= howManyXContain(ANSI_WHITE, s);
        num+= howManyXContain(ANSI_PURPLE_BACKGROUND, s);
        num+= howManyXContain(ANSI_PURPLE, s);
        num+= howManyXContain(ANSI_RED_BACKGROUND, s);
        num+= howManyXContain(ANSI_RED, s);
        num+= howManyXContain(ANSI_CYAN_BACKGROUND, s);
        num+= howManyXContain(ANSI_CYAN, s);
        num+= howManyXContain(ANSI_YELLOW_BACKGROUND, s);
        num+= howManyXContain(ANSI_YELLOW, s);
        num+= howManyXContain(ANSI_BLACK, s);
        num+= howManyXContain(ANSI_BLACK_BACKGROUND, s);
        num+= howManyXContain(ANSI_BLUE, s);
        num+= howManyXContain(ANSI_BLUE_BACKGROUND, s);
        num+= howManyXContain(ANSI_GREEN, s);
        num+= howManyXContain(ANSI_GREEN_BACKGROUND, s);
        return num;
    }

    /**
     * Return the number of reset contained in s
     * @param s the string to check
     * @return the number of reset contained in s
     * */
    private int howManyResetContain(String s){
        return howManyXContain(ANSI_RESET, s);
    }

    /**
     * Return the number of color contained in s
     * @param s the string to check
     * @param color we are searching for
     * @return the number of color contained in s
     * */
    private int howManyXContain(String color, String s){
        int num=0;
        Pattern p= Pattern.compile(color, Pattern.LITERAL);
        Matcher m= p.matcher(s);
        int index=0;
        while(m.find(index)){
            num++;
            index=m.start()+1;
        }
        return num;
    }

    /**
     * Return a string of c with length num
     * @param c character to use
     * @param num length of final string
     * @return a string of c with length num
     * */
    public String generateAStringOf(char c, int num){
        StringBuilder string= new StringBuilder();
        if(num<=0)
            return string.toString();
        string.append(String.valueOf(c).repeat(num));
        return string.toString();
    }

    /**
     * Print the invalid param command error
     * */
    public void invalidParamCommand(String command){
        command = command.toUpperCase();
        errorPrint("Invalid param of "+command+", pls type help "+command+" to know the right one!");
    }

    /**
     * Print the invalid state command error
     * */
    public void invalidStateCommand(String command){
        command = command.toUpperCase();
        errorPrint("You can't call the command "+command+" now, you haven't the permissions!");
    }
}
