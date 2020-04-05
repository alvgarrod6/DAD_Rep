package types;

public class Riego {
	
	private int id;
	private long timestamp;
	private int humedad;
	private boolean manualAuto;
	private int idsensor;
	
	public Riego(int id, long timestamp, int humedad, boolean manualAuto, int idsensor) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.humedad = humedad;
		this.manualAuto = manualAuto;
		this.idsensor = idsensor;
	}

	public Riego() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getHumedad() {
		return humedad;
	}

	public void setHumedad(int humedad) {
		this.humedad = humedad;
	}

	public boolean isManualAuto() {
		return manualAuto;
	}

	public void setManualAuto(boolean manualAuto) {
		this.manualAuto = manualAuto;
	}

	public int getIdsensor() {
		return idsensor;
	}

	public void setIdsensor(int idsensor) {
		this.idsensor = idsensor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + humedad;
		result = prime * result + id;
		result = prime * result + idsensor;
		result = prime * result + (manualAuto ? 1231 : 1237);
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
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
		Riego other = (Riego) obj;
		if (humedad != other.humedad)
			return false;
		if (id != other.id)
			return false;
		if (idsensor != other.idsensor)
			return false;
		if (manualAuto != other.manualAuto)
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
	
	
	
	
}
