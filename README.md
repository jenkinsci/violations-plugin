# Violations Plugin

[![Build Status](https://jenkins.ci.cloudbees.com/job/plugins/job/violations-plugin/badge/icon)](https://jenkins.ci.cloudbees.com/job/plugins/job/violations-plugin/)

It visualises violations found in report files from static code analysis. There is also documentation in the [Jenkins wiki](https://wiki.jenkins-ci.org/display/JENKINS/Violations).

You may also want to have a look at [Violation Comments to Github Plugin](https://github.com/jenkinsci/violation-comments-to-github-plugin) and [Violation Comments to Bitbucket Server Plugin](https://github.com/jenkinsci/violation-comments-to-stash-plugin).

It supports the same formats as [Violations Lib](https://github.com/tomasbjerre/violations-lib).

It supports:
 * [_AndoidLint_](http://developer.android.com/tools/help/lint.html)
 * [_Checkstyle_](http://checkstyle.sourceforge.net/) ([_ESLint_](https://github.com/sindresorhus/grunt-eslint) with `format: 'checkstyle'`)
 * [_CodeNarc_](http://codenarc.sourceforge.net/)
 * [_CPD_](http://pmd.sourceforge.net/pmd-4.3.0/cpd.html)
 * [_CPPLint_](https://github.com/theandrewdavis/cpplint)
 * [_CPPCheck_](http://cppcheck.sourceforge.net/)
 * [_CSSLint_](https://github.com/CSSLint/csslint)
 * [_Findbugs_](http://findbugs.sourceforge.net/)
 * [_Flake8_](http://flake8.readthedocs.org/en/latest/) ([_Pep8_](https://github.com/PyCQA/pycodestyle), [_Mccabe_](https://pypi.python.org/pypi/mccabe), [_PyFlakes_](https://pypi.python.org/pypi/pyflakes))
 * [_FxCop_](https://en.wikipedia.org/wiki/FxCop)
 * [_Gendarme_](http://www.mono-project.com/docs/tools+libraries/tools/gendarme/)
 * [_JSHint_](http://jshint.com/)
 * _Lint_ A common XML format, used by different linters.
 * [_JCReport_](https://github.com/jCoderZ/fawkez/wiki/JcReport)
 * [_PerlCritic_](https://github.com/Perl-Critic)
 * [_PiTest_](http://pitest.org/)
 * [_PyLint_](https://www.pylint.org/)
 * [_PMD_](https://pmd.github.io/)
 * [_ReSharper_](https://www.jetbrains.com/resharper/)
 * [_Simian_](http://www.harukizaemon.com/simian/)
 * [_StyleCop_](https://stylecop.codeplex.com/)
 * [_XMLLint_](http://xmlsoft.org/xmllint.html)
 * [_ZPTLint_](https://pypi.python.org/pypi/zptlint)
 

## Job DSL Plugin

This plugin can be used with the Job DSL Plugin.

```
job('example') {
 publishers {
  violationsPublisher {
   violationConfigs {
    violationConfig {
     reporter("FINDBUGS")
     pattern(".*/findbugs/.*\\.xml\$")
    }
    violationConfig {
     reporter("CHECKSTYLE")
     pattern(".*/checkstyle/.*\\.xml\$")
    }
   }
   canComputeNew(false)
   canResolveRelativePaths(false)
   canRunOnFailed(false)
   defaultEncoding("")
   failedNewAll("")
   failedNewHigh("")
   failedNewLow("")
   failedNewNormal("")
   failedTotalAll("")
   failedTotalHigh("")
   failedTotalLow("")
   failedTotalNormal("")
   healthy("")
   shouldDetectModules(false)
   thresholdLimit("")
   unHealthy("")
   unstableNewAll("")
   unstableNewHigh("")
   unstableNewLow("")
   unstableNewNormal("")
   unstableTotalAll("")
   unstableTotalHigh("")
   unstableTotalLow("")
   unstableTotalNormal("")
   useDeltaValues(false)
   usePreviousBuildAsReference(false)
   useStableBuildAsReference(false)
  }
 }
}
```

## Pipeline Plugin

This plugin can be used with the Pipeline Plugin:

```
node {

 checkout([
  $class: 'GitSCM', 
  branches: [[ name: '*/master' ]], 
  doGenerateSubmoduleConfigurations: false,
  extensions: [],
  submoduleCfg: [],
  userRemoteConfigs: [[ url: 'https://github.com/tomasbjerre/violations-test.git' ]]
 ])

 sh '''
 ./gradlew build
 '''

 step([
  $class: 'ViolationsPublisher',
  canComputeNew: false,
  canResolveRelativePaths: false,
  canRunOnFailed: false,
  defaultEncoding: "",
  failedNewAll: "",
  failedNewHigh: "",
  failedNewLow: "",
  failedNewNormal: "",
  failedTotalAll: "",
  failedTotalHigh: "",
  failedTotalLow: "",
  failedTotalNormal: "",
  healthy: "",
  shouldDetectModules: false,
  thresholdLimit: "",
  unHealthy: "",
  unstableNewAll: "",
  unstableNewHigh: "",
  unstableNewLow: "",
  unstableNewNormal: "",
  unstableTotalAll: "",
  unstableTotalHigh: "",
  unstableTotalLow: "",
  unstableTotalNormal: "",
  useDeltaValues: false,
  usePreviousBuildAsReference: false,
  useStableBuildAsReference: false,
  violationConfigs: [
   [ pattern: '.*/checkstyle/.*\\.xml$', reporter: 'CHECKSTYLE' ], 
   [ pattern: '.*/findbugs/.*\\.xml$', reporter: 'FINDBUGS' ], 
  ]
 ])
}
```

## Developer instructions
More details on Jenkins plugin development is available [here](https://wiki.jenkins-ci.org/display/JENKINS/Plugin+tutorial).

A release is created like this. You need to clone from jenkinsci-repo, with https and have username/password in settings.xml.
```
mvn release:prepare release:perform
```
