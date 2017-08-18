package kafka

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{JodaReads, Reads, __}

object VeeamReads {

  implicit val awaitingDownloadReads: Reads[AwaitingDownload] = (
    (__ \ "state").read[String].filter(str=>{str.matches("AwaitingDownload")}) and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (AwaitingDownload.apply _)

  implicit val beingDownloadedReads: Reads[BeingDownloaded] = (
    (__ \ "state").read[String].filter(str=>{str.matches("BeingDownloaded")}) and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (BeingDownloaded.apply _)

  implicit val noLongerAvailableReads: Reads[NoLongerAvailable] = (
    (__ \ "state").read[String].filter(str=>{str.matches("NoLongerAvailable")}) and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (NoLongerAvailable.apply _)

  implicit val availableForProcessingReads: Reads[AvailableForProcessing] = (
    (__ \ "state").read[String].filter(str=>{str.matches("AvailableForProcessing")}) and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (AvailableForProcessing.apply _)

  implicit val queuedForProcessingReads: Reads[QueuedForProcessing] = (
    (__ \ "state").read[String].filter(str=>{str.matches("QueuedForProcessing")}) and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (QueuedForProcessing.apply _)

  implicit val readyForCleanupReads: Reads[ReadyForCleanup] = (
    (__ \ "state").read[String].filter(str=>{str.matches("ReadyForCleanup")}) and
      (__ \ "uri").read[String].map(URI) and
      (__ \ "handler_id").read[String].map(_.toString.replace("\"", "")
        .split("_") match
      { case Array(a, b) => HandlerID(a, b.toInt)
      }) and
      (__ \ "size").read[Int] and
      (__ \ "last_modified").read[DateTime](JodaReads.DefaultJodaDateTimeReads)
    ) (ReadyForCleanup.apply _)
}