/*global Promise: false, firebase: false */

var PLUGIN_NAME = "FirebaseRealtime";

var loadJS = function(url, implementationCode, location) {
  var scriptTag = document.createElement('script');
  scriptTag.src = url;

  scriptTag.onload = implementationCode;
  scriptTag.onreadystatechange = implementationCode;

  location.appendChild(scriptTag);
};

function FirebaseRealtime(config) {
  this.config = config;
}

FirebaseRealtime.prototype.initialise = function() {
  var self = this;

  return new Promise(function(resolve, reject) {

    var initialise = function() {
      firebase.initializeApp(self.config);
      self.database = firebase.database();
      resolve();
    };

    loadJS('https://www.gstatic.com/firebasejs/4.5.0/firebase.js', function() {
      loadJS('https://www.gstatic.com/firebasejs/4.5.0/firebase-database.js', initialise, document.body);
    }, document.body);
  });
};

FirebaseRealtime.prototype.ref = function(path) {
  return this.database.ref(path);
};

if (typeof module !== undefined && module.exports) {
  module.exports = FirebaseRealtime;
}
