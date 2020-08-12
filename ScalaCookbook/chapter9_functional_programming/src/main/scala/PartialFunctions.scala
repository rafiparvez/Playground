/*
This module presents different ways partial functions could be used
A PartialFunction[A, B] is a unary function where the domain does not necessarily include all values of type A.
 */

object PartialFunctions {
  def main(args: Array[String]): Unit = {
    println("Understanding partial functions")
    val divide = (x: Int) => 42 / x
    try {
      println(divide(7))
      // 1. The below should throw an exception
      println(divide(0))
    } catch {
      case ex: Exception => println(s"Exception: $ex")
    }
    // 2. Scala allows creating partial functions and explicitly stating the domain using isDefinedAt
    val dividePartialFunc = new PartialFunction[Int, Int] {
      println("\nUsing dividePartialFunc....")
      def apply(x: Int): Int = 42 / x
      override def isDefinedAt(x: Int): Boolean = x != 0
    }
    // Using this approach you can first test the domain before divide
    println(if (dividePartialFunc.isDefinedAt(6)) dividePartialFunc(6))
    println(if (dividePartialFunc.isDefinedAt(0)) dividePartialFunc(0))

    // 3. Another approach using case statements
    val dividePartialFunc2: PartialFunction[Int, Int] = {
      case x: Int if x != 0 => 42 / x
    }

    println("\nUsing dividePartialFunc2....")
    println(if (dividePartialFunc2.isDefinedAt(6)) dividePartialFunc2(7))
    println(dividePartialFunc2.isDefinedAt(0))

    // 4. Partial functions act as transformers (A => B)
    println("\nNum to words example....")
    val numsToWords: PartialFunction[Int, String] =
      new PartialFunction[Int, String] {
        val nums: Array[String] = Array("one", "two", "three", "four", "five")
        def apply(x: Int): String = nums(x - 1)
        override def isDefinedAt(x: Int): Boolean = x > 0 && x < 6
      }
    println(if (numsToWords.isDefinedAt(1)) numsToWords(1))
    println(numsToWords.isDefinedAt(0))

    // 5. Chaining Partial Functions
    println("\nChaining Partial Functions....")
    val numsToWords1to5: PartialFunction[Int, String] =
      new PartialFunction[Int, String] {
        val nums: Array[String] = Array("one", "two", "three", "four", "five")
        def apply(x: Int): String = nums(x - 1)
        override def isDefinedAt(x: Int): Boolean = x >= 1 && x <= 5
      }

    val numsToWords6to10: PartialFunction[Int, String] =
      new PartialFunction[Int, String] {
        val nums: Array[String] = Array("six", "seven", "eight", "nine", "ten")
        def apply(x: Int): String = nums(x - 1 - 5)
        override def isDefinedAt(x: Int): Boolean = x >= 6 && x <= 10
      }

    val numsToWords1to10 = numsToWords1to5
      .orElse(numsToWords6to10)
      .andThen(str => s"${str} is the answer")
    println(numsToWords1to10(2))
    println(numsToWords1to10(9))

    // 6. Collect method takes partial function and applies it to every element in the collection.
    // Map method explodes for undefined values, but collect also tests isDefinedAt in addition
    val nums = List(1, 3, 4, 7, 0)
    val res = nums.collect(numsToWords1to10)
    println(res)
    try {
      println(nums.map {
        numsToWords1to10
      })
    } catch {
      case ex: Exception =>
        println(s"Map func exploded with exception: ${ex}  \n")
    }

    val isEvenPF: PartialFunction[Int, String] = {
      case x if x % 2 == 0 => x + " is even"
    }

    val isOddPF: PartialFunction[Int, String] = {
      case x if x % 2 != 0 => x + " is odd"
    }
    val numList = (1 to 10).toList

    val checkEvenOdd = isEvenPF.orElse(isOddPF)
    val output = numList.collect { checkEvenOdd }

    println(output)

    try {
      println(numList.map(isOddPF))
    } catch {
      case ex: Exception =>
        println(s"Map func exploded with exception: ${ex}  \n")
    }

    println(numList.map(checkEvenOdd))

  }
}
