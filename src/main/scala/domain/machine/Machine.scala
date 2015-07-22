package domain.machine

//import slick.driver.H2Driver.api._
//import slick.driver.MySQLDriver.api._
import database.DatabaseProfile.profile.api._

case class Machine(id: Option[Long], name: String)

class Machines(tag: Tag) extends Table[Machine](tag, "machines") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc, O.NotNull)
  def name = column[String]("name", O.NotNull)

  def * = (id.?, name) <> ((Machine.apply _).tupled, Machine.unapply)
}

object Machines {
  lazy val table = TableQuery[Machines]
}
