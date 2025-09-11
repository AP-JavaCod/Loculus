package com.apjc.loculus;

import java.io.*;

public class FileLoculus {
	
	public static <T extends Serializable> void serializableFale(Loculus<T> loculus, String file) throws IOException {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
			oos.writeObject(loculus);
		}
	}
	
	public static Object getData(String file) throws IOException, ClassNotFoundException{
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
			return ois.readObject();
		}
	}

}
