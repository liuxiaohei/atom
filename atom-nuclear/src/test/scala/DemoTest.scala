import beans.DemoBean
import org.atom.fsm.{Phases, PhasestailRec}
import org.atom.tools.Logger
import org.junit.Test

class DemoTest {

  @Test
  def demo(): Unit = {
    Logger.newInstance(classOf[DemoTest]).info("a demo test")
    val demobean = new DemoBean
  }

  /**
    * 互相递归模式实现的状态机
    */
  @Test
  def fsmTest0(): Unit = {
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
  def  fsmTest1(): Unit = {
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
  def  fsmTest2(): Unit = {
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
  def 测试(): Unit = {
    val 槑 = "😯"
    println("测试中文方法" + 槑)
  }
}
