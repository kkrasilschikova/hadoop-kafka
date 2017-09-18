package kafka

import java.util

import org.apache.kafka.common.serialization.Deserializer
import play.api.libs.json._

class JsonDeserializer extends Deserializer[JsValue] {

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def close(): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): JsValue = Json.parse(data)

}