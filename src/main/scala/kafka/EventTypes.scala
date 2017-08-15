package kafka

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

trait VeeamLogBundleEvent {
  val state: String
  val uri: String
  val handler_id: String
  val size: Int
  val last_modified: DateTime

  def formatTime: String = ISODateTimeFormat.dateTime.print(last_modified)

  override def equals(obj: scala.Any): Boolean = {
    val that=obj.asInstanceOf[VeeamLogBundleEvent]
    (this.uri==that.uri) && (this.last_modified==that.last_modified) && (this.size==that.size)
  }
}

case class AwaitingDownload(
                               state: String = "AwaitingDownload",
                               uri: String,
                               handler_id: String,
                               size: Int,
                               last_modified: DateTime
                             ) extends VeeamLogBundleEvent

case class BeingDownloaded(
                              state: String = "BeingDownloaded",
                              uri: String,
                              handler_id: String,
                              size: Int,
                              last_modified: DateTime
                            ) extends VeeamLogBundleEvent

case class NoLongerAvailable(
                                state: String = "NoLongerAvailable",
                                uri: String,
                                handler_id: String,
                                size: Int,
                                last_modified: DateTime
                              ) extends VeeamLogBundleEvent

case class AvailableForProcessing(
                                     state: String = "AvailableForProcessing",
                                     uri: String,
                                     handler_id: String,
                                     size: Int,
                                     last_modified: DateTime
                                   ) extends VeeamLogBundleEvent

case class QueuedForProcessing(
                                  state: String = "QueuedForProcessing",
                                  uri: String,
                                  handler_id: String,
                                  size: Int,
                                  last_modified: DateTime
                                ) extends VeeamLogBundleEvent

  case class ReadyForCleanup(
                              state: String = "ReadyForCleanup",
                              uri: String,
                              handler_id: String,
                              size: Int,
                              last_modified: DateTime
                            ) extends VeeamLogBundleEvent