package com.leo.netty.nio;

import java.util.stream.Stream;

public class StreamDemo {
	public static void main(String[] args) {
		String demo = "My name is leo";
 	
		Stream.of(demo.split(" ")).filter(s -> s.length() > 2).
				map(String::length).peek(System.out::println).forEach(System.out::println);
	}
}
