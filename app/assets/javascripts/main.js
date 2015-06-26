(function (requirejs) {
    'use strict';

    // -- RequireJS config --
    requirejs.config({
        // Packages = top-level folders; loads a contained file named 'main.js"
        packages: [],
        shim: {
            // Hopefully this all will not be necessary but can be fetched from WebJars in the future
            'angular': {
                deps: ['jquery'],
                exports: 'angular'
            },
            'foundation': ['jquery'],

            'application': {
                deps: ['jquery']
            }
        },
        paths: {
            'requirejs': ['../lib/requirejs/require'],
            'jquery': ['../lib/jquery/jquery'],
            'modernizr': ['../lib/modernizr/modernizr'],
            'fastclick': ['../lib/fastclick/fastclick'],
            'angular': ['../lib/angularjs/angular'],
            'foundation': ['../lib/foundation/js/foundation.min'],
            'application': ['./application']
        }
    });

    requirejs.onError = function (err) {
        console.log(err);
    };

    // Load the app. This is kept minimal so it doesn't need much updating.
    require(['angular', 'jquery', 'foundation', 'application'], function(angular) {
        $(document).ready(function () {
            $(document).foundation();
        });
    });

})(requirejs);