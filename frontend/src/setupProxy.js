const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://77.73.132.179:8080',
      changeOrigin: true,
    })
  );
};