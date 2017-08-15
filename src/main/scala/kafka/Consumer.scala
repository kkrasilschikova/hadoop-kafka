package kafka

import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.KafkaConsumer.Conf
import org.apache.kafka.clients.consumer.{ConsumerRecords, OffsetResetStrategy}
import org.apache.kafka.common.serialization.StringDeserializer
import org.joda.time.DateTime
import play.api.libs.json.{JodaReads, JsValue, Json, Reads}

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

  def getKafkaEvents(topic: String, ofType: String): Seq[AvailableForProcessing]= {
    //implicit val uriReads: Reads[URI]=Json.reads[URI]
    //implicit val handlerReads: Reads[HandlerID]=Json.reads[HandlerID]
    implicit val dateReads: Reads[DateTime] = JodaReads.DefaultJodaDateTimeReads
    implicit val availableReads: Reads[AvailableForProcessing]=Json.reads[AvailableForProcessing]

    consumer.subscribe(Seq(topic))
    val records: ConsumerRecords[String, JsValue] = consumer.poll(1000)
    val recordsOfType: Seq[JsValue] = (for (record <- records if (record.value \ "state").as[String] == ofType) yield record.value()).toSeq

    val result: Seq[AvailableForProcessing] = recordsOfType.map(elem => Json.fromJson[AvailableForProcessing](elem).get)
    result
  }

}

object Consumer {
  def apply(bootstrapServers: String): Consumer = new Consumer(bootstrapServers=bootstrapServers)
}