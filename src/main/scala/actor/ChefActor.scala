package actor

import akka.actor.{Actor, ActorLogging}
import database.DatabaseConfig
import message.Messages.ReadyToCook

class ChefActor extends Actor with ActorLogging with DatabaseConfig {
  override def receive: Receive = {
    case ReadyToCook(foodName) => log.info(s"ChefActor received ReadyToCook(${foodName}) from MainActor... \n Everything is ready for cooking... \n Closing database!")
      db.close()
      log.info("Terminate actor system! \n The end!")
      context.system.terminate()
  }
}
