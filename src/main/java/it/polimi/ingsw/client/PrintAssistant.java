package it.polimi.ingsw.client;

import java.util.ArrayList;

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
        System.out.println(bkColor + txColor+text+ANSI_RESET);
    }
    public void printf(String text, String txColor){
        System.out.println(txColor+text+ANSI_RESET);
    }
    public void printf(String text){
        System.out.println(text);
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
        int widthRealColumn = numberOfCharTot -s.length()+ (numContained*12)+(numContained==0?0:1);
        String string=startChar+"";
        for(int i=0; i<(widthRealColumn/2)-2;i++){
            string+=charToFill;
        }
        string+=s;
        for(int i=0; i<(widthRealColumn/2)-1;i++){
            string+=charToFill;
        }
        string+=endChar;
        return string;
    }

    public String fitToWidth(String originalString, int width, char spacing, char starChar, char endChar, int offSet){
        String s=starChar+"";
        s+=originalString;
        int numContained = howManyColorContain(originalString);
        int numberOfSpaces=width-s.length()-offSet + (numContained*9)+(numContained==0?0:1);
        s+=generataAStringOf(spacing, numberOfSpaces);
        s+=endChar;
        return s;
    }

    private int howManyColorContain(String s){
        int num=0;
        if(s.contains(ANSI_WHITE_BACKGROUND)) num++;
        if(s.contains(ANSI_WHITE)) num++;
        if(s.contains(ANSI_PURPLE_BACKGROUND)) num++;
        if(s.contains(ANSI_PURPLE)) num++;
        if(s.contains(ANSI_RED_BACKGROUND)) num++;
        if(s.contains(ANSI_RED)) num++;
        if(s.contains(ANSI_CYAN_BACKGROUND)) num++;
        if(s.contains(ANSI_CYAN)) num++;
        if(s.contains(ANSI_YELLOW_BACKGROUND)) num++;
        if(s.contains(ANSI_YELLOW)) num++;
        if(s.contains(ANSI_BLACK)) num++;
        if(s.contains(ANSI_BLACK_BACKGROUND)) num++;
        if(s.contains(ANSI_BLUE)) num++;
        if(s.contains(ANSI_BLUE_BACKGROUND)) num++;
        if(s.contains(ANSI_GREEN)) num++;
        if(s.contains(ANSI_GREEN_BACKGROUND)) num++;
        return num;
    }

    public String generataAStringOf(char c, int num){
        String string= "";
        for(int i=0;i<num;i++){
            string+=c;
        }
        return string;
    }

}
