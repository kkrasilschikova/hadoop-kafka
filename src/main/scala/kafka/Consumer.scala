package kafka

import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.KafkaConsumer.Conf
import org.apache.kafka.clients.consumer.{ConsumerRecords, OffsetResetStrategy}
import org.apache.kafka.common.serialization.StringDeserializer
import play.api.libs.json.JsValue

import scala.collection.JavaConversions._

class Consumer(bootstrapServers: String) {

  val consumer = KafkaConsumer(
    Conf(new StringDeserializer,
      new JsonDeserializer,
      bootstrapServers = bootstrapServers,
      groupId = "group",
      enableAutoCommit = true,
      autoCommitInterval = 1000,
      sessionTimeoutMs = 30000,
      maxPollRecords = Integer.MAX_VALUE,
      autoOffsetReset = OffsetResetStrategy.EARLIEST
    )
  )

  def getKafkaEvents(ofType: String, topic: String, kafkaHost: String): Seq[JsValue] = {
    consumer.subscribe(Seq(topic))
    val records: ConsumerRecords[String, JsValue]=consumer.poll(1000)

    def loop(records: ConsumerRecords[String, JsValue], acc: Seq[JsValue]): Seq[JsValue]={
      val result=for (record <- records) yield (record.value() \ s"$ofType").asOpt[String] match{
        case Some(string)=>acc:+record.value()
        case None=>acc
      }
      result.flatten.toSeq
    }
    loop(records, Seq.empty[JsValue])
  }

}

object Consumer {
  def apply(bootstrapServers: String): Consumer = new Consumer(bootstrapServers=bootstrapServers)
}
