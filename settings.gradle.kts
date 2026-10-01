pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
            maven {
                url = uri("https://jitpack.io")

                url = uri("<path-to>/samiksha/build/repo")


            }

        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("libs")
        }

        maven { url = uri("https://jitpack.io")

        }

    }
}

rootProject.name = "Kaushal Panjee"
include(":app")
