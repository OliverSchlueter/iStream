tasks.register<Exec>("npmBuild") {
  workingDir = file("$rootDir/frontend")

  commandLine = if (System.getProperty("os.name").lowercase().contains("win")) {
    listOf("$rootDir/frontend/build.bat")
  } else {
    listOf("bash", "$rootDir/frontend/build.sh")
  }
}
