const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://physics-site-816529-b1036c-77-73-132-179.traefik.me',
      changeOrigin: true,
    })
  );
};