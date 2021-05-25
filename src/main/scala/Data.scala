import java.io.FileWriter
import java.io.IOException
import com.opencsv.CSVWriter

object Data {
  val outputFile: FileWriter = new FileWriter(Params.FILE_DATA)
  val writer: CSVWriter = new CSVWriter(outputFile)
  val header: Array[String] = Array(
    "Day", "Suspectible", "Exposed", "Infectious", "Recovered", "Dead"
  )
  writer.writeNext(header)

  def writeData(data: Array[String]) =
    writer.writeNext(data)
  
  def closeFile() = writer.close()
}
