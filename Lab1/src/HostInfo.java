package firstnetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Distributed Computer Systems - Laboratory 1, tasks 2-5.
 *
 * Usage:  java firstnetwork.HostInfo <hostname or IPv4 address> [timeout ms]
 * e.g.    java firstnetwork.HostInfo google.com
 *         java firstnetwork.HostInfo 8.8.8.8 2000
 */
public class HostInfo {

    public static void main(String[] args) {
        // Task 2: the host to connect to is taken from the execution arguments.
        if (args.length < 1) {
            System.out.println("Usage: java firstnetwork.HostInfo <hostname or IPv4 address> [timeout ms]");
            return;
        }
        String target = args[0];
        int timeout = (args.length > 1) ? Integer.parseInt(args[1]) : 2000;

        try {
            InetAddress node;
            byte[] ip = parseIPv4(target);

            if (ip != null) {
                // Task 5: the argument is an IP address, so the InetAddress object is
                // built directly from its 4 bytes - no DNS lookup is done here.
                node = InetAddress.getByAddress(ip);
                System.out.println("Created from IP bytes using getByAddress()");
            } else {
                // The argument is a name, so it has to be resolved through DNS / hosts file.
                node = InetAddress.getByName(target);
                System.out.println("Resolved from name using getByName()");
            }

            printInfo(node);

            // Task 3: check whether the remote host can be reached.
            checkReachable(node, timeout);

            if (ip == null) {
                // Task 5: connect to the same host again, this time by IP address only.
                // getAddress() returns the raw bytes, getByAddress() builds a new object
                // from them, which carries no host name.
                InetAddress byIp = InetAddress.getByAddress(node.getAddress());
                System.out.println();
                System.out.println("Connecting again by IP address only (getByAddress): "
                        + byIp.getHostAddress());
                checkReachable(byIp, timeout);
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        }
    }

    /** Task 4: canonical host name, hash code of the IP address, multicast check. */
    static void printInfo(InetAddress node) {
        System.out.println("IP address:          " + node.getHostAddress());
        System.out.println("Host name:           " + node.getHostName());
        System.out.println("Canonical host name: " + node.getCanonicalHostName());
        // For IPv4 the hash code is simply the 32-bit address stored as an int.
        System.out.println("Hash code:           " + node.hashCode()
                + " (hex 0x" + Integer.toHexString(node.hashCode()) + ")");
        System.out.println("Multicast address:   " + node.isMulticastAddress());
        System.out.println("Loopback address:    " + node.isLoopbackAddress());
    }

    /** Task 3: try to reach the host, waiting at most timeout milliseconds. */
    static void checkReachable(InetAddress node, int timeout) {
        try {
            if (node.isReachable(timeout)) {
                System.out.println("The host " + node.getHostAddress() + " is alive!");
            } else {
                System.out.println("The host " + node.getHostAddress()
                        + " did not respond within " + timeout + " ms.");
            }
        } catch (IOException e) {
            System.out.println("Network error while checking " + node.getHostAddress()
                    + ": " + e.getMessage());
        }
    }

    /**
     * Converts a dotted IPv4 string such as "192.168.1.10" into 4 bytes.
     * Returns null if the text is not a valid IPv4 address (then it is treated as a name).
     */
    static byte[] parseIPv4(String text) {
        String[] parts = text.split("\\.");
        if (parts.length != 4) {
            return null;
        }
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            try {
                int value = Integer.parseInt(parts[i]);
                if (value < 0 || value > 255) {
                    return null;
                }
                bytes[i] = (byte) value;   // 200 becomes -56 as a Java byte, which is fine
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return bytes;
    }
}
