package kafka.model

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

trait VeeamLogBundleEvent {
  def state: String
  def uri: URI
  def handler_id: HandlerID
  def size: Int
  def last_modified: DateTime

  def formatTime: String = ISODateTimeFormat.dateTime.print(last_modified)

  override def equals(obj: scala.Any): Boolean = {
    val that = obj.asInstanceOf[VeeamLogBundleEvent]
    (this.uri == that.uri) && (this.last_modified == that.last_modified) && (this.size == that.size)
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

case class MalformedBundle(
                            state: String = "MalformedBundle",
                            uri: URI = URI(""),
                            handler_id: HandlerID = HandlerID(),
                            size: Int = 0,
                            last_modified: DateTime = new DateTime
                          ) extends VeeamLogBundleEvent