# Music Lookup

## Intro
An application which exposes Artist information through a REST-API.
The application merges information from MusicBrainz, Wikipedia and 
Cover Art Archive. The application is based on [Spring Boot](http://projects.spring.io/spring-boot/).

## Project Structure

The project is setup as a Gradle multi-project build. 
The project root is located in `src/main/java/com/wooz86/musiclookup/`. 
This package is the base for the application, where you can find the entrypoint for the application (MusiclookupApplication.java)

* `artist` - This is the package which contains the "bounded context" for the artist. It has been developed using a DDD mindset. It contains the layers usually found in a DDD-Application:
    * `domain` - Contains the domain models, domain services and repository interfaces among other things.
    * `infrastructure` - Contains code which is of infrastructural type and does not belong in the domain layer. Examples of such code is the implementation of `ArtistRepository` and the `ArtistService`
    * `interfaces` - This is the part of the application which houses the REST-API but could have other
     
There are also three other folders containing the following:
* `coverartarchive` - Contains the interface and an implementation of that interface for the [Cover Art Archive API](https://musicbrainz.org/doc/Cover_Art_Archive/API). 
* `mediawiki` - Contains the interface and an implementation of that interface for the [MediaWiki API](https://www.mediawiki.org/wiki/API:Main_page). 
* `musicbrainz` - Contains the interface and an implementation of that interface for the [MusicBrainz API](https://musicbrainz.org/doc/Development/JSON_Web_Service).

## Cache
The application uses Spring Context to cache results. The caching is being done in the implementation of ArtistRepository found in `src/main/java/com/wooz86/musiclookup/artist/infrastructure/ArtistRepositoryImpl.java`.
The configuration for the Caching can be found in the CachingConfig bean found in `src/main/java/com/wooz86/musiclookup/CacheConfig.java` 


## Installation

### Requirements
* [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* Optional: [Docker](http://www.docker.com/products/docker)

### Configuration
The application configuration can be found in `src/main/resources/application.properties`.

### Building the application
The application is built using the build tool [Gradle](https://gradle.org/).
Building the application is done simply by running the following command in the project root.
`./gradlew build`
This will generate a `.jar`-file in `build/libs/` named `musiclookup-0.0.1.jar`.

### Running the application
To run the application using Java only, run the following command in the project root.
The first command will build a jar file and the second will run the jar file using Java.
`./gradlew build && java -jar build/libs/musiclookup-0.0.1.jar`

### Docker
The application can be run in a Docker container. Before trying this, make sure you have 
[Docker](http://www.docker.com/products/docker) installed before continuing. Then run the following commands in the
project root.

1. Build the Docker image: `./gradlew build buildDocker`
2. Running a Docker container `docker run -p 8080:8080 -t wooz86/musiclookup`. The container will now be running
in the foreground. To exit, press Ctrl+C.

* To run the container as daemon, add the `-d` flag to the previous command.

### Accessing the REST-API

The application can then be accessed by doing a GET-request to the endpoint `http://localhost:8080/api/v1/artist/{mbid}`.
with `{mbid}` representing a valid UUID, i.e. Metallica, with MBID `65f4f0c5-ef9e-490c-aee3-909e7ae6b2ab`.
The application will take a bit longer for the first request for a new Artist which hasn't been accessed before.
This is because the application is communicating with the third party APIs and merging the information. All results will be cached
and will therefore return a response almost instantly after the first request.

Example response for Metallica:
```
{
  "mbid": "65f4f0c5-ef9e-490c-aee3-909e7ae6b2ab",
  "description": "<p><b>Metallica</b> is an American heavy metal band formed in Los Angeles, California. The band was formed in 1981 when vocalist/guitarist James Hetfield responded to an advertisement posted by drummer Lars Ulrich in a local newspaper. Metallica's current line-up comprises founding members Hetfield and Ulrich, longtime lead guitarist Kirk Hammett and bassist Robert Trujillo. Guitarist Dave Mustaine and bassists Ron McGovney, Cliff Burton and Jason Newsted are former members of the band.</p>\n<p>The band's fast tempos, instrumentals, and aggressive musicianship placed them as one of the founding \"big four\" bands of thrash metal, alongside Anthrax, Megadeth, and Slayer. Metallica earned a growing fan base in the underground music community and won critical acclaim with its first four albums; their third album <i>Master of Puppets</i> (1986) was described as one of the most influential and heaviest of thrash metal albums. The band expanded its musical direction and achieved substantial commercial success with its eponymous fifth album <i>Metallica</i> (1991), which resulted in an album that appealed to a more mainstream audience. The album was also their first to debut at number one on the <i>Billboard</i> 200, a success that they also achieved on their following four studio albums. In 2000, Metallica joined with other artists who filed a lawsuit against Napster for sharing the band's copyright-protected material without consent from the band. A settlement was reached and Napster became a pay-to-use service. The release of <i>St. Anger</i> (2003) alienated fans with the exclusion of guitar solos and the \"steel-sounding\" snare drum, and a film titled <i>Some Kind of Monster</i> documented the recording of <i>St. Anger</i> and the tensions within the band during that time. The band returned to its original musical style with the release of <i>Death Magnetic</i> (2008), and in 2009, Metallica was inducted into the Rock and Roll Hall of Fame.</p>\n<p>Metallica has released nine studio albums, four live albums, five extended plays, 26 music videos, and 37 singles. The band has won eight Grammy Awards and five of its albums have consecutively debuted at number one on the <i>Billboard</i> 200. The band's eponymous 1991 album has sold over 16 million copies in the United States, making it the best-selling album of the SoundScan era. Metallica ranks as one of the most commercially successful bands of all time, having sold over 110 million records worldwide. Metallica has been listed as one of the greatest artists of all time by many magazines, including <i>Rolling Stone</i>, which ranked them 61st on its list of The 100 Greatest Artists of All Time. As of December 2012, Metallica is the third-best-selling music artist since Nielsen SoundScan began tracking sales in 1991, selling a total of 54.26 million albums in the U.S. Metallica collaborated over a long period with producer Bob Rock, who produced four of the band's studio albums between 1990 and 2003 and served as a temporary bassist during the production of <i>St. Anger</i>. In 2012, Metallica formed the independent record label Blackened Recordings and took full ownership of its albums and videos. The band is currently promoting its tenth studio album, <i>Hardwired... to Self-Destruct</i>, which is expected to be released on November 18, 2016.</p>\n<p></p>",
  "albums": [
    {
      "id": "0da580f2-6768-498f-af9d-2becaddf15e0",
      "title": "Ride the Lightning",
      "image": "http://coverartarchive.org/release/589ff96d-0be8-3f82-bdd2-299592e51b40/1847083972.jpg"
    },
    {
      "id": "18c52517-edbd-3bcd-813d-cffebb4ecd12",
      "title": "Garage Days Revisited",
      "image": null
    },
    {
      "id": "2ad9a54b-7d22-4ec6-924f-7b4c0d3d2ce9",
      "title": "The $9.98 C.D. Garage Days Re-Revisited + B Side & Demos",
      "image": "http://coverartarchive.org/release/ed54a819-3cc7-47d8-a209-9ce8abd7f7f9/1812341717.jpg"
    },
    {
      "id": "38d1077e-c270-49a8-92ca-87e7eb8d9fe4",
      "title": "Lulu",
      "image": "http://coverartarchive.org/release/6c514dd9-f7c1-422b-8fcf-cb0aa04ac234/7123246331.jpg"
    },
    {
      "id": "3d00fb45-f8ab-3436-a8e1-b4bfc4d66913",
      "title": "Master of Puppets",
      "image": "http://coverartarchive.org/release/cb100c18-fb29-4828-8d24-2fdbac6d6cff/1847615867.jpg"
    },
    {
      "id": "4c05fe77-d5bd-40f8-aa91-0514aa333b25",
      "title": "Jump in the Fire / Creeping Death / Garage Days Re‐Revisited",
      "image": null
    },
    {
      "id": "55d8ab42-990b-4c94-93c8-9ca7927b5fc0",
      "title": "Hardwired… to Self‐Destruct",
      "image": "http://coverartarchive.org/release/4f8dc580-0c06-43f1-9aaf-ec3031fa738d/14402335791.jpg"
    },
    {
      "id": "5c8a25bf-4764-3cce-8f37-30af79d3b101",
      "title": "Death Magnetic",
      "image": "http://coverartarchive.org/release/940d6fba-3603-4ac2-8f5d-ea0a11e51765/11162781382.jpg"
    },
    {
      "id": "62f3ebd3-5aba-35cd-a7d3-75fd9b966eb6",
      "title": "Garage Days 3",
      "image": null
    },
    {
      "id": "67553e23-8dad-3792-b6f2-8fedd5650ff3",
      "title": "…and Justice for All",
      "image": "http://coverartarchive.org/release/da53e497-8c61-4d4a-a29b-a5b53d86ccb7/7476858115.jpg"
    },
    {
      "id": "6d4b4468-16dc-3e19-91c6-2a394ac99762",
      "title": "Heavy Best",
      "image": null
    },
    {
      "id": "88032b2b-dd3b-36a8-9fe0-de720fb451fd",
      "title": "Ultra Rare Trax",
      "image": null
    },
    {
      "id": "8f1cc89b-7e80-3e1c-b571-8cf2b98db347",
      "title": "St. Anger",
      "image": "http://coverartarchive.org/release/5997cf87-5225-4830-9edf-358638a24905/11163378974.jpg"
    },
    {
      "id": "8fd32554-02a7-3788-a761-7012e0e75e55",
      "title": "Reload",
      "image": "http://coverartarchive.org/release/64c581c6-7270-3f3a-8dd2-3fb14a41d679/1751609374.jpg"
    },
    {
      "id": "9c7f657f-c21b-352c-bf7c-665120ba42a1",
      "title": "The $5.98 E.P.: Garage Days Re‐Revisited …and More",
      "image": "http://coverartarchive.org/release/046fc4e3-d3b4-4dd9-98bd-8eb6ef9c74b8/4797113741.jpg"
    },
    {
      "id": "b9ebd6d5-3256-38fe-a455-222ca403472a",
      "title": "The Other Side Of...",
      "image": null
    },
    {
      "id": "c68f2af8-cf49-357e-bcce-5da72fae4756",
      "title": "Covering 'em",
      "image": "http://coverartarchive.org/release/5e5f6a3f-d519-475f-bd6c-b302690c3a9f/5576625018.jpg"
    },
    {
      "id": "d2cbcb28-b8bc-313a-8e01-9392c6af44c6",
      "title": "Secret Demos",
      "image": null
    },
    {
      "id": "e160fae5-075f-3917-b991-39ffc952f380",
      "title": "Tales From the Cliff",
      "image": null
    },
    {
      "id": "e389b7df-862d-3d91-a612-acca150f6e71",
      "title": "Load",
      "image": "http://coverartarchive.org/release/d0edc24b-f201-3076-b015-e54401239a8f/4462141147.jpg"
    },
    {
      "id": "e683880a-b1d7-3599-9f70-680ac56d8667",
      "title": "Garage Inc.",
      "image": "http://coverartarchive.org/release/7f5a52aa-4429-4771-80ec-6c6a545b0df9/11162732155.jpg"
    },
    {
      "id": "e8f70201-8899-3f0c-9e07-5d6495bc8046",
      "title": "Metallica",
      "image": "http://coverartarchive.org/release/69a8ca83-a182-3375-a702-a30e216748c9/13607879014.jpg"
    },
    {
      "id": "ea0612d0-f425-3d20-b124-66c0a4862643",
      "title": "New Skulls for the Old Ceremony",
      "image": "http://coverartarchive.org/release/6cb60625-fa55-4594-90e8-c9f53437c907/1288562726.jpg"
    },
    {
      "id": "f44f4f73-a714-31a1-a4b8-bfcaaf311f50",
      "title": "Kill ’em All",
      "image": "http://coverartarchive.org/release/a89e1d92-5381-4dab-ba51-733137d0e431/1288556796.jpg"
    },
    {
      "id": "f9922478-a12c-3745-97e9-1fd37fe45e7b",
      "title": "Garage Days, Part II",
      "image": "http://coverartarchive.org/release/7cd35876-baa9-48a6-9c22-5daebab8779c/9787184902.jpg"
    }
  ]
}
```

# TODO
* Unit Tests
* Integration tests for API:s
* Improve async requests
* Domain model clean up
* Logging
* Implement a backing store for Cache (done in-memory as of now).