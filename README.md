# Prova Finale Ingegneria del Software 2021


- ###  Campana Lorenzo ([@lorecampa](https://github.com/lorecampa))<br>lorenzo.campana@mail.polimi.it
- ###  Canali Davide ([@CanaliDavide](https://github.com/CanaliDavide))<br>davide1.canali@mail.polimi.it
- ###  Cordioli Matteo ([@MatteoCordioli](https://github.com/MatteoCordioli))<br>matteo.cordioli@mail.polimi.it


| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | <img src="DoneClipArt.png" width="40"/> |
| Complete rules | <img src="DoneClipArt.png" width="40"/> |
| Socket | <img src="DoneClipArt.png" width="40"/> |
| GUI | <img src="DoneClipArt.png" width="40"/> |
| CLI | <img src="DoneClipArt.png" width="40"/> |
| Multiple games | <img src="DoneClipArt.png" width="40"/> |
| Custom Parameters | <img src="NotDoneClipArt.png" width="40"/> |
| Server Persistence | <img src="DoneClipArt.png" width="40"/> |
| Reconnect | <img src="DoneClipArt.png" width="40"/> |
| Local Single Player | <img src="NotDoneClipArt.png" width="40"/> |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

# Master Of Renaissance

![MOR Logo](logo.jpg)

## Setup

- In the [shade](shade) folder there are two multi-platform jar files, one to set the Server up and the other one to start the Client.
- The Server can be run with the following command, as default it runs on port 2020:
    ```shell
    > java -jar MORServer.jar
    ```
  This command can be followed by these arguments:
  - **-port**: followed by the desired port number as argument, must be >=1024;

  
- The Client can be run with the following command:
    ```shell
    > java -jar MORClient.jar
    ```
    This command can be followed by these arguments:
  - **-gui**: it opens the game on Graphical User Interface(GUI);
  - **-IPADDRESS** write the ip address u want to connect to;
  - **-PORT** write the port u want to access to.
 
 ## Build
 Use maven to build jar files for both the Client and the Server by choosing the appropriate Maven Profile.  
 
 To build the Server, issue:  
    ```
       > mvn clean    
    ```  
    ```
      > mvn package -P Server    
    ```  
 <br>
 To build the Client, issue:  
    ```
        > mvn clean    
    ```  
    ```
       > mvn package -P Client    
    ```    
  
  After these processes both jars can be found in the shade folder.
 ## Extra
 
 ## Tools
 
 * [StarUML](http://staruml.io) - UML Diagram
 * [Maven](https://maven.apache.org/) - Dependency Management
 * [IntelliJ](https://www.jetbrains.com/idea/) - IDE
 * [JavaFX](https://openjfx.io) - Graphical Framework
 
 ## License
 
 This project is developed in collaboration with [Politecnico di Milano](https://www.polimi.it) and [Cranio Creations](http://www.craniocreations.it).
 
