package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;


import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		EObject root = manager.loadModel("model_input/example.sct");
		
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		ArrayList<String> events  = new ArrayList<String>();
		ArrayList<String> variables = new ArrayList<String>();
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			
			if(content instanceof VariableDefinition) {
				VariableDefinition vd = (VariableDefinition) content;
				variables.add(vd.getName());
			}
			else if(content instanceof EventDefinition) {
				EventDefinition ed = (EventDefinition) content;
				events.add(ed.getName());
			}
			
		}
		
		System.out.println("public class RunStatechart {\n");
		System.out.println("\tpublic static void main(String[] args) throws IOException {");
		System.out.println("\t\tScanner scanner = new Scanner(System.in);\r\n " + 
			    "		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		Scanner inputScanner = new Scanner(System.in);\r\n" + 
				"		while(scanner.hasNextLine()) {\r\n" +
				"		\tString cmd = scanner.nextLine();\r\n" + 
				"			switch(cmd) {");
		
		for(String event:events) {
			String capitalized = event.substring(0, 1).toUpperCase() + event.substring(1);
			System.out.println("\t\t\t\tcase \"" + event + "\":\r\n" +
					"			\t\ts.raise" + capitalized + "();");
			System.out.println("\t\t\t\t\ts.runCycle();\r\n" + 
					"			\t\tbreak;");
		}
		System.out.println("\t\t\t\tcase \"exit\":\r\n" + 
				"					System.out.println(\"Exiting...\"); \r\n" +
				"					System.exit(0);\r\n" + 
				"					break;\r\n" + 
				"				default:\r\n" + 
				"					System.out.println(\"Pleas enter a legit command...\");\r\n" + 
				"					break;");
		
		System.out.println("\t\t\t}");
		System.out.println("\t\t\tprint(s);");
		System.out.println("\t\t}");
		System.out.println("\t}\n");
		
		System.out.println("\tpublic static void print(IExampleStateachine s) {");
		
		for(String var:variables) {
			String cpd = var.substring(0, 1).toUpperCase() + var.substring(1);
			System.out.println("\t\tSystem.out.println(\"" + cpd.charAt(0) + " = \" + s.getSCInterface().get" + cpd + "());");
		}
		System.out.println("\t}");
		System.out.println("}");

		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}

//ArrayList<String> its_a_trap = new ArrayList<String>();
//int counter = 1;
//
//
//// Loading model
//EObject root = manager.loadModel("model_input/example.sct");
//
//// Reading model
//Statechart s = (Statechart) root;
//TreeIterator<EObject> iterator = s.eAllContents();
//while (iterator.hasNext()) {
//	EObject content = iterator.next();
//	if(content instanceof Transition){
//		Transition tr = (Transition) content;
//		System.out.println(tr.getSource().getName() + " -> " + tr.getTarget().getName());
//		
//	}else if (content instanceof State) {
//		State state = (State) content;
//		if(state.getOutgoingTransitions().size() == 0) {
//			its_a_trap.add(state.getName());
//		}
//		if(state.getName() == "") {
//			String newName;
//			System.out.println("Az egyik állapotnak nincs neve!");
//			if(state.getIncomingTransitions().size() != 0) {
//				newName = "state " + state.getIncomingTransitions().get(0).getSpecification() + " tranzicióból"; 
//			}
//			else {
//				newName = "elérhetetlen állapot " + counter;
//			}
//			System.out.println("Egy javasolt név: " + newName);
//		}
//		
//		System.out.println(state.getName()  );
//	}
//	
//	
//}
//
//for(int i = 0 ;i < its_a_trap.size();i++) {
//	System.out.println("Ez egy csapda: " + its_a_trap.get(i));
//}
