package scheduleforme

import javax.sound.sampled._
import java.io.File
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object Alarm {
  import Scheduler.dtf

  def setAlarm(referenceTime: LocalTime): Unit = {
    val now: LocalTime = LocalTime.now()
    if (referenceTime.isBefore(now)){
      println("Can't schedule an alarm for the past event!")
    } else {
      val timerMillis = now.until(referenceTime, ChronoUnit.MILLIS)
      Thread.sleep(timerMillis)
      new Sound("Ding").timeUpSound(3)
    }
  }
}

class Sound(name: String) {
    import Controller.currentPath

    val soundFile: File = new File(s"${currentPath}/src/main/scala/audio/${name}.wav")

    def checkSoundFile(): Unit = {
        if (!soundFile.exists()) {
            throw new Exception("No audio file found!")
        }
    }

    private def playSoundFile() {
        val audioInputStream = AudioSystem.getAudioInputStream(soundFile)
        val clip = AudioSystem.getClip()
        clip.open(audioInputStream)
        clip.start
    }

    def timeUpSound(repeat: Int) {
        (1 to repeat).foreach(_ => {
            new Sound("Ding").playSoundFile()
            Thread.sleep(1000)
        })
    }
}