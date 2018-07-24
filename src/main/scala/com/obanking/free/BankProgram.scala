package com.obanking.free

import cats.free.Free
import com.obanking.{Amount, Balance}

class BankProgram(
    implicit bankAlgebra: BankAlgebraF[Combined.BankAndConsoleAlgebra],
    consoleAlgebra:       ConsoleAlgebraF[Combined.BankAndConsoleAlgebra]
) {
  def program(): Free[Combined.BankAndConsoleAlgebra, Balance] =
    for {
      firstBalance <- bankAlgebra.currentBalance()
      deposited    <- bankAlgebra.deposit(Amount(100))
      balance      <- bankAlgebra.withdraw(Amount(10))
      _            <- consoleAlgebra.print(balance.toString)
    } yield balance
}
