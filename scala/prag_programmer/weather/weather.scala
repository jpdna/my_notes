import scala.io.Source
import scala.xml._
import java.net.URLEncoder;


def getWeatherData(city: String) = {
	val url = "http://api.openweathermap.org/data/2.5/weather"

    val city_safe_url = URLEncoder.encode(city, "UTF-8")

//	val response = Source.fromURL(s"$url?q=$city&units=imperial&mode=xml&APPID=69a05814b9eff9b07bd97e10c72f90b5")
val response = Source.fromURL(s"$url?q=$city_safe_url&units=imperial&mode=xml&APPID=69a05814b9eff9b07bd97e10c72f90b5")

	val xmlResponse = XML.loadString(response.mkString)

    val cityName = (xmlResponse \\ "city" \ "@name").text
    val temperature = (xmlResponse \\ "temperature" \ "@value").text
    val condition = (xmlResponse \\ "weather" \ "@value").text
 	
    (cityName, temperature, condition)
 	
}	

def printWeatherData(weatherData: (String, String, String)) = {
	val (cityName, temperature, condition) = weatherData
	println(f"$cityName%-15s $temperature%-6s $condition")
}


//69a05814b9eff9b07bd97e10c72f90b5

def timeSample(getData: List[String] => List[(String, String, String)]) = {
    val filename = "/home/paschall/Downloads/city_list.1000.txt"
    val cities = Source.fromFile(filename).getLines.map(_.split("\t")).map(line => line(1) + "," + line(4)).toList

    val start = System.nanoTime
    getData(cities) sortBy { _._1 } foreach printWeatherData
    val end = System.nanoTime
 	
    println(s"Time taken: ${(end - start)/1.0e9} sec")
}



timeSample { cities => (cities.par map getWeatherData).toList }
//timeSample { cities => cities map getWeatherData }