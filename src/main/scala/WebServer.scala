import java.net._
import java.io._
import scala.io._

object WebServer {
  def read_and_write(in: BufferedReader, out: BufferedWriter): Unit = {
    val msg = in.readLine()
    System.out.println("Received: " + msg)
    val fileName = get_fileName(get_path(msg))
//    println("filename " + fileName)
    val validHTML = html_files(this_dir)
    println("HTML files here: " + validHTML)
    System.out.println("Returning: " + fileName)
    if(fileName.endsWith("./")) {
      get_index(in, out, validHTML)
    }
    else if (fileName.endsWith(".html")){
      get_request(in,out,validHTML, fileName)
    }
    out.flush()
    in.close()
    out.close()
  }

  def get_path (msg: String): String ={
    val path = msg.slice(4, msg.length() - 8)
    return path
  }

  def get_fileName (path: String): String ={
    val fileName = "./" + path.substring(1, path.length() - 1)
    return fileName
  }

  def this_dir : File = {
    val dir = new File(".") //sets dir to be the current directory
    return dir
  }

  def html_files (dir: File) : List[File] = {
    try {
      val htmlFiles = dir.listFiles.toList.filter { file => file.getName.endsWith(".html") }
      return htmlFiles
    }
  }

  def get_not_found (in: BufferedReader, out: BufferedWriter): Unit = {
    out.write("HTTP/1.1 404 not found\r\n")
    out.write("Content-Type: text/html; charset=UTF-8\r\n")
    out.write("\r\n")
    out.write("404 not found")
    System.out.println("404 not found")
    System.out.println("")
  }

  def get_index (in: BufferedReader, out: BufferedWriter, dir: List[File]): Unit ={
    if (!dir.isEmpty) {
      out.write("HTTP/1.1 200 ok\r\n")
      out.write("Content-Type: text/html; charset=UTF-8\r\n")
      out.write("\r\n")
      System.out.println()
      println(dir.head)
      for (line <- Source.fromFile(dir.head).getLines) {
        println(line)
        out.write(line + "\r\n")
      }
      out.flush()
    }
    else {
      get_not_found(in,out)
    }
  }

  def get_request (in: BufferedReader, out: BufferedWriter, dir: List[File], fileName: String): Unit ={
    if (!dir.isEmpty) {
      out.write("HTTP/1.1 200 ok\r\n")
      out.write("Content-Type: text/html; charset=UTF-8\r\n")
      out.write("\r\n")
      System.out.println()
      println(dir.head)
      for (line <- Source.fromFile(fileName).getLines) {
        println(line)
        out.write(line + "\r\n")
      }
      out.flush()
    }
    else {
      get_not_found(in,out)
    }
  }

  def serve(server: ServerSocket): Unit ={
    val s = server.accept()
    val in = new BufferedReader(new InputStreamReader(s.getInputStream))
    val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))

    read_and_write(in,out)

    s.close()
  }

  def main(args: Array[String]): Unit = {
    val server = new ServerSocket(8080)
    while(true) {
      serve (server)
    }
  }

}