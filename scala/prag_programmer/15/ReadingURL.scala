import scala.io.Source
import java.net.URL

val source = Source.fromURL(new URL("http://www.cnn.com"))

println(s"What's Source?: ${source}")
println(s"Raw String: ${source.mkString}")

