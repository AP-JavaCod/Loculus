package com.apjc.examole;

import com.apjc.loculus.Loculus;

public class ExampleLoculus {

	public static void main(String[] args) {
		Loculus<Character> loculus = new Loculus<>();
		String str = "Hello world";
		String cod = loculus.setCod(str.chars().mapToObj(i -> (char)i).toArray(Character[]::new));
		System.out.println(cod);
		for(char el: loculus.getValues(cod)) {
			System.out.print(el);
		}
		System.out.println();
	}

}
