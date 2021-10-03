# GEOK
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit&logoColor=white)](https://github.com/pre-commit/pre-commit)
[![build](https://github.com/piruin/geok/actions/workflows/build.yml/badge.svg)](https://github.com/piruin/geok/actions/workflows/build.yml)
[![Download](https://jitpack.io/v/piruin/geok.svg)](https://jitpack.io/#piruin/geok)
![Maven Central](https://img.shields.io/maven-central/v/io.github.piruin/geok)

Small geometry library for Java and Kotlin. Contains useful basic utilities that require on most application. Designed
to support data exchange between client (such as Android) and Restful api server with [GeoJSON](http://geojson.org/)
Spec

## Download

<details>
<summary>MavenCentral</summary>

```groovy
def geokVersion = '1.2.0' //see download badge or latest released tag

repositories {
  mavenCentral()
}

// For multiplatform projects
kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation "io.github.piruin:geok:$geokVersion"
      }
    }
  }
}

dependencies {
  // For JVM or JS
  implementation "io.github.piruin:geok:$geokVersion"
  // For JVM only
  implementation "io.github.piruin:geok-gson:$geokVersion" // for work with `gson` library
}
```

</details>
<details>
<summary>Jitpack</summary>

```groovy
def geokVersion = '1.2.0' //see download badge or latest released tag

repositories {
  maven { url 'https://jitpack.io' }
}

// For multiplatform projects
kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation "com.github.piruin.geok:geok:$geokVersion"
      }
    }
  }
}

dependencies {
  // For JVM or JS
  implementation "com.github.piruin.geok:geok:$geokVersion"
  // For JVM only
  implementation "com.github.piruin.geok:geok-gson:$geokVersion" // for work with `gson` library
}
```

</details>

## Usage

### LatLng

LatLng is base class of this library to build up any Geometry you want. It come with utilities such as

- `toUtm()` -- convert to Utm type that widely use on GPS handheld system (also support create Latlng form Utm)
- `distanceTo(latlng: LatLng)` -- calculate distance between 2 Latlng in meter

### Geometry

List of Supported Geometry type
- `Point`
- `LineString`
- `Polygon`
- `MultiPoint`
- `MultiLineString`
- `MultiPolygon`

#### Point

```kotlin
  val point = Point(100.0 to 0.0)
```
- `distanceTo(latlng: Point)` -- calculate distance between 2 Point in meter
- `insideOf(polygon: Polygon)` -- Determines whether this LatLng are inside of given Polygon.

#### LineString

```kotlin
  val lineString = LineString(
      100.0 to 0.0,
      101.0 to 1.0
  )
```
LineString come with
- `distance` -- calculate distance of LineString in meter

#### Polygon

```kotlin
    val polygon = Polygon(
            LatLng(16.4268129901041, 102.8380009059),
            LatLng(16.4266819930293, 102.8379568936),
            LatLng(16.4267047695460, 102.8378494011),
            LatLng(16.4268502721458, 102.8378330329),
            LatLng(16.4268937418855, 102.8378020293),
            LatLng(16.4268129901041, 102.8380009059)
    )
```

Polygon come with utilities such as
- `area` -- calculate area of polygon in sq.meter
- `centroid` -- calculate centroid point of Polygon
- `perimeter` -- calculate perimeter of Polygon's boundary in meter
- `contains(latlng: LatLng)` -- check whether latlng is in bound of polygon
- `intersactionWith(other: Polygon)` -- get intersection with other polygon

> Object creation support both LatLng styles and pair of X, Y value

### BBox

Every geometry except `Point` automatically generate `BBox` on create instance.
You can also create your BBox's instance with

```kotlin
  val bbox = BBox.from(listOf<LatLng>())
```

### GeoJson

All `Geometry` of GEOK is designed to fully support GeoJSON,
You can easily use [Gson](https://github.com/google/gson) to serialize/deserialize them with `geok-gson` module

```kotlin
  private val gson: Gson = GsonBuilder()
            .registerGeokTypeAdapter() // call this
            .create()
```

#### Feature

Follow GeoJSON specification, You can use `Feature` to contain extra properties along with Geometry data.

```kotlin
 val feature = Feature(Polygon(
                100.0 to 0.0,
                101.0 to 0.0,
                101.0 to 1.0,
                100.0 to 1.0,
                100.0 to 0.0), Properties())
```

And you may pack many `Feature` together with `FeatureCollection`

## License

    Copyright (c) 2018-2021 Piruin Panichphol

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

- [Convert Between Geographic and UTM Coordinates](http://www.uwgb.edu/dutchs/UsefulData/ConvertUTMNoOZ.HTM)
  by [Professor Steven Dutch](http://www.uwgb.edu/dutchs/index.htm)
- [Intersection of Convex Polygons Algorithm](https://www.swtestacademy.com/intersection-convex-polygons-algorithm/)
  by [Sinan Oz](https://www.swtestacademy.com/author/sinanoz/)
