# OFNotes - backend

# Detailed description.
OFNotes(online fast notes) is a backend for simple notes online service. To comunicate with frontend, application uses own REST api(handle HTTP requests, and return result in JSON or information about error). Administrator - user with id 1 can control users and change service settings: maksimum length of note name, maksimum length of note content and maksimum number of notes per one user. Default login and password for administrator account is "admin" and "admin". Every user can read, create, modyfiy and delete own notes. 

# Requirements.
To build and run this program, you need a Maven, installed Spring libary, and compiler for Java in version 11 or newer. Program wasn't tested under older Java versions. Also you need MySQL server for program database. Database settings are in file application.properties which must be in the same directory as jar archive with program. Examle file application.properties is in repository. Before run program, import ofnotes_default.sql to database with no changes.
Build under Windows:  
mvnw clean package  
Run:  
java -jar OFNotes-2.0.jar  

# API usage on Curl example.
HTTP methods:  
POST - for create user or note.  
GET - for get user data, note or settings.  
PUT - for modify user data, note or settings.  
DELETE - for remove user or note.  
  
Create user with login: user1 and password: password1  
curl --data "login=user1&password=password1" --user admin:admin http://localhost:8080/user  
  
Create note with name: note and content: my note:  
curl --data "name=note&note=my note" --user admin:admin http://localhost:8080/note  

Show about page:
curl --user admin:admin http://localhost:8080

Get current user id and login:  
curl --user admin:admin http://localhost:8080/user  
  
Get user id and login with id: 2(only if you are logged as admin - user with id 1 or user with id 2):  
curl --user admin:admin http://localhost:8080/user/2  
  
Get user id and login of all users(only if you are logged as admin - user with id 1):  
curl --user admin:admin http://localhost:8080/user/0  

Find users containing "adm":
curl --user admin:admin http://localhost:8080/searchusers/adm

Get your all notes:  
curl --user admin:admin http://localhost:8080/note/0
  
Get setups:  
curl --user admin:admin http://localhost:8080/setups  
  
Get note with id 1(works only if note is your):  
curl --user admin:admin http://localhost:8080/note/1  

Find notes containing "12345":
curl --user admin:admin http://localhost:8080/searchnotes/12345
  
Change login to useeer and password to pass for user with id 2(only if you are logged as admin - user with id 1 or user with id 2):  
curl -X PUT -d "login=useeer&password=pass" --user admin:admin http://localhost:8080/user/2  
  
Give note 1 name note1 and content 12345(works only if note is your):  
curl -X PUT -d "name=note1&note=12345" --user admin:admin http://localhost:8080/note/1  
  
Change setups max_name_length to 3, max_note_length to 4 and max_note_count to 2(only if you are logged as admin - user with id 1):  
curl -X PUT -d "max_name_length=3&max_note_length=4&max_note_count=2" --user admin:admin http://localhost:8080/setups  
  
Remove user with id: 2(only if you are logged as admin - user with id 1):  
curl -X DELETE -d "id=2" --user admin:admin http://localhost:8080/user  
  
Remove note with id: 1(works only if note is your):  
curl -X DELETE -d "id=1" --user admin:admin http://localhost:8080/note  
  
# Screenshots.  

![Firefox](https://github.com/arkadiusz97/OFNotes/blob/master/screenshots/Firefox.PNG)
![cmd](https://github.com/arkadiusz97/OFNotes/blob/master/screenshots/cmd.png)

# Download.
Download the latest version for JVM 11 or newer: https://github.com/arkadiusz97/OFNotes/releases/download/2.0/OFNotes-2.0.zip
