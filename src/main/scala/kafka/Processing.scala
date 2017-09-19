package kafka

import java.io.File

import kafka.model.URI

import scala.sys.process._
import scala.util.matching.Regex

class Processing {
  val http: Regex="^https?://[0-9a-z]+(.?)[0-9a-z]+(.?)[a-z]+(.?)[a-z]+:\\d{1,}/[a-z]+/[a-z0-9]+/[a-z]+/\\d{8}/[0-9-_a-zA-Z]+\\.zip".r
  val archive: Regex="[0-9-_a-zA-Z]+\\.zip".r

  def validated(uri: URI,
                pattern: String="^https?://[0-9a-z]+(.?)[0-9a-z]+(.?)[a-z]+(.?)[a-z]+:\\d{1,}/[a-z]+/[a-z0-9]+/[a-z]+/\\d{8}/[0-9-_a-zA-Z]+\\.zip\\?op=OPEN$"): Boolean={
    uri.uri.matches(pattern)
  }

  def processWithFunc[A](uri: URI, func: String => A, cleanup: Boolean=true): A = {
    val tempDir: String = System.getProperty("java.io.tmpdir")
    val fileName: String = archive.findFirstIn(uri.uri).get
    val linkToDownloadFrom: String = http.findFirstIn(uri.uri).get
    s"curl -o $tempDir\\$fileName $linkToDownloadFrom".!
    val file = new File(s"$tempDir\\$fileName")
    val result: A = func(fileName)
    if (cleanup) {
      if (file.exists()) file.delete()
    }
    result
  }

}