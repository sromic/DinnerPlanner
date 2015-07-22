package dal.item

import dal.Repositories

import domain.items.Item
import org.joda.time.DateTime

import scala.concurrent.{Future}
import scala.concurrent.ExecutionContext.Implicits.global

import com.github.tototoshi.slick.H2JodaSupport._

//import slick.driver.H2Driver.api._
import slick.driver.MySQLDriver.api._

object ItemRepositoryImpl extends ItemRepository with Repositories {
  override def getExpiryItem: Future[Seq[String]] = {
    val expiredItems = (for {
      item <- itemsTable if item.quantity > 0
    } yield item).sortBy(_.expDate.asc).map { _.name }.take(5)

    db.run(expiredItems.result)
  }

  override def getMissingItems(missingItemList: Seq[String]): Future[Seq[String]] = {
    val items = (for {
      item <- itemsTable if (item.name inSetBind missingItemList) && (item.quantity > 0)
    } yield item).map { _.name }

    db.run(items.result)
  }

  override def buyItems(itemsMissing: Seq[String]): Future[Unit] = {
    getItemsByNames(itemsMissing).map { items =>
      items.foreach { item: Item =>
        val newQuantity = item.quantity + 2
        val newExpiryDate = DateTime.now
        val newItem = item.copy(quantity = newQuantity, expDate = newExpiryDate)

        updateItem(newItem)
      }
    }
  }

  override def removeItemsFromFridge(foodName: String): Future[Unit] = {
   getFoodItems(foodName).map { items =>
     items.foreach { item =>
       println(s"Removing item: ${item}")
       val newQuantity = item.quantity - 1
       val newItem = item.copy(quantity = newQuantity)

       updateItem(newItem)
     }
   }
  }

  override def updateItem(newItem: Item): Future[Int] = {
    val updateStatement = for { item <- itemsTable if item.id === newItem.id} yield item
    db.run(updateStatement.update(newItem))
  }

  override def getAllItems: Future[Seq[Item]] = {
    db.run(itemsTable.result)
  }

  override def getItemsByNames(itemsNameList: Seq[String]): Future[Seq[Item]] = {
    val items = itemsTable.filter { _.name inSetBind itemsNameList }

    db.run(items.result)
  }

  override def getFoodItems(foodName: String): Future[Seq[Item]] = {
    val itemNames =
      for {
        food <- foodTable if food.name === foodName
        item <- itemsTable if item.name === food.itemName
      } yield item

    db.run(itemNames.result)
  }
}
