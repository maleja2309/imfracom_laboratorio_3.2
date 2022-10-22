package contenedor;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Scanner;

public class Servidor
{
    public static final int PUERTO = 3400;
    public static void main(String args[]) throws IOException
	{
		DatagramSocket socket = null;
		boolean continuar = true;
		int numeroThreads = 25;
		String option = null;

		System.out.println("Main Server ...");
		System.out.println("Seleccione una de las opciones para el envio del archivo: ");
		System.out.println("1. 100 MB ");
		System.out.println("2. 250 MB ");

		try
		{
			// Espacio para seleccionar el archivo que se le envia a los clientes
			Scanner scanner = new Scanner(System.in);	
			option = scanner.nextLine();
			System.out.println("");
			System.out.println("Servidor escuchando ... ");
			socket = new DatagramSocket(PUERTO);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		while (continuar)
		{
			// Crear el thread con el socket y el id 
			ThreadServidor thread = new ThreadServidor(socket, numeroThreads, option);
			thread.proceso();
			
			thread.start();

			numeroThreads++;
		}
		socket.close();	
	}    
}
