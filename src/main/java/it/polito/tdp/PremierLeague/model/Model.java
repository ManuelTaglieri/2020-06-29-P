package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
	private double peso;
	private List<Match> percorsoMig;
	
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
	
	public Collection<Match> getVertici() {
		List<Match> risultato = new ArrayList<>(idMap.values());
		Collections.sort(risultato);
		return risultato;
	}
	
	public List<Match> getPercorso(Match m1, Match m2) {
		this.peso = 0;
		this.percorsoMig = new LinkedList<>();
		LinkedList<Match> percorso = new LinkedList<Match>();
		percorso.add(m1);
		ricorsiva(m1, m2, percorso, 0);
		return this.percorsoMig;
	}
	
	public double getPeso() {
		return this.peso;
	}

	private void ricorsiva(Match m1, Match m2, LinkedList<Match> percorso, double peso) {
		
		if (m1.equals(m2)) {
			if (peso>this.peso) {
				this.peso = peso;
				this.percorsoMig = new LinkedList<>(percorso);
			}
		} else {
			for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(m1)) {
				Match m = Graphs.getOppositeVertex(this.grafo, e, m1);
				if (!(m.getTeamHomeID()==m1.getTeamHomeID() && m.getTeamAwayID()==m1.getTeamAwayID()) && !(m.getTeamAwayID()==m1.getTeamHomeID() && m.getTeamHomeID()==m1.getTeamAwayID()) && !percorso.contains(m)) {
					percorso.add(m);
					ricorsiva(m, m2, percorso, (int) (peso + this.grafo.getEdgeWeight(e)));
					percorso.remove(m);
				}
			}
		}
		
	}
	
}
