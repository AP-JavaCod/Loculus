package com.apjc.loculus;

import java.io.Serializable;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class BitData implements Serializable{
	
	private static final long serialVersionUID = -3491889709823755046L;
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
	
	public static BitData merge(BitData... vals) throws InterruptedException {
		MergeTask task = new MergeTask();
		BitData data = task.add(vals);
		ExecutorService pool = Executors.newFixedThreadPool(4);
		pool.invokeAll(task.getTask());
		pool.shutdown();
		return data;
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
	
	private static class MergeTask{
		
		private BitData buffer = null;
		private List<Callable<Object>> task = new ArrayList<>();
		
		public BitData add(BitData[] data) {
			int totalSize = 0;
			for(BitData el : data) {
				int position = totalSize;
				totalSize += el.SIZE;
				task.add(Executors.callable(() -> {
					for(int i = 0; i < el.SIZE; i++) {
						buffer.setBit(position + i, el.isActiveBit(i));
					}
				}));
			}
			buffer = new BitData(totalSize);
			return buffer;
		}
		
		public List<Callable<Object>> getTask(){
			return task;
		}
		
	}

}
