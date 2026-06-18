plugins {
    id("dev.prism")
}

group = "com.leclowndu93150"
version = "2.1.1"

prism {
    metadata {
        modId = "duradisplay"
        name = "DurabilityDisplay"
        description = "Displays item durability and energy levels as a percentage overlay."
        license = "MIT"
        author("Leclowndu93150")
    }

    curseMaven()
    maven("FabricMC", "https://maven.fabricmc.net/")

    publishing {
        changelogFile = "CHANGELOG.md"

        curseforge {
            accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
            projectId = "1009842"
        }

        modrinth {
            accessToken = providers.environmentVariable("MODRINTH_TOKEN")
            projectId = "hjotWeH6"
        }

        github {
            accessToken = providers.environmentVariable("GITHUB_TOKEN")
            repository = "Leclowndu93150/DuraDisplayMultiloader"
        }
    }

    version("1.20.1") {
        fabric {
            loaderVersion = "0.19.3"
            fabricApi("0.92.9+1.20.1")

            dependencies {
                modCompileOnly("teamreborn:energy:3.0.0")
            }

            publishingDependencies {
                requires("fabric-api")
            }
        }
        forge {
            loaderVersion = "47.4.10"

            dependencies {
                modCompileOnly("curse.maven:gregtechceu-modern-890405:7917773")
                modCompileOnly("curse.maven:ldlib-626676:7809449")
            }

            publishingDependencies {
                curseforge {
                    optional("gregtechceu-modern")
                }
                modrinth {
                    optional("gregtechceu-modern")
                }
            }
        }
    }

    version("1.21.1") {
        fabric {
            loaderVersion = "0.19.3"
            fabricApi("0.116.12+1.21.1")

            dependencies {
                modCompileOnly("teamreborn:energy:4.2.0")
            }

            publishingDependencies {
                requires("fabric-api")
            }
        }
        neoforge {
            loaderVersion = "21.1.228"

            dependencies {
                modCompileOnly("curse.maven:gregtechceu-modern-890405:6792853")
                modCompileOnly("curse.maven:ldlib-626676:5578399")
            }

            publishingDependencies {
                curseforge {
                    optional("gregtechceu-modern")
                }
                modrinth {
                    optional("gregtechceu-modern")
                }
            }
        }
    }

    version("26.1.2") {
        fabric {
            loaderVersion = "0.19.3"
            fabricApi("0.152.1+26.1.2")

            dependencies {
                modCompileOnly("teamreborn:energy:5.0.0")
            }

            publishingDependencies {
                requires("fabric-api")
            }
        }
        neoforge {
            loaderVersion = "26.1.2.76"
        }
    }

    version("26.2") {
        fabric {
            loaderVersion = "0.19.3"
            fabricApi("0.152.2+26.2")

            dependencies {
                modCompileOnly("teamreborn:energy:5.0.0")
            }

            publishingDependencies {
                requires("fabric-api")
            }
        }
        neoforge {
            loaderVersion = "26.2.0.3-beta"
        }
    }
}
