package it.polito.tdp.metroparis.model;

import java.util.Objects;

public class coppieF {
	Fermata partenza;
	Fermata arrivo;
	public Fermata getPartenza() {
		return partenza;
	}
	public void setPartenza(Fermata partenza) {
		this.partenza = partenza;
	}
	public Fermata getArrivo() {
		return arrivo;
	}
	public void setArrivo(Fermata arrivo) {
		this.arrivo = arrivo;
	}
	public coppieF(Fermata partenza, Fermata arrivo) {
		super();
		this.partenza = partenza;
		this.arrivo = arrivo;
	}
	@Override
	public int hashCode() {
		return Objects.hash(arrivo, partenza);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		coppieF other = (coppieF) obj;
		return Objects.equals(arrivo, other.arrivo) && Objects.equals(partenza, other.partenza);
	}
	
	
}
