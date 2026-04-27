buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
    implementation("androidx.drawerlayout:drawerlayout:1.1.1") // Add this line
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
