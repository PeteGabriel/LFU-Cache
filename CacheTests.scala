package LFU_Cache

import java.util.UUID

import LFU.LfuCache
import org.scalatest.FunSuite

class CacheTests extends FunSuite{

  test("Main behavior is correct") {
    val cache = new LfuCache

    val key = UUID.randomUUID();
    cache.insert(key, 1000)

    val key1 = UUID.randomUUID();
    cache.insert(key1, 1001)

    val key2 = UUID.randomUUID();
    cache.insert(key2, 1002)

    cache.access(key1)
    cache.access(key2)
    cache.access(key2)

    //Assert head values
    assert(cache.frequencyHead.items.isEmpty)
    assert(cache.frequencyHead.value === -1)
    //assert first frequency node values
    assert(cache.frequencyHead.next.value === 1)
    assert(cache.frequencyHead.next.items.size === 1)

    //assert second frequency node values
    assert(cache.frequencyHead.next.next.value === 2)
    assert(cache.frequencyHead.next.next.items.size === 1)

    //assert third frequency node values
    assert(cache.frequencyHead.next.next.next.value === 3)
    assert(cache.frequencyHead.next.next.next.items.size === 1)

  }

  test("Frequency nodes without items should be removed from cache") {
    val cache = new LfuCache

    val key1 = UUID.randomUUID();
    cache.insert(key1, 1001)

    val key2 = UUID.randomUUID();
    cache.insert(key2, 1002)

    //Assert head values
    assert(cache.frequencyHead.items.isEmpty)
    assert(cache.frequencyHead.value === -1)

    //assert first frequency node values
    assert(cache.frequencyHead.next.value === 1)
    assert(cache.frequencyHead.next.items.size === 2)

    cache.access(key1)
    cache.access(key2)

    //after access, expect frequency nodes to have been removed
    //assert first frequency node values
    assert(cache.frequencyHead.next.value === 2)
    assert(cache.frequencyHead.next.items.size === 2)
  }

  test("We should be able to delete frequency nodes") {
    val cache = new LfuCache

    val key1 = UUID.randomUUID();
    cache.insert(key1, 1001)

    val key2 = UUID.randomUUID();
    cache.insert(key2, 1002)

    //Assert head values
    assert(cache.frequencyHead.items.isEmpty)
    assert(cache.frequencyHead.value === -1)

    //assert first frequency node values
    assert(cache.frequencyHead.next.value === 1)
    assert(cache.frequencyHead.next.items.size === 2)

    cache.deleteNode(cache.frequencyHead.next)

    //assert first frequency node values
    assert(cache.frequencyHead.next.value === -1)
    assert(cache.frequencyHead.next.items.size === 0)
  }

}
