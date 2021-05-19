package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMenuCMD implements Command{

    private final String cmd="MAINMENU";
    private final String param;
    private final Client client;

    public MainMenuCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    @Override
    public void doCommand() {
        if(param.isEmpty() || param.isBlank()){                         //se uno dal game fa mainmenu come comando puo tornare al main menu o facciamo un disconnect command???
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        if(client.getState()!= ClientState.MAIN_MENU){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }
        String[] split= param.split(" ", 2);
        int num=Integer.parseInt(split[0]);
        if(split.length>1 || num<1 || num>3){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }

        switch (num){
            case 1:
                client.writeToStream(new ConnectionMessage(ConnectionType.CONNECT, ""));
                client.setState(ClientState.ENTERING_LOBBY);
                break;
            case 2:
                //single player, how do we connect to that game?
                client.setState(ClientState.ENTERING_LOBBY);
                break;
            case 3:
                int matchID=-1;
                int clientID=-1;
                try{
                    File file = new File(client.getNameFile());
                    Scanner scanner= new Scanner(file);
                    matchID=Integer.parseInt(scanner.nextLine());
                    clientID=Integer.parseInt(scanner.nextLine());
                }
                catch(IOException e){
                    PrintAssistant.instance.errorPrint("Some errors with files where your data to re-log are stored!");
                }
                client.writeToStream( new ReconnectionMessage(matchID,clientID));
                client.setState(ClientState.ENTERING_LOBBY);
                break;
        }

    }

    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("write the command followed by the number you want to select from the Main Menu shown");
        rowHelp.add("ex: "+cmd.toLowerCase()+" 2");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"select the action you want to do from the Main Menu");
    }
}
