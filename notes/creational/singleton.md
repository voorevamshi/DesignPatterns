# Singleton Design Pattern

## Overview
The **Singleton Design Pattern** is a creational pattern that ensures a class has **only one instance** throughout the application's lifecycle and provides a **global point of access** to that instance.

## Key Characteristics
- **Single Instance**: Only one object of the class is ever created.
- **Global Access**: The single instance is accessible from anywhere in the application.
- **Private Constructor**: Prevents direct instantiation from outside the class.

## Singleton Implementations in This Project

This project demonstrates **4 approaches** to implementing the Singleton pattern, each with different trade-offs regarding thread safety and performance.

---

### 1. Eager Singleton (`EagerSingleton.java`)

The instance is created **at class loading time**, before it is ever requested.

```java
public class EagerSingleton {

    private EagerSingleton() {}

    private static final EagerSingleton instance = new EagerSingleton();

    public static EagerSingleton getInstance() {
        return instance;
    }
}
```

| | |
|---|---|
| ✅ **Thread-Safe** | The JVM guarantees class-level static fields are initialized only once. |
| ✅ **Simple** | No synchronization logic needed. |
| ❌ **No Lazy Loading** | Instance is created even if it's never used, which wastes resources. |

---

### 2. Lazy Singleton (`LazySingleton.java`)

The instance is created **only when first requested** (lazy initialization). Uses `synchronized` to ensure thread safety. Also protects against **Serialization** and **Reflection** attacks.

```java
public class LazySingleton extends MyClone implements Serializable {

    private static LazySingleton instance;

    private LazySingleton() {
        // Reflection protection
        if (instance != null) {
            throw new IllegalStateException("object can't be created using reflection");
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Clone protection
        throw new CloneNotSupportedException();
    }

    protected Object readResolve() {
        // Serialization protection
        return instance;
    }

    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            return instance = new LazySingleton();
        }
        return instance;
    }
}
```

| | |
|---|---|
| ✅ **Lazy Loading** | Instance is created only when needed. |
| ✅ **Reflection-Safe** | Throws exception if reflection tries to create a new instance. |
| ✅ **Serialization-Safe** | `readResolve()` ensures the same instance is returned after deserialization. |
| ✅ **Clone-Safe** | Overrides `clone()` to throw `CloneNotSupportedException`. |
| ❌ **Performance** | `synchronized` on `getInstance()` causes a lock on every call, which is slow under high concurrency. |

---

### 3. Double-Checked Locking Lazy Singleton (`DoubleCheckingLazySingleton.java`)

Improves on the `LazySingleton` by reducing the synchronization overhead. The `synchronized` block is only entered when the instance is `null` (i.e., only on the very first call).

```java
public class DoubleCheckingLazySingleton {

    private DoubleCheckingLazySingleton() {}

    private static volatile DoubleCheckingLazySingleton instance; // volatile for correctness

    public static DoubleCheckingLazySingleton getInstance() {
        if (instance == null) {                                      // First check (no lock)
            synchronized (DoubleCheckingLazySingleton.class) {
                if (instance == null) {                              // Second check (with lock)
                    instance = new DoubleCheckingLazySingleton();
                }
            }
        }
        return instance;
    }
}
```

#### How it Works — Step by Step
1. **Thread A** calls `getInstance()`. `instance` is `null`, so it enters the first `if` block.
2. **Thread A** acquires the lock via `synchronized`.
3. **Thread A** checks `instance` again inside the lock (second check). Still `null`, so it creates the object and assigns it.
4. **Thread A** releases the lock.
5. **Thread B** calls `getInstance()`. `instance` is now **not null**, so it skips both `if` blocks entirely — **no lock acquired, no overhead**.

#### Why Two Null Checks?
- The **first check** avoids acquiring the lock on every call after the instance is already created — this is the **performance optimization**.
- The **second check** (inside the `synchronized` block) prevents a race condition: two threads could both pass the first check simultaneously before either acquires the lock. Without the inner check, both would create a new instance.

#### Why `volatile`?
Without `volatile`, the CPU or JVM compiler may **reorder instructions** during object creation:
```
// What you write:
instance = new DoubleCheckingLazySingleton();

// What the JVM might actually do (reordered):
1. Allocate memory for the object
2. Assign reference to `instance`   ← instance is non-null but NOT yet initialized!
3. Call the constructor to initialize
```
Another thread could see `instance != null` at step 2 and use a **partially constructed object**. `volatile` forbids this reordering and ensures full visibility across threads.

| | |
|---|---|
| ✅ **Lazy Loading** | Created only when needed — no upfront resource waste. |
| ✅ **High Performance** | Lock is acquired only once ever — on the very first `getInstance()` call. |
| ✅ **Thread-Safe** | Double-check prevents race conditions between concurrent threads. |
| ✅ **Scalable** | Performs well under high-concurrency since subsequent calls are lock-free. |
| ⚠️ **Needs `volatile`** | Without `volatile`, CPU instruction reordering can expose a partially constructed object to other threads. |
| ❌ **Verbose** | More complex to read and understand than simpler alternatives. |

