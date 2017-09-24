name := "akka-streams-db-to-csv"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "3.0.2",
  "org.scalikejdbc" %% "scalikejdbc-streams" % "3.0.2",
  "com.typesafe.akka" %% "akka-stream" % "2.5.4",
  "mysql" % "mysql-connector-java" % "5.1.44",
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "0.8"
)