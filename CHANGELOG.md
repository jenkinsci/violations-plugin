# Violations Plugin Changelog

Changelog of Violations Plugin.

## Unreleased
### GitHub [#71](https://github.com/jenkinsci/violations-plugin/issues/71) NPE in FileModelProxy.java line 475

**Using a Supplier for loading FileModel**

 * Avoids NPE in FileModelProxy.java line 475 #71 

[f172b29ba5a23c8](https://github.com/jenkinsci/violations-plugin/commit/f172b29ba5a23c8) Tomas Bjerre *2015-08-15 04:19:53*


### GitHub [#73](https://github.com/jenkinsci/violations-plugin/pull/73) Fix URL escape

**Printing URL encoded type name in Violations Report**

 * As reported in PR #73 

[b8115115e914f12](https://github.com/jenkinsci/violations-plugin/commit/b8115115e914f12) Tomas Bjerre *2015-08-14 19:36:19*


### No issue

**Setting myself as contributor, not developer**


[cab7103bd00057c](https://github.com/jenkinsci/violations-plugin/commit/cab7103bd00057c) Tomas Bjerre *2015-11-25 05:35:23*

**Refactoring, moving HTML from FileModelProxy to its view**


[572bc1ce6fd3c76](https://github.com/jenkinsci/violations-plugin/commit/572bc1ce6fd3c76) Tomas Bjerre *2015-06-28 05:13:12*

**Reformat**


[3415f17c308f4e8](https://github.com/jenkinsci/violations-plugin/commit/3415f17c308f4e8) Tomas Bjerre *2015-06-28 04:53:01*


## 2
### GitHub [#56](https://github.com/jenkinsci/violations-plugin/issues/56) ratcheting support

**Implementing test cases for ratcheting JENKINS-15248 #56**


[e727084f673648c](https://github.com/jenkinsci/violations-plugin/commit/e727084f673648c) Tomas Bjerre *2015-06-24 19:19:30*


### GitHub [#70](https://github.com/jenkinsci/violations-plugin/issues/70) HTML not rendered correctly

**HTML not rendered correctly #70**


[296e3d1889a1dbe](https://github.com/jenkinsci/violations-plugin/commit/296e3d1889a1dbe) Tomas Bjerre *2015-06-27 08:26:23*


### Jira JENKINS-15248 

**Implementing test cases for ratcheting JENKINS-15248 #56**


[e727084f673648c](https://github.com/jenkinsci/violations-plugin/commit/e727084f673648c) Tomas Bjerre *2015-06-24 19:19:30*

**Implementation for [JENKINS-15248], untested**


[f2ddeb40705b936](https://github.com/jenkinsci/violations-plugin/commit/f2ddeb40705b936) Tobias Baum *2013-04-01 17:51:51*


### No issue

**Using JenkinsRule instead depricated HudsonTestCase in test cases**


[c1ab5b1dc71a153](https://github.com/jenkinsci/violations-plugin/commit/c1ab5b1dc71a153) Tomas Bjerre *2015-06-23 16:09:56*

**Adding CHANGELOG.md**


[b7393b987983299](https://github.com/jenkinsci/violations-plugin/commit/b7393b987983299) Tomas Bjerre *2015-06-23 15:59:41*

**use jenkins 450 so that java7 can be used to compile**


[0cfa02662a906f7](https://github.com/jenkinsci/violations-plugin/commit/0cfa02662a906f7) Peter Reilly *2012-10-15 12:56:07*


## 1
### GitHub [#20](https://github.com/jenkinsci/violations-plugin/issues/20) CssLintParser does not map csslint severity

**map csslint severity, fixes #20**

 * same as checkstyle, except the default should remain High. 

[6f338d89c831d6b](https://github.com/jenkinsci/violations-plugin/commit/6f338d89c831d6b) christoph.roensch *2012-11-09 15:30:30*


### GitHub [#61](https://github.com/jenkinsci/violations-plugin/issues/61) New release?

**Avoiding crash when setting Maven2 jobs to unstable in 1.609 #61 JENKINS-28880**

 * Avoiding crash when setting Maven2 build to UNSTABLE. By simply catching the IllegalStateException. This is not the final solution. The build will still not be marked as unstable, instead of crashing (FAIL) it will succeed (SUCCESS). Instead of showing no violation results, it will show them. 
 * Making the example projects compile 
 * Testing example projects in web tests 
 * Stepping up Jenkins version to 1.580.3 
 * Adding Java 8 to Travis config 
 * Using Firefox and Xvfb in Travis CI, HTMLUnit is a bit unpredictable 

[1c991520930ff4e](https://github.com/jenkinsci/violations-plugin/commit/1c991520930ff4e) Tomas Bjerre *2015-06-17 20:44:51*

**Adding web test #61**

 * Letting TypeDescriptor handle initialization of all descriptors. 

[464c6d1fdf31e5e](https://github.com/jenkinsci/violations-plugin/commit/464c6d1fdf31e5e) Tomas Bjerre *2015-06-16 02:55:42*

**Adding test cases #61**

 * For FindBugs, Checkstyle, PMD, CPD, CPPLint, CSSLint, PEP8, PerlCritic, PyLint, PyFlakes, XmlLint, Zptlint and Resharper 
 * Adding new test util to ease writing test cases 
 * Using latest Findbugs and FB-contrib messages 
 * Letting Resharper parser report files, even if they dont exist on filesystem. The parser should parse and only parse. 
 * Letting CSSLint support both lint and csslint format. It was implemented to support only lint. Would be better to have a Lint type but this makes us backwards compatible. We should probably also add a lint type. 
 * Removing the static block from TypeDescriptor. For it to work, the parser descriptors has to load after TypeDescriptor and that is not guaranteed. 
 * These tests increases code coverage from 26.6% to 59.4%. They make code more flexible as their input is report-files and config, output is data structures exposed to GUI. They make it clear what input is actually expected by the parsers. 
 * Adding Travis CI, it gives more control of build process and can build for all supported JDK:s. 

[597569af6baa1b9](https://github.com/jenkinsci/violations-plugin/commit/597569af6baa1b9) Tomas Bjerre *2015-06-14 18:03:15*

**Removing ovverride annotation on methods that are not available anymore #61**


[14d96b5a539d856](https://github.com/jenkinsci/violations-plugin/commit/14d96b5a539d856) Tomas Bjerre *2015-06-07 14:44:26*

**Adding README.md #61**


[8f7a6fd3981f507](https://github.com/jenkinsci/violations-plugin/commit/8f7a6fd3981f507) Tomas Bjerre *2015-06-07 14:17:42*


### Jira JENKINS-11227 

**[JENKINS-11227] Fixed so the Critical severity is marked as High and not Medium**


[a2f3db98f5d9d6d](https://github.com/jenkinsci/violations-plugin/commit/a2f3db98f5d9d6d) Erik Ramfelt *2011-10-06 19:05:19*

**[FIXED JENKINS-11227] Fixed so the gendarme parser support multiple defects in a rule target, and Type and Method rule violations get the correct source file**


[8a6c7321f62c580](https://github.com/jenkinsci/violations-plugin/commit/8a6c7321f62c580) Erik Ramfelt *2011-10-05 18:55:34*


### Jira JENKINS-12764 

**[JENKINS-12764] Missing Overview in Violations Plugin**

 * Clicking on FxCop results leads to blank page 
 * Changed the plugin according to 
 * http://stackoverflow.com/questions/15905131/clicking-on-fxcop-results-in-jenkins-violations-plugin-leads-to-blank-page and the 
 * FxCop results are now shown 

[371d38065873b56](https://github.com/jenkinsci/violations-plugin/commit/371d38065873b56) Christian Erhardt *2013-07-25 08:42:01*


### Jira JENKINS-13567 

**[JENKINS-13567] Diagnosis for an NPE.**


[a11c253f3065fc0](https://github.com/jenkinsci/violations-plugin/commit/a11c253f3065fc0) Jesse Glick *2013-06-03 12:45:25*


### Jira JENKINS-14970 

**JENKINS-14970: 'pattern' is not being persisted.**


[32c01194f91e568](https://github.com/jenkinsci/violations-plugin/commit/32c01194f91e568) Darrell King *2013-02-19 21:18:51*


### Jira JENKINS-15438 

**Fixed exception due to invalid characters in filename (JENKINS-15438)**

 * https://issues.jenkins-ci.org/browse/JENKINS-15438 

[35df78331241525](https://github.com/jenkinsci/violations-plugin/commit/35df78331241525) Juho Tykkälä *2014-03-04 08:09:37*

**Fixed exception due to invalid characters in filename (JENKINS-15438)**

 * https://issues.jenkins-ci.org/browse/JENKINS-15438 

[d20e6730f71c7ef](https://github.com/jenkinsci/violations-plugin/commit/d20e6730f71c7ef) Juho Tykkälä *2014-03-04 08:07:02*


### Jira JENKINS-17722 

**JENKINS-17722: parse CodeNarc SourceDirectory tag in report XML and use in file path.**


[842f17c3b80cec4](https://github.com/jenkinsci/violations-plugin/commit/842f17c3b80cec4) John Engelman *2013-04-23 18:03:34*


### Jira JENKINS-19260 

**JENKINS-19260 Allowing empty path in CodenarcParser**


[c74da20e0b4272f](https://github.com/jenkinsci/violations-plugin/commit/c74da20e0b4272f) Tomas Bjerre *2015-06-22 18:03:36*

**JENKINS-19260 Reformatting code before fix**


[34527bf81b0623a](https://github.com/jenkinsci/violations-plugin/commit/34527bf81b0623a) Tomas Bjerre *2015-06-22 17:54:56*


### Jira JENKINS-28880 

**Avoiding crash when setting Maven2 jobs to unstable in 1.609 #61 JENKINS-28880**

 * Avoiding crash when setting Maven2 build to UNSTABLE. By simply catching the IllegalStateException. This is not the final solution. The build will still not be marked as unstable, instead of crashing (FAIL) it will succeed (SUCCESS). Instead of showing no violation results, it will show them. 
 * Making the example projects compile 
 * Testing example projects in web tests 
 * Stepping up Jenkins version to 1.580.3 
 * Adding Java 8 to Travis config 
 * Using Firefox and Xvfb in Travis CI, HTMLUnit is a bit unpredictable 

[1c991520930ff4e](https://github.com/jenkinsci/violations-plugin/commit/1c991520930ff4e) Tomas Bjerre *2015-06-17 20:44:51*


### No issue

**Adding -Xdoclint:none property**

 * mvn javadoc:javadoc fails on Java 8 where doclint is tuned on by default. It fails on generated sources. Javadoc task is run when performing a release. 

[06c8efa43698ceb](https://github.com/jenkinsci/violations-plugin/commit/06c8efa43698ceb) Tomas Bjerre *2015-06-23 14:47:40*

**Fixing bug, icons for '?' were not displayed**


[a4fe06f210fdd6a](https://github.com/jenkinsci/violations-plugin/commit/a4fe06f210fdd6a) Tomas Bjerre *2015-06-22 17:19:23*

**Cleaning up pom.xml preparing for a 0.8.0 alpha release**

 * Adding license, MIT 
 * Adding description, taken from the wiki page 
 * Extending scm section with URL and TAG 
 * Adding myself as developer 
 * Stepping up versions of dependencies 
 * Setting Java version to 1.6 

[f4eed6be475cef6](https://github.com/jenkinsci/violations-plugin/commit/f4eed6be475cef6) Tomas Bjerre *2015-06-14 19:17:52*

**Some cleaning after merging the PR:s**

 * Unresolved merge in pom.xml and index.jelly 
 * Duplicate README 
 * GendarmeParserTest did not find xml-report files, it was using capital case in resource names 

[9fca7f5b0a1be1e](https://github.com/jenkinsci/violations-plugin/commit/9fca7f5b0a1be1e) Tomas Bjerre *2015-06-07 15:39:28*

**Fix JSLint find files with relative paths**


[ac060663c3c5c46](https://github.com/jenkinsci/violations-plugin/commit/ac060663c3c5c46) Michael Barrientos *2014-09-20 16:10:13*

**fix URI to wiki page in floating help**


[27cf3ba57bfcdcc](https://github.com/jenkinsci/violations-plugin/commit/27cf3ba57bfcdcc) Eito Katagiri *2014-08-31 18:10:49*

**CodeNarc should display source when running on windows https://github.com/jenkinsci/violations-plugin/issues/45**


[c13ec15cb97a036](https://github.com/jenkinsci/violations-plugin/commit/c13ec15cb97a036) Sascha Kiedrowski *2014-03-06 10:22:39*

**Removed unnecessary "</p>", "the" and some white spaces.**


[123bc6b02e1732b](https://github.com/jenkinsci/violations-plugin/commit/123bc6b02e1732b) yonexyonex *2014-03-05 05:12:44*

**somehow .jelly file will not execute these functions unless they are explicitly defined even though they are simply calling its superclass**


[fba58b2a3da5dc1](https://github.com/jenkinsci/violations-plugin/commit/fba58b2a3da5dc1) Roger Hu *2014-03-02 17:40:13*

**Added a hint on using the verbosity flag for the perlcritic command**


[9e3bc76384e4c9b](https://github.com/jenkinsci/violations-plugin/commit/9e3bc76384e4c9b) Jonas B. Nielsen *2014-01-29 21:33:07*

**Corrected spelling in comment**


[917c8e0e2aa2f38](https://github.com/jenkinsci/violations-plugin/commit/917c8e0e2aa2f38) Jonas B. Nielsen *2014-01-29 21:24:24*

**Lines with violations made red**


[213efe7b494a08c](https://github.com/jenkinsci/violations-plugin/commit/213efe7b494a08c) Radek Simko *2014-01-09 10:58:31*

**Escaping turned off for numberdiff as output may contain HTML**


[08d033d138b6a01](https://github.com/jenkinsci/violations-plugin/commit/08d033d138b6a01) Radek Simko *2013-12-03 23:10:59*

**update code to build on master**


[b36d0547a1a2fac](https://github.com/jenkinsci/violations-plugin/commit/b36d0547a1a2fac) tstivers *2013-11-12 19:48:54*

**add parser for ReSharper commandline analysis reports**


[e02dbfa4d60f048](https://github.com/jenkinsci/violations-plugin/commit/e02dbfa4d60f048) tstivers *2013-11-12 18:54:58*

**display violations when no source file is present**

 * Adds some more getter functions to expose data to the jelly view, 
 * jelly was failing silently on a lot of directives. 
 * When no source is present, displays a message with links to more 
 * detail. 
 * Tweaks some CSS to prevent horizontal scrollbars with long messages. 

[a1ed66d8f146b31](https://github.com/jenkinsci/violations-plugin/commit/a1ed66d8f146b31) Ryan Davis *2013-07-26 17:04:01*

**map jslint severity**

 * same as checkstyle, except the default should remain Medium. 

[7f2c5457fad0745](https://github.com/jenkinsci/violations-plugin/commit/7f2c5457fad0745) christoph.roensch *2013-03-27 14:11:04*

**security count in file build violation summary**


[4a11dc8da1ee42a](https://github.com/jenkinsci/violations-plugin/commit/4a11dc8da1ee42a) vcarluer *2012-02-23 10:50:02*

**Security violations are listed in summary**


[84b2a30f1c0ee9f](https://github.com/jenkinsci/violations-plugin/commit/84b2a30f1c0ee9f) vcarluer *2012-02-23 09:16:57*

**visual studio link in lines**


[1a98fa111aad0eb](https://github.com/jenkinsci/violations-plugin/commit/1a98fa111aad0eb) vcarluer *2012-02-22 15:43:15*

**New column in file summary: open in visual studio**


[a07a04d0e4ca52e](https://github.com/jenkinsci/violations-plugin/commit/a07a04d0e4ca52e) vcarluer *2012-02-22 15:18:52*

**readme**


[8413ef42d92760b](https://github.com/jenkinsci/violations-plugin/commit/8413ef42d92760b) vcarluer *2012-02-21 14:36:16*

**2 methods overriden for jelly to see them.**


[b25c6e545834297](https://github.com/jenkinsci/violations-plugin/commit/b25c6e545834297) vcarluer *2012-02-21 14:08:05*

**Target jenkins 1.451.**

 * Un-needed @Override removed. 
 * Compiling version. 

[28374d91574c1fd](https://github.com/jenkinsci/violations-plugin/commit/28374d91574c1fd) vcarluer *2012-02-21 11:10:19*

**add violation descriptors**


[7fccea856e9543c](https://github.com/jenkinsci/violations-plugin/commit/7fccea856e9543c) Jean-François Roche *2012-01-24 16:36:30*

**add pyflakes, xmllint, zptlint verifications**


[1771e7fd4c79f8f](https://github.com/jenkinsci/violations-plugin/commit/1771e7fd4c79f8f) Jean-François Roche *2012-01-24 16:16:36*

**Added violations-plugin.chart.y-axis.auto-range system property that can be set to make the Y (Number) axis to be auto ranged**


[aacd39a1ffc55c5](https://github.com/jenkinsci/violations-plugin/commit/aacd39a1ffc55c5) Erik Ramfelt *2011-10-05 19:49:04*


## 0.7.14
### No issue

**cargo cult attempt to fix release upload failure - change the number**


[51bccdbf3543932](https://github.com/jenkinsci/violations-plugin/commit/51bccdbf3543932) Peter Reilly *2012-10-12 14:09:01*

**revert cargo cult attempt to fix release upload failure**


[fd324323df9a147](https://github.com/jenkinsci/violations-plugin/commit/fd324323df9a147) Peter Reilly *2012-10-12 14:07:00*

**cargo cult attempt to fix release upload failure**


[8f83e7d0b336a2a](https://github.com/jenkinsci/violations-plugin/commit/8f83e7d0b336a2a) Peter Reilly *2012-10-12 14:03:35*


## 0.7.12
### No issue

**Fixed the link.**


[ce064321085109e](https://github.com/jenkinsci/violations-plugin/commit/ce064321085109e) Kohsuke Kawaguchi *2012-09-08 20:38:19*

**set severity level according to the confidence not the message ID**


[e31426fd4b9d702](https://github.com/jenkinsci/violations-plugin/commit/e31426fd4b9d702) Andy Wilde *2011-12-13 16:58:24*


## 0.7.11
### No issue

**This compilation problem prevents the debugging.**

 * The idea is to have something compile but causes a warning, right? 

[56740bb540b1ad4](https://github.com/jenkinsci/violations-plugin/commit/56740bb540b1ad4) Kohsuke Kawaguchi *2012-09-08 20:24:57*

**escape HTML unsafe characters properly**


[6dcbef2114adb0f](https://github.com/jenkinsci/violations-plugin/commit/6dcbef2114adb0f) Kohsuke Kawaguchi *2012-09-08 20:21:52*

**chart width increased to 500px**


[0b47b4024660d2c](https://github.com/jenkinsci/violations-plugin/commit/0b47b4024660d2c) Jozsef Kozma *2012-04-27 16:36:55*

**updated maven pom to use repo.jenkins-ci.org repository**


[7bd75c09e36173d](https://github.com/jenkinsci/violations-plugin/commit/7bd75c09e36173d) Nicolas De Loof *2012-04-06 16:21:08*

**Add a parser for Perl::Critic output.**


[5f9c54054b62dbe](https://github.com/jenkinsci/violations-plugin/commit/5f9c54054b62dbe) David McGuire *2012-01-05 22:49:53*


## 0.7.10
### Jira HUDSON-2242 

**[FIXED HUDSON-2242] Fixed so it works with new Stylecop XML output**


[f17fe522ba4cc9c](https://github.com/jenkinsci/violations-plugin/commit/f17fe522ba4cc9c) redsolo *2008-08-20 22:15:49*


### Jira HUDSON-3251 

**[FIXED HUDSON-3442] Applied patch (14/18) from HUDSON-3251 to fix compilation problems**


[93fae4124f60ded](https://github.com/jenkinsci/violations-plugin/commit/93fae4124f60ded) kohsuke *2009-04-07 15:56:01*

**applied patch (14/18) from HUDSON-3251**


[f886aa803739d74](https://github.com/jenkinsci/violations-plugin/commit/f886aa803739d74) kohsuke *2009-03-31 03:15:54*


### Jira HUDSON-3442 

**[FIXED HUDSON-3442] Applied patch (14/18) from HUDSON-3251 to fix compilation problems**


[93fae4124f60ded](https://github.com/jenkinsci/violations-plugin/commit/93fae4124f60ded) kohsuke *2009-04-07 15:56:01*


### Jira HUDSON-3529 

**[FIXED HUDSON-3529] fixed the path handling.**


[67c08ed79c78a9f](https://github.com/jenkinsci/violations-plugin/commit/67c08ed79c78a9f) kohsuke *2010-10-05 20:33:58*

**[FIXED HUDSON-3529] fixed the path handling.**


[7ac6e60d5eac979](https://github.com/jenkinsci/violations-plugin/commit/7ac6e60d5eac979) kohsuke *2010-10-05 20:10:59*


### Jira HUDSON-5815 

**HUDSON-5815: incorrect tag looked at for accessors violations in fxcop**


[e9dfc99c80a4a49](https://github.com/jenkinsci/violations-plugin/commit/e9dfc99c80a4a49) peterkittreilly *2010-03-03 10:58:49*


### Jira HUDSON-7169 

**HUDSON-7169: patch to add narc violations patch from Robin Bramley**


[d702cba0f239f28](https://github.com/jenkinsci/violations-plugin/commit/d702cba0f239f28) peterkittreilly *2010-08-21 13:48:12*


### Jira HUDSON-7271 

**[HUDSON-7271] patch for CodeArc parser does not parse AbcComplexity violations**


[3f35ca81a7aaecd](https://github.com/jenkinsci/violations-plugin/commit/3f35ca81a7aaecd) peterkittreilly *2010-10-06 09:59:06*


### Jira JENKINS-7308 

**JENKINS-7308: use different mappings for checkstyle violations**


[63db9149b82de5c](https://github.com/jenkinsci/violations-plugin/commit/63db9149b82de5c) Peter Reilly *2011-07-16 16:27:18*


### Jira JENKINS-7728 

**[FIXED JENKINS-7728]**


[30a4cb1ca63804e](https://github.com/jenkinsci/violations-plugin/commit/30a4cb1ca63804e) cactusman *2011-07-03 12:22:07*


### Jira utf-8 

**ensure that the file specifies utf-8, in-case the xml decoder does not understand the xml specification**


[aeeadb4e040d415](https://github.com/jenkinsci/violations-plugin/commit/aeeadb4e040d415) peterkittreilly *2008-03-29 14:17:46*


### No issue

**Added support to csslint xml report**


[b8753094ba8121f](https://github.com/jenkinsci/violations-plugin/commit/b8753094ba8121f) Marcelo Brunken *2011-07-26 09:58:41*

**Added support to csslint xml report**


[ca7ce551c9cd0e6](https://github.com/jenkinsci/violations-plugin/commit/ca7ce551c9cd0e6) Marcelo Brunken *2011-07-26 09:58:24*

**fixed findbugs warnings and removed unused imports**


[aa9efac41a06e1a](https://github.com/jenkinsci/violations-plugin/commit/aa9efac41a06e1a) Bartosz Ocytko *2011-06-25 17:41:33*

**fixed typos in comments**


[a3ac343721f05e1](https://github.com/jenkinsci/violations-plugin/commit/a3ac343721f05e1) Bartosz Ocytko *2011-06-25 17:41:33*

**Refactored unit-tests. Logic in getFullBuildModel was duplicated over all test classes. Moved this logic to an abstract class for the tests.**


[96039322a44a11d](https://github.com/jenkinsci/violations-plugin/commit/96039322a44a11d) Bartosz Ocytko *2011-06-25 17:41:33*

**fixed typos in comments**


[469f8ec7eb3f1cb](https://github.com/jenkinsci/violations-plugin/commit/469f8ec7eb3f1cb) Bartosz Ocytko *2011-06-25 17:41:33*

**Added Cpplint parser for the violations plugin.**

 * The code is a copy from pylint with some minor adjustments 

[c4cecd9e348714f](https://github.com/jenkinsci/violations-plugin/commit/c4cecd9e348714f) Jos Houtman *2011-04-08 09:31:26*

**use IOException2 for 1.5 compilation**


[38f94f1ac39e786](https://github.com/jenkinsci/violations-plugin/commit/38f94f1ac39e786) olamy *2011-01-05 11:44:20*

**codenarc doesn't always provide the message, so use ruleName as the default.**

 * Elsewhere in the violations plugin, the message property is expected to be non-null. 

[8852103e11e8f07](https://github.com/jenkinsci/violations-plugin/commit/8852103e11e8f07) kohsuke *2010-11-03 13:33:22*

**- fixed NPE. It's not clear if the Violation class is supposed to have null message or not, but it doesn't hurt to be defensive.**

 * - added a test case that reproduces NPE on message. 
 * - Codenarc parser shouldn&#39;t override the &#39;source&#39; information by a pointless copy of the source code. 

[c99b09dc2aa096d](https://github.com/jenkinsci/violations-plugin/commit/c99b09dc2aa096d) kohsuke *2010-10-29 01:51:30*

**ignore IDE generated files**


[728baed6d0102c2](https://github.com/jenkinsci/violations-plugin/commit/728baed6d0102c2) kohsuke *2010-10-06 16:23:14*

**avoid using APIs that only exist in Java6**


[e6926116aeb3912](https://github.com/jenkinsci/violations-plugin/commit/e6926116aeb3912) kohsuke *2010-10-01 23:15:43*

**Added support for jcReport xml output (http://www.jcoderz.org/fawkez/wiki/JcReport).**


[20fc0e3e3922703](https://github.com/jenkinsci/violations-plugin/commit/20fc0e3e3922703) andreasmandel *2010-04-06 17:20:17*

**Added violation source, needed during jslint XML parsing.**


[0164012a86fd039](https://github.com/jenkinsci/violations-plugin/commit/0164012a86fd039) cliffano *2010-04-02 04:35:01*

**Added JSLint violation reporting, currently supports XML report produced by jslint4java .**


[3bcf85c46881d0a](https://github.com/jenkinsci/violations-plugin/commit/3bcf85c46881d0a) cliffano *2010-01-19 14:44:57*

**[violations] add developer info in pom**


[64c5ee3136210f4](https://github.com/jenkinsci/violations-plugin/commit/64c5ee3136210f4) mindless *2009-12-30 00:45:20*

**[violations] Update most uses of deprecated APIs (still uses old ChartUtil)**


[1dfccaff7ff59d8](https://github.com/jenkinsci/violations-plugin/commit/1dfccaff7ff59d8) mindless *2009-10-08 00:13:54*

**bumping up POM version**


[fcb867802a2b3db](https://github.com/jenkinsci/violations-plugin/commit/fcb867802a2b3db) kohsuke *2009-08-01 02:00:50*

**bumping up POM version**


[c4ed26bfb0fa764](https://github.com/jenkinsci/violations-plugin/commit/c4ed26bfb0fa764) kohsuke *2009-07-24 23:42:01*

**Add Gendarme http://mono-project.com/Gendarme support**


[2865460f7826bc9](https://github.com/jenkinsci/violations-plugin/commit/2865460f7826bc9) grozeille *2009-07-19 21:38:36*

**bumping up POM version**


[50e3364ef9b05a2](https://github.com/jenkinsci/violations-plugin/commit/50e3364ef9b05a2) kohsuke *2009-07-18 01:59:22*

**bumping up POM version**


[27c310cb1a16029](https://github.com/jenkinsci/violations-plugin/commit/27c310cb1a16029) kohsuke *2009-07-11 01:41:24*

**bumping up POM version**


[af9c19f4abfca8d](https://github.com/jenkinsci/violations-plugin/commit/af9c19f4abfca8d) kohsuke *2009-07-03 01:03:43*

**bumping up POM version**


[b06bfb79ec35c7c](https://github.com/jenkinsci/violations-plugin/commit/b06bfb79ec35c7c) kohsuke *2009-06-27 01:13:13*

**bumping up POM version**


[71f5ca7ad2ab5cb](https://github.com/jenkinsci/violations-plugin/commit/71f5ca7ad2ab5cb) kohsuke *2009-06-23 20:59:35*

**bumping up POM version**


[c9da002448ab124](https://github.com/jenkinsci/violations-plugin/commit/c9da002448ab124) kohsuke *2009-06-20 02:36:46*

**bumping up POM version**


[549630f92297eeb](https://github.com/jenkinsci/violations-plugin/commit/549630f92297eeb) kohsuke *2009-06-15 16:05:37*

**bumping up POM version**


[0c7fcabb3dc4b08](https://github.com/jenkinsci/violations-plugin/commit/0c7fcabb3dc4b08) kohsuke *2009-05-31 16:28:02*

**bumping up POM version**


[292ef0724fa22b6](https://github.com/jenkinsci/violations-plugin/commit/292ef0724fa22b6) kohsuke *2009-05-28 19:29:46*

**bumping up POM version**


[5770b9b057e6e38](https://github.com/jenkinsci/violations-plugin/commit/5770b9b057e6e38) kohsuke *2009-05-23 04:20:25*

**bumping up POM version**


[cebacdf7de42377](https://github.com/jenkinsci/violations-plugin/commit/cebacdf7de42377) kohsuke *2009-05-16 19:46:05*

**bumping up POM version**


[3e31c100e20e930](https://github.com/jenkinsci/violations-plugin/commit/3e31c100e20e930) kohsuke *2009-05-16 15:14:10*

**use resolve name in StyleCop**


[36e4cf586d909b9](https://github.com/jenkinsci/violations-plugin/commit/36e4cf586d909b9) peterkittreilly *2009-05-13 21:33:06*

**bumping up POM version**


[2e5dfc8d3c1ca67](https://github.com/jenkinsci/violations-plugin/commit/2e5dfc8d3c1ca67) kohsuke *2009-05-09 01:10:35*

**Issue 3578: remove mavenmodule lookup**


[b3271b05a7aea42](https://github.com/jenkinsci/violations-plugin/commit/b3271b05a7aea42) peterkittreilly *2009-05-05 20:35:16*

**bumping up POM version**


[cc9547f8a014b14](https://github.com/jenkinsci/violations-plugin/commit/cc9547f8a014b14) kohsuke *2009-05-03 22:41:34*

**bumping up POM version**


[f202dbc4d85049a](https://github.com/jenkinsci/violations-plugin/commit/f202dbc4d85049a) kohsuke *2009-05-02 02:08:39*

**bumping up POM version**


[1dda36504541d8c](https://github.com/jenkinsci/violations-plugin/commit/1dda36504541d8c) kohsuke *2009-04-25 09:16:01*

**rolling back botched 1.301 release**


[3ef3903cf553972](https://github.com/jenkinsci/violations-plugin/commit/3ef3903cf553972) kohsuke *2009-04-25 06:58:49*

**bumping up POM version**


[1b413722679991a](https://github.com/jenkinsci/violations-plugin/commit/1b413722679991a) kohsuke *2009-04-25 06:18:59*

**Removing .cvsignore files, obsolete after move to svn.**


[1a400f48d138115](https://github.com/jenkinsci/violations-plugin/commit/1a400f48d138115) jglick *2009-04-24 16:54:36*

**updated wiki links to the proper URL**


[a7b5715e149e954](https://github.com/jenkinsci/violations-plugin/commit/a7b5715e149e954) kohsuke *2009-04-23 19:21:09*

**fix for npe when configured unstable is zero**


[bbba8f06ec19d27](https://github.com/jenkinsci/violations-plugin/commit/bbba8f06ec19d27) peterkittreilly *2009-04-23 13:02:09*

**bumping up POM version**


[27d69c788b4811b](https://github.com/jenkinsci/violations-plugin/commit/27d69c788b4811b) kohsuke *2009-04-17 23:47:15*

**bumping up POM version**


[eee662039beb4dc](https://github.com/jenkinsci/violations-plugin/commit/eee662039beb4dc) kohsuke *2009-04-11 18:56:24*

**bumping up POM version**


[ea7c57b333f260f](https://github.com/jenkinsci/violations-plugin/commit/ea7c57b333f260f) kohsuke *2009-04-11 18:55:24*

**bumping up POM version**


[244f54cefc08f96](https://github.com/jenkinsci/violations-plugin/commit/244f54cefc08f96) kohsuke *2009-04-07 00:39:27*

**bumping up POM version**


[645c36967a9ba00](https://github.com/jenkinsci/violations-plugin/commit/645c36967a9ba00) kohsuke *2009-04-04 02:26:28*

**reverting rev.16752. This can be only applied after the next version ships**


[f793ede88bae4e5](https://github.com/jenkinsci/violations-plugin/commit/f793ede88bae4e5) kohsuke *2009-03-31 16:10:35*

**bumping up POM version**


[225821ebd5d0b5d](https://github.com/jenkinsci/violations-plugin/commit/225821ebd5d0b5d) kohsuke *2009-03-30 21:27:27*

**bumping up POM version**


[4efa36d79cbe41e](https://github.com/jenkinsci/violations-plugin/commit/4efa36d79cbe41e) kohsuke *2009-03-28 21:36:21*

**ignore IDE project files**


[4640a3a2ef519f0](https://github.com/jenkinsci/violations-plugin/commit/4640a3a2ef519f0) kohsuke *2009-03-25 16:36:31*

**bumping up POM version**


[c1747a4380c8ff3](https://github.com/jenkinsci/violations-plugin/commit/c1747a4380c8ff3) kohsuke *2009-03-21 02:03:22*

**bumping up POM version**


[fe9893b447b9403](https://github.com/jenkinsci/violations-plugin/commit/fe9893b447b9403) kohsuke *2009-03-14 02:18:35*

**bumping up POM version**


[9219bd0b446e1cd](https://github.com/jenkinsci/violations-plugin/commit/9219bd0b446e1cd) kohsuke *2009-03-11 05:43:17*

**bumping up POM version**


[463a07f69e26380](https://github.com/jenkinsci/violations-plugin/commit/463a07f69e26380) kohsuke *2009-03-07 01:22:06*

**bumping up POM version**


[0ccca2e382b8ed3](https://github.com/jenkinsci/violations-plugin/commit/0ccca2e382b8ed3) kohsuke *2009-03-05 19:24:39*

**bumping up POM version**


[1b605ea84ca0a91](https://github.com/jenkinsci/violations-plugin/commit/1b605ea84ca0a91) kohsuke *2009-03-03 17:25:53*

**bumping up POM version**


[7ae8a3a5e426a63](https://github.com/jenkinsci/violations-plugin/commit/7ae8a3a5e426a63) kohsuke *2009-02-27 16:06:40*

**bumping up POM version**


[413bdef1c2ce249](https://github.com/jenkinsci/violations-plugin/commit/413bdef1c2ce249) kohsuke *2009-02-26 18:34:28*

**bumping up POM version**


[80049f642c14a9c](https://github.com/jenkinsci/violations-plugin/commit/80049f642c14a9c) kohsuke *2009-02-19 22:19:27*

**bumping up POM version**


[84be33c525eb385](https://github.com/jenkinsci/violations-plugin/commit/84be33c525eb385) kohsuke *2009-02-18 05:18:51*

**bumping up POM version**


[b5f2d7da781b4b9](https://github.com/jenkinsci/violations-plugin/commit/b5f2d7da781b4b9) kohsuke *2009-02-13 19:13:17*

**fix url for violations wiki entry**


[0a613c855dcfd8d](https://github.com/jenkinsci/violations-plugin/commit/0a613c855dcfd8d) peterkittreilly *2009-02-12 10:31:05*

**attempt to fix messed up install upload**


[1e721a056e97e0e](https://github.com/jenkinsci/violations-plugin/commit/1e721a056e97e0e) peterkittreilly *2009-02-10 23:33:59*

**adding some more suppression code (from .class files)**


[f6e3714ba3bd684](https://github.com/jenkinsci/violations-plugin/commit/f6e3714ba3bd684) peterkittreilly *2009-02-10 23:03:21*

**bumping up POM version**


[efcfb42576fc689](https://github.com/jenkinsci/violations-plugin/commit/efcfb42576fc689) kohsuke *2009-02-08 22:29:00*

**bumping up POM version**


[d0388a551dbbe4e](https://github.com/jenkinsci/violations-plugin/commit/d0388a551dbbe4e) kohsuke *2009-02-06 01:02:13*

**bumping up POM version**


[f56a471b7ebc962](https://github.com/jenkinsci/violations-plugin/commit/f56a471b7ebc962) kohsuke *2009-02-01 17:47:34*

**remove debug printf**


[53863fcda809fa4](https://github.com/jenkinsci/violations-plugin/commit/53863fcda809fa4) peterkittreilly *2009-01-30 11:32:31*

**adding in a suppression class**


[98a37acea15d0ea](https://github.com/jenkinsci/violations-plugin/commit/98a37acea15d0ea) peterkittreilly *2009-01-30 11:11:17*

**bumping up POM version**


[193875d83a1701a](https://github.com/jenkinsci/violations-plugin/commit/193875d83a1701a) kohsuke *2009-01-28 19:36:42*

**bumping up POM version**


[4726f03d31dfbac](https://github.com/jenkinsci/violations-plugin/commit/4726f03d31dfbac) kohsuke *2009-01-24 03:16:50*

**bumping up POM version**


[b8a24dec97da90f](https://github.com/jenkinsci/violations-plugin/commit/b8a24dec97da90f) kohsuke *2009-01-21 03:21:15*

**bumping up POM version**


[5a4e45ce9e481e2](https://github.com/jenkinsci/violations-plugin/commit/5a4e45ce9e481e2) kohsuke *2009-01-16 21:41:12*

**bumping up POM version**


[f2a182d81746157](https://github.com/jenkinsci/violations-plugin/commit/f2a182d81746157) kohsuke *2009-01-15 18:11:15*

**[ISSUE 2814] use isFile and not exits as sometimes findbugs reports on a directory and not on a file**


[b7d782e1af9639a](https://github.com/jenkinsci/violations-plugin/commit/b7d782e1af9639a) peterkittreilly *2009-01-14 10:38:22*

**add some utility classes**


[e3ac99e5ad376c3](https://github.com/jenkinsci/violations-plugin/commit/e3ac99e5ad376c3) peterkittreilly *2009-01-13 11:59:42*

**bumping up POM version**


[4d91c65507db747](https://github.com/jenkinsci/violations-plugin/commit/4d91c65507db747) kohsuke *2009-01-13 02:45:00*

**bumping up POM version**


[152b947c6776319](https://github.com/jenkinsci/violations-plugin/commit/152b947c6776319) kohsuke *2009-01-11 19:45:06*

**bumping up POM version**


[4f117228eebd018](https://github.com/jenkinsci/violations-plugin/commit/4f117228eebd018) kohsuke *2009-01-10 18:17:28*

**bumping up POM version**


[6658220a44e608e](https://github.com/jenkinsci/violations-plugin/commit/6658220a44e608e) kohsuke *2009-01-09 22:20:54*

**bumping up POM version**


[733561474b08c36](https://github.com/jenkinsci/violations-plugin/commit/733561474b08c36) kohsuke *2009-01-06 02:10:49*

**bumping up POM version**


[cc622dd3ea26f1c](https://github.com/jenkinsci/violations-plugin/commit/cc622dd3ea26f1c) kohsuke *2009-01-05 20:33:31*

**bumping up POM version**


[3f20686720e4089](https://github.com/jenkinsci/violations-plugin/commit/3f20686720e4089) kohsuke *2008-12-28 20:22:58*

**bumping up POM version**


[767239bfc459b9c](https://github.com/jenkinsci/violations-plugin/commit/767239bfc459b9c) kohsuke *2008-12-24 20:16:52*

**bumping up POM version**


[6fcb0dce20f0f2c](https://github.com/jenkinsci/violations-plugin/commit/6fcb0dce20f0f2c) kohsuke *2008-12-20 02:06:33*

**bumping up POM version**


[9bd868b4cae55f8](https://github.com/jenkinsci/violations-plugin/commit/9bd868b4cae55f8) kohsuke *2008-12-17 18:29:41*

**bumping up POM version**


[37b7550400627d3](https://github.com/jenkinsci/violations-plugin/commit/37b7550400627d3) kohsuke *2008-12-16 17:16:20*

**bumping up POM version**


[02c7ca16765a947](https://github.com/jenkinsci/violations-plugin/commit/02c7ca16765a947) kohsuke *2008-12-11 22:33:20*

**and copying the project to the other Hudson project types**


[8aff6cb5ace3a85](https://github.com/jenkinsci/violations-plugin/commit/8aff6cb5ace3a85) stephenconnolly *2008-12-04 19:06:35*

**should be working for both build lifecycle and site lifecycle with the checkstyle plugin... needs the other plugins (findbugs and pmd though)**


[f70bad1ec6586dc](https://github.com/jenkinsci/violations-plugin/commit/f70bad1ec6586dc) stephenconnolly *2008-12-04 19:01:07*

**Here's a project... need to get the reporting plugins integrated and working though**


[a90a10c650710eb](https://github.com/jenkinsci/violations-plugin/commit/a90a10c650710eb) stephenconnolly *2008-12-04 18:18:19*

**adding some default test projects... still need to add the actual test projects within each project**


[752788cfc2c7c7c](https://github.com/jenkinsci/violations-plugin/commit/752788cfc2c7c7c) stephenconnolly *2008-12-03 23:29:54*

**Issue 2174: protect against a null healthreport from a sub project**


[6aec46c6b08c061](https://github.com/jenkinsci/violations-plugin/commit/6aec46c6b08c061) peterkittreilly *2008-12-03 07:59:46*

**bumping up POM version**


[ba88a1d3c4c2544](https://github.com/jenkinsci/violations-plugin/commit/ba88a1d3c4c2544) kohsuke *2008-11-14 18:19:06*

**bumping up POM version**


[0e40bb5695ee79b](https://github.com/jenkinsci/violations-plugin/commit/0e40bb5695ee79b) kohsuke *2008-11-12 02:43:46*

**bumping up POM version**


[167fd5da950c4c4](https://github.com/jenkinsci/violations-plugin/commit/167fd5da950c4c4) kohsuke *2008-11-06 00:46:18*

**bumping up POM version**


[610218c6edeba4d](https://github.com/jenkinsci/violations-plugin/commit/610218c6edeba4d) kohsuke *2008-11-04 01:00:29*

**bumping up POM version**


[ec1f1df7d4efbae](https://github.com/jenkinsci/violations-plugin/commit/ec1f1df7d4efbae) kohsuke *2008-10-30 20:59:35*

**bumping up POM version**


[7566ed1fbdfdd85](https://github.com/jenkinsci/violations-plugin/commit/7566ed1fbdfdd85) kohsuke *2008-10-28 22:23:06*

**bumping up POM version**


[b9b107aa5dbf9fa](https://github.com/jenkinsci/violations-plugin/commit/b9b107aa5dbf9fa) kohsuke *2008-10-25 01:34:15*

**bumping up POM version**


[d873f47c242f772](https://github.com/jenkinsci/violations-plugin/commit/d873f47c242f772) kohsuke *2008-10-02 02:21:10*

**bumping up POM version**


[793dabc47e481e4](https://github.com/jenkinsci/violations-plugin/commit/793dabc47e481e4) kohsuke *2008-09-27 01:37:24*

**bumping up POM version**


[858255bb33510ce](https://github.com/jenkinsci/violations-plugin/commit/858255bb33510ce) kohsuke *2008-09-24 23:42:39*

**bumping up POM version**


[20b179655bfec4a](https://github.com/jenkinsci/violations-plugin/commit/20b179655bfec4a) kohsuke *2008-09-02 22:55:41*

**bumping up POM version**


[6f951701b0e97dd](https://github.com/jenkinsci/violations-plugin/commit/6f951701b0e97dd) kohsuke *2008-08-23 01:03:05*

**bumping up POM version**


[80ddcb490f849e6](https://github.com/jenkinsci/violations-plugin/commit/80ddcb490f849e6) kohsuke *2008-08-22 18:15:33*

**bumping up POM version**


[feb7b80df79bbd2](https://github.com/jenkinsci/violations-plugin/commit/feb7b80df79bbd2) kohsuke *2008-08-19 18:32:29*

**bumping up POM version**


[075c612ac0fae8e](https://github.com/jenkinsci/violations-plugin/commit/075c612ac0fae8e) kohsuke *2008-08-15 02:30:56*

**bumping up POM version**


[6e048b6d5c59533](https://github.com/jenkinsci/violations-plugin/commit/6e048b6d5c59533) kohsuke *2008-08-12 03:14:23*

**bumping up POM version**


[503c9bf763826c0](https://github.com/jenkinsci/violations-plugin/commit/503c9bf763826c0) kohsuke *2008-08-12 03:13:19*

**bumping up POM version**


[9eb15cd205d402c](https://github.com/jenkinsci/violations-plugin/commit/9eb15cd205d402c) kohsuke *2008-08-06 16:41:45*

**bumping up POM version**


[630684b256ecf9a](https://github.com/jenkinsci/violations-plugin/commit/630684b256ecf9a) kohsuke *2008-08-06 01:37:55*

**bumping up POM version**


[2ffde7d5966fb5c](https://github.com/jenkinsci/violations-plugin/commit/2ffde7d5966fb5c) kohsuke *2008-08-05 00:05:32*

**bumping up POM version**


[057ac639e3032ca](https://github.com/jenkinsci/violations-plugin/commit/057ac639e3032ca) kohsuke *2008-08-01 01:37:28*

**bumping up POM version**


[f5736258bd42d0f](https://github.com/jenkinsci/violations-plugin/commit/f5736258bd42d0f) kohsuke *2008-07-31 19:26:45*

**bumping up POM version**


[194fc1bd97cba79](https://github.com/jenkinsci/violations-plugin/commit/194fc1bd97cba79) kohsuke *2008-07-31 02:11:06*

**bumping up POM version**


[f5c8417255e9e45](https://github.com/jenkinsci/violations-plugin/commit/f5c8417255e9e45) kohsuke *2008-07-30 16:23:04*

**bumping up POM version**


[23df192253c98e3](https://github.com/jenkinsci/violations-plugin/commit/23df192253c98e3) kohsuke *2008-07-26 15:13:03*

**bumping up POM version**


[d84736a404fbd3f](https://github.com/jenkinsci/violations-plugin/commit/d84736a404fbd3f) kohsuke *2008-07-23 20:52:48*

**bumping up POM version**


[278427cf3d147f3](https://github.com/jenkinsci/violations-plugin/commit/278427cf3d147f3) kohsuke *2008-07-20 22:49:12*

**bumping up POM version**


[9d8ef4572a54d3c](https://github.com/jenkinsci/violations-plugin/commit/9d8ef4572a54d3c) kohsuke *2008-07-15 00:55:05*

**reverted revision 10806 (accidental deletion)**


[90e3cd06bb09692](https://github.com/jenkinsci/violations-plugin/commit/90e3cd06bb09692) btosabre *2008-07-14 18:56:05*

**bumping up POM version**


[ff7e10b782c00ba](https://github.com/jenkinsci/violations-plugin/commit/ff7e10b782c00ba) kohsuke *2008-07-11 06:23:01*

**bumping up POM version**


[276a22c6529bb9b](https://github.com/jenkinsci/violations-plugin/commit/276a22c6529bb9b) kohsuke *2008-07-08 01:40:49*

**bumping up POM version**


[7ef1d8061e662b8](https://github.com/jenkinsci/violations-plugin/commit/7ef1d8061e662b8) kohsuke *2008-07-03 19:12:59*

**bumping up POM version**


[046c63ee5fe48a9](https://github.com/jenkinsci/violations-plugin/commit/046c63ee5fe48a9) kohsuke *2008-07-02 01:37:08*

**bumping up POM version**


[376d2073d7a1f03](https://github.com/jenkinsci/violations-plugin/commit/376d2073d7a1f03) kohsuke *2008-06-27 00:17:32*

**bumping up POM version**


[b79478a9b7bb96d](https://github.com/jenkinsci/violations-plugin/commit/b79478a9b7bb96d) kohsuke *2008-06-24 19:55:44*

**applied a patch from http://www.nabble.com/Violations-layout-issue-%28native-m2-build%29-tp18030421p18030421.html**


[e1dfcea4e2f1788](https://github.com/jenkinsci/violations-plugin/commit/e1dfcea4e2f1788) kohsuke *2008-06-24 01:07:44*

**bumping up POM version**


[d4e79d3d294def5](https://github.com/jenkinsci/violations-plugin/commit/d4e79d3d294def5) kohsuke *2008-06-23 21:08:16*

**bumping up POM version**


[152662b2a9c183c](https://github.com/jenkinsci/violations-plugin/commit/152662b2a9c183c) kohsuke *2008-06-20 16:00:28*

**bumping up POM version**


[5319728d7107439](https://github.com/jenkinsci/violations-plugin/commit/5319728d7107439) kohsuke *2008-06-17 20:17:17*

**bumping up POM version**


[ea33f43d441505b](https://github.com/jenkinsci/violations-plugin/commit/ea33f43d441505b) kohsuke *2008-06-16 21:03:07*

**bumping up POM version**


[244ac9ad88e0120](https://github.com/jenkinsci/violations-plugin/commit/244ac9ad88e0120) kohsuke *2008-06-13 20:57:26*

**bumping up POM version**


[1b10a045e6f654f](https://github.com/jenkinsci/violations-plugin/commit/1b10a045e6f654f) kohsuke *2008-06-11 19:24:06*

**bumping up POM version**


[e180f86bb012273](https://github.com/jenkinsci/violations-plugin/commit/e180f86bb012273) kohsuke *2008-06-09 18:52:38*

**bumping up POM version**


[932abc304bf3ff7](https://github.com/jenkinsci/violations-plugin/commit/932abc304bf3ff7) kohsuke *2008-06-04 00:07:51*

**bumping up POM version**


[a1905873619f3e7](https://github.com/jenkinsci/violations-plugin/commit/a1905873619f3e7) kohsuke *2008-06-02 16:17:37*

**[violations-plugin] Add support for StyleCop violation reports**


[d0e4735b7b2a915](https://github.com/jenkinsci/violations-plugin/commit/d0e4735b7b2a915) redsolo *2008-05-28 05:55:17*

**bumping up POM version**


[0a043813d71432d](https://github.com/jenkinsci/violations-plugin/commit/0a043813d71432d) kohsuke *2008-05-27 23:49:58*

**bumping up POM version**


[31b20d918e87d96](https://github.com/jenkinsci/violations-plugin/commit/31b20d918e87d96) kohsuke *2008-05-21 22:38:37*

**bumping up POM version**


[cdcd458c495780d](https://github.com/jenkinsci/violations-plugin/commit/cdcd458c495780d) kohsuke *2008-05-21 16:35:34*

**bumping up POM version**


[c9f9fb816d7af3b](https://github.com/jenkinsci/violations-plugin/commit/c9f9fb816d7af3b) kohsuke *2008-05-21 00:53:27*

**bumping up POM version**


[a001415d8c082ae](https://github.com/jenkinsci/violations-plugin/commit/a001415d8c082ae) kohsuke *2008-05-16 06:30:50*

**bumping up POM version**


[2b2f89f59f072c5](https://github.com/jenkinsci/violations-plugin/commit/2b2f89f59f072c5) kohsuke *2008-05-14 22:26:06*

**fix width attribute of pattern td in non-maven config**


[6580db419c5c65d](https://github.com/jenkinsci/violations-plugin/commit/6580db419c5c65d) peterkittreilly *2008-05-13 09:42:29*

**this was getting out of sync**


[e70be00c9655749](https://github.com/jenkinsci/violations-plugin/commit/e70be00c9655749) kohsuke *2008-05-13 00:51:31*

**make common code for the violations report table**


[71cacf27691c40b](https://github.com/jenkinsci/violations-plugin/commit/71cacf27691c40b) peterkittreilly *2008-05-06 21:57:22*

**fix 'stuck' aggregated report**


[b5033639cc1a563](https://github.com/jenkinsci/violations-plugin/commit/b5033639cc1a563) peterkittreilly *2008-05-06 21:53:56*

**fix aggregated counts and reduce number of NPEs**


[3faa29746aa0f45](https://github.com/jenkinsci/violations-plugin/commit/3faa29746aa0f45) peterkittreilly *2008-05-06 11:33:30*

**only register the ViolationsMavenReporter once**


[562f6913bb4674c](https://github.com/jenkinsci/violations-plugin/commit/562f6913bb4674c) peterkittreilly *2008-05-06 10:13:41*

**report in a violationsbuildaction may be null, especially while building a m2 project**


[a9a737c134d1ebf](https://github.com/jenkinsci/violations-plugin/commit/a9a737c134d1ebf) peterkittreilly *2008-05-06 10:06:23*

**remove system.out**


[d5bc1f63806dc9b](https://github.com/jenkinsci/violations-plugin/commit/d5bc1f63806dc9b) peterkittreilly *2008-05-05 22:37:34*

**remove system.out**


[769ca0b2be29afd](https://github.com/jenkinsci/violations-plugin/commit/769ca0b2be29afd) peterkittreilly *2008-05-05 22:06:15*

**back a couple of releases for plugin version**


[fa2260e36c232f5](https://github.com/jenkinsci/violations-plugin/commit/fa2260e36c232f5) peterkittreilly *2008-05-05 18:01:56*

**hacking at pom for findbugs**


[301eddd342fc83d](https://github.com/jenkinsci/violations-plugin/commit/301eddd342fc83d) peterkittreilly *2008-05-05 17:59:11*

**allow unstable setting to be unset**


[9189c75f9cc2f17](https://github.com/jenkinsci/violations-plugin/commit/9189c75f9cc2f17) peterkittreilly *2008-05-05 17:22:56*

**m2 and unstable**


[428fd663811a0f2](https://github.com/jenkinsci/violations-plugin/commit/428fd663811a0f2) peterkittreilly *2008-05-05 17:05:46*

**new config for m2 and for unstable**


[e904949412f6fa9](https://github.com/jenkinsci/violations-plugin/commit/e904949412f6fa9) peterkittreilly *2008-05-05 16:57:25*

**some maveny jelly pages**


[ac15b49a00ed483](https://github.com/jenkinsci/violations-plugin/commit/ac15b49a00ed483) peterkittreilly *2008-05-05 16:54:21*

**fix medium bugs from maven findbugs format**


[daad4f12c7650a7](https://github.com/jenkinsci/violations-plugin/commit/daad4f12c7650a7) peterkittreilly *2008-05-03 18:04:19*

**bumping up POM version**


[46446e8ab3e910b](https://github.com/jenkinsci/violations-plugin/commit/46446e8ab3e910b) kohsuke *2008-05-02 02:01:03*

**some more help files for the fields**


[1582ce7c3ab21db](https://github.com/jenkinsci/violations-plugin/commit/1582ce7c3ab21db) peterkittreilly *2008-05-02 00:09:08*

**bumping up POM version**


[dc736dd51fe69fa](https://github.com/jenkinsci/violations-plugin/commit/dc736dd51fe69fa) kohsuke *2008-04-28 17:31:25*

**updated svn:ignore**


[357768fc555e320](https://github.com/jenkinsci/violations-plugin/commit/357768fc555e320) elefevre *2008-04-28 09:07:49*

**more m2**


[241575c830d7418](https://github.com/jenkinsci/violations-plugin/commit/241575c830d7418) peterkittreilly *2008-04-25 09:42:05*

**bumping up POM version**


[09e948b2f430c51](https://github.com/jenkinsci/violations-plugin/commit/09e948b2f430c51) kohsuke *2008-04-24 23:53:16*

**disable M2 for the moment**


[f2fb3b2cee10c6c](https://github.com/jenkinsci/violations-plugin/commit/f2fb3b2cee10c6c) peterkittreilly *2008-04-21 22:36:47*

**more magic for maven project**


[af95107d304290c](https://github.com/jenkinsci/violations-plugin/commit/af95107d304290c) peterkittreilly *2008-04-19 13:55:18*

**bumping up POM version**


[2a849130fa058fb](https://github.com/jenkinsci/violations-plugin/commit/2a849130fa058fb) kohsuke *2008-04-19 02:17:54*

**[violations-plugin] Does not shuffle the duplication blocks around in the messages**


[4878de839725e8f](https://github.com/jenkinsci/violations-plugin/commit/4878de839725e8f) redsolo *2008-04-18 09:46:52*

**[violations-plugin] Fixed better formatting of several duplication blocks, and added testFourFileMessages() test**


[11522db84c17716](https://github.com/jenkinsci/violations-plugin/commit/11522db84c17716) redsolo *2008-04-18 09:28:00*

**[plugins-violarions] Fixed loading of files, now it works if the path contains spaces.**


[609fd8075c9daca](https://github.com/jenkinsci/violations-plugin/commit/609fd8075c9daca) redsolo *2008-04-18 09:15:12*

**[violations-plugin] Refactored test and added testTwoFileMessage()**


[7f77d8d79f1dbe5](https://github.com/jenkinsci/violations-plugin/commit/7f77d8d79f1dbe5) redsolo *2008-04-18 08:45:03*

**[violations-plugin] Added SimianDescriptor to descriptor list**


[17d8e7ff366038f](https://github.com/jenkinsci/violations-plugin/commit/17d8e7ff366038f) redsolo *2008-04-18 06:45:07*

****


[d757acc7aa35c91](https://github.com/jenkinsci/violations-plugin/commit/d757acc7aa35c91) redsolo *2008-04-18 06:43:39*

**[violations-plugin] Moved relativePath() method from CPDParser to StringUtil**


[99a95fad83a1eea](https://github.com/jenkinsci/violations-plugin/commit/99a95fad83a1eea) redsolo *2008-04-18 06:38:53*

**initial maven reporter code**


[8398bea08c24aa5](https://github.com/jenkinsci/violations-plugin/commit/8398bea08c24aa5) peterkittreilly *2008-04-17 22:38:43*

**bumping up POM version**


[ef164545f4ca2ed](https://github.com/jenkinsci/violations-plugin/commit/ef164545f4ca2ed) kohsuke *2008-04-16 22:54:09*

**bumping up POM version**


[58760c9f89c424e](https://github.com/jenkinsci/violations-plugin/commit/58760c9f89c424e) kohsuke *2008-04-16 21:15:58*

**disable freestyle descriptor appearing in the m2 job page**


[403e2a51efbdad9](https://github.com/jenkinsci/violations-plugin/commit/403e2a51efbdad9) peterkittreilly *2008-04-16 10:20:18*

**issue 1550: invalid decoding of some cpd.xml files.**

 * This changes the xml parser of cpd from xpp3 to the std dom parser. 

[e793e4b75a9f0ad](https://github.com/jenkinsci/violations-plugin/commit/e793e4b75a9f0ad) peterkittreilly *2008-04-16 10:08:24*

**issue 1558: add in explicit dependency on xpp3**


[d2d681fd67c94ce](https://github.com/jenkinsci/violations-plugin/commit/d2d681fd67c94ce) peterkittreilly *2008-04-16 09:43:37*

**bumping up POM version**


[9a0cf4e710e79af](https://github.com/jenkinsci/violations-plugin/commit/9a0cf4e710e79af) kohsuke *2008-04-15 01:30:24*

**moving to absract**


[8226948096edccb](https://github.com/jenkinsci/violations-plugin/commit/8226948096edccb) peterkittreilly *2008-04-10 11:04:56*

**bumping up POM version**


[a1072fc47f24e4f](https://github.com/jenkinsci/violations-plugin/commit/a1072fc47f24e4f) kohsuke *2008-04-09 06:42:51*

**moving to abstract**


[39d1f829e2be1da](https://github.com/jenkinsci/violations-plugin/commit/39d1f829e2be1da) peterkittreilly *2008-04-08 23:02:00*

**up the version number**


[df7991a679f031d](https://github.com/jenkinsci/violations-plugin/commit/df7991a679f031d) peterkittreilly *2008-04-08 13:33:01*

**issue 1129: handle .. in absolute paths**


[b1517e5a738ea1a](https://github.com/jenkinsci/violations-plugin/commit/b1517e5a738ea1a) peterkittreilly *2008-04-08 13:26:41*

**bumping up POM version**


[14e1f56a47d1e9f](https://github.com/jenkinsci/violations-plugin/commit/14e1f56a47d1e9f) kohsuke *2008-04-08 05:38:56*

**ignore *.iml files**


[b487feb61bf7075](https://github.com/jenkinsci/violations-plugin/commit/b487feb61bf7075) kohsuke *2008-04-07 14:57:45*

**bumping up POM version**


[6740093681c1777](https://github.com/jenkinsci/violations-plugin/commit/6740093681c1777) kohsuke *2008-04-05 08:46:25*

**bumping up POM version**


[6b25501038025a2](https://github.com/jenkinsci/violations-plugin/commit/6b25501038025a2) kohsuke *2008-04-03 09:14:54*

**bumping up POM version**


[4d958e6ece3059e](https://github.com/jenkinsci/violations-plugin/commit/4d958e6ece3059e) kohsuke *2008-04-02 04:39:41*

**attempt (not working yet) to get findbugs maven plugin to use new xml output**


[97f4204149d3be6](https://github.com/jenkinsci/violations-plugin/commit/97f4204149d3be6) peterkittreilly *2008-03-30 14:35:54*

**adding source encoding config jelly code**


[8756bd7acc745b4](https://github.com/jenkinsci/violations-plugin/commit/8756bd7acc745b4) peterkittreilly *2008-03-30 14:24:38*

**help for the encoding parameter**


[77539fcdeebd1f5](https://github.com/jenkinsci/violations-plugin/commit/77539fcdeebd1f5) peterkittreilly *2008-03-30 13:54:44*

**add support for setting the encoding of source files (java code)**


[677584b42b6516f](https://github.com/jenkinsci/violations-plugin/commit/677584b42b6516f) peterkittreilly *2008-03-30 13:50:51*

**bumping up POM version**


[2d43f81695bd109](https://github.com/jenkinsci/violations-plugin/commit/2d43f81695bd109) kohsuke *2008-03-29 16:13:31*

**remove the old findbugs messages file**


[aab64b9dd5af51b](https://github.com/jenkinsci/violations-plugin/commit/aab64b9dd5af51b) peterkittreilly *2008-03-29 14:09:20*

**use the new findbugs messages**


[691da451b27ea1e](https://github.com/jenkinsci/violations-plugin/commit/691da451b27ea1e) peterkittreilly *2008-03-29 14:07:47*

**adding new findbugs messages (1.3.3-rc2) and fb-contrib messages (3.4.2)**


[8d019a7c670e0cf](https://github.com/jenkinsci/violations-plugin/commit/8d019a7c670e0cf) peterkittreilly *2008-03-29 14:05:37*

**use xmlreader class to correct the character encoding for xpp3**


[8b8c54ac684c139](https://github.com/jenkinsci/violations-plugin/commit/8b8c54ac684c139) peterkittreilly *2008-03-29 11:53:04*

**adding XmlReader copied from maven plexus, copied from rome**


[0361b0b9e337042](https://github.com/jenkinsci/violations-plugin/commit/0361b0b9e337042) peterkittreilly *2008-03-29 11:41:25*

**correct severities for PMD violations**


[d2811b2bfd77577](https://github.com/jenkinsci/violations-plugin/commit/d2811b2bfd77577) peterkittreilly *2008-03-25 10:01:47*

**bumping up POM version**


[d8c62eba0910482](https://github.com/jenkinsci/violations-plugin/commit/d8c62eba0910482) kohsuke *2008-03-22 01:44:17*

**Added exclusion pattern for a couple of Eclipse files**


[a910e15d515b474](https://github.com/jenkinsci/violations-plugin/commit/a910e15d515b474) elefevre *2008-03-20 21:13:15*

**bumping up POM version**


[73a49b49e2dce75](https://github.com/jenkinsci/violations-plugin/commit/73a49b49e2dce75) kohsuke *2008-03-20 07:03:55*

**bumping up POM version**


[74f5b959250c2f1](https://github.com/jenkinsci/violations-plugin/commit/74f5b959250c2f1) kohsuke *2008-03-19 16:48:52*

**bumping up POM version**


[be53b0aa8201ee4](https://github.com/jenkinsci/violations-plugin/commit/be53b0aa8201ee4) kohsuke *2008-03-17 03:15:16*

**bumping up POM version**


[0e524af346299e5](https://github.com/jenkinsci/violations-plugin/commit/0e524af346299e5) kohsuke *2008-03-15 07:18:42*

**bumping up POM version**


[2e8b7357ca3f016](https://github.com/jenkinsci/violations-plugin/commit/2e8b7357ca3f016) kohsuke *2008-03-13 23:38:41*

**bumping up POM version**


[d1d8f50cdb178d0](https://github.com/jenkinsci/violations-plugin/commit/d1d8f50cdb178d0) kohsuke *2008-03-13 03:44:48*

**bumping up POM version**


[f922966418fb792](https://github.com/jenkinsci/violations-plugin/commit/f922966418fb792) kohsuke *2008-03-11 23:41:40*

**bumping up POM version**


[48e13629c0fffa1](https://github.com/jenkinsci/violations-plugin/commit/48e13629c0fffa1) kohsuke *2008-03-11 06:55:21*

**bumping up POM version**


[171cf864394b7d9](https://github.com/jenkinsci/violations-plugin/commit/171cf864394b7d9) kohsuke *2008-03-10 21:35:16*

**bumping up POM version**


[d13ea4b80f2ec0a](https://github.com/jenkinsci/violations-plugin/commit/d13ea4b80f2ec0a) kohsuke *2008-03-07 19:31:34*

**bumping up POM version**


[b859788b2bc8400](https://github.com/jenkinsci/violations-plugin/commit/b859788b2bc8400) kohsuke *2008-03-07 05:07:36*

**bumping up POM version**


[8a296c40668617e](https://github.com/jenkinsci/violations-plugin/commit/8a296c40668617e) kohsuke *2008-03-05 02:26:55*

**bumping up POM version**


[a2d5c81357af31e](https://github.com/jenkinsci/violations-plugin/commit/a2d5c81357af31e) kohsuke *2008-03-04 02:45:24*

**bumping up POM version**


[d8ae6cb012bb5fd](https://github.com/jenkinsci/violations-plugin/commit/d8ae6cb012bb5fd) kohsuke *2008-02-29 02:34:14*

**bumping up POM version**


[e4ab1589b2fb272](https://github.com/jenkinsci/violations-plugin/commit/e4ab1589b2fb272) kohsuke *2008-02-26 18:44:59*

**[violations-plugin] Fixing pom.xml before releasing**


[5c68cba760c939c](https://github.com/jenkinsci/violations-plugin/commit/5c68cba760c939c) redsolo *2008-02-25 18:54:17*

**[violation-plugin] Minor case fix**


[b5730b1ac008fed](https://github.com/jenkinsci/violations-plugin/commit/b5730b1ac008fed) redsolo *2008-02-20 09:56:33*

**[violations-plugin] Fixed so pylint errors are marked as High (errors) in the plugin**


[d6311e0bbabe283](https://github.com/jenkinsci/violations-plugin/commit/d6311e0bbabe283) redsolo *2008-02-20 09:53:20*

**bumping up POM version**


[d5f792b9cff69ba](https://github.com/jenkinsci/violations-plugin/commit/d5f792b9cff69ba) kohsuke *2008-02-13 19:12:59*

**bumping up POM version**


[3e2c97d680dd282](https://github.com/jenkinsci/violations-plugin/commit/3e2c97d680dd282) kohsuke *2008-02-10 20:03:44*

**bumping up POM version**


[73ded02f3d8f726](https://github.com/jenkinsci/violations-plugin/commit/73ded02f3d8f726) kohsuke *2008-02-09 03:47:04*

**bumping up POM version**


[39009d4520aa7d6](https://github.com/jenkinsci/violations-plugin/commit/39009d4520aa7d6) kohsuke *2008-02-08 07:42:41*

**bumping up POM version**


[4bde6be1f607015](https://github.com/jenkinsci/violations-plugin/commit/4bde6be1f607015) kohsuke *2008-02-05 08:11:04*

**bumping up POM version**


[b43ffe10d117bc0](https://github.com/jenkinsci/violations-plugin/commit/b43ffe10d117bc0) kohsuke *2008-02-03 17:21:31*

**bumping up POM version**


[432dd4d24de77c9](https://github.com/jenkinsci/violations-plugin/commit/432dd4d24de77c9) kohsuke *2008-02-03 06:46:44*

**bumping up POM version**


[df98e9ef6f55342](https://github.com/jenkinsci/violations-plugin/commit/df98e9ef6f55342) kohsuke *2008-01-30 09:16:16*

**bumping up POM version**


[060367e4bf6f8b7](https://github.com/jenkinsci/violations-plugin/commit/060367e4bf6f8b7) kohsuke *2008-01-25 03:31:28*

**bumping up POM version**


[ae7c3f187860eee](https://github.com/jenkinsci/violations-plugin/commit/ae7c3f187860eee) kohsuke *2008-01-21 22:54:27*

**bumping up POM version**


[512fd15719c5967](https://github.com/jenkinsci/violations-plugin/commit/512fd15719c5967) kohsuke *2008-01-19 07:56:46*

**bumping up POM version**


[d95a53095f5de05](https://github.com/jenkinsci/violations-plugin/commit/d95a53095f5de05) kohsuke *2008-01-17 08:18:58*

**bumping up POM version**


[a08800378df98df](https://github.com/jenkinsci/violations-plugin/commit/a08800378df98df) kohsuke *2008-01-16 07:01:25*

**bumping up POM version**


[4b2b2c50d69fd16](https://github.com/jenkinsci/violations-plugin/commit/4b2b2c50d69fd16) kohsuke *2008-01-14 06:14:14*

**bumping up POM version**


[7fc1883ec1534ca](https://github.com/jenkinsci/violations-plugin/commit/7fc1883ec1534ca) kohsuke *2008-01-13 05:57:59*

**bumping up POM version**


[a5c9536c9a76a9f](https://github.com/jenkinsci/violations-plugin/commit/a5c9536c9a76a9f) kohsuke *2008-01-12 02:57:53*

**bumping up POM version**


[5aa7c3318b67479](https://github.com/jenkinsci/violations-plugin/commit/5aa7c3318b67479) kohsuke *2008-01-11 03:10:59*

**bumping up POM version**


[9a28ba6f62fcc29](https://github.com/jenkinsci/violations-plugin/commit/9a28ba6f62fcc29) kohsuke *2008-01-10 02:23:04*

**bumping up POM version**


[ab883b4c6da7dc9](https://github.com/jenkinsci/violations-plugin/commit/ab883b4c6da7dc9) kohsuke *2008-01-05 01:54:39*

**bumping up POM version**


[6a94413105b3387](https://github.com/jenkinsci/violations-plugin/commit/6a94413105b3387) kohsuke *2008-01-02 18:33:40*

**bumping up POM version**


[638e415da856de4](https://github.com/jenkinsci/violations-plugin/commit/638e415da856de4) kohsuke *2007-12-29 05:45:17*

**bumping up POM version**


[8db07cef6b332f1](https://github.com/jenkinsci/violations-plugin/commit/8db07cef6b332f1) kohsuke *2007-12-22 01:51:13*

**bumping up POM version**


[1da993a5f1b296e](https://github.com/jenkinsci/violations-plugin/commit/1da993a5f1b296e) kohsuke *2007-12-22 01:20:19*

**bumping up POM version**


[b5afc5865edda09](https://github.com/jenkinsci/violations-plugin/commit/b5afc5865edda09) kohsuke *2007-12-16 06:01:38*

**<thead> only marks the <tr>'s that are the header, it does not provide an implicit <tr>**

 * Fixes some rendering issues 

[632114a99ac433c](https://github.com/jenkinsci/violations-plugin/commit/632114a99ac433c) stephenconnolly *2007-12-13 08:48:37*

**bumping up POM version**


[5de1330f79266b1](https://github.com/jenkinsci/violations-plugin/commit/5de1330f79266b1) kohsuke *2007-11-30 19:16:49*

**bumping up to 1.159**


[c4f255fd2593633](https://github.com/jenkinsci/violations-plugin/commit/c4f255fd2593633) kohsuke *2007-11-28 00:50:09*

**added URL and moved junit dependency up to the plugin root POM**


[644c43d07b145a0](https://github.com/jenkinsci/violations-plugin/commit/644c43d07b145a0) kohsuke *2007-11-23 00:04:27*

**update to version 1.3.0 of findbugs**


[903866a6a22f215](https://github.com/jenkinsci/violations-plugin/commit/903866a6a22f215) peterkittreilly *2007-11-22 15:17:39*

**bumping up POM version**


[7719e24e26a88ac](https://github.com/jenkinsci/violations-plugin/commit/7719e24e26a88ac) kohsuke *2007-11-14 02:30:13*

**bumping up POM version**


[9a4263f70c82e9d](https://github.com/jenkinsci/violations-plugin/commit/9a4263f70c82e9d) kohsuke *2007-11-13 17:24:13*

**bumping up POM version**


[ce718503f352e85](https://github.com/jenkinsci/violations-plugin/commit/ce718503f352e85) kohsuke *2007-11-13 03:18:19*

**bumping up POM version**


[e6bace1071a38e6](https://github.com/jenkinsci/violations-plugin/commit/e6bace1071a38e6) kohsuke *2007-11-06 21:20:24*

**bumping up POM version**


[b2a780f17f3a21f](https://github.com/jenkinsci/violations-plugin/commit/b2a780f17f3a21f) kohsuke *2007-11-06 01:50:17*

**bumping up POM version**


[af7e36e6f6e67a8](https://github.com/jenkinsci/violations-plugin/commit/af7e36e6f6e67a8) kohsuke *2007-11-04 05:00:15*

**bumping up POM version**


[6d6cc400c5069a6](https://github.com/jenkinsci/violations-plugin/commit/6d6cc400c5069a6) kohsuke *2007-10-31 00:55:58*

**bumping up POM version**


[b697260e36cdbc2](https://github.com/jenkinsci/violations-plugin/commit/b697260e36cdbc2) kohsuke *2007-10-24 05:54:32*

**update to 0.5.3**


[2318a893fc0037b](https://github.com/jenkinsci/violations-plugin/commit/2318a893fc0037b) peterkittreilly *2007-10-23 17:20:31*

**bumping up POM version**


[0c842e0093e93d9](https://github.com/jenkinsci/violations-plugin/commit/0c842e0093e93d9) kohsuke *2007-10-22 22:42:48*

**bumping up POM version**


[4fdb4c900c10ee1](https://github.com/jenkinsci/violations-plugin/commit/4fdb4c900c10ee1) kohsuke *2007-10-20 01:23:54*

**getUrlName() shouldn't return null.**


[dc00743f6ed00ab](https://github.com/jenkinsci/violations-plugin/commit/dc00743f6ed00ab) kohsuke *2007-10-19 17:33:08*

**off-by-one**


[ac047b3c9e64032](https://github.com/jenkinsci/violations-plugin/commit/ac047b3c9e64032) peterkittreilly *2007-10-10 16:49:15*

**bumping up POM version**


[fdf93e6598134c6](https://github.com/jenkinsci/violations-plugin/commit/fdf93e6598134c6) kohsuke *2007-10-10 06:11:27*

**bumping up POM version**


[f6708151ceffccf](https://github.com/jenkinsci/violations-plugin/commit/f6708151ceffccf) kohsuke *2007-10-05 01:07:53*

**matched the version number**


[1700a2bb0eb408c](https://github.com/jenkinsci/violations-plugin/commit/1700a2bb0eb408c) kohsuke *2007-09-28 16:36:21*

**set version to 0.5.1, and hudson version to 1.142 (do not want to upgrade my mvn cache just yet**


[e633353cec18121](https://github.com/jenkinsci/violations-plugin/commit/e633353cec18121) peterkittreilly *2007-09-28 09:14:42*

**use IOException2 for IO exception chaining, version to 0.5.1**


[345bce8a30bfd0b](https://github.com/jenkinsci/violations-plugin/commit/345bce8a30bfd0b) peterkittreilly *2007-09-28 09:10:46*

**bumping up POM version**


[08b1c77c07fdde8](https://github.com/jenkinsci/violations-plugin/commit/08b1c77c07fdde8) kohsuke *2007-09-28 02:26:44*

**bumping up POM version**


[4a50781d6d1daac](https://github.com/jenkinsci/violations-plugin/commit/4a50781d6d1daac) kohsuke *2007-09-24 23:56:15*

**make this 0.5**


[fd89fc6d5b20f09](https://github.com/jenkinsci/violations-plugin/commit/fd89fc6d5b20f09) peterkittreilly *2007-09-24 14:31:08*

**make icon color on file line match severity**


[885df3d514ec86b](https://github.com/jenkinsci/violations-plugin/commit/885df3d514ec86b) peterkittreilly *2007-09-24 14:26:20*

**bumping up POM version**


[7040848f961f6dd](https://github.com/jenkinsci/violations-plugin/commit/7040848f961f6dd) kohsuke *2007-09-22 17:41:12*

**bumping up POM version**


[a664b39e1a429fe](https://github.com/jenkinsci/violations-plugin/commit/a664b39e1a429fe) kohsuke *2007-09-22 05:23:23*

**Fixed a NPE**


[c0c9180965156ae](https://github.com/jenkinsci/violations-plugin/commit/c0c9180965156ae) redsolo *2007-09-20 21:25:34*

**bumping up POM version**


[9bcdcf409506ae7](https://github.com/jenkinsci/violations-plugin/commit/9bcdcf409506ae7) kohsuke *2007-09-20 04:52:26*

**bumping up POM version**


[7a04849c1d20af0](https://github.com/jenkinsci/violations-plugin/commit/7a04849c1d20af0) kohsuke *2007-09-17 01:32:21*

**Reworked parsing of FxCop.**

 * Added parsing of rules tags to add more info to the rules 

[162a73b9bb881b0](https://github.com/jenkinsci/violations-plugin/commit/162a73b9bb881b0) redsolo *2007-09-16 20:59:58*

**bumping up POM version**


[f0c875510dc23cb](https://github.com/jenkinsci/violations-plugin/commit/f0c875510dc23cb) kohsuke *2007-09-15 01:10:02*

**bumping up POM version**


[93f00c791d97a6a](https://github.com/jenkinsci/violations-plugin/commit/93f00c791d97a6a) kohsuke *2007-09-13 05:41:26*

**Uses the AbsoluteFileFinder to find the absolute path for files**


[1c33f53266e518a](https://github.com/jenkinsci/violations-plugin/commit/1c33f53266e518a) redsolo *2007-09-12 20:37:07*

**Moved getFileFromName() to AbsoluteFileFinder**


[af093674a4b10e9](https://github.com/jenkinsci/violations-plugin/commit/af093674a4b10e9) redsolo *2007-09-12 20:36:22*

**Initial version**


[c56fd8cc529aa48](https://github.com/jenkinsci/violations-plugin/commit/c56fd8cc529aa48) redsolo *2007-09-12 20:34:29*

**Inital addd for FxCop support**


[569637e45cd441d](https://github.com/jenkinsci/violations-plugin/commit/569637e45cd441d) redsolo *2007-09-10 06:58:26*

**Fixed so relative files are looked up using the source paths.**


[9f1895a42b8f54a](https://github.com/jenkinsci/violations-plugin/commit/9f1895a42b8f54a) redsolo *2007-09-09 16:26:34*

**bumping up POM version**


[bb2ef5278ebe02c](https://github.com/jenkinsci/violations-plugin/commit/bb2ef5278ebe02c) kohsuke *2007-09-05 00:24:46*

**bumping up POM version**


[b9a74ffcd3f78d0](https://github.com/jenkinsci/violations-plugin/commit/b9a74ffcd3f78d0) kohsuke *2007-09-03 06:59:38*

**bumping up POM version**


[bd1525ffc1afeaf](https://github.com/jenkinsci/violations-plugin/commit/bd1525ffc1afeaf) kohsuke *2007-09-01 16:34:37*

**moving types**


[cd35ad54f138824](https://github.com/jenkinsci/violations-plugin/commit/cd35ad54f138824) peterkittreilly *2007-08-31 08:59:36*

**checkstyle**


[a7aacf11f250d18](https://github.com/jenkinsci/violations-plugin/commit/a7aacf11f250d18) peterkittreilly *2007-08-31 08:53:07*

**moving types**


[bd55d7c20fd72dd](https://github.com/jenkinsci/violations-plugin/commit/bd55d7c20fd72dd) peterkittreilly *2007-08-31 08:52:37*

**checkstyle**


[23a8404dcc060fb](https://github.com/jenkinsci/violations-plugin/commit/23a8404dcc060fb) peterkittreilly *2007-08-31 08:44:41*

**update**


[66fd5b1022ede9d](https://github.com/jenkinsci/violations-plugin/commit/66fd5b1022ede9d) peterkittreilly *2007-08-31 08:44:07*

**moving violation type parsing**


[96c7fb502a4f7a4](https://github.com/jenkinsci/violations-plugin/commit/96c7fb502a4f7a4) peterkittreilly *2007-08-31 08:41:00*

**moving violation types**


[0a1f5d6bd56cecb](https://github.com/jenkinsci/violations-plugin/commit/0a1f5d6bd56cecb) peterkittreilly *2007-08-31 08:28:40*

**Added PyLintParser and PyLintDescriptor**


[c33ac011afeac0d](https://github.com/jenkinsci/violations-plugin/commit/c33ac011afeac0d) redsolo *2007-08-30 12:08:21*

**bumping up POM version**


[b95f4baa35b2d94](https://github.com/jenkinsci/violations-plugin/commit/b95f4baa35b2d94) kohsuke *2007-08-29 04:41:43*

**change parser api to allow non-xml violation files**


[eaf3db5f70d37eb](https://github.com/jenkinsci/violations-plugin/commit/eaf3db5f70d37eb) peterkittreilly *2007-08-28 09:33:25*

**modify +- code**


[7ee86d60346b8cd](https://github.com/jenkinsci/violations-plugin/commit/7ee86d60346b8cd) peterkittreilly *2007-08-28 09:29:21*

**ignoring target**


[ab833c13404702a](https://github.com/jenkinsci/violations-plugin/commit/ab833c13404702a) kohsuke *2007-08-28 04:16:53*

**bumping up POM version**


[6d973e9ad128ed8](https://github.com/jenkinsci/violations-plugin/commit/6d973e9ad128ed8) kohsuke *2007-08-23 01:23:11*

**bumping up POM version**


[56e5075585187af](https://github.com/jenkinsci/violations-plugin/commit/56e5075585187af) kohsuke *2007-08-21 23:59:59*

**bumping up POM version**


[376accb7e237d37](https://github.com/jenkinsci/violations-plugin/commit/376accb7e237d37) kohsuke *2007-08-18 19:47:40*

**bumping up POM version**


[a9cbb562a00e4d5](https://github.com/jenkinsci/violations-plugin/commit/a9cbb562a00e4d5) kohsuke *2007-08-15 23:14:13*

**change number 0.5 for new code**


[499e8706791445e](https://github.com/jenkinsci/violations-plugin/commit/499e8706791445e) peterkittreilly *2007-08-15 12:49:00*

**change number 0.4**


[a97743ad7ee4173](https://github.com/jenkinsci/violations-plugin/commit/a97743ad7ee4173) peterkittreilly *2007-08-15 12:48:27*

**source for 0.4**


[21c8acb910839a5](https://github.com/jenkinsci/violations-plugin/commit/21c8acb910839a5) peterkittreilly *2007-08-15 12:47:45*

**adding prev and sorting**


[ec4e61b44b452af](https://github.com/jenkinsci/violations-plugin/commit/ec4e61b44b452af) peterkittreilly *2007-08-15 12:43:17*

**update the style**


[bff7c3156708da3](https://github.com/jenkinsci/violations-plugin/commit/bff7c3156708da3) peterkittreilly *2007-08-15 12:41:02*

**adding icons**


[75be612861c3815](https://github.com/jenkinsci/violations-plugin/commit/75be612861c3815) peterkittreilly *2007-08-15 12:39:15*

**add the svg files**


[3a69c4f34b42739](https://github.com/jenkinsci/violations-plugin/commit/3a69c4f34b42739) peterkittreilly *2007-08-15 12:36:09*

**adding icons**


[4373f6114532cbe](https://github.com/jenkinsci/violations-plugin/commit/4373f6114532cbe) peterkittreilly *2007-08-15 11:17:28*

**marking a few classes as serializable, as these classes are serialized during remote operation.**


[28172d11bac4449](https://github.com/jenkinsci/violations-plugin/commit/28172d11bac4449) kohsuke *2007-08-12 23:16:09*

**bumping up POM version**


[62d585c2165b13c](https://github.com/jenkinsci/violations-plugin/commit/62d585c2165b13c) kohsuke *2007-08-11 06:13:50*

**Updates for version 0.3:**

 * fix npes for findbugs type 
 * better parsing of findbugs xml files 
 * allow users to specify source code directories 
 * allow users to specify location of project 
 * help now points to wiki page 

[8b72ee52511f3fb](https://github.com/jenkinsci/violations-plugin/commit/8b72ee52511f3fb) peterkittreilly *2007-08-10 11:45:20*

**use the correct checkstyle.xml**


[4e9db2512f759fa](https://github.com/jenkinsci/violations-plugin/commit/4e9db2512f759fa) peterkittreilly *2007-08-09 16:06:21*

**remove trailing ws**


[a8d29665060f6e5](https://github.com/jenkinsci/violations-plugin/commit/a8d29665060f6e5) peterkittreilly *2007-08-09 16:05:48*

**Adding the pom.xml - thanks Stephen**


[8adef967cfad3f1](https://github.com/jenkinsci/violations-plugin/commit/8adef967cfad3f1) peterkittreilly *2007-08-09 15:59:36*

**allow compile with hudson 1.127**


[d0244e1251ac176](https://github.com/jenkinsci/violations-plugin/commit/d0244e1251ac176) peterkittreilly *2007-08-09 15:58:15*

**the checkstyle config file**


[24985a2fc31d5d1](https://github.com/jenkinsci/violations-plugin/commit/24985a2fc31d5d1) peterkittreilly *2007-08-09 10:58:24*

**adding jelly scripts and findbugs messages file**


[678e93e5decf36d](https://github.com/jenkinsci/violations-plugin/commit/678e93e5decf36d) peterkittreilly *2007-08-09 10:56:34*

**adding java files**


[fb3cbc9a8e382e8](https://github.com/jenkinsci/violations-plugin/commit/fb3cbc9a8e382e8) peterkittreilly *2007-08-09 10:52:22*

**adding html files**


[d7bb0dd58d242d1](https://github.com/jenkinsci/violations-plugin/commit/d7bb0dd58d242d1) peterkittreilly *2007-08-09 10:50:13*

**adding icons and html files**


[6e6c41c53f95414](https://github.com/jenkinsci/violations-plugin/commit/6e6c41c53f95414) peterkittreilly *2007-08-09 10:49:16*


## 0.7.9
### Jira JENKINS-1850 

**[FIXED JENKINS-1850] multi module m2 project - dead links**


[388c80802bd7c07](https://github.com/jenkinsci/violations-plugin/commit/388c80802bd7c07) Peter Reilly *2011-05-17 15:06:11*


### No issue

**add Mirko as a contributor**


[48a5032015e955e](https://github.com/jenkinsci/violations-plugin/commit/48a5032015e955e) Peter Reilly *2011-05-19 11:38:09*

**readd work**


[80c8bf75a60fe94](https://github.com/jenkinsci/violations-plugin/commit/80c8bf75a60fe94) Peter Reilly *2011-05-18 13:14:18*

**do not ignore work**


[b74958415b6e246](https://github.com/jenkinsci/violations-plugin/commit/b74958415b6e246) Peter Reilly *2011-05-18 13:12:47*

**tidy-up the pom and the build a tad**


[2668d963c4d822c](https://github.com/jenkinsci/violations-plugin/commit/2668d963c4d822c) Stephen Connolly *2011-05-17 12:40:54*


## 0.7.8
### No issue

**add more magic from wiki to pom**


[dbdcd3644aff9b2](https://github.com/jenkinsci/violations-plugin/commit/dbdcd3644aff9b2) Peter Reilly *2011-05-17 11:46:37*

**fix .gitignore again and add distrib management to pom.xml**


[4ab14e966658ad8](https://github.com/jenkinsci/violations-plugin/commit/4ab14e966658ad8) Peter Reilly *2011-05-17 11:38:57*

**initial attempt at magic changes to pom.xml, set scm and issue stuff, leave hudson/jenkins plugin and maven plugin the same**


[cac68deede68963](https://github.com/jenkinsci/violations-plugin/commit/cac68deede68963) Peter Reilly *2011-05-17 11:23:15*

**fix .gitignore**


[d80ba4e5b1e1bdc](https://github.com/jenkinsci/violations-plugin/commit/d80ba4e5b1e1bdc) Peter Reilly *2011-05-17 11:21:08*

**opps: remove work**


[a07e4da6874cb4f](https://github.com/jenkinsci/violations-plugin/commit/a07e4da6874cb4f) Peter Reilly *2011-05-17 11:20:23*

**adding .gitignore**


[fa0826274cce064](https://github.com/jenkinsci/violations-plugin/commit/fa0826274cce064) Peter Reilly *2011-05-16 11:24:48*


