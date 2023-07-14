package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;



import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	private Graph<Fermata, DefaultWeightedEdge> grafo;
	List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIDMap;

	public void creaGrafo() {
		// Bisogna creare sempre il grafo specificando il tipo degli archi
		this.grafo = new SimpleGraph<>(DefaultWeightedEdge.class);

		// aggiungi vertici
		MetroDAO dao = new MetroDAO();
		fermate = dao.readFermate();
		fermateIDMap = new HashMap<Integer, Fermata>();
		for (Fermata f : this.fermate) {
			this.fermateIDMap.put(f.getIdFermata(), f);
		}

		Graphs.addAllVertices(this.grafo, this.fermate);
		// aggiungi archi
		/**
		 * Metodo 1 Vado a prendere qualunque coppia di vertici e vedo se sono connessi
		 * o meno, se si aggiungo l'arco altrimenti non lo aggiungo, poco efficiente
		 * grafi poco densi, ovvero quei grafi che hanno pochi collegamenti
		 */
//		for (Fermata partenza: this.grafo.vertexSet()) {
//			for (Fermata arrivo : this.grafo.vertexSet()) {
//				if(dao.isConnesse(partenza,arrivo)) {
//					this.grafo.addEdge(partenza, arrivo);
//				}
//			}
//		}

		/**
		 * Metodo 2: prendo una stazione per volta e mi chiedo quali sono le stazioni
		 * vicine, ovvero trovo la lista delle stazioni e poi aggiungi gli archi
		 */
		for (Fermata partenza : this.grafo.vertexSet()) {
			List<Fermata> collegate = dao.trovaCollegate(partenza);

			for (Fermata arrivo : collegate) {
				this.grafo.addEdge(partenza, arrivo);
			}
		}

		/**
		 * Metodo 2a; cerchiamo di alleggerire il costo comuputazionale migliorando la
		 * query. Data una fermata troviamo la lista di id connessi.
		 */
		for (Fermata partenza : this.grafo.vertexSet()) {
			List<Fermata> collegate = dao.trovaIdCollegate(partenza, this.fermateIDMap);

			for (Fermata arrivo : collegate) {
				this.grafo.addEdge(partenza, arrivo);
			}
		}

		/**
		 * Metodo 3: faccio una query per prendere tutti gli edges. Molto più efficiente
		 * degli altri metodi in quanto facciamo una sola query invece di farne 619,
		 * ovvero una per ogni vertice
		 */
		List<coppieF> allCoppie = dao.getAllCoppie(fermateIDMap);
		for (coppieF coppia : allCoppie) {
			double distanza =LatLngTool.distance(coppia.getPartenza().getCoords(), coppia.getArrivo().getCoords(), LengthUnit.METER);
			Graphs.addEdge(this.grafo, coppia.getPartenza(), coppia.getArrivo(), distanza);
		}

		System.out.println("Grafo creato con " + this.grafo.vertexSet().size() + " vertici e "
				+ this.grafo.edgeSet().size() + " archi");

	}

	/**
	 * Determina il percorso minimo tra due fermate Per trovare la soluzione si deve
	 * dividere il problema in due sottoproblemi: visitare il grafo partendo dalla
	 * soluzione di partenza che mi costruisce l'albero di visita; Analisi
	 * dell'albero per ricostruire il percorso
	 * 
	 * @return lista di fermate da attraversare per arrivare da una fermata
	 *         all'altra
	 */
	public List<Fermata> percorso(Fermata partenza, Fermata arrivo) {
		DijkstraShortestPath<Fermata, DefaultWeightedEdge> sp =
				new DijkstraShortestPath<>(grafo);
		
		GraphPath<Fermata, DefaultWeightedEdge> gp = sp.getPath(partenza, arrivo);
		return gp.getVertexList();
	}

	public List<Fermata> getAllFermate() {
		MetroDAO dao = new MetroDAO();
		return dao.readFermate();
	}

	public boolean isGrafoLoaded() {
		return this.grafo.vertexSet().size() > 0;
	}
}
