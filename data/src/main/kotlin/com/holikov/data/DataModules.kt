package com.holikov.data

import org.kodein.di.Kodein

val dataModules = Kodein.Module("dataModules", false) {
    import(networkModule)
    import(dbModule)
    import(prefsModule)
    import(repositoryModule)
    import(coroutinesModule)
}

