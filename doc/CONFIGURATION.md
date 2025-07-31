# Configuration

## Tomcat database configuration
 ${TOMCAT_HOME}/conf/server.xml
```xml
...
  <GlobalNamingResources>
          <Resource name="jdbc/H2Database"
                  global="jdbc/H2Database"
              auth="Container"
              type="javax.sql.DataSource"
              driverClassName="org.h2.Driver"
            url="jdbc:h2:file:/opt/tomcat/data/h2.db;AUTO_SERVER=TRUE"
              username="<YOUR_USERNAME>"
              password="<YOUR_PASSWORD>"
              maxTotal="17"
              maxIdle="5"
              maxWaitMillis="100000"
              validationQuery="SELECT 1"
              testOnBorrow="true"
            />
    <Resource name="jdbc/SQLiteDatabase"
            global="jdbc/SQLiteDatabase"
              auth="Container"
              type="javax.sql.DataSource"
              driverClassName="org.sqlite.JDBC"
            url="jdbc:sqlite:/opt/tomcat/data/sqlite.db"
              maxTotal="17" maxIdle="5"
              maxWaitMillis="100000"
              validationQuery="SELECT 1"
              testOnBorrow="true"
            />
  </GlobalNamingResources>
...
```

 ${TOMCAT_HOME}/conf/context.xml
```xml
<Context>
    ...
    <ResourceLink name="jdbc/H2DataSource"
            global="jdbc/H2Database"
              auth="Container"
              type="javax.sql.DataSource"
            />
    <ResourceLink name="jdbc/SQLiteDataSource"
            global="jdbc/SQLiteDatabase"
              auth="Container"
              type="javax.sql.DataSource"
            />
    ...
</Context>
```

Get database library and then put them to tomcat lib folder.
```sh
sudo su
cd $TOMCAT_HOME/lib
wget https://repo1.maven.org/maven2/com/h2database/h2/2.3.232/h2-2.3.232.jar
wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.50.3.0/sqlite-jdbc-3.50.3.0.jar
sudo chown tomcat:tomcat -R ./*
systemctl restart tomcat.service
```