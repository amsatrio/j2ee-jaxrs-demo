# Deployment

## build
```sh
mvn clean
mvn package
```

## deploy
```sh
sudo rm -rf $TOMCAT_HOME/webapps/j2ee-jaxrs-demo* 
sudo cp /home/user0/workspace/java/j2ee-jaxrs-demo/target/j2ee-jaxrs-demo.war $TOMCAT_HOME/webapps/ 
sudo chown tomcat:tomcat $TOMCAT_HOME/webapps/j2ee-jaxrs-demo.war
```