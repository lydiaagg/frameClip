import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Frame {

	  public class Pan extends JPanel implements MouseMotionListener, MouseListener, Observer
	  {
		  boolean drawing = false;
		  boolean loaded = false;
		  boolean startClipping = false;
		  boolean saving = false;
		  
		 // nom du fichier a save 
		  int index = 0;
		  
		  BufferedImage img = null;
		  animatedObject a = new animatedObject(1);
		  ArrayList <animatedObject> listAnimation = new ArrayList();
				  
		  
		  GeneralPath gp = new GeneralPath();
		  public Pan()
		  {
			  this.addMouseMotionListener(this);
			  this.addMouseListener(this);
			  this.setSize(2000, 2000);
			  a.addObserver(this);
			  
		  }
		  public void  paintComponent(Graphics g)
		  {
			 super.paintComponent(g);
			 if(!loaded)
			 {
				 loaded = true;
				try 
				{
					img = ImageIO.read(new File("c:/images/Capture1.png"));
					
				} catch (IOException e) {}
				
			 }
			 
			 if(startClipping)
			 {
				 g.setClip(gp);
			 }
			 
			 if(saving)
			 {
				// saving = false pour eviter de resauvegarder a chaque fois
			    saving = false;
			    //créer l'image a save
			    BufferedImage imgSave = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
			    Graphics gSave = imgSave.getGraphics();
			    paint(gSave);
			    
			    try {
			    	// save l'image
					ImageIO.write(imgSave.getSubimage(gp.getBounds().x,gp.getBounds().y, gp.getBounds().width, gp.getBounds().height),
							       "PNG" ,
							       new File("c:/images/"+index++ +".png"));
					// ouvrir le fichier
					Desktop.getDesktop().open(new File("c:/images"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
				}
			    
			   // reinitialiser le gp 
			    gp = new GeneralPath();
			    startClipping = false;
				repaint();
			    
				   
			 }
			 
			 // afficher l'image a crop
			 g.drawImage(img, 0, 0, null);
			 
			 // afficher l'image de l'aimtion
			 g.drawImage(a.img, 0, 0, null);
			 
			 // afficher le graphic path pour savoir quoi decouper
			 if(drawing)
			 {
				 g.setColor(Color.black);
				 ((Graphics2D)g).draw(gp);
			 }
		  }
		@Override
		public void mouseDragged(MouseEvent e)
		{
			if(!drawing)
			{
				drawing = true;
				startClipping = false;
				gp.moveTo(e.getX(), e.getY());
			}
			else
			{
			    gp.lineTo(e.getX(), e.getY());
			}
			
			repaint();
			
		}
		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == e.BUTTON3)
			{
				a.startAnimation();
			}
			
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
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
		public void mouseReleased(MouseEvent e) {
			
			if(e.getButton() == e.BUTTON1)
			{
				drawing = false;
				startClipping = true;
				saving = true;
				repaint();
			}
		}
		@Override
		public void update(Observable arg0, Object arg1) {
			// TODO Auto-generated method stub
			repaint();
		}
		  
	  }
	  
	  public class animatedObject extends Observable implements Runnable 
	  {

		  boolean stop = false;
		  BufferedImage img = null;
		  int speed = 1000;
		  int type = 1;
	      File fileToRead = new File("c:/images");	
	      
	      int x = 0;
	      int y = 0;
	      
	      Thread t = new Thread(this);
		
		public animatedObject(int type)
		{
			this.type = type;
			
			
		}
		
		public void startAnimation()
		{
			t.start();
		}
		public void run() {
			while(!stop)
			{
				for(File f:fileToRead.listFiles())
				{
					try 
					{
						img = ImageIO.read(f);
						// modifier x et y ou il sera collé
						// verifier si il y'a collision avec un autre character
						  // si il ya une collision decider du nouveau fichier a ouvrir  type = 2
						
						
						this.setChanged();
						this.notifyObservers();
						
					    Thread.sleep(speed);	
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}
		  
	  }
	  public Frame()
	  {
		  JFrame f = new JFrame();
		  Pan p  = new Pan();
		  JScrollPane scroll = new JScrollPane(p,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		  
		  
		  f.setVisible(true);
		  f.setSize(500, 500);
		  
		  f.setLayout(null);
		 // f.add(p);
		  f.add(scroll);
			
		 // p.setBounds(new Rectangle(5,5,400,400));
		  p.setSize(2000, 2000);
		  p.setLayout(new BorderLayout());
		  scroll.setBounds(new Rectangle(5,5,400,400));
		  p.setBackground(Color.white);
		  
		   
		  
	  }	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
          Frame f  = new Frame();
	}

}

;