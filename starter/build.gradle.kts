plugins { id("com.eventslooped.klyph") version "1.2.0" }
klyph { entryPoint = "gemminer.main" }
kotlin { sourceSets.nativeMain.dependencies { implementation("com.eventslooped.klyph:klyph:1.2.0") } }
