const path = require('path');

module.exports = {
  entry: {
    notebookPage: './src/main/resources/static/js/entry/notebookEntry.js',
    notePage: './src/main/resources/static/js/entry/noteEntry.js'
  },
  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, 'src/main/resources/static/js/dist'),
    clean: true,
  },
  resolve: {
    alias: {
      '@components': path.resolve(__dirname, 'src/main/resources/static/js/component')
    }
  },
  mode: 'development',
};