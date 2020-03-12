package types;

import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Dispositivo {
	private static final AtomicInteger COUNTER = 
			new AtomicInteger();
	
	private int id;
	private float value;
	private String planta;
	
	@JsonCreator	
	public Dispositivo(
			@JsonProperty("value") float value, 
			@JsonProperty("planta") String planta) {
		super();
		this.id = COUNTER.getAndIncrement();
		this.value = value;
		this.planta = planta;
	}
	
	public Dispositivo() {
		super();
		this.id = COUNTER.getAndIncrement();
		this.value = 0;
		this.planta = "";
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getPlanta() {
		return planta;
	}

	public void setPlanta(String planta) {
		this.planta = planta;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((planta == null) ? 0 : planta.hashCode());
		result = prime * result + Float.floatToIntBits(value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dispositivo other = (Dispositivo) obj;
		if (id != other.id)
			return false;
		if (planta == null) {
			if (other.planta != null)
				return false;
		} else if (!planta.equals(other.planta))
			return false;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}
	
	
}
