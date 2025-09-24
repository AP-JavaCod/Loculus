package com.apjc.loculus;

import java.util.List;
import java.util.concurrent.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Loculus <T> implements Serializable, Iterable<T>{
	
	private static final long serialVersionUID = -1055085668157322995L;
	private final Noda NODA;
	private BitData bitCod;
	
	public Loculus(List<T> data) throws InterruptedException {
		NODA = new Noda(data.getFirst());
		for(int i = 1; i < data.size(); i++) {
			NODA.add(data.get(i));
		}
		setCod(data);
	}
	
	public void add(T values) throws InterruptedException  {
		List<T> data = getValues();
		data.add(values);
		NODA.add(values);
		setCod(data);
	}
	
	public void add(int index, T values) throws InterruptedException {
		List<T> data = getValues();
		data.add(index, values);
		NODA.add(values);
		setCod(data);
	}
	
	public void add(List<T> list) throws InterruptedException {
		List<T> data = getValues();
		for(T val : list) {
			data.add(val);
			NODA.add(val);
		}
		setCod(data);
	}
	
	public String getCod() {
		return bitCod.toString();
	}
	
	private void setCod(List<T> data) throws InterruptedException {
		int sizeList = data.size();
		BitData[] buffers = new BitData[sizeList];
		ExecutorService pool = Executors.newFixedThreadPool(4);
		List<Callable<Object>> task = new ArrayList<>();
		for(int i = 0; i < sizeList; i++) {
			BitData buffer = new BitData(NODA.getIndex(data.get(i)) + 1);
			buffers[i] = buffer;
			task.add(Executors.callable(() -> {
				for(int j = 1; j < buffer.size(); j++) {
					buffer.setBit(j - 1, true);
				}
			}));
		}
		pool.invokeAll(task);
		pool.shutdown();
		bitCod = BitData.merge(buffers);
	}
	
	public T getValue(int index) {
		int position = 0;
		while(position < bitCod.size() && index > 0) {
			if(!bitCod.isActiveBit(position)) {
				index --;
			}
			position++;
		}
		for(int i = position; i < bitCod.size(); i++) {
			if(!bitCod.isActiveBit(i)) {
				return NODA.getValues(i - position);
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
	
	private class BitIterator implements Iterator<T>{

		private int index = 0;
		
		@Override
		public boolean hasNext() {
			return index < bitCod.size();
		}

		@Override
		public T next() {
			int pos = index + 1;
			for(;index < bitCod.size(); index++) {
				if(!bitCod.isActiveBit(index)) {
					index++;
					return NODA.getValues(index - pos);
				}
			}
			return null;
		}
		
	}
	
}
