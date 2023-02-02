# Team Report

### Last week's goals
- Back-end (Devi, Charles, Jason): Design/setup database in Mongo and allow users to be able to login using an email/username and password.
- Front-end (Caleb, Ricky): This week our goal is to complete the architecture and design milestone which is due on 1/31.
- UI design (Devi, Charles, Jason): Design the recipe page

### Progress and Issues

We have done a lot of reserach on auth0 and we think it would be a good choice for our web server for login authentication through validation tokens for front/back-end and for security reasons too. Back-end has designed what the software architecture would look like and mapping out dependencies and the front-end has been working on the login page and planning out MVC (model view controller).

### This week's goals

As a whole, implement CI to automatically run a collection of unit tests on each commit. 
- Back-end (Devi, Charles, Jason): Setup Gradle for automated testing, write tests, get some starter code based on architecture and design, and setup a server to connect to front-end.
- Front-end (Caleb, Ricky): Setup Jest for unit testing. Implement functional components using React based on goals in Milestone Schedule. Track authentication by user throughout website navigation.
- UI design (Devi, Charles, Jason): Designing menu/other pages.

# Individual Contributions

### Last week's goals
- Charles: Revise the ER diagram and Mongo DB schema if needed, come up with the architecture and design for the backend, get some spec for the backend based on the design, learn some more about SparkJava and using Mongo with Java.
- Devi: Work on the ER diagram and create an endpoint for the front-end. Start organizing the various databases.
- Ricky: Help complete our architecture and design milestone which includes: having the login page complete, having API calls to the backend working, and having a page set up that displays the recipes.
- Caleb: Meet with the front-end team to devise architecture schema. Decide on login page authentication service and setup routes to access backend API.
- Jason: Learn more about Mongo and transform our E/R diagram into a setup for a database in Mongo. Also work towards users being able to login with an email/username and password.

### Progress and Issues

- Charles: I went into detail on the architecture and design for the backend (including details what classes there will be, what components they will form, and what has dependencies on what). 
- Devi: I started setting up the spark java server and researched about various resources that would be useful. This includes working with CORS Filters and setting up the server file structure. I've also done personal research on React to better set up the connections between front-end and back-end.
- Ricky: I helped with figuring out how to implement Auth0 for our applications.
- Caleb: I setup local development for our React App to meet the milestones of Architecture and Design. Using this, I helped fulfill various functionality of the View portion of our Software Architecture. One issue I faced this week was properly implementing Auth0 login to our website. More specifically, page re-routing and token tracking after a successful login.
- Jason: I helped turn our ER diagram into a Mongo database schema, added to the living documents explaining our future usage of Google style guidelines for coding and the potential major risks that could halt our progress towards completing this project.

### This week's goals

- Charles: Set up Gradle for the project, make starter code based on the architecture and design (ideally with javadocs for important methods), help write tests for the backend.
- Devi: This week, I hope to set up the Gradle and write some request endpoints. This will include starting to put the ER diagram plan into code. I will also help with testing.
- Ricky: This week, I plan to add functionality to the components on the home page. Additonally, I plan write unit tests that will be used in our CI pipeline using Jest. 
- Caleb: This week I will setup Jest and write some basic unit tests to implement into our CI pipeline. Additionally, I will solidify the login experience to track the authentication token necessary for calling the API. Finally, I will help to create functional components for the website, including a logo, a profile page, and a search bar for finding recipes.
- Jason: I want to help setup Gradle and potentially write out the project specs based off of the architeture and design and create tests before we start coding to create the idea of "blackbox" testing.
