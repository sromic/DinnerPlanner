package database

import slick.driver.{H2Driver, MySQLDriver, JdbcProfile}
import environment.Environment._

object DatabaseProfile {
  val profile: JdbcProfile = setJdbcProfile

  def setJdbcProfile: JdbcProfile = {
    environmentMode match {
      case PROD => MySQLDriver
      case DEV => H2Driver
      case _ => H2Driver
    }
  }
}
