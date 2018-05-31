# ASE-FS2018
[![Build Status](https://travis-ci.org/sflin/ASER_Parser.svg?branch=master)](https://travis-ci.org/sflin/ASER_Parser)

This is the parser of the ASER-project. Its counter-part and more information can be found [here](https://github.com/sflin/ASER_Recommender).

For re-usage of the ASER-Parser, add the following to the dependencyManagement section of your pom.xml: 
```xml
<repositories>
  <repository>
    <id>sflin-ASER_Parser</id>
    <url>https://packagecloud.io/sflin/ASER_Parser/maven2</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

as well as this dependency:
```xml
<dependency>
  <groupId>ASER_Paser</groupId>
  <artifactId>ASER_Parser</artifactId>
  <version>1.0</version>
</dependency>
```
