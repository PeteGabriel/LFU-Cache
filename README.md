# LFU-Cache
LFU cache with runtime complexity of O(1)

Implementation is based upon this [paper](http://dhruvbird.com/lfu.pdf).

### Abstract

> Cache eviction algorithms are used widely in operating systems, databases
and other systems that use caches to speed up execution by caching data
that is used by the application. There are many policies such as MRU
(Most  Recently  Used),  MFU  (Most  Frequently  Used),  LRU  (Least  Re-
cently  Used)  and  LFU  (Least  Frequently  Used)  which  each  have  their
advantages and drawbacks and are hence used in specific scenarios.  By
far,  the most widely used algorithm is LRU, both for its O(1) speed of
operation as well as its close resemblance to the kind of behaviour that
is expected by most applications.  

### Data structures

* Doubly-linked list to hold frequency nodes;
* HashMap to reach any element in contant time;
* Doubly-linked list to hold nodes of a certain frequency.

### Implementation

This LFU algorithm has a runtime complexity of O(1) for each of the
dictionary operations (insertion, lookup and deletion) that can be performed on
an LFU cache.  

This insert operation has runtime of O(1) because every item being added to the cache
does it so with the frequency of access of 1. Therefore, we search for the frequency node of 1 and if it
exists we add the new item to it otherwise we create the frequency node before adding to it.

The access operation is achieved bu indexing into the HashMap with a key that must be unique to every element
present in the cache. Accessing an element also moves it to another list to comply with it´s new frequency value. 
If the previously frequency node gets empty after that move, the node is evicted from the cache. Removing a certain node is done 
in constant time by just removing the references to it.

2 linked lists; one on the access frequency and one for all elements that have the same access frequency.
A hash table is used to access elements by key.

This set of nodes that have the same access frequency is actually a doubly
linked list of such nodes (shown as circular nodes in the diagram below).  

To facilitate moving a certain node to another list of frequencies each node in the internal list 
has a pointer to its parent node in the frequency list. In the image below, nodes x will have a pointer back to node 1.

![fig1](https://cdn-images-1.medium.com/max/1600/1*CXc375NnOfkgT50a2VSuJA.jpeg)
