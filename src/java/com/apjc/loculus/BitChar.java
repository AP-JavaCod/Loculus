package com.apjc.loculus;

public class BitChar {
	
	private long bitID;
	
	public BitChar(int size) {
		bitID = create(size);
	}
	
	private BitChar(long id) {
		bitID = id;
	}
	
	public boolean isOpen() {
		return bitID > 0;
	}
	
	public void close(){
		delete();
		bitID = -1;
	}
	
	public boolean[] isActiveAll(){
		int l = size();
		boolean[] bits = new boolean[l];
		for(int i = 0; i < l; i++) {
			bits[i] = isActive(i);
		}
		return bits;
	}
	
	public BitChar merge(BitChar bits){
		return new BitChar(merge(this.bitID, bits.bitID));
	}
	
	public native void setBit(int index, boolean isActivite);
	public native boolean isActive(int index);
	public native int size();
	public native String getCode();
	
	private native long create(int size);
	private native void delete();
	private native long merge(long idA, long id2);
	
	static{
		
		System.loadLibrary("./Debug/libLoculus");
		
	}

}
