# fast logger with file output

[![GitHub license](https://img.shields.io/badge/license-Apache%20Version%202.0-blue.svg)](https://github.com/sbrukhanda/fragmentviewpager/blob/master/LICENSE.txt)
[![Build Status](https://travis-ci.org/hannesa2/loggerLib.svg?branch=master)](https://travis-ci.org/hannesa2/loggerLib)
[![](https://jitpack.io/v/hannesa2/loggerLib.svg)](https://jitpack.io/#hannesa2/loggerLib)

A fast logger with optional output to file

### Usage

##### Set output file 

###### Kotlin
```Kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        L.Builder(BuildConfig.DEBUG)
                .changeLogFileName("myfilenameAnywhereInFileSystem.log")
                .setLogToFileEnabled(true, this)
                .build()
    }
}
```

##### Use logger everywhere
```Kotlin
    L.d("some text")
    L.w("some text")
    L.e("some text")
 ```   

That's it !

## Download 
Repository available on https://jitpack.io

```Gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
```Gradle
dependencies {
    implementation 'com.github.hannesa2:loggerLib:1.2' 
}

```

## License 
```
Copyright 2018 Hannes Achleitner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


