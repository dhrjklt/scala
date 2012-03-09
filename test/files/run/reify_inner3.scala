import scala.tools.nsc.reporters._
import scala.tools.nsc.Settings
import reflect.runtime.Mirror.ToolBox

object Test extends App {
  val code = scala.reflect.mirror.reify{
    object C {
      class D {
        val x = 2
      }
    }

    val outer = C
    val inner = new outer.D
    println(inner.x)
  };

  val reporter = new ConsoleReporter(new Settings)
  val toolbox = new ToolBox(reporter)
  toolbox.runExpr(code.tree)
}
