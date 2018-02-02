import Dependencies._
import sbt.Project.projectToRef

name in ThisBuild := """bay-scalajs"""

version in ThisBuild := "0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.2"

resolvers in ThisBuild ++= Seq(Resolver.bintrayRepo("scalameta", "maven"), Resolver.jcenterRepo)

lazy val web = (project in file("web"))
  .settings(
    scalaJSUseMainModuleInitializer := true,
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    emitSourceMaps := false,
    webpackConfigFile in fullOptJS := Some(baseDirectory.value / "prod.webpack.config.js"),
    libraryDependencies ++= Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra" % scalajsReact,
      "org.scala-js" %%% "scalajs-dom" % scalajsDom,
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTime
    ),
    npmDependencies in Compile ++= Seq(
      "react" -> "15.5.4",
      "react-dom" -> "15.5.4"
    )
  )
  .dependsOn(sharedJS)
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin, ScalaJSWeb)

lazy val dbdriver = (project in file("dbdriver"))
  .settings(
    libraryDependencies ++= Seq(
      "com.github.tminglei" %% "slick-pg" % slickPg,
      "com.github.tminglei" %% "slick-pg_circe-json" % slickPg
    )
  )
  .dependsOn(sharedJVM)

lazy val dbschema = (project in file("dbschema"))
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % slick
    )
  )
  .dependsOn(sharedJVM, dbdriver)

lazy val codegen = (project in file("codegen"))
  .settings(libraryDependencies ++= Seq(
    "org.flywaydb" % "flyway-core" % flyway,
    "com.typesafe.slick" %% "slick-codegen" % slick,
    "org.scalameta" %% "scalameta" % scalaMeta,
    "com.geirsson" %% "scalafmt-core" % scalaFmt,
    "com.github.pathikrit" %% "better-files" % betterFiles,
    "io.swagger" % "swagger-parser" % 	swaggerParser//TODO

  ))
  .dependsOn(dbdriver)

lazy val xcodegen = (project in file("xcodegen"))
  .settings(libraryDependencies ++= Seq(
    //"io.swagger" % "swagger-core" % 	"1.5.18",
    //"io.swagger" % "swagger-parser" % 	"1.0.34"
    "io.swagger" % "swagger-codegen" % 	"2.3.1"
  ))

lazy val `xcodegen-run` = (project in file("xcodegen-run"))
  .settings(libraryDependencies ++= Seq(
    "io.swagger" % "swagger-codegen-cli" % 	"2.3.1"
  ))
  .dependsOn(xcodegen)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(libraryDependencies ++= Seq(
    "de.daxten" %%% "autowire" % autowire,
    "org.typelevel" %%% "cats-core" % cats,
    "io.circe" %%% "circe-core" % circeVersion,
    "io.circe" %%% "circe-generic" % circeVersion,
    "io.circe" %%% "circe-parser" % circeVersion,
    "io.circe" %%% "circe-java8" % circeVersion,
    "com.lihaoyi" %%% "upickle" % upickle
  ))
  .jsConfigure(_.enablePlugins(ScalaJSPlugin))
  .jsSettings()

lazy val server = (project in file("server"))
  .settings(
    name := (name in ThisBuild).value,
    commands ++= Seq(CodegenCmd, RecreateCmd, SwaggerCmd),
    WebKeys.exportedMappings in Assets := Seq(),
    libraryDependencies ++= Seq(
      filters,
      jdbc,
      ehcache,
      ws,
      "com.github.t3hnar" %% "scala-bcrypt" % bcrypt,
      "com.typesafe.slick" %% "slick" % slick,
      "com.typesafe.play" %% "play-slick" % playSlick,
      "org.flywaydb" %% "flyway-play" % flywayPlay,
      "com.github.pathikrit" %% "better-files" % betterFiles,
      "play-circe" %% "play-circe" % playCirce,
      "com.softwaremill.macwire" %% "macros" % macwire % "provided",
      "com.softwaremill.macwire" %% "macrosakka" % macwire % "provided",
      "com.softwaremill.macwire" %% "util" % macwire,
      "com.softwaremill.macwire" %% "proxy" % macwire,
      lagomScaladslApi,
      lagomScaladslServer,

    ),
    scalaJSProjects := Seq(web),
    pipelineStages in Assets := Seq(scalaJSPipeline, digest, gzip),
    routesGenerator := InjectedRoutesGenerator,
    PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value
  )
  .dependsOn(dbdriver, dbschema, sharedJVM)
  .disablePlugins(PlayLayoutPlugin)
  .enablePlugins(PlayScala, SbtWeb, WebScalaJSBundlerPlugin, DockerPlugin, JavaServerAppPackaging)

lazy val sharedJVM = shared.jvm
lazy val sharedJS = shared.js

onLoad in Global ~= (_ andThen ("project server" :: _))

lazy val CodegenCmd = Command.command("codegen") { state =>
  "codegen/runMain app.DbCodegen" ::
    state
}

lazy val SwaggerCmd = Command.command("swagger") { state =>
  "codegen/runMain app.SwaggerCodegen" ::
    state
}

lazy val RecreateCmd = Command.command("codegen-re") { state =>
  "codegen/runMain app.DbCodegen recreate" ::
    state
}
