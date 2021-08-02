# Quinn Challenges

### The Big Ones
1. Jpa or just vanilla dao? 
This confuses me a lot since there are tutorials that uses some jpa implementations like hibernate while some others are using vanilla jdbc. I thought for a while and decided to pick the jdbc one since it is more basic. 

2. Could not add links/resources to my user entity when the entity is sent to client as http respoinse

### Marginal Ones
1. Not being able to use React Route since Spring boot will map the routes to some non-existing endpoint before the client static files are sent. 
So I made the root url maps to the static files, and all client router will be handled by client side and all api endpoints will be handled byspring. i.e., user would never need to go to pages like xxx/user/login directly.
