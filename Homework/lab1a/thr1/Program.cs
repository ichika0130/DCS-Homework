using System;
using System.Threading;


class Example {
	static void Fun() {
		Console.WriteLine("The worker thread is running...");
	}

	static void Main() {
		Thread t = new Thread(Fun);
		t.Start();
		Console.WriteLine("The MAIN thread has launched the worker thread!");
		t.Join();
		Console.WriteLine("The worker thread has been finished.");

	}
}

