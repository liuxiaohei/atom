import org.atom.tools.Logger
import org.junit.Test

class DemoTest {

  @Test
  def demo(): Unit = {
    Logger.newInstance(classOf[DemoTest]).info("a demo test")
  }

  @Test
  def enumTest(): Unit = {
  }
}
