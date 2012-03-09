package scala.reflect
package api

abstract class Universe extends Symbols
                           with Types
                           with Constants
                           with Scopes
                           with Names
                           with Trees
                           with AnnotationInfos
                           with Positions
                           with Exprs
                           with TypeTags
                           with TreePrinters
                           with StandardDefinitions
                           with StandardNames
                           with TreeBuildUtil {

  /** Given an expression, generate a tree that when compiled and executed produces the original tree.
   *  The produced tree will be bound to the Universe the originating reify was called from.
   *
   *  For instance, given the abstract syntax tree representation of the <[ x + 1 ]> expression:
   *
   *    Apply(Select(Ident("x"), "+"), List(Literal(Constant(1))))
   *
   *  The reifier transforms it to the following expression:
   *
   *    <[
   *      val $mr: scala.reflect.api.Universe = <reference to the Universe that calls the reify>
   *      $mr.Expr[Int]($mr.Apply($mr.Select($mr.Ident($mr.newFreeVar("x", <Int>, x), "+"), List($mr.Literal($mr.Constant(1))))))
   *    ]>
   *
   *  The transformation looks mostly straightforward, but it has its tricky parts:
   *    * Reifier retains symbols and types defined outside the reified tree, however
   *      locally defined entities get erased and replaced with their original trees
   *    * Free variables are detected and wrapped in symbols of the type FreeVar
   *    * Mutable variables that are accessed from a local function are wrapped in refs
   *    * Since reified trees can be compiled outside of the scope they've been created in,
   *      special measures are taken to ensure that all members accessed in the reifee remain visible
   */
  def reify[T](expr: T): Expr[T] = macro Universe.reify[T]
}

object Universe {
  def reify[T](cc: scala.reflect.makro.Context{ type PrefixType = Universe })(expr: cc.Expr[T]): cc.Expr[cc.prefix.value.Expr[T]] = {
    import cc.mirror._
    val prefix = cc.prefix
    scala.reflect.api.Reifiers.reify[T](cc)(prefix)(expr)
  }
}
