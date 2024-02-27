package io.github.iltotore.iron.macros

import io.github.iltotore.iron.*
import io.github.iltotore.iron.compileTime.applyConstraint
import scala.compiletime.summonInline
import scala.quoted.*
object StringLiteral:
  inline def apply[C, T <: IronType[String, C]](inline sc: StringContext, inline args: Any*)(using inline c: Constraint[String, C]): T =
    ${ applyImpl('{ sc }, '{ c }, '{ args }) }
  private def applyImpl[C, T <: IronType[String, C]](
      stringContextExpr: Expr[StringContext],
      constraintExpr: Expr[Constraint[String, C]],
      argsExpr: Expr[Any]*
  )(using q: Quotes): Expr[T] =
    val parts = stringContextExpr.valueOrAbort.parts
    if argsExpr.forall(isConstantImpl(_).valueOrAbort) then
      val args = argsExpr.map(_.asExprOf[String].valueOrAbort)
      val str = parts.zip(args).foldLeft("")((acc, pair) => acc + pair._1 + pair._2)
      if applyConstraint(Expr(str), constraintExpr).valueOrAbort then
        Expr(str.asInstanceOf[T])
      else
        q.reflect.report.errorAndAbort(s"String $str does not satisfy the constraint $constraintExpr")
    else
      q.reflect.report.errorAndAbort("StringLiteral macro only supports constant expressions")
