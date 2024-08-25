import scala.collection.mutable._
import sttp.client4.quick.*
import sttp.model.Uri

type Vehicles = ArrayBuffer[ujson.Value]

def makeRequest(url: Uri): ujson.Value = {
  val response = quickRequest
    .get(url)
    .send()

  val json = ujson.read(response.body)

  return json
}

def getVehicles(): Vehicles = {
  var url = uri"https://swapi.dev/api/vehicles/"
  val vehicles: Vehicles = ArrayBuffer()

  while (url != null) {
    val response = makeRequest(url)
    vehicles ++= response("results").arr

    if (response("next").isNull) {
      return vehicles
    }
    val next = response("next").str
    url = uri"$next"
  }

  return vehicles
}

def cleanVehicles(vehicles: Vehicles): Vehicles = {
  return vehicles.filterNot(v => v("max_atmosphering_speed").str == "unknown")
}

def sortVehicles(vehicles: Vehicles): Vehicles = {
  val cleanedVehicles: Vehicles = cleanVehicles(vehicles)
  val sortedVehicles =
    cleanedVehicles.sortWith(
      _("max_atmosphering_speed").str.toDouble < _(
        "max_atmosphering_speed"
      ).str.toDouble
    )

  return sortedVehicles
}

def displayVehicles(vehicles: Vehicles) = {
  println()
  vehicles.foreach(v =>
    println(v("name").str + ": " + v("max_atmosphering_speed").str)
  )
  println()
}

@main def hello(): Unit =

  val vehicles = getVehicles()

  println("Sorting vehicles..")

  val sortedVehicles = sortVehicles(vehicles)

  val fastestVehicle = sortedVehicles(sortedVehicles.length - 1)
  val secondFastestVehicle = sortedVehicles(sortedVehicles.length - 2)
  val thirdFastestVehicle = sortedVehicles(sortedVehicles.length - 3)
  val fastVehicles =
    ArrayBuffer(fastestVehicle, secondFastestVehicle, thirdFastestVehicle)

  displayVehicles(fastVehicles)
