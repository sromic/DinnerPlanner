package dal.food

import dal.Repositories
import dal.item.ItemRepositoryImpl
import domain.food.{Food, Foods}
//import slick.driver.H2Driver.api._
//import slick.driver.MySQLDriver.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object FoodRepositoryImpl extends FoodRepository with Repositories {
  import database.DatabaseProfile.profile.api._

  override def getFoodName: Future[Seq[String]] = {
    val foodNames = (for {
      food <- foodTable
    } yield food.name).groupBy(name => name).map(_._1)

    db.run(foodNames.result)
  }

  override def selectFood(items: Seq[String]): Future[String] = {
    val foodItem = (for {
      food <- foodTable if food.itemName inSetBind items
    } yield (food, food.itemName)).groupBy(_._1.name).map{ case (p, i) => (p, i.length)
    }.sortBy(_._2.desc).map { case (food, i) => food }

    db.run(foodItem.result.head)
  }

  override def getFoodItems(foodName: String): Future[Seq[String]] = {
    val itemNames =
      for {
        food <- foodTable if food.name === foodName
        item <- itemsTable if item.name === food.itemName
      } yield item.name

    db.run(itemNames.result)
  }

  override def getMissingItemsForFood(foodName: String, expiryItemsList: Seq[String]): Future[Seq[String]] = {
    val foodItems = getFoodItems(foodName)

    foodItems.map { items =>
      val missingItems = items.filterNot(expiryItemsList.contains)
      println(s"MissingItems are: ${missingItems}")

      if(missingItems == 0) List()
      else {
          val result = ItemRepositoryImpl.getMissingItems(missingItems)

        //await for first future to finsh, to avoid composing future inside future
          Await.result(result, 1 second)
      }
    }
  }

  override def getFoodByName(foodName: String): Future[Food] = {
    val food = foodTable.filter { _.name === foodName }

    db.run(food.result.head)
  }
}
