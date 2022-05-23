package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		model.creaGrafo("traffic-accident", 2);
		
		System.out.println(model.getArchiMaggioriPesoMedio());
		
	}

}
