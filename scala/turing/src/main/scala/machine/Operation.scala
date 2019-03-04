package machine

import machine.encode.StandardDescription

/**
  * operation
  *
  * print Symbol and move {Right/Left/None}
  */
trait Operation

case class R(p: S) extends Operation

case class L(p: S) extends Operation

case class N(p: S) extends Operation

object Operation {
  def sd[T <: Operation](op: T)(implicit ev: StandardDescription[T]): String = ev.encode(op)
}