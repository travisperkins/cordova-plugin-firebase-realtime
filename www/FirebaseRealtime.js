var exec = require('cordova/exec');
var utils = require("cordova/utils");
var PLUGIN_NAME = "FirebaseRealtime";

"use strict";

function FirebaseRealtime(persist) {

  this.isOnline = false;
  this.persist = persist;
  this.isPersistenceEnabled = true;
}

FirebaseRealtime.prototype = {
  initialise: function() {

    var self = this;

    return new Promise(function(resolve, reject) {

      exec(dispatchEvent, null, PLUGIN_NAME, 'initialize', [self.persist]);
      resolve();

      function dispatchEvent(event) {
        window.dispatchEvent(new CustomEvent(event.type, {
          detail: event.data
        }));
      }
    });
  },
  goOnline: function() {
    this.isOnline = !!value;
    exec(dispatchEvent, null, PLUGIN_NAME, 'setOnline', [this.isOnline]);
  },
  ref: function(path) {
    return new DbRef(path);
  },
  refFromUrl: function(url) {
    throw "FirebaseRealtime.refFromUrl: not supported"
  }
};

Object.defineProperties(FirebaseRealtime.prototype, {

  online: {
    set: function(value) {
      this.isOnline = !!value;
      exec(dispatchEvent, null, PLUGIN_NAME, 'setOnline', [this.isOnline]);
    },
    get: function() {
      return this.isOnline;
    }
  },
  loggingEnabled: {
    set: function(value) {
      this.isLoggingEnabled = !!value;
      exec(dispatchEvent, null, PLUGIN_NAME, 'setLoggingEnabled', [this.isLoggingEnabled]);
    },
    get: function() {
      return this.isLoggingEnabled;
    }
  },
  persistenceEnabled: {
    get: function() {
      return this.isPersistenceEnabled;
    }
  }
});

function DbQuery(ref, orderBy) {
  this.ref = ref;
  this._orderBy = orderBy;
  this._includes = [];
  this._limit = {};
}

DbQuery.prototype = {
  endAt: function(value, key) {
    this._includes.push({
      endAt: value,
      key: key
    });
    return this;
  },
  startAt: function(value, key) {
    this._includes.push({
      startAt: value,
      key: key
    });
    return this;
  },
  equalTo: function(value, key) {
    this._includes.push({
      equalTo: value,
      key: key
    });
    return this;
  },
  limitToFirst: function(limit) {
    this._limit = {
      first: limit
    };
    return this;
  },
  limitToLast: function(limit) {
    this._limit = {
      last: limit
    };
    return this;
  },
  on: function(eventType, success, error) {
    var callback = function(data) {
      success(new DbSnapshot(data));
    };

    callback._id = utils.createUUID();

    exec(callback, error, PLUGIN_NAME, "on", [this.ref._path, eventType, this._orderBy, this._includes, this._limit, callback._id]);

    return callback;
  },
  once: function() {
    var args = [this.ref._path, this._orderBy, this._includes, this._limit];
    return new Promise(function(resolve, reject) {
      exec(function(data) {
        var snapshot = new DbSnapshot(data);
        resolve(snapshot);
        if (typeof fn === 'function') fn(snapshot);
      }, reject, PLUGIN_NAME, "once", args);
    });
  },
  off: function(eventType, callback) {
    var args = [this.ref._path, callback._id];
    return new Promise(function(resolve, reject) {
      exec(resolve, reject, PLUGIN_NAME, "off", args);
    });
  },
  orderByChild: function(path) {
    return new DbQuery(this.ref, {
      child: path
    });
  },
  orderByKey: function() {
    return new DbQuery(this.ref, {
      key: true
    });
  },
  orderByPriority: function() {
    return new DbQuery(this.ref, {
      priority: true
    });
  },
  orderByValue: function() {
    return new DbQuery(this.ref, {
      value: true
    });
  },
  keepSynced: function(keepSynced) {
    var args = [this.ref._path, keepSynced];
    return new Promise(function(resolve, reject) {
      exec(resolve, reject, PLUGIN_NAME, "keepSynced", args);
    });
  }
};

function DbRef(path) {
  this._path = path || "/";
}

DbRef.prototype = Object.create(DbQuery.prototype, {
  key: {
    get: function() {
      return this.path.split('/').slice(-1)[0];
    }
  },
  parent: {
    get: parent = function() {
      return this.path.split('/').slice(0, -1).join('/');
    }
  },
  ref: {
    get: function() {
      return this;
    }
  }
});

DbRef.prototype.push = function(value) {
  var args = [this._path, value];
  return new Promise(function(resolve, reject) {
    exec(function(path) {
      resolve(new DbRef(path));
    }, reject, PLUGIN_NAME, "push", args);
  });
};

DbRef.prototype.set = function(value) {
  var args = [this._path, value];
  return new Promise(function(resolve, reject) {
    exec(resolve, reject, PLUGIN_NAME, "set", args);
  });
};

DbRef.prototype.update = function(value) {
  var args = [this._path, value];
  return new Promise(function(resolve, reject) {
    exec(resolve, reject, PLUGIN_NAME, "update", args);
  });
};

DbRef.prototype.remove = function() {
  var args = [this._path];
  return new Promise(function(resolve, reject) {
    exec(resolve, reject, PLUGIN_NAME, "remove", args);
  });
};

DbRef.prototype.child = function(path) {
  return new DbRef(this._path.split("/").concat(path.split("/")).join("/"));
};

DbRef.prototype.setPriority = function(priority) {
  var args = [this._path, null, priority];
  return new Promise(function(resolve, reject) {
    exec(resolve, reject, PLUGIN_NAME, "set", args);
  });
};

DbRef.prototype.setWithPriority = function(value, priority) {
  var args = [this._path, value, priority];
  return new Promise(function(resolve, reject) {
    exec(resolve, reject, PLUGIN_NAME, "set", args);
  });
};

function DbSnapshot(data) {
  this.key = data.key;
  this._data = data;
}

DbSnapshot.prototype = {
  child: function(path) {
    return new DbSnapshot(findChild(String(path).split('/'), this._data.value));
  },
  hasChild: function(path) {
    var child = findChild(String(path).split('/'), this._data.value);
    return child !== null && child !== undefined;
  },
  val: function() {
    return this._data.value;
  },
  exists: function() {
    return !!this._data.value;
  },
  hasChildren: function() {
    return typeof this._data.value === 'object' && !!Object.keys(this._data.value).length;
  },
  numChildren: function() {
    return typeof this._data.value === 'object' ? Object.keys(this._data.value).length : 0;
  }
};

DbSnapshot.prototype[Symbol.iterator] = function() {
  var index = 0;
  var data  = this._data;

  return {
    next: function() {
      return { value: data[++index], done: !(index in data) }
    }
  };
};

function findChild(path, obj) {

  var key = path[0];
  if (!obj) {
    return null;
  } else if (path.length > 1) {
    return findChild(path.slice(1), key.trim() ? obj[key] : obj);
  } else {
    return obj[key];
  }
}


if (typeof module !== undefined && module.exports) {
  module.exports = FirebaseRealtime;
}
