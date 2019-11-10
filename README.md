# PR_KN
## kristoffer näsström projectwork

# Fully reactive blog site using Spring Webflux
This project uses a fully reactive backend to manage a h2-in-memory database (development, will be migrated to 
mongoDb at deployment), as well as a reactive Spring layer using Spring Webflux and the webserver Netty. 

## REACTIVE SPRING QUICK EXPLANATION
Reactive Spring works in a similar way to the JS/Node event-loop instead of the older 1 thread per request model. 

Every time a blocking piece of code is reached the thread will look for new requests to begin handling instead of waiting
for a response (for example waiting for a database query to resolve). Instead of working with the actual datatypes like
BlogEntity for example, all datatypes are stored in either Mono or Flux containers that work similarly
to Promises in Javascript.
##### Mono datatype is a container for 0...1 objects.
##### Flux datatype is a container for 0...many objects.

## LIVE URL
Project is now live at https://cryptic-savannah-22712.herokuapp.com/client

Database converted to reactive mongoDb, hostad at mbLab.

### RESTCONTROLLER

Available at localhost:8080/api/

### CLIENT

Available at localhost:8080/client/

Accessing the client url will load available data into an html page rendered with the Thymeleaf templating engine.