package kafka

import java.util
import java.util.Properties

import kafka.model.{AvailableForProcessing, VeeamReads}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import play.api.libs.json._

import scala.collection.JavaConverters._

class Consumer(bootstrapServers: String) {

  val props = new Properties()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "MyGroup")
  props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
  props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
  props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[JsonDeserializer])
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  val consumer = new KafkaConsumer[String, JsValue](props)

  def getKafkaEvents(topic: String,
                     ofType: Reads[AvailableForProcessing] = VeeamReads.availableForProcessingReads): Seq[AvailableForProcessing] = {

    if (consumer.listTopics().containsKey(topic)) {

      consumer.subscribe(util.Collections.singletonList(topic))
      val records: ConsumerRecords[String, JsValue] = consumer.poll(1000)
      val jsonRecords: Seq[JsValue] = (for (record <- records.asScala) yield record.value()).toSeq

      def getFinalSeq(seq: Seq[JsValue], acc: Seq[AvailableForProcessing]): Seq[AvailableForProcessing] = {
        val result = for (rec <- seq) yield rec.validate[AvailableForProcessing](ofType) match {
          case success: JsSuccess[AvailableForProcessing] => acc :+ success.get
          case error: JsError => acc
        }
        result.flatten
      }
      consumer.close()
      getFinalSeq(jsonRecords, Seq.empty[AvailableForProcessing])
    }

    else {
      println(s"\nTopic $topic doesn't exist")
      Seq.empty[AvailableForProcessing]
    }
  }

}

object Consumer {
  def apply(bootstrapServers: String): Consumer = new Consumer(bootstrapServers=bootstrapServers)
}