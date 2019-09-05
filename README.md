# 화이트핀 SDK JAVA  

> ## 시작하기  

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

- Fabric ca client 관련 기능
    - affiliation 관리
    - identity 관리
    - 
- Fabric channel 관련 기능
- Fabric chaincode 관련 기능
- Whitepin chaincode 관련 기능  


---  
