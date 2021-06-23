package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private Graph<Match, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Match> idMap;
	private List<Adiacenza> archi;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo(int month, int num) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.idMap = new HashMap<>();
		this.dao.getMatchesByMonth(month, idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		this.archi = this.dao.getAdiacenze(idMap, num);
		for (Adiacenza a : archi) {
			Graphs.addEdge(this.grafo, a.getMatchId1(), a.getMatchId2(), a.getPeso());
		}
		
	}
	
	public List<Adiacenza> getMaggiorPeso() {
		
		int max = 0;
		
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (this.grafo.getEdgeWeight(e)>max) {
				max = (int) this.grafo.getEdgeWeight(e);
			}
		}
		
		List<Adiacenza> risultato = new ArrayList<>();
		for (Adiacenza a : archi) {
			if (a.getPeso()==max) {
				risultato.add(a);
			}
		}
		
		return risultato;
		
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
