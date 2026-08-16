
plugins {
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
}

ktlint {
    android.set(true)
    ignoreFailures.set(false)
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    // Обращаемся к корню основного проекта, а не модуля плагина
    config.setFrom(files("${rootProject.projectDir}/config/detekt/detekt.yml"))
}