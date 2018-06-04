package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
public class MusicOnline extends JFrame {

	private JPanel contentPane;
	 JTextField txt;
	JTextArea txtarea;
	JScrollPane scroll;
	JButton btn,btnplay;
	String ssource;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

        com.sun.javafx.application.PlatformImpl.startup(()->{});
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusicOnline frame = new MusicOnline();
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
	public MusicOnline() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e)
            {
                
                e.getWindow().dispose();
            }
        });
		setBounds(100, 100, 450, 440);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setLayout(new FlowLayout());
		contentPane.setBackground(Color.GRAY);
		setContentPane(contentPane);
		txt=new JTextField(20);
		AutoSuggestor auto=new AutoSuggestor(txt, this, null,  Color.WHITE.brighter(), Color.BLUE, Color.RED, 0.75f) {
		
		   @Override
           boolean wordTyped(String typedWord) {

               //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
			   ArrayList<String> words = new ArrayList<>();

               HttpClient connection = new HttpClient();
               String data = connection.getData();
               org.json.JSONArray json;
               try {
               json = new org.json.JSONArray(data);
              

               for ( int i = 0 ; i < json.length() ; i++)
               {
               org.json.JSONObject object = json.getJSONObject(i);
               String s = getString(object,"title").toLowerCase();
            
               words.add(s);
               }
             } catch (JSONException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          }

               setDictionary(words);
               //addToDictionary("bye");//adds a single word

               return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
           }
       };
       add(txt);	
		txtarea=new JTextArea(15,45);
		txtarea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtarea.setEditable(false);
		btn=new JButton("Search");
		add(btn);
		getContentPane().add(txtarea);
		scroll=new JScrollPane();
		scroll.setViewportView(txtarea);
		add(scroll);
		
		this.setTitle("FIND MUSIC ONLINE");
		this.setVisible(true);
		btnplay=new JButton("Play");
		btnplay.setVisible(false);
		add(btnplay);

		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(checkIntConnection()==false)
				{
					 JOptionPane.showMessageDialog(null, "Check your network connection",
			                  "Title", JOptionPane.WARNING_MESSAGE);
				}
				findmusic();
			}
		});
		btnplay.setActionCommand("Open");
		btnplay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub	
				 Audio.audio.mediaOpen(ssource.substring(8));
				 Audio.audio.play();
				contentPane.setEnabled(false);
			}
		});

	}
	 private JScrollBar scrollBar;
		 
	public static String removeAccent(String s) {
		  
		 String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		 Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		 return pattern.matcher(temp).replaceAll("");
		 
	}
	public void findmusic()
	{
	String a=txt.getText().replace(" ", "").toLowerCase();
	 HttpClient connection = new HttpClient();
	    String data = connection.getData();
	    String salbum = null,sdate=null,slanguage=null,sbody=null,scopy=null,sshare=null;

	    org.json.JSONArray json;
		  try {
			json = new org.json.JSONArray(data);
			int count=0;
			  for ( int i = 0 ; i < json.length() ; i++)
			  {
				  org.json.JSONObject object = json.getJSONObject(i);
				  String stitle = getString(object,"title").toLowerCase();
				  String stitles = getString(object,"title").replace(" ", "").toLowerCase();
				  if(stitles.compareTo(a)==0)
				  {
					String sartist ="Artist: "+ getString(object,"artist") ;
					ssource ="Source: "+ getString(object,"source") ;
					TrackData data1 = null,data2=null;
					String apiKey = "646a669754a9946fee5494bb77678e61";
					MusixMatch musixMatch = new MusixMatch(apiKey);
					String trackName = stitle;
					String artistName = sartist;

					// Track Search [ Fuzzy ]
					Track track,track1;
					Lyrics lyrics;
					try {
						track = musixMatch.getMatchingTrack(trackName, artistName);
						data1 = track.getTrack();
						salbum="Album Name : " + data1.getAlbumName();
						sdate="First Release Date :"  + data1.getFirstReleaseDate();				
						lyrics = musixMatch.getLyrics(data1.getTrackId());
						slanguage="Lyrics Language : "     + lyrics.getLyricsLang();
						sbody="Lyrics Body : "  +"\n"   + lyrics.getLyricsBody();
						scopy="Lyrics Copyright : "    + lyrics.getLyricsCopyright();
						sshare="Share :"+data1.getTrackShareUrl();
						txtarea.setText(sartist+"\n"+ssource+"\n"+salbum+"\n"+sdate+"\n"+slanguage+"\n"+sbody+"\n"+scopy+"\n"+sshare);
						
					} catch (MusixMatchException e2) {
						// TODO Auto-generated catch block
						//e2.printStackTrace();
						String sname=removeAccent(getString(object,"title"));
						String sartists=removeAccent(getString(object,"artist"));
						try {
							track1 = musixMatch.getMatchingTrack(sname, sartists);
							data2 = track1.getTrack();
							salbum="Album Name : " + data2.getAlbumName();
							sdate="First Release Date :"  + data2.getFirstReleaseDate();
							lyrics = musixMatch.getLyrics(data2.getTrackId());
							slanguage="Lyrics Language : "     + lyrics.getLyricsLang();
							sbody="Lyrics Body : " +"\n"    + lyrics.getLyricsBody();
							scopy="Lyrics Copyright : "    + lyrics.getLyricsCopyright();
							sshare="Share :"+data2.getTrackShareUrl();
							txtarea.setText(sartist+"\n"+ssource+"\n"+salbum+"\n"+sdate+"\n"+slanguage+"\n"+sbody+"\n"+scopy+"\n"+sshare);
						} catch (MusixMatchException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}  
					  count++;
					  btnplay.setVisible(true);
				  } 
			  }
			  if(count==0)
			  {
				  txtarea.setText("Not found");		  
			  }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static org.json.JSONObject getObject(String tagName, org.json.JSONObject json) throws JSONException {
		org.json.JSONObject subObj = json.getJSONObject(tagName);
		return subObj;
		}
	public boolean checkIntConnection()
    {
        boolean status = false;
        Socket sock = new Socket();
        InetSocketAddress address = new InetSocketAddress("www.google.com", 80);
        try
        {
           sock.connect(address, 3000);
           if(sock.isConnected())
           {
               status=true;
           }
        }
        catch(Exception e)
        {

        }
        finally
        {
            try
            {
                sock.close();
            }
            catch(Exception e)
            {

            }
        }

        return status;
    }  
	    

	public static String getString(org.json.JSONObject obj , String data) throws JSONException
	{
		return obj.getString(data);
	}
}
