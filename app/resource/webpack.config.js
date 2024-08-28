const path = require('path');

module.exports = {
  entry: {
    index: './src/main/resources/static/js/entry/indexEntry.js',
    notebook: './src/main/resources/static/js/entry/notebookEntry.js',
    note: './src/main/resources/static/js/entry/noteEntry.js'
  },
  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, 'src/main/resources/static/js/dist'),
    clean: true,
  },
  resolve: {
    alias: {
      '@indexJS': path.resolve(__dirname, 'src/main/resources/static/js/index'),
      '@notebookJS': path.resolve(__dirname, 'src/main/resources/static/js/notebook'),
      '@noteJS': path.resolve(__dirname, 'src/main/resources/static/js/note'),
      '@unityJS': path.resolve(__dirname, 'src/main/resources/static/js/unity'),
      '@webSocketJS': path.resolve(__dirname, 'src/main/resources/static/js/websocket'),
    }
  },
  module: {
    rules: [
      {
        test: /\.css$/i,
        use: ['style-loader', 'css-loader'],
      },
    ],
  },
  mode: 'development',
};