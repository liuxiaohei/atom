import org.junit.Test

class AcotrLearn {

  import akka.actor.ActorDSL._
  import akka.actor.ActorSystem
  implicit val system = ActorSystem("demo")

  @Test
  def demo1(): Unit = {
    val actor1 = actor(new Act {
      become {
        case _ => 1 to 10 foreach(e => {
          println(e + "actor1")
          Thread sleep 50
        })
      }
    })
    val actor2 = actor(new Act {
      become {
        case _ => 1 to 10 foreach(e => {
          println(e + "actor2")
          Thread sleep 50
        })
      }
    })
    actor1 ! "info" // !代表发送信息不等待
    actor2 ! "info"
    Thread sleep 500
  }
}
