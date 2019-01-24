# POO
To do before using ChatSystem app:
-Install JRE (Java Runtime Environment) version 10 or older --> https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

-Install postgresql on the localhost:5432
-Create a new Database named Clavardage
-Create a new user named ClavardageUser with password SECRET. This user should have superuser rights and be allowed to login.
-Create two tables in Clavardage/public/tables 
	-Session with columns:
		-IP of type character varying with attribute private key not null 
		-nickname of type character varying of lenght 15 with attribute not null
	-MessageChat with columns:
		-id of type integer with attribute private key not null
		-date of type timestamp without zone with attribute not null
		-content of type string of lenght 1023 with attribute not null
		-author of type string of lenght 15 with attribute not null
		-IP of type string with attribute not null
	Create a foreign key in the constraints of MessageChat, where columns MessageChat.IP references Session.IP
-----------------------------------------------Launch exe files----------------------------------------------------------
To launch ChatSystem app:
- download git repository
- go into the directory ExeFiles
- Double-click on ChatSystem.exe file (Windows)

To launch Server app:
- download git repository
- go into the directory ExeFiles
- Double-click on Server.exe file (Windows)
-----------------------------------------------Launch Jar files----------------------------------------------------------------
To launch ChatSystem app:
- download git repository
- go into the directory JarFiles
- Double-click on ChatSystem.jar file (Windows)
- type : java -jar ChatSystem.jar (Linux)

To launch Server app:
- download git repository
- go into the directory JarFiles
- Double-click on Server.jar file (Windows)
- type : java -jar Server.jar (Linux)
-----------------------------------------------Launch in Eclipse---------------------------------------------------------------
To launch ChatSystem app:
- download git repository
- open POO workspace under eclipse
- do File/"Open Projects from File System"
- select POO/Clavardage
- select Clavardage/src/data/Interface/Test.java as Java Application

/!\ When setting up the App, you NEED to write the IPAdress of the distant server into src/network/HTTPServerThread.java line 22
    That address needs to be routable in order for the server to work over the Internet

To launch Server app:
- download git repository
- open POO workspace under eclipse
- do File/"Open Projects from File System"
- select POO/Clavardage
- select Clavardage/src/data/httpServer/LaunchHTTPServer.java as Java Application

(Note: if Eclipse sends error at launch, do Project/Clean)
