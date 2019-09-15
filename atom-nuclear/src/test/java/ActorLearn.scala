import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.util.Timeout
import org.junit.Test

import scala.concurrent._
import scala.util.{Failure, Success}

class ActorLearn {

  import akka.actor.ActorDSL._
  import akka.pattern.ask
  implicit val system = ActorSystem("MySystem") // 隐式变量

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

  import ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5L,TimeUnit.SECONDS) //隐式变量设定1s为超时的时间

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
    val future = actor3 ? SyncMsg(2,"hello")
    future onSuccess {
      case ReplyMsg(id,msg) => println("reply:" + "id:" + id + "msg:" + msg)
    }

    future onFailure {
      case e => println("失败")
    }

    future onComplete {
      case Success(ReplyMsg(id,msg)) => println("reply2:" + "id:" + id + "msg:" + msg)
      case Failure(value) => println("失败")
    }
    Thread sleep 50
  }

  case class AsynMsg(id:Int,msg:String) // 异步消息
  case class SyncMsg(id:Int,msg:String) // 同步消息
  case class ReplyMsg(id:Int,msg:String)// 返回消息
}
