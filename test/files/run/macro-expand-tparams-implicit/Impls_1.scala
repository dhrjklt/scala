import scala.reflect.makro.{Context => Ctx}

object Impls {
  def foo[U: c.TypeTag](c: Ctx)(x: c.Expr[U]) = {
    import c.mirror._
    val U = typeTag[U]
    val body = Apply(Select(Ident(definitions.PredefModule), newTermName("println")), List(Literal(Constant(U.toString))))
    Expr[Unit](body)
  }
}
