import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.io.*;
import javax.swing.SwingConstants;


public class eform extends JFrame implements ActionListener {

	private JPanel contentPane;
	JButton btnEncrypt = new JButton("Encrypt");
	JTextArea txtrTextToBe = new JTextArea();
	JTextArea txtrEncryptedText = new JTextArea();
	
	String encryptedString;
	private static SecretKeySpec secretKey;
    private static byte[] key;
    
    
    
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					eform frame = new eform();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public eform() {
		super("Encryption");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		
		JButton btnNewButton = new JButton("Main Menu");
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				Encrypt en=new Encrypt();
				en.setVisible(true);
				
			}
			
		});
	    
	   JButton btnNewButton_1 = new JButton("Decrypt");
	   btnNewButton_1.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			dispose();
			deform de=new deform();
			de.setVisible(true);
		}
	} );
	   
		
		
				
		contentPane.add(btnEncrypt);
		btnEncrypt.addActionListener(this);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
							.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
						.addComponent(txtrTextToBe, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
					.addGap(18))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtrEncryptedText, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
					.addGap(18))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(157)
					.addComponent(btnEncrypt, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
					.addGap(154))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(7)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(txtrTextToBe, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnEncrypt, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(txtrEncryptedText, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
					.addGap(12))
		);
		contentPane.setLayout(gl_contentPane);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		 Object o = ae.getSource();
		    if(o == btnEncrypt)
		       passData();
	}

	private void passData() {
		// TODO Auto-generated method stub
		String mess = txtrTextToBe.getText();
		final String secretKey = "pratiti";
		encryptedString=eform.encrypt(mess, secretKey);
		txtrEncryptedText.setText(encryptedString);
		txtrEncryptedText.setEditable(false);
		
		try {
	    	FileWriter writer= new FileWriter("C:\\Users\\HP\\Desktop\\encrypt.txt",true);
	    	BufferedWriter bufferwrite=new BufferedWriter(writer);
	    	bufferwrite.write(mess);
	    	bufferwrite.newLine();
	    	bufferwrite.write(encryptedString);
	    	bufferwrite.newLine();
	    	bufferwrite.write("-------------------------------------------");
	    	bufferwrite.newLine();
	    	bufferwrite.close();
	    } catch(IOException e){
	    	txtrEncryptedText.setText("could not enter data to the file");
	    }
		
	}
	public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
	 
	

	
}
