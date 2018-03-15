package presentation.swing

import java.awt.Font

import scala.swing.Button

object FrameKitFactory {
  def apply(fontSpecs: Font, kitName: KitNames): FrameKit = kitName match {
    case ManualEntry => new FrameKit(
      new ManualEntry(fontSpecs),
      new Button {
        text = "Manual Entry"
        font = fontSpecs
      })
  }
}

sealed trait KitNames
case object ManualEntry extends KitNames
