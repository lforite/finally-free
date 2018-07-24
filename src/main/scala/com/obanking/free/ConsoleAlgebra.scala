package com.obanking.free

import cats.free.Free
import cats.{~>, Id, InjectK}

import scala.concurrent.Future

sealed trait ConsoleAlgebra[T]
case class Print(string: String) extends ConsoleAlgebra[Unit]

class ConsoleAlgebraF[F[_]](implicit injectK: InjectK[ConsoleAlgebra, F]) {
  def print(string: String): Free[F, Unit] = Free.inject[ConsoleAlgebra, F](Print(string))
}

object ConsoleAlgebraF {
  implicit def consoleFreeAlgebraTI[F[_]](implicit I: InjectK[ConsoleAlgebra, F]): ConsoleAlgebraF[F] = new ConsoleAlgebraF[F]

  val futureInterpreter: (ConsoleAlgebra ~> Future) = new (ConsoleAlgebra ~> Future) {
    override def apply[A](fa: ConsoleAlgebra[A]): Future[A] = fa match {
      case Print(string) => Future.successful(println(string))
    }
  }

  val idInterpreter: (ConsoleAlgebra ~> Id) = new (ConsoleAlgebra ~> Id) {
    override def apply[A](fa: ConsoleAlgebra[A]): Id[A] = fa match {
      case Print(string) => println(string)
    }
  }
}
