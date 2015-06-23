#Violations Plugin

Changelog of Violations Plugin

#0.8

This is the first release since 12 oct 2012.

* Maintenance
 * 06c8efa43698ceb105bbd55ca007989372349ac0 Adding -Xdoclint:none property * mvn javadoc:javadoc fails on Java 8 where doclint is tuned on by default. It fails on generated sources. Javadoc task is run when performing a release.
 * 34527bf81b0623ad9120ceac1b9042f06019a5bf JENKINS-19260 Reformatting code before fix
 * 464c6d1fdf31e5e40c4e3890a8d1820123078aaf Adding web test #61 * Letting TypeDescriptor handle initialization of all descriptors.
 * f4eed6be475cef6e8d0c21d89670ad4d71dd5fd0 Cleaning up pom.xml preparing for a 0.8.0 alpha release * Adding license, MIT * Adding description, taken from the wiki page * Extending scm section with URL and TAG * Adding myself as developer * Stepping up versions of dependencies * Setting Java version to 1.6
 * 597569af6baa1b95fc420252fdf27a3855fc0617 Adding test cases #61 * For FindBugs, Checkstyle, PMD, CPD, CPPLint, CSSLint, PEP8, PerlCritic, PyLint, PyFlakes, XmlLint, Zptlint and Resharper * Adding new test util to ease writing test cases * Using latest Findbugs and FB-contrib messages * Letting Resharper parser report files, even if they dont exist on filesystem. The parser should parse and only parse. * Letting CSSLint support both lint and csslint format. It was implemented to support only lint. Would be better to have a Lint type but this makes us backwards compatible. We should probably also add a lint type. * Removing the static block from TypeDescriptor. For it to work, the parser descriptors has to load after TypeDescriptor and that is not guaranteed. * These tests increases code coverage from 26.6% to 59.4%. They make code more flexible as their input is report-files and config, output is data structures exposed to GUI. They make it clear what input is actually expected by the parsers. * Adding Travis CI, it gives more control of build process and can build for all supported JDK:s.
 * 9fca7f5b0a1be1e26fa4701bc819eacb69aa2386 Some cleaning after merging the PR:s * Unresolved merge in pom.xml and index.jelly * Duplicate README * GendarmeParserTest did not find xml-report files, it was using capital case in resource names
 * 14d96b5a539d856f67ee2536daa6d4db18091d57 Removing ovverride annotation on methods that are not available anymore #61
 * 8f7a6fd3981f50781ab5e7df8265f5e9b2cd4373 Adding README.md #61
 * 917c8e0e2aa2f387820246762a92ccbc3dc8ddb6 Corrected spelling in comment
 * b36d0547a1a2fac593918ff8c01b3a10938fa1bf update code to build on master
 * 0cfa02662a906f755a64a73e80b1c15fff881ff5 use jenkins 450 so that java7 can be used to compile
* Bugs
 * c74da20e0b4272f09556001b07b4e70028c5a76c JENKINS-19260 Allowing empty path in CodenarcParser
 * a4fe06f210fdd6a4524d3971e0e1a8d1cbad47e7 Fixing bug, icons for '?' were not displayed
 * 1c991520930ff4edcc83af1260de13d0ae001d74 Avoiding crash when setting Maven2 jobs to unstable in 1.609 #61 JENKINS-28880 * Avoiding crash when setting Maven2 build to UNSTABLE. By simply catching the IllegalStateException. This is not the final solution. The build will still not be marked as unstable, instead of crashing (FAIL) it will succeed (SUCCESS). Instead of showing no violation results, it will show them. * Making the example projects compile  * Testing example projects in web tests * Stepping up Jenkins version to 1.580.3 * Adding Java 8 to Travis config  * Using Firefox and Xvfb in Travis CI, HTMLUnit is a bit unpredictable
 * ac060663c3c5c4649f1ee88b30ed06e47dc42373 Fix JSLint find files with relative paths
 * 27cf3ba57bfcdcc15b0c57732cff2559a45c7133 fix URI to wiki page in floating help
 * c13ec15cb97a0369ab5e3f33c55f2de41176776e CodeNarc should display source when running on windows https://github.com/jenkinsci/violations-plugin/issues/45
 * 123bc6b02e1732bcfd100cd07800c6842192a2da Removed unnecessary "</p>", "the" and some white spaces.
 * 35df7833124152530bb030fef1b7e2ebfdf2a94c Fixed exception due to invalid characters in filename (JENKINS-15438)
 * d20e6730f71c7ef8ce03a85174752aa6cb820280 Fixed exception due to invalid characters in filename (JENKINS-15438)
 * fba58b2a3da5dc1e4efe3d088a1ae7ca9a426ee2 somehow .jelly file will not execute these functions unless they are explicitly defined even though they are simply calling its superclass
 * 08d033d138b6a019450f5b7a55a37e63460edbfa Escaping turned off for numberdiff as output may contain HTML
 * a1ed66d8f146b31af52c295ed6c49402144cc007 display violations when no source file is present
 * 371d38065873b56262bb2db9a34a4cb21a9d268b [JENKINS-12764] Missing Overview in Violations Plugin
 * a11c253f3065fc0b9ebf6fe6d7f3257c39c69265 [JENKINS-13567] Diagnosis for an NPE.
 * 6f338d89c831d6bb60d7b0dc75c6461d5dee0f2f map csslint severity, fixes #20
 * b25c6e545834297938a76f039ac81244fb43a87d 2 methods overriden for jelly to see them.
 * 28374d91574c1fdaf427adb9b3f3dd0bbd0933b4 Target jenkins 1.451. Un-needed @Override removed. Compiling version.
 * a2f3db98f5d9d6d020844e562bc4bc76eeab2627 [JENKINS-11227] Fixed so the Critical severity is marked as High and not Medium
* Features
 * 9e3bc76384e4c9b6f5f111822ba3983cbd7c0000 Added a hint on using the verbosity flag for the perlcritic command
 * 213efe7b494a08ce8cfb324cf6dfe8f0be93bd48 Lines with violations made red
 * e02dbfa4d60f0489afe9191439ddb2394aa26417 add parser for ReSharper commandline analysis reports
 * 842f17c3b80cec480f97529dc2d0d66be6a386c3 JENKINS-17722: parse CodeNarc SourceDirectory tag in report XML and use in file path.
 * 7f2c5457fad0745554cbed31c8c4dae588d29f85 map jslint severity
 * 32c01194f91e56861f07114b4d8599b9ce094c99 JENKINS-14970: 'pattern' is not being persisted.
 * 4a11dc8da1ee42a5920b06507bd2af840c6f915f security count in file build violation summary
 * 84b2a30f1c0ee9f3afdeff7f74fb167459b86a21 Security violations are listed in summary
 * 1a98fa111aad0eb3f719d34b14f56cdff801377c visual studio link in lines
 * a07a04d0e4ca52e7d5be22983f886bc571563bc9 New column in file summary: open in visual studio
 * 7fccea856e9543c62025592afe1abf1606a347fc add violation descriptors
 * 1771e7fd4c79f8f8158c1046bc41b71674cf9eac add pyflakes, xmllint, zptlint verifications
 * aacd39a1ffc55c5d57de5e6ac33f4cb62916476f Added violations-plugin.chart.y-axis.auto-range system property that can be set to make the Y (Number) axis to be auto ranged
 * 8a6c7321f62c5803a3b477c4b5bf385cfe4ff35d [FIXED JENKINS-11227] Fixed so the gendarme parser support multiple defects in a rule target, and Type and Method rule violations get the correct source file
