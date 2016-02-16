package org.fundaciobit.plugins.scanweb;

/**
 * 
 * @author anadal
 *
 */
public class ScanWebResource {

  String name;
  String mime;
  byte[] content;

  /**
   * @param name
   * @param mime
   * @param content
   */
  public ScanWebResource(String name, String mime, byte[] content) {
    super();
    this.name = name;
    this.mime = mime;
    this.content = content;
  }

  /**
   * 
   */
  public ScanWebResource() {
    super();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMime() {
    return mime;
  }

  public void setMime(String mime) {
    this.mime = mime;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

}
