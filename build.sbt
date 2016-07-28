

name := "datacenter"

version := "1.0"

scalaVersion := "2.11.8"

organization :="com.xuxue.code"


val raw: Seq[File] =Seq(file("lib"))

val cp: Classpath = raw.classpath




libraryDependencies += "log4j" % "log4j" % "1.2.17"


libraryDependencies += "junit" % "junit" % "4.11"


libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"

libraryDependencies += "antlr" % "antlr" % "2.7.2"

libraryDependencies += "net.sourceforge.jchardet" % "jchardet" % "1.0"

libraryDependencies +="org.apache.httpcomponents" % "httpclient" % "4.5.1"


libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"


mainClass in assembly := some("package.MainClass")

assemblyJarName := "tools.jar"

val meta = """META.INF(.)*""".r

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case meta(_) => MergeStrategy.discard
  case x => MergeStrategy.first
}