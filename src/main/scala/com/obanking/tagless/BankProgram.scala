package com.obanking.tagless

import cats.Monad
import cats.implicits._
import com.obanking.{Amount, Balance}

class BankProgram[F[_]](
    bankAlgebra:    BankAlgebra[F],
    consoleAlgebra: ConsoleAlgebra[F]
)(implicit monad:   Monad[F]) {

  def program(): F[Balance] =
    for {
      firstBalance <- bankAlgebra.currentBalance()
      deposited    <- bankAlgebra.deposit(Amount(100))
      balance      <- bankAlgebra.withdraw(Amount(10))
      _            <- consoleAlgebra.print(balance.toString)
    } yield balance
}
