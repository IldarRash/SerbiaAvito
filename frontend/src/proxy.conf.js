const PROXY_CONFIG = {
  "**": {
    "target": "http://localhost:8080",
    "secure": false,
    "bypass": function (req) {
      if (req && req.headers && req.headers.accept && req.headers.accept.indexOf("html") !== -1) {
        console.log("Skipping proxy for browser request.");
        return "/index.html";
      }
    }
  }
};

module.exports = PROXY_CONFIG;
