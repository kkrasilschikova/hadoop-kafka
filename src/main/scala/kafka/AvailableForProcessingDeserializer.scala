package kafka

import java.util

import org.apache.kafka.common.serialization.Deserializer
import play.api.libs.json.{Json, Reads}

class AvailableForProcessingDeserializer extends Deserializer[AvailableForProcessing]{
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def close(): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): AvailableForProcessing = {
    implicit val uriReads: Reads[URI]=Json.reads[URI]
    implicit val handlerReads: Reads[HandlerID]=Json.reads[HandlerID]
    implicit val availableReads: Reads[AvailableForProcessing]=Json.reads[AvailableForProcessing]
    Json.fromJson[AvailableForProcessing](Json.parse(data)).get
  }
}