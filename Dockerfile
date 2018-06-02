# Pull base image
From tomcat:8-jre8

RUN rm -rf /usr/local/tomcat/webapps/ROOT
ENV TZ=America/New_York
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ADD ./target/monitoring-execution-service.war  /usr/local/tomcat/webapps/ROOT.war