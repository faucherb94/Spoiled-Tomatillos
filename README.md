# CS4500 Spring 2018, Team 26: Coders Unlimited

## Team members

* Madeline Leger
* Ben Faucher
* George Abinader
* Joseph Donovan

## Docs Folder

Contains all documents generated over the course of the project, including planning docs and sprint reviews.

## Final Folder

Contains all materials for the final submission.

## App Links

* [Deployed App](http://app.codersunltd.me/)
* [Team Jenkins](http://jenkins.codersunltd.me/)
* [Team SonarQube Server](http://qube.codersunltd.me/)

## Video Links

* [Final Presentation](https://youtu.be/QfabMusIyvU)
* [System Demo](https://youtu.be/ePxkW__SbwY)
* [System Setup](https://youtu.be/ijBeSTAH_IM)

## Development

This app was creating with the SpringBoot framework via [Spring Tool Suite](https://spring.io/tools/sts). To develop the app, any IDE that supports Java can be used. STS has direct support for debugging spring framework apps.

### Maven POM

You might want to edit the team-specific variables in pom.xml, namely `name`, `artifactId`, `description`, and `url`.

### Build

To build the app locally:

1. Install a [Java development kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) on your OS (version 8 or above).
1. Install [Apache Maven](https://maven.apache.org/install.html).
1. Clone the repo to your hard drive.
1. Run `mvn clean install` in the project root. This will produce a .war file in a target subdirectory.

To run the app, you'll need to take care of the following:

### Database

This app requires access to an existing schema on a MySQL database. Set one up somewhere and take note of:

* Its URL
* Username and password with write permission

In the file `src/main/resources/application.properties` update the `url` and `username` fields to your team-specific values. The password _can_ be stored here too, but storing passwords in source code is a bad idea.

Our method of mitigating this is telling the app to search for an environment variable called `DB_PASS` every time it runs. For this to work, the `DB_PASS` env var must be set in the environment running the app every time it runs. We made this work by editing the `.bashrc` file of every machine that runs this app (both development and deployment) to contain the line `export DB_PASS=<password>`. This isn't a perfect method, but it worked for rapid development.

Once you have a database setup, you'll need to source the sql script spoiled_tomatillos.sql to setup the proper schema the app expects.

### External APIs

The app also makes API calls to OMDB and TMDB. For these calls to succeed, you must generate API keys for your team:

* [OMDB API key generator](http://www.omdbapi.com/apikey.aspx)
* [TMDB API Registration](https://developers.themoviedb.org/3/getting-started/introduction)

We also pass these into the app via environment variables called OMDB_API_KEY and TMDB_API_KEY, respectively, and we also used `export` commands in `.bashrc` to inject them into the app environment.

### Running

The app is run from the command line with `java -jar path/to/team26-st-app-255.war`

## Deployment

This details the continuous deployment process our team used. You can choose to also utilize it or ignore it and use your own.

### Deployment Servers

Some kind of server is necessary to run this app. This can be achieved in a number of ways and any OS will work, but however your team deploys its apps, it is necessary the server is running at least Java runtime version 8.

### Jenkins

This project utilizes a Jenkins declarative pipeline continuous development process. The Jenkinsfile in this repo was last used with Jenkins v2.107.2 and requires the following Jenkins plugins:

* Pipeline
* Pipeline Utility Steps
* Environment Injector
* Slack Notification
* SonarQube Scanner for Jenkins
* SSH Agent
* Workspace Cleanup

The following parts of the Jenkinsfile are project-specific and should be changed if development is taken up by another team:

* The __`environment` directive__ at the top defines the three environment variables mentioned earlier: `DB_PASS`, `OMDB_API_KEY`, and `TMDB_API_KEY`. We stored these values in Jenkins via [secret text credentials](https://support.cloudbees.com/hc/en-us/articles/203802500-Injecting-Secrets-into-Jenkins-Build-Jobs). If you keep the method of using environment variables to store this data, it is suggested you also store these values in Jenkins via secret text.
* The __SonarQube stage__: The `withSonarQubeEnv()` step is referencing SonarQube parameters stored on our team-specific Jenkins server. Refer to [SonarQube's documentation](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Jenkins) on how to set up SonarQube integration with your team's Jenkins server. You will need to have your own instance of a SonarQube server running.
* The __Deploy stage__:
  * This stage uses ssh to copy files from Jenkins to the deployment server. You will have to replace `app.codersunltd.me` with the address of your own deployment servers.
  * Also note the `withCredentials` directive. Similar to storing passwords and other secret text via Jenkins credentials, the credentials to your deploy server can be stored in a similar way. Access to our deploy server is via a key file, which Jenkins is able to store as a retrievable credential.
  * Before running your first deployment, ensure the files `stopapp.sh`, `rotate.sh`, and `startapp.sh` have been copied from the project repo to the folder on your deploy server you intend to run the app out of.
* (If you plan on using it, the __Dev Deploy stage__ will need similar updates.)
* The `post` directive calls the `notifyBuild` function which calls the `slackSend` function. This will only work if you have the Slack notifier plugin for Jenkins installed and [set up](https://github.com/jenkinsci/slack-plugin). If you don't need slack, remove the `notifyBuild` line from the `post` directive.