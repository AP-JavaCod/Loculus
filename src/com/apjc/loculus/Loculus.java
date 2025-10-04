package com.apjc.loculus;

import java.util.List;
import java.util.concurrent.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Loculus <T> implements Serializable, Iterable<T>{
	
	private static final long serialVersionUID = 2588370670733477205L;
	private Noda noda;
	private BitData bitCod;
	
	public Loculus() {
		noda = null;
		bitCod = null;
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
	
	public String getCod() {
		return bitCod.toString();
	}
	
	private void setNoda(T values) {
		if(noda != null) {
			noda.add(values);
		}else {
			noda = new Noda(values);
		}
	}
	
	private void setCod(List<T> list) throws InterruptedException {
		int totalTask = 0;
		List<Callable<Object>> task = new ArrayList<>();
		for(T el : list) {
			int position = totalTask;
			int lim = noda.getIndex(el);
			task.add(Executors.callable(() -> {
				for(int i = 0; i < lim; i++) {
					bitCod.setBit(position + i, true);
				}
			}));
			totalTask += lim + 1;
		}
		bitCod = new BitData(totalTask);
		ExecutorService pool = Executors.newFixedThreadPool(4);
		pool.invokeAll(task);
		pool.shutdown();
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
			return bitCod != null && index < bitCod.size();
		}

		@Override
		public T next() {
			int pos = index + 1;
			for(;index < bitCod.size(); index++) {
				if(!bitCod.isActiveBit(index)) {
					index++;
					return noda.getValues(index - pos);
				}
			}
			return null;
		}
		
	}
	
}
