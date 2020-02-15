const HtmlWebPackPlugin = require("html-webpack-plugin");
const path = require('path');

module.exports = {
    output: {
        filename: 'main.js',
        path: path.resolve(__dirname, 'dist/'),
        publicPath: '/',
    },
    devServer:{
        contentBase: './dist',
        historyApiFallback: true
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader"
                }
            },
            {
                test: /\.html$/,
                use: [
                    {
                        loader: "html-loader"
                    }
                ]
            },
            {
                test: /\.css$/,
                use: [
                    {
                    loader: "css-loader"
                }
                ]
            }
        ]
    },
    node: {
        fs: 'empty'
    },
    plugins: [
        new HtmlWebPackPlugin({
            template: path.resolve(__dirname, "html/index.html"),
        })
    ]
};
