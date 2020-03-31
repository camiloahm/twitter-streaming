# Solution Syntac Java Exercise  

The exercise is place [here](README_EXERCISE.md)
### Build and Run
If you don't want to configure the environment in your machine but you want to test it locally you can use a docker public image I already pushed.
`Docker`
```
$ docker run -it camiloahm/bieber-tweets 
```

The App is packaged with Maven, to build and run this app you can execute in the root file this command, you need `JDK 8`
and `Maven`
```
$ mvn clean package spring-boot:run -Dspring-boot.run.profiles=local
```

Also if you are in a UNIX base env like MAC or Linux you can use `./run.sh` which is in the root folder

Note: if you prefer to build the image locally you can use
`Docker`
```
$ docker build -t camiloahm/bieber-tweets .
```

## Architecture

This is a Spark application, Twitter contains a huge stream of data, and that is the reason why I used a BigData framework 
like Spark, this will reduce the amount of work to solve common problems while we are processing such as volume of information.

This app contains 3 main components `Crawler,Reader,Writer`

**Crawler:** this component controls the execution of the app, It calls the reader in order to get a Stream of Tweets, then
it processes and order the tweets based on the exercise criteria, then it gives to the writer the dataset.

**Reader:** the reader is in charge of filtering and returning a Stream with the required information of the tweets, there is one implementation
of the reader which uses Twitter4j library to communicate with twitter, this implementation also uses the oauth flow 
`Authorization code`, for a production environment the approach will be to use `Application Only` authentication, which requests on behalf of the application 
itself, as opposed to on behalf of a specific user.

**Writer:** the writer manages the output generation, there is one implementation which is for local development, It writes on console and
also creates csv files. For a production environment bearing in mind that probably we would like to process much more data 
a better approach will be to write in HDFS, S3, Cassandra, Elasticsearch or any other distributed data storage depending on how this
data will be used, also is better to use an output format like Parquet or Avro. Besides this It's important to turn on Spark 
checkpoints and store them also in a distributed storage, this is one of the spark mechanism to be more fault tolerant. For 
the local Writer the files are create by default in the root where the app is run in a folder called `twitterdata`; inside this
folder there is another folder that contains the statistic per every second of streaming data, if you want to change the writer configuration
please refer to [application-local.yml](src/main/resources/application-local.yml).

This is an [Application Context Diagram](https://www.draw.io/?lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Untitled%20Diagram.drawio#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1JeAJamzuUN_Nlnt4NMh2KGya-zUTRrzc%26export%3Ddownload)
 
### Output Format
The output type is csv, this format is supported by legacy and new machines but if this output is going to be processed
by a system that supports `Parquet` or `Avro` I would suggest to use it instead of `csv`. Parquet and Avro have a better 
performance for data analysis.     
 
### Deployment Model
This app can be deployed using any spark cluster with spark support for version 2.4, some examples of these are 
AWS Elastic Map Reduce, Azure Data Bricks, Hadoop cluster setup as IAAS or on premise. 
   
### Dependency Injection Framework

I use Spring for dependency Injection to invert control and decouple this app. This keeps the code cleaner and gives an 
idea to other devs how is the application structure and design leading in a decoupled and extensible design; 
besides that I also used some Spring utilities to load application properties making this application in terms of
configuration ready to be deployed in different environments.   

### Logs
Logs are ready for production, they are configure with log4J  

### Dependencies
I used these libraries to improve code quality and maintainability 

* Lombok
* SLF4
* AssertJ
* Spring
* Hamcrest

### CI Server
*  Continuous integration is configured with Travis CI, you can check .travis.yml, It requires permissions from Syntac 
Organization in Github.