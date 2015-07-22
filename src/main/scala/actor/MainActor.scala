package actor

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.util.Timeout
import dal.food.FoodRepositoryImpl
import dal.item.ItemRepositoryImpl
import domain.food.Food
import domain.items.Item
import message.Messages._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._


class MainActor(fridgeActor: ActorRef, storeActor: ActorRef, stoveActor: ActorRef, microwaveActor: ActorRef, chefActor: ActorRef) extends Actor with ActorLogging {

  implicit val timeout = Timeout(5 seconds)

  var m_selectedFood: String = _

  override def receive: Receive = {
    case ExpiredItems(items) =>
      log.info("MainActor received expiry items from FridgeActor")
      items.foreach(item => println(s"Received soon expiry item from FridgeActor is: ${item}"))

      val selectedFoodFuture = foodSelection(items)

      selectedFoodFuture.map { selectedFood =>
        m_selectedFood = selectedFood

        log.info(s"Selected food is: ${m_selectedFood}")

        checkIfAllItemsAvailable(items).map {
          _ match {
            case true => fridgeActor ! RemoveItemsFromFridge(m_selectedFood)

            case false => getMissingItems(items). map { items =>
              log.info(s"Missing food item are: ${items}")
              storeActor ! MissingItems(items)
            }
          }
        }
      }

    case Start =>
      log.info("Starting MainActor ...")
      log.info("Sending ExpiredItems message to FridgeActor...")
      fridgeActor ! ExpiredItems

   /* case MissingItems(items) => log.info("Received MissingItems from FridgeActor... Having all items now...")
      log.info("Sending Prepare message to MicrowaveActor...")
      microwaveActor ! Prepare(m_selectedFood)*/

/*    case ItemNotAvailable(items) =>
      log.info(s"Need to buy items...${items}")
      log.info("Sending MissingItems to StoreActor in order to buy them...")
      storeActor ! MissingItems(items)*/

    case FridgeIsEmpty =>
      log.info(s"MainActore received FridgeIsEmpty message from ${sender}")
      getAllItems.map { items =>
        storeActor ! MissingItems(items.map { _.name })
      }

    case Bought(items) =>
      log.info("Received Bought message from StoreActor")
      items.foreach( item => log.info(s"Bought ites is: ${item}"))
      m_selectedFood match {
        case selectedFood if !Option(selectedFood).getOrElse("").isEmpty =>
          fridgeActor ! RemoveItemsFromFridge(m_selectedFood)
        case selectedFood if Option(selectedFood).getOrElse("").isEmpty =>
          log.info("Items are bought ask FridgeActor to rescan...")
          fridgeActor ! ExpiredItems
      }

    case Prepared(foodName) =>
      log.info(s"Received Prepared(${foodName}) message from ${sender}.. \n Sending ReadyToCook(${foodName}) message to ChefActor..")
      chefActor ! ReadyToCook(foodName)

    case ItemsRemovedSuccessful =>
      log.info("Received message ItemRemovedSuccessful from FridgeActor")

      getFoodByName(m_selectedFood).map { food =>
        food.machineType match {
          case 1L =>
            log.info(s"Sending Prepare(${m_selectedFood}) to MicrowaveActor...")
            microwaveActor ! Prepare(m_selectedFood)

          case 2L =>
            log.info(s"Sending Prepare(${m_selectedFood}) to StoveActor...")
            stoveActor ! Prepare(m_selectedFood)
        }
      }
  }

  def foodSelection(expiryItems: Seq[String]): Future[String] = {
    FoodRepositoryImpl.selectFood(expiryItems).mapTo[String]
  }

  def checkIfAllItemsAvailable(expiryItems: Seq[String]): Future[Boolean] = {
    FoodRepositoryImpl.getFoodItems(m_selectedFood).map { foodItems =>
      foodItems.filterNot(expiryItems.contains).isEmpty
    }
  }

  def getMissingItems(expiryItems: Seq[String]): Future[Seq[String]] = {
    FoodRepositoryImpl.getFoodItems(m_selectedFood).map { foodItems =>
      foodItems.filterNot(expiryItems.contains)
    }
  }

  def getFoodByName(foodName: String): Future[Food] = {
    FoodRepositoryImpl.getFoodByName(foodName)
  }

  def getAllItems: Future[Seq[Item]] = {
    ItemRepositoryImpl.getAllItems
  }
}

