package application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class VolumeUI extends BasicSliderUI  {
	 public VolumeUI(JSlider slider)
     {
         super(slider);
     }
 
     @Override
     public void paintTrack(Graphics g) 
     {
         Graphics2D g2d = (Graphics2D)g;
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
         int thump_x = (int)((double)slider.getValue()/slider.getMaximum()*trackRect.width);
         int temp = (int)((double)thump_x/trackRect.width*thumbRect.height);
         int[] x = {trackRect.x, trackRect.x + trackRect.width, trackRect.x + trackRect.width};
         int[] y = {trackRect.y + trackRect.height, trackRect.y + trackRect.height, trackRect.y};
         g2d.setColor(Color.GRAY);
         g2d.fillPolygon(x, y, 3);
         x = new int[] {trackRect.x, thumbRect.x + thumbRect.width/2, thumbRect.x + thumbRect.width/2};
         y = new int[] {trackRect.y + trackRect.height, trackRect.y + trackRect.height - temp, trackRect.y + trackRect.height};
         g2d.setColor(Color.WHITE);
         g2d.fillPolygon(x, y, 3);
     }
     
     @Override
     public void paintThumb(Graphics g) 
     {
     }
     
     @Override
     protected Dimension getThumbSize() 
     {
         return new Dimension(10, slider.getHeight());
     }
     
     @Override
     protected Color getFocusColor()
     {
         return new Color(0, 0, 0, 0);
     }
}
