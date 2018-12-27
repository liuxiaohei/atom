import org.junit.Test

class Option学习 {

  def toInt(s: String): Option[Int] = {
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      case e: Exception => None
    }
  }

  @Test
  def demo1: Unit = {
    println(toInt("aaa").getOrElse("-1"))
    println(toInt("1").getOrElse("-1"))
  }

  @Test
  def demo2: Unit = {
    toInt("1").foreach {
      i => println(i)
    }
  }

  @Test
  def demo3: Unit = {
    toInt("1") match {
      case Some(i) => println(i)
      case None => println("That didn't work.")
    }

    toInt("aaa") match {
      case Some(i) => println(i)
      case None => println("That didn't work.")
    }
  }


}

