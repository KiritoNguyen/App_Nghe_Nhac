package application;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.imageio.*;

import javafx.util.*;
import javafx.beans.property.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.SwingFXUtils;



@SuppressWarnings("deprecation")
public class Audio extends JFrame
{
    static String path = "";
    static ArrayList<String> folder = new ArrayList<String>();
    static String resource = System.getProperty("user.dir") + "/Resources/";
    
    static Time_counter timer = new Time_counter();
    
    static MediaPlayer player;
    
    static JFrame frame;
    static JSlider sli_time = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
    static JSlider sli_volume = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    static JButton btn_stop = new JButton();
    static JButton btn_play = new JButton();
    static JButton btn_sound = new JButton();
    static JButton btn_forward = new JButton();
    static JButton btn_previous = new JButton();

    static ControlPanel controlpanel = new ControlPanel();
    static DisplayPanel displaypanel = new DisplayPanel();
    static menuBar menubar = new menuBar();
    
    static boolean local = false; 
    static String artist = null;
    static String album = null;
    static String title = null;
    static String year = null;
    static String url = null;
    static Image image = null;
    
    static Audio audio;
     
    public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) 
    {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
    }
    
    public static String getDirectory(String s) 
    {
        if(!s.isEmpty())
            return s.substring(0,s.lastIndexOf(File.separator));
        return "";
    }
    
    public Audio()
    {
        frame = this;
        frame.setBounds(100, 50, 600, 400); //location, size
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        audio = this;
        
        {   
            controlpanel.setSize(new Dimension(500, 1000));
            frame.setJMenuBar(menubar);
            frame.add(controlpanel);
            frame.add(displaypanel);
            frame.getContentPane().addComponentListener(new ComponentAdapter()
            {
                public void componentResized(ComponentEvent e)
                {
                    Dimension temp = frame.getContentPane().getSize();
                    displaypanel.setBounds(0, 0, temp.width, temp.height - controlpanel.height);
                    controlpanel.setBounds(0, temp.height - controlpanel.height, temp.width, controlpanel.height);
                    repaint();
                }
            });
            frame.getContentPane().setPreferredSize(new Dimension(0, controlpanel.height + menubar.getSize().height));
            frame.pack();
            frame.setMinimumSize(frame.getBounds().getSize());
            frame.getContentPane().setPreferredSize(new Dimension(500, 300));
            frame.pack();
            frame.setVisible(true);
        }

        btn_play.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(player != null)
                {
                    if(player.getStatus() != MediaPlayer.Status.PLAYING)
                    {
                        player.play();
                    }
                    else
                    {
                        player.pause();
                    }
                }
            }
        });

        btn_stop.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    player.stop();
                } catch (Exception ex) {}
            }
        });

        btn_sound.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    player.setMute(!player.isMute());
                } catch (Exception ex) {}
            }
        });

        btn_forward.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(local)
                {
                    File[] arrayfile = new File(getDirectory(path)).listFiles((dir, name) -> {
                        name = name.toLowerCase();
                        return name.endsWith(".mp3") || name.endsWith(".wav");
                    });
                    folder.clear();
                    for (File file : arrayfile)
                        folder.add(file.getAbsolutePath());
                    int temp = folder.indexOf(path);
                    if(temp != -1 && ++temp < folder.size())
                        mediaOpen(new File(folder.get(temp)));
                }
            }
        });

        btn_previous.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(local)
                {
                    File[] arrayfile = new File(getDirectory(path)).listFiles((dir, name) -> {
                        name = name.toLowerCase();
                        return name.endsWith(".mp3") || name.endsWith(".wav");
                    });
                    folder.clear();
                    for (File file : arrayfile)
                        folder.add(file.getAbsolutePath());
                    int temp = folder.indexOf(path);
                    if(temp > 0)
                        mediaOpen(new File(folder.get(--temp)));
                }
            }
        });
        
        sli_volume.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e) 
            {
                try
                {
                    player.setVolume((double)sli_volume.getValue()/100);
                }
                catch (Exception ex) {}
            }
        });

        sli_time.addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseMoved(MouseEvent e) {} 
            public void mouseDragged(MouseEvent e) 
            {
                sli_time.setValue((int)((double)sli_time.getMousePosition().x/sli_time.getSize().width*sli_time.getMaximum()));
                Duration temp = new Duration((double)(sli_time.getValue()));
                timer.seek(temp);
                try
                {
                    player.seek(temp);
                }catch(Exception ex) {}
            }
        });
    }
    
    public static class QuickOpenFile implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(getDirectory(path)));
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.mp3, *.wav", "mp3", "wav"));
            if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                Audio.mediaOpen(fileChooser.getSelectedFile());
        }
    }    
    
    static void OpenURL()
    {
        String input = "";
        input = JOptionPane.showInputDialog("URL: ");
        if(!input.isEmpty())
            mediaOpen(input);
    };
    public void play()
    {
         if(player != null)
         {
             if(player.getStatus() != MediaPlayer.Status.PLAYING)
                 player.play();
             else
             {
                 player.pause();
              //   btn_play.setIcon(map_icon.get("pause2"));
             }
         }
    }
    public static void mediaOpen(Object input)
    {
        try
        {
            player.stop();
        } catch (Exception ex) {}
        File file;
        player = null;
        artist = null;
        album = null;
        title = null;
        image = null;
        year = null;
        if(input instanceof File)
        {
            file = (File)input;
            local = true;
            path = file.getAbsolutePath();
            player = new MediaPlayer(new Media(file.toURI().toString()));
        }
        else
        {
            url = (String)input;
            file = new File(url);
            if(file.isFile())
            {
                local = true;
                path = file.getAbsolutePath();
                player = new MediaPlayer(new Media(file.toURI().toString()));
            }
            else
            {
                local = false;
                try
                {
                    new URL(url).toURI();
                    player = new MediaPlayer(new Media(url));
                }catch (Exception e) {}
            }
        }
        if(player != null)
        {
            player.setOnReady(() -> {
                Media media = player.getMedia();
                sli_time.setMaximum((int)media.getDuration().toMillis());
                for (Map.Entry<String, Object> entry : media.getMetadata().entrySet())
                    switch(entry.getKey())
                    {
                        case "artist":
                            artist = entry.getValue().toString();
                            break;

                        case "title":
                            title = entry.getValue().toString();
                            break;

                        case "album":
                            album = entry.getValue().toString();
                            break;
                            
                        case "year":
                            year = entry.getValue().toString();
                            break;

                        case "image":
                            image = SwingFXUtils.fromFXImage((javafx.scene.image.Image)entry.getValue(), null);
                            break;
                    }
                String text = "";
                if(title != null)
                {
                    text += ("Title: " + title);
                    if(album != null)
                    {
                        text += ("      Album: " + album);
                        if(year != null)
                            text += (" (" + year + ")");
                    }
                }
                if(text != "")
                    frame.setTitle(text);
                else
                {
                    if(local)
                        frame.setTitle(file.getName());
                    else
                        frame.setTitle(url);
                }
                displaypanel.repaint();
            });
            player.setOnPaused(() -> {timer.pause();});
            player.setOnPlaying(() -> {
                player.setVolume((double)sli_volume.getValue()/sli_volume.getMaximum());
                timer.start();
            });
            player.setOnStopped(() -> {
                timer.stop();
                try
                {
                    player.pause();
                    player.seek(new Duration(0));
                } catch (Exception ex) {}
            });
            player.setOnEndOfMedia(() -> {
                player.stop();
            });
            player.setAutoPlay(false);
        }
        else
            frame.setTitle("");
        menubar.repaint();
    }
    
    public static void main(String[] argv) throws Exception 
    {
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        //System.out.println("Working Directory = "+System.getProperty("user.dir"));
        new Audio();
    }
}