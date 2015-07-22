package database

import slick.driver.H2Driver.api._

trait DatabaseConfig {

  lazy val db = Database.forConfig("mysql")

}
