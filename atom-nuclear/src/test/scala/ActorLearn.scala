import org.junit.Test

class ActorLearn {

  import akka.actor.ActorDSL._
  import akka.actor.ActorSystem
  implicit val system = ActorSystem("MySystem")

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

  @Test
  def demo2(): Unit = {
    val actor3 = actor(new Act {
      become {
        case "start" => println("starting...")
        case AsynMsg(id,msg) =>
          println("asynMsg " +  "id:" + id + " msg:" + msg)
          Thread sleep 50
          sender() ! ReplyMsg(id,"success") // 发送异步消息给发送者
        case SyncMsg(id,msg) =>
          println("syncMsg " +  "id:" + id + " msg:" + msg)
          Thread sleep 50
          sender() ! ReplyMsg(id,"success") // 发送消息给发送者
      }
    })
    actor3 ! "start"           // 发送 start
    actor3 ! AsynMsg(1,"demo") // 没有返回值的异步消息
    actor3 ! SyncMsg(2,"hello")
  }

  case class AsynMsg(id:Int,msg:String) // 异步消息
  case class SyncMsg(id:Int,msg:String) // 同步消息
  case class ReplyMsg(id:Int,msg:String)// 返回消息
}
