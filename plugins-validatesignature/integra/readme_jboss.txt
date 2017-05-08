

Per usar en JBOSS 5.x s'ha d'usar la següent despendència:

  <dependency>
    <groupId>org.fundaciobit.plugins</groupId>
    <artifactId>plugin-validatesignature-integra</artifactId>
    <version>1.0.0</version>
    
    <exclusions>
        <!-- Eliminam tota la comunicació AXIS -->
        <exclusion>  
          <groupId>org.apache.axis</groupId>
          <artifactId>axis</artifactId>
        </exclusion>
        <exclusion>  
          <groupId>org.apache.axis</groupId>
          <artifactId>axis-jaxrpc</artifactId>
        </exclusion>
        <exclusion>  
          <groupId>org.apache.axis</groupId>
          <artifactId>axis-saaj</artifactId>
        </exclusion>
        <exclusion>  
          <groupId>org.apache.santuario</groupId>
          <artifactId>xmlsec</artifactId>
        </exclusion>
        <exclusion>  
          <groupId>com.sun.xml.wss</groupId>
          <artifactId>xws-security</artifactId>
        </exclusion>
        
        <!-- Altres llibreries no necessaries -->
        <exclusion>  
          <groupId>xerces</groupId>
          <artifactId>xercesImpl</artifactId>
        </exclusion>

        <exclusion>  
          <groupId>com.sun.xml.bind</groupId>
          <artifactId>jaxb-impl</artifactId>
        </exclusion>

        <exclusion>  
          <groupId>xalan</groupId>
          <artifactId>xalan</artifactId>
        </exclusion>

        <exclusion>  
          <groupId>org.slf4j</groupId>
          <artifactId>log4j-over-slf4j</artifactId>
        </exclusion>

        <exclusion>  
          <groupId>org.slf4j</groupId>
          <artifactId>jcl-over-slf4j</artifactId>
        </exclusion>
        
        <exclusion>  
          <groupId>org.apache.ws.security</groupId>
          <artifactId>wss4j</artifactId>
        </exclusion>
        
        <exclusion>  
          <groupId>org.apache.santuario</groupId>
          <artifactId>xmlsec</artifactId>
        </exclusion>
        
      </exclusions> 
       
  </dependency>
