package kafka

import java.util
import java.util.Properties

import kafka.model.VeeamReaderInstances._
import kafka.model.VeeamSyntax._

import kafka.model.{AvailableForProcessing, VeeamLogBundleEvent}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import play.api.libs.json._

import scala.annotation.tailrec
import scala.collection.JavaConverters._

class Consumer(bootstrapServers: String) {

  val props = new Properties()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "Group")
  props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
  props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[JsonDeserializer])
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  val consumer = new KafkaConsumer[String, JsValue](props)

  def getKafkaEvents(topic: String): Seq[AvailableForProcessing] = {

    if (consumer.listTopics().containsKey(topic)) {
      consumer.subscribe(util.Collections.singletonList(topic))
      val records: ConsumerRecords[String, JsValue] = consumer.poll(5000)
      val jsonRecords: Seq[JsValue] = (for (record <- records.asScala) yield record.value()).toSeq

      //every record we cast to VeeamLogBundleEvent
      // and then add to final sequence only of needed type (AvailableForProcessing, for example)
      def getFinalSeq(seq: Seq[JsValue]): Seq[AvailableForProcessing] = {
        val result = for (rec <- seq) yield rec.toVeeamLogBundleEvent
        println(s"Total number of Malformed Bundles: $badBundlesCounter") //counted in VeeamReader
        badBundlesCounter = 0

        @tailrec
        def loop(source: Seq[VeeamLogBundleEvent], acc: Seq[AvailableForProcessing]): Seq[AvailableForProcessing] = {
          source match {
            case Nil => acc
            case head :: tail => head match {
              case elem: AvailableForProcessing => loop(tail, acc :+ elem)
              case _ => loop(tail, acc)
            }
          }
        }

        loop(result, Seq.empty)
      }

      consumer.close()
      getFinalSeq(jsonRecords)
    }

    else {
      consumer.close()
      println(s"\nTopic $topic doesn't exist")
      Seq.empty[AvailableForProcessing]
    }
  }

}

object Consumer {
  def apply(bootstrapServers: String): Consumer = new Consumer(bootstrapServers=bootstrapServers)
}