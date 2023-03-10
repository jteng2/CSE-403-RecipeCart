# Team Report

### Last week's goals

As a whole, implement CI to automatically run a collection of unit tests on each commit. 
- Back-end (Devi, Charles, Jason): Setup Gradle for automated testing, write tests, get some starter code based on architecture and design, and setup a server to connect to front-end.
- Front-end (Caleb, Ricky): Setup Jest for unit testing. Implement functional components using React based on goals in Milestone Schedule. Track authentication by user throughout website navigation.
- UI design (Devi, Charles, Jason): Designing menu/other pages.

### Progress and Issues

The frontend made progress on building functionality on the website that users will go to for RecipeCart. The backend made starter code based on the architecture and design, which can now be implemented. We set up the CI system so that it checks if both the front-end and back-end properly build/pass tests/etc. on the remote repository.

### This week's goals

- Back-end (Devi, Charles, Jason): Get the backend to communicate with a Mongo database. Implement a use case. Write tests that covers that use case (and stuff it depends on).
- Front-end (Caleb, Ricky): Finalize user authentication and tracking througout website. Fully functional components. Display dynamic data from backend.
- UI design (Devi, Charles, Jason):

Also, establish some protocol for communication between the front-end and back-end parts of the system (i.e. how to structure HTTP requests/responses), and document this protocol.

# Individual Contributions

### Last week's goals
- Charles: Set up Gradle for the project, make starter code based on the architecture and design (ideally with javadocs for important methods), help write tests for the backend.
- Devi: This week, I hope to set up the Gradle and write some request endpoints. This will include starting to put the ER diagram plan into code. I will also help with testing.
- Ricky: This week, I plan to add functionality to the components on the home page. Additonally, I plan write unit tests that will be used in our CI pipeline using Jest. 
- Caleb: This week I will setup Jest and write some basic unit tests to implement into our CI pipeline. Additionally, I will solidify the login experience to track the authentication token necessary for calling the API. Finally, I will help to create functional components for the website, including a logo, a profile page, and a search bar for finding recipes.
- Jason: I want to help setup Gradle and potentially write out the project specs based off of the architeture and design and create tests before we start coding to create the idea of "blackbox" testing.

### Progress and Issues

- Charles: I successfully set up Gradle (so that the project builds, runs tests, lints, etc.), helped set up the CI, and wrote starter code for the whole backend (there is getter/setter/constructor/builder functionality, but everything else is left unimplemented). I also made javadocs detailing the backend. Although, setting up detailed starter code left little time to write tests for them.
- Devi: I set up the remote Mongo and set up data bases. I helped write the CI tools document and do research about the tools. I also set up a meeting and action items document to keep track of backend goals following some confusion.
- Ricky: I created some react components for the website. Furthermore, I helped researched the different potential CI tools we could use. From there, through discussing with the team, I helped in determining which CI tool we should use. 
- Caleb: This week I set up Jest for frontend unit testing and implemented it into our CI pipeline using Github Actions. I also helped fill out and make adjustments to our living document. I've encountered a few issues with state navigation throughout the website, but they are slowly getting resolved.
- Jason: I helped research potential options for our CI system, helped fill out this week's update on our living document on CI and testing. I didn't write tests or help with Gradle like I had said, but I have read over most of what was already written.

### This week's goals

- Charles: Help write more unit tests for the backend, especially for the use case that will be implemented for beta release. Implement that use case.
- Devi: I plan to finish database setup, write tests and starter code, and create some initial database calls.
- Ricky: This week I plan to build core react components. This includes the various ways to display the recipe to users, the shopping item component, and the acount info component. Furthermore, I plan to implement call to the backend API to populate the front end components. Additionally, I will write tests for the components.
- Caleb: This week I am going to finalize the majority of components necessary for our website. Additionally, I am going to implement user tracking throughout the app. I will also expand our unit testing environment. Lastly, I will work with the backend team to finalize a protocol for communication with the API.
- Jason: I want to help write unit tests/starter code for back-end and continue helping setup MongoDB.
