package com.apjc.loculus;

import java.io.Serializable;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class BitData implements Serializable{
	
	private final int SIZE;
	private final byte[] buffer;
	
	public BitData(int bitSize) {
		SIZE = bitSize;
		int size = bitSize / 8;
		if(bitSize % 8 > 0) {
			size++;
		}
		buffer = new byte[size];
	}
	
	public static BitData split(BitData... vals) throws InterruptedException {
		int sizeVal = vals.length;
		int sizeTotal = 0;
		int[] indexArr = new int[sizeVal];
		for(int i = 0; i < sizeVal; i++) {
			indexArr[i] = sizeTotal;
			sizeTotal += vals[i].SIZE;
		}
		ExecutorService pool = Executors.newFixedThreadPool(4);
		List<Callable<Object>> task = new ArrayList<>();
		BitData newData = new BitData(sizeTotal);
		for(int i = 0; i < sizeVal; i++) {
			int position = indexArr[i];
			BitData d = vals[i];
			task.add(Executors.callable(() -> { 
				for(int j = 0; j < d.SIZE; j++) {
					newData.setBit(position + j, d.isActiveBit(j));
				}
			}));
		}
		List<Future<Object>> futures = pool.invokeAll(task);
		pool.shutdown();
		return newData;
	}
	
	public synchronized void setBit(int position, boolean isActive) {
		if(SIZE <= position) {
			throw new ArrayIndexOutOfBoundsException();
		}
		int index = position / 8;
		int bitPos = position % 8;
		if(isActive) {
			buffer[index] |= (1 << bitPos);
		}else {
			buffer[index] &= ~(1 << bitPos);
		}
	}
	
	public boolean isActiveBit(int position) {
		if(SIZE <= position) {
			throw new ArrayIndexOutOfBoundsException();
		}
		int index = position / 8;
		int bitPos = position % 8;
		return (buffer[index] & (1 << bitPos)) != 0;
	}
	
	public boolean[] isActiveAll() {
		boolean[] bool = new boolean[SIZE];
		for(int i = 0; i < SIZE; i++) {
			bool[i] = isActiveBit(i);
		}
		return bool;
	}
	
	public int size() {
		return SIZE;
	}
	
	@Override
	public String toString() {
		String str = "";
		for(int i = 0; i < SIZE; i++) {
			str += isActiveBit(i) ? "1" : "0";
		}
		return str;
	}

}
