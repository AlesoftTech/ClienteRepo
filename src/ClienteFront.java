import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.Timer;

public class ClienteFront extends JPanel
implements ActionListener{

	 JButton go;
	
	 ClienteTcp c; 
	   
	   JFileChooser chooser;
	   String choosertitle;
	   
	  public ClienteFront() {
	    go = new JButton("Seleccionar directorio");
	    go.addActionListener(this);
	    
	    JLabel lblNewLabel = new JLabel("Seleccione el directorio donde desea guardar el archivo.");
	    add(lblNewLabel);
	    add(go);
	   }

	  public void actionPerformed(ActionEvent e) {
	    int result;
	        
	    chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle(choosertitle);
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
	      System.out.println("getCurrentDirectory(): " 
	         +  chooser.getCurrentDirectory());
	      System.out.println("getSelectedFile() : " 
	         +  chooser.getSelectedFile());
	      String path = chooser.getSelectedFile().getAbsolutePath().toString();
	     
	      try {
			c = new ClienteTcp(InetAddress.getByName("3.88.127.147"),4446, path );
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      }
	    else {
	      System.out.println("No Selection ");
	      }
	     }
	   
	  public Dimension getPreferredSize(){
	    return new Dimension(400, 200);
	    }
	    
	  public static void main(String s[]) {
	    JFrame frame = new JFrame("");
	    ClienteFront panel = new ClienteFront();
	    frame.addWindowListener(
	      new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          System.exit(0);
	          }
	        }
	      );
	    frame.getContentPane().add(panel,"Center");
	    frame.setSize(panel.getPreferredSize());
	    frame.setVisible(true);
	    }
}
