FROM suinsit/jetty-10-es
EXPOSE 8080/tcp
ADD target/suinsit-nova-1.2.24 /var/lib/jetty/webapps/ROOT
ADD ROOT.xml /var/lib/jetty/webapps/ROOT.xml 
#ADD target/*.* /opt/webapps/ROOT.war
COPY application-source/apps /opt/suinsit/apps
COPY application-source/data /opt/suinsit/data
COPY application-source/images /opt/suinsit/images
COPY application-source/bpm /opt/suinsit/bpm
COPY application-source/*.properties /opt/suinsit
COPY application-source/*.json /opt/suinsit