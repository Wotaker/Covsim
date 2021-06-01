import java.io.FileWriter
import java.io.IOException
import com.opencsv.CSVWriter

class Data {
  var outputFile: Option[FileWriter] = None
  var writer: Option[CSVWriter] = None
  val header: Array[String] = Array(
      "Day", "Suspectible", "Exposed", "Infectious", "Recovered", "Dead"
    )
  var keepWriting = true
  
  def initialize(): Unit = {
    outputFile = Some(new FileWriter(Params.FILE_DATA))
    writer = Some(new CSVWriter(outputFile.get))
    writer.get.writeNext(header)
  }
  

  def writeData(data: Array[String]) =
    writer.get.writeNext(data)
  
  def closeFile() = writer.get.close()
}
