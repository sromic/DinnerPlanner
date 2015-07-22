package actor

import akka.actor.{Actor, ActorLogging}
import dal.item.ItemRepositoryImpl
import message.Messages.{Bought, MissingItems}

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by simun on 16.7.2015..
 */
class StoreActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case MissingItems(items) =>
      log.info(s"Receivde MissingItems(${items}) from MainActor...")
      buyItems(items)
  }

  def buyItems(items: Seq[String]) = {
    val origin = sender

    log.info("StoreActor is buying missing items...")
    ItemRepositoryImpl.buyItems(items).map { _ =>
      log.info(s"StoreActore is sending Bought(${items}) message to MainActor...")
      origin ! Bought(items)
    }

  }
}
