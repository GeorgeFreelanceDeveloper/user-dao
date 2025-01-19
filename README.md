# Introduction to the DAO workshop

As you already know, the goal of today's workshop is to create an object-oriented data access layer. As part of the workshop, you will create a UserDao class, which will allow you to perform operations on the database. We will store the data in a database named workshop2 in a table named users.

It will include the following columns:

```
+------------------+--------------+------+-----+---------+----------------+
| Field            | Type         | Null | Key | Default | Extra          |
+------------------+--------------+------+-----+---------+----------------+
| id               | int(11)      | NO   | PRI | NULL    | auto_increment |
| email            | varchar(255) | NO   | UNI | NULL    |                |
| username         | varchar(255) | NO   |     | NULL    |                |
| password         | varchar(60)  | NO   |     | NULL    |                |
+------------------+--------------+------+-----+---------+----------------+
```

## What will you learn in this workshop?

A workshop in the form of one huge task, which is to prepare one bigger application, certainly gives a big shot of practical knowledge and allows you to move faster and more confidently in the Java code, or the IntelliJ program. You will combine the knowledge of OOP and MySQL.

You will create a reusable code, test it by calling individual methods of UserDao and by verifying that they have had the desired effect on the database.

This project uses practically all the things that were presented and discussed during this module such as:

    classes,
    objects,
    reading data in SQL,
    removing data in SQL,
    changing data in SQL.

All this will be used for this project! That will certainly consolidate your knowledge.
## How do you get started with this project?

First, create a new repository! A detailed description of how to do this can be found in the topic How to prepare project repository After you've done as described there, go back to this article.

First, create a database and then prepare all necessary queries:

    adding a user,
    change of data,
    retrieving via id,
    deleting via id,
    downloading all users.

Start by creating a DbUtil class, which will be responsible for creating a connection to the database. Move on to creating a User class, adding the required attributes, getters and setters to it. Then create a USerDao class and add more methods to it gradually, as you need them.
Keep testing

Adding a new functionalitt, test how the program works. Don't try to do the whole workshop right away. If you have a problem with something, use the debugger to follow the program step by step.
