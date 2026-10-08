/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package firstnetwork;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author pbils
 */
public class FirstNetwork {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*if (args.length > 0)
            System.out.println(args[0]);
    }*/
        InetAddress node, remotenode;
        byte[] ipaddr;
        try {
		node = InetAddress.getLocalHost();
                System.out.println("Local address: " + node.getHostAddress());
                System.out.println("Local hostname: " + node.getHostName());
		if (node.isLoopbackAddress()) {
                    System.out.println("This is the loopback address");
		}
		node = InetAddress.getByName("localhost");
		System.out.println("Remote address: " + node.getHostAddress());
                System.out.println("remote hostname: " + node.getHostName());
		if (node.isLoopbackAddress()) {
                    System.out.println("This is the loopback address");
		}
                node = InetAddress.getByName("192.168.1.1");//("192.168.113.43");
                try {
                    // We check if the host is alive, waiting for the response 1 sec.
                    if (node.isReachable(1000))
                    {
                        System.out.println("The host " + node.getHostAddress() +  " is alive!");
                    }
                    else
                    {
                        System.out.println("The host " + node.getHostAddress() + " is dead!");
                    }
                }
                catch (Exception e)
                {
                            
                }
		node = InetAddress.getLocalHost();
                ipaddr = node.getAddress();
                for (int i=1; i<255;i++)
                {
                    ipaddr[3] = (byte)i;
                    remotenode = InetAddress.getByAddress(ipaddr);
                    try {
                        if (remotenode.isReachable(10))
                            System.out.println(remotenode.getHostAddress());
                    }
                    catch (Exception e)
                    {
                        
                    }
                }
	}
	catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }
}