package com.johnfreed.wiiupccontroller;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class PCController {
	private Robot robot;
	private int autoDelay = 40;
	private int sensitivity = 20;
	private boolean waitForIdle = true;
	
	public ButtonState LeftClickState = ButtonState.Up;
	public ButtonState RightClickState = ButtonState.Up;
	
	public enum ButtonState {
		Up,
		Down
	}
	
	public PCController() throws AWTException {
		robot = new Robot();
		robot.setAutoDelay(autoDelay);
		robot.setAutoWaitForIdle(waitForIdle);
	}
	
	public Point GetMousePosition() {
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	public void MoveMouseToPosition(int x, int y) {
		robot.mouseMove(x, y);
	}
	
	public void MoveMouse(float xDeflection, float yDeflection) {
		// The deflection comes in as a value ranging from -1.0 to 1.0
		Point currentPosition = this.GetMousePosition();
		int newX = (int) (currentPosition.x + (sensitivity * xDeflection));
		int newY = (int) (currentPosition.y - (sensitivity * yDeflection));
		this.MoveMouseToPosition(newX, newY);
	}
	
	public void LeftClickDown() {
		if (LeftClickState == ButtonState.Up) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			this.LeftClickState = ButtonState.Down;
		}
	}
	
	public void LeftClickUp() {
		if (LeftClickState == ButtonState.Down) {
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			this.LeftClickState = ButtonState.Up;
		}
	}
	
	public void RightClickDown() {
		if (RightClickState == ButtonState.Up) {
			robot.mousePress(InputEvent.BUTTON3_MASK);
			this.RightClickState = ButtonState.Down;
		}
	}
	
	public void RightClickUp() {
		if (RightClickState == ButtonState.Down) {
			robot.mouseRelease(InputEvent.BUTTON3_MASK);
			this.RightClickState = ButtonState.Up;
		}
	}
	
	public void TypeKey(int i) {
		robot.delay(50);
		robot.keyPress(i);
		robot.keyRelease(i);
	}
	
	public void TypeString(String text) {
		byte[] bytes = text.getBytes();
		
		robot.delay(500);
		for (byte b : bytes) {
			int code = b;
			
			if (code > 96 && code < 123) code = code - 32;
			robot.delay(50);
			robot.keyPress(code);
			robot.keyRelease(code);
		}
	}
	
	public void SetAutoDelay(int value) {
		this.autoDelay = value;
		robot.setAutoDelay(this.autoDelay);
	}
	
	public int GetAutoDelay() {
		return this.autoDelay;
	}
	
	public void SetAutoWaitForIdle(boolean value) {
		this.waitForIdle = value;
		robot.setAutoWaitForIdle(this.waitForIdle);
	}
	
	public boolean GetAutoWaitForIdle() {
		return this.waitForIdle;
	}
}
