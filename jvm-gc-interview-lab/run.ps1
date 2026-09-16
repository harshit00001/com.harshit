<#
    Runs one lab step with the JVM flags that make its behaviour visible.

    Usage:
      .\run.ps1                 # list the steps
      .\run.ps1 step02          # young GC / Eden demo
      .\run.ps1 step07 leak     # leak mode
      .\run.ps1 step07 churn    # allocation-pressure mode
      .\run.ps1 step10 -Collector serial   # same workload under a different collector
      .\run.ps1 step12 -NoEscapeAnalysis   # disable scalar replacement to see the difference
#>
param(
    [string]$Step = "--list",
    [string]$Mode = "",
    [ValidateSet("", "serial", "parallel", "g1", "zgc")]
    [string]$Collector = "",
    [switch]$NoEscapeAnalysis,
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"
$projectRoot = $PSScriptRoot
$classes = Join-Path $projectRoot "target\classes"

# JDK 17 is not on PATH on this machine; point JAVA_HOME at it explicitly.
if (-not $env:JAVA_HOME -or -not (Test-Path (Join-Path $env:JAVA_HOME "bin\java.exe"))) {
    $candidate = "C:\Users\harshraj\OneDrive - AMDOCS\Bell Canada\Java"
    if (Test-Path (Join-Path $candidate "bin\java.exe")) { $env:JAVA_HOME = $candidate }
}
$java = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME "bin\java.exe" } else { "java" }

if (-not $SkipBuild) {
    Push-Location $projectRoot
    try { & mvn -q -B compile } finally { Pop-Location }
}

# Per-step flags: heap deliberately small so collections happen within seconds.
$flags = switch ($Step) {
    "step01" { @("-Xms256m", "-Xmx256m") }
    "step02" { @("-Xms64m", "-Xmx64m", "-Xlog:gc") }
    "step03" { @("-Xms64m", "-Xmx64m", "-Xlog:gc") }
    "step04" { @("-Xms128m", "-Xmx128m", "-Xlog:gc", "-Xlog:gc+age=trace") }
    "step05" { @("-Xms128m", "-Xmx128m", "-Xlog:gc") }
    "step06" { @("-Xms64m", "-Xmx64m") }
    "step07" { @("-Xms96m", "-Xmx96m", "-Xlog:gc", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=$projectRoot\dumps") }
    "step08" { @("-Xms128m", "-Xmx128m") }
    "step09" { @("-Xms256m", "-Xmx256m", "-XX:G1HeapRegionSize=1m", "-Xlog:gc") }
    "step10" { @("-Xms256m", "-Xmx256m", "-Xlog:gc") }
    "step11" { @("-Xms64m", "-Xmx64m", "-XX:MaxDirectMemorySize=64m", "-XX:NativeMemoryTracking=summary") }
    "step12" { @("-Xms128m", "-Xmx128m") }
    default  { @() }
}

switch ($Collector) {
    "serial"   { $flags += "-XX:+UseSerialGC" }
    "parallel" { $flags += "-XX:+UseParallelGC" }
    "g1"       { $flags += "-XX:+UseG1GC" }
    "zgc"      { $flags += @("-XX:+UseZGC", "-XX:+ZGenerational") }
}
if ($NoEscapeAnalysis) { $flags += "-XX:-DoEscapeAnalysis" }

$appArgs = @($Step)
if ($Mode) { $appArgs += $Mode }

if (-not (Test-Path (Join-Path $projectRoot "dumps"))) {
    New-Item -ItemType Directory -Path (Join-Path $projectRoot "dumps") | Out-Null
}

& $java @flags -cp $classes com.harshit.gclab.GcLab @appArgs