---

### 4. Lazy Inner Class Singleton (`LazzyInnerClassSingleton.java`)

The **most recommended** approach. Uses a private static inner class (`SingletonHelper`) to hold the instance. The inner class is only loaded when `getInstance()` is called for the first time, achieving lazy initialization without any explicit synchronization. This technique is also known as the **Initialization-on-demand Holder (IODH)** idiom.

```java
public class LazzyInnerClassSingleton {

    private LazzyInnerClassSingleton() {
        // Reflection protection
        if (SingletonHelper.instance != null) {
            throw new IllegalStateException("Singleton instance already exists. Use getInstance() instead.");
        }
    }

    private static class SingletonHelper {
        private static final LazzyInnerClassSingleton instance = new LazzyInnerClassSingleton();
    }

    public static LazzyInnerClassSingleton getInstance() {
        return SingletonHelper.instance;
    }
}
```

#### How it Works — Step by Step
1. When `LazzyInnerClassSingleton` class is loaded by the JVM, the `SingletonHelper` **inner class is NOT loaded yet**.
2. On the **first call** to `getInstance()`, the JVM loads the `SingletonHelper` class.
3. During class loading, the `static final instance` field is initialized — the object is created **exactly once**.
4. The JVM's class loading mechanism guarantees this is **atomic and thread-safe** with no extra synchronization code needed.
5. All subsequent calls to `getInstance()` simply return the already-initialized `SingletonHelper.instance`.

#### Why is it Thread-Safe Without `synchronized`?
The **Java Language Specification (JLS §12.4)** guarantees that a class is initialized **at most once** by the JVM. The class loader uses an internal lock to ensure that even if multiple threads call `getInstance()` simultaneously for the first time, only one initialization occurs. This is a **free, built-in guarantee** provided by the JVM itself.

#### Why is the Inner Class `static`?
- A **non-static** inner class holds an implicit reference to the outer class instance. Since the outer class cannot be instantiated (private constructor), this would be problematic.
- A **`static`** inner class has no such dependency — it is independent and can be loaded by the JVM on its own.

#### Why `private`?
Declaring `SingletonHelper` as `private` ensures it is **only accessible within `LazzyInnerClassSingleton`**. No external code can directly access or manipulate the holder class.

#### Reflection Protection — Same Guard as `LazySingleton`
By default, reflection can bypass the private constructor and create a second instance. We guard against this the same way as `LazySingleton` — by checking if the instance already exists inside the constructor:
```java
private LazzyInnerClassSingleton() {
    if (SingletonHelper.instance != null) {
        throw new IllegalStateException("Singleton instance already exists. Use getInstance() instead.");
    }
}
```
This works because when reflection tries to invoke the constructor to create a second object, `SingletonHelper.instance` is already non-null (initialized during the first legitimate call), so the guard fires and blocks the creation.

| | |
|---|---|
| ✅ **Lazy Loading** | `SingletonHelper` class is loaded only on the first call to `getInstance()` — zero memory waste. |
| ✅ **Thread-Safe** | Class loading is guaranteed to be atomic and thread-safe by the JVM (JLS §12.4). |
| ✅ **High Performance** | No `synchronized`, no `volatile`, no locking overhead whatsoever. |
| ✅ **Clean Code** | Simple and elegant — no complex double-check or synchronization logic. |
| ✅ **Reflection-Safe** | Constructor guard (`if (instance != null) throw`) blocks reflection from creating a second instance. |
| ❌ **Serialization** | Does not protect against serialization/deserialization by default — needs `readResolve()` if serializable. |

---

## Comparison Table

| Implementation | Lazy Loading | Thread-Safe | Performance | Complexity |
|---|---|---|---|---|
| `EagerSingleton` | ❌ No | ✅ Yes | ✅ High | ✅ Low |
| `LazySingleton` | ✅ Yes | ✅ Yes | ❌ Low (lock on every call) | 🔶 Medium |
| `DoubleCheckingLazySingleton` | ✅ Yes | ✅ Yes* | ✅ High | 🔶 Medium |
| `LazzyInnerClassSingleton` | ✅ Yes | ✅ Yes | ✅ High | ✅ Low |

> *Requires `volatile` keyword for full correctness.

---

## Threats to Singleton & How the Project Handles Them

| Threat | How to Break It | Solution Used in `LazySingleton` |
|---|---|---|
| **Reflection** | Using `getDeclaredConstructors()` to invoke the private constructor | `if (instance != null) throw new IllegalStateException(...)` inside the constructor |
| **Serialization** | Deserializing a saved object creates a new instance | Implement `readResolve()` to return the existing `instance` |
| **Cloning** | Calling `clone()` on the singleton object | Override `clone()` to throw `CloneNotSupportedException` |

---

## When to Use Singleton
- **Configuration classes**: Application-wide settings loaded once.
- **Logger instances**: A single logging channel for the whole app.
- **Database connection pool**: Shared and managed centrally.
- **Caches**: A single in-memory cache accessed globally.
- **Spring Beans**: By default, Spring manages all beans as Singletons within the application context.
