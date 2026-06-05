# Stage 1: Build
FROM node:22-alpine AS build
WORKDIR /app

ARG BUILD_CONFIG=production

RUN npm install -g pnpm@10.28.1
# Copiar archivos de configuración de dependencias
COPY package.json pnpm-lock.yaml* ./
# Instalación limpia 
RUN pnpm install --frozen-lockfile

# Copiar el resto del código (el .dockerignore filtrará node_modules)
COPY . .
# Ejecutar el build usando el binario local de node_modules
RUN pnpm run build --configuration=${BUILD_CONFIG}

# Stage 2: Runtime
FROM nginx:stable-alpine
WORKDIR /usr/share/nginx/html

# Limpieza y copia de artefactos de Angular 21
RUN rm -rf ./*
COPY --from=build /app/dist/rrhh-frontend/browser .

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
