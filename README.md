# DinnerPlanner

Akka based application. Solves dinner selection problem based on soon expiry items from fridge.
Akka actors are representing real house devices. Actor are communicating with sending messages.

To build project, use sbt/ativator: **sbt/activator clean compile**
To run project, use sbt/activator: **sbt/activator run**

###OLD
Using MySQL or H2 database.
In order to use **H2** database need to *import slick.driver.H2Driver.api._*, and for **MySQL** *import slick.driver.MySQLDriver.api._*
Also DatabaseConfig class needs to be changed to load appropriate database configuration.

###NEW
Added support for multi database support over abstraction of *JdbcProfile* and *JdbcDriver*. <br>Two profiles are defined: **dev** and **prod**.
To use one of them, need to setup environment variable **runMode**
