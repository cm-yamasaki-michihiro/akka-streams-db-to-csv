import java.nio.charset.{ Charset, StandardCharsets }
import java.nio.file.Files

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.alpakka.csv.scaladsl.CsvFormatting
import akka.stream.scaladsl.{ Flow, _ }
import akka.stream.{ ActorMaterializer, _ }
import org.reactivestreams.Publisher
import scalikejdbc._
import scalikejdbc.streams._

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

object Main extends App {

  implicit val system = ActorSystem("streams")
  implicit val materializer = ActorMaterializer()

  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton("jdbc:mysql://localhost/test?characterEncoding=UTF-8&useSSL=false", "root", "password")

  implicit val session = AutoSession

  import java.nio.file.Paths

  val csvOutputPath = Paths.get("./sample.csv")

  Files.deleteIfExists(csvOutputPath)

  val source: Source[Goods, NotUsed] = Source.fromPublisher(Goods.streamAll())

  //それぞれの商品をCSVの要素を要素に持つIterableに変換
  def fractionize(g: Goods): immutable.Iterable[String] = immutable.Seq(g.id.toString, g.name, g.price.toString)

  val flow = Flow[Goods].map(fractionize).via(CsvFormatting.format(charset = Charset.forName("Shift-JIS")))

  val sink =  FileIO.toPath(csvOutputPath)

  def csvOutPut() = for {
    ioResult <- source.via(flow).runWith(sink)
    _ <- Future.fromTry(ioResult.status)
  } yield ()

  csvOutPut().onComplete{ _ =>
    system.terminate()
  }

}

case class Goods(id: Long, name: String, price: Int)

object Goods extends SQLSyntaxSupport[Goods] {


  override def columns: Seq[String] = Seq("id", "name", "price")

  override val tableName = "goods"

  def apply(c: SyntaxProvider[Goods])(rs: WrappedResultSet): Goods = apply(c.resultName)(rs)

  def apply(c: ResultName[Goods])(rs: WrappedResultSet): Goods = new Goods(
    rs.long(c.id), rs.string(c.name), rs.int(c.price))

  val g = Goods.syntax("c")

  def streamAll(): Publisher[Goods] = DB readOnlyStream {
    withSQL{ select.from(Goods as g) }
      .map(rs => Goods(g)(rs)).iterator
  }
}