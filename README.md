# DDDSample
改造成maven多模块形式。

domain模块做成各模块中心，domain模块只依赖于一些util，这些util可以单独抽成util模块，或者保留在domain中。

domain模块的测试类原先依赖infrastructure的repository实现，按照domain不依赖原则，改成mock形式。

独立启动类到单独的boot模块，该模块依赖其它所有模块。如果启动类放在interfaces模块，则infrastructure模块的配置无法处理，导致依赖获取不到，启动失败。
如果启动类放在infrastructure，则导致infrastructure依赖所有模块。

由于原来在interfaces下的一些测试类依赖启动类，一并移到boot模块。现在boot模块下的src/test/java可以放集成测试类了。

[![Build Status](https://travis-ci.org/citerus/dddsample-core.svg?branch=master)](https://travis-ci.org/citerus/dddsample-core)
