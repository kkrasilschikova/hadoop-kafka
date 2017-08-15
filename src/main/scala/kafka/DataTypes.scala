package kafka

import java.lang.management.ManagementFactory

case class HandlerID (hostname: String = java.net.InetAddress.getLocalHost.getHostName,
                 pid: Int = ManagementFactory.getRuntimeMXBean.getName.split("@")(0).toInt) {
  def getHandlerID(hostname: String, pid: Int): String =
    s"${hostname}_$pid"
}

case class URI (uri: String)