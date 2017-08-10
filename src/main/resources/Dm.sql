Enter password: ****
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 2
Server version: 5.5.18 MySQL Community Server (GPL)

Copyright (c) 2000, 2011, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> create database springboot;
Query OK, 1 row affected (0.02 sec)

mysql> use springboot;
Database changed

mysql> CREATE TABLE employee
    -> (
    -> ID int(11) NOT NULL AUTO_INCREMENT,
    -> FirstName varchar(75),
    -> LastName varchar(75) NOT NULL,
    -> Address varchar(55),
    -> City varchar(55),
    -> Email varchar(55) UNIQUE,
    -> Mobile int(20) UNIQUE,
    -> PRIMARY KEY (ID)
    -> );
Query OK, 0 rows affected (0.13 sec)

mysql> desc employee;
+-----------+-------------+------+-----+---------+----------------+
| Field     | Type        | Null | Key | Default | Extra          |
+-----------+-------------+------+-----+---------+----------------+
| ID        | int(11)     | NO   | PRI | NULL    | auto_increment |
| FirstName | varchar(75) | YES  |     | NULL    |                |
| LastName  | varchar(75) | NO   |     | NULL    |                |
| Address   | varchar(55) | YES  |     | NULL    |                |
| City      | varchar(55) | YES  |     | NULL    |                |
| Email     | varchar(55) | YES  | UNI | NULL    |                |
| Mobile    | int(20)     | YES  | UNI | NULL    |                |
+-----------+-------------+------+-----+---------+----------------+
7 rows in set (0.09 sec)

mysql> select * from employee;
+----+-----------+----------+---------+------+--------------+-----------+
| ID | FirstName | LastName | Address | City | Email        | Mobile    |
+----+-----------+----------+---------+------+--------------+-----------+
|  1 | Roo6      | L        | Sec49   | BNDA | r1@gmail.com | 644681423 |
|  2 | Manul     | Lsdfs    | Sec49   | BNDA | r2@gmail.com | 644681424 |
|  3 | Ruchi     | Rawat    | Sec49   | BNDA | r3@gmail.com | 644681425 |
|  6 | Shraddha  | L        | Sec49   | BNDA | r4@gmail.com | 644681426 |
|  7 | Sushma    | Lohiya   | Sec50   | BNDA | r5@gmail.com | 644681427 |
|  8 | Sushma    | L        | Sec51   | HPP  | r6@gmail.com | 644681428 |
+----+-----------+----------+---------+------+--------------+-----------+
6 rows in set (0.00 sec)

mysql>
