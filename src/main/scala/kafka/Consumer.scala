package kafka

import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.KafkaConsumer.Conf
import org.apache.kafka.clients.consumer.{ConsumerRecords, OffsetResetStrategy}
import org.apache.kafka.common.serialization.StringDeserializer
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.collection.JavaConversions._

class Consumer(bootstrapServers: String) {

  val consumer = KafkaConsumer(
    Conf(keyDeserializer = new StringDeserializer,
      valueDeserializer = new JsonDeserializer,
      bootstrapServers = bootstrapServers,
      groupId = "group",
      enableAutoCommit = true,
      autoCommitInterval = 1000,
      sessionTimeoutMs = 30000,
      maxPollRecords = Integer.MAX_VALUE,
      autoOffsetReset = OffsetResetStrategy.EARLIEST
    )
  )

  def getKafkaEvents(topic: String, ofType: AvailableForProcessing): Seq[AvailableForProcessing]= {
    implicit val availableReads: Reads[AvailableForProcessing]= (
      (__ \ "state").read[String] and
        (__ \ "uri").read[String].map(URI) and
        (__ \ "handler_id").read[String].map(HandlerID) and//doesn't work
        (__ \ "size").read[Int] and
        (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    )(AvailableForProcessing.apply _)

    consumer.subscribe(Seq(topic))
    val records: ConsumerRecords[String, JsValue] = consumer.poll(1000)
    val recordsOfType: Seq[JsValue] = (for (record <- records if (record.value \ "state").as[String] == ofType.state) yield record.value()).toSeq

    val result: Seq[AvailableForProcessing] = recordsOfType.map(elem => Json.fromJson[AvailableForProcessing](elem).get)
    result
  }


}

object Consumer {
  def apply(bootstrapServers: String): Consumer = new Consumer(bootstrapServers=bootstrapServers)
}