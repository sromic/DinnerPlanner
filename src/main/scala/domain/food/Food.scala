package domain.food

import domain.machine.Machines
//import slick.driver.H2Driver.api._
//import slick.driver.MySQLDriver.api._
import database.DatabaseProfile.profile.api._

case class Food(id: Option[Long], name: String, itemName: String, machineType: Long)

class Foods(tag: Tag) extends Table[Food](tag, "foods") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)
  def itemName = column[String]("item_name", O.NotNull)
  def machineType = column[Long]("machine_type_fk")

  def machine = foreignKey("machine_fk", machineType, Machines.table)(_.id)

  //def item = foreignKey("item_fk", itemId, ItemsDataBase.items)(_.id)

  def * = (id.?, name, itemName, machineType) <> ((Food.apply _).tupled, Food.unapply)
}

object Foods {
  lazy val table = TableQuery[Foods]
}
