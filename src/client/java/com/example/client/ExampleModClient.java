package com.example.client;

import net.fabricmc.api.ClientModInitializer;

public class ExampleModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
System.out.println("Simple Macro client loaded!");
	}
}