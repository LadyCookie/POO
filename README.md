# POO
To do before using ChatSystem app:
-Install java

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
