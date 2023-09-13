package funsets

/** This class is a test suite for the methods in object FunSets.
  *
  * To run this test suite, start "sbt" then run the "test" command.
  */
class FunSetSuite extends munit.FunSuite:

  import FunSets.*

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /** When writing tests, one would often like to re-use certain values for
    * multiple tests. For instance, we would like to create an Int-set and have
    * multiple test about it.
    *
    * Instead of copy-pasting the code for creating the set into every test, we
    * can store it in the test class using a val:
    *
    * val s1 = singletonSet(1)
    *
    * However, what happens if the method "singletonSet" has a bug and crashes?
    * Then the test methods are not even executed, because creating an instance
    * of the test class fails!
    *
    * Therefore, we put the shared values into a separate trait (traits are like
    * abstract classes), and create an instance inside each test method.
    */

  trait TestSets:
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)

    /** This test is currently disabled (by using .ignore) because the method
      * "singletonSet" is not yet implemented and the test would fail.
      *
      * Once you finish your implementation of "singletonSet", remove the
      * .ignore annotation.
      */

    test("singleton set one contains one") {

      /** We create a new instance of the "TestSets" trait, this gives us access
        * to the values "s1" to "s3".
        */
      new TestSets:
        /** The string argument of "assert" is a message that is printed in case
          * the test fails. This helps identifying which assertion failed.
          */
        assert(contains(s1, 1), "Singleton")
    }

  test("union contains all elements of each set") {
    new TestSets:
      val s = union(s1, s2)
//      val s1 = union(s1, Nil)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
//      assert(contains(s1, 1), "Union 4")
  }

  test("intersection contains all elements of both set") {
    new TestSets:
      val s = union(s1, s2)
      assert(contains(intersect(s1, s1), 1), "Intersection 1")
      assert(contains(intersect(s1, s), 1), "Intersection 2")
      assert(!contains(intersect(s1, s3), 3), "Intersection 3")

  }

  test("differences contains all elements of one set but not another") {
    new TestSets:
      val s = union(s1, s2)
      assert(contains(diff(s1, s2), 1), "Differences 1")
      assert(!contains(diff(s1, s1), 1), "Differences 2")
      assert(contains(diff(s, s3), 2), "Differences 3")
  }

  test("filter only values satisfy a predicate") {
    new TestSets:
      val s = union(union(s1, s2), s3)
      assert(contains(filter(s, _ < 3), 1), "Filter 1")
      assert(!contains(filter(s3, _ < 3), 3), "Filter 2")
  }

  test(
    "forall tests whether a given predicate is true for all elements of the set "
  ) {
    new TestSets:
      val s = union(union(s1, s2), s3)
      assert(forall(s, _ > 0), "Forall 1")
      assert(!forall(s, _ % 2 == 0), "Forall 2")
  }

  test(
    "exists tests whether a given predicate is true for at least one element of the set "
  ) {
    new TestSets:
      val s = union(union(s1, s2), s3)
      assert(exists(s, _ > 0), "Exists 1")
      assert(exists(s, _ % 2 == 0), "Exists 2")
      assert(!exists(s, _ < 0), "Exists 3")
  }

  test ("map transforms all elements in a set") {
    new TestSets:
      val s = union(union(s1, s2), s3)
      assert(contains(map(s, _ + 1), 4), "Map 1")
      assert(contains(map(s, _ - 1), 0), "Map 2")
      assert(!contains(map(s, _ - 1), 100), "Map 3")

  }

  import scala.concurrent.duration.*
  override val munitTimeout = 10.seconds
