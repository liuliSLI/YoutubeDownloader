/*
 * http://stackoverflow.com/questions/13537202/play-high-quality-youtube-videos-using-videoview#comment19679509_13537202
 * http://stackoverflow.com/questions/15773511/decode-url-encoded-fmt-stream-map-to-valid-url
 * http://stackoverflow.com/questions/16503166/getting-a-youtube-download-url-from-existing-url
 * http://www.youtube.com/watch?v=3jcOdfMlgi4
 * */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;

import java.util.ArrayList;

public class YoutubeURL
{
  public static void main(String[] args)
  {
    YoutubeURL vid = new YoutubeURL("http://www.youtube.com/watch?v=3jcOdfMlgi4");
    //System.out.println(vid.getDownloadURL());
  }
  
  private URL url;
  private ArrayList<YoutubeFormat> formats = new ArrayList<YoutubeFormat>();
  
  
  
  public YoutubeURL(String url)
  {
    try { this.url = new URL(url); } catch(Exception e) {}
    constructFormatMap();
    
    System.out.println("Finished Constructing Formats" + formats.size());
    for (YoutubeFormat format : formats)
      System.out.println("\n\nDL: " + format.getDlURL() + "\nQuality: " + format.getQuality() +  "\nBitrate: " + format.getBitrate() + "\nFile Type: " + format.getFileType());
  }
  
  private void constructFormats(String formatMap)
  {
    int index;
    while((index = formatMap.indexOf("\\u0026")) != -1) // Places ampersands here
      formatMap = formatMap.substring(0, index) + "&" + formatMap.substring(index + 6, formatMap.length());
    
    while((index = formatMap.lastIndexOf("url=")) != -1) // Split the formats apart, creating objects for each.
    {
      //System.out.println(formatMap.substring(index) + "\n\nNew Format");
      try { formats.add(new YoutubeFormat(formatMap.substring(index))); } catch(Exception e) { System.out.println(e.getMessage() + " Failed creating format"); }
      formatMap = formatMap.substring(0, index);
    }
    
  }
  
  public String getDownloadURL(YoutubeFormat chosenFormat) // When all finished, call this method. Parameter chosen format should depend on quality etc
  {
    return chosenFormat.getDlURL().toString();
  }
  
  private void constructFormatMap()
  {
    String formatMap = "";
    
    BufferedReader in = null;
    try 
    {
      in = new BufferedReader(new InputStreamReader(this.url.openStream()));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        if (inputLine.contains("url_encoded_fmt_stream_map")) // Find the format map. Only concerned with that.
          formatMap = inputLine.substring(inputLine.indexOf("url_encoded_fmt_stream_map") - 1);
      }
    }
    catch (Exception e) { e.printStackTrace(); } finally { if (in != null) { try { in.close(); } catch (IOException e) { System.out.println(e.toString() + " Connection Timed Out"); } } }//e.printStackTrace(); } } }
    
    try  // Decode the URL 
    { formatMap = URLDecoder.decode(formatMap, "UTF-8"); } catch (Exception e) { System.out.println("FAILED DECODE"); }
    
    constructFormats(formatMap);
  }
  
  public URL getURL()
  { return this.url; }
  public String getAddress()
  { return this.url.toString(); }
  public String getID()
  { return this.url.toString().substring(this.url.toString().indexOf("=")); }
  
}