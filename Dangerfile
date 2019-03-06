# Sometimes it's a README fix, or something like that - which isn't relevant for
# including in a project's CHANGELOG for example
declared_trivial = github.pr_title.include? "#trivial"

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

####
# Android Lint
####
android_lint.gradle_task = "conveyor:lintDebug"
android_lint.report_file = "conveyor/build/reports/lint-results-debug.xml"
android_lint.lint