package kafka

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{JodaReads, Json, Reads, __}

object VeeamReads {

  implicit val veeamReads: Reads[VeeamLogBundleEvent] = Json.fromJson[VeeamLogBundleEvent]

  implicit val awaitingReads: Reads[AwaitingDownload] = (
    (__ \ "state").read[String] and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (AwaitingDownload.apply _)

  implicit val downloadedReads: Reads[BeingDownloaded] = (
    (__ \ "state").read[String] and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (BeingDownloaded.apply _)

  implicit val noavailableReads: Reads[NoLongerAvailable] = (
    (__ \ "state").read[String] and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (NoLongerAvailable.apply _)

  implicit val availableReads: Reads[AvailableForProcessing] = (
    (__ \ "state").read[String] and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (AvailableForProcessing.apply _)

  implicit val queuedReads: Reads[QueuedForProcessing] = (
    (__ \ "state").read[String] and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (QueuedForProcessing.apply _)

  implicit val cleanupReads: Reads[ReadyForCleanup] = (
    (__ \ "state").read[String] and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (ReadyForCleanup.apply _)
}
