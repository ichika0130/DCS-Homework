using System;
using System.Threading;


class Example {
	static void Fun() {
		for (int i = 0; i < 16; ++i) {
			Console.WriteLine($"The worker thread: {i}.");
		}
	}

	static void Main() {
		Thread t = new Thread(Fun);
		t.Start();
		for (int i = 0; i < 16; ++i) {
			Console.WriteLine($"The MAIN thread: {i}.");
		}
		t.Join();
		Console.WriteLine("The worker thread has been finished.");

	}
}

