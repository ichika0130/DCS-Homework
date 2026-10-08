using System;
using System.Threading;


class RaceCond {
	static long x = 0;

	static void Fun() {
		for (int i = 0; i < 1_000_000; ++i) {
			++x;
		}
	}

	static void Main() {
		int num_threads = 32;
		Thread[] threads = new Thread[num_threads];
		for (int i = 0; i < num_threads; ++i) {
			threads[i] = new Thread(Fun);
		}
		foreach(var t in threads) t.Start();
		foreach(var t in threads) t.Join();
		Console.WriteLine($"x = {x}");
	}
}

