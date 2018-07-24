package com.obanking.tagless

import cats.Id

import scala.concurrent.Future

trait ConsoleAlgebra[F[_]] {
  def print(string: String): F[Unit]
}

object FutureConsoleAlgebra {
  val futureConsoleAlgebra = new ConsoleAlgebra[Future] {
    override def print(string: String): Future[Unit] = Future.successful {
      println(string)
    }
  }
}

object IdConsoleAlgebra {
  val idConsoleAlgebra = new ConsoleAlgebra[Id] {
    override def print(string: String): Id[Unit] = println(string)
  }
}
