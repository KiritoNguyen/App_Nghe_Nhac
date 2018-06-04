package application; 

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class TimeUI extends BasicSliderUI 
{
	        
    public TimeUI(JSlider slider)
    {
        super(slider);
    }

    @Override
    public void paintTrack(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int thump_x = (int)((double)slider.getValue()/slider.getMaximum()*trackRect.width);
        g2d.setColor(new Color(0x0F0F0F));
        g2d.fillRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height);
        g2d.setPaint(new GradientPaint(0, 0, Color.decode("#006b3c"), trackRect.width, 0, Color.decode("#7fff00")));
        g2d.fillRect(trackRect.x, trackRect.y, thump_x, trackRect.height);
        g2d.setPaint(new GradientPaint(thump_x, 0, new Color(0x00FFFFFF, true), thump_x + 5, 0, new Color(0x99FFFFFF, true)));
        g2d.fillRect(thump_x, trackRect.y, 5, trackRect.height);
        g2d.setColor(new Color(0x20FFFFFF, true));
        g2d.fillRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height/2);
    }

    @Override
    public void paintThumb(Graphics g) 
    {
    }
    
    @Override
    protected Color getFocusColor()
    {
        return new Color(0, 0, 0, 0);
    }
}
