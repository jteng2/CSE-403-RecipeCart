var HTMLWebpackPlugin = require('html-webpack-plugin');
var webpack = require('webpack');
var HTMLWebpackPluginConfig = new HTMLWebpackPlugin({
    template: __dirname + '/app/index.html',
    filename: 'index.html',
    inject: 'body'
});
module.exports = {
    entry: __dirname + '/app/index.js',
    mode: 'development',
    performance: {
        hints: false,
        maxEntrypointSize: 512000,
        maxAssetSize: 512000
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader'
            },
            {
                test: /\.(png|jpe?g|gif)$/i,
                use: [
                  {
                    loader: 'file-loader',
                    options: {
                      name: '[path][name].[ext]',
                    },
                  },
                ],
            },
            {
                test: /\.css$/,
                use: [
                  "style-loader",
                  "css-loader"
                ]
            }
        ]
    },
    devServer: {
        historyApiFallback: true,
        allowedHosts: "all",
    },
    output: {
        filename: 'transformed.js',
        path: __dirname + '/build',
        publicPath: "/" 
    },
    plugins: [HTMLWebpackPluginConfig]
};
