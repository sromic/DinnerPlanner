package actor

import akka.actor.{Actor, ActorLogging}
import message.Messages.{Prepare, Prepared}

class StoveActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case Prepare(foodName) =>

      log.info(s"StoveActor received food: ${foodName} to prepare")

      prepareDinner(foodName)
  }

  def prepareDinner(foodName: String) = {
    val origin = sender

      println(s"returned food is ${foodName}")

      origin ! Prepared(foodName)
  }
}
