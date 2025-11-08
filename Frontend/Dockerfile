FROM node:20-alpine

WORKDIR /usr/src/app

COPY package*.json ./
RUN npm install

COPY . .

EXPOSE 5173

# Vite будет доступен по локальной сети
CMD ["npm", "run", "dev", "--", "--host"]
