package kafka

import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.KafkaConsumer.Conf
import org.apache.kafka.clients.consumer.{ConsumerRecords, OffsetResetStrategy}
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConversions._

class Consumer(bootstrapServers: String) {

  val consumer = KafkaConsumer(
    Conf(new StringDeserializer,
      new AvailableForProcessingDeserializer,
      bootstrapServers = bootstrapServers,
      groupId = "group",
      enableAutoCommit = true,
      autoCommitInterval = 1000,
      sessionTimeoutMs = 30000,
      maxPollRecords = Integer.MAX_VALUE,
      autoOffsetReset = OffsetResetStrategy.EARLIEST
    )
  )

  def getKafkaEvents(ofType: String, topic: String): Seq[AvailableForProcessing] = {
    consumer.subscribe(Seq(topic))
    val records: ConsumerRecords[String, AvailableForProcessing] = consumer.poll(1000)
    (for (record <- records if record.value.state == ofType) yield record.value).toSeq
  }

}

object Consumer {
  def apply(bootstrapServers: String): Consumer = new Consumer(bootstrapServers=bootstrapServers)
}