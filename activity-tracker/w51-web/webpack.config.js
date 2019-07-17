const path = require('path');

module.exports = {
	entry: path.resolve(__dirname, './src/index.js'),
	output: {
		path: path.resolve(__dirname, './public/js'),
		filename: 'app.js'
	},
	module : {
		loaders: [
			{ 
				test: /\.js$/,
				exclude: /node_modules/,
				loader: 'babel-loader',
				query: {
					presets: [ 'es2015', 'stage-2', 'react' ]
				}
			}
		]
	}
};
