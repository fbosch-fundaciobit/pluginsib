package org.fundaciobit.plugins.test;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.MetadataType;

/**
 * 
 * @author anadal
 * 
 */
public class TestMetadata  {

  //@org.junit.Test
  public void test() {

    System.out.println();
  }
  
  
  public static void main(String[] args) {
    
    try {
      
      HashMap<String, ArrayList<Metadata>> all = new HashMap<String, ArrayList<Metadata>>();

      
      
      ArrayList<Metadata> k1 = new ArrayList<Metadata>();
      k1.add(new Metadata("k1", "value11", MetadataType.STRING));
      k1.add(new Metadata("k1", "value12", MetadataType.STRING));
      
      all.put("k1", k1);
      
      
      ArrayList<Metadata> k2 = new ArrayList<Metadata>();
      k2.add(new Metadata("k2", "12", MetadataType.INTEGER));
      
      all.put("k2", k2);
      
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      
      {
      java.beans.XMLEncoder xmlEnc = new XMLEncoder(baos);
      
      xmlEnc.writeObject(all);
      
      xmlEnc.close();
      }
      
      System.out.println(new String(baos.toByteArray()));
      
      
      XMLDecoder dec = new XMLDecoder(new ByteArrayInputStream(baos.toByteArray()));
      
      HashMap<String, ArrayList<Metadata>> all2 = (HashMap<String, ArrayList<Metadata>>)dec.readObject();
      
      System.out.println(all2.size());
      
      
      
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
  }


}
