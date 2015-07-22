import actor._
import akka.actor.{ActorSystem, Props, _}
import akka.util.Timeout
import dal.Repositories
import dal.item.ItemRepositoryImpl
import database.DatabaseConfig
import domain.food._
import domain.items.{Item, Items}
import domain.machine.{Machine, Machines}
import message.Messages
import org.joda.time.DateTime
//import slick.driver.H2Driver.api._
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App with DatabaseConfig with Repositories {

  //try {
    val items: TableQuery[Items] = Items.table

    val foods: TableQuery[Foods] = Foods.table

    val machines: TableQuery[Machines] = Machines.table

    val setup = DBIO.seq(
    (machines.schema ++ items.schema ++ foods.schema).create,
    //create machines
    machines += Machine(Some(1L), "microwave"),
    machines += Machine(Some(2L), "stove"),

    //fill fridge with some items
    items += Item(None, "milk", new DateTime(), 0),
    items += Item(None, "salt", new DateTime(), 0),
    items += Item(None, "sauce", new DateTime(), 0),
    items += Item(None, "pepper", new DateTime(), 0),
    items += Item(None, "bacon", new DateTime(), 0),
    items += Item(None, "carrot", new DateTime(), 0),

    //define some food with items required and machine to cook
    foods += Food(Some(1L), "pizza", "milk", 1L),
    foods += Food(Some(2L), "pizza", "salt", 1L),
    foods += Food(Some(3L), "pasta", "milk", 2L),
    foods += Food(Some(3L), "pasta", "salt", 2L),
    foods += Food(Some(3L), "pasta", "sauce", 2L)

    )

  println(setup)
    //println SQL statement for creating tables
    //(machines.schema ++ items.schema ++ foods.schema).createStatements.foreach(println)
    //fill up database with some test data
    val setupFuture: Future[Unit] = db.run(setup)

      //create actor system
      val system = ActorSystem("smartHouse")
      //create actors
      val fridgeActor = system.actorOf(Props[FridgeActor] ,"fridgeActor")
      val storeActor = system.actorOf(Props[StoreActor] ,"storeActor")
      val microwaveActor = system.actorOf(Props[MicrowaveActor] ,"microwaveActor")
      val stoveActor = system.actorOf(Props[StoveActor] ,"stoveActor")
      val chefActor = system.actorOf(Props[ChefActor], "chefActor")

      val mainActor = system.actorOf(Props(new MainActor(fridgeActor, storeActor, stoveActor, microwaveActor, chefActor)), "mainActor")

      //start actor communication
      mainActor ! Messages.Start

}
