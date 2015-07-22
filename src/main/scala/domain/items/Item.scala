package domain.items

import org.joda.time.DateTime
//import slick.driver.H2Driver.api._
import slick.driver.MySQLDriver.api._
import com.github.tototoshi.slick.H2JodaSupport._


case class Item(id: Option[Long] = None, name: String, expDate: DateTime, quantity: Int)


class Items(tag: Tag) extends Table[Item](tag, "items") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)
  def expDate = column[DateTime]("exp_date", O.NotNull)
  def quantity = column[Int]("quantity")

  def * = (id.?, name, expDate, quantity) <> ((Item.apply _).tupled, Item.unapply)

}

object Items {
  lazy val table = TableQuery[Items]
}
