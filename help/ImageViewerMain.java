import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import info.clearthought.layout.TableLayout;

public class ImageViewerMain extends JFrame implements ActionListener, KeyListener, MouseListener{

	private static double fill = TableLayout.FILL;
	private static double preferred = TableLayout.PREFERRED;
	private static int border = 10;
	private JButton browse,nextBtn,prevBtn;
	private JTextField dirTF;
	private String dirPath;
	private JPanel imagePanel;
	private File dirFile;
	int image_index=0;
	 ArrayList<String> fileList;
	 private  JMenu fileMenu ;
	 JPanel upPanel,downPanel,singlePanel,doublePanel;
	public static void main(String[] args) {
				SwingUtilities.invokeLater(new Runnable() {
					 
		            @Override
		            public void run() {
		                new ImageViewerMain();
		            }
		        });
	}

	public ImageViewerMain() 
	  {
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setTitle("Image Viewer");
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    this.setLocationRelativeTo(null);
	    initUI();
	    addKeyListener(this);
	    System.out.println("GUI Elements loaded");
	    this.setVisible(true);
	  }
	private void initUI() {
		JPanel mainPanel = new JPanel();
		double size[][] = {{border,preferred,fill,preferred,border},{border*2,preferred,border*2,fill,border*2,preferred,border*2}};
		mainPanel.setLayout(new TableLayout(size));
		
		JPanel browsePanel = new JPanel();
		double browseSize[][] = {{preferred,border,250,border,preferred,border,preferred},{preferred}};
		browsePanel.setLayout(new TableLayout(browseSize));
		dirTF = new JTextField();
		browse = new JButton("Browse");
		browse.addActionListener(this);
		browsePanel.add(new JLabel("Directory Path : "),"0,0");
		browsePanel.add(dirTF, "2,0");
		browsePanel.add(browse, "4,0");
		
		imagePanel = new JPanel(new BorderLayout());
		imagePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f)));
		
		nextBtn = new JButton("Next");
		prevBtn = new JButton("Previous");
		prevBtn.addActionListener(this);
		nextBtn.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(prevBtn);
		buttonPanel.add(nextBtn);
		
		
		 //create a menu bar
	      final JMenuBar menuBar = new JMenuBar();

	      //create menus
	      fileMenu = new JMenu("Change Image");
	     
	      //create menu items
	      JMenuItem nextMenuItem = new JMenuItem("Next Image");
	      nextMenuItem.setActionCommand("Next");

	      JMenuItem prevMenuItem = new JMenuItem("Previous Image");
	      prevMenuItem.setActionCommand("Prev");
	      
	      MenuItemListener menuItemListener = new MenuItemListener();

	      nextMenuItem.addActionListener(menuItemListener);
	      prevMenuItem.addActionListener(menuItemListener);
	      
	    //add menu items to menus
	      fileMenu.add(nextMenuItem);
	      fileMenu.add(prevMenuItem);
	      
	      menuBar.add(fileMenu);
	      fileMenu.setEnabled(false);
	      this.setJMenuBar(menuBar);
	      
	      upPanel = new JPanel();
	      downPanel = new JPanel();
	      JPanel mouseHoverPanel = new JPanel();
	      double mouseSize[][] = {{preferred},{fill,fill}};
	      mouseHoverPanel.setLayout(new TableLayout(mouseSize));
	      
	      mouseHoverPanel.add(upPanel,"0,0");
	      mouseHoverPanel.add(downPanel,"0,1");
	      upPanel.setLayout(new BorderLayout());
	      upPanel.addMouseListener(this);
	      upPanel.add(new JLabel("<html><body>Mouse Hover<br>For<br><br>Previous<br>Image</body></html>"),BorderLayout.CENTER);
	      downPanel.setLayout(new BorderLayout());
	      downPanel.add(new JLabel("<html><body>Mouse Hover<br>For<br><br>Next<br>Image</body></html>"),BorderLayout.CENTER);
	      downPanel.addMouseListener(this);
	      upPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
	      downPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
	      
	      
	      singlePanel = new JPanel();
	      doublePanel = new JPanel();
	      JPanel clickPanel = new JPanel();
	      double clickSize[][] = {{preferred},{fill,fill}};
	      clickPanel.setLayout(new TableLayout(clickSize));
	      
	      clickPanel.add(singlePanel,"0,0");
	      clickPanel.add(doublePanel,"0,1");
	      singlePanel.setLayout(new BorderLayout());
	      singlePanel.addMouseListener(this);
	      singlePanel.add(new JLabel("<html><body>Single Click<br>For<br><br>Previous<br>Image</body></html>"),BorderLayout.CENTER);
	      doublePanel.setLayout(new BorderLayout());
	      doublePanel.add(new JLabel("<html><body>Double Click<br>For<br><br>Next<br>Image</body></html>"),BorderLayout.CENTER);
	      doublePanel.addMouseListener(this);
	      singlePanel.setBorder(BorderFactory.createLineBorder(Color.RED));
	      doublePanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
	      
	      mainPanel.add(clickPanel,"1,3");
	      mainPanel.add(browsePanel,"1,1,3,1");
	      mainPanel.add(imagePanel, "2,3");
		mainPanel.add(buttonPanel, "2,5");
		mainPanel.add(mouseHoverPanel, "3,3");
		
		mainPanel.setVisible(true);
		add(mainPanel);
	}

	void loadImage()
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		 File[] directoryListing = dirFile.listFiles();
		  fileList = new ArrayList<>();
		for (File child : directoryListing) {
		      // Do something with child
			try {
			    Image image = ImageIO.read(child);
			    if (image == null) {
			    	continue;
			    }
			    fileList.add(child.getAbsolutePath());
			} catch(IOException ex) {
			   continue;
			}
			
		   }
		fileMenu.setEnabled(true);
		//show first image
		displayImage();
		/*for(int i=0;i<fileList.size();i++)
		{
			String filePath = fileList.get(i);
			ImageIcon imageicon = new ImageIcon(filePath);
		    JLabel label = new JLabel("", imageicon, JLabel.CENTER);
		    imagePanel.add( label, BorderLayout.CENTER );
		    repaint();
		    revalidate();
		}*/
		setCursor(Cursor.getDefaultCursor());
		
	}
	
	void displayNextImage()
	{
		if(image_index == fileList.size())
			return;
			image_index++;
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			String filePath = fileList.get(image_index);
			ImageIcon imageicon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_DEFAULT));
		    JLabel label = new JLabel("", imageicon, JLabel.CENTER);
		    imagePanel.removeAll();
		    imagePanel.add( label, BorderLayout.CENTER );
		    repaint();
		    revalidate();
		    imagePanel.repaint();
		    imagePanel.revalidate();
		    this.setFocusable(true);
		    this.requestFocus();
		    setCursor(Cursor.getDefaultCursor());
	}
	void displayPrevImage()
	{
		if(image_index == 0)
			return;
			image_index--;
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			String filePath = fileList.get(image_index);
			ImageIcon imageicon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_DEFAULT));
		    JLabel label = new JLabel("", imageicon, JLabel.CENTER);
		    imagePanel.removeAll();
		    imagePanel.add( label, BorderLayout.CENTER );
		    repaint();
		    revalidate();
		    imagePanel.repaint();
		    imagePanel.revalidate();
		    this.setFocusable(true);
		    this.requestFocus();
		    setCursor(Cursor.getDefaultCursor());
	}
	void displayImage()
	{
			String filePath = fileList.get(image_index);
			ImageIcon imageicon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_DEFAULT));
		    JLabel label = new JLabel("", imageicon, JLabel.CENTER);
		    imagePanel.removeAll();
		    imagePanel.add( label, BorderLayout.CENTER );
		    repaint();
		    revalidate();
		    imagePanel.repaint();
		    imagePanel.revalidate();
		    this.setFocusable(true);
		    this.requestFocus();
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if(ae.getSource() == browse)
		{
			JFileChooser chooser = new JFileChooser();
 			chooser = new JFileChooser(); 
 		    chooser.setCurrentDirectory(new java.io.File("."));
 		    chooser.setDialogTitle("Select Image Directory");
 		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
 		    // disable the "All files" option.
 		    chooser.setAcceptAllFileFilterUsed(false);
 		    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
 		    { 
 		    	dirFile = chooser.getSelectedFile(); 
 		    }
 		    
 	      if (dirFile == null)
 	        return;
 	      final File dir = dirFile;
 	      if(!dir.isDirectory())
 	      {
 	    	 JOptionPane.showMessageDialog(this, "Not a Directory", "Error", JOptionPane.ERROR_MESSAGE);
				return;
 	      }
 	      dirTF.setText(dirFile.getAbsolutePath());
 	      loadImage();
		}
		else if(ae.getSource() == nextBtn)
			displayNextImage();
		else if(ae.getSource() == prevBtn)
			displayPrevImage();
		
		this.setFocusable(true);
	    this.requestFocus();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		char c = e.getKeyChar();
        if(c == 'n' || c == 'N')
        	displayNextImage();
        else if(c == 'p' || c == 'P')
        	displayPrevImage();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
		
	class MenuItemListener implements ActionListener {
	      public void actionPerformed(ActionEvent e) {            
	        if(e.getActionCommand().equals("Next"))
	        	displayNextImage();
	        else if(e.getActionCommand().equals("Prev"))
	        	displayPrevImage();
	      }    
	   }

	@Override
	public void mouseClicked(MouseEvent me) {
		// TODO Auto-generated method stub
		if(me.getSource() == singlePanel && me.getClickCount()!=2)
			displayPrevImage();
		else if(me.getSource() == doublePanel && me.getClickCount()==2)
			displayNextImage();
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub
		if(me.getSource() == upPanel)
			displayPrevImage();
		else if(me.getSource() == downPanel)
			displayNextImage();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
