# Stock Hawk
> Stock Hawk is an Android application that displays current stock prices and price changes.

This application is part of the Udacity Android Developer Nanodegree. The target of this project is taking it from a functional state to a production-ready app. The main screen list stock prices, which can me added new stocks using a fab button. A user can see detailed chart, taping in one stock. Another useful feature is a widget for displaying the stocks.

For syncronzing data the Android Content Provider pattern was used. The main libs used are:
* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) for generating the charts
* [okhttp](https://github.com/square/okhttp) as http/https client

## Install
I recommend you use [Android Studio](https://developer.android.com/studio/index.html) because the development is easier thant command line.

For generate the apk using command line use this command: `./gradlew assembleDebug`

## Screenshot
<img src="/screenshots/device-2017-12-10-133441.png" width="280" height="500">

For generate the apk using command line use this command: `./gradlew assembleDebug`

## Meta
Alexandre Navarro – [@navarro_alex](https://twitter.com/navarro_alex) – alexandrenavarro@gmail.com

Distributed under the MIT license. See [LICENSE](LICENSE) for more information.

[https://github.com/alexnavarro](https://github.com/alexnavarro)

1. Fork it (<https://github.com/alexnavarro/StockHawk#fork-destination-box>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

Some other ways you can contribute:
* by suggesting new features
* by writing specifications
* by writing code ( **no patch is too small** : fix typos, add comments, clean up inconsistent whitespace)
* by refactoring code
