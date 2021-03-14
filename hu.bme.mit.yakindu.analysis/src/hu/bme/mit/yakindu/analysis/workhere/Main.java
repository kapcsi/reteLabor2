package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

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
		ArrayList<String> its_a_trap = new ArrayList<String>();
		int counter = 1;
		
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof Transition){
				Transition tr = (Transition) content;
				System.out.println(tr.getSource().getName() + " -> " + tr.getTarget().getName());
				
			}else if (content instanceof State) {
				State state = (State) content;
				if(state.getOutgoingTransitions().size() == 0) {
					its_a_trap.add(state.getName());
				}
				if(state.getName() == "") {
					String newName;
					System.out.println("Az egyik állapotnak nincs neve!");
					if(state.getIncomingTransitions().size() != 0) {
						newName = "state " + state.getIncomingTransitions().get(0).getSpecification() + " tranzicióból"; 
					}
					else {
						newName = "elérhetetlen állapot " + counter;
					}
					System.out.println("Egy javasolt név: " + newName);
				}
				
				System.out.println(state.getName()  );
			}
			
			
		}
		
		for(int i = 0 ;i < its_a_trap.size();i++) {
			System.out.println("Ez egy csapda: " + its_a_trap.get(i));
		}
		
		
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
