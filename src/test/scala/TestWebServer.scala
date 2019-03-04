import java.io._
import java.net._

import org.scalatest.{Matchers, _}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class TestWebServer extends FlatSpec with Matchers with MockitoSugar {
  "Bytes in" should "be bytes out" in {
    val serversocket = mock[ServerSocket]
    val socket = mock[Socket]
    val bytearrayinputstream = new ByteArrayInputStream("This is a test".getBytes())
    val bytearrayoutputstream = new ByteArrayOutputStream()

    when(serversocket.accept()).thenReturn(socket)
    when(socket.getInputStream).thenReturn(bytearrayinputstream)
    when(socket.getOutputStream).thenReturn(bytearrayoutputstream)

    WebServer.serve(serversocket)

    bytearrayoutputstream.toString() should be("This is a test")
    verify(socket).close()

  }

  "Read and write" should "echo" in {
    val in = mock[BufferedReader]
    val out = mock[BufferedWriter]

    when(in.readLine()).thenReturn("This is a test")

    verify(out).write("This is a test")
    verify(out).flush()
    verify(out).close()
    verify(in).close()
  }

  "Get path" should "echo" in {
    val msg1 = "GET /mockfile HTTP/1.1"
    val path1 = msg1.slice(4, msg1.length() - 8)

    verify(path1).equals("/mockfile")

    val msg2 = "GET / HTTP/1.1"
    val path2 = msg1.slice(4, msg1.length() - 8)

    verify(path2).equals("./")
  }

  "Get filename" should "echo" in {
    val path1 = "mockfile"
    val fileName1 = "./" + path1

    verify(fileName1).equals("./mockfile")

    val path2 = "./"
    verify(path2).equals("./")
  }

  "This dir" should "exist as directory" in{
    val dir = new File(".")

    verify(dir.isDirectory)
  }

  "Html files" should "return filtered list" in {
    val dir = mock[List[File]]

    verify(dir).forall(File => File.getAbsolutePath.endsWith(".html"))
  }
}