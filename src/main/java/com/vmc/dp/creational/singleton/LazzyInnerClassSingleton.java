package com.vmc.dp.creational.singleton;

public class LazzyInnerClassSingleton {

	private LazzyInnerClassSingleton() {
		// Reflection protection: if instance already exists, deny creation of another one
		if (SingletonHelper.instance != null) {
			throw new IllegalStateException("Singleton instance already exists. Use getInstance() instead.");
		}
	}

	private static class SingletonHelper {
		private static final LazzyInnerClassSingleton instance = new LazzyInnerClassSingleton();
	}

	public static LazzyInnerClassSingleton getInstance() {
		return SingletonHelper.instance;
	}

}
