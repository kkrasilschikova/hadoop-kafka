package kafka

import java.io.File

object Main {
  def main(args: Array[String]): Unit = {
    case class Config(topic: String = "", kafkaIpPort: String = "")

    val parser = new scopt.OptionParser[Config]("kafka") {
      opt[String]("topic").required().valueName("<source-kafka-topic>").
        action((x, c) => c.copy(topic = x))
      opt[String]("kafkaIpPort").required().valueName("<kafka_IP:9092>").
        action((x, c) => c.copy(kafkaIpPort = x))
    }

    parser.parse(args, Config()) match {
      case Some(config) =>
        val cons = new Consumer(config.kafkaIpPort)
        val events= cons.getKafkaEvents(config.topic)
println(s"\n\nevents", events)
        def someFunc(f: File) = f.getAbsolutePath // provide with any function

        val p = new Processing
        //for (event <- events if p.validated(event.uri)) p.processWithFunc(event.uri, someFunc)

      case None => // arguments are bad, error message will be displayed
    }
  }

}