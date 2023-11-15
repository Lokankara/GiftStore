module.exports = function(config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', 'browserify', 'webpack'],
    files: [
      'src/**/*.ts',
      'src/**/*.spec.ts',
    ],
    flags: [
      '--disable-gpu',
      '--no-sandbox'
    ],
    exclude: [ ],
    preprocessors: {
      'src/**/*.spec.ts': ['webpack'],
      'src/**/*.ts': ['webpack']
    },
    plugins: [
        require('karma-webpack'),
        require('karma-jasmine'),
        require('karma-chrome-launcher'),
        require('karma-spec-reporter'),
        require('karma-jasmine-html-reporter'),
        require('karma-coverage'),
        require('karma-browserify')
    ],
    reporters: ['spec','coverage'],
    port: 9876,
    colors: true,
    logLevel: config.LOG_DISABLE,
    autoWatch: true,
    browsers: ['Chrome'],
    client: {
       clearContext: false
    },
    singleRun: false,
    concurrency: Infinity,
    browserNoActivityTimeout: 20000,
  })
}
