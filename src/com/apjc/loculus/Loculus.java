package com.apjc.loculus;

import java.util.List;
import java.util.concurrent.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Loculus <T> implements Serializable, Iterable<T>{
	
	private Noda noda;
	private BitData bitCod;
	
	public Loculus(List<T> data) throws InterruptedException {
		for(T val : data) {
			if(noda == null) {
				noda = new Noda(val);
			}else {
				noda.add(val);
			}
		}
		setCod(data);
	}
	
	public void add(T values) throws InterruptedException  {
		List<T> data = getValues();
		data.add(values);
		noda.add(values);
		setCod(data);
	}
	
	public void add(List<T> list) throws InterruptedException {
		List<T> data = getValues();
		for(T val : list) {
			data.add(val);
			noda.add(val);
		}
		setCod(data);
	}
	
	public String getCod() {
		return bitCod.toString();
	}
	
	private void setCod(List<T> data) throws InterruptedException {
		int sizeList = data.size();
		BitData[] buffers = new BitData[sizeList];
		for(int i = 0; i < sizeList; i++) {
			buffers[i] = new BitData(noda.getIndex(data.get(i)) + 1);
		}
		ExecutorService pool = Executors.newFixedThreadPool(4);
		List<Callable<Object>> task = new ArrayList<>();
		for(int i = 0; i < sizeList; i++) {
			BitData buffer = buffers[i];
			task.add(Executors.callable(() -> {
				for(int j = 1; j < buffer.size(); j++) {
					buffer.setBit(j - 1, true);
				}
			}));
		}
		List<Future<Object>> futures = pool.invokeAll(task);
		pool.shutdown();
		bitCod = BitData.split(buffers);
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
	
	@Override
	public Iterator<T> iterator() {
		return new BitIterator();
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
	
	private class BitIterator implements Iterator<T>{

		private int index = 0;
		
		@Override
		public boolean hasNext() {
			return index < bitCod.size();
		}

		@Override
		public T next() {
			int pos = 0;
			while(index + pos < bitCod.size()) {
				if(!bitCod.isActiveBit(index + pos)) {
					index += pos + 1;
					return noda.getValues(pos);
				}
				pos++;
			}
			return null;
		}
		
	}
	
}
