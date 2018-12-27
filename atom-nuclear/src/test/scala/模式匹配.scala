import org.junit.Test

class 模式匹配 {

  @Test
  def 模式匹配测试: Unit = {
    // 匹配一个数组，它由三个元素组成，第一个元素为1，第二个元素为2，第三个元素为3
    Array(1, 2, 3) match {
      case Array(1, 2, 3) => println("ok")
    }
    // 匹配一个数组，它至少由一个元素组成，第一个元素为1
    Array(1, 2, 3) match {
      case Array(1, _*) => println("ok")
    }
    // 匹配一个List，它由三个元素组成，第一个元素为“A"，第二个元素任意类型，第三个元素为"C"
    List("A", "B", "C") match {
      case List("A", _, "C") => println("ok")
    }
    val a = 100
    // 常量模式，如果a与100相等则匹配成功
    a match {
      case 100 => println("ok")
    }
    a match {
      case _: Int => println("ok")
    }
  }

  //在 scala里对pattern有明确的定义，在形式上有以下几种pattern：
  //1)常量模式(constant patterns) 包含常量变量和常量字面量
  @Test
  def 常量模式: Unit = {
    val site = "alibaba.com"
    site match {
      case "alibaba.com" => println("ok")
    }
    val ALIBABA = "alibaba.com"
    //注意这里常量必须以大写字母开头
    site match {
      case ALIBABA => println("ok")
    }
  }

  //2) 变量模式(variable patterns)
  @Test
  def 变量模式: Unit = {
    val site = "小黑喵"
    site match {
      case whateverName => println(whateverName)
    }
  }

  //  3) 通配符模式(wildcard patterns)
  //
  //  通配符用下划线表示："_" ，可以理解成一个特殊的变量或占位符。
  //  单纯的通配符模式通常在模式匹配的最后一行出现，case _ => 它可以匹配任何对象，用于处理所有其它匹配不成功的情况。
  //  通配符模式也常和其他模式组合使用：
  def 通配符模式: Unit = {
    List(1, 2, 3) match {
      case List(_, _, 3) => println("ok")
    }
  }

  //  4) 构造器模式(constructor patterns)
  //
  //  这个是真正能体现模式匹配威力的一个模式！
  //  我们来定义一个二叉树：
  @Test
  def 构造器模式: Unit = {
    trait Node
    case class TreeNode(v: String, left: Node, right: Node) extends Node
    case class Tree(root: TreeNode)
    val tree = Tree(TreeNode("root", TreeNode("left", null, null), TreeNode("right", null, null)))
    tree.root match {
      case TreeNode(_, TreeNode("left", _, _), TreeNode("right", null, null)) =>
        println("bingo")
    }
  }

  //5) 类型模式(type patterns)
  //类型模式很简单，就是判断对象是否是某种类型：
  @Test
  def 类型模式: Unit ={
    "hello" match { case _:String => println("ok") }
    // 跟 isInstanceOf 判断类型的效果一样，需要注意的是scala匹配泛型时要注意
    val a:Any = List("1","2")
     a match {
      case a :List[String] => println("ok")
      case _ =>  println("other")
    }
  }

  // 6) 变量绑定模式 (variable binding patterns)
  // 这个和前边的变量模式有什么不同？看一下代码就清楚了：
  // 依然是上面的TreeNode，如果我们希望匹配到左边节点值为”left”就返回这个节点的话：
  @Test
  def 变量绑定模式: Unit = {
    trait Node
    case class TreeNode(v: String, left: Node, right: Node) extends Node
    case class Tree(root: TreeNode)
    val tree = Tree(TreeNode("root", TreeNode("left", null, null), TreeNode("right", null, null)))
    tree.root match {
      case TreeNode(_, leftNode@TreeNode("left",_,_), _) =>
        println(leftNode.v)
    }
  }
}
