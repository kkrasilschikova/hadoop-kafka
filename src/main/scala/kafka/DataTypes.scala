package kafka

import java.lang.management.ManagementFactory

//ManagementFactory class is a factory class for getting managed beans for the Java platform
//RuntimeMXBean is management interface for the runtime system of the Java virtual machine
//getName() returns the name representing the running Java virtual machine, for example 7292@sup-a1483

case class HandlerID (hostname: String = java.net.InetAddress.getLocalHost.getHostName,
                 pid: Int = ManagementFactory.getRuntimeMXBean.getName.split("@")(0).toInt) {
  override def toString: String = s"${hostname}_$pid"
}

case class URI (uri: String)