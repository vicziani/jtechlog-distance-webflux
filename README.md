# Bevezetés a reaktív programozásba Spring Boottal

## Szükséges előfeltételek

* Internet elérés
* Java 13: https://www.oracle.com/technetwork/java/javase/downloads/jdk13-downloads-5672538.html
* IntelliJ IDEA Community
* Lombok plugin, enable annotation processing

## Projekt létrehozásának lépései

* A https://start.spring.io/ oldalon válasszuk a Java 13-at (_Options_), valamint a következő függőségeket (_Dependencies_):
    * Spring Reactive Web
    * H2 Database
    * Spring Data R2DBC [Experimental]
    * Lombok
* A `pom.xml`-ben a `com.h2database:h2` verziójának `1.4.199`-re állítása
* A `pom.xml`-ben a `com.h2database:h2` és `io.r2dbc:r2dbc-h2` artifact 
* `cities.csv` állomány elhelyezése az `src/main/resources` könyvtárban

https://howtodoinjava.com/spring-webflux/spring-webflux-tutorial/