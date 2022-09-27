package imagebrowser.view;

import imagebrowser.controller.ImageBrowserController;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.Rectangle;
import java.awt.Dimension;

import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JFrameImageBrowser extends JFrame {

	ImageBrowserController ibController = new ImageBrowserController();
	private JPanel contentPane;
	private JLabel labelImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameImageBrowser frame = new JFrameImageBrowser();
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
	public JFrameImageBrowser() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 607, 498);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnActions = new JMenu("Actions");
		menuBar.add(mnActions);
		mnActions.setEnabled(true);
		
		JMenuItem mntmPrevious = new JMenuItem("Previous");
		mnActions.add(mntmPrevious);
		
		JMenuItem mntmNext = new JMenuItem("Next");
		mnActions.add(mntmNext);
		
		mntmPrevious.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				moveToPreviousImage();

			}
		});
		
		mntmNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				moveToNextImage();

			}
		});

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JButton buttonPrevious = new JButton("<");
		buttonPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				moveToPreviousImage();
			}
		});
		contentPane.add(buttonPrevious, BorderLayout.WEST);

		JButton buttonNext = new JButton(">");
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveToNextImage();
			}
		});
		contentPane.add(buttonNext, BorderLayout.EAST);

		labelImage = new JLabel("");
		contentPane.add(labelImage, BorderLayout.CENTER);
		labelImage.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					moveToPreviousImage();
				} else if (SwingUtilities.isRightMouseButton(e)) {
					moveToNextImage();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(100, 80));
		panel.setBounds(new Rectangle(0, 0, 100, 50));
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(null);

		JLabel lblDisplay = new JLabel("");
		lblDisplay.setBounds(10, 55, 529, 14);
		panel.add(lblDisplay);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select Image Directory");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				// disable the "All files" option.
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File dirFile = chooser.getSelectedFile();
					if (dirFile != null) {
						ibController.loadImage(dirFile);
						lblDisplay.setText(dirFile.getAbsolutePath());
						displayImage();
					}
				}
			}
		});
		btnBrowse.setBounds(10, 11, 89, 23);
		panel.add(btnBrowse);

		JButton btnSlideShow = new JButton("Slide show");
		btnSlideShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//while (FolderImages.getIndex() < FolderImages.getImageFiles().size() - 1) {
						while (moveToNextImage()) {							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {

							}
						}
					}
				}).start();
			}
		});
		btnSlideShow.setBounds(106, 11, 100, 23);
		panel.add(btnSlideShow);

		JComboBox comboBox = new JComboBox();
		comboBox.addItem("Previous");
		comboBox.addItem("Next");
		comboBox.setBounds(288, 12, 100, 20);
		panel.add(comboBox);

		JButton btnGo = new JButton("Go");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getSelectedIndex() == 0) {
					moveToPreviousImage();
				} else if (comboBox.getSelectedIndex() == 1) {
					moveToNextImage();
				}
			}
		});
		btnGo.setBounds(398, 11, 55, 23);
		panel.add(btnGo);
	}

	private boolean moveToNextImage() {
		boolean ret=ibController.moveToNextImage();
		displayImage();
		return ret;
	}

	private void moveToPreviousImage() {
		ibController.moveToPreviousImage();
		displayImage();
	}

	private void displayImage() {
		try {
			File imgFile = new File(ibController.getCurrentImage());
			BufferedImage image = ImageIO.read(imgFile);
			ImageIcon ii = new ImageIcon(resizeImage(image,
					labelImage.getWidth(), labelImage.getHeight()));
			labelImage.setIcon(ii);
			labelImage.setHorizontalAlignment(JLabel.CENTER);
			labelImage.setVerticalAlignment(JLabel.CENTER);
		} catch (Exception e) {

		}
	}

	public static BufferedImage resizeImage(BufferedImage image, int width,
			int height) {
		int scaledWidth = width;
		int scaledHeight = height;
		double iWidth = image.getWidth();
		double iHeight = image.getHeight();

		if (width >= height) {
			scaledHeight = height;
			scaledWidth = (int) ((height / iHeight) * iWidth);
		} else {
			scaledWidth = width;
			scaledHeight = (int) ((width / iWidth) * iHeight);
		}

		BufferedImage bi = new BufferedImage(scaledWidth, scaledHeight,
				BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();
		return bi;
	}

}
