

<h1 align="center">
  <br>
  <a href="http://www.amitmerchant.com/electron-markdownify"><img src="https://energyeducation.ca/wiki/images/2/25/Cl.png" alt="Markdownify" width="250"></a>
  <br>
  Clorine
  <br>
</h1>

<h4 align="center">A child's play LRU In-memory & disk caching library for java apps.</h4>

<p align="center">
  <img src="https://img.shields.io/badge/Version-1.0-green?style=for-the-badge">
  <img src="https://img.shields.io/github/license/ErrorxCode/Clorine?style=for-the-badge">
  <img src="https://img.shields.io/github/stars/ErrorxCode/Clorine?style=for-the-badge">
  <img src="https://img.shields.io/github/issues/ErrorxCode/Clorine?color=red&style=for-the-badge">
  <img src="https://img.shields.io/github/forks/ErrorxCode/Clorine?color=teal&style=for-the-badge">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Author-Rahil--Khan-cyan?style=flat-square">
  <img src="https://img.shields.io/badge/Open%20Source-Yes-cyan?style=flat-square">
  <img src="https://img.shields.io/badge/Written%20In-Java-cyan?style=flat-square">
</p>

<p align="center">
  <a href="#key-features">Key Features</a> •
  <a href="#Usage">How To Use</a> •
  <a href="#Implimentation">Implimentation</a> 
</p>

<img src="https://i.pinimg.com/originals/65/47/b7/6547b7cebfd77d52070ec6a0319bac84.png" alt="Markdownify" width="1000">

Clorine is a simple and easy to use caching library for java. It uses LRU (Least recently used) algorithim for saving and deleting cache. This library facilates In-memory cache along with disk cache. Means that your cache is actually sotred on the disk but a another in-memory cache is also created when you use that cache frequently to save time.

## 🎯Key Features

* Super simple, made using the most simples design ever
* Automatically manage the cache heap and memory
* Can directly cache objects (POJO's)

## 💉Implimentation 
[![](https://jitpack.io/v/ErrorxCode/Clorine.svg)](https://jitpack.io/#ErrorxCode/Clorine)
### Gradle :-
Add it in your root build.gradle at the end of repositories:

```css
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.**  Add the dependency

```css
	dependencies {
	        implementation 'com.github.ErrorxCode:Clorine:Tag'
	}
```

### Maven :-
**Step 1**. Add to project level file
```markup
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

**Step 2.**  Add the dependency

```markup
	<dependency>
	    <groupId>com.github.ErrorxCode</groupId>
	    <artifactId>Clorine</artifactId>
	    <version>Tag</version>
	</dependency>
```

## 📃Usage
The usage of this library is this much simple that it cannot be explained in words.

#### First create a cache:
```java
Cache demo = Clorine.createOrOpen(20,"demo",cacheDir);
```
20 is the limit of data/objects in the memory cache. There is no limit in disk cache.
#### Then use it like this:
```java
// To store cache, you don't need to do anything else
cache.put("name","xcode");  
cache.put("person1",new Person("xcode",5,false));  
cache.put("person2",new Person("frank",42,true));

// To get data
cache.get("name",String.class);  
cache.get("person",Person.class);
```


## 🆘Support

If you like my work then you can suppot me by giving this repo a ⭐. You can check my other repos as well, if you found this library userfull then you will definetly fine more in my profiles.

## You may also like...

- [CloremDB](https://github.com/Clorabase/CloremDB) - A realm like embadded database
- [ClorographDB](https://github.com/amitmerchant1990/correo) - CLorabase offline graph database


## Powered by 💓
#### [Clorabase](https://github.com/clorabase)
> A account less backend for android
