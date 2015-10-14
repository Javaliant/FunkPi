""" Author: Luigi Vincent
*   Raspberry Pi Project
*   Push a button and an LED lights up, and sends a message to another computer
*   The target computer plays a song as a result.
*
*   Created to practice using RasPi, GPIO, and Socket connections.
"""

import RPi.GPIO as GPIO
import socket

funk_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
button = 17 # pin used for button
led = 22 # pin used for led
sent = False # flag to send message only once

def gpio_setup():
	GPIO.setwarnings(False)
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(led, GPIO.OUT)
	GPIO.setup(button, GPIO.IN)

def button_loop():
	global sent
	while True:
		if GPIO.input(button):
			GPIO.output(led, GPIO.LOW)
		else:
			GPIO.output(led, GPIO.HIGH)
			if not sent:
				funk_socket.send(bytearray("play\r\n", "utf-8"))
				sent = True

def main():
	funk_socket.connect(("10.0.0.18", 5290))
	gpio_setup()
	button_loop()

if __name__ == "__main__":
	main()