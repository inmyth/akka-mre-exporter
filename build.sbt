name := "akka-mre-exporter"

version := "0.1"

scalaVersion := "2.12.4"

lazy val akkaHttpVersion = "10.1.0-RC2"
lazy val akkaVersion = "2.5.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
  "com.typesafe.play" %% "play-json"            % "2.6.8",
  "com.typesafe.play" %% "play"                 % "2.6.11",
  "javax.inject" % "javax.inject" % "1",
  "com.neovisionaries" % "nv-websocket-client" % "2.3",
  "com.typesafe.play" %% "play-functional" % "2.6.8",
  "com.beachape.filemanagement" %% "schwatcher" % "0.3.2",
  "com.typesafe.slick" %% "slick" % "3.2.2",
  "mysql" % "mysql-connector-java" % "5.1.6",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
  "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
  "org.scalatest"     %% "scalatest"            % "3.0.1"         % Test
)

excludeDependencies += "commons-logging" % "commons-logging"
libraryDependencies += "com.typesafe" % "config" % "1.3.2"
//libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "0.17"