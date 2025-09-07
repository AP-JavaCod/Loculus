package com.apjc.loculus;

import java.util.List;
import java.util.ArrayList;

public class Loculus <T> {
	
	private Noda noda;
	
	public String setCod(T[] data) {
		for(T val : data) {
			if(noda == null) {
				noda = new Noda(val);
			}else {
				noda.add(val);
			}
		}
		String cod = "";
		for(T val : data) {
			cod += noda.getCod(val);
		}
		return cod;
	}
	
	public List<T> getValues(String cod) {
		List<T> data = new ArrayList<>();
		String codes = "";
		for(char el : cod.toCharArray()) {
			codes += el;
			if(el == '0') {
				data.add(noda.getValues(codes));
				codes = "";
			}
		}
		return data;
	}
	
	private class Noda{
		
		private T values;
		private int quantityValues;
		private Noda next;
		
		public Noda(T values) {
			this.values = values;
			quantityValues = 1;
		}
		
		public void add(T values) {
			if(this.values.equals(values)) {
				quantityValues++;
			}else if(next != null){
				next.add(values);
				if(this.quantityValues < next.quantityValues) {
					T pasV = this.values;
					int pasQ = this.quantityValues;
					this.values = next.values;
					this.quantityValues = next.quantityValues;
					next.values = pasV;
					next.quantityValues = pasQ;
				}
			}else {
				next = new Noda(values);
			}
		}
		
		public String getCod(T elements) {
			if(this.values.equals(elements)) {
				return "0";
			}
			return next != null ? "1" + next.getCod(elements) : null;
		}
		
		public T getValues(String cod) {
			if(cod.equals("0")) {
				return values;
			}else if(next != null) {
				return next.getValues(cod.substring(1, cod.length()));
			}
			return null;
		}
		
	}
	
}
