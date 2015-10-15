package abe;

/*
 * author: wenzilong,licong
 */

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

public class SecretKey extends Key{
	private Attribute[] attributes;

	public SecretKey(){
		super(Type.SECRET);
	}
	
	public void setAttributes(Attribute[] attributes){
		this.attributes = attributes;
	}
	
	public Attribute[] getAttributes(){
		return attributes;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Type:" + getType() + "\n");
		sb.append("Attributes:{\n");
		for(int i=0; i<attributes.length; i++)
			sb.append(attributes[i] + "\t");
		sb.append("}\n");
		sb.append("Components:{\n");
		for(Map.Entry<String, Element> element : getComponents().entrySet()){
			sb.append(element.getKey() + "----> " + element.getValue() + "\n");
		}
		sb.append("}");
		return sb.toString();
	}
}
