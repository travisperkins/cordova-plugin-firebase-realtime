# cordova-plugin-firebase-realtime

> Cordova plugin for [Firebase Realtime Database](https://firebase.google.com/docs/database/)

## Installation

    cordova plugin add https://github.com/travisperkins/cordova-plugin-firebase-realtime --save

or

    phonegap plugin add https://github.com/travisperkins/cordova-plugin-firebase-realtime

# Proxy

If running behind a proxy it may be necessary to clone the above repo and use

    phonegap plugin add /path/to/cloned/repo

## Supported Platforms

- Android
- Browser

## Initialisation

To initialise the plugin use the following:

    var config;

    realTimeDatabase = new cordova.plugins.firebase.realtime(config);
    realTimeDatabase.initialise().then(< do your stuff>);

# initialise()

As can be seen above, this returns a promise once the database has been
successfully initialised.

From thereon access the database via the realTimeDatabase variable.

# Browser nuances

When using in browser, supply the firebase app details:

    if (cordova.platformId === 'browser') {
      config = {
          apiKey: "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
          authDomain: "xxxxxxxxx.firebaseapp.com",
          databaseURL: "https://xxxxxxxxx.firebaseio.com",
          projectId: "xxxxxxxxx",
          storageBucket: "",
          messagingSenderId: "99999999999999"
      };
    }

## API

The API strives to mimic the Web API as closely as possible although not all
features are supported.

    https://firebase.google.com/docs/database/web/start
    https://firebase.google.com/docs/reference/js/firebase.database

## What works

- Fetching and listening for data
- Ordering and limiting of queries
- Setting and updating
- Local persistence
- Local synchronisation
- Possibly more

## Versions

# 0.0.2
- Added ability to specify Firebase version

# 0.0.1
- Initial release
