package me.piruin.geok

import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.Test
import kotlin.math.roundToInt

class PolygonTest {

  val polygon = Polygon(
      LatLng(16.4268129901041, 102.8380009059),
      LatLng(16.4266819930293, 102.8379568936),
      LatLng(16.4267047695460, 102.8378494011),
      LatLng(16.4268502721458, 102.8378330329),
      LatLng(16.4268937418855, 102.8378020293),
      LatLng(16.4268129901041, 102.8380009059)
  )

  @Test
  fun contain() {
    polygon.contain(LatLng(16.4268129901041, 102.8380009059)) `should be` true
    polygon.contain(LatLng(16.4268502721458, 102.8378330329)) `should be` true
  }

  @Test
  fun isClose() {
    polygon.isClosed `should be` true
  }

  @Test
  fun area() {
    polygon.area().roundToInt() `should equal` 268
  }
}
