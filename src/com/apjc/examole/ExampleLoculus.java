package com.apjc.examole;

import com.apjc.loculus.Loculus;

public class ExampleLoculus {

	public static void main(String[] args) throws InterruptedException {
		String str = "Hello world";
		Loculus<Character> loculus = new Loculus<>(str.chars().mapToObj(i -> (char)i).toList());
		loculus.add('!');
		loculus.add(' ');
		String data = "I am lave java cod";
		loculus.add(data.chars().mapToObj(i -> (char)i).toList());
		System.out.println(loculus.getCod());
		for(char el: loculus.getValues()) {
			System.out.print(el);
		}
		System.out.println();
	}

}
