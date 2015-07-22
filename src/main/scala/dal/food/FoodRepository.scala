package dal.food

import database.DatabaseConfig
import domain.food._

import scala.concurrent.Future

trait FoodRepository extends DatabaseConfig {
  /**
   * for given food return it's items
   * @param foodName
   * @return list of items for given food
   */
  def getFoodItems(foodName: String): Future[Seq[String]]

  /**
   * base on given items decide what food will be prepared with the best match
   * @param items
   * @return selected food based on given items
   */
  def selectFood(items: Seq[String]): Future[String]

  /**
   * Based on given food and soon expiry items get missing items from fridge
   * @param foodName
   * @param expiryItemsList
   * @return missing items for given food
   */
  def getMissingItemsForFood(foodName: String, expiryItemsList: Seq[String]): Future[Seq[String]]

  def getFoodByName(name: String): Future[Food]

  def getFoodName: Future[Seq[String]]

}
