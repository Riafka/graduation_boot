# Restaurant Voting System

Voting system for deciding where to have lunch (without frontend).

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

Tech stack: Maven, Spring Boot 2.6.1, Spring Security, Spring Data JPA, REST (Jackson), JDK8/17, <br>
Stream API, HSQLDB (in memory),  JUnit5, Swagger (API)

Login information:
* user@gmail.com / password
* admin@gmail.com / admin

[Swagger REST API](http://localhost:8080/swagger-ui/index.html)