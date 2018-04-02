# GEOK
[![Build Status](https://travis-ci.org/piruin/geok.svg?branch=master)](https://travis-ci.org/piruin/geok)
[![Download](https://api.bintray.com/packages/blazei/maven/geok-gson/images/download.svg) ](https://bintray.com/blazei/maven/geok-gson/_latestVersion)

Small geometry library for Java and Kotlin

## Download

```groovy
repositories {
    maven { url  "https://dl.bintray.com/blazei/maven" }
}

dependencies {
    def geokVersion = '1.+' //see download badge or latest released tag
    
    ...
    implementation "me.piruin:geok:$geokVersion"
    implementation "me.piruin.geok-gson:$geokVersion" //for support geojson with gson library
    ...
}
```

## License

    Copyright (c) 2018 Piruin Panichphol
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

### Notice
[Convert Between Geographic and UTM Coordinates](http://www.uwgb.edu/dutchs/UsefulData/ConvertUTMNoOZ.HTM) by [Professor Steven Dutch](http://www.uwgb.edu/dutchs/index.htm)
