FROM nginx:1.25.4

RUN apt update

WORKDIR /etc/nginx/conf.d
COPY linknote.conf .

RUN rm default.conf

EXPOSE 80

