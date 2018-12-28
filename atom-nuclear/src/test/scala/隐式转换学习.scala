import org.junit.Test

class 隐式转换学习 {

  //  摘要：
  //  通过隐式转换，程序员可以在编写Scala程序时故意漏掉一些信息，让编译器去尝试在编译期间自动推导出这些信息来，
  //  这种特性可以极大的减少代码量，忽略那些冗长，过于细节的代码。

  //  隐式转换的前提：
  //  1.不存在二义性（如例1）
  //
  //  2.隐式操作不能嵌套使用（如 convert1(covert2(x))）+y
  //
  //  3.代码能够在不使用隐式转换的前提下能编译通过，就不会进行隐式黑铁

  //  使用方式：
  //  1.将方法或变量标记为implicit
  //  2.将方法的参数列表标记为implicit
  //  3.将类标记为implicit
  //
  //  Scala支持两种形式的隐式转换：
  //  隐式值：用于给方法提供参数
  //  隐式视图：用于类型间转换或使针对某类型的方法能调用成功

  def person(implicit name: String) = name

  implicit val p = "mobin"

  @Test
  def 隐式值: Unit = {
    println(person) // 语法报错
  }

  implicit def toString(a: Int): String = a + ""

  @Test
  def 隐式视图1: Unit = {
    val str: String = 1
  }

  class SwingType {
    def wantLearned(sw: String) = println("兔子已经学会了" + sw)
  }

  object swimming {
    implicit def learningType(s: AminalType) = new SwingType
  }

  class AminalType

  object AminalType extends App {

    import swimming._

    val rabbit = new AminalType
    rabbit wantLearned "breaststroke" //蛙泳
  }

  //  像intToString，learningType这类的方法就是隐式视图，通常为Int => String的视图，定义的格式如下：
  //  implicit def  originalToTarget (<argument> : OriginalType) : TargetType
  //  其通常用在于以两种场合中：
  //  1.如果表达式不符合编译器要求的类型，编译器就会在作用域范围内查找能够使之符合要求的隐式视图。如例2，当要传一个整数类型给要求是字符串类型参数的方法时，在作用域里就必须存在Int => String的隐式视图
  //
  //  2.给定一个选择e.t，如果e的类型里并没有成员t，则编译器会查找能应用到e类型并且返回类型包含成员t的隐式视图

  object Stringutils {

    implicit class StringImprovement(val s: String) { //隐式类
      def increment = s.map(x => (x + 1).toChar)
    }

  }

  object Main extends App {

    import Stringutils._

    println("mobin".increment)
  }

  //编译器在mobin对象调用increment时发现对象上并没有increment方法，此时编译器
  // 就会在作用域范围内搜索隐式实体，发现有符合的隐式类可以用来转换成带有increment方法的StringImprovement类，最终调用increment方法。

}
