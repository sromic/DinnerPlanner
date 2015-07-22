name := "DinnerPlanner"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4-M2",
  "com.h2database" % "h2" % "1.4.187",
  "com.typesafe.slick" % "slick_2.11" % "3.0.0",
  "mysql" % "mysql-connector-java" % "5.1.12",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.scalatest" % "scalatest_2.11" % "3.0.0-M7" % "test",
  "joda-time" % "joda-time" % "2.8.1",
  "org.joda" % "joda-convert" % "1.7",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.0.0",
  "ch.qos.logback" % "logback-classic" % "1.1.1",
  "com.zaxxer" % "HikariCP" % "2.3.9"
)
