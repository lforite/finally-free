package com.obanking.tagless

import cats.Id
import com.obanking.{Amount, Balance}

import scala.concurrent.Future

trait BankAlgebra[F[_]] {
  def deposit(amount:  Amount): F[Balance]
  def withdraw(amount: Amount): F[Balance]
  def currentBalance(): F[Balance]
}

object FutureBankAlgebra {
  val futureBankAlgebra = new BankAlgebra[Future] {
    private var balance = Balance(Amount(0))

    override def deposit(amount: Amount): Future[Balance] = Future.successful {
      balance = Balance(Amount(balance.amount.value + amount.value))
      balance
    }

    override def withdraw(amount: Amount): Future[Balance] = Future.successful {
      if(balance.amount.value - amount.value >= 0) {
        balance = Balance(Amount(balance.amount.value - amount.value))
        balance
      } else {
        balance
      }
    }

    override def currentBalance(): Future[Balance] = Future.successful(balance)
  }
}

object IdBankAlgebra {
  val idBankAlgebra = new BankAlgebra[Id] {
    private var balance = Balance(Amount(0))

    override def deposit(amount: Amount): Id[Balance] = {
      balance = Balance(Amount(balance.amount.value + amount.value))
      balance
    }

    override def withdraw(amount: Amount): Id[Balance] =
      if(balance.amount.value - amount.value >= 0) {
        balance = Balance(Amount(balance.amount.value - amount.value))
        balance
      } else {
        balance
      }

    override def currentBalance(): Id[Balance] = balance
  }
}
