 package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class menuBar extends JMenuBar 
{
    JMenu menu = new JMenu("");
    
    public menuBar()
    {
        menu.setIcon(Audio.resizeIcon(new ImageIcon(Audio.resource + "menu.png"),20, 20));
        JMenuItem menuitem = new JMenuItem("Quick Open File", Audio.resizeIcon(new ImageIcon(Audio.resource + "folder.png"), 30, 30));
        menuitem.addActionListener(new Audio.QuickOpenFile());
        menu.add(menuitem);
        menuitem = new JMenuItem("Open URL", Audio.resizeIcon(new ImageIcon(Audio.resource + "internet.png"), 30, 30));
        menuitem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {Audio.OpenURL();}});
        menu.add(menuitem);
        menu.setOpaque(true);
        menu.setBorder(null);
        menuitem = new JMenuItem("Find music online", Audio.resizeIcon(new ImageIcon(Audio.resource + "tracking.png"), 30, 30));
        menuitem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {new MusicOnline();}});
        menu.add(menuitem);
        //menu.getPopupMenu().setBorder(null);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(menu);
        setBorder(null);
        setBackground(Color.BLACK);
    };

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        
        g2d.setColor(Color.GREEN);
        try
        {
        	if(Audio.local)
        		g2d.drawString(Audio.path, menu.getWidth() + 5, menu.getY() + menu.getHeight()/2 + metrics.getHeight()/4);
        	else
        		g2d.drawString(Audio.url, menu.getWidth() + 5, menu.getY() + menu.getHeight()/2 + metrics.getHeight()/4);	
        }
        catch (Exception ex) {}
    }
}
