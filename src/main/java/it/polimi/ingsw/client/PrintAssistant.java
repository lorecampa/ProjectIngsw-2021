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

    public void printf(String text, String txColor, String bkColor){
        System.out.println(bkColor + txColor+text+ANSI_RESET+ANSI_BLACK+"|"+ANSI_RESET);
    }
    public void printf(String text, String txColor){
        System.out.println(txColor+text+ANSI_RESET);
    }
    public void printf(String text){
        System.out.println(text);
    }

    public void errorPrint(String text){
        System.out.println(ANSI_RED_BACKGROUND + ANSI_BLACK+text+ANSI_RESET);
    }

    public void printfMultipleString(ArrayList<String>texts, String txColor, String bkColor){
        int maxSize = findMaxLengh(texts);
        for(String text : texts){
            System.out.println(bkColor + txColor+padRight(text, maxSize)+ANSI_RESET);
        }
    }

    public void printfMultipleString(ArrayList<String>texts, String txColor){
        for(String text : texts){
            System.out.println(txColor+text+ANSI_RESET);
        }
    }

    public void printfMultipleString(ArrayList<String>texts){
        for(String text : texts){
            System.out.println(text+ANSI_RESET);
        }
    }

    public void clear(){

    }

    private int findMaxLengh(ArrayList<String> texts){
        int max=texts.get(0).length();
        for(String text : texts){
            if(max<text.length())
                max=text.length();
        }
        return max;
    }

    public String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

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

    public String fitToWidth(String originalString, int width){
        String s="";
        s+=originalString;
        int numContained = howManyColorContain(originalString);
        int numResetCont= howManyResetContain(originalString);
        int numberOfSpaces=width-s.length() + (numContained*5)+(numResetCont*4);
        s+= generateAStringOf(' ', numberOfSpaces);
        return s;
    }

    public static void main(String[] args) {
        String s= ANSI_RED+"1234"+ANSI_RESET+"5678"+ ANSI_GREEN_BACKGROUND+"9"+ANSI_RESET;
        System.out.println(s.length()+" Red:"+ANSI_RED.length()+" rese:"+ANSI_RESET.length());
        instance.printf(instance.fitToWidth(s, 40, ' ', ' ', ' '));
    }

    private int howManyColorContain(String s){
        int num=0;
        num+=howManyXCotain(ANSI_WHITE_BACKGROUND, s);
        num+=howManyXCotain(ANSI_WHITE, s);
        num+=howManyXCotain(ANSI_PURPLE_BACKGROUND, s);
        num+=howManyXCotain(ANSI_PURPLE, s);
        num+=howManyXCotain(ANSI_RED_BACKGROUND, s);
        num+=howManyXCotain(ANSI_RED, s);
        num+=howManyXCotain(ANSI_CYAN_BACKGROUND, s);
        num+=howManyXCotain(ANSI_CYAN, s);
        num+=howManyXCotain(ANSI_YELLOW_BACKGROUND, s);
        num+=howManyXCotain(ANSI_YELLOW, s);
        num+=howManyXCotain(ANSI_BLACK, s);
        num+=howManyXCotain(ANSI_BLACK_BACKGROUND, s);
        num+=howManyXCotain(ANSI_BLUE, s);
        num+=howManyXCotain(ANSI_BLUE_BACKGROUND, s);
        num+=howManyXCotain(ANSI_GREEN, s);
        num+=howManyXCotain(ANSI_GREEN_BACKGROUND, s);
        return num;
    }

    private int howManyResetContain(String s){
        return howManyXCotain(ANSI_RESET, s);
    }

    private int howManyXCotain(String color, String s){
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

    public String generateAStringOf(char c, int num){
        StringBuilder string= new StringBuilder();
        if(num<=0)
            return string.toString();
        string.append(String.valueOf(c).repeat(num));
        return string.toString();
    }

    public void invalidParamCommand(String command){
        command.toUpperCase();
        errorPrint("Invalid param of "+command+", pls type help "+command+" to know the right one!");
    }
    public void invalidStateCommand(String command){
        command.toUpperCase();
        errorPrint("You can't call the command "+command+" now, you haven't the permissions!");
    }
}
