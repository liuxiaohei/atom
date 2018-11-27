import java.util.stream.IntStream

import org.atom.fsm.{Phases, PhasestailRec}
import org.junit.Test

class DemoTest {

  @Test
  def 互相递归模式实现的状态机 {
    val a = new Phases
    println("气体电离" + a.vapor(List(a.Ionization)))
    println("固体电离" + a.solid(List(a.Ionization)))
    println("液体电离" + a.liquid(List(a.Ionization)))
    println("等离子体电离" + a.plasma(List(a.Ionization)))

    println("气体熔化" + a.vapor(List(a.Melting)))
    println("固体熔化" + a.solid(List(a.Melting)))
    println("液体熔化" + a.liquid(List(a.Melting)))
    println("等离子体熔化" + a.plasma(List(a.Melting)))

    println("气体汽化" + a.vapor(List(a.Vaporization)))
    println("固体汽化" + a.solid(List(a.Vaporization)))
    println("液体汽化" + a.liquid(List(a.Vaporization)))
    println("等离子体汽化" + a.plasma(List(a.Vaporization)))

    println("气体升华" + a.vapor(List(a.Sublimation)))
    println("固体升华" + a.solid(List(a.Sublimation)))
    println("液体升华" + a.liquid(List(a.Sublimation)))
    println("等离子体升华" + a.plasma(List(a.Sublimation)))
  }

  @Test
  def 状态组合 {
    val a = new Phases
    println("固体熔化 + 升华" + a.solid(List(a.Melting,a.Sublimation)))
    println("------")
    println("固体电离 + 升华" + a.solid(List(a.Ionization,a.Sublimation)))
    println("------")
    println("固体熔化 + 熔化" + a.solid(List(a.Melting,a.Melting)))
    println("------")
    println("固体熔化 + 汽化" + a.solid(List(a.Melting,a.Vaporization)))
  }

  @Test
  def 尾递归优化 {
    val a = new PhasestailRec
    println("固体熔化 + 升华" + a.solid(List(a.Melting,a.Sublimation)).result)
    println("------")
    println("固体电离 + 升华" + a.solid(List(a.Ionization,a.Sublimation)).result)
    println("------")
    println("固体熔化 + 熔化" + a.solid(List(a.Melting,a.Melting)).result)
    println("------")
    println("固体熔化 + 汽化" + a.solid(List(a.Melting,a.Vaporization)).result)
  }

  @Test
  def 测试 {
    val 槑 = "😯"
    println("测试中文方法" + 槑)
  }

  @Test
  def ಥ_ಥ {
    val 槑 = "😯"
    println("测试表情符方法" + 槑)
  }

  import akka.actor.ActorDSL._
  import akka.actor.ActorSystem
  implicit val system = ActorSystem("demo")

  @Test
  def 异步终极武器Akka打印一千万条数据 {
    val a = actor(new Act {
      become {                          // this will replace the initial (empty) behavior
        case "info" => println("info");sender() ! "A"
        case "switch" =>
          becomeStacked {               // this will stack upon the "A" behavior
            case "info"   => sender() ! "B"
            case "switch" => unbecome() // return to the "A" behavior
          }
        case "lobotomize" => println("lobotomize"); unbecome() // OH NOES: Actor.emptyBehavior
      }

      superviseWith(OneForOneStrategy() {
        case e: Exception if e.getMessage == "hello" => Stop
        case _: Exception                            => Resume
      })
    })
    IntStream.rangeClosed(1,10000000).forEach(e => a ! "info")
    a ! "info"
    a ! "lobotomize"
  }

  @Test
  def 传统手段打印一百万条数据 {
    IntStream.rangeClosed(1,1000000).forEach(e => println("info"))
  }
}
