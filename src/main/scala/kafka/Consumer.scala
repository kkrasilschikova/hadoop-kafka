package kafka

import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.collection.JavaConverters._

class Consumer(bootstrapServers: String) {

  val props = new Properties()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "group")
  props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
  props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
  props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[JsonDeserializer])
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  val consumer = new KafkaConsumer[String, JsValue](props)

  def getKafkaEvents(topic: String, ofType: AvailableForProcessing=AvailableForProcessing("AvailableForProcessing",URI(""),HandlerID(""),0,new DateTime)): Seq[AvailableForProcessing] = {
    implicit val availableReads: Reads[AvailableForProcessing] = (
      (__ \ "state").read[String] and
        (__ \ "uri").read[String].map(URI) and
        (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
          .split("_") match { case Array(a, b) => HandlerID(a, b.toInt)
        }) and
        (__ \ "size").read[Int] and
        (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
      ) (AvailableForProcessing.apply _)

    consumer.subscribe(util.Collections.singletonList(topic))
    val records: ConsumerRecords[String, JsValue] = consumer.poll(1000)

    val recordsOfType: Seq[JsValue] = (for (record <- records.asScala if (record.value \ "state").as[String] == ofType.state) yield record.value()).toSeq

    val result: Seq[AvailableForProcessing] = recordsOfType.map(elem => Json.fromJson[AvailableForProcessing](elem).get)
    result
  }

}

object Consumer {
  def apply(bootstrapServers: String): Consumer = new Consumer(bootstrapServers=bootstrapServers)
}