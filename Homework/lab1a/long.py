from threading import Thread


def fun():
    for i in range(16):
        print(f"The worker thread: {i}\n", end="")


def main():
    t = Thread(target=fun)
    t.start()
    for i in range(16):
        print(f"The MAIN thread: {i}\n", end="")
    t.join()
    print("The worker thread has been finished.")


if __name__ == "__main__":
    main()

