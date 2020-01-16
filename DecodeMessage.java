
//DecodeMessage.java
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class DecodeMessage extends JFrame implements ActionListener {
	JButton open = new JButton("Open"), decode = new JButton("Decode");
	JTextArea message = new JTextArea(10, 3);
	BufferedImage image = null;
	JScrollPane imagePane = new JScrollPane();
	private final JButton btnEncode = new JButton("Encode");

	public DecodeMessage() {
		super("Decode stegonographic message in image");
		btnEncode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				EmbedMessage em = new EmbedMessage();
				em.setVisible(true);
			}
		});
		btnEncode.setMnemonic('D');
		assembleInterface();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		this.setVisible(true);
	}

	private void assembleInterface() {
		JPanel p = new JPanel();

		this.getContentPane().add(p, BorderLayout.NORTH);
		open.addActionListener(this);
		decode.addActionListener(this);

		open.setMnemonic('O');
		decode.setMnemonic('D');

		JButton btnNewButton = new JButton("Main Menu");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Encrypt en = new Encrypt();
				en.setVisible(true);
			}
		});
		GroupLayout gl_p = new GroupLayout(p);
		gl_p.setHorizontalGroup(gl_p.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_p.createSequentialGroup().addGap(436).addComponent(open).addGap(18).addComponent(decode)
						.addGap(18).addComponent(btnEncode, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
						.addGap(524)));
		gl_p.setVerticalGroup(gl_p.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_p.createSequentialGroup().addGap(5).addGroup(gl_p.createParallelGroup(Alignment.BASELINE)
						.addComponent(open).addComponent(decode).addComponent(btnEncode).addComponent(btnNewButton))));
		p.setLayout(gl_p);

		p = new JPanel(new GridLayout(1, 1));
		p.add(new JScrollPane(message));
		message.setFont(new Font("Arial", Font.BOLD, 20));
		p.setBorder(BorderFactory.createTitledBorder("Decoded message"));
		message.setEditable(false);
		this.getContentPane().add(p, BorderLayout.SOUTH);

		imagePane.setBorder(BorderFactory.createTitledBorder("Steganographed Image"));
		this.getContentPane().add(imagePane, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent ae) {
		Object o = ae.getSource();
		if (o == open)
			openImage();
		else if (o == decode)
			decodeMessage();

	}

	private java.io.File showFileDialog(boolean open) {
		JFileChooser fc = new JFileChooser("Open an image");
		javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File f) {
				String name = f.getName().toLowerCase();
				return f.isDirectory() || name.endsWith(".png") || name.endsWith(".bmp");
			}

			public String getDescription() {
				return "Image (*.png, *.bmp)";
			}
		};
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(ff);

		java.io.File f = null;
		if (open && fc.showOpenDialog(this) == fc.APPROVE_OPTION)
			f = fc.getSelectedFile();
		else if (!open && fc.showSaveDialog(this) == fc.APPROVE_OPTION)
			f = fc.getSelectedFile();
		return f;
	}

	private void openImage() {
		java.io.File f = showFileDialog(true);
		try {
			image = ImageIO.read(f);
			JLabel l = new JLabel(new ImageIcon(image));
			imagePane.setViewportView(l);
			this.validate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void decodeMessage() {
		int len = extractInteger(image, 0, 0);
		byte b[] = new byte[len];
		for (int i = 0; i < len; i++)
			b[i] = extractByte(image, i * 8 + 32, 0);
		message.setText(new String(b));
	}

	private int extractInteger(BufferedImage img, int start, int storageBit) {
		int maxX = img.getWidth(), maxY = img.getHeight(), startX = start / maxY, startY = start - startX * maxY,
				count = 0;
		int length = 0;
		for (int i = startX; i < maxX && count < 32; i++) {
			for (int j = startY; j < maxY && count < 32; j++) {
				int rgb = img.getRGB(i, j), bit = getBitValue(rgb, storageBit);
				length = setBitValue(length, count, bit);
				count++;
			}
		}
		return length;
	}

	private byte extractByte(BufferedImage img, int start, int storageBit) {
		int maxX = img.getWidth(), maxY = img.getHeight(), startX = start / maxY, startY = start - startX * maxY,
				count = 0;
		byte b = 0;
		for (int i = startX; i < maxX && count < 8; i++) {
			for (int j = startY; j < maxY && count < 8; j++) {
				int rgb = img.getRGB(i, j), bit = getBitValue(rgb, storageBit);
				b = (byte) setBitValue(b, count, bit);
				count++;
			}
		}
		return b;
	}

	private int getBitValue(int n, int location) {
		int v = n & (int) Math.round(Math.pow(2, location));
		return v == 0 ? 0 : 1;
	}

	private int setBitValue(int n, int location, int bit) {
		int toggle = (int) Math.pow(2, location), bv = getBitValue(n, location);
		if (bv == bit)
			return n;
		if (bv == 0 && bit == 1)
			n |= toggle;
		else if (bv == 1 && bit == 0)
			n ^= toggle;
		return n;
	}

	public static void main(String arg[]) {
		new DecodeMessage();
	}
}