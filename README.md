# TestApp
The application contains a full solution for the required test.

To build and run, clone the repo and run the ./mvnw file. 
application is configured to use port 8080 by default, in case you need to change configurations please check the application.yml file. 
the aaplication uses h2 in memory database. 

all endpoints (except register and login) are authenticated, you need to register a user, login in with it and use the token in the login reponse as an Authorization header to access the rest of the APIs 

to access swagger-ui:
http://localhost:8080/swagger-ui/index.html
