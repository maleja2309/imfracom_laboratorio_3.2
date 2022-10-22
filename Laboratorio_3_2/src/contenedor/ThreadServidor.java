package contenedor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ThreadServidor extends Thread
{
    DatagramSocket socket;
    int numeroThreads;
    String option;

    public ThreadServidor(DatagramSocket pSocket, int pNumeroThreads, String Option)
    {
        socket = pSocket;
        numeroThreads = pNumeroThreads;
        option = Option;
    }
    
    public void proceso()
    {
	    try 
		{
            ProtocoloServidor protocol = new ProtocoloServidor(socket, option);

			protocol.procesar();			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
    }

}