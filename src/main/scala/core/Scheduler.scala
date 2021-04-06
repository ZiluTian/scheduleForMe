package scheduleforme

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

sealed trait ScheduleMode
// A fair scheduler interleaves the tasks proportionally
final case object Fair extends ScheduleMode 
// A sequential scheduler schedules the tasks in order
final case object Sequential extends ScheduleMode

// final case object Priority extends ScheduleMode

// Use type class to generate a schedule 
trait SchedulerWriter[T] {
    def schedule(tasks: TaskList)(startTime: LocalTime): TimeTable
}

object SchedulerWriter {
    implicit val fairScheduler = {
        new SchedulerWriter[Fair.type] {
            def schedule(tasks: TaskList)(start: LocalTime): TimeTable = {
                val totalTasks = tasks.length
                val timePerSlice = tasks.map(x => {
                (x.name, (x.duration / totalTasks * 60).toLong)
                })

                var tmp: LocalTime = start
                
                (1 to totalTasks).flatMap(_ => {
                    timePerSlice.map(x => {
                        tmp = tmp.plus(x._2, ChronoUnit.MINUTES)
                        TimeTableEntry(x._1, tmp)
                    })
                }).toList
            }
        }

    }

    implicit val sequentialScheduler = {
        new SchedulerWriter[Sequential.type] {
            def schedule(tasks: TaskList)(start: LocalTime): TimeTable = {
                var tmp: LocalTime = start
                tasks.map(x => {
                    tmp = tmp.plus((x.duration * 60).toLong, ChronoUnit.MINUTES)
                    TimeTableEntry(x.name, tmp)
                })
            }
        }
    }
}

object Scheduler {
    val now: LocalTime = LocalTime.now()
    val dtf = DateTimeFormatter.ofPattern("HH:mm")

    def apply[T](tasks: TaskList)(implicit writer: SchedulerWriter[T]): TimeTable = {
        val schedule = writer.schedule(tasks)(now)
        displaySchedule(schedule)
        schedule
    }

    def displaySchedule(schedule: TimeTable): Unit = {
        println(s"${Console.YELLOW}时间表: ${Console.RESET}")
        println(s"${dtf.format(now)} 开始${schedule.head.name}")
        schedule.foreach(x => println(s"${dtf.format(x.time)} ${x.name}结束"))
    }
}