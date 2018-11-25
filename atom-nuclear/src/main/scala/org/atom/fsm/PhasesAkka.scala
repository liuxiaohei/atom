package org.atom.fsm

import akka.actor.FSM
import org.atom.tools.Logger


/**
  * 使用Akka FSM 实现的
  * 物态转化的状态机
  * 个人感觉这种应该是状态机实现的最干净漂亮的手段了
  */
class PhasesAkka  {
  import States._
  import Actions._

  object States {
    sealed trait MachineState
    case object Plasma extends MachineState       // 等离子态
    case object Vapor  extends MachineState       // 气态
    case object Liquid extends MachineState       // 液态
    case object Solid  extends MachineState       // 固态
    case class MachineData(a: Int, b: Int, c: Int)// 状态机的数据类
  }

  object Actions {
    trait Transition
    case object Ionization   extends Transition   // 电离
    case object Deionization extends Transition   // 去离子化
    case object Vaporization extends Transition   // 汽化
    case object Condensation extends Transition   // 液化
    case object Freezing     extends Transition   // 固化
    case object Melting      extends Transition   // 熔化
    case object Sublimation  extends Transition   // 升华
    case object Deposition   extends Transition   // 凝华
  }

  class PhasesMachine extends FSM[MachineState, MachineData] {
    private val logger = Logger.newInstance(classOf[PhasesMachine])

    startWith(States.Liquid, States.MachineData( a = 0, b = 0, c = 0))

    // 等离子态
    when(Plasma) {
      case Event(Deionization,_) => goto(Vapor)  // 去离子化成为气态
    }

    // 气态
    when(Vapor) {
      case Event(Condensation,_) => goto(Liquid) // 液化成液体
      case Event(Deionization,_) => goto(Solid)  // 凝华成固态
      case Event(Ionization,_) => goto(Plasma)   // 等离子化
    }

    // 液态
    when(Liquid) {
      case Event(Vaporization,_) => goto(Vapor) // 蒸发成气态
      case Event(Freezing,_) => goto(Solid)     // 凝结成固态
    }

    // 固态
    when(Solid) {
      case Event(Melting,_) => goto(Liquid)     // 熔化成液态
      case Event(Sublimation,_) => goto(Vapor)  // 升华成气态
    }

    // 上面没有考虑到的状态
    whenUnhandled {
      case Event(_,_) => {
        logger.info("状态机错误")
        goto(Liquid)
      }
    }

    // 状态转化的自定义动作
    onTransition {
      case Plasma -> Vapor => logger.debug("等离子态转化为气态")
    }
  }

}
