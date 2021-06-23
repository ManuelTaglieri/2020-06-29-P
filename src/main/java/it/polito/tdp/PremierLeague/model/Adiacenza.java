package it.polito.tdp.PremierLeague.model;

public class Adiacenza {
	
	private Match match1;
	private Match match2;
	private int peso;
	public Adiacenza(Match match, Match match2, int peso) {
		super();
		this.match1 = match;
		this.match2 = match2;
		this.peso = peso;
	}
	public Match getMatchId1() {
		return match1;
	}
	public void setMatchId1(Match matchId1) {
		this.match1 = matchId1;
	}
	public Match getMatchId2() {
		return match2;
	}
	public void setMatchId2(Match matchId2) {
		this.match2 = matchId2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "match1=" + match1.toString() + ", match2=" + match2.toString() + ", numGiocatori=" + peso;
	}

}
