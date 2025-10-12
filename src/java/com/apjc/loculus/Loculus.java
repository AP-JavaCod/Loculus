package com.apjc.loculus;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Loculus <T> implements Serializable, Iterable<T>{
	
	private static final long serialVersionUID = 2588370670733477205L;
	private Noda noda;
	private BitChar bitCod;
	
	public Loculus() {
		noda = null;
		bitCod = null;
	}
	
	public void error(){
		bitCod.close();
		bitCod.setBit(0, true);
	}
	
	public Loculus(T values) throws InterruptedException {
		add(values);
	}
	
	public Loculus(List<T> data) throws InterruptedException {
		add(data);
	}
	
	public void add(T values) throws InterruptedException  {
		List<T> data = getValues();
		data.add(values);
		setNoda(values);
		setCod(data);
	}
	
	public void add(int index, T values) throws InterruptedException {
		List<T> data = getValues();
		data.add(index, values);
		setNoda(values);
		setCod(data);
	}
	
	public void add(List<T> list) throws InterruptedException {
		List<T> data = getValues();
		for(T val : list) {
			data.add(val);
			setNoda(val);
		}
		setCod(data);
	}
	
	public String getCod(){
		return bitCod.getCode();
	}
	
	private void setNoda(T values) {
		if(noda != null) {
			noda.add(values);
		}else {
			noda = new Noda(values);
		}
	}
	
	private void setCod(List<T> list) {
		bitCod = null;
		for(T el : list) {
			BitChar buffer = noda.getBits(el, 0);
			if(bitCod == null) {
				bitCod = buffer;
			}else {
				BitChar code = bitCod;
				bitCod = code.merge(buffer);
			}
		}
	}
	
	public T getValue(int index){
		int position = 0;
		while(position < bitCod.size() && index > 0) {
			if(!bitCod.isActive(position)) {
				index --;
			}
			position++;
		}
		for(int i = position; i < bitCod.size(); i++) {
			if(!bitCod.isActive(i)) {
				return noda.getValues(i - position);
			}
		}
		throw new ArrayIndexOutOfBoundsException();
	}
	
	public List<T> getValues() {
		List<T> data = new ArrayList<>();
		for(T values : this) {
			data.add(values);
		}
		return data;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new BitIterator();
	}
	
	public void close(){
		if(bitCod != null) {
			bitCod.close();
			bitCod = null;
		}
	}
	
	private class Noda implements Serializable{
		
		private static final long serialVersionUID = -7808087483619656304L;
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
		
		public BitChar getBits(T element, int size){
			BitChar bits;
			if(this.values.equals(element)) {
				bits = new BitChar(size + 1);
				bits.setBit(size, false);
			}else{
				bits = next != null ? next.getBits(element, size + 1) : null;
				if(bits != null) {
					bits.setBit(size, true);
				}
			}
			return bits;
		}
		
		public T getValues(int index) {
			if(index == 0) {
				return values;
			}
			return next != null ? next.getValues(index - 1) : null;
		}
		
	}
	
	private class BitIterator implements Iterator<T>{

		private int index = 0;
		
		@Override
		public boolean hasNext() {
			return bitCod != null && index < bitCod.size();
		}

		@Override
		public T next() {
			int pos = index + 1;
			for(;index < bitCod.size(); index++) {
				if(!bitCod.isActive(index)) {
					index++;
					return noda.getValues(index - pos);	
				}	
			}
			return null;
		}
		
	}
	
}
