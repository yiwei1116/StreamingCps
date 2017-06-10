name := "FPro"

version := "1.0"
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
scalaVersion := "2.11.8"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.0"

  // "org.apache.spark" %% "spark-mllib_2.10" % "2.0.0"d
)