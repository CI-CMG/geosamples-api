# geosamples-api

a simple RESTful API for read-only access to the Index to Marine and Lacustrine Geological Samples (IMLGS) database.

Represents the data access component for the replacement of the legacy [geosamples](https://www.ngdc.noaa.gov/geosamples/) JSP applcation

Resources exposed via the API include:
* Geologic Age
* Cruise
* IGSN
* Interval (along a core sample)
* Lake
* Platform
* Physiographic Province
* Repository (aka Facility)
* Sample
* Storage Method
* Texture
* Mineralogy
* Lithology
* Weathering
* Metamorphism

See [notes](https://www.ngdc.noaa.gov/mgg/curator/curatorcoding.html) 
on the database fields underlying the API 