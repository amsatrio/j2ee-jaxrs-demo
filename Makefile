clean:
	rm -rf target
	mvn clean
build:
	mvn package
deploy: clean build
	sudo rm -rf /opt/tomcat/updated/webapps/j2ee-jaxrs-demo* && sudo cp /home/user0/workspace/java/j2ee-jaxrs-demo/target/j2ee-jaxrs-demo.war /opt/tomcat/updated/webapps/ && sudo chown tomcat:tomcat /opt/tomcat/updated/webapps/j2ee-jaxrs-demo.war