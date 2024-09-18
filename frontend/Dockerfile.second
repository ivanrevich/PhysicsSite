FROM node:18-alpine

WORKDIR /frontapp

EXPOSE 3005

COPY ["package.json", "package-lock.json*", "./"]

RUN npm install

COPY . .

CMD ["npm", "start"]