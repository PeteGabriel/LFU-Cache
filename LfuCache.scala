package LFU

import java.util.UUID
import scala.collection.mutable

class LfuCache {

  val byKey = mutable.HashMap[UUID, LfuItem]()
  val frequencyHead = FrequencyNode(-1)

  def deleteNode(node: FrequencyNode): Unit = {
    node.prev.next = node.next
    node.next.prev = node.prev
    node.next= null; node.prev = null
  }

  def access(key: UUID): Int = {
    val tmp = this.byKey(key)
    if (tmp == null) {
      throw new Exception("No such key")
    }

    val freq = tmp.parent
    var nextFreq = freq.next

    if (nextFreq == this.frequencyHead || nextFreq.value != (freq.value + 1)) {
      nextFreq = LfuCache.getNewNode(freq, nextFreq, freq.value + 1)
    }

    nextFreq.items += this.byKey(key)
    tmp.parent = nextFreq

    nextFreq.prev.items -= nextFreq.prev.items.filter(freq => freq.key == key).head
    if (nextFreq.prev.items.isEmpty){
      this.deleteNode(freq)
    }

    tmp.data
  }

  def insert(key: UUID, value : Int) = {
    if (this.byKey.contains(key)){
      throw new Exception("Key already exists")
    }

    val freqNode = this.frequencyHead.next
    if (freqNode.value != 1){
      this.frequencyHead.next = LfuCache.getNewNode(this.frequencyHead, freqNode)
      this.byKey(key) = LfuItem(this.frequencyHead.next, value, key)
      this.frequencyHead.next.items += this.byKey(key)
    }else{
      this.byKey(key) = LfuItem(freqNode, value, key)
      freqNode.items += this.byKey(key)
    }

  }

  def getLfuItem(): LfuItem = {
    if (this.byKey.isEmpty) {
      throw new Exception("The set is empty")
    }
    this.byKey(this.frequencyHead.next.items.head.key)
  }
}

case class LfuItem(var parent: FrequencyNode, data : Int, key: UUID = UUID.randomUUID()){ }

case class FrequencyNode(value: Int = 1) {
  val items: mutable.ArrayBuffer[LfuItem] = mutable.ArrayBuffer[LfuItem]()
  var prev: FrequencyNode = this
  var next: FrequencyNode = this
}

object LfuCache {

  def getNewNode(prev: FrequencyNode, next: FrequencyNode, freqValue: Int = 1): FrequencyNode = {
    val node = FrequencyNode(freqValue)
    node.prev = prev
    node.next = next
    node.prev.next = node
    node.next.prev = node
    node
  }

}
