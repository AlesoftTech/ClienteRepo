

import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;




public class ClienteTcp {
	private DataInputStream in;
	private DataOutputStream out;
	private InetAddress ip;
	private int port;
	private Socket socket;
	private static String path; 

	public ClienteTcp(InetAddress ip, int port, String pPath) throws NumberFormatException {             
		this.ip = ip;
		this.port = port;
		try {
			//Creamos el socket y los streams de input y output
			this.socket = new Socket(ip, port);       
			this.path = pPath; 
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());		
			receiveFile(in, out);
			in.close();
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException, NumberFormatException  {
		// Connect to local socket on port 4444
		ClienteTcp tcp = new ClienteTcp(InetAddress.getByName("localhost"),4446, path);
	}



	private static boolean receiveFile( DataInputStream in, DataOutputStream out) throws NumberFormatException  {
		String filename = null;
		try {		
			//Esta listo para recibir
			out.writeUTF("R"); 	
			
			//recibe el header del arcivo
			String longitud = in.readUTF();
			System.out.println("longitud: "+longitud);
			
			//Parsea el mensaje recibido para obtener el nombre y la longitud			

			//Crea un array de bytes con la longitud recibida para almacenar el archivo
			byte[] receivedData = new byte[Integer.valueOf(longitud)];

			//Crea un archivo con el filename recibido            
		    filename = in.readUTF();
			System.out.println("nombre: "+filename);
			
			String stringHashServer = in.readUTF();
            long hashServer = Long.parseLong(stringHashServer);
            System.out.println("hash code: "+hashServer);


			FileOutputStream fos = new FileOutputStream(path + "/"+  filename);

			//Carga la data recibida a traves del InputStream, en el FileOutputStream a traves de un while
			int count=0;           
			long size = Integer.valueOf(longitud);   

			while (size > 0 && (count = in.read(receivedData, 0, (int)Math.min(receivedData.length, size))) != -1)   
			{   
				fos.write(receivedData, 0, count);   
				size -= count;   
			}
			fos.close();
			File file = new File(path + "/"+  filename);
			
			long hashCliente = (long)file.length()/13;
            System.out.println("en el cliente el hash es:" +hashCliente);
			
			
			
			if(hashCliente ==  hashServer)
			{
				System.out.println("el archivo fue correctamente recibido.");			
				out.writeUTF("R"); 	
			}
			else			
			{
				System.out.println("el archivo no fue correctamente recibido.");				
				out.writeUTF("E"); 	
			}

			

			

		
			return true;

		} catch (IOException e) {
			((Throwable) e).printStackTrace();
			return false;
		}


	}
}
