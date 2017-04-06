name := "electronic-voting-protocol"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"

play.Project.playJavaSettings
