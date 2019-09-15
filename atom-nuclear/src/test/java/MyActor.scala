import akka.actor.{Actor, Props}
import akka.event.Logging

/**
  * 创建一个Actor 样例
  */
class MyActor extends Actor {
  val log = Logging(context.system, this)

  override def postRestart(reason: Throwable): Unit = println("postRestart")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = println("preRestart")

  def receive = {
    case "test" ⇒ log.info("received test")
    case _      ⇒ log.info("received unknown message")
  }
}

class FirstActor extends Actor {
  import context._
  val myActor = actorOf(Props[MyActor], name = "myactor")
  def receive = {
    case x ⇒ myActor ! x
  }
}