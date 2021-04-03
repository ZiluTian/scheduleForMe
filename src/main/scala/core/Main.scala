package scheduleforme

import scala.io.StdIn.readLine
import scala.io.Source.fromFile


object entrypoint extends App {

  // select either input or default 
  // val tasks = Controller.getTasks()
  val tasks = Controller.getDefaultInput()

  // A fair schedule interleaves the tasks
  val schedule = Scheduler(tasks)(Fair)

  // Schedule the alarms that ring at the scheduled time
  schedule.foreach(x => {
    Alarm.setAlarm(x._2)
  })
}

object Controller {

  val currentPath = new java.io.File(".").getCanonicalPath
  val defaultFile = new java.io.File(s"${currentPath}/src/main/scala/default/routine")

  def getTasks(): List[(String, Double)] = {
    println("What would you like to do? Please enter the title and time in hours.\nOtherwise, schedule the routine in default")
    var tasks = inputToSchedule(Iterator.continually(readLine))
    if (tasks.isEmpty) {
      tasks = getDefaultInput()
    }
    tasks
  }

  def inputToSchedule(bufferedLines: Iterator[String]): List[(String, Double)] = {
    bufferedLines.takeWhile(_.nonEmpty).map(line => {
        val pieces = line.split("\\s+").toList.filter(_.nonEmpty)
        (pieces.dropRight(1).mkString(" "), pieces.last.toDouble)
      }).toList
  }

  def getDefaultInput(): List[(String, Double)] = {
    if (!defaultFile.exists) {
      throw new Exception("Default activities undefined!")
    } else {
      val cleanedData = fromFile(defaultFile).getLines().filterNot(x => x.startsWith("#"))

      inputToSchedule(cleanedData)
    }
  }
}




