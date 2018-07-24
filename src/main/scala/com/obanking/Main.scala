package com.obanking

import cats.Id
import cats.instances.future._
import com.obanking.free.{Combined, BankProgram => FreeBankProgram}
import com.obanking.tagless.{
  IdBankAlgebra,
  IdConsoleAlgebra,
  BankProgram => TaglessBankProgram,
  FutureBankAlgebra => TaglessFutureBankAlgebra,
  FutureConsoleAlgebra => TaglessFutureConsoleAlgebra
}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Main {

  def main(args: Array[String]): Unit = {
    taglessFuture()
    taglessId()

    freeFuture()
    freeId()
  }

  private def taglessFuture(): Balance = {
    val futureTaglessBankProgram = new TaglessBankProgram[Future](TaglessFutureBankAlgebra.futureBankAlgebra, TaglessFutureConsoleAlgebra.futureConsoleAlgebra)
    Await.result(futureTaglessBankProgram.program(), 10 seconds)
  }

  private def taglessId(): Balance = {
    val idTaglessBankProgram = new TaglessBankProgram[Id](IdBankAlgebra.idBankAlgebra, IdConsoleAlgebra.idConsoleAlgebra)
    idTaglessBankProgram.program()
  }

  private def freeFuture(): Balance = {
    val freeBankProgram = new FreeBankProgram().program()
    Await.result(freeBankProgram.foldMap(Combined.futureInterpreter), 10 seconds)
  }

  private def freeId(): Balance = {
    val idBankProgram = new FreeBankProgram().program()
    idBankProgram.foldMap(Combined.idInterpreter)
  }
}
