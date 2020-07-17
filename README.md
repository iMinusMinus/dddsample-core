# DDDSample
改造成maven多模块形式。

domain模块做成各模块中心，domain模块只依赖于一些util，这些util可以单独抽成util模块，或者保留在domain中。

domain模块的测试类原先依赖infrastructure的repository实现，那招domain不依赖原则，改成mock形式。

infrastructure模块的测试类依赖application的SampleDataGenerator，其它地方未使用，直接移到infrastructure。

[![Build Status](https://travis-ci.org/citerus/dddsample-core.svg?branch=master)](https://travis-ci.org/citerus/dddsample-core)
