package kmean.stat;

import java.util.ArrayList;
import java.util.List;

public class Punto {
    private Float[] data;
    private String[] AttributeValues;

	public Punto(String[] strings) {
		super();
		List<Float> puntos = new ArrayList<Float>();
		for (String string : strings) {
			puntos.add(Float.parseFloat(string));
		}
		this.data = puntos.toArray(new Float[strings.length]);
	}

    public Punto(String[] strings,String[] attributeValues) {
	super();
	List<Float> puntos = new ArrayList<Float>();
	for (String string : strings) {
	    puntos.add(Float.parseFloat(string));
	}
	this.data = puntos.toArray(new Float[strings.length]);
	List<String> TempAttribute=new ArrayList<>();
	for(String Attribute: attributeValues){
		TempAttribute.add(Attribute);
	}
	this.AttributeValues=TempAttribute.toArray(new String[attributeValues.length]);
    }

    public Punto(Float[] data) {
	this.data = data;
    }

    public float get(int dimension) {
	return data[dimension];
    }

    public int getGrado() {
	return data.length;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(data[0]);
	for (int i = 1; i < data.length; i++) {
	    sb.append(", ");
	    sb.append(data[i]);
	}
	return sb.toString();
    }

    public String AttributeToString(){
	StringBuilder sb=new StringBuilder();
	sb.append(AttributeValues[0]);
	if(AttributeValues.length>1){
		for(int i=1;i<AttributeValues.length;i++){
			sb.append(",");
			sb.append(AttributeValues[i]);
		}
	}
	return sb.toString();
	}

    public Double distanciaEuclideana(Punto destino) {
	Double d = 0d;
	for (int i = 0; i < data.length; i++) {
	    d += Math.pow(data[i] - destino.get(i), 2);
	}
	return Math.sqrt(d);
    }

    @Override
    public boolean equals(Object obj) {
	Punto other = (Punto) obj;
	for (int i = 0; i < data.length; i++) {
	    if (data[i] != other.get(i)) {
		return false;
	    }
	}
	return true;
    }
}