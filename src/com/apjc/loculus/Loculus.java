package com.apjc.loculus;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

public class Loculus <T> implements Serializable{
	
	private Noda noda;
	private BitData bitCod;
	
	public Loculus(List<T> data) {
		setCod(data);
	}
	
	public void add(T values) {
		List<T> data = getValues();
		noda.add(values);
		data.add(values);
		setCod(data);
	}
	
	public void add(List<T> list) {
		List<T> data = getValues();
		for(T val : list) {
			noda.add(val);
			data.add(val);
		}
		setCod(data);
	}
	
	public String getCod() {
		return bitCod.toString();
	}
	
	private void setCod(List<T> data) {
		for(T val : data) {
			if(noda == null) {
				noda = new Noda(val);
			}else {
				noda.add(val);
			}
		}
		bitCod = new BitData(0);
		for(T val : data) {
			int size = noda.getIndex(val) + 1;
			BitData pas = new BitData(size);
			for(int i = 1; i < size; i++) {
				pas.setBit(i - 1, true);
			}
			bitCod = BitData.split(bitCod, pas);
		}
	}
	
	public List<T> getValues() {
		List<T> data = new ArrayList<>();
		int index = 0;
		for(boolean bool : bitCod.isActiveAll()) {
			if(bool) {
				index++;
			}else {
				data.add(noda.getValues(index));
				index = 0;
			}
		}
		return data;
	}
	
	private class Noda implements Serializable{
		
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
		
		public int getIndex(T element) {
			if(this.values.equals(element)) {
				return 0;
			}
			return next != null ? 1 + next.getIndex(element) : -1;
		}
		
		public T getValues(int index) {
			if(index == 0) {
				return values;
			}
			return next != null ? next.getValues(index - 1) : null;
		}
		
	}
	
}
