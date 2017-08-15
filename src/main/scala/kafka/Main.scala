package kafka

import java.io.File

object Main {
  def main(args: Array[String]): Unit = {
    case class Config(topic: String = "bundle_events",
                      kafkaIpPort: String = "",
                      ofType: String = "AvailableForProcessing")

    val parser = new scopt.OptionParser[Config]("kafka") {
      opt[String]("topic").optional().valueName("<source-kafka-topic>").
        action((x, c) => c.copy(topic = x)).text("topic is optional")
      opt[String]("kafkaIpPort").required().valueName("<kafka_IP:9092>").
        action((x, c) => c.copy(kafkaIpPort = x))
      opt[String]("ofType").optional().valueName("<kafka-event-type>").
        action((x, c) => c.copy(ofType = x)).text("type is optional")
    }

    parser.parse(args, Config()) match {
      case Some(config) =>
        val cons = new Consumer(config.kafkaIpPort)
        val events: Seq[AvailableForProcessing] = cons.getKafkaEvents(config.topic, config.ofType)

        def someFunc(f: File) = f.getAbsolutePath //any function

        val p = new Processing
        for (event <- events if p.validated(event.uri)) p.processWithFunc(event.uri, someFunc)

      case None => // arguments are bad, error message will be displayed
    }
  }

}