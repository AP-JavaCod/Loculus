package com.apjc.loculus;

public class BitData {
	
	private final int size;
	private final byte[] array;
	
	public BitData(int bitSize) {
		this.size = bitSize;
		int size = bitSize / 8;
		if(bitSize % 8 > 0) {
			size++;
		}
		array = new byte[size];
	}
	
	public static BitData split(BitData val1, BitData val2) {
		int size1 = val1.size;
		int size2 = val2.size;
		BitData data = new BitData(size1 + size2);
		for(int i = 0; i < size1; i++) {
			data.setBit(i, val1.isActiveBit(i));
		}
		for(int i = 0; i < size1; i++) {
			data.setBit(i + size1, val2.isActiveBit(i));
		}
		return data;
	}
	
	public void setBit(int position, boolean isActive) {
		if(size <= position) {
			throw new ArrayIndexOutOfBoundsException();
		}
		int index = position / 8;
		int bitPos = position % 8;
		if(isActive) {
			array[index] |= (1 << bitPos);
		}else {
			array[index] &= ~(1 << bitPos);
		}
	}
	
	public boolean isActiveBit(int position) {
		if(size <= position) {
			throw new ArrayIndexOutOfBoundsException();
		}
		int index = position / 8;
		int bitPos = position % 8;
		return (array[index] & (1 << bitPos)) != 0;
	}
	
	public int getSize() {
		return size;
	}
	
	@Override
	public String toString() {
		String str = "";
		for(int i = 0; i < size; i++) {
			str += isActiveBit(i) ? "1" : "0";
		}
		return str;
	}

}
