package com.apjc.loculus;

import java.io.Serializable;

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
	
	public static BitData split(BitData... vals) {
		int size = 0;
		for(BitData d : vals) {
			size += d.SIZE;
		}
		BitData newData = new BitData(size);
		int index = 0;
		for(BitData d : vals) {
			for(int i = 0; i < d.SIZE; i++) {
				newData.setBit(index, d.isActiveBit(i));
				index++;
			}
		}
		return newData;
	}
	
	public void setBit(int position, boolean isActive) {
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
	
	public int getSize() {
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
