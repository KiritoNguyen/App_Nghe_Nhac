package application;
import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;

import javax.swing.*;

import javafx.scene.media.MediaPlayer;


public class ControlPanel extends JPanel 
{
    public int height = 90;
    public int width = 110;
    
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JLabel state = new JLabel("", SwingConstants.LEFT);
    private JLabel time = new JLabel("", SwingConstants.RIGHT);
    
    ImageIcon icPlay = Audio.resizeIcon(new ImageIcon(Audio.resource + "play.png"), 40, 40);
    ImageIcon icPause = Audio.resizeIcon(new ImageIcon(Audio.resource + "pause.png"), 40, 40);
    ImageIcon icSound = Audio.resizeIcon(new ImageIcon(Audio.resource + "sound.png"), 40, 40);
    ImageIcon icMuted = Audio.resizeIcon(new ImageIcon(Audio.resource + "muted.png"), 40, 40);;
    
    public ControlPanel()
    {
        setLayout(null);
        setBackground(Color.decode("#002020"));

        Audio.sli_volume.setOpaque(false);
        Audio.sli_volume.setUI(new VolumeUI(Audio.sli_volume));
        Audio.btn_play.setIcon(icPlay);
        Audio.btn_stop.setIcon(Audio.resizeIcon(new ImageIcon(Audio.resource + "stop.png"), 40, 40));
        Audio.btn_forward.setIcon(Audio.resizeIcon(new ImageIcon(Audio.resource + "next.png"), 40, 40));
        Audio.btn_previous.setIcon(Audio.resizeIcon(new ImageIcon(Audio.resource + "back.png"), 40, 40));
        Audio.btn_sound.setIcon(icSound);
        panel2.setLayout(new GridBagLayout());
        panel2.setBackground(Color.decode("#002020"));
        panel2.add(Audio.btn_stop);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setPreferredSize(new Dimension(30, 40));
        panel2.add(temp);
        panel2.add(Audio.btn_previous);
        panel2.add(Audio.btn_play);
        panel2.add(Audio.btn_forward);
        temp = new JPanel();
        temp.setOpaque(false);
        temp.setPreferredSize(new Dimension(30, 40));
        panel2.add(temp);
        panel2.add(Audio.btn_sound);
        temp = new JPanel();
        temp.setLayout(null);
        temp.setOpaque(false);
        temp.setPreferredSize(new Dimension(80, 40));
        Audio.sli_volume.setBounds(0, 0, 80, 40);
        temp.add(Audio.sli_volume);
        panel2.add(temp);
        for(Component com : panel2.getComponents())
            if(com instanceof JButton)
            {
                JButton btn = (JButton)com;
                btn.setOpaque(false);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setPreferredSize(new Dimension(50, 40));
            }
        Audio.sli_time.setOpaque(false);
        Audio.sli_time.setUI(new TimeUI(Audio.sli_time));
        for (MouseListener ev : Audio.sli_time.getMouseListeners())
            Audio.sli_time.removeMouseListener(ev);
        add(Audio.sli_time);
        panel1.setLayout(null);
        panel1.setBackground(Color.BLACK);
        state.setBounds(5, 0, 100, 20);
        state.setForeground(Color.GREEN);
        panel1.add(state);
        time.setForeground(Color.GREEN);
        panel1.add(time);
        add(panel1);
        add(panel2);
        addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {
                Dimension size = getSize();
                Audio.sli_time.setBounds(5, 5, size.width - 10, 10);
                panel2.setBounds(0, 25, size.width, 40);
                panel1.setBounds(0, 70, size.width, 20);
                time.setBounds(0, 0, size.width - 5, 20);
            }
        });
        
        java.util.Timer step = new java.util.Timer();
        step.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    String s = Audio.player.getStatus().toString();
                    if(s == "STALLED")
                        Audio.player.pause();
                    if(s != "PLAYING")
                        Audio.btn_play.setIcon(icPlay);
                    else
                        Audio.btn_play.setIcon(icPause);
                    if(Audio.player.isMute())
                        Audio.btn_sound.setIcon(icMuted);
                    else
                        Audio.btn_sound.setIcon(icSound);
                    Audio.sli_time.setValue((int)Audio.timer.getTicks().toMillis());
                    state.setText(s);
                    int temp = (int)Audio.timer.getTicks().toSeconds();
                    String text = "";
                    text += (String.format("%02d", temp/60) + ":" + String.format("%02d", temp%60));
                    text += (" (");
                    temp = (int)Audio.player.getCurrentTime().toSeconds();
                    text += (String.format("%02d", temp/60) + ":" + String.format("%02d", temp%60));
                    text += ") / ";
                    temp = (int)Audio.player.getMedia().getDuration().toSeconds();
                    text += (String.format("%02d", temp/60) + ":" + String.format("%02d", temp%60));
                    time.setText(text);
                }
                catch(Exception ex)
                {
                    state.setText("");
                    time.setText("");
                }
                repaint();
            }
        }, 0, 25);
    };
}
