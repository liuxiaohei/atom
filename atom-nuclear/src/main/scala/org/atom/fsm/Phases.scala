package org.atom.fsm

import scala.util.control.TailCalls
import scala.util.control.TailCalls.TailRec


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
  def plasma(transitions: List[Transition]):TailRec[Boolean] = transitions match {
    case Nil => TailCalls.done(true);
    case Deionization :: restTransitions => TailCalls.tailcall(vapor(restTransitions))
    case _ => TailCalls.done(false);
  }

  /**
    * 蒸汽
    */
  def vapor(transitions: List[Transition]):TailRec[Boolean] = transitions match {
    case Nil => TailCalls.done(true);
    case Condensation :: restTransitions => TailCalls.tailcall(liquid(restTransitions))
    case Deionization :: restTransitions => TailCalls.tailcall(solid(restTransitions))
    case Ionization :: restTransitions => TailCalls.tailcall(plasma(restTransitions))
    case _ => TailCalls.done(false);
  }

  /**
    * 液体
    */
  def liquid(transitions: List[Transition]):TailRec[Boolean] = transitions match {
    case Nil => TailCalls.done(true);
    case Vaporization :: restTransitions => TailCalls.tailcall(vapor(restTransitions))
    case Freezing :: restTransitions => TailCalls.tailcall(solid(restTransitions))
    case _ => TailCalls.done(false);
  }

  /**
    * 固体
    */
  def solid(transitions: List[Transition]):TailRec[Boolean] = transitions match {
    case Nil => TailCalls.done(true);
    case Melting :: restTransitions => TailCalls.tailcall(liquid(restTransitions))
    case Sublimation :: restTransitions => TailCalls.tailcall(vapor(restTransitions))
    case _ => TailCalls.done(false);
  }

}
