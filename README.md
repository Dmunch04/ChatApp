# ChatApp
A simple chat app, built with Java for back-end and React.js for frontend.

## Run
Before you'll be able to run the server, you need to install some jar files on your system using Maven. To install Maven:
**Windows:**
Visit this website to download: https://maven.apache.org/download.cgi

**MacOS:**
Run in a terminal:
```bash
  brew update
  brew install maven
```

**Linux:**
Run in a terminal:
```bash
  sudo apt-get install maven
```

To verify that Maven was installed correctly, type in
```bash
  mvn --version
```
in a console or terminal. Make sure you've added Maven, as well as Java, to your `PATH`.

When you've installed Java and Maven, you need to add 2 jar files to your local Maven repository. Simple type in these 2 commands in a terminal:
```bash
  mvn install:install-file -Dfile=ChatApp/src/main/resources/Dependencies/socket.io-server.jar -DgroupId=socket.io -DartifactId=server -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
  mvn install:install-file -Dfile=ChatApp/src/main/resources/Dependencies/engine.io-server.jar -DgroupId=engine.io -DartifactId=server -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
```

After that you're ready to go, and can now run the `App.java` file.


