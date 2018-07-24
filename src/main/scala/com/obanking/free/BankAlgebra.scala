package com.obanking.free

import cats.free.Free
import cats.{~>, Id, InjectK}
import com.obanking.{Amount, Balance}

import scala.concurrent.Future

sealed trait BankAlgebra[T]
case class Deposit(amount:  Amount) extends BankAlgebra[Balance]
case class Withdraw(amount: Amount) extends BankAlgebra[Balance]
case class CurrentBalance() extends BankAlgebra[Balance]

class BankAlgebraF[F[_]](implicit injectK: InjectK[BankAlgebra, F]) {
  def deposit(amount:  Amount): Free[F, Balance] = Free.inject[BankAlgebra, F](Deposit(amount))
  def withdraw(amount: Amount): Free[F, Balance] = Free.inject[BankAlgebra, F](Withdraw(amount))
  def currentBalance(): Free[F, Balance] = Free.inject[BankAlgebra, F](CurrentBalance())
}

object BankAlgebraF {
  implicit def bankFreeAlgebraF[F[_]](implicit I: InjectK[BankAlgebra, F]): BankAlgebraF[F] = new BankAlgebraF[F]

  val futureInterpreter: (BankAlgebra ~> Future) = new (BankAlgebra ~> Future) {
    private var balance = Balance(Amount(0))

    override def apply[A](fa: BankAlgebra[A]): Future[A] = fa match {
      case Deposit(amount) =>
        Future.successful {
          balance = Balance(Amount(balance.amount.value + amount.value))
          balance
        }
      case Withdraw(amount) =>
        Future.successful {
          if(balance.amount.value - amount.value >= 0) {
            balance = Balance(Amount(balance.amount.value - amount.value))
            balance
          } else {
            balance
          }
        }
      case CurrentBalance() => Future.successful(balance)
    }
  }

  val idInterpreter: (BankAlgebra ~> Id) = new (BankAlgebra ~> Id) {
    private var balance = Balance(Amount(0))

    override def apply[A](fa: BankAlgebra[A]): Id[A] = fa match {
      case Deposit(amount) =>
        balance = Balance(Amount(balance.amount.value + amount.value))
        balance
      case Withdraw(amount) =>
        if(balance.amount.value - amount.value >= 0) {
          balance = Balance(Amount(balance.amount.value - amount.value))
          balance
        } else {
          balance
        }
      case CurrentBalance() => balance
    }
  }
}
