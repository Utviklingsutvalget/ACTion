'use strict';
var gulp = require('gulp');
var concat = require('gulp-concat');
var jshint = require('gulp-jshint');
var cached = require('gulp-cached');
var remember = require('gulp-remember');
var sass = require('gulp-sass');

gulp.task('default', ['scripts', 'sass', 'watch']);

var sources = [
    'node_modules/jquery/dist/jquery.min.js',
    'node_modules/foundation-sites/js/foundation.min.js',
    'node_modules/angular/angular.min.js',

    // Internal sources last
    'app/assets/javascripts/**/*.js'
];

var styles = [
    // Internal sources last
    'app/assets/stylesheets/main.scss'
];

var extraStyles = [
    'app/assets/stylesheets/extras/**/*.scss'
];

var scriptsDest = 'target/web/public/main/javascripts/';
var stylesDest = 'target/web/public/main/stylesheets/';

gulp.task('sass', function () {
    gulp.src(styles)
        .pipe(sass())
        .pipe(gulp.dest(stylesDest))
        .pipe(concat("main.css"))
        .pipe(gulp.dest(stylesDest));
});

gulp.task('scripts', function () {
    return gulp.src(sources)
        .pipe(cached('scripts'))        // only pass through changed files
        .pipe(jshint())                 // do special things to the changed files...
        .pipe(remember('scripts'))      // add back all files to the stream
        .pipe(concat('main.js'))         // do things that require all files
        .pipe(gulp.dest(scriptsDest));
});

gulp.task('watch', ['scripts', 'sass'], function () {
    var scriptWatcher = gulp.watch(sources, ['scripts']);

    scriptWatcher.on('change', function (event) {
        if (event.type === 'deleted') {
            delete cached.caches.scripts[event.path];
            remember.forget('scripts', event.path);
        }
    });

    var styleWatcher = gulp.watch([styles, extraStyles], ['sass']);

    styleWatcher.on('change', function (event) {
        if (event.type === 'deleted') {
            delete cached.caches.sass[event.path];
            remember.forget('sass', event.path);
        }
    });
});
