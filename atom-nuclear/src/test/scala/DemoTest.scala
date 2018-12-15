import org.atom.constant.正则表达式
import org.atom.fsm.{Phases, PhasestailRec}
import org.atom.functions.BaseTrait
import org.junit.Test

class DemoTest extends BaseTrait {

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

  //参照文档  https://doc.akka.io/docs/akka/2.5.13/

  import akka.actor.ActorDSL._
  import akka.actor.ActorSystem
  implicit val system = ActorSystem("demo")

  @Test
  def Akka样例 {
    var num = 0
    val a = actor(new Act {
      become {                          // this will replace the initial (empty) behavior
        case "info" => num = num + 1; println(num + "info");sender() ! "A"
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
    1 to 100000 foreach (e => a ! "info")
    a ! "info"
    a ! "lobotomize"
    println("end")
    Thread sleep 1000 // Scala 之中是不存在首检异常的
  }

  @Test
  def 对比 {
    1 to 100000 foreach(e => println(e + "info"))
    println("end")
  }

  @Test
  def json校验测试: Unit = {
    println(isValidJSON("[1,3,4,5,6,7,8,9,10]"))
  }

  @Test
  def 正则表达式测试: Unit = {
    println("😊" matches 正则表达式.emoji表情)
    println("1" matches 正则表达式.数字)
    println("1" matches 正则表达式.Email地址)
    println("1" matches 正则表达式.InternetURL)
    println("1" matches 正则表达式.域名)
    println("刘" matches 正则表达式.汉字)
  }
}
