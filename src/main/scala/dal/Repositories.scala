package dal

import domain.food.Foods
import domain.items.Items

trait Repositories {

  lazy val itemsTable = Items.table

  lazy val foodTable = Foods.table
}
