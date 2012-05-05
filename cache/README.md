Build and Dependencies
======================

Maven 2 build system is used for build. Cache implementation does not
use any 3rd party libraries, only JUnit 4.10 for unit testing.

Cache configuration
===================

2-level cache is presented by `MultiLevelCache` class.

Cache configuration can be done with `CacheBuilder` class instance. It
allows to configure the following parameters:

* In-memory and file cache size.
* Default entries expiration time.
* Entry replacement strategy.
* Serialization mechanism for file cache.
* Root directory for file cache storage.

`CacheBuilder` holds reasonable defaults for all the configuration
parameters.

Additional cache configuration can be done with cache decoration using
methods provided by `Caches` class. It allows to make cache instance
synchronized (to work in multithread environment) and/or auto-cleaning
(to delete the expired entries automatically by timer).

Implemented Replacement Strategies
----------------------------------

* LRU (least recently used)
* MRU (most recently used)
* LFU (least recently used)
* Random (selects random entry for replacement)

Possible improvements
=====================

* Improve multithreading support for in-memory and file caches.
* Add ability to set-up different cleanup timers for in-memory and
  file caches (since file cache cleanup takes much more time than
  in-memory cache cleanup).
* More descriptive and helpful logging.
