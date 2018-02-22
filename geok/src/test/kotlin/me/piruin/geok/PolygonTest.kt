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

    @Test
    fun centroid() {
        val boundary = Polygon(
          (98.3432525117 to 7.82002427060229),
          (98.3434401322 to 7.81997546731602),
          (98.3436993774 to 7.81984612009375),
          (98.3438997787 to 7.81976395641512),
          (98.3443444709 to 7.819589152106),
          (98.3444144772 to 7.81956097493145),
          (98.3444239906 to 7.81952327705373),
          (98.3443940576 to 7.8193176915304),
          (98.344314997 to 7.8190894007154),
          (98.3443018769 to 7.81901584006111),
          (98.3442867794 to 7.81899695939897),
          (98.343921432 to 7.81911263611518),
          (98.3437296833 to 7.81920436671021),
          (98.3434939411 to 7.81929517678774),
          (98.3433884209 to 7.81931254759861),
          (98.3431356439 to 7.81905382113133),
          (98.3429054628 to 7.81933634314872),
          (98.3428266146 to 7.81933344001763),
          (98.3428941219 to 7.81945102277898),
          (98.3429116108 to 7.81951066351522),
          (98.3429473824 to 7.8196843045481),
          (98.3430250791 to 7.82003262732049),
          (98.3432525117 to 7.82002427060229)
        )

        boundary.centroid.toUtm() `should equal` Utm(47, 'N', 427634.2, 864397.6)
    }
}
