package kafka

import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.KafkaConsumer.Conf
import org.apache.kafka.clients.consumer.{ConsumerRecords, OffsetResetStrategy}
import org.apache.kafka.common.serialization.StringDeserializer
import scala.collection.JavaConversions._

class Consumer(bootstrapServers: String) {

  val consumer = KafkaConsumer(
    Conf(new StringDeserializer(),
      new StringDeserializer(),
      bootstrapServers = bootstrapServers,
      groupId = "group",
      enableAutoCommit = true,
      autoCommitInterval = 1000,
      sessionTimeoutMs = 30000,
      maxPollRecords = Integer.MAX_VALUE,
      autoOffsetReset = OffsetResetStrategy.EARLIEST
    )
  )

  def consumerResults(topic: String) = {
    consumer.subscribe(Seq(topic))
    val records: ConsumerRecords[String, String] = consumer.poll(100)
    for (record <- records) println(record)
  }

}

object Consumer {
  def apply(bootstrapServers: String): Consumer = new Consumer(bootstrapServers=bootstrapServers)
}
