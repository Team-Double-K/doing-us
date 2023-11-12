package com.kim.hackathon.config

class LocationManger {
    companion object{
        var longitude = 0.0
        var latitude = 0.0
        fun setPosition(longitude:Double, latitude:Double){
            this.longitude = longitude
            this.latitude = latitude
        }
        fun getPosition():List<Double>{
            var rtnData:List<Double> = listOf(this.longitude,this.latitude)

            return rtnData

        }

    }

}