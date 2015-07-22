package dal.item

import database.DatabaseConfig
import domain.items.Item

import scala.concurrent.Future

trait ItemRepository extends DatabaseConfig {

  /**
   * Select first 5 items according to expiry date if there is any item available
   * @return item names
   */
  def getExpiryItem: Future[Seq[String]]

  /**
   * Get missing items from fridge
   * @param missingItemList
   * @return missing items to MainActor
   */
  def getMissingItems(missingItemList: Seq[String]): Future[Seq[String]]

  /**
   * If there some items are not available in fridge, place order to buy missing items
   * @param items
   */
  def buyItems(items: Seq[String]): Future[Unit]

  /**
   * Remove selected food's items from fridge, update their quantity in database
   * @param foodName
   */
  def removeItemsFromFridge(foodName: String): Future[Unit]

  /**
   * Return all items, this method is needed when fridge is empty
   * @return
   */
  def getAllItems: Future[Seq[Item]]

  /**
   * Return list of Items based on given names
   * @param itemsNameList
   * @return
   */
  def getItemsByNames(itemsNameList: Seq[String]): Future[Seq[Item]]

  def getFoodItems(foodName: String): Future[Seq[Item]]

  /**
   * Update given item in database
   * @param item
   * @return
   */
  def updateItem(item: Item): Future[Int]

}