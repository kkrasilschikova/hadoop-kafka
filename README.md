# hadoop-kafka
#### Purpose:
1) Read events from topic and return only of specified type (default - AvailableForProcessing)
2) Download URI (using cURL), apply specified function, return what function returns and clean downloaded files

#### Prerequisites:
You may provide your own function in Main method instead of a default someFunc(f: File).
You may change what type to return in getKafkaEvents in Consumer.

#### In order to run the program

- run *sbt assembly*
- run jar with parameters (topic name to read from, kafka IP address), example:  
*java -jar hadoop-kafka-assembly-1.0.jar --topic TopicName --kafkaIpPort localhost:9092*
