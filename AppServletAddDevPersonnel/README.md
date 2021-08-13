# Task 2:

A basic web application using servlets that offers a form for you to add 
a person to with certain dev attributes. This  information is then stored in a list.

The WelcomeServlet class which extends HttpServlet offers the homepage.

# Homepage:
    http://localhost:8080/DevPersonServlet/

Input form:

![Screenshot](../images/DevPersonnel/inputScreen.png)

Successfully added:
![Screenshot](../images/DevPersonnel/successfullyAdded.png)

getpersons screenshot:
![Screenshot](../images/DevPersonnel/getpersons.png)



For functionality and not for need I implement placing two cookies,
    "Lab1cookie_firstName"
    "Lab1cookie_lastName"
Areas in the Forms for firstname and lastname are automatically populated with these. Although this
is a silly implementation it is mainly for the practise of using cookies.
    
This person object shall be saved into the data structure PersonsList 
which is implemented using the Singleton pattern.

    To access this task servlet:
        http://localhost:8080/lab1task2/newperson

# Task 2:
##  For PUT operations, I used Postman with these settings entered as raw in body:
    
    fname=Sarah&lname=Taylor - These being mandatory
    For the other attributes use the keys below:
        availability = day
        prog Language = plang
        Movies = movie

###   All Records are now stored in the data.json in the resource folder. 

## Issue a get request to retrieve a person object.

    http://localhost:8080/lab1task1/newperson?"addQuery"
    
The gettest.html form provide in this folder is not implemented. 
query keys must be:
        firstname, lastname, language, day, favoriteMovies

TODO:

Lab1.html homepage screen is not implemented.

Thread safety needs to be sorted:

GET should fail on invalid request parameters

POST does not update languages for an existing entry (initially C, C++; unable to change to Java, Javascript)

File storage needs to be sorted it is throwing:
 FileNotFoundException in tomcat logs (Refresh /welcome)

JSON request header requires content-type to be set (Accept should be set instead)

Incorrect response code on unsupported content type/accept

PUT for invalid request returns 405 which should be 422, 400, or 409
POST for invalid request returns 405 which should be 422, 400, 409

- The FileNotFoundException reveals a larger issue - the data.json file is specified to be in /resources/data.json and it is not - this is the source tree directory. The data.json file is not in fact deployed anywhere under the application context. This is a big problem as there is effectively no way for you to instantiate PersonsList from data.json in task 2 making Task 2 pretty much impossible to verify. To fix this, I moved data.json to a location on my filesystem where it could be accessed. What I then got was the attached stacktrace. Your code is neither deployed properly nor is it functional for Task 2.
- Regarding thread safety in Task 1:
--> you synchronize the add method, but then for getList you return a direct object reference to the underlying LinkedList, which then may be manipulated in any non-threadsafe way by any client who called that method. This is not threadsafe.
- Regarding thread safety in Task 2 - there are a few things here:
1. You have a Singleton PersonsList, so you do not need to store a reference to it on a class member variable on the welcome servlet.
2. The consecutive lines in WelcomeServlet.init to get the PL instance and then set the filename are poor coding practice. There is a contract on the PL object that somewhere the init must be called to set up object state. Make this part of the object definition itself (e.g. part of getInstance) so your code is safe and there is not this contract on the client. For the PL to work you have hit the WelcomeServlet first so this init call is made. What if someone hit getpersons (or even newperson) directly?
-- No deductions on 1 or 2, just informing on coding practices.
3. You synch on the addPerson method to make your file read/write act like a transaction. OK this works on add. But you allow unsynchronized reads through direct calls to readinJSONArray via getList. This may interleave at the same time as you are doing the add and is therefore not thread-safe. This could easily fail your Task 2 doPut, which gets the list and loops through it unprotected. This is admittedly a tough case to catch