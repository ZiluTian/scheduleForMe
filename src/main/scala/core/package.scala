  package object scheduleforme {

    final case class Task(name: String, duration: Double)
    final case class TimeTableEntry(name: String, time: java.time.LocalTime)

    type TaskList = List[Task]
    type TimeTable = List[TimeTableEntry]
}