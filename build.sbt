name := "electronic-voting-protocol"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.postgresql" % "postgresql" % "42.0.0"
)     

play.Project.playJavaSettings
