package application; 

import java.awt.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;



public class DisplayPanel extends JPanel 
{

    Image background = null;
    public DisplayPanel()
    {
      try
      {
          background = ImageIO.read(new File(System.getProperty("user.dir") + "/Resources" + "/ms.png"));
      }catch(Exception ex) {}
      setLayout(null);
      setBackground(new Color(0x0F0F0F));
    };
    
    private static Dimension getScaledDimension(Dimension imageSize, Dimension boundary)
    {
      double widthRatio = boundary.getWidth()/imageSize.getWidth();
      double heightRatio = boundary.getHeight()/imageSize.getHeight();
      double ratio = Math.min(widthRatio, heightRatio);
      return new Dimension((int)(imageSize.width*ratio), (int)(imageSize.height*ratio));
    }
    
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      
      Graphics2D g2d = (Graphics2D)g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      
      if(Audio.image != null)
      {
          Dimension temp = getScaledDimension(new Dimension(Audio.image.getWidth(null), Audio.image.getHeight(null)), getSize());
          g2d.drawImage(Audio.image, getWidth()/2 - temp.width/2, getHeight()/2 - temp.height/2, temp.width, temp.height, null);
      }
      else if(background != null)
      {
          Dimension temp = getScaledDimension(new Dimension(background.getWidth(null), background.getHeight(null)), getSize());
          g2d.drawImage(background, getWidth()/2 - temp.width/2, getHeight()/2 - temp.height/2, temp.width, temp.height, null);
      }
    }
}
