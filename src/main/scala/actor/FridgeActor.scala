package actor

import akka.actor.{Actor, ActorLogging}
import dal.food.FoodRepositoryImpl
import dal.item.ItemRepositoryImpl
import message.Messages._

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by simun on 16.7.2015..
 */
class FridgeActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case ExpiredItems =>
      log.info("FridgeActor asked to get soon expired items")
      getExpiryItems

    case MissingFoodItems(foodName, items) =>
      log.info(s"FridgeActor asked to get missing ${items}")
      getMissingItemsForFood(foodName, items)

    case RemoveItemsFromFridge(foodName) =>
      val origin = sender
      log.info("Removing items from fridge")
      removeItemsFromFridge(foodName).map { _ =>
        origin ! ItemsRemovedSuccessful
      }
  }

  def getExpiryItems = {
    val origin = sender

    val expiryItems = ItemRepositoryImpl.getExpiryItem

    expiryItems.map { items =>
      log.info("Sending ExpiredItems message to MainActor...")
      items match {
        case list if list.isEmpty => origin ! FridgeIsEmpty
        case list if !list.isEmpty => origin ! ExpiredItems(items)
      }
    }
  }

  def getMissingItemsForFood(foodName: String, items: Seq[String]) = {
    val origin = sender
    log.info("Starting to get missing items from fridge....")
    FoodRepositoryImpl.getMissingItemsForFood(foodName, items).map { itemsList =>
      itemsList match {
        case list if list.isEmpty => origin ! FridgeIsEmpty

        case list if !list.isEmpty =>
          log.info("Removing items from fridge....")
          removeItemsFromFridge(foodName).map { _ =>
            origin ! MissingItems(list)
          }
      }
    }

  }

  def removeItemsFromFridge(foodName: String) = {
    ItemRepositoryImpl.removeItemsFromFridge(foodName)
  }
}
