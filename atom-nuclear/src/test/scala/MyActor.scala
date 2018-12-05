import akka.actor.Actor
import akka.event.Logging

/**
  * 创建一个Actor 样例
  */
class MyActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case "test" ⇒ log.info("received test")
    case _      ⇒ log.info("received unknown message")
  }
}
