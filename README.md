# urs-connector-main
![Android](https://img.shields.io/badge/Spring-3DDC84?style=for-the-badge&logo=spring&logoColor=white) [![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)](https://join.slack.com/t/codegambit/shared_invite/zt-pe1nuhbk-iPuFm2B1JuMS86od4a4wXQ) [![License](https://img.shields.io/badge/License-APACHE-lightgrey.svg?style=for-the-badge)](https://github.com/code-gambit/urs-connector-main/blob/master/LICENSE) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/code-gambit/VT-AndroidClient?style=for-the-badge) <br>
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/code-gambit/urs-connector-main/Build%20Test?style=for-the-badge)](https://github.com/code-gambit/VT-AndroidClient/actions/workflows/build.yml) ![Java](https://img.shields.io/badge/Java-0095D5?&style=for-the-badge&logo=Java&logoColor=white) [![GitHub last commit](https://img.shields.io/github/last-commit/code-gambit/urs-connector-main?style=for-the-badge)](https://github.com/code-gambit/VT-AndroidClient/commits)

## Project Description
This is the microservice for the URL-Shortener application. It is the main entry point to the entire url-shortener BaaS(Backend As A Service). A spring based java application ready to be deployed in kubernetes. Below are some of the features of this microservice.
1. Supports in memory database for encoding puposes.
2. Serves as the API server for the incoming request.
3. Has in-build H2 database console for database monitoring and debugging.
4. Build using spring framework, hence light weight and fast.

## Dependency
1. Cassandra v3.11+
2. Zookeeper v3.7.0

## Development Setup
Before starting with the setup make sure you have java 8 or higher installed on your machine. The java version can be checked using command `java --version`. Java can be downloaded [using this link](https://www.java.com/en/download/).
Once you have java installed you can move ahead with the following requirements.
* IDE(Integrated Development Environment) for opening the project. You can use any IDE of your choice which is capable of handeling java projects. But `IntelliJ IDEA` is highly recomended, it is easy to use and freely available IDE. Can be downloaded [using this link](https://www.jetbrains.com/idea/download/).
* Cassandra installed and running on your local host. Cassandra have pretty comprehensive guide on installing and running the application, [you can follow up here](https://cassandra.apache.org/doc/latest/cassandra/getting_started/installing.html).
    * Once cassandra is intsalled make sure you can login into cqlsh using command `cqlsh -u <your-username> -p <your-password>`.
    * Now create a deafult keyspace which `urs-connector-main` uses for managing the data. This can be done by executing below command in `cqlsh`.
       ```
       CREATE KEYSPACE urlspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'} AND durable_writes = 'true';
       ```
* Zookeeper installed and running on your local host. Refer official documentation for setting up the zookeeper. The releases can be [found here](https://zookeeper.apache.org/releases.html).

### Running the application
Once you are able to complete the above requirement setup. You can follow below setup for launching the application.
1. You can get the project on your local sytem by either cloning the repository using `git` or can download the project directly using the below links.
    * Command: `git clone https://github.com/code-gambit/urs-connector-main.git`
    * Direct Download: [urs-connector-main](https://github.com/code-gambit/urs-connector-main/archive/refs/heads/master.zip).
2. Open and wait for the project to be setup in `IntelliJ IDEA`.
3. To start the application open the terminal in the project path and run the below command.
    * Boot Application: `./gradlew bootJar`

## Contributing
DISCLAIMER: Make sure not to force push untill unavoidable.
1. Fork it
2. Create your feature branch `(git checkout -b my-new-feature)`
3. Commit your changes `(git commit -m 'Add some feature')`
4. Clear the checks and make sure build is successfull
5. Push your branch `(git push origin my-new-feature)`
6. Create a new Pull Request.
