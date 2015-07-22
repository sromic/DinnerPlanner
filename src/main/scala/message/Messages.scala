package message

/**
 * Created by simun on 16.7.2015..
 */
object Messages {
  //Ask Fridge actor for getting soon expired items
  case object ExpiredItems
  //response message from Fridge actor with soon expired actors
  case class ExpiredItems(items: Seq[String])

  //ask Fridge actor to get missing food items, based on food name and soon expired items
  case class MissingFoodItems(foodName: String, items: Seq[String])
  //response message from Fridge actor with missing items
  //ask Store actor to order missing items
  case class MissingItems(items: Seq[String])

  case class RemoveItemsFromFridge(foodName: String)
  case object ItemsRemovedSuccessful

  //Response message from Fridge actor
  case class ItemNotAvailable(items: Seq[String])
  //Response message from Fridge actor where there are not any item to Main actor
  case object FridgeIsEmpty

  //response message from Store actor to Main actor with bought items
  case class Bought(items: Seq[String])
  //message to Microwave and Stove actor for preparing dinner
  case class Prepare(foodName: String)
  //response message from Microwave and Stove actor to Main actor that they are prepared for food to be inserted
  case class Prepared(foodName: String)

  //message from MainActor to ChiefActor that everything is ready to start cooking
  case class ReadyToCook(foodName: String)

  //Start Main actor
  case object Start
}
