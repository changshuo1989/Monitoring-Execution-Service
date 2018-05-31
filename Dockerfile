# Pull base image
From tomcat:8-jre8

RUN rm -rf /usr/local/tomcat/webapps/ROOT
ADD ./target/monitoring-execution-service.war  /usr/local/tomcat/webapps/ROOT.war