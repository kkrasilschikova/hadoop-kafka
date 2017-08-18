package kafka

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._

trait VeeamLogBundleEvent {
  val state: String
  val uri: URI
  val handler_id: HandlerID
  val size: Int
  val last_modified: DateTime

  def formatTime: String = ISODateTimeFormat.dateTime.print(last_modified)
  
  override def equals(obj: scala.Any): Boolean = {
    val that=obj.asInstanceOf[VeeamLogBundleEvent]
    (this.uri==that.uri) && (this.last_modified==that.last_modified) && (this.size==that.size)
  }

  object VeeamLogBundleEvent{
    def apply(`class` : String, data: JsValue): VeeamLogBundleEvent = {
      (`class` match{
        case "AwaitingDownload" => Json.fromJson[AwaitingDownload](data)(VeeamReads.awaitingReads)
        case "BeingDownloaded" => Json.fromJson[BeingDownloaded](data)(VeeamReads.downloadedReads)
        case "NoLongerAvailable" => Json.fromJson[NoLongerAvailable](data)(VeeamReads.noavailableReads)
        case "AvailableForProcessing" => Json.fromJson[AvailableForProcessing](data)(VeeamReads.availableReads)
        case "QueuedForProcessing" => Json.fromJson[QueuedForProcessing](data)(VeeamReads.queuedReads)
        case "ReadyForCleanup" => Json.fromJson[ReadyForCleanup](data)(VeeamReads.cleanupReads)
      }).get
    }
  }
}

case class AwaitingDownload(
                               state: String = "AwaitingDownload",
                               uri: URI,
                               handler_id: HandlerID,
                               size: Int,
                               last_modified: DateTime
                             ) extends VeeamLogBundleEvent

case class BeingDownloaded(
                              state: String = "BeingDownloaded",
                              uri: URI,
                              handler_id: HandlerID,
                              size: Int,
                              last_modified: DateTime
                            ) extends VeeamLogBundleEvent

case class NoLongerAvailable(
                                state: String = "NoLongerAvailable",
                                uri: URI,
                                handler_id: HandlerID,
                                size: Int,
                                last_modified: DateTime
                              ) extends VeeamLogBundleEvent

case class AvailableForProcessing(
                                     state: String = "AvailableForProcessing",
                                     uri: URI,
                                     handler_id: HandlerID,
                                     size: Int,
                                     last_modified: DateTime
                                   ) extends VeeamLogBundleEvent

case class QueuedForProcessing(
                                  state: String = "QueuedForProcessing",
                                  uri: URI,
                                  handler_id: HandlerID,
                                  size: Int,
                                  last_modified: DateTime
                                ) extends VeeamLogBundleEvent

  case class ReadyForCleanup(
                              state: String = "ReadyForCleanup",
                              uri: URI,
                              handler_id: HandlerID,
                              size: Int,
                              last_modified: DateTime
                            ) extends VeeamLogBundleEvent