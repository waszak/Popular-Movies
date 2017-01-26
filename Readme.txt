API Key location to fill
MyApplication/app/build.gradle

EXAMPLE:
 buildTypes {
        release {
            resValue "string", "api_key_the_movie_db", "<Api_Key>"
        }
        debug {

            resValue "string", "api_key_the_movie_db", "<Api_Key>"
        }