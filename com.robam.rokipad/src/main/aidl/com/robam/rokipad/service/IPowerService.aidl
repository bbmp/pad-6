package com.robam.rokipad.service;

interface IPowerService
{ 
	String getName();
	int onShortPressToOn();
	int onShortPressToOff();
	int onLongPress();
	int isRunning();
}