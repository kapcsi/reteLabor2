package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import java.util.Scanner; 


public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		Scanner inputScanner = new Scanner(System.in);
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
	
		while(true) {
			String cmd = inputScanner.nextLine();
			switch(cmd) {
				case "start":
					s.raiseStart();
					s.runCycle();
					break;
				case "black":
					s.raiseBlack();
					s.runCycle();
					break;
				case "white":
					s.raiseWhite();
					s.runCycle();
					break;
				case "exit":
					System.out.println("Exiting...");
					System.exit(0);
				default:
					System.out.println("Pleas enter a legit command...");
					break;
			}
			// mindig kiiratom az időt...
			print(s);
			
		}
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
