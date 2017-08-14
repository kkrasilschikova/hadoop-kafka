package kafka

import java.io.File

import scala.sys.process._
import scala.util.matching.Regex

class Processing {
  val http: Regex="^https?://[a-z]+(.*)[a-z]+(.*)[a-z]+(.*)[a-z]+:\\d{1,}/[a-z]+/[a-z0-9]+/\\d{1,}/\\d{4}-\\d{2}-\\d{2}T\\d{1,}_[a-zA-Z]+(.*)zip".r
  val archive: Regex="\\d{4}-\\d{2}-\\d{2}[A-Z]\\d{1,}_[a-zA-Z]+(.*)zip".r

  def validated(str: String, pattern: String="^https?://[a-z]+(.*)[a-z]+(.*)[a-z]+(.*)[a-z]+:\\d{1,}/[a-z]+/[a-z0-9]+/\\d{1,}/\\d{4}-\\d{2}-\\d{2}T\\d{1,}_[a-zA-Z]+(.*)zip\\?op=OPEN$"): Boolean={
    str.matches(pattern)
  }

  def processWithFunc[A](uri: String, func: File=> A, cleanup: Boolean=true): A = {
    s"curl -O ${http.findFirstIn(uri).get}".!!
    val file = new File(archive.findFirstIn(uri).get)
    val result: A = func(file)
    if (cleanup) {
      if (file.exists()) file.delete()
    }
    result
  }

}
