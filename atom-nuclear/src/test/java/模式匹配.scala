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
  def 类型模式: Unit = {
    "hello" match {
      case _: String => println("ok")
    }
    // 跟 isInstanceOf 判断类型的效果一样，需要注意的是scala匹配泛型时要注意
    val a: Any = List("1", "2")
    a match {
      case a: List[String] => println("ok")
      case _ => println("other")
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
      case TreeNode(_, leftNode@TreeNode("left", _, _), _) =>
        println(leftNode.v)
    }
  }

  def finall: Unit = {
    //1
    val data = 30
    data match {
      case 1 => println("First") //这是函数定义与实现的方式  =>左侧可以是变量和条件表达式 =>右侧可以是函数的实现
      case 2 => println("Second")
      case _ => println("Not know number") //case _ 代表所有的其它情况
    } //row result:Not know number
    //Scala中的模式匹配和Java中switch case相似
    //其区别在于scala在每个匹配完成之后自动break跳出不在往下匹配  等价于Java中的switch ... case : ... ; break;


    //2
    val result = data match {
      case i if i == 1 => "The First" //i 和 number 为val 用于接收传进来的data 他们在声明的时候会被实例化为data的内容
      case number if number == 2 => "Second"
      case _ => "Not Know Number"
    }
    println("result=" + result) //row result:Not know number

    //3
    "Spark !".foreach {
      c =>
        println(
          c match {
            case ' ' => "space"
            case ch => "Char:" + ch
          }
        )
    }

    /**
      * Char:S
      * Char:p
      * Char:a
      * Char:r
      * Char:k
      * space
      * Char:!
      */
    /**
      * 通过模式匹配判断参数的类型
      */
    def match_type(t: Any) = t match {
      case p: Int => println("It is Integer") //这里的p变量 val p: Int  t会赋值给变量p
      case p: String => println("It is String, the content is " + p)
      case m: Map[_, _] => m.foreach(println) //val m: Map[_, _]
      case _ => println("Unknow type!!!")
    }

    match_type(2) //It is Integer
    match_type("Spark") //It is String, the content is Spark
    match_type(Map("Scala" -> "Spark")) //(Scala,Spark)

    /**
      * 模式匹配数组
      */
    def match_array(arr: Any) = arr match {
      case Array(0) => println("Array" + "0")
      case Array(x, y) => println("Array" + x + " " + y) //匹配有两个元素的数组
      case Array(0, _*) => println("Array" + "0 ...") //匹配至少是1个元素，并且第一个元素为0的数组  _*表示后面有许多元素
      case _ => println("something else")
    }

    match_array(Array(0)) //Array0
    match_array(Array(0, 1)) //Array0 1
    match_array(Array(0, 1, 2, 3, 4, 5, 6)) //Array0 ...


    /**
      * 模式匹配List
      */
    def match_list(lst: Any) = lst match {
      case 0 :: Nil => println("List" + "0") //匹配有一个元素的且为0的集合
      case x :: y :: Nil => println("List" + x + " " + y) //匹配有两个元素的集合
      case 0 :: tail => println("List" + "0 ...") //x :: tail 表示开头的元素为0 tail表示剩下的若干个元素  匹配至少有一个元素的集合
      case _ => println("something else")
    }

    match_list(List(0)) //List0
    match_list(List(0, 1)) //List0 1
    match_list(List(0, 1, 2, 3, 4, 5)) //List0 ...

    /**
      * 模式匹配Tuple元祖
      */
    def match_tuple(tuple: Any) = tuple match {
      case (0, _) => println("Tuple" + "0") //匹配第一个元素为0，第二个元素任意
      case (x, 0) => println("Tuple:" + x)
      case _ => println("something else")
    }

    match_tuple((0, "Scala")) //Tuple0
    match_tuple((2, 0)) //Tuple:2
    match_tuple((0, 1, 2, 3, 4, 5)) //something else

  }

  //总结：模式匹配的核心功能是解构
  //  解构对象 (De-constructing objects)
  //
  //  Bill Venners: 你说模式像表达式，但它更像“逆表达式”，不同于插入值并得到结果(构造一个对象的过程)，
  // 你放入一个值，当它匹配，一串值弹出来。
  //
  //  Martin Odersky: 是的，它确实是反向构造，我可以通过嵌套的构造器来构造对象。我有一个方法一些参数，
  // 通过这些参数可以构造出复杂的对象结构。模式匹配正好相反，它从一个复杂的对象结构中抽出原来用于构造这个对象的参数
  //  可扩展性的两个方向(Two directions of extensibility)
  //
  //  扩展性的另一个概念是数据结构相对固定，你不想改变它，但你想要用到的行为操作是开放的。你随时都想要添加新的操作。
  //  典型的例子是编译器，编译器用语法树表达你的程序，只要你没有改变你的语言，语法树就不会变，一直都是同一颗树
  //  但编译器想要这棵语法树每天改变。明天你或许想到一种新的优化在遍历树的阶段。
  //
  //  所以，你想采取的办法是操作定义在你的语法树外部，否则你要不断的添加新方法
  //
  //  这个工作正确的方向，取决于你想在那个方向扩展，如果你想要扩展新的数据，你选择经典的面向对象通过虚方法调用实现。
  // 如果你想保持数据固定，扩展新的操作，模式更适合。实际上有一个设计模式，不要和模式匹配混淆，在面向对象程序中称为“访问者模式”，也可以用面向对象的方式表达模式匹配的方式，基于虚方法委派的。
  //
  //  但实际中用visitor模式是非常笨重的，不能像模式匹配那样轻松的做很多事。你应该终结笨重的vistors，
  // 同时在现代虚拟机技术中也证明vistor模式远没有模式匹配有效。所有这些原因，我想应该为模式匹配定义一套规则

  //  ps, 前段时间王垠同学在批判设计模式的一篇文章中，提到visitor模式就是模式匹配。
  //  可以对比一下scala语言通过case class/extractor方式在语言级别支持模式匹配，与通过visitor模式来达到同样的效果时的代码差别

}
