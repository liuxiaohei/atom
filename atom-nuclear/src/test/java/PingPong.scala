import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}

import scala.language.postfixOps

case object Ping
case object Pong

class Pinger extends Actor {
  var countDown = 10000
  def receive = {
    case Pong ⇒
      println(s"${self.path} received pong, count down $countDown")

      if (countDown > 0) {
        countDown -= 1
        sender() ! Ping
      } else {
        sender() ! PoisonPill // 毒药 杀死 Actor
        self ! PoisonPill
      }
  }
}

class Ponger(pinger: ActorRef) extends Actor {
  def receive = {
    case Ping ⇒
      println(s"${self.path} received ping")
      pinger ! Pong
  }
}

/**
  * Actor 使用样例
  */
object main extends App {
  val system = ActorSystem("pingpong")
  val pinger = system.actorOf(Props[Pinger], "pinger")
  val ponger = system.actorOf(Props(classOf[Ponger], pinger), "ponger")
  ponger ! Ping
}
