package scheduleforme

import scala.io.StdIn.readLine
import scala.io.Source.fromFile

import SchedulerWriter._ 

object entrypoint extends App {
  
  // select either input or default 
  // val tasks = Controller.getTasks()
  val tasks = Controller.getDefaultInput()

  // A fair schedule interleaves the tasks
  val schedule = Scheduler[Fair.type](tasks)

  // Schedule the alarms that ring at the scheduled time
  schedule.foreach(x => {
    Alarm.setAlarm(x.time)
  })
}

object Controller {

  val currentPath = new java.io.File(".").getCanonicalPath
  val defaultFile = new java.io.File(s"${currentPath}/src/main/scala/default/routine")

  def getTasks(): TaskList = {
    println("What would you like to do? Please enter the title and time in hours.\nOtherwise, schedule the routine in default")
    var tasks = inputToSchedule(Iterator.continually(readLine))
    if (tasks.isEmpty) {
      tasks = getDefaultInput()
    }
    tasks
  }

  def inputToSchedule(bufferedLines: Iterator[String]): TaskList = {
    bufferedLines.takeWhile(_.nonEmpty).map(line => {
        val pieces = line.split("\\s+").toList.filter(_.nonEmpty)
        Task(pieces.dropRight(1).mkString(" "), pieces.last.toDouble)
      }).toList
  }

  def getDefaultInput(): TaskList = {
    if (!defaultFile.exists) {
      throw new Exception("Default activities undefined!")
    } else {
      val cleanedData = fromFile(defaultFile).getLines().filterNot(x => x.startsWith("#"))

      inputToSchedule(cleanedData)
    }
  }
}




