using System;
using System.Threading;


class Synchr {
	static long x = 0;
	static object lck_x = new object();

	static void Fun() {
		for (int i = 0; i < 1_000_000; ++i) {
			lock(lck_x) {
				++x;
			}
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

