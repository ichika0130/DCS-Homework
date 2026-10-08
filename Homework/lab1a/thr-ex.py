from threading import Thread


def fun():
    print("The worker thread is running...\n", end="")


def main():
    t = Thread(target=fun)
    t.start()
    print("The MAIN thread has launched the worker thread!\n", end="")
    t.join()
    print("The worker thread has been finished.")


if __name__ == "__main__":
    main()

