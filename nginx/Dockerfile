FROM node:latest as build_frontend

# See the .dockerignore for details
COPY frontend /home/app

WORKDIR /home/app
RUN npm install
RUN npm run build

FROM nginx:latest

# frontend
COPY --from=build_frontend /home/app/dist /usr/share/nginx/html/frontend
RUN chown -R nginx:nginx /usr/share/nginx/html/frontend
RUN chmod -R 755 /usr/share/nginx/html/frontend

# ssl
COPY --chmod=600 --chown=nginx:nginx  ssl/ssl-cert-snakeoil.key /etc/nginx/ssl/ssl-cert-snakeoil.key
COPY --chmod=600 --chown=nginx:nginx  ssl/ssl-cert-snakeoil.crt /etc/nginx/ssl/ssl-cert-snakeoil.crt

# config
COPY --chown=nginx:nginx --chmod=644 nginx/nginx.conf /etc/nginx/conf.d/default.conf