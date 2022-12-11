FROM 812436859307.dkr.ecr.us-west-2.amazonaws.com/java_base_image:latest

USER tcp

COPY jars/*.jar /opt/tcp/app
COPY start-app-services.sh /opt/tcp/app/start-app.sh
USER root
RUN chmod 755 /opt/tcp/app/start-app.sh
USER tcp
EXPOSE 2020 2021
CMD ["/opt/tcp/app/start-app.sh"]