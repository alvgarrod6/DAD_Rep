package types;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Riego {
	private static final AtomicInteger COUNTER = 
			new AtomicInteger();
	
	private int id;
	private float value;
	private long timestamp;
	
	@JsonCreator	
	public Riego(
			@JsonProperty("value") float value, 
			@JsonProperty("timestamp") long timestamp) {
		super();
		this.id = COUNTER.getAndIncrement();
		this.value = value;
		this.timestamp = timestamp;
	}
	
	public Riego() {
		super();
		this.id = COUNTER.getAndIncrement();
		this.value = 0;
		this.timestamp = Calendar.getInstance().getTimeInMillis();
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
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
		Riego other = (Riego) obj;
		if (id != other.id)
			return false;
		if (timestamp != other.timestamp)
			return false;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}
	
	
}
