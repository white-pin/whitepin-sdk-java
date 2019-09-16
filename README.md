# WHITE PIN SDK JAVA

This project is java toolkit for fabric and white pin chaincode.
  

> ## Getting started  

- **maven**  
  
```aidl
...

<repositories>
    <repository>
        <id>jcenter</id>
        <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories> 

...

<dependencies>
    <dependency>
        <groupId>com.github.white-pin</groupId>
        <artifactId>whitepin-sdk-java</artifactId>
        <version>0.0.1</version>
    </dependency>
</dependencies>
```  

- **gradle**  

```aidl
...
repositories {
    jcenter()
}
...

dependencies {
    compile 'com.github.white-pin:whitepin-sdk-java:0.0.1'
}
```  

---  

> ## Features  

- **FabricCaClient**  
    - wrapper of HFCAClient.java
    - management of affiliation
    - management of identity
- **FabricChannelClient**
    - wrapper of HFClient.java
    - create a channel (request to orderer)
    - build Channel.java about existing channel
- **FabricChaincodeClient**  
    - wrapper of HFClient.java
    - invoke and query for instantiated chaincode
- **Whitepin chaincode**  
    - invoke and query for instantiated whitepin chaincode
---  

---  

> ## Integration tests  


- **FabricCertClient**  

- start ca server  

```$xslt
$ src/test/fixture/certintegration/whitepin.sh restart
```  

- run com.github.whitepin.sdk.integration.FabricCertClientIT


- **FabricChannelClient**  

- start orderers, peers  

```$xslt
$ src/test/fixture/channelintegration/whitepin.sh restart
```  

- run com.github.whitepin.sdk.integration.FabricChannelClientIT
