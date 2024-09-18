const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'https://physics-site-816529-0d821b-77-73-132-179.traefik.me',
      changeOrigin: true,
    })
  );
};