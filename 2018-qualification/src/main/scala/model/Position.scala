package model
import scala.beans.BeanProperty
import scala.math.abs

case class Position(@BeanProperty row: Integer, @BeanProperty column: Integer)

object Position {
  def distance(a: Position, b: Position) = {
    abs(a.row - b.row) + abs(a.column - b.column)
  }
  val Origin: Position = Position(0,0)
}

