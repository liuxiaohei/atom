package org.atom.fsm

/**
  * 用互相递归模式不用框架实现的
  * 物态转化的状态机
  */
class Phases {

  class Transition

  case object Ionization extends Transition   // 电离
  case object Deionization extends Transition // 去离子化
  case object Vaporization extends Transition // 汽化
  case object Condensation extends Transition // 液化
  case object Freezing extends Transition     // 固化
  case object Melting extends Transition      // 熔化
  case object Sublimation extends Transition  // 升华
  case object Deposition extends Transition   // 凝华

  /**
    * 等离子体
    */
  def plasma(transitions: List[Transition]):Boolean = {
    println("-等离子体-")
    transitions match {
      case Nil => true
      case Deionization :: restTransitions => vapor(restTransitions)
      case _ => false
    }
  }

  /**
    * 蒸汽
    */
  def vapor(transitions: List[Transition]):Boolean = {
    println("-蒸汽-")
    transitions match {
      // 模式匹配 会在序列的首个元素有效的时候使用模式匹配获取该元素,
      // 并调用函数转换到合适的状态,同时将剩下的转换过程作为入参传入该元素
      case Nil => true;
      case Condensation :: restTransitions => liquid(restTransitions)
      case Deionization :: restTransitions => solid(restTransitions)
      case Ionization :: restTransitions => plasma(restTransitions)
      case _ => false;
    }
  }

  /**
    * 液体
    */
  def liquid(transitions: List[Transition]):Boolean = {
    println("-液体-")
    transitions match {
      case Nil => true;
      case Vaporization :: restTransitions => vapor(restTransitions)
      case Freezing :: restTransitions => solid(restTransitions)
      case _ => false;
    }
  }
  /**
    * 固体
    */
  def solid(transitions: List[Transition]):Boolean = {
    println("-固体-")
    transitions match {
      case Nil => true
      case Melting :: restTransitions => liquid(restTransitions)
      case Sublimation :: restTransitions => vapor(restTransitions)
      case _ => false
    }
  }

}
