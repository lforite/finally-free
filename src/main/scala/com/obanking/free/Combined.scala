package com.obanking.free

import cats.data.EitherK
import cats.{~>, Id}

import scala.concurrent.Future

object Combined {
  type BankAndConsoleAlgebra[T] = EitherK[BankAlgebra, ConsoleAlgebra, T]

  val futureInterpreter: BankAndConsoleAlgebra ~> Future =
    BankAlgebraF.futureInterpreter or ConsoleAlgebraF.futureInterpreter

  val idInterpreter: BankAndConsoleAlgebra ~> Id =
    BankAlgebraF.idInterpreter or ConsoleAlgebraF.idInterpreter
}
