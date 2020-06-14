package types;

public class Sensor {
	
	private int id;
	private int iddispositivo;
	private String planta;
	private int umbral;
	private int potencia;
	private long initialTimestamp;
	
	public Sensor(int id, int iddispositivo, String planta, int umbral, int potencia, long initialTimestamp) {
		super();
		this.id = id;
		this.iddispositivo = iddispositivo;
		this.planta = planta;
		this.umbral = umbral;
		this.potencia = potencia;
		this.initialTimestamp = initialTimestamp;
	}

	public Sensor() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIddispositivo() {
		return iddispositivo;
	}

	public void setIddispositivo(int iddispositivo) {
		this.iddispositivo = iddispositivo;
	}

	public String getPlanta() {
		return planta;
	}

	public void setPlanta(String planta) {
		this.planta = planta;
	}

	public int getUmbral() {
		return umbral;
	}

	public void setUmbral(int umbral) {
		this.umbral = umbral;
	}

	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public long getInitialTimestamp() {
		return initialTimestamp;
	}

	public void setInitialTimestamp(long initialTimestamp) {
		this.initialTimestamp = initialTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + iddispositivo;
		result = prime * result + (int) (initialTimestamp ^ (initialTimestamp >>> 32));
		result = prime * result + ((planta == null) ? 0 : planta.hashCode());
		result = prime * result + potencia;
		result = prime * result + umbral;
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
		Sensor other = (Sensor) obj;
		if (id != other.id)
			return false;
		if (iddispositivo != other.iddispositivo)
			return false;
		if (initialTimestamp != other.initialTimestamp)
			return false;
		if (planta == null) {
			if (other.planta != null)
				return false;
		} else if (!planta.equals(other.planta))
			return false;
		if (potencia != other.potencia)
			return false;
		if (umbral != other.umbral)
			return false;
		return true;
	}
	
	
	
	
}
