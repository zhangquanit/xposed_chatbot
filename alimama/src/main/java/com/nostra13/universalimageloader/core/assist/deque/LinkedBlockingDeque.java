package com.nostra13.universalimageloader.core.assist.deque;

import com.taobao.weex.el.parse.Operators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedBlockingDeque<E> extends AbstractQueue<E> implements BlockingDeque<E>, Serializable {
    private static final long serialVersionUID = -387911632671998426L;
    private final int capacity;
    private transient int count;
    transient Node<E> first;
    transient Node<E> last;
    final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    static final class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(E e) {
            this.item = e;
        }
    }

    public LinkedBlockingDeque() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingDeque(int i) {
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        if (i > 0) {
            this.capacity = i;
            return;
        }
        throw new IllegalArgumentException();
    }

    public LinkedBlockingDeque(Collection<? extends E> collection) {
        this(Integer.MAX_VALUE);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            for (Object next : collection) {
                if (next == null) {
                    throw new NullPointerException();
                } else if (!linkLast(new Node(next))) {
                    throw new IllegalStateException("Deque full");
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    private boolean linkFirst(Node<E> node) {
        if (this.count >= this.capacity) {
            return false;
        }
        Node<E> node2 = this.first;
        node.next = node2;
        this.first = node;
        if (this.last == null) {
            this.last = node;
        } else {
            node2.prev = node;
        }
        this.count++;
        this.notEmpty.signal();
        return true;
    }

    private boolean linkLast(Node<E> node) {
        if (this.count >= this.capacity) {
            return false;
        }
        Node<E> node2 = this.last;
        node.prev = node2;
        this.last = node;
        if (this.first == null) {
            this.first = node;
        } else {
            node2.next = node;
        }
        this.count++;
        this.notEmpty.signal();
        return true;
    }

    private E unlinkFirst() {
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        Node<E> node2 = node.next;
        E e = node.item;
        node.item = null;
        node.next = node;
        this.first = node2;
        if (node2 == null) {
            this.last = null;
        } else {
            node2.prev = null;
        }
        this.count--;
        this.notFull.signal();
        return e;
    }

    private E unlinkLast() {
        Node<E> node = this.last;
        if (node == null) {
            return null;
        }
        Node<E> node2 = node.prev;
        E e = node.item;
        node.item = null;
        node.prev = node;
        this.last = node2;
        if (node2 == null) {
            this.first = null;
        } else {
            node2.next = null;
        }
        this.count--;
        this.notFull.signal();
        return e;
    }

    /* access modifiers changed from: package-private */
    public void unlink(Node<E> node) {
        Node<E> node2 = node.prev;
        Node<E> node3 = node.next;
        if (node2 == null) {
            unlinkFirst();
        } else if (node3 == null) {
            unlinkLast();
        } else {
            node2.next = node3;
            node3.prev = node2;
            node.item = null;
            this.count--;
            this.notFull.signal();
        }
    }

    public void addFirst(E e) {
        if (!offerFirst(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    public void addLast(E e) {
        if (!offerLast(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    public boolean offerFirst(E e) {
        if (e != null) {
            Node node = new Node(e);
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return linkFirst(node);
            } finally {
                reentrantLock.unlock();
            }
        } else {
            throw new NullPointerException();
        }
    }

    public boolean offerLast(E e) {
        if (e != null) {
            Node node = new Node(e);
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return linkLast(node);
            } finally {
                reentrantLock.unlock();
            }
        } else {
            throw new NullPointerException();
        }
    }

    public void putFirst(E e) throws InterruptedException {
        if (e != null) {
            Node node = new Node(e);
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            while (!linkFirst(node)) {
                try {
                    this.notFull.await();
                } finally {
                    reentrantLock.unlock();
                }
            }
            return;
        }
        throw new NullPointerException();
    }

    public void putLast(E e) throws InterruptedException {
        if (e != null) {
            Node node = new Node(e);
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            while (!linkLast(node)) {
                try {
                    this.notFull.await();
                } finally {
                    reentrantLock.unlock();
                }
            }
            return;
        }
        throw new NullPointerException();
    }

    public boolean offerFirst(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        boolean z;
        if (e != null) {
            Node node = new Node(e);
            long nanos = timeUnit.toNanos(j);
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lockInterruptibly();
            while (true) {
                try {
                    if (linkFirst(node)) {
                        z = true;
                        break;
                    } else if (nanos <= 0) {
                        z = false;
                        break;
                    } else {
                        nanos = this.notFull.awaitNanos(nanos);
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
            return z;
        }
        throw new NullPointerException();
    }

    public boolean offerLast(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        boolean z;
        if (e != null) {
            Node node = new Node(e);
            long nanos = timeUnit.toNanos(j);
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lockInterruptibly();
            while (true) {
                try {
                    if (linkLast(node)) {
                        z = true;
                        break;
                    } else if (nanos <= 0) {
                        z = false;
                        break;
                    } else {
                        nanos = this.notFull.awaitNanos(nanos);
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
            return z;
        }
        throw new NullPointerException();
    }

    public E removeFirst() {
        E pollFirst = pollFirst();
        if (pollFirst != null) {
            return pollFirst;
        }
        throw new NoSuchElementException();
    }

    public E removeLast() {
        E pollLast = pollLast();
        if (pollLast != null) {
            return pollLast;
        }
        throw new NoSuchElementException();
    }

    public E pollFirst() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return unlinkFirst();
        } finally {
            reentrantLock.unlock();
        }
    }

    public E pollLast() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return unlinkLast();
        } finally {
            reentrantLock.unlock();
        }
    }

    public E takeFirst() throws InterruptedException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        while (true) {
            try {
                E unlinkFirst = unlinkFirst();
                if (unlinkFirst != null) {
                    return unlinkFirst;
                }
                this.notEmpty.await();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public E takeLast() throws InterruptedException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        while (true) {
            try {
                E unlinkLast = unlinkLast();
                if (unlinkLast != null) {
                    return unlinkLast;
                }
                this.notEmpty.await();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public E pollFirst(long j, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                E unlinkFirst = unlinkFirst();
                if (unlinkFirst != null) {
                    reentrantLock.unlock();
                    return unlinkFirst;
                } else if (nanos <= 0) {
                    return null;
                } else {
                    nanos = this.notEmpty.awaitNanos(nanos);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public E pollLast(long j, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                E unlinkLast = unlinkLast();
                if (unlinkLast != null) {
                    reentrantLock.unlock();
                    return unlinkLast;
                } else if (nanos <= 0) {
                    return null;
                } else {
                    nanos = this.notEmpty.awaitNanos(nanos);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public E getFirst() {
        E peekFirst = peekFirst();
        if (peekFirst != null) {
            return peekFirst;
        }
        throw new NoSuchElementException();
    }

    public E getLast() {
        E peekLast = peekLast();
        if (peekLast != null) {
            return peekLast;
        }
        throw new NoSuchElementException();
    }

    public E peekFirst() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.first == null ? null : this.first.item;
        } finally {
            reentrantLock.unlock();
        }
    }

    public E peekLast() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.last == null ? null : this.last.item;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean removeFirstOccurrence(Object obj) {
        if (obj == null) {
            return false;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            for (Node<E> node = this.first; node != null; node = node.next) {
                if (obj.equals(node.item)) {
                    unlink(node);
                    return true;
                }
            }
            reentrantLock.unlock();
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean removeLastOccurrence(Object obj) {
        if (obj == null) {
            return false;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            for (Node<E> node = this.last; node != null; node = node.prev) {
                if (obj.equals(node.item)) {
                    unlink(node);
                    return true;
                }
            }
            reentrantLock.unlock();
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public boolean offer(E e) {
        return offerLast(e);
    }

    public void put(E e) throws InterruptedException {
        putLast(e);
    }

    public boolean offer(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        return offerLast(e, j, timeUnit);
    }

    public E remove() {
        return removeFirst();
    }

    public E poll() {
        return pollFirst();
    }

    public E take() throws InterruptedException {
        return takeFirst();
    }

    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        return pollFirst(j, timeUnit);
    }

    public E element() {
        return getFirst();
    }

    public E peek() {
        return peekFirst();
    }

    public int remainingCapacity() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.capacity - this.count;
        } finally {
            reentrantLock.unlock();
        }
    }

    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, Integer.MAX_VALUE);
    }

    public int drainTo(Collection<? super E> collection, int i) {
        if (collection == null) {
            throw new NullPointerException();
        } else if (collection != this) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                int min = Math.min(i, this.count);
                for (int i2 = 0; i2 < min; i2++) {
                    collection.add(this.first.item);
                    unlinkFirst();
                }
                return min;
            } finally {
                reentrantLock.unlock();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void push(E e) {
        addFirst(e);
    }

    public E pop() {
        return removeFirst();
    }

    public boolean remove(Object obj) {
        return removeFirstOccurrence(obj);
    }

    public int size() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.count;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            for (Node<E> node = this.first; node != null; node = node.next) {
                if (obj.equals(node.item)) {
                    return true;
                }
            }
            reentrantLock.unlock();
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    public Object[] toArray() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] objArr = new Object[this.count];
            int i = 0;
            Node<E> node = this.first;
            while (node != null) {
                int i2 = i + 1;
                objArr[i] = node.item;
                node = node.next;
                i = i2;
            }
            return objArr;
        } finally {
            reentrantLock.unlock();
        }
    }

    public <T> T[] toArray(T[] tArr) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int length = tArr.length;
            T[] tArr2 = tArr;
            if (length < this.count) {
                tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.count);
            }
            int i = 0;
            Node<E> node = this.first;
            while (node != null) {
                tArr2[i] = node.item;
                node = node.next;
                i++;
            }
            if (tArr2.length > i) {
                tArr2[i] = null;
            }
            return tArr2;
        } finally {
            reentrantLock.unlock();
        }
    }

    public String toString() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Node<E> node = this.first;
            if (node == null) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(Operators.ARRAY_START);
            while (true) {
                E e = node.item;
                if (e == this) {
                    e = "(this Collection)";
                }
                sb.append(e);
                node = node.next;
                if (node == null) {
                    sb.append(Operators.ARRAY_END);
                    String sb2 = sb.toString();
                    reentrantLock.unlock();
                    return sb2;
                }
                sb.append(',');
                sb.append(' ');
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public void clear() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Node<E> node = this.first;
            while (node != null) {
                node.item = null;
                Node<E> node2 = node.next;
                node.prev = null;
                node.next = null;
                node = node2;
            }
            this.last = null;
            this.first = null;
            this.count = 0;
            this.notFull.signalAll();
        } finally {
            reentrantLock.unlock();
        }
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    public Iterator<E> descendingIterator() {
        return new DescendingItr();
    }

    private abstract class AbstractItr implements Iterator<E> {
        private Node<E> lastRet;
        Node<E> next;
        E nextItem;

        /* access modifiers changed from: package-private */
        public abstract Node<E> firstNode();

        /* access modifiers changed from: package-private */
        public abstract Node<E> nextNode(Node<E> node);

        AbstractItr() {
            ReentrantLock reentrantLock = LinkedBlockingDeque.this.lock;
            reentrantLock.lock();
            try {
                this.next = firstNode();
                this.nextItem = this.next == null ? null : this.next.item;
            } finally {
                reentrantLock.unlock();
            }
        }

        private Node<E> succ(Node<E> node) {
            while (true) {
                Node<E> nextNode = nextNode(node);
                if (nextNode == null) {
                    return null;
                }
                if (nextNode.item != null) {
                    return nextNode;
                }
                if (nextNode == node) {
                    return firstNode();
                }
                node = nextNode;
            }
        }

        /* access modifiers changed from: package-private */
        public void advance() {
            ReentrantLock reentrantLock = LinkedBlockingDeque.this.lock;
            reentrantLock.lock();
            try {
                this.next = succ(this.next);
                this.nextItem = this.next == null ? null : this.next.item;
            } finally {
                reentrantLock.unlock();
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public E next() {
            if (this.next != null) {
                this.lastRet = this.next;
                E e = this.nextItem;
                advance();
                return e;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            Node<E> node = this.lastRet;
            if (node != null) {
                this.lastRet = null;
                ReentrantLock reentrantLock = LinkedBlockingDeque.this.lock;
                reentrantLock.lock();
                try {
                    if (node.item != null) {
                        LinkedBlockingDeque.this.unlink(node);
                    }
                } finally {
                    reentrantLock.unlock();
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private class Itr extends AbstractItr {
        private Itr() {
            super();
        }

        /* access modifiers changed from: package-private */
        public Node<E> firstNode() {
            return LinkedBlockingDeque.this.first;
        }

        /* access modifiers changed from: package-private */
        public Node<E> nextNode(Node<E> node) {
            return node.next;
        }
    }

    private class DescendingItr extends AbstractItr {
        private DescendingItr() {
            super();
        }

        /* access modifiers changed from: package-private */
        public Node<E> firstNode() {
            return LinkedBlockingDeque.this.last;
        }

        /* access modifiers changed from: package-private */
        public Node<E> nextNode(Node<E> node) {
            return node.prev;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            objectOutputStream.defaultWriteObject();
            for (Node<E> node = this.first; node != null; node = node.next) {
                objectOutputStream.writeObject(node.item);
            }
            objectOutputStream.writeObject((Object) null);
        } finally {
            reentrantLock.unlock();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.count = 0;
        this.first = null;
        this.last = null;
        while (true) {
            Object readObject = objectInputStream.readObject();
            if (readObject != null) {
                add(readObject);
            } else {
                return;
            }
        }
    }
}
