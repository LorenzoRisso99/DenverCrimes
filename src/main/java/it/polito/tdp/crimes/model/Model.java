package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private Graph<String, DefaultWeightedEdge> grafo;
	
	private EventsDao dao;
	
	private List<String> best;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiunta vertici
		
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		
		//aggiunta archi
		
		for(Adiacenza a : dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(), a.getPeso());
		}
		
		System.out.println("grafo creato");
		System.out.println("Vertici : " + this.grafo.vertexSet().size());
		System.out.println("Archi : " + this.grafo.edgeSet().size());
		
		// DA FARE --> Riempire tendina combo box degli archi
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getArchi(){
		List<Adiacenza> archi = new ArrayList<Adiacenza>();
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			archi.add(new Adiacenza(this.grafo.getEdgeSource(e),
						this.grafo.getEdgeTarget(e), 
						(int) this.grafo.getEdgeWeight(e)));
		}
		return archi;
	}
	
	public List<String> getCategorie(){
		return this.dao.getCategorie();
	}
	
	// PUNTO D
	
	public List<Adiacenza> getArchiMaggioriPesoMedio() {
		
		// scorro archi del grafo e calcolo peso medio
		
		double pesoTot = 0.0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoTot += this.grafo.getEdgeWeight(e);
		}
		double avg = pesoTot / this.grafo.edgeSet().size();
		
		// ri-scorro tutti gli archi prendendo quelli maggiori di avg
		
		List<Adiacenza> result = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e) > avg) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int)this.grafo.getEdgeWeight(e)));
			}
		}
		return result;	
	}
	
	
	// PUNTO 2
	// Ricorsione 
	
	public List<String> calcolaPercoso(String sorgente, String destinazione) {
		
		best = new LinkedList<>();
		
		List<String> parziale = new LinkedList<>();
		
		parziale.add(sorgente);
		
		cerca(parziale, destinazione);
		
		return best;
		
	}
	
	public void cerca(List<String> parziale, String destinazione) {
		
		//condizione di terminazione
		
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			
			// è la soluzione migliore ???
			
			if(parziale.size() > best.size()) {
				
				best = new LinkedList<>(parziale);
				
			}
			
			return;
			
		}
		
		//scorro i vicini dell'ultimo inserito e provo le varie "strade"
		
		for(String s : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			
			if(!parziale.contains(s)) {
			
				parziale.add(s);
				
				cerca(parziale,destinazione);
				
				parziale.remove(parziale.size()-1);
				
			}
		}
		
	}
	
}
