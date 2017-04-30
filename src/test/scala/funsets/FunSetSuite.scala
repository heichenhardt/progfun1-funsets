package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(4)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
      assert(!contains(s1, 2), "Singleton")
    }
  }

  test("union between (1) and (2) contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect between (1), (2) and (3) contains only (2)") {
    new TestSets {
      val u1 = union(s1, s2)
      val u2 = union(s2, s3)

      val s = intersect(u1, u2)
      assert(!contains(s, 1), "Intersect 1")
      assert(contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect 3")
    }
  }

  test("diff between (1), (2) and (3) contains only (1)") {
    new TestSets {
      val u1 = union(s1, s2)
      val u2 = union(s2, s3)

      val s = diff(u1, u2)
      assert(contains(s, 1), "Diff 1")
      assert(!contains(s, 2), "Diff 2")
      assert(!contains(s, 3), "Diff 3")
    }
  }

  test("filter for even numbers") {
    new TestSets {
      val u1 = union(s1, s2)
      val u2 = union(s3, s4)
      val u3 = union(u1, u2)

      val s = filter(u3, x => x % 2 == 0)
      assert(!contains(s, 1), "Filter 1")
      assert(contains(s, 2), "Filter 2")
      assert(!contains(s, 3), "Filter 3")
      assert(contains(s, 4), "Filter 4")
    }
  }

  test("forall for even numbers") {
    new TestSets {
      val u1 = union(s2, s4)
      val u2 = union(s1, s2)
      val u3 = union(s2, s3)
      val u4 = union(s3, s4)

      def even = (x: Int) => x % 2 == 0;

      val s = filter(u3, x => x % 2 == 0)
      assert(forall(u1, even), "forall even")
      assert(!forall(u2, even), "forall uneven")
      assert(!forall(u3, even), "forall uneven")
      assert(!forall(u4, even), "forall uneven")
    }
  }

  test("exists for even numbers") {
    new TestSets {
      val u1 = union(s2, s4)
      val u2 = union(s1, s2)
      val u3 = union(s2, s3)
      val u4 = union(s3, s4)
      val u5 = union(s1, s3)

      def even = (x: Int) => x % 2 == 0;

      val s = filter(u3, x => x % 2 == 0)
      assert(exists(u1, even), "exists even")
      assert(exists(u2, even), "exists even")
      assert(exists(u3, even), "exists even")
      assert(exists(u4, even), "exists even")
      assert(!exists(u5, even), "!exists even")
      assert(!exists(s1, even), "!exists even")
      assert(!exists(s3, even), "!exists even")
    }
  }

  test("map for double numbers") {
    new TestSets {
      val u1 = union(s1, s3)
      val s = map(u1, x => 2 * x)

      def f(x: Int) = 2 * x;

      printSet(u1)
      printSet(s)

      assert(!contains(s, 1), "map double")
      assert(contains(s, 2), "map double")
      assert(!contains(s, 3), "map double")
      assert(!contains(s, 4), "map double")
      assert(!contains(s, 5), "map double")
      assert(contains(s, 6), "map double")
    }
  }
}
