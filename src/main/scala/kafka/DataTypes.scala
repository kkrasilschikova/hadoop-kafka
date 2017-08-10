package kafka

import java.lang.management.ManagementFactory

class HandlerID {
  val hostname: String = java.net.InetAddress.getLocalHost.getHostName
  val pid: Int = ManagementFactory.getRuntimeMXBean.getName.split("@")(0).toInt

  def getHandlerID(hostname: String, pid: Int): String =
    s"${hostname}_$pid"
}

abstract class URI{
  val uri: String
}



