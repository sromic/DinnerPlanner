package actor

import akka.actor.{Actor, ActorLogging}
import message.Messages.{Prepare, Prepared}

/**
 * Created by simun on 16.7.2015..
 */
class MicrowaveActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case Prepare(foodName) =>
      log.info(s"MicrowaveActor received food: ${foodName} to prepare")
      prepareDinner(foodName)
  }

  def prepareDinner(foodName: String) = {
    val origin = sender

      log.info(s"MicrovaweActor is preparing microwave for food: ${foodName}...")
      origin ! Prepared(foodName)
  }
}
