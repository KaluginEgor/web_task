# Movie rating project
## Project concept
The user creates an account, view information about movies and media persons, other users,
creates ratings and reviews of films.
The admin creates and manages movies, media persons.
## Application architecture: Model-View-Controller
## Project Features
- Database
    - Dynamic thread-safe connection pool
    - Protection from SQL injection
    - Proxy connection
    - Number of tables : 11
- Message sending using Gmail mail server
- Pagination
- Filters
    - Command access filter
    - Filter for transitions between pages
    - Encoding filter
    - Cross-site scripting protection filter
- Localization: EN, RU
- Available 2 user roles
- Available 4 custom tags
- Passwords hashing
- Accompanying user actions with messages
- Validating the information on a client and server
- Used design patterns: Command, Proxy, Dao, Transaction, State, Singleton, MVC
## Common Features
- Locale changing
- All movies viewing
- All media persons viewing
- Movie info viewing
- Media person info viewing
- Other users info viewing
- Other users reviews viewing
- Other users ratings viewing
## Guests Features
- Locale changing
- Signing in
- Registering
- Automatic activation after clicking on a link from the mail
## Common Authorized Users Features
- Avatar uploading
- Editing account
- Deleting account
- Creating reviews
- Editing reviews
- Deleting reviews
- Creating ratings
- Editing ratings
- Deleting ratings
- Log out
## Admin Features
- Creating new movie 
- Editing movies
- Deleting movies
- Creating new media person
- Editing media persons
- Deleting media persons
- User blocking
- User activatings
- Editing users reviews
- Deleting users reviews
- Deleting users ratings
- Editing users accounts
- Deleting users
- Viewing all users information
### &copy; Egor Kalugin, 2021