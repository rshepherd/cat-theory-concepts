// A common example of a functor in Scala is the Option type.
// In Scala, Option is a functor because it provides the map method, which allows you to
// apply a function to the value inside an Option while preserving its structure.
val someNumber: Option[Int] = Some(3)
val incremented: Option[Int] = someNumber.map(_ + 1)
println(incremented) // Value transformed while structure is preserved

// Similarly List is a Functor
val numbers = List(1, 2, 3)
val doubled = numbers.map(_ * 2)
println(doubled) // Outputs: List(2, 4, 6)

// A functor is essentially defined by the presence of a map method.
// However, for a type to truly qualify as a functor, it must also obey two important laws:
//   * Identity Law: Mapping the identity function over a functor should yield the same functor.
//                   In other words, F.map(x => x) should be equivalent to F.
//
//   * Composition Law: Mapping the composition of two functions should be the same as first
//                      mapping one function and then mapping the other.
//                      That is, F.map(f andThen g) should equal F.map(f).map(g).


// Natural transformations and polymorphism
// A 'natural transformation' is like a rule that converts one kind of Functor into another in
// a consistent way for any type inside. Imagine you have two functors, say F and G.
// A natural transformation provides a function that takes any F[A] and turns it into a G[A]
// — for every type A.

// A natural transformation from Option to Either
def optionToEither[A](opt: Option[A]): Either[String, A] = opt match {
  case Some(value) => Right(value)                // Successful computation
  case None        => Left("No value found")      // Failure case
}

// Example usage:
val someOption: Option[Int] = Some(42)
val noneOption: Option[Int] = None

println(optionToEither(someOption))  // Outputs: Right(42)
println(optionToEither(noneOption))  // Outputs: Left("No value found")

// A function with a type like forall A. F[A] => G[A] is a candidate for being a natural transformation,
// because it's parametrically polymorphic and its behavior is uniform for all types A.
// However, to truly be a natural transformation, it must also satisfy the 'naturality condition'
// Simply put, if you apply a function to the contents of a Functor before or after a transformation,
// if must have the same end value. i.e. η(fa.map(f))=η(fa).map(f)

// Our natural transformation from Option to List
def optionToList[A](opt: Option[A]): List[A] = opt.toList

// A simple function to test with
val f: Int => String = _.toString

// Example with Some value
val optSome: Option[Int] = Some(5)

// Applying the mapping then the transformation
val leftSide: List[String] = optionToList(optSome.map(f))

// Applying the transformation then mapping over the List
val rightSide: List[String] = optionToList(optSome).map(f)

println(leftSide == rightSide)
println(leftSide)
println(rightSide)

// Natural transformation "commute" with mapping functions inside the containers.

// Monoids
// In simple terms, a monoid can be thought of as a category that has only one object.
//    * One Object: Imagine a category where there's just one "thing" (object) you care about.
//    * Arrows as Elements: In this category, all the arrows (or morphisms) go from that single object back to itself.
//                          *These arrows represent the elements of the monoid.*
//    * Composition: The way you combine these arrows acts like the monoid’s *associative* binary operation.
//    * Identity: There exists an identity arrow, which acts like the identity element in the monoid.

// A generic Monoid trait
trait Monoid[A] {
  def combine(x: A, y: A): A
  def empty: A
}

// Create an instance for natural numbers (Int) under addition
// (The monoid's object is not "all natural numbers" itself; rather, it's the single
// context in which all the natural number arrows (morphisms) exist and interact via addition.)
val intAdditionMonoid = new Monoid[Int] {
  def combine(x: Int, y: Int): Int = x + y
  def empty: Int = 0
}

// Example usage: summing a list of natural numbers
val naturals = List(1, 2, 3, 4, 5)
val sum = naturals.reduce(intAdditionMonoid.combine)
println(s"The sum is: $sum")

// Here's a simple example that demonstrates a monoid using lists, where the operation is list concatenation:

// Create a monoid instance for lists under concatenation
def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
  def combine(x: List[A], y: List[A]): List[A] = x ++ y
  def empty: List[A] = Nil
}

// Example usage: Concatenating a list of lists of integers
val lists = List(List(1, 2), List(3, 4), List(5))
val result = lists.reduce(listMonoid.combine)
println(result)
