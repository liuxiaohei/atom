import beans.DemoBean
import org.junit.Test

class 中文类测试 {

  @Test
  def 样例 {
    val demobean = new DemoBean
    demobean.setName("小黑喵")
    println(demobean.getName)
  }

}
