import beans.DemoBean
import org.atom.tools.Logger
import org.junit.Test

class DemoTest {

  @Test
  def demo(): Unit = {
    Logger.newInstance(classOf[DemoTest]).info("a demo test")
    val a = new DemoBean
  }

  @Test
  def enumTest(): Unit = {
  }
}
