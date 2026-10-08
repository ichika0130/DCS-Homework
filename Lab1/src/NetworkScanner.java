package firstnetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Distributed Computer Systems - Laboratory 1, task 6.
 * Checks every host address inside the given network and prints the ones that respond.
 *
 * Usage:  java firstnetwork.NetworkScanner <network>/<prefix> [timeout ms]
 * e.g.    java firstnetwork.NetworkScanner 192.168.1.0/24
 *         java firstnetwork.NetworkScanner 192.168.1.0/28 300
 */
public class NetworkScanner {

    private static final int MAX_HOSTS = 65536;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java firstnetwork.NetworkScanner <network>/<prefix> [timeout ms]");
            System.out.println("Example: java firstnetwork.NetworkScanner 192.168.1.0/24");
            return;
        }

        // Split "192.168.1.0/24" into the address part and the prefix length.
        String[] parts = args[0].split("/");
        byte[] base = HostInfo.parseIPv4(parts[0]);
        if (base == null) {
            System.out.println("Not a valid IPv4 address: " + parts[0]);
            return;
        }
        int prefix = (parts.length > 1) ? Integer.parseInt(parts[1]) : 24;
        if (prefix < 0 || prefix > 32) {
            System.out.println("Prefix must be between 0 and 32.");
            return;
        }
        int timeout = (args.length > 1) ? Integer.parseInt(args[1]) : 100;

        // Work on the address as one 32-bit number (kept in a long to avoid sign problems).
        long address = toLong(base);
        long mask = (prefix == 0) ? 0 : (0xFFFFFFFFL << (32 - prefix)) & 0xFFFFFFFFL;
        long network = address & mask;                    // all host bits set to 0
        long broadcast = network | (~mask & 0xFFFFFFFFL); // all host bits set to 1

        // The network and broadcast addresses are not real hosts, so skip them
        // (except in /31 and /32 networks, where there are no such addresses).
        long first = (prefix <= 30) ? network + 1 : network;
        long last = (prefix <= 30) ? broadcast - 1 : broadcast;
        long count = last - first + 1;

        if (count > MAX_HOSTS) {
            System.out.println("Range too large (" + count + " addresses). Use a prefix of /16 or longer.");
            return;
        }

        System.out.println("Network:   " + toText(network) + "/" + prefix);
        System.out.println("Broadcast: " + toText(broadcast));
        System.out.println("Scanning " + toText(first) + " - " + toText(last)
                + " (" + count + " addresses, timeout " + timeout + " ms)...");

        int alive = 0;
        long start = System.currentTimeMillis();

        for (long ip = first; ip <= last; ip++) {
            try {
                InetAddress host = InetAddress.getByAddress(toBytes(ip));
                if (host.isReachable(timeout)) {
                    System.out.println("  alive: " + host.getHostAddress());
                    alive++;
                }
            } catch (UnknownHostException e) {
                // cannot happen for a 4-byte array, but the method declares it
            } catch (IOException e) {
                // network error for this address - treat it as not reachable
            }
        }

        long seconds = (System.currentTimeMillis() - start) / 1000;
        System.out.println("Done: " + alive + " of " + count + " hosts responded (" + seconds + " s).");
    }

    /** 4 bytes -> one unsigned 32-bit number, e.g. 192.168.1.10 -> 3232235786. */
    static long toLong(byte[] b) {
        return ((b[0] & 0xFFL) << 24) | ((b[1] & 0xFFL) << 16) | ((b[2] & 0xFFL) << 8) | (b[3] & 0xFFL);
    }

    /** One 32-bit number -> 4 bytes, the format getByAddress() expects. */
    static byte[] toBytes(long ip) {
        return new byte[] {
                (byte) (ip >> 24), (byte) (ip >> 16), (byte) (ip >> 8), (byte) ip
        };
    }

    static String toText(long ip) {
        return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
    }
}
