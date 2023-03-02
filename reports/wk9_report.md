# Team Report

### Last week's goals

- Back-end (Devi, Charles, Jason): Finish coding use-cases and connecting to mongo
- Front-end (Caleb, Ricky):
- UI design (Devi, Charles, Jason):

Also, establish some protocol for communication between the front-end and back-end parts of the system (i.e. how to structure HTTP requests/responses), and document this protocol.

### Progress and Issues

The backend implemented the rest of the use cases as well as their API routes: these routes can be called, and the backend will do the appropriate work. Also, the login validation for the routes that require it hasn't been implemented yet, so for now, those routes don't require authentication. Although progress on Mongo hasn't finished, a file-system implementation of the data access layer has been made, so that recipes, etc. can still be permanently stored.

The frontend made progress on _______.

### This week's goals

- Back-end (Devi, Charles, Jason): Implement Mongo connection in data access layer. Implement login validation for the routes that need it.
- Front-end (Caleb, Ricky):
- UI design (Devi, Charles, Jason):

# Individual Contributions

### Last week's goals
- Charles: Write tests for the backend part of the frontend-backend connector. Implement and write tests for more use cases.
- Devi: Next week, I hope to be working on a remaining use cases and do research for the login component.
- Ricky: Work on more of the use cases and build out more of the features.
- Caleb: This week I will finalize user authentication and update our existing use cases to properly access the database via our RESTful API. Additionally, I will finalize the user profile page to work with user login.
- Jason: Finish connecting to mongo and help with any tests.

### Progress and Issues

- Charles: This week I designed more API routes to cover all 5 of our original use cases, as well as some more basic routes (such as retrieving a recipe, creating an ingredient, etc.). There are now 12 total API routes. I also implemented and tested all not-yet-implemented API routes in the backend, and the tests pass. I also provided a file-system implementation for the data access layer, so that permanent recipe/user/etc. storage is possible before Mongo connection is implemented. Also, I did write tests for the backend-frontend connector, and they do pass when ran in IntelliJ, but I'm having trouble getting them to pass in `./gradlew` and remotely (these tests are temporarily disabled for now).
- Devi:
- Ricky:
- Caleb:
- Jason:
### This week's goals

- Charles: Implement login validation. If possible, figure out a way to test the backend-frontend connector that works with `./gradlew`. 
- Devi:
- Ricky:
- Caleb:
- Jason:
