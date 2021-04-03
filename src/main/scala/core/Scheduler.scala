package scheduleforme

import java.time.LocalTime

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

sealed trait SchedulerMode 
// A fair scheduler interleaves the tasks proportionally
final case object Fair extends SchedulerMode 
// A sequential scheduler schedules the tasks in order
final case object Sequential extends SchedulerMode

// final case object Priority extends SchedulerMode

object Scheduler {
    val now: LocalTime = LocalTime.now()
    val dtf = DateTimeFormatter.ofPattern("HH:mm")

    def apply(tasks: List[(String, Double)])(mode: SchedulerMode): List[(String, LocalTime)] = {
        val schedule = {
            mode match {
                case Fair => fair(tasks)
                case Sequential => sequential(tasks)
            }
        }
        displaySchedule(schedule)
        schedule
    }

    def displaySchedule(schedule: List[(String, LocalTime)]): Unit = {
        println(s"${Console.YELLOW}时间表: ${Console.RESET}")
        println(s"${dtf.format(now)} 开始${schedule.head._1}")
        schedule.foreach(x => println(s"${dtf.format(x._2)} ${x._1}结束"))
    }

    // each task gets an equal share per hour 
    def sequential(tasks: List[(String, Double)]): List[(String, LocalTime)] = {
        var tmp: LocalTime = now
        tasks.map(x => {
            tmp = tmp.plus((x._2 * 60).toLong, ChronoUnit.MINUTES)
            (x._1, tmp)
        })
    }

    def fair(tasks: List[(String, Double)]): List[(String, LocalTime)] = {
        val totalTasks = tasks.length
        val timePerSlice = tasks.map(x => {
        (x._1, (x._2 / totalTasks * 60).toLong)
        })

        var tmp: LocalTime = now
        
        (1 to totalTasks).flatMap(_ => {
            timePerSlice.map(x => {
                tmp = tmp.plus(x._2, ChronoUnit.MINUTES)
                (x._1, tmp)
            })
        }).toList
    }
}