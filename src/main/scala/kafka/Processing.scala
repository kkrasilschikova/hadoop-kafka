package kafka

import java.io.File

import kafka.model.URI

import scala.sys.process._
import scala.util.matching.Regex

class Processing {
  val http: Regex="^https?://[0-9a-z]+(.*)[0-9a-z]+(.*)[a-z]+(.*)[a-z]+:\\d{1,}/[a-z]+/[a-z0-9]+/[a-z]+/\\d{8}/[0-9-_a-zA-Z]+(.*)zip".r
  val archive: Regex="[0-9-_a-zA-Z]+(.*)zip".r

  def validated(uri: URI,
                pattern: String="^https?://[0-9a-z]+(.*)[0-9a-z]+(.*)[a-z]+(.*)[a-z]+:\\d{1,}/[a-z]+/[a-z0-9]+/[a-z]+/\\d{8}/[0-9-_a-zA-Z]+(.*)zip\\?op=OPEN$"): Boolean={
    uri.uri.matches(pattern)
  }

  def processWithFunc[A](uri: URI, func: File=> A, cleanup: Boolean=true): A = {
    s"curl -O ${http.findFirstIn(uri.uri).get}".!!
    val file = new File(archive.findFirstIn(uri.uri).get)
    val result: A = func(file)
    if (cleanup) {
      if (file.exists()) file.delete()
    }
    result
  }
}