import java.net.URL;
import java.lang.Exception;

public class YoutubeFormat
{
  private URL dlURL;
  private String quality;
  private String fileType;
  private int bitrate;
  
  public YoutubeFormat(String format) throws Exception
  {
    String shortened;
    int index;
    
    try { this.dlURL = new URL(format.substring(4, format.indexOf(";"))); } catch (Exception e) { throw new Exception("Failed DL URL"); }
    
    if ((index = format.indexOf("type=")) == -1)
      throw new Exception("No Type Found");
    
    shortened = format.substring(format.indexOf("type="));
    this.fileType = shortened.substring(5, shortened.indexOf(";"));
    
    try
    {
    shortened = format.substring(format.indexOf("bitrate="));
    this.bitrate = Integer.valueOf(shortened.substring(8, shortened.indexOf("&")));
    } catch(Exception e) { throw new Exception("No bitrate found"); } 
    
      try {
        shortened = format.substring(format.indexOf("size=")); 
        this.quality = shortened.substring(shortened.indexOf("x") + 1, shortened.indexOf("&"));
      }
      catch(Exception e) { 
        try {
          shortened = format.substring(format.indexOf("quality=")); 
          this.quality = shortened.substring(shortened.indexOf("x") + 1, shortened.indexOf("&")).replaceAll("[a-zA-Z]", "");
        } catch(Exception e2) {  throw new Exception("Size and Quality failed"); }
      }
    
  }
  
  public URL getDlURL()
  { return this.dlURL; }
  public String getQuality()
  { return this.quality; }
  public String getFileType()
  { return this.fileType; }
  public int getBitrate()
  { return this.bitrate; }
  
}