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
	
	public static BitData split(BitData... vals) {
		int size = 0;
		for(BitData d : vals) {
			size += d.size;
		}
		BitData newData = new BitData(size);
		int index = 0;
		for(BitData d : vals) {
			for(int i = 0; i < d.size; i++) {
				newData.setBit(index, d.isActiveBit(i));
				index++;
			}
		}
		return newData;
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
