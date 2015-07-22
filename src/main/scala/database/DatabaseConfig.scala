package database

import environment.Environment._

trait DatabaseConfig {
  import DatabaseProfile.profile.api._

  val db = getDatabase

  def getDatabase: Database = {
    environmentMode match {
      case PROD => Database.forConfig("mysql")
      case DEV => Database.forConfig("h2mem")
      case _ => Database.forConfig("h2mem")
    }
  }

}
