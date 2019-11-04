# PR_KN
## kristoffer näsström projectwork

# Fully reactive blog site using Spring Webflux
This project uses a fully reactive backend to manage a h2-in-memory database (development, will be migrated to 
postgres at deployment), as well as a reactive Spring layer using Spring Webflux and the webserver Netty. 

### RESTCONTROLLER

Available at localhost:8080/api/

### CLIENT

Available at localhost:8080/client/

Accessing the client url will load available data into an html page rendered with the Thymeleaf templating engine.