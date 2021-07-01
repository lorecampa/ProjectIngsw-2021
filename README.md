# Prova Finale Ingegneria del Software 2021


- ###  Campana Lorenzo ([@lorecampa](https://github.com/lorecampa))<br>lorenzo.campana@mail.polimi.it
- ###  Canali Davide ([@CanaliDavide](https://github.com/CanaliDavide))<br>davide1.canali@mail.polimi.it
- ###  Cordioli Matteo ([@MatteoCordioli](https://github.com/MatteoCordioli))<br>matteo.cordioli@mail.polimi.it


| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | <img src="./deliverables/ImageReadme/DoneClipArt.png" width="40"/> |
| Complete rules | <img src="./deliverables/ImageReadme/DoneClipArt.png" width="40"/> |
| Socket | <img src="./deliverables/ImageReadme/DoneClipArt.png" width="40"/> |
| GUI | <img src="./deliverables/ImageReadme/DoneClipArt.png" width="40"/> |
| CLI | <img src="./deliverables/ImageReadme/DoneClipArt.png" width="40"/> |
| Multiple games | <img src="./deliverables/ImageReadme/DoneClipArt.png" width="40"/> |
| Custom Parameters | <img src="./deliverables/ImageReadme/NotDoneClipArt.png" width="40"/> |
| Server Persistence | <img src="./deliverables/ImageReadme/DoneClipArt.png" width="40"/> |
| Reconnect | <img src="./deliverables/ImageReadme/DoneClipArt.png" width="40"/> |
| Local Single Player | <img src="./deliverables/ImageReadme/NotDoneClipArt.png" width="40"/> |



# Master Of Renaissance

![MOR Logo](deliverables/ImageReadme/logo.jpg)

## Setup

- In the [shade](shade) folder there are two multi-platform jar files, one to set the Server up and the other one to start the Client, java version required must be >=13.
- The Server can be run with the following command, as default it runs on port 2020:
    ```shell
    > java -jar MORServer.jar
    ```
  This command can be followed by these arguments (deafult: -port 2020 -load false):
  - **-port** followed by the desired port number as argument, must be >=1024;
  - **-load** followed by true or false to reload the macthes active while closing the server last time.

  
- The Client can be run with the following command:
    ```shell
    > java -jar MORClient.jar
    ```
    Client must be run on Linux OS.
    This command can be followed by these arguments (deafult: -interface cli -address 127.0.0.1 -port 2020):
  - **-interface** followed by cli or gui based on which interface you want to play; 
  - **-address** followed by the ip address u want to connect to;
  - **-port** followed by the port you want to access to.
 
 ## Server commands
    In server terminal you can type this commands in order to get some information:
    
    - numofmatch: print the number of active matches;
    - listmatch: print the info of all the active matches;
    - logs: followed by match id will print all the logs of that match;
    - resources: followed by match id will send 20 of each concrete resources to all players of the match;
    - quit: close the server.
 
 ## Tools
 
 * [StarUML](http://staruml.io) - UML Diagram
 * [Maven](https://maven.apache.org/) - Dependency Management
 * [IntelliJ](https://www.jetbrains.com/idea/) - IDE
 * [JavaFX](https://openjfx.io) - Graphical Framework
 * [Azure](https://azure.microsoft.com/en-us/) - Virtual Machine
 
 ## License
 
 This project is developed in collaboration with [Politecnico di Milano](https://www.polimi.it) and [Cranio Creations](http://www.craniocreations.it).
 
