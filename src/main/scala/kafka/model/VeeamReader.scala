package kafka.model

import org.joda.time.DateTime
import play.api.libs.json._

trait VeeamReader[A] {
  def read(value: A): VeeamLogBundleEvent

  implicit val dateReads: Reads[DateTime] = JodaReads.DefaultJodaDateTimeReads
}

object VeeamReaderInstances {
  var badBundlesCounter = 0

  implicit val jsValueReader: VeeamReader[JsValue] = new VeeamReader[JsValue] {
    def read(value: JsValue): VeeamLogBundleEvent = {
      val state = (value \ "state").as[String]
      val uri = (value \ "uri").as[String] match {
        case link => URI(link)
      }
      val handlerID = (value \ "handler_id").as[String]
        .split("_") match {
        case Array(a, b) => HandlerID(a, b.toInt)
      }
      val size = (value \ "size").as[Int]
      val last_modified = (value \ "last_modified").as[DateTime]

      state match {
        case "AwaitingDownload" => AwaitingDownload(state, uri, handlerID, size, last_modified)
        case "BeingDownloaded" => BeingDownloaded(state, uri, handlerID, size, last_modified)
        case "NoLongerAvailable" => NoLongerAvailable(state, uri, handlerID, size, last_modified)
        case "AvailableForProcessing" => AvailableForProcessing(state, uri, handlerID, size, last_modified)
        case "QueuedForProcessing" => QueuedForProcessing(state, uri, handlerID, size, last_modified)
        case "ReadyForCleanup" => ReadyForCleanup(state, uri, handlerID, size, last_modified)
        case _ => badBundlesCounter += 1; MalformedBundle()
      }
    }
  }
}

object VeeamSyntax {
  implicit class VeeamReaderOps[A](value: A) {
    def toVeeamLogBundleEvent(implicit r: VeeamReader[A]): VeeamLogBundleEvent =
      r.read(value)
  }
}
