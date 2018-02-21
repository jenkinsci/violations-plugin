# Violations Plugin

[![Build Status](https://ci.jenkins.io/job/Plugins/job/violations-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/violations-plugin)

It visualizes violations found in report files from static code analysis. There is also documentation in the [Jenkins wiki](https://wiki.jenkins-ci.org/display/JENKINS/Violations).

It supports the same formats as [Violations Lib](https://github.com/tomasbjerre/violations-lib).

It supports:
 * [_AndroidLint_](http://developer.android.com/tools/help/lint.html)
 * [_Checkstyle_](http://checkstyle.sourceforge.net/)
   * [_Detekt_](https://github.com/arturbosch/detekt) with `--output-format xml`.
   * [_ESLint_](https://github.com/sindresorhus/grunt-eslint) with `format: 'checkstyle'`.
   * [_KTLint_](https://github.com/shyiko/ktlint)
   * [_SwiftLint_](https://github.com/realm/SwiftLint) with `--reporter checkstyle`.
   * [_PHPCS_](https://github.com/squizlabs/PHP_CodeSniffer) with `phpcs api.php --report=checkstyle`.
 * [_CLang_](https://clang-analyzer.llvm.org/)
   * [_RubyCop_](http://rubocop.readthedocs.io/en/latest/formatters/) with `rubycop -f clang file.rb`
 * [_CodeNarc_](http://codenarc.sourceforge.net/)
 * [_CPD_](http://pmd.sourceforge.net/pmd-4.3.0/cpd.html)
 * [_CPPLint_](https://github.com/theandrewdavis/cpplint)
 * [_CPPCheck_](http://cppcheck.sourceforge.net/)
 * [_CSSLint_](https://github.com/CSSLint/csslint)
 * [_DocFX_](http://dotnet.github.io/docfx/)
 * [_Findbugs_](http://findbugs.sourceforge.net/)
 * [_Flake8_](http://flake8.readthedocs.org/en/latest/)
   * [_AnsibleLint_](https://github.com/willthames/ansible-lint) with `-p`
   * [_Mccabe_](https://pypi.python.org/pypi/mccabe)
   * [_Pep8_](https://github.com/PyCQA/pycodestyle)
   * [_PyFlakes_](https://pypi.python.org/pypi/pyflakes)
 * [_FxCop_](https://en.wikipedia.org/wiki/FxCop)
 * [_Gendarme_](http://www.mono-project.com/docs/tools+libraries/tools/gendarme/)
 * [_GoLint_](https://github.com/golang/lint)
   * [_GoVet_](https://golang.org/cmd/vet/) Same format as GoLint.
 * [_GoogleErrorProne_](https://github.com/google/error-prone)
 * [_JSHint_](http://jshint.com/)
 * _Lint_ A common XML format, used by different linters.
 * [_JCReport_](https://github.com/jCoderZ/fawkez/wiki/JcReport)
 * [_Klocwork_](http://www.klocwork.com/products-services/klocwork/static-code-analysis)
 * [_MyPy_](https://pypi.python.org/pypi/mypy-lang)
 * [_PCLint_](http://www.gimpel.com/html/pcl.htm) PC-Lint using the same output format as the Jenkins warnings plugin, [_details here_](https://wiki.jenkins.io/display/JENKINS/PcLint+options)
 * [_PerlCritic_](https://github.com/Perl-Critic)
 * [_PiTest_](http://pitest.org/)
 * [_PyDocStyle_](https://pypi.python.org/pypi/pydocstyle)
 * [_PyLint_](https://www.pylint.org/)
 * [_PMD_](https://pmd.github.io/)
   * [_Infer_](http://fbinfer.com/) Facebook Infer. With `--pmd-xml`.
   * [_PHPPMD_](https://phpmd.org/) with `phpmd api.php xml ruleset.xml`.
 * [_ReSharper_](https://www.jetbrains.com/resharper/)
 * [_SbtScalac_](http://www.scala-sbt.org/)
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
     parser("FINDBUGS")
     reporter("Findbugs")
     pattern(".*/findbugs/.*\\.xml\$")
    }
    violationConfig {
     parser("CHECKSTYLE")
     reporter("Checkstyle")
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
   [ pattern: '.*/checkstyle/.*\\.xml$', parser: 'CHECKSTYLE', reporter: 'Checkstyle' ],
   [ pattern: '.*/findbugs/.*\\.xml$', parser: 'FINDBUGS', reporter: 'Findbugs' ],
   [ pattern: '.*/pmd/.*\\.xml$', parser: 'PMD', reporter: 'PMD' ],
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
