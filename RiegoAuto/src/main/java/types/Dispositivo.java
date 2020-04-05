package types;

public class Dispositivo {
	
	private int idDispositivo;
	private String ip;
	private int idUsuario;
	private long initialTimestamp;
	
	public Dispositivo(int idDispositivo, String ip, int idUsuario,
			long initialTimestamp) {
		super();
		this.idDispositivo = idDispositivo;
		this.ip = ip;
		this.idUsuario = idUsuario;
		this.initialTimestamp = initialTimestamp;
	}

	public Dispositivo() {
		super();
	}

	public int getIdDispositivo() {
		return idDispositivo;
	}

	public void setIdDispositivo(int idDispositivo) {
		this.idDispositivo = idDispositivo;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
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
		result = prime * result + idDispositivo;
		result = prime * result + idUsuario;
		result = prime * result + (int) (initialTimestamp ^ (initialTimestamp >>> 32));
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
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
		if (idDispositivo != other.idDispositivo)
			return false;
		if (idUsuario != other.idUsuario)
			return false;
		if (initialTimestamp != other.initialTimestamp)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		return true;
	}
	
	
	

	
	
	
}
