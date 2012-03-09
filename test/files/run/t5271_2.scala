import scala.tools.nsc.reporters._
import scala.tools.nsc.Settings
import reflect.runtime.Mirror.ToolBox

object Test extends App {
  val code = scala.reflect.mirror.reify{
    case class C(foo: Int, bar: Int)
    val c = C(2, 2)
    println(c.foo * c.bar)
  };

  val reporter = new ConsoleReporter(new Settings)
  val toolbox = new ToolBox(reporter)
  println(code.tree)
}
