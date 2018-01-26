package utils

import org.scalafmt.Scalafmt

object ScalaFmtHelper {
  private val style                    = org.scalafmt.config.Config.fromHoconString(scala.io.Source.fromFile(".scalafmt.conf").mkString).get
  def formatCode(code: String): String = Scalafmt.format(code, style).get
}
