plugins { id("com.eventslooped.klyph") version "0.1.0" }
klyph { entryPoint = "gemminer.main" }
kotlin { sourceSets.nativeMain.dependencies { implementation("com.eventslooped.klyph:klyph:0.1.0") } }
